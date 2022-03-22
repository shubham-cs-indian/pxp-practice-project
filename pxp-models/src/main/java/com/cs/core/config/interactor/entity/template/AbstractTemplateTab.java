package com.cs.core.config.interactor.entity.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
    property = "baseType", visible = true)
public abstract class AbstractTemplateTab implements ITemplateTab {
  
  private static final long serialVersionUID     = 1L;
  
  protected String          id;
  protected String          label;
  protected String          icon;
  protected String          iconKey;
  protected String          baseType             = this.getClass()
      .getName();
  protected Boolean         isVisible            = true;
  protected String          code;
  protected Boolean         isSelected;
  protected List<String>    propertySequenceList = new ArrayList<>();
  protected Boolean         isStandard;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return this.iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Boolean getIsVisible()
  {
    return isVisible;
  }
  
  @Override
  public void setIsVisible(Boolean isVisible)
  {
    this.isVisible = isVisible;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return this.isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  /**
   * ******************** ignored properties *********************
   */
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @JsonIgnore
  @Override
  public String getType()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setType(String type)
  {
  }
  
  public Boolean getIsSelected()
  {
    if (isSelected == null) {
      isSelected = false;
    }
    return isSelected;
  }
  
  public void setIsSelected(Boolean isSelected)
  {
    this.isSelected = isSelected;
  }
  
  @Override
  public List<String> getPropertySequenceList()
  {
    return propertySequenceList;
  }
  
  @Override
  public void setPropertySequenceList(List<String> propertySequenceList)
  {
    this.propertySequenceList = propertySequenceList;
  }
}
