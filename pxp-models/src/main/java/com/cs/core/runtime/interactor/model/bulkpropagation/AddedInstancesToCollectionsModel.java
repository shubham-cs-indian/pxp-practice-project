package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class AddedInstancesToCollectionsModel implements IAddedInstancesToCollectionsModel {
  
  private static final long       serialVersionUID = 1L;
  protected List<IIdAndTypeModel> contentIdBaseTypeInfo;
  protected List<String>          instancesAddedToCollectionIds;
  
  @Override
  public List<IIdAndTypeModel> getContentIdBaseTypeInfo()
  {
    if (contentIdBaseTypeInfo == null) {
      contentIdBaseTypeInfo = new ArrayList<>();
    }
    return contentIdBaseTypeInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  public void setContentIdBaseTypeInfo(List<IIdAndTypeModel> contentIdBaseTypeInfo)
  {
    this.contentIdBaseTypeInfo = contentIdBaseTypeInfo;
  }
  
  @Override
  public List<String> getIntancesAddedToCollectionIds()
  {
    if (instancesAddedToCollectionIds == null) {
      instancesAddedToCollectionIds = new ArrayList<>();
    }
    return instancesAddedToCollectionIds;
  }
  
  @Override
  public void setInstancesAddedToCollectionIds(List<String> instancesAddedToCollectionIds)
  {
    this.instancesAddedToCollectionIds = instancesAddedToCollectionIds;
  }
}
