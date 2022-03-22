package com.cs.core.config.interactor.model.iconlibrary;

import java.util.List;

import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface ISaveOrReplaceIconRequestModel extends IModel {
  
  public static final String MULTIPART_FILE_INFO_LIST = "multiPartFileInfoList";
  public static final String ICON_CODE                = "iconCode";
  public static final String ICON_NAME                = "iconName";
  
  
  public List<IMultiPartFileInfoModel> getMultiPartFileInfoList();
  public void setMultiPartFileInfoList(List<IMultiPartFileInfoModel> multiPartFileInfoList);
  
  public String getIconCode();
  public void setIconCode(String iconCode);
  
  public String getIconName();
  public void setIconName(String iconName);
  
}
