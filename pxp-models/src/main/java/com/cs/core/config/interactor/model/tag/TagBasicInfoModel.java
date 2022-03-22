package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class TagBasicInfoModel extends ConfigEntityInformationModel implements ITagBasicInfoModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          icon;
  protected String          color;
  protected Boolean         isMultiselect;
  protected String          tagType;
  
  @Override
  public Boolean getIsMultiselect()
  {
    return isMultiselect;
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.isMultiselect = isMultiselect;
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
  public String getColor()
  {
    return color;
  }
  
  @Override
  public void setColor(String color)
  {
    this.color = color;
  }
  
  @Override
  public String getTagType()
  {
    return this.tagType;
  }
  
  @Override
  public void setTagType(String tagType)
  {
    this.tagType = tagType;
  }
  
}
