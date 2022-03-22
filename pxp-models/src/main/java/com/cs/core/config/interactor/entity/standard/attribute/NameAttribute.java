package com.cs.core.config.interactor.entity.standard.attribute;

import com.cs.config.standard.IStandardConfig;


public class NameAttribute extends AbstractStandardAttribute implements INameAttribute {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id               = IStandardConfig.StandardProperty.nameattribute
      .toString();
  protected String          label            = "Name";
  protected Boolean         isStandard       = true;
  protected String          rendererType     = Renderer.TEXT.name();
  protected Boolean         isGridEditable   = true;
  
  public NameAttribute()
  {
    this.isSortable = true;
    this.isFilterable = true;
    this.isSearchable = true;
    this.isTranslatable = true;
  }
  
  public NameAttribute(String label)
  {
    this.label = label;
    this.isSortable = true;
    this.isFilterable = true;
    this.isSearchable = true;
    this.isTranslatable = true;
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
    return rendererType;
  }
  
  @Override
  public Boolean getIsGridEditable()
  {
    return isGridEditable;
  }
  
  @Override
  public void setIsGridEditable(Boolean isGridEditable)
  {
    this.isGridEditable = isGridEditable;
  }
}
