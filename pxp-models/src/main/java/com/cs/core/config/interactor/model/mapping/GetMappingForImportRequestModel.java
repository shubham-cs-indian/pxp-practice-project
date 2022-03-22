package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetMappingForImportRequestModel implements IGetMappingForImportRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>        fileHeaders      = new ArrayList<>();
  protected String              mappingId;
  protected String              boardingType;
  protected Map<String, Object> klassesTaxonomiesFromFile;
  
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
  public String getMappingId()
  {
    return mappingId;
  }
  
  @Override
  public void setMappingId(String mappingId)
  {
    this.mappingId = mappingId;
  }

  
  public String getBoardingType()
  {
    return boardingType;
  }

  
  public void setBoardingType(String boardingType)
  {
    this.boardingType = boardingType;
  }

  
  public Map<String, Object>  getKlassesTaxonomiesFromFile()
  {
    return klassesTaxonomiesFromFile;
  }

  
  public void setKlassesTaxonomiesFromFile(Map<String, Object>  klassesTaxonomiesFromFile)
  {
    this.klassesTaxonomiesFromFile = klassesTaxonomiesFromFile;
  }
  
}
