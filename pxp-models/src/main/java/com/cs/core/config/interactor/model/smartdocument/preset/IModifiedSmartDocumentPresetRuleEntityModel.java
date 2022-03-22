package com.cs.core.config.interactor.model.smartdocument.preset;

import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetEntityRule;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedSmartDocumentPresetRuleEntityModel extends IModel {
  
  public static final String ID             = "id";
  public static final String ADDED_RULES    = "addedRules";
  public static final String MODIFIED_RULES = "modifiedRules";
  public static final String DELETED_RULES  = "deletedRules";
  
  public String getId();
  
  public void setId(String id);
  
  public List<ISmartDocumentPresetEntityRule> getAddedRules();
  
  public void setAddedRules(List<ISmartDocumentPresetEntityRule> addedRules);
  
  public List<ISmartDocumentPresetEntityRule> getModifiedRules();
  
  public void setModifiedRules(List<ISmartDocumentPresetEntityRule> modifiedRules);
  
  public List<String> getDeletedRules();
  
  public void setDeletedRules(List<String> deletedRules);
}
