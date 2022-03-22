package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDeletedCollectionAssociatedInstancesInfoForBulkPropagationModel extends IModel {
  
  public static final String CONTENT_TYPE_INFO_LIST = "contentTypeInfoList";
  public static final String DELETED_COLLECTION_IDS = "deletedCollectionIds";
  
  public List<IContentTypeIdsInfoModel> getContentTypeInfoList();
  
  public void setContentTypeInfoList(List<IContentTypeIdsInfoModel> contentTypeInfolist);
  
  public List<String> getDeletedCollectionIds();
  
  public void setDeletedCollectionIds(List<String> deletedCollectionIds);
}
