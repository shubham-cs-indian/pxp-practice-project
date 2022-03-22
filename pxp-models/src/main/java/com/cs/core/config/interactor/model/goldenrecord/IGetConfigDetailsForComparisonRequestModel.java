package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;

public interface IGetConfigDetailsForComparisonRequestModel
    extends IMulticlassificationRequestModel {
  
  public static final String RULE_ID = "ruleId";
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
}
