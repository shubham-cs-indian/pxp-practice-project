package com.cs.core.config.interactor.model.iconlibrary;

import java.util.List;

import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;

public class SaveOrReplaceIconRequestModel implements ISaveOrReplaceIconRequestModel {

  private static final long serialVersionUID = 1L;
  
  protected List<IMultiPartFileInfoModel> multiPartFileInfoList;
  protected String                        iconCode;
  protected String                        iconName;
  
  @Override
  public List<IMultiPartFileInfoModel> getMultiPartFileInfoList()
  {
    return multiPartFileInfoList;
  }
  
  @Override
  public void setMultiPartFileInfoList(List<IMultiPartFileInfoModel> multiPartFileInfoList)
  {
    this.multiPartFileInfoList = multiPartFileInfoList;
  }
  
  @Override
  public String getIconCode()
  {
    return iconCode;
  }
  
  @Override
  public void setIconCode(String iconCode)
  {
    this.iconCode = iconCode;
  }
  
  @Override
  public String getIconName()
  {
    return iconName;
  }
  
  @Override
  public void setIconName(String iconName)
  {
    this.iconName = iconName;
  }
  
}
