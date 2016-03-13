package com.jenzz.pojobuilder.processor.rules;

import com.jenzz.pojobuilder.api.Ignore;
import com.jenzz.pojobuilder.processor.expections.PrivateFieldException;
import com.jenzz.pojobuilder.processor.expections.RuleException;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

import static com.jenzz.pojobuilder.processor.BuilderProcessor.ANNOTATION;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.util.ElementFilter.fieldsIn;

public class NonPrivateFieldsRule implements Rule {

  @Override
  public void validate(Element element) throws RuleException {
    for (VariableElement field : fieldsIn(element.getEnclosedElements())) {
      boolean isPrivate = field.getModifiers().contains(PRIVATE);
      boolean isIgnored = field.getAnnotation(Ignore.class) != null;
      if (isPrivate && !isIgnored) {
        throw new PrivateFieldException("Class annotated with " + ANNOTATION +
            " has private field " + field.getSimpleName() + "." +
            " Add @Ignore annotation or make field at least package-private.");
      }
    }
  }
}
