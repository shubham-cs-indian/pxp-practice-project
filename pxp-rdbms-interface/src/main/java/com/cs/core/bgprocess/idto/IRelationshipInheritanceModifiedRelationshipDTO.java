package com.cs.core.bgprocess.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IRelationshipInheritanceModifiedRelationshipDTO extends ISimpleDTO {
  
  public String getId();
  
  public void setId(String id);
  
  public String getSide();
  
  public void setSide(String side);
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
  
}
