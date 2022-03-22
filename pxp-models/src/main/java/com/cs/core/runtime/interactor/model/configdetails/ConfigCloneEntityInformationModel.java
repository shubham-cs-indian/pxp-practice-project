package com.cs.core.runtime.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;

public class ConfigCloneEntityInformationModel extends ConfigEntityInformationModel
    implements IConfigCloneEntityInformationModel {
  
  private static final long serialVersionUID   = 1L;
  private String            originalEntityId;
  private Boolean           isFromTemplate;
  private String            timerDefinitionType;
  private String            timerStartExpression;
  protected List<String>    physicalCatalogIds = new ArrayList<String>();
  
  @Override
  public String getOriginalEntityId()
  {
    return originalEntityId;
  }
  
  @Override
  public void setOriginalEntityId(String originalEntityId)
  {
    this.originalEntityId = originalEntityId;
  }
  
  @Override
  public Boolean getIsFromTemplate()
  {
    return isFromTemplate;
  }
  
  @Override
  public void setIsFromTemplate(Boolean isFromTemplate)
  {
    this.isFromTemplate = isFromTemplate;
  }
  
  @Override
  public String getTimerDefinitionType()
  {
    return timerDefinitionType;
  }
  
  @Override
  public void setTimerDefinitionType(String timerDefinitionType)
  {
    this.timerDefinitionType = timerDefinitionType;
  }
  
  @Override
  public String getTimerStartExpression()
  {
    return timerStartExpression;
  }
  
  @Override
  public void setTimerStartExpression(String timerStartExpression)
  {
    this.timerStartExpression = timerStartExpression;
  }

  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return physicalCatalogIds;
  }

  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
  
}
