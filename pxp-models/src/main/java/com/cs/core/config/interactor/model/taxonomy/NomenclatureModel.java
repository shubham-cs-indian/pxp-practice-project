package com.cs.core.config.interactor.model.taxonomy;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonPropertyOrder({ "id", "label", "children" })
public class NomenclatureModel implements INomenclatureModel {
  
  private static final long                  serialVersionUID = 1L;
  protected String                           id;
  protected String                           label;
  protected List<INomenclatureElementsModel> children;
  protected String                           taxonomyType;
  protected String                           type;
  protected String                           action;
  
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
  public List<INomenclatureElementsModel> getChildren()
  {
    return children;
  }
  
  @Override
  @JsonDeserialize(contentAs = NomenclatureElementsModel.class)
  public void setChildren(List<INomenclatureElementsModel> children)
  {
    this.children = children;
  }
  
  @Override
  public String getTaxonomyType()
  {
    return taxonomyType;
  }
  
  @Override
  public void setTaxonomyType(String taxonomyType)
  {
    this.taxonomyType = taxonomyType;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getAction()
  {
    return action;
  }
  
  @Override
  public void setAction(String ops)
  {
    this.action = ops;
  }
}
