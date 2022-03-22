package com.cs.core.config.interactor.model.taxonomy;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "label", "parentId" })
public class NomenclatureElementsModel implements INomenclatureElementsModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          label;
  protected String          parentId;
  
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
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
}
