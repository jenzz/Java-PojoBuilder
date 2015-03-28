package com.jenzz.pojobuilder.processor;

import com.jenzz.pojobuilder.api.Builder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import static com.jenzz.pojobuilder.processor.Utils.decapitalize;
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
        .addModifiers(PUBLIC, FINAL)
        .addMethod(constructor())
        .addMethod(fromConstructor());

    for (Element field : builderAnnotatedClass.nonIgnoredFields()) {
      builder.addField(field(field));
      builder.addMethod(method(field));
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

  private MethodSpec constructor() {
    return methodBuilder(decapitalize(builderAnnotatedClass.simpleName()))
        .addModifiers(PUBLIC, STATIC)
        .returns(generatedClassName)
        .addStatement("return new $T()", generatedClassName)
        .build();
  }

  private MethodSpec fromConstructor() {
    MethodSpec.Builder builder =
        methodBuilder(decapitalize(builderAnnotatedClass.simpleName()))
            .addModifiers(PUBLIC, STATIC)
            .returns(generatedClassName)
            .addParameter(builderAnnotatedClass.className(), "from")
            .addStatement("$T builder = new $T()", generatedClassName, generatedClassName);

    for (Element field : builderAnnotatedClass.nonIgnoredFields()) {
      String fieldName = field.getSimpleName().toString();
      builder.addStatement("builder.$L = from.$L", fieldName, fieldName);
    }

    return builder.addStatement("return builder").build();
  }

  private MethodSpec build() {
    String instanceName = decapitalize(builderAnnotatedClass.simpleName());
    MethodSpec.Builder builder = methodBuilder("build")
        .addModifiers(PUBLIC)
        .returns(builderAnnotatedClass.className())
        .addStatement("$T $L = new $T()", builderAnnotatedClass.classElement(),
            instanceName, builderAnnotatedClass.classElement());

    for (Element field : builderAnnotatedClass.nonIgnoredFields()) {
      String fieldName = field.getSimpleName().toString();
      builder.addStatement("$L.$L = $L", instanceName, fieldName, fieldName);
    }

    return builder.addStatement("return $L", instanceName).build();
  }

  private FieldSpec field(Element field) {
    return FieldSpec.builder(get(field.asType()), field.getSimpleName().toString())
        .addModifiers(PRIVATE)
        .build();
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