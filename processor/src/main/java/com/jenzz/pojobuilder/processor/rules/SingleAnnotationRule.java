package com.jenzz.pojobuilder.processor.rules;

import com.jenzz.pojobuilder.api.Ignore;
import com.jenzz.pojobuilder.api.Required;
import com.jenzz.pojobuilder.processor.expections.MultipleAnnotationsException;
import com.jenzz.pojobuilder.processor.expections.RuleException;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

import static javax.lang.model.util.ElementFilter.fieldsIn;

public class SingleAnnotationRule implements Rule {

  @Override
  public void validate(Element element) throws RuleException {
    for (VariableElement field : fieldsIn(element.getEnclosedElements())) {
      boolean isRequired = field.getAnnotation(Required.class) != null;
      boolean isIgnored = field.getAnnotation(Ignore.class) != null;
      if (isRequired && isIgnored) {
        throw new MultipleAnnotationsException("Field " + field.getSimpleName() +
            " cannot be required and ignored at the same time.");
      }
    }
  }
}
