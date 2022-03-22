package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface ITagBasicInfoModel extends IConfigEntityInformationModel {
  
  public static final String ICON           = "icon";
  public static final String COLOR          = "color";
  public static final String IS_MULTISELECT = "isMultiselect";
  public static final String TAG_TYPE       = ITag.TAG_TYPE;
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public String getColor();
  
  public void setColor(String color);
  
  public Boolean getIsMultiselect();
  
  public void setIsMultiselect(Boolean isMultiselect);
  
  public String getTagType();
  
  public void setTagType(String tagType);
  
}
