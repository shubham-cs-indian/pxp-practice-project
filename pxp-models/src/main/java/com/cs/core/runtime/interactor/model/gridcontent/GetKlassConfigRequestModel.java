package com.cs.core.runtime.interactor.model.gridcontent;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetKlassConfigRequestModel implements IGetKlassConfigRequestModel {
  
  private static final long                                serialVersionUID = 1L;
  protected Map<String, IGetReferencedElementRequestModel> klassReferencedElements;
  protected List<String>                                   selectedPropertyIds;
  protected String                                         userId;
  protected Map<String, List<String>>                      tagIdTagValueIdsMap;
  
  @Override
  public Map<String, IGetReferencedElementRequestModel> getKlassReferencedElements()
  {
    return klassReferencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetReferencedElementRequestModel.class)
  public void setKlassReferencedElements(
      Map<String, IGetReferencedElementRequestModel> klassReferencedElements)
  {
    if (klassReferencedElements == null) {
      klassReferencedElements = new HashMap<>();
    }
    this.klassReferencedElements = klassReferencedElements;
  }
  
  @Override
  public List<String> getSelectedPropertyIds()
  {
    return selectedPropertyIds;
  }
  
  @Override
  public void setSelectedPropertyIds(List<String> selectedPropertyIds)
  {
    if (selectedPropertyIds == null) {
      selectedPropertyIds = new ArrayList<>();
    }
    this.selectedPropertyIds = selectedPropertyIds;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public Map<String, List<String>> getTagIdTagValueIdsMap()
  {
    return tagIdTagValueIdsMap;
  }
  
  @Override
  public void setTagIdTagValueIdsMap(Map<String, List<String>> tagIdTagValueIdsMap)
  {
    this.tagIdTagValueIdsMap = tagIdTagValueIdsMap;
  }
}
