package com.cs.core.config.interactor.model.propertycollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.klass.DefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SavePropertyCollectionResponseModel extends ConfigResponseWithAuditLogModel implements ISavePropertyCollectionResponseModel {
  
  private static final long                serialVersionUID = 1L;
  protected List<IDefaultValueChangeModel> defaultValuesDiff;
  IGetPropertyCollectionModel              getResponse;
  Map<String, List<String>>                deletedPropertiesFromSource;
  
  @Override
  public IGetPropertyCollectionModel getPropertyCollectionGetRespose()
  {
    return getResponse;
  }
  
  @Override
  @JsonDeserialize(as = GetPropertyCollectionModel.class)
  public void setPropertyCollectionGetResponse(IGetPropertyCollectionModel getResponse)
  {
    this.getResponse = getResponse;
  }
  
  @Override
  public Map<String, List<String>> getDeletedPropertiesFromSource()
  {
    if (deletedPropertiesFromSource == null) {
      deletedPropertiesFromSource = new HashMap<>();
    }
    return deletedPropertiesFromSource;
  }
  
  @Override
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource)
  {
    this.deletedPropertiesFromSource = deletedPropertiesFromSource;
  }
  
  @Override
  public List<IDefaultValueChangeModel> getDefaultValuesDiff()
  {
    if (defaultValuesDiff == null) {
      defaultValuesDiff = new ArrayList<>();
    }
    return defaultValuesDiff;
  }
  
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  @Override
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff)
  {
    this.defaultValuesDiff = defaultValuesDiff;
  }
}
