package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IPropertyCollectionBasicInfoModel extends IConfigEntityInformationModel {
  
  public static final String IS_FOR_XRAY = "isForXRay";
  
  public Boolean getIsForXRay();
  
  public void setIsForXRay(Boolean isForXRay);
}
