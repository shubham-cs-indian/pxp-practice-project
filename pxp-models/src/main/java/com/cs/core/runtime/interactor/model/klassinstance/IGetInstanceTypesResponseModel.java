package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetInstanceTypesResponseModel extends IModel {
  
  public static final String INSTANCES_TYPES = "instancesTypes";
  
  // key is content id..
  public Map<String, IContentTypeIdsInfoModel> getInstancesTypes();
  
  public void setInstancesTypes(Map<String, IContentTypeIdsInfoModel> instancesTypes);
}
