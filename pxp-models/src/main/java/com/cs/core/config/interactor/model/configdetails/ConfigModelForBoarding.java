package com.cs.core.config.interactor.model.configdetails;

import java.util.HashMap;
import java.util.Map;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigModelForBoarding implements IConfigModelForBoarding {
  
  private static final long                            serialVersionUID = 1L;
  protected Map<String, IConfigEntityInformationModel> attributes;
  protected Map<String, ITag>                          tags;
  protected Map<String, IConfigEntityInformationModel> taxonomy;
  protected Map<String, IConfigEntityInformationModel> klasses;
  protected Map<String, IConfigEntityInformationModel> propertyCollections;
  protected Map<String, IConfigEntityInformationModel> relationships;
  protected Map<String, IConfigEntityInformationModel> contexts;
  
  @Override
  public Map<String, IConfigEntityInformationModel> getAttributes()
  {
    return attributes;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setAttributes(Map<String, IConfigEntityInformationModel> attributes)
  {
    if (attributes == null) {
      attributes = new HashMap<>();
    }
    this.attributes = attributes;
  }
  
  @Override
  public Map<String, ITag> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setTags(Map<String, ITag> tags)
  {
    if (tags == null) {
      tags = new HashMap<>();
    }
    this.tags = tags;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getTaxonomy()
  {
    return taxonomy;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setTaxonomy(Map<String, IConfigEntityInformationModel> taxonomy)
  {
    if (taxonomy == null) {
      taxonomy = new HashMap<>();
    }
    this.taxonomy = taxonomy;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getKlasses()
  {
    return klasses;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setKlasses(Map<String, IConfigEntityInformationModel> klasses)
  {
    if (klasses == null) {
      klasses = new HashMap<>();
    }
    this.klasses = klasses;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getPropertyCollections()
  {
    return propertyCollections;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setPropertyCollections(Map<String, IConfigEntityInformationModel> propertyCollections)
  {
    if (propertyCollections == null) {
      propertyCollections = new HashMap<>();
    }
    this.propertyCollections = propertyCollections;
  }

  @Override
  public Map<String, IConfigEntityInformationModel> getRelationships()
  {
    return relationships;
  }

  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setRelationships(Map<String, IConfigEntityInformationModel> relationships)
  {
    this.relationships = relationships;
  }

  @Override
  public Map<String, IConfigEntityInformationModel> getContexts()
  {
    return contexts;
  }

  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setContexts(Map<String, IConfigEntityInformationModel> contexts)
  {
    this.contexts = contexts;
  }
}
