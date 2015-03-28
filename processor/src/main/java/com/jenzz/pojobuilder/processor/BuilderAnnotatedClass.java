package com.jenzz.pojobuilder.processor;

import com.jenzz.pojobuilder.api.Ignore;
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

  List<Element> nonIgnoredFields() {
    List<Element> list = new ArrayList<>();
    for (Element element : fieldsIn(classElement.getEnclosedElements())) {
      boolean isIgnored = element.getAnnotation(Ignore.class) != null;
      if (!isIgnored) {
        list.add(element);
      }
    }
    return list;
  }
}