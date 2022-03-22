package com.cs.core.config.interactor.model.taxonomy;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class JMSImportTaxonomyModel implements IJMSImportTaxonomyModel {
  
  private static final long               serialVersionUID = 1L;
  
  protected String                        id;
  protected String                        label;
  protected String                        type;
  protected String                        taxonomyType;
  protected Map<String, String>           linkedLevels;
  protected String                        linkedLevelCode;
  protected String                        parentId;
  protected List<IJMSImportTaxonomyModel> children;
  
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
  public String getParentId()
  {
    return this.parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public List<IJMSImportTaxonomyModel> getChildren()
  {
    return children;
  }
  
  @Override
  @JsonDeserialize(contentAs = JMSImportTaxonomyModel.class)
  public void setChildren(List<IJMSImportTaxonomyModel> children)
  {
    this.children = (List<IJMSImportTaxonomyModel>) children;
  }
  
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
  public Map<String, String> getLinkedLevels()
  {
    return linkedLevels;
  }
  
  @Override
  public void setLinkedLevels(Map<String, String> linkedLevels)
  {
    this.linkedLevels = linkedLevels;
  }
  
  @Override
  public String getLinkedLevelCode()
  {
    return linkedLevelCode;
  }
  
  @Override
  public void setLinkedLevelCode(String linkedLevelCode)
  {
    this.linkedLevelCode = linkedLevelCode;
  }
}
