package com.cs.core.config.rulelist;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.exception.InvalidCodeException;

import javax.naming.InvalidNameException;

public class RuleListValidations extends Validations {

  public static void validateRuleList(IRuleListModel ruleListModel, boolean isCreated) throws Exception
  {
    if (isCreated) {
      if (!isCodeValid(ruleListModel.getCode())) {
        throw new InvalidCodeException();
      }
    }
    if (isEmpty(ruleListModel.getLabel())) {
      throw new InvalidNameException();
    }
  }
}
