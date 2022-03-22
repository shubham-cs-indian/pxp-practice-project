package com.cs.core.config.interactor.model.processevent;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class ConfigTagInformationModel extends ConfigEntityInformationModel
    implements IConfigTagInformationModel {
  
  private static final long serialVersionUID = 1L;
  
  private String            tagType;
  protected Boolean         isMultiselect    = false;
  
  @Override
  public String getTagType()
  {
    return tagType;
  }
  
  @Override
  public void setTagType(String tagType)
  {
    this.tagType = tagType;
  }
  
  @Override
  public Boolean getIsMultiselect()
  {
    return this.isMultiselect;
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.isMultiselect = isMultiselect;
  }
  
}
