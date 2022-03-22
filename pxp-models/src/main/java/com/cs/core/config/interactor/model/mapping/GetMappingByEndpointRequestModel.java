package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetMappingByEndpointRequestModel implements IGetMappingByEndpointRequestModel {
  
  private static final long     serialVersionUID          = 1L;
  
  protected List<String>        fileHeaders               = new ArrayList<>();
  protected String              endpointId;
  protected String              boardingType;
  protected Map<String, Object> klassesTaxonomiesFromFile = new HashMap<>();
  
  @Override
  public List<String> getFileHeaders()
  {
    if (fileHeaders == null) {
      fileHeaders = new ArrayList<>();
    }
    return fileHeaders;
  }
  
  @Override
  public void setFileHeaders(List<String> fileHeaders)
  {
    this.fileHeaders = fileHeaders;
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
  public String getBoardingType()
  {
    return this.boardingType;
  }
  
  @Override
  public void setBoardingType(String boardingType)
  {
    this.boardingType = boardingType;
  }
  
  @Override
  public Map<String, Object> getKlassesTaxonomiesFromFile()
  {
    return klassesTaxonomiesFromFile;
  }
  
  @Override
  public void setKlassesTaxonomiesFromFile(Map<String, Object> klassesTaxonomiesFromFile)
  {
    this.klassesTaxonomiesFromFile = klassesTaxonomiesFromFile;
  }
}
