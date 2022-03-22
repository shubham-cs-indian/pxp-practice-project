package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface INomenclatureModel extends IModel {
  
  public static final String ID            = "id";
  public static final String lABEL         = "label";
  public static final String CHILDREN      = "children";
  public static final String TAXONOMY_TYPE = "taxonomyType";
  public static final String TYPE          = "type";
  public static final String ACTION        = "action";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<INomenclatureElementsModel> getChildren();
  
  public void setChildren(List<INomenclatureElementsModel> children);
  
  public String getTaxonomyType();
  
  public void setTaxonomyType(String taxonomyType);
  
  public String getType();
  
  public void setType(String type);
  
  public String getAction();
  
  public void setAction(String action);
}
