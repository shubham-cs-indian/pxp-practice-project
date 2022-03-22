package com.cs.core.runtime.interactor.model.propertycollection;

public class PropertyCollectionEntityInformationModel
    implements IPropertyCollectionEntityInformationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          label;
  protected String          icon;
  protected String          iconKey;
  protected Boolean         isStandard;
  protected Boolean         isForXRay        = false;
  protected Boolean         isDefaultForXRay = false;
  protected String          code;
  
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
  public Boolean getIsForXRay()
  {
    return isForXRay;
  }
  
  @Override
  public void setIsForXRay(Boolean isForXRay)
  {
    this.isForXRay = isForXRay;
  }
  
  @Override
  public Boolean getIsDefaultForXRay()
  {
    return isDefaultForXRay;
  }
  
  @Override
  public void setIsDefaultForXRay(Boolean isDefaultForXRay)
  {
    this.isDefaultForXRay = isDefaultForXRay;
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
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
}
