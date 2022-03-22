package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;

import java.util.ArrayList;
import java.util.List;

public class RemovedInstancesFromCollectionModel implements IRemovedInstancesFromCollectionModel {
  
  private static final long       serialVersionUID = 1L;
  protected List<IIdAndTypeModel> contentIdBaseTypeInfo;
  protected List<String>          collectionIds;
  
  @Override
  public List<IIdAndTypeModel> getContentIdBaseTypeInfo()
  {
    if (contentIdBaseTypeInfo == null) {
      contentIdBaseTypeInfo = new ArrayList<>();
    }
    return contentIdBaseTypeInfo;
  }
  
  @Override
  public void setContentIdBaseTypeInfo(List<IIdAndTypeModel> contentIdBaseTypeInfo)
  {
    this.contentIdBaseTypeInfo = contentIdBaseTypeInfo;
  }
  
  @Override
  public List<String> getIntancesRemovedFromCollectionIds()
  {
    if (collectionIds == null) {
      collectionIds = new ArrayList<>();
    }
    return collectionIds;
  }
  
  @Override
  public void setInstancesRemovedFromCollectionIds(List<String> collectionIds)
  {
    this.collectionIds = collectionIds;
  }
}
