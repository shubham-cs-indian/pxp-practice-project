package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;

public class GetConfigDetailsForComparisonRequestModel extends MulticlassificationRequestModel
    implements IGetConfigDetailsForComparisonRequestModel {
  
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
