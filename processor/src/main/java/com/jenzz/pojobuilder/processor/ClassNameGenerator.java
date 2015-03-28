package com.jenzz.pojobuilder.processor;

import com.squareup.javapoet.ClassName;
import javax.lang.model.element.Element;

import static com.squareup.javapoet.ClassName.get;

class ClassNameGenerator {

  public static final String CLASS_SUFFIX = "Builder";

  private final String packageName;

  ClassNameGenerator(String packageName) {
    this.packageName = packageName;
  }

  String generateBuilderClassString(BuilderAnnotatedClass clazz) {
    return generateBuilderClassString(clazz.simpleName());
  }

  String generateBuilderClassString(Element clazz) {
    return generateBuilderClassString(clazz.getSimpleName().toString());
  }

  String generateBuilderClassString(String clazz) {
    return clazz + CLASS_SUFFIX;
  }

  ClassName generateBuilderClassName(BuilderAnnotatedClass clazz) {
    return generateBuilderClassName(clazz.simpleName());
  }

  ClassName generateBuilderClassName(Element clazz) {
    return generateBuilderClassName(clazz.getSimpleName().toString());
  }

  ClassName generateBuilderClassName(String clazz) {
    return get(packageName, generateBuilderClassString(clazz));
  }
}
