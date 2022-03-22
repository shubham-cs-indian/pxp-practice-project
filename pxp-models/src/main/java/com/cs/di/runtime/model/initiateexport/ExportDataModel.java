package com.cs.di.runtime.model.initiateexport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportDataModel implements IExportDataModel {
  
  private static final long     serialVersionUID = 1L;
  protected String              configType;
  protected List<String>        configCodes;
  protected Map<String, Object> searchCriteria;
  protected String              exportType;
  protected String              endpointId;
  protected String              languageCode;
  
  @Override
  public List<String> getConfigCodes()
  {
    if (configCodes == null) {
      configCodes = new ArrayList<>();
    }
    return configCodes;
  }
  
  @Override
  public void setConfigCodes(List<String> configCodes)
  {
    this.configCodes = configCodes;
  }
  
  @Override
  public Map<String, Object> getSearchCriteria()
  {
    if (searchCriteria == null) {
      searchCriteria = new HashMap<>();
    }
    return searchCriteria;
  }
  
  @Override
  public void setSearchCriteria(Map<String, Object> searchCriteria)
  {
    this.searchCriteria = searchCriteria;
  }
  
  @Override
  public String getConfigType()
  {
    return configType;
  }
  
  @Override
  public void setConfigType(String configType)
  {
    this.configType = configType;
  }
  
  @Override
  public String getExportType()
  {
    return exportType;
  }
  
  @Override
  public void setExportType(String exportType)
  {
    this.exportType = exportType;
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
  public String getLanguageCode()
  {
    return this.languageCode;
  }

  @Override
  public void setLanguageCode(String languageCode)
  {
    this.languageCode = languageCode;
  } 
}
