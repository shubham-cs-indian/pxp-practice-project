package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IUnitCollections extends IModel {
  
  public static final String UNIT_COLLECTIONS = "unitcollections";
  
  public Map<String, List<IPropertyCollectionsWithTimestampDiModel>> getUnitInfo();
  
  public void setUnitInfo(Map<String, List<IPropertyCollectionsWithTimestampDiModel>> unitinfo);
  
  public String getUnitLabel();
  
  public void setUnitLabel(String unitLabel);
}
