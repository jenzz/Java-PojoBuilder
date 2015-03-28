package com.jenzz.pojobuilder.processor.expections;

import javax.lang.model.element.Element;

public class UnnamedPackageException extends Exception {

  public UnnamedPackageException(Element element) {
    super("The package of " + element.getSimpleName() + " is unnamed");
  }
}