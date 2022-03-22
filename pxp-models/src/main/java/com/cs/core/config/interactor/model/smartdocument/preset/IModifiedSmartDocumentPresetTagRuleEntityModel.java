package com.cs.core.config.interactor.model.smartdocument.preset;

import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetTagRule;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedSmartDocumentPresetTagRuleEntityModel extends IModel {
  
  public static final String ID             = "id";
  public static final String ADDED_RULES    = "addedRules";
  public static final String MODIFIED_RULES = "modifiedRules";
  public static final String DELETED_RULES  = "deletedRules";
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getDeletedRules();
  
  public void setDeletedRules(List<String> deletedRules);
  
  public List<ISmartDocumentPresetTagRule> getAddedRules();
  
  public void setAddedRules(List<ISmartDocumentPresetTagRule> addedRules);
  
  public List<ISmartDocumentPresetTagRule> getModifiedRules();
  
  public void setModifiedRules(List<ISmartDocumentPresetTagRule> modifiedRules);
}
