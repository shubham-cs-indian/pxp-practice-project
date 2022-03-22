package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IRelationshipInheritanceOnConfigChangeDTO extends ISimpleDTO{
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public List<String> getDeletedRelationships();
  
  public void setDeletedRelationships(List<String> deletedRelationships);
  
  public List<IRelationshipInheritanceModifiedRelationshipDTO> getModifiedRelationships();
  
  public void setModifiedRelationships(List<IRelationshipInheritanceModifiedRelationshipDTO> modifiedRelationshipDTOs);
  
  public List<String> getDeletedNatureRelationshipIds();
  
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds);
  
  
}
