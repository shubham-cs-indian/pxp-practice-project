package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllRelationshipsModel extends IModel {
  
  public static final String KLASS_IDS               = "klassIds";
  public static final String RELATIONSHIP_IDS        = "relationshipIds";
  public static final String NATURE_RELATIONSHIP_IDS = "natureRelationshipIds";
  public static final String ID                      = "id";
  public static final String SIZE                    = "size";
  public static final String CURRENT_USER_ID         = "currentUserId";
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> relationshipIds);
  
  public List<String> getNatureRelationshipIds();
  
  public void setNatureRelationshipIds(List<String> natureRelationshipIds);
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
}
