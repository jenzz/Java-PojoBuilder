package com.jenzz.pojobuilder.processor;

import com.jenzz.pojobuilder.processor.expections.UnnamedPackageException;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import java.util.List;
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

  static CodeBlock newObjectWithParams(ClassName objectName, List<Element> params) {
    return newObjectWithParams(objectName, params, "");
  }

  static CodeBlock newObjectWithParams(ClassName objectName, List<Element> params, String paramsPrefix) {
    CodeBlock.Builder builder = CodeBlock.builder()
        .add("new $T(", objectName);

    int len = params.size();
    for (int i = 0; i < len; i++) {
      Element field = params.get(i);
      String fieldName = field.getSimpleName().toString();
      builder.add(paramsPrefix + fieldName);
      if (i < len -1) {
        builder.add(", ");
      }
    }

    return builder.add(")").build();
  }
}