package com.cs.core.runtime.interactor.model.dynamichierarchy;

public class IdLabelNatureTypeModel implements IIdLabelNatureTypeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          label;
  protected Boolean         isNature = false;
  protected String          code;
  protected String          type;
  protected String          icon;
  protected String          iconKey;
  protected Long            iid;

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
  public Boolean getIsNature()
  {
    return isNature;
  }
  
  @Override
  public void setIsNature(Boolean isNature)
  {
    this.isNature = isNature;
  }
 
  @Override
  public String getType() {
	return type;
 }
  
  @Override
  public void setType(String type) {
	this.type = type;
  }

  @Override 
  public void setIcon(String icon)
  {
    this.icon = icon;
  }

  @Override
  public String getIcon()
  {
    return icon;
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
  public Long getIid()
  {
    return iid;
  }

  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
