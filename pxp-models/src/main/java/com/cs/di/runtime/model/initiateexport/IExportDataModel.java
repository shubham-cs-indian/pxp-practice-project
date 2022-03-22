package com.cs.di.runtime.model.initiateexport;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IExportDataModel extends IModel {
  
  public String getConfigType();
  public void setConfigType(String configType);
  
  public List<String> getConfigCodes();
  public void setConfigCodes(List<String> configCodes);

  public Map<String, Object> getSearchCriteria();
  public void setSearchCriteria(Map<String, Object> searchCriteria);

  public String getExportType();
  public void setExportType(String exportType);
  
  public String getEndpointId();
  public void setEndpointId(String endpointId);
  
  public String getLanguageCode();
  public void setLanguageCode(String languageCode);
}
