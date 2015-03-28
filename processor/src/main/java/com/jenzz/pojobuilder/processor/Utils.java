package com.jenzz.pojobuilder.processor;

import com.jenzz.pojobuilder.processor.expections.UnnamedPackageException;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;

import static java.lang.Character.toLowerCase;

final class Utils {

  private Utils() {
    // no instances
  }

  static String packageName(Elements elements, Element type) throws UnnamedPackageException {
    PackageElement pkg = elements.getPackageOf(type);
    if (pkg.isUnnamed()) {
      throw new UnnamedPackageException(type);
    }
    return pkg.getQualifiedName().toString();
  }

  static String decapitalize(String text) {
    if (text == null || text.length() == 0) {
      return text;
    }
    return toLowerCase(text.charAt(0)) + text.substring(1);
  }
}