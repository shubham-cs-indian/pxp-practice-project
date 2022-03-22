package com.cs.core.runtime.interactor.model.configuration;

import java.util.Map;

public interface IIdLabelCodeMapModel {
  
  public static final String ID    = "id";
  public static final String LABEL = "label";
  public static final String CODE  = "code";
  public static final String BLOCK_ID  = "blockId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getCode();
  
  public void setCode(String code);
  
  public Map<String, String> getBlockId();
  
  public void setBlockId(Map<String, String> blockId);
}
