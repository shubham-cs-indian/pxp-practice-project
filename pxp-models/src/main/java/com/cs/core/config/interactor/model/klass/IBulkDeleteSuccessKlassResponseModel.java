package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkDeleteSuccessKlassResponseModel extends IModel {
  
  public static final String IDS                             = "ids";
  public static final String DELETED_RELATIONSIP_IDS         = "deletedRelationsipIds";
  public static final String DELETED_NATURE_RELATIONSHIP_IDS = "deletedNatureRelationshipIds";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public List<String> getDeletedRelationsipIds();
  
  public void setDeletedRelationsipIds(List<String> deletedRelationsipIds);
  
  public List<String> getDeletedNatureRelationshipIds();
  
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds);
  
}
