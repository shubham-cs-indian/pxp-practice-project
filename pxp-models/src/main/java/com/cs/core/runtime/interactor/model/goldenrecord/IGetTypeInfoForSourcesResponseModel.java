package com.cs.core.runtime.interactor.model.goldenrecord;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForTypeInfoModel;
import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetTypeInfoForSourcesResponseModel extends IModel {
  
  public static final String SOURCE_ID_TYPE_INFO_MAP = "sourceIdTypeInfoMap";
  public static final String SOURCE_IDS              = "sourceIds";
  public static final String CONFIG_DETAILS          = "configDetails";
  
  public Map<String, ITypesListModel> getSourceIdTypeInfoMap();
  
  public void setSourceIdTypeInfoMap(Map<String, ITypesListModel> sourceIdTypeInfoMap);
  
  public List<String> getSourceIds();
  
  public void setSourceIds(List<String> sourceIds);
  
  public void setConfigDetails(IConfigDetailsForTypeInfoModel configDetails);
  
  public IConfigDetailsForTypeInfoModel getConfigDetails(); 
}
