package com.cs.core.config.interactor.entity.standard.attribute;

import com.cs.config.standard.IStandardConfig;

public class LastModifiedAttribute extends AbstractStandardAttribute
    implements ILastModifiedAttribute {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id               = IStandardConfig.StandardProperty.lastmodifiedattribute
      .toString();
  
  protected String          label            = "Last Modified";
  
  protected Boolean         isStandard       = true;
  
  protected Boolean         isDisabled       = true;
  
  protected String          rendererType     = Renderer.DATE.name();
  
  public LastModifiedAttribute()
  {
    this.isSortable = true;
    this.isFilterable = true;
    this.isSearchable = true;
  }
  
  public LastModifiedAttribute(String label)
  {
    this.label = label;
    this.isSortable = true;
    this.isFilterable = true;
    this.isSearchable = true;
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
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  public Boolean getIsDisabled()
  {
    return isDisabled;
  }
  
  public void setIsDisabled(Boolean isDisabled)
  {
    this.isDisabled = isDisabled;
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
  
  @Override
  public Boolean getIsFilterable()
  {
    return isFilterable;
  }
  
  @Override
  public void setIsFilterable(Boolean isFilterable)
  {
    this.isFilterable = isFilterable;
  }
  
  @Override
  public Boolean getIsSortable()
  {
    return isSortable;
  }
  
  @Override
  public void setIsSortable(Boolean isSortable)
  {
    this.isSortable = isSortable;
  }
  
  @Override
  public Boolean getIsSearchable()
  {
    return isSearchable;
  }
  
  @Override
  public void setIsSearchable(Boolean isSearchable)
  {
    this.isSearchable = isSearchable;
  }
}
