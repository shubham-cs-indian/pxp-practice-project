package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.ContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class DeletedCollectionAssociatedInstancesInfoForBulkPropagationModel
    implements IDeletedCollectionAssociatedInstancesInfoForBulkPropagationModel {
  
  private static final long                serialVersionUID = 1L;
  protected List<IContentTypeIdsInfoModel> contentTypeInfoList;
  protected List<String>                   deletedCollectionIds;
  
  @Override
  public List<IContentTypeIdsInfoModel> getContentTypeInfoList()
  {
    return contentTypeInfoList;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContentTypeIdsInfoModel.class)
  public void setContentTypeInfoList(List<IContentTypeIdsInfoModel> contentTypeInfolist)
  {
    this.contentTypeInfoList = contentTypeInfolist;
  }
  
  @Override
  public List<String> getDeletedCollectionIds()
  {
    return deletedCollectionIds;
  }
  
  @Override
  public void setDeletedCollectionIds(List<String> deletedCollectionIds)
  {
    this.deletedCollectionIds = deletedCollectionIds;
  }
}
