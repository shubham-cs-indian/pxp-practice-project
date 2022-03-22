package com.cs.core.config.interactor.entity.template;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
    property = "baseType", visible = true)
public interface ITemplateTab extends IConfigMasterEntity {
  
  public static final String BASE_TYPE              = "baseType";
  public static final String IS_VISIBLE             = "isVisible";
  public static final String IS_SELECTED            = "isSelected";
  public static final String PROPERTY_SEQUENCE_LIST = "propertySequenceList";
  public static final String IS_STANDARD            = "isStandard";
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Boolean getIsVisible();
  
  public void setIsVisible(Boolean isVisible);
  
  public Boolean getIsSelected();
  
  public void setIsSelected(Boolean isSelected);
  
  public List<String> getPropertySequenceList();
  
  public void setPropertySequenceList(List<String> propertySequenceList);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
}
