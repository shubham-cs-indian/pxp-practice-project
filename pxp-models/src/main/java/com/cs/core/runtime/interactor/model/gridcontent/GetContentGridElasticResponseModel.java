package com.cs.core.runtime.interactor.model.gridcontent;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetContentGridElasticResponseModel implements IGetContentGridElasticResponseModel {
  
  private static final long                       serialVersionUID = 1L;
  protected List<IGetContentForGridResponseModel> klassInstance;
  protected Map<String, List<String>>             tagIdTagValueIdsMap;
  
  @Override
  public List<IGetContentForGridResponseModel> getKlassInstances()
  {
    if (klassInstance == null) {
      klassInstance = new ArrayList<>();
    }
    return klassInstance;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetContentForGridResponseModel.class)
  public void setKlassInstances(List<IGetContentForGridResponseModel> klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public Map<String, List<String>> getTagIdTagValueIdsMap()
  {
    if (tagIdTagValueIdsMap == null) {
      return new HashMap<String, List<String>>();
    }
    return tagIdTagValueIdsMap;
  }
  
  @Override
  public void setTagIdTagValueIdsMap(Map<String, List<String>> tagIdTagValueIdsMap)
  {
    this.tagIdTagValueIdsMap = tagIdTagValueIdsMap;
  }
}
