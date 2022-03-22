package com.cs.core.runtime.interactor.model.configuration;

import java.util.Map;

public class IdLabelCodeMapModel implements IIdLabelCodeMapModel{
  
  protected String              id;
  protected String              label;
  protected String              code;
  protected Map<String, String> blockId;
  
  
  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public void setId(String id)
  {
    this.id = id;
  }

  @Override
  public String getLabel()
  {
    return label;
  }

  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }

  @Override
  public String getCode()
  {
    return code;
  }

  @Override
  public void setCode(String code)
  {
    this.code = code;
  }

  @Override
  public Map<String, String> getBlockId()
  {
    return blockId;
  }

  @Override
  public void setBlockId(Map<String, String> blockId)
  {
    this.blockId = blockId;
  }
  
}
