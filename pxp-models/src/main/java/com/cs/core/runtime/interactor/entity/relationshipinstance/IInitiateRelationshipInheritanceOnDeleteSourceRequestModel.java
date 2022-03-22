package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.config.interactor.model.klass.IDeletedContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.List;

public interface IInitiateRelationshipInheritanceOnDeleteSourceRequestModel extends IModel {
  
  public static final String DELETED_CONTENT_IDS           = "deletedContentIds";
  public static final String DELETED_CONTENT_TYPE_IDS_INFO = "deletedContentTypeIdsInfo";
  
  public List<String> getDeletedContentIds();
  
  public void setDeletedContentIds(List<String> deletedContentIds);
  
  public List<IDeletedContentTypeIdsInfoModel> getDeletedContentTypeIdsInfo();
  
  public void setDeletedContentTypeIdsInfo(
      List<IDeletedContentTypeIdsInfoModel> newDeletedContentTypeIdsInfo);
}
