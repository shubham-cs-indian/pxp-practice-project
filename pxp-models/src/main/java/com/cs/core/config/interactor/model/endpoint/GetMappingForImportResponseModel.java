package com.cs.core.config.interactor.model.endpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.mapping.ConfigRuleTagMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetMappingForImportResponseModel implements IGetMappingForImportResponseModel {
  
  private static final long                  serialVersionUID = 1L;
  
  protected Map<String, List<String>>        attributes;
  protected Map<String, String>              tags;
  protected Map<String, String>              classes;
  protected Map<String, String>              taxonomies;
  protected List<String>                     headersToRead    = new ArrayList<>();
  protected String                           endpointId;
  protected List<IConfigRuleTagMappingModel> tagMappings      = new ArrayList<>();
  protected String                           mappingType;
  protected Map<String, String>              relationships;
  
  @Override
  public Map<String, List<String>> getAttributes()
  {
    if(attributes == null)
      return new HashMap<String, List<String>>();
    return attributes;
  }
  
  @Override
  public void setAttributes(Map<String, List<String>> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public Map<String, String> getTags()
  {
    if(tags == null)
      return new HashMap<String, String>();
    return tags;
  }
  
  @Override
  public void setTags(Map<String, String> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public Map<String, String> getClasses()
  {
    if(classes ==null)
      return new HashMap<String, String>();
    return classes;
  }
  
  @Override
  public void setClasses(Map<String, String> classes)
  {
    this.classes = classes;
  }
  
  @Override
  public Map<String, String> getTaxonomies()
  {
    if(taxonomies == null)
      return new HashMap<String, String>();
    return taxonomies;
  }
  
  @Override
  public void setTaxonomies(Map<String, String> taxonomies)
  {
    this.taxonomies = taxonomies;
  }
  
  @Override
  public List<String> getHeadersToRead()
  {
    if(headersToRead == null)
      return new ArrayList<String>();
    return headersToRead;
  }
  
  @Override
  public void setHeadersToRead(List<String> headersToRead)
  {
    this.headersToRead = headersToRead;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }

  @Override
  @JsonDeserialize(contentAs = ConfigRuleTagMappingModel.class)
  public void setTagMappings(List<IConfigRuleTagMappingModel> tagMappings)
  {
    this.tagMappings = tagMappings;    
  }

  @Override
  public List<IConfigRuleTagMappingModel> getTagMappings()
  {    
    return tagMappings;
  } 
  
  @Override
  public String getMappingType()
  {
    return mappingType;
  }
  
  @Override
  public void setMappingType(String mappingType)
  {
    this.mappingType = mappingType;
  }

  @Override
  public Map<String, String> getRelationships()
  {
    return relationships;
  }

  @Override
  public void setRelationships(Map<String, String> relationships)
  {
    this.relationships = relationships;
  }
  
}
