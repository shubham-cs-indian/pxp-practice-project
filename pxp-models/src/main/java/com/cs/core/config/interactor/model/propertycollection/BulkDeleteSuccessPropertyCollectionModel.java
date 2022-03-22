package com.cs.core.config.interactor.model.propertycollection;

import java.util.List;
import java.util.Map;

public class BulkDeleteSuccessPropertyCollectionModel
    implements IBulkDeleteSuccessPropertyCollectionModel {
  
  private static final long serialVersionUID = 1L;
  List<String>              success;
  Map<String, List<String>> associatedTypeIds;
  
  @Override
  public List<String> getSuccess()
  {
    return success;
  }
  
  @Override
  public void setSuccess(List<String> success)
  {
    this.success = success;
  }
  
  @Override
  public Map<String, List<String>> getAssociatedTypeIds()
  {
    return associatedTypeIds;
  }
  
  @Override
  public void setAssociatedTypeIds(Map<String, List<String>> associatedTypeIds)
  {
    this.associatedTypeIds = associatedTypeIds;
  }
}
