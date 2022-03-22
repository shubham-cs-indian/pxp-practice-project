package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

import java.util.List;
import java.util.Map;

public interface ITransferDataStrategyResponseModel extends IIdsListParameterModel {
  
  public static final String INSTANCE_KLASS_IDS_MAP = "instanceKlassIdsMap";
  
  public Map<String, List<String>> getInstanceKlassIdsMap();
  
  public void setInstanceKlassIdsMap(Map<String, List<String>> instanceKlassIdsMap);
}
