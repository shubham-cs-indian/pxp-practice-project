package com.cs.core.runtime.interactor.model.tabledata;

import com.cs.core.runtime.interactor.model.assetinstance.IMatchAssetValueModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface ITableAssetModel extends IModel {
  
  public static final String ID                    = "id";
  public static final String ATTRIBUTE_ID          = "attributeId";
  public static final String KLASS_INSTANCE_VALUES = "klassInstanceValues";
  public static final String MATCH_VALUES          = "matchValues";
  
  public String getId();
  
  public void setId(String id);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public Map<String, List<String>> getKlassInstanceValues();
  
  public void setKlassInstanceValues(Map<String, List<String>> klassInstances);
  
  public List<IMatchAssetValueModel> getMatchValues();
  
  public void setMatchValues(List<IMatchAssetValueModel> matchValues);
}
