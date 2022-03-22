package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IEntityRelationshipInfoDTO  extends ISimpleDTO{
  
 public List<Long> getAddedElements();
  
  public void setAddedElements(List<Long> addedElements);
  
  public List<Long> getRemovedElements();
  
  public void setRemovedElements(List<Long> removedElements);
  
  public String  getNatureRelationshipId();
  
  public void setNatureRelationshipId(String natureRelationshipId);
  
  public String  getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
 public String  getSideId();
  
  public void setSideId(String sideId);
  
  
}
