package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.klass.ITypesListModel;

public interface ITypesInfoWithRuleIdModel extends ITypesListModel {
  
  public static final String RULE_ID = "ruleId";
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
}
