package com.cs.core.config.interactor.entity.standard.attribute;

import com.cs.config.standard.IStandardConfig;

public class CreatedOnAttribute extends AbstractStandardAttribute implements ICreatedOnAttribute {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id               = IStandardConfig.StandardProperty.createdonattribute
      .toString();
  
  protected String          label            = "Created On";
  
  protected String          rendererType     = Renderer.DATE.name();
  
  protected Boolean         isStandard       = true;
  
  protected Boolean         isDisabled       = true;
  
  public CreatedOnAttribute()
  {
    this.isSortable = true;
    this.isFilterable = true;
    this.isSearchable = true;
  }
  
  public CreatedOnAttribute(String label)
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
  
  @Override
  public Boolean getIsDisabled()
  {
    return isDisabled;
  }
  
  @Override
  public void setIsDisabled(Boolean isDisabled)
  {
    this.isDisabled = isDisabled;
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
}
