package com.jenzz.pojobuilder.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;
import static javax.tools.Diagnostic.Kind.WARNING;

final class Messager {

  private static javax.annotation.processing.Messager messager;

  static void init(ProcessingEnvironment processingEnvironment) {
    messager = processingEnvironment.getMessager();
  }

  static void note(Element e, String msg, Object... args) {
    checkInitialized();
    messager.printMessage(NOTE, String.format(msg, args), e);
  }

  static void warn(Element e, String msg, Object... args) {
    checkInitialized();
    messager.printMessage(WARNING, String.format(msg, args), e);
  }

  static void error(Element e, String msg, Object... args) {
    checkInitialized();
    messager.printMessage(ERROR, String.format(msg, args), e);
  }

  private static void checkInitialized() {
    if (messager == null) {
      throw new IllegalStateException("Messager not ready. Have you called init()?");
    }
  }
}