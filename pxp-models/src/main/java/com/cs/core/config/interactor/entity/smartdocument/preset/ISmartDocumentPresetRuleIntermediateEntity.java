package com.cs.core.config.interactor.entity.smartdocument.preset;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.List;

public interface ISmartDocumentPresetRuleIntermediateEntity extends IConfigEntity {
  
  public static final String ENTITY_ID = "entityId";
  public static final String RULES     = "rules";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public List<ISmartDocumentPresetEntityRule> getRules();
  
  public void setRules(List<ISmartDocumentPresetEntityRule> rules);
}
