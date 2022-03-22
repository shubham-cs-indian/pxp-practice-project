package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetMappingByEndpointRequestModel extends IModel {
  
  public static final String FILE_HEADERS                 = "fileHeaders";
  public static final String ENDPOINT_ID                  = "endpointId";
  public static final String BOARDING_TYPE                = "boardingType";
  public static final String KLASSES_TAXONOMIES_FROM_FILE = "klassesTaxonomiesFromFile";
  
  public String getBoardingType();
  
  public void setBoardingType(String boardingType);
  
  public List<String> getFileHeaders();
  
  public void setFileHeaders(List<String> fileHeaders);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public Map<String, Object> getKlassesTaxonomiesFromFile();
  
  public void setKlassesTaxonomiesFromFile(Map<String, Object> klassesTaxonomiesFromFile);
}
