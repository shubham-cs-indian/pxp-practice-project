package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IBulkDeleteSuccessPropertyCollectionModel extends IModel {
  
  public static final String SUCCESS             = "success";
  public static final String ASSOCIATED_TYPE_IDS = "associatedTypeIds";
  
  public List<String> getSuccess();
  
  public void setSuccess(List<String> success);
  
  // Map of entityId vs associated klassIds
  public Map<String, List<String>> getAssociatedTypeIds();
  
  public void setAssociatedTypeIds(Map<String, List<String>> associatedTypeIds);
}
