package com.cs.core.config.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedRuleEntityModel extends IModel {
  
  public static final String ID = "id";
  public static final String ADDED_RULES =   "addedRules";
  public static final String MODIFIED_RULES =   "modifiedRules";
  public static final String DELETED_RULES =   "deletedRules";


  public String getId();
  
  public void setId(String id);
  
  public List<IDataRuleEntityRule> getAddedRules();
  
  public void setAddedRules(List<IDataRuleEntityRule> addedRules);
  
  public List<IDataRuleEntityRule> getModifiedRules();
  
  public void setModifiedRules(List<IDataRuleEntityRule> modifiedRules);
  
  public List<String> getDeletedRules();
  
  public void setDeletedRules(List<String> deletedRules);
}
