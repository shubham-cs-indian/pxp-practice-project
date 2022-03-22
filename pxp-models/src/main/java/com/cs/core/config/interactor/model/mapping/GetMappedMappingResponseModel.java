package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetMappedMappingResponseModel implements IGetMappedMappingResponseModel {
  
  private static final long           serialVersionUID = 1L;
  
  protected Map<String, List<String>> attributes;
  protected Map<String, String>       tags;
  protected Map<String, String>       classes;
  protected Map<String, String>       taxonomies;
  protected List<String>              headersToRead    = new ArrayList<>();
  protected String                    endpointId;
  
  @Override
  public Map<String, List<String>> getAttributes()
  {
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
    return tags;
  }
  
  @Override
  public void setTags(Map<String, String> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<String> getHeadersToRead()
  {
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
  public Map<String, String> getClasses()
  {
    
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
    
    return taxonomies;
  }
  
  @Override
  public void setTaxonomies(Map<String, String> taxonomies)
  {
    this.taxonomies = taxonomies;
  }
}
