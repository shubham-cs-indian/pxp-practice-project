package com.cs.core.runtime.interactor.model.instancetree;

public class GetDefaultTypesResponseModel implements IGetDefaultTypesResponseModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          type;
  protected String          label;
  protected String          icon             = "";
  protected String          iconKey             = "";
  protected String          code;
  protected String          natureType;
  
  @Override
  public String getNatureType()
  {
    return natureType;
  }
  
  @Override
  public void setNatureType(String natureType)
  {
    this.natureType = natureType;
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
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey= iconKey;
  }
  
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
  
}
