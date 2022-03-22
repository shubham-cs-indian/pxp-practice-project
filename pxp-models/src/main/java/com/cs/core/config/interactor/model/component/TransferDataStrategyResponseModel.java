package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferDataStrategyResponseModel extends IdsListParameterModel
    implements ITransferDataStrategyResponseModel {
  
  private static final long           serialVersionUID    = 1L;
  protected Map<String, List<String>> instanceKlassIdsMap = new HashMap<>();
  
  @Override
  public Map<String, List<String>> getInstanceKlassIdsMap()
  {
    return instanceKlassIdsMap;
  }
  
  @Override
  public void setInstanceKlassIdsMap(Map<String, List<String>> instanceKlassIdsMap)
  {
    this.instanceKlassIdsMap = instanceKlassIdsMap;
  }
}
