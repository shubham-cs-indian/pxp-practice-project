package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

public class BulkDeleteSuccessKlassResponseModel implements IBulkDeleteSuccessKlassResponseModel {
  
  private static final long serialVersionUID = 1L;
  List<String>              ids;
  List<String>              deletedRelationsipIds;
  List<String>              deletedNatureRelationshipIds;
  
  @Override
  public List<String> getIds()
  {
    if (ids == null) {
      ids = new ArrayList<>();
    }
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public List<String> getDeletedRelationsipIds()
  {
    if (deletedRelationsipIds == null) {
      deletedRelationsipIds = new ArrayList<>();
    }
    return deletedRelationsipIds;
  }
  
  @Override
  public void setDeletedRelationsipIds(List<String> deletedRelationsipIds)
  {
    this.deletedRelationsipIds = deletedRelationsipIds;
  }
  
  @Override
  public List<String> getDeletedNatureRelationshipIds()
  {
    if (deletedNatureRelationshipIds == null) {
      deletedNatureRelationshipIds = new ArrayList<>();
    }
    return deletedNatureRelationshipIds;
  }
  
  @Override
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds)
  {
    this.deletedNatureRelationshipIds = deletedNatureRelationshipIds;
  }
  
}
