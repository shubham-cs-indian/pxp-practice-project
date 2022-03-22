package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;

import java.util.List;

public interface IGoldenRecordTypeInfoModel extends IKlassInstanceTypeModel {
  
  public static final String ADDED_TYPES      = "addedTypes";
  public static final String ADDED_TAXONOMIES = "addedTaxonomies";
  public static final String RULE_ID          = "ruleId";
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
  
  public List<String> getAddedTypes();
  
  public void setAddedTypes(List<String> addedTypes);
  
  public List<String> getAddedTaxonomies();
  
  public void setAddedTaxonomies(List<String> addedTaxonomies);
}
