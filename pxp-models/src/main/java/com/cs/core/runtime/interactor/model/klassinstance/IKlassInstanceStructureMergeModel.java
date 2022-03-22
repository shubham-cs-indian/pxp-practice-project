package com.cs.core.runtime.interactor.model.klassinstance;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public interface IKlassInstanceStructureMergeModel {
  
  public static final String ID         = "id";
  public static final String IS_REMOVED = "isRemoved";
  public static final String LABEL      = "label";
  public static final String DATA       = "data";
  public static final String ATTRIBUTES = "attributes";
  
  public String getId();
  
  public void setId(String id);
  
  @JsonIgnore
  public Boolean getIsRemoved();
  
  public void setIsRemoved(Boolean isRemoved);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getData();
  
  public void setData(String data);
  
  @JsonIgnore
  public Map<String, String> getAttributes();
  
  public void setAttributes(Map<String, String> attributes);
}
