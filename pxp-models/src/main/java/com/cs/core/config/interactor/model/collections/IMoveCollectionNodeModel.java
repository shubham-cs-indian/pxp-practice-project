package com.cs.core.config.interactor.model.collections;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;

public interface IMoveCollectionNodeModel extends IConfigResponseWithAuditLogModel {
  
  public static final String PARENT_ID     = "parentId";
  public static final String COLLECTION_ID = "collectionId";
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getCollectionId();
  
  public void setCollectionId(String collectionId);
}
