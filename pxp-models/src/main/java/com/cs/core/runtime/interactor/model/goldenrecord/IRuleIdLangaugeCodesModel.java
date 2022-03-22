package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IRuleIdLangaugeCodesModel extends IModel {
  
  public static String RULE_ID          = "ruleId";
  public static String LANGUAGE_CODES   = "languageCodes";
  public static String ORGANIZATION_IDS = "organizationIds";
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public List<String> getOrganizationIds();
  
  public void setOrganizationIds(List<String> organizationIds);
}
