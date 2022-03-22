package com.cs.core.config.interactor.model.mapping;

import java.util.List;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IConfigCloneEntityInformationModel extends IConfigEntityInformationModel {
  
  public static final String ORIGINAL_ENTITY_ID     = "originalEntityId";
  public static final String IS_FROM_TEMPLATE       = "isFromTemplate";
  public static final String TIMER_DEFINITION_TYPE  = "timerDefinitionType";
  public static final String TIMER_START_EXPRESSION = "timerStartExpression";
  public static final String PHYSICAL_CATALOG_IDS   = "physicalCatalogIds";
  
  public String getOriginalEntityId();
  
  public void setOriginalEntityId(String originalEntityId);
  
  public Boolean getIsFromTemplate();
 
  public void setIsFromTemplate(Boolean isFromTemplate);
  
  public String getTimerDefinitionType();
  
  public void setTimerDefinitionType(String timerDefinitionType);
  
  public String getTimerStartExpression();
  
  public void setTimerStartExpression(String timerStartExpression);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
}
