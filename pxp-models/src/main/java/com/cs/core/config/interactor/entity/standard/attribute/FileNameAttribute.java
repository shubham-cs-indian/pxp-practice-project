package com.cs.core.config.interactor.entity.standard.attribute;

import com.cs.config.standard.IStandardConfig;

public class FileNameAttribute extends AbstractStandardAttribute implements IFileNameAttribute {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id               = IStandardConfig.StandardProperty.firstnameattribute
      .toString();
  
  protected String          label            = "File Name";
  
  protected String          rendererType     = Renderer.TEXT.name();
  
  public FileNameAttribute()
  {
  }
  
  public FileNameAttribute(String label)
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
  
  @Override
  public String getRendererType()
  {
    return Renderer.TEXT.name();
  }
}
