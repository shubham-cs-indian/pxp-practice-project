package com.cs.core.config.interactor.model.collections;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;

public class MoveCollectionNodeModel extends ConfigResponseWithAuditLogModel
    implements IMoveCollectionNodeModel {
  
  private static final long serialVersionUID = 1L;
  protected String          parentId;
  protected String          collectionId;
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public String getCollectionId()
  {
    return collectionId;
  }
  
  @Override
  public void setCollectionId(String collectionId)
  {
    this.collectionId = collectionId;
  }
}
