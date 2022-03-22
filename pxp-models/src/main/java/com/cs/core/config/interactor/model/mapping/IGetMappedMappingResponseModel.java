package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetMappedMappingResponseModel extends IModel {
  
  public static final String ATTRIBUTES      = "attributes";
  public static final String TAGS            = "tags";
  public static final String CLASSES         = "classes";
  public static final String TAXONOMIES      = "taxonomies";
  public static final String HEADERS_TO_READ = "headersToRead";
  public static final String ENDPOINT_ID     = "endpointId";
  
  public Map<String, List<String>> getAttributes();
  
  public void setAttributes(Map<String, List<String>> attributes);
  
  public Map<String, String> getTags();
  
  public void setTags(Map<String, String> tags);
  
  public Map<String, String> getClasses();
  
  public void setClasses(Map<String, String> classes);
  
  public Map<String, String> getTaxonomies();
  
  public void setTaxonomies(Map<String, String> taxonomies);
  
  public List<String> getHeadersToRead();
  
  public void setHeadersToRead(List<String> headersToRead);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
}
