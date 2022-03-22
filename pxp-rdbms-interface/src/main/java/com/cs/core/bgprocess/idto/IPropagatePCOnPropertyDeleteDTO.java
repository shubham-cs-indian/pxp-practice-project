package com.cs.core.bgprocess.idto;

import java.util.List;
import java.util.Set;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IPropagatePCOnPropertyDeleteDTO extends ISimpleDTO {
  
  public void setDeletedPropertyCodes(List<String> deletedPropertyCodes);
  public List<String> getDeletedPropertyCodes();
  
  public void setClassifierCodes(Set<String> classifierCodes);
  public Set<String> getClassifierCodes();
  
  public void setAddedPropertyCodes(Set<String> addedPropertyCodes);
  public Set<String> getAddedPropertyCodes();
  
}
