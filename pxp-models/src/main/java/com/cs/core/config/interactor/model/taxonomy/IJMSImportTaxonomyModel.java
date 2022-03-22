package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IJMSImportTaxonomyModel extends IModel {
  
  public static final String ID               = "id";
  public static final String LABEL            = "label";
  public static final String TYPE             = "type";
  public static final String TAXONOMY_TYPE    = "taxonomyType";
  public static final String LINKED_lEVELS    = "linkedLevels";
  public static final String LINKED_lEVELCODE = "linkedLevelCode";
  public static final String PARENT_ID        = "parentId";
  public static final String CHILD            = "child";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getTaxonomyType();
  
  public void setTaxonomyType(String taxonomyType);
  
  public Map<String, String> getLinkedLevels();
  
  public void setLinkedLevels(Map<String, String> linkedLevels);
  
  public String getLinkedLevelCode();
  
  public void setLinkedLevelCode(String linkedLevelCode);
  
  public String getType();
  
  public void setType(String type);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public List<IJMSImportTaxonomyModel> getChildren();
  
  public void setChildren(List<IJMSImportTaxonomyModel> children);
  
  public String getId();
  
  public void setId(String id);
}
