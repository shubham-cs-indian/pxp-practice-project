package com.cs.core.runtime.interactor.model.instancetree;

public class NewApplicableFilterModel implements INewApplicableFilterModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          type;
  protected String          code;
  protected String          label;
  protected String          propertyType;
  protected String 			icon;
  protected String 			iconKey;
  protected String      defaultUnit;
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getId()
  {
    return id;
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
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public String getPropertyType()
  {
    return propertyType;
  }
  
  @Override
  public void setPropertyType(String propertyType)
  {
    this.propertyType = propertyType;
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
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }

  @Override
  public String getDefaultUnit()
  {
    return defaultUnit;
  }

  @Override
  public void setDefaultUnit(String defaultUnit)
  {
    this.defaultUnit = defaultUnit;
  }
}
