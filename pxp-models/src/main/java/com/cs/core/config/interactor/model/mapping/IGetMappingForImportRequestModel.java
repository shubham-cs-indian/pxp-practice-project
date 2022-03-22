package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetMappingForImportRequestModel extends IModel {
  
  public static final String FILE_HEADERS                 = "fileHeaders";
  public static final String MAPPING_ID                   = "mappingId";
  public static final String BOARDING_TYPE                = "boardingType";
  public static final String KLASSES_TAXONOMIES_FROM_FILE = "klassesTaxonomiesFromFile";
  
  public String getBoardingType();
  
  public void setBoardingType(String boardingType);
  
  public List<String> getFileHeaders();
  
  public void setFileHeaders(List<String> fileHeaders);
  
  public String getMappingId();
  
  public void setMappingId(String endpointId);
  
  public Map<String, Object> getKlassesTaxonomiesFromFile();
  
  public void setKlassesTaxonomiesFromFile(Map<String, Object> klassesTaxonomiesFromFile);
}
