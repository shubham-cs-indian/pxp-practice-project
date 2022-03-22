package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;

public class GetEntityModel implements IGetEntityModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          label;
  protected String          type;
  protected String          icon;
  
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
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
}
