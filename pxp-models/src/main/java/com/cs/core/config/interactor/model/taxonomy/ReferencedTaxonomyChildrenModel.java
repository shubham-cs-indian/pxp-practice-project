package com.cs.core.config.interactor.model.taxonomy;

public class ReferencedTaxonomyChildrenModel implements IReferencedTaxonomyChildrenModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          code;
  protected String          label;
  protected String          icon             = "";
  protected String          iconKey          = "";
  protected String          taxonomyType;
  protected String          baseType;
  
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
  public String getTaxonomyType()
  {
    return taxonomyType;
  }
  
  @Override
  public void setTaxonomyType(String taxonomyType)
  {
    this.taxonomyType = taxonomyType;
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
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
}
