package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.klass.TypesListModel;

public class TypesInfoWithRuleIdModel extends TypesListModel implements ITypesInfoWithRuleIdModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          ruleId;
  
  @Override
  public String getRuleId()
  {
    return ruleId;
  }
  
  @Override
  public void setRuleId(String ruleId)
  {
    this.ruleId = ruleId;
  }
}
