package com.cs.core.config.strategy.usecase.duplicatecode;

import java.util.List;

public class BulkCheckForDuplicateCodesModel implements IBulkCheckForDuplicateCodesModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    codes;
  protected String          entityType;
  protected List<String>    names;

  @Override
  public List<String> getNames()
  {
    return names;
  }

  @Override
  public void setNames(List<String> names)
  {
    this.names = names;
  }

  @Override
  public List<String> getCodes()
  {
    return codes;
  }
  
  @Override
  public void setCodes(List<String> codes)
  {
    this.codes = codes;
  }
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
}
