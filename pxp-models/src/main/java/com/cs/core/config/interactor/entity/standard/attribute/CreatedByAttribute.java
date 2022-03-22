package com.cs.core.config.interactor.entity.standard.attribute;

import com.cs.config.standard.IStandardConfig;

public class CreatedByAttribute extends AbstractStandardAttribute implements ICreatedByAttribute {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id               = IStandardConfig.StandardProperty.createdbyattribute
      .toString();
  
  protected String          label            = "Created By";
  
  protected Boolean         isStandard       = true;
  
  protected Boolean         isDisabled       = true;
  
  protected String          rendererType     = Renderer.TEXT.name();
  
  public CreatedByAttribute()
  {
  }
  
  public CreatedByAttribute(String label)
  {
    this.label = label;
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
}
