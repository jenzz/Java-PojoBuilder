package com.jenzz.pojobuilder.processor;

import com.jenzz.pojobuilder.api.Ignore;
import com.jenzz.pojobuilder.api.Required;
import com.squareup.javapoet.ClassName;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.squareup.javapoet.ClassName.get;
import static javax.lang.model.util.ElementFilter.fieldsIn;

class BuilderAnnotatedClass {

  private final String packageName;
  private final TypeElement classElement;

  BuilderAnnotatedClass(String packageName, TypeElement classElement) {
    this.packageName = packageName;
    this.classElement = classElement;
  }

  String packageName() { return packageName; }

  TypeElement classElement() {
    return classElement;
  }

  String simpleName() {
    return classElement.getSimpleName().toString();
  }

  ClassName className() {
    return get(classElement);
  }

  List<Element> requiredFields() {
    List<Element> list = new ArrayList<>();
    for (Element element : fieldsIn(classElement.getEnclosedElements())) {
      boolean isRequired = element.getAnnotation(Required.class) != null;
      if (isRequired) {
        list.add(element);
      }
    }
    return list;
  }

  List<Element> optionalFields() {
    List<Element> list = new ArrayList<>();
    for (Element element : fieldsIn(classElement.getEnclosedElements())) {
      boolean isRequired = element.getAnnotation(Required.class) != null;
      boolean isIgnored = element.getAnnotation(Ignore.class) != null;
      if (!isRequired && !isIgnored) {
        list.add(element);
      }
    }
    return list;
  }
}