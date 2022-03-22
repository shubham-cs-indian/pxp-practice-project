package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class UnitCollections implements IUnitCollections {
  
  private static final long                                           serialVersionUID = 1L;
  private Map<String, List<IPropertyCollectionsWithTimestampDiModel>> unitInfo;
  private String                                                      unitLabel;
  
  public String getUnitLabel()
  {
    return unitLabel;
  }
  
  public void setUnitLabel(String unitLabel)
  {
    this.unitLabel = unitLabel;
  }
  
  @Override
  public Map<String, List<IPropertyCollectionsWithTimestampDiModel>> getUnitInfo()
  {
    return this.unitInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyCollectionsWithTimestampDiModel.class)
  public void setUnitInfo(Map<String, List<IPropertyCollectionsWithTimestampDiModel>> unitinfo)
  {
    this.unitInfo = unitinfo;
  }
}
