package com.jenzz.pojobuilder.processor.rules;

import com.jenzz.pojobuilder.processor.expections.AbstractClassException;
import com.jenzz.pojobuilder.processor.expections.RuleException;
import javax.lang.model.element.Element;

import static com.jenzz.pojobuilder.processor.BuilderProcessor.ANNOTATION;
import static javax.lang.model.element.Modifier.ABSTRACT;

public class NonAbstractClassRule implements Rule {

  @Override
  public void validate(Element element) throws RuleException {
    if (element.getModifiers().contains(ABSTRACT)) {
      throw new AbstractClassException("Classes annotated with " + ANNOTATION + " must not be abstract.");
    }
  }
}
