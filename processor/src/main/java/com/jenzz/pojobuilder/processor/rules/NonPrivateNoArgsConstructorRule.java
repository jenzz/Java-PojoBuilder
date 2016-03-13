package com.jenzz.pojobuilder.processor.rules;

import com.jenzz.pojobuilder.processor.expections.MissingNoArgsConstructorException;
import com.jenzz.pojobuilder.processor.expections.RuleException;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import static com.jenzz.pojobuilder.processor.BuilderProcessor.ANNOTATION;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.util.ElementFilter.constructorsIn;

public class NonPrivateNoArgsConstructorRule implements Rule {

  @Override
  public void validate(Element element) throws RuleException {
    for (ExecutableElement constructor : constructorsIn(element.getEnclosedElements())) {
      if (constructor.getParameters().isEmpty() && !constructor.getModifiers().contains(PRIVATE)) {
        return;
      }
    }
    throw new MissingNoArgsConstructorException("Class annotated with " + ANNOTATION +
        " must have a non-private no args constructor.");
  }
}
