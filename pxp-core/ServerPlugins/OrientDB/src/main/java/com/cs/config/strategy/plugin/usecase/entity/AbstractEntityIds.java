package com.cs.config.strategy.plugin.usecase.entity;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEntityIds extends AbstractOrientPlugin {
  
  public abstract List<String> getList();
  
  public AbstractEntityIds(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
    returnMap.put(IIdsListParameterModel.IDS, getList());
    return returnMap;
  }
}
