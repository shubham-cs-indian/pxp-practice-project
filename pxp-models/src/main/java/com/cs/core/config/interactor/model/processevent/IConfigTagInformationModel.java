package com.cs.core.config.interactor.model.processevent;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IConfigTagInformationModel extends IConfigEntityInformationModel {
  
  public static final String TAG_TYPE = "tagType";  
  public static final String IS_MULTI_SELECT = ITag.IS_MULTI_SELECT;
  
  public String getTagType();
  
  public void setTagType(String tagType);
  
  public Boolean getIsMultiselect();
  
  public void setIsMultiselect(Boolean isMultiselect);

}
