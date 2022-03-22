package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetKlassConfigRequestModel extends IModel {
  
  public static final String KLASS_REFERENCED_ELEMENTS = "klassReferencedElements";
  public static final String SELECTED_PROPERTY_IDS     = "selectedPropertyIds";
  public static final String USER_ID                   = "userId";
  public static final String TAG_ID_TAG_VALUE_IDS_MAP  = "tagIdTagValueIdsMap";
  
  public Map<String, IGetReferencedElementRequestModel> getKlassReferencedElements();
  
  public void setKlassReferencedElements(
      Map<String, IGetReferencedElementRequestModel> klassReferencedElements);
  
  public List<String> getSelectedPropertyIds();
  
  public void setSelectedPropertyIds(List<String> selectedPropertyIds);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public Map<String, List<String>> getTagIdTagValueIdsMap();
  
  public void setTagIdTagValueIdsMap(Map<String, List<String>> tagIdTagValueIdsMap);
}
