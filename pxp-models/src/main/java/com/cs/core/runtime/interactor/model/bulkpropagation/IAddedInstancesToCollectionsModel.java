package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IAddedInstancesToCollectionsModel extends IModel {
  
  public static final String CONTENT_ID_BASE_TYPE_INFO         = "contentIdBaseTypeInfo";
  public static final String INSTANCES_ADDED_TO_COLLECTION_IDS = "instancesAddedToCollectionIds";
  
  public List<IIdAndTypeModel> getContentIdBaseTypeInfo();
  
  public void setContentIdBaseTypeInfo(List<IIdAndTypeModel> contentIdBaseTypeInfo);
  
  public List<String> getIntancesAddedToCollectionIds();
  
  public void setInstancesAddedToCollectionIds(List<String> collectionIds);
}
