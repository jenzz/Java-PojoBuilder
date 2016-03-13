package com.jenzz.pojobuilder.processor;

import com.jenzz.pojobuilder.api.Builder;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import static com.jenzz.pojobuilder.processor.Utils.decapitalize;
import static com.jenzz.pojobuilder.processor.Utils.newObjectWithParams;
import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeName.get;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.type.TypeKind.DECLARED;

@SuppressWarnings("ConstantConditions")
final class CodeGenerator {

  private final BuilderAnnotatedClass builderAnnotatedClass;
  private final ClassNameGenerator classNameGenerator;
  private final ClassName generatedClassName;

  CodeGenerator(BuilderAnnotatedClass builderAnnotatedClass,
      ClassNameGenerator classNameGenerator) {
    this.builderAnnotatedClass = builderAnnotatedClass;
    this.classNameGenerator = classNameGenerator;
    this.generatedClassName = classNameGenerator.generateBuilderClassName(builderAnnotatedClass);
  }

  TypeSpec brewJava() {
    String className = classNameGenerator.generateBuilderClassString(builderAnnotatedClass);
    TypeSpec.Builder builder = classBuilder(className)
        .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
            .addMember("value", "$S", "unused")
            .build())
        .addModifiers(PUBLIC, FINAL)
        .addMethod(privateConstructor())
        .addMethod(publicConstructor());

    List<Element> optionalFields = builderAnnotatedClass.optionalFields();
    if (!optionalFields.isEmpty()) {
      builder.addMethod(fromConstructor());
    }

    for (Element field : builderAnnotatedClass.requiredFields()) {
      builder.addField(field(field, true));
    }

    for (Element field : optionalFields) {
      builder.addField(field(field, false))
          .addMethod(method(field));
      if (hasBuilderAnnotation(field)) {
        builder.addMethod(builderMethod(field));
      }
    }

    return builder.addMethod(build()).build();
  }

  private boolean hasBuilderAnnotation(Element field) {
    boolean hasBuilderAnnotation = false;
    TypeMirror fieldType = field.asType();
    if (fieldType.getKind() == DECLARED) {
      Element clazz = ((DeclaredType) fieldType).asElement();
      hasBuilderAnnotation = clazz.getAnnotation(Builder.class) != null;
    }
    return hasBuilderAnnotation;
  }

  private MethodSpec privateConstructor() {
    MethodSpec.Builder builder = constructorBuilder()
        .addModifiers(PRIVATE);

    for (Element field : builderAnnotatedClass.requiredFields()) {
      String fieldName = field.getSimpleName().toString();
      builder.addParameter(get(field.asType()), fieldName)
          .addStatement("this.$L = $L", fieldName, fieldName);
    }

    return builder.build();
  }

  private MethodSpec publicConstructor() {
    MethodSpec.Builder builder = methodBuilder(decapitalize(builderAnnotatedClass.simpleName()))
            .addModifiers(PUBLIC, STATIC)
            .returns(generatedClassName);

    List<Element> requiredFields = builderAnnotatedClass.requiredFields();
    for(Element field : requiredFields) {
      String fieldName = field.getSimpleName().toString();
      builder.addParameter(get(field.asType()), fieldName);
    }

    return builder.addCode("return ")
        .addCode(newObjectWithParams(generatedClassName, requiredFields))
        .addCode(";\n")
        .build();
  }

  private MethodSpec fromConstructor() {
    String paramName = "from";
    MethodSpec.Builder builder = methodBuilder(decapitalize(builderAnnotatedClass.simpleName()))
        .addModifiers(PUBLIC, STATIC)
        .returns(generatedClassName)
        .addParameter(builderAnnotatedClass.className(), paramName)
        .addCode("return ")
        .addCode(newObjectWithParams(generatedClassName, builderAnnotatedClass.requiredFields(), paramName + "."));

    for (Element field : builderAnnotatedClass.optionalFields()) {
      String fieldName = field.getSimpleName().toString();
      builder.addCode("\n\t.$L(from.$L)", fieldName, fieldName);
    }

    return builder.addCode(";\n").build();
  }

  private MethodSpec build() {
    String instanceName = decapitalize(builderAnnotatedClass.simpleName());
    MethodSpec.Builder builder = methodBuilder("build")
        .addModifiers(PUBLIC)
        .returns(builderAnnotatedClass.className());

    List<Element> optionalFields = builderAnnotatedClass.optionalFields();
    if (optionalFields.isEmpty()) {
      return builder.addStatement("return new $T()", builderAnnotatedClass.classElement()).build();
    }

    builder.addStatement("$T $L = new $T()", builderAnnotatedClass.classElement(),
        instanceName, builderAnnotatedClass.classElement());

    for (Element field : optionalFields) {
      String fieldName = field.getSimpleName().toString();
      builder.addStatement("$L.$L = $L", instanceName, fieldName, fieldName);
    }

    return builder.addStatement("return $L", instanceName).build();
  }

  private FieldSpec field(Element field, boolean isFinal) {
    FieldSpec.Builder builder = FieldSpec.builder(get(field.asType()), field.getSimpleName().toString())
        .addModifiers(PRIVATE);

    if (isFinal) {
      builder.addModifiers(FINAL);
    }

    return builder.build();
  }

  private MethodSpec method(Element field) {
    String methodName = field.getSimpleName().toString();
    return methodBuilder(methodName)
        .addModifiers(PUBLIC)
        .returns(generatedClassName)
        .addParameter(get(field.asType()), methodName)
        .addStatement("this.$L = $L", methodName, methodName)
        .addStatement("return this")
        .build();
  }

  private MethodSpec builderMethod(Element field) {
    String methodName = field.getSimpleName().toString();
    Element clazz = ((DeclaredType) field.asType()).asElement();
    String clazzString = classNameGenerator.generateBuilderClassString(clazz);
    ClassName clazzName = classNameGenerator.generateBuilderClassName(clazz);
    return methodBuilder(methodName)
        .addModifiers(PUBLIC)
        .returns(generatedClassName)
        .addParameter(clazzName, decapitalize(clazzString))
        .addStatement("this.$L = $L.build()", methodName, decapitalize(clazzString))
        .addStatement("return this")
        .build();
  }
}