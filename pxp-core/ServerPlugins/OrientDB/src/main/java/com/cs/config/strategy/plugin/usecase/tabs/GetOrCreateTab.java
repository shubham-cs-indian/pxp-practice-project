package com.cs.config.strategy.plugin.usecase.tabs;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.system.ICreateSystemModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetOrCreateTab extends AbstractOrientPlugin {
  
  public GetOrCreateTab(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateTab/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> list = (List<Map<String, Object>>) requestMap.get("list");
    
    for (Map<String, Object> map : list) {
      String id = (String) map.get(ICreateSystemModel.ID);
      try {
        UtilClass.getVertexById(id, VertexLabelConstants.TAB);
        continue; // It is already created. Don't try to create it again
      }
      catch (NotFoundException e) {
        // Do nothing
      }
      Vertex tabNode = TabUtils.createTabNode(map, new ArrayList<String>());
    }
    UtilClass.getGraph()
        .commit();
    return new HashMap<String, Object>();
  }
}
