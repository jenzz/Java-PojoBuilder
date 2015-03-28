package com.jenzz.pojobuilder.processor.rules;

import com.jenzz.pojobuilder.processor.expections.RuleException;
import javax.lang.model.element.Element;

public interface Rule {

  void validate(Element element) throws RuleException;

  RuleException exception();
}