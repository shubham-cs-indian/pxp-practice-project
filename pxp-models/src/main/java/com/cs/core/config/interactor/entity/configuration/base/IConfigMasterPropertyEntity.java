package com.cs.core.config.interactor.entity.configuration.base;

public interface IConfigMasterPropertyEntity extends IConfigMasterEntity {
  
  public static final String DESCRIPTION  = "description";
  public static final String TOOLTIP      = "tooltip";
  public static final String IS_MANDATORY = "isMandatory";
  public static final String IS_STANDARD  = "isStandard";
  public static final String PLACEHOLDER  = "placeholder";
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public String getTooltip();
  
  public void setTooltip(String tooltip);
  
  public Boolean getIsMandatory();
  
  public void setIsMandatory(Boolean isMandatory);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public String getPlaceholder();
  
  public void setPlaceholder(String placeholder);
}
