package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetContentGridElasticResponseModel extends IModel {
  
  public static final String KLASS_INSTANCES          = "klassInstances";
  public static final String TAG_ID_TAG_VALUE_IDS_MAP = "tagIdTagValueIdsMap";
  
  public List<IGetContentForGridResponseModel> getKlassInstances();
  
  public void setKlassInstances(List<IGetContentForGridResponseModel> klassInstance);
  
  public Map<String, List<String>> getTagIdTagValueIdsMap();
  
  public void setTagIdTagValueIdsMap(Map<String, List<String>> tagIdTagValueIdsMap);
}
