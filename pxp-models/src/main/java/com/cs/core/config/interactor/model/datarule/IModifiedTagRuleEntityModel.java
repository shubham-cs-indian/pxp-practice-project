package com.cs.core.config.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedTagRuleEntityModel extends IModel {
  
  public static final String ID             = "id";
  public static final String ADDED_RULES    = "addedRules";
  public static final String MODIFIED_RULES = "modifiedRules";
  public static final String DELETED_RULES  = "deletedRules";
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getDeletedRules();
  
  public void setDeletedRules(List<String> deletedRules);
  
  public List<IDataRuleTagRule> getAddedRules();
  
  public void setAddedRules(List<IDataRuleTagRule> addedRules);
  
  public List<IDataRuleTagRule> getModifiedRules();
  
  public void setModifiedRules(List<IDataRuleTagRule> modifiedRules);
}
