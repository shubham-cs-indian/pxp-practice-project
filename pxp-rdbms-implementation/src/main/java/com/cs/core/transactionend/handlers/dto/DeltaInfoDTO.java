package com.cs.core.transactionend.handlers.dto;

import java.util.ArrayList;
import java.util.List;

public class DeltaInfoDTO implements IDeltaInfoDTO {

  private List<Long>                      modifiedPropertyIIDs   = new ArrayList<>();
  private List<Long>                      deletedPropertyIIDs    = new ArrayList<>();
  private List<Long>                      addedPropertyIIDs      = new ArrayList<>();
  private String                          localeId;
  private boolean                         isCreated              = false;
  private List<String> addedCoupledProperties = new ArrayList<>();
  private List<String> removedCoupledProperties = new ArrayList<>();

  @Override
  public List<String> getAddedCoupledProperties()
  {
    return addedCoupledProperties;
  }

  @Override
  public void setAddedCoupledProperties(List<String> addedCoupledProperties)
  {
    this.addedCoupledProperties = addedCoupledProperties;
  }

  @Override
  public List<String> getRemovedCoupledProperties()
  {
    return removedCoupledProperties;
  }

  @Override
  public void setRemovedCoupledProperties(List<String> removedCoupledProperties)
  {
    this.removedCoupledProperties = removedCoupledProperties;
  }

  @Override
  public boolean isCreated()
  {
    return isCreated;
  }

  @Override
  public void setCreated(boolean created)
  {
    isCreated = created;
  }

  @Override
  public String getLocaleId()
  {
    return localeId;
  }

  @Override
  public void setLocaleId(String localeId)
  {
    this.localeId = localeId;
  }

  @Override
  public List<Long> getModifiedPropertyIIDs()
  {
    return modifiedPropertyIIDs;
  }

  @Override
  public void setModifiedPropertyIIDs(List<Long> modifiedPropertyIIDs)
  {
    this.modifiedPropertyIIDs = modifiedPropertyIIDs;
  }

  @Override
  public List<Long> getDeletedPropertyIIDs()
  {
    return deletedPropertyIIDs;
  }

  @Override
  public void setDeletedPropertyIIDs(List<Long> deletedPropertyIIDs)
  {
    this.deletedPropertyIIDs = deletedPropertyIIDs;
  }

  @Override
  public List<Long> getAddedPropertyIIDs()
  {
    return addedPropertyIIDs;
  }

  @Override
  public void setAddedPropertyIIDs(List<Long> addedPropertyIIDs)
  {
    this.addedPropertyIIDs = addedPropertyIIDs;
  }
}
