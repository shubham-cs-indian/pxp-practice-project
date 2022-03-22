package com.cs.core.transactionend.handlers.dto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

import java.util.Map;

public interface IDependencyChangeDTO extends ISimpleDTO {

  enum Change {
    Added, Deleted, Modified;
  }

  public long getEntityIID();
  public void setEntityIID(long entityIID);

  boolean isCreated();
  void setCreated(boolean created);

  public Map<String, Change> getPropertiesChange();
  public void setPropertiesChange(Map<String, Change> propertiesChange);
}
