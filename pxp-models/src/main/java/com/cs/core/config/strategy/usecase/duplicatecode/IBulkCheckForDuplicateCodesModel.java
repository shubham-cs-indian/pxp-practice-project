package com.cs.core.config.strategy.usecase.duplicatecode;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkCheckForDuplicateCodesModel extends IModel {
  
  public static final String CODES       = "codes";
  public static final String ENTITY_TYPE = "entityType";
  public static final String NAMES       = "names";

  public List<String> getNames();
  
  public void setNames(List<String> names);
  
  public List<String> getCodes();
  
  public void setCodes(List<String> codes);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
}
