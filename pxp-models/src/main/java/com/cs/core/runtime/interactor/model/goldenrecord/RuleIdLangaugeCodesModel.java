package com.cs.core.runtime.interactor.model.goldenrecord;

import java.util.ArrayList;
import java.util.List;

public class RuleIdLangaugeCodesModel implements IRuleIdLangaugeCodesModel {
  
  private static final long serialVersionUID = 1L;
  protected String          ruleId;
  protected List<String>    languageCodes;
  protected List<String>    organizationIds;
  
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
  
  @Override
  public List<String> getLanguageCodes()
  {
    if (languageCodes == null) {
      languageCodes = new ArrayList<>();
    }
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  @Override
  public List<String> getOrganizationIds()
  {
    return organizationIds;
  }
  
  @Override
  public void setOrganizationIds(List<String> organizationIds)
  {
    this.organizationIds = organizationIds;
  }
}
