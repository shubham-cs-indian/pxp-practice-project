package com.cs.core.transactionend.handlers.dto;

import java.util.List;

public interface IDeltaInfoDTO {

  public String getLocaleId();
  public void setLocaleId(String localeId);

  List<Long> getModifiedPropertyIIDs();
  void setModifiedPropertyIIDs(List<Long> modifiedPropertyIIDs);

  List<Long> getDeletedPropertyIIDs();
  void setDeletedPropertyIIDs(List<Long> deletedPropertyIIDs);

  List<Long> getAddedPropertyIIDs();
  void setAddedPropertyIIDs(List<Long> addedPropertyIIDs);

  List<String> getAddedCoupledProperties();
  void setAddedCoupledProperties(List<String> addedCoupledProperties);

  List<String> getRemovedCoupledProperties();
  void setRemovedCoupledProperties(List<String> removedCoupledProperties);

  boolean isCreated();
  void setCreated(boolean created);

}
