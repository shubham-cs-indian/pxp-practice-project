package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.klass.ContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetInstanceTypesResponseModel implements IGetInstanceTypesResponseModel {
  
  private static final long                       serialVersionUID = 1L;
  protected Map<String, IContentTypeIdsInfoModel> instancesTypes;
  
  @Override
  public Map<String, IContentTypeIdsInfoModel> getInstancesTypes()
  {
    return instancesTypes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContentTypeIdsInfoModel.class)
  public void setInstancesTypes(Map<String, IContentTypeIdsInfoModel> instancesTypes)
  {
    this.instancesTypes = instancesTypes;
  }
}
