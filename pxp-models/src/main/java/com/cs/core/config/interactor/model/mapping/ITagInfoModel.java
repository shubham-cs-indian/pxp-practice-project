package com.cs.core.config.interactor.model.mapping;

import java.util.List;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface ITagInfoModel extends IConfigEntityInformationModel {
  
  public static final String CHILD_TAG = "childTag";
  
  public List<IConfigEntityInformationModel> getChildTag();
  
  public void setChildTag(List<IConfigEntityInformationModel> childTag);
  
}
