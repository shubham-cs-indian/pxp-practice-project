package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface ITaskReferencedInstanceModel extends IModel {
  
  public static final String ID             = "id";
  public static final String LABEL          = "label";
  public static final String ASSET_INSTANCE = "assetInstance";
  public static final String TYPES          = "types";
  
  public Map<String, Object> getAssetInstance();
  
  public void setAssetInstance(Map<String, Object> assetInstance);
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
}
