package com.cs.core.rdbms.coupling.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IBGPCouplingDTO extends ISimpleDTO {
  
  public static final String SOURCE_BASE_ENTITY_IIDS = "sourceBaseEntityIIDs";
  public static final String ADDED_ENTITY_IIDS       = "addedEntityIIDs";
  public static final String DELETED_ENTITY_IIDS     = "deletedEntityIIDs";
  public static final String RELATIONSHIP_ID         = "relationshipId";
  public static final String NATURE_RELATIONSHIP_ID  = "natureRelationshipId";
  public static final String SIDE_ID                 = "sideId";
  
  public void setSourceBaseEntityIIDs(List<Long> sourceBaseEntityIIDs);
  
  public List<Long> getSourceBaseEntityIIDs();
  
  public void setAddedEntityIIDs(List<Long> addedEntityIIDs);
  
  public List<Long> getAddedEntityIIDs();
  
  public void setDeletedEntityIIDs(List<Long> deletedEntityIIDs);
  
  public List<Long> getDeletedEntityIIDs();
  
  public void setRelationshipId(String relationshipId);
  
  public String getRelationshipId();
  
  public void setNatureRelationshipId(String natureRelationshipId);
  
  public String getNatureRelationishipId();
  
  public void setSideId(String sideId);
  
  public String getSideId();
  
}
