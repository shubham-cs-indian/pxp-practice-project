package com.cs.config.strategy.plugin.usecase.processevent;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.processevent.ProcessEventNotFoundException;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// @SuppressWarnings("unchecked")
public class GetProcessEvent extends AbstractOrientPlugin {
  
  public GetProcessEvent(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    HashMap<String, Object> configMap = new HashMap<String, Object>();
    
    String processEventId = (String) requestMap.get(CommonConstants.ID_PROPERTY);
    
    try {
      Vertex processEventNode = UtilClass.getVertexByIndexedId(processEventId,
          VertexLabelConstants.PROCESS_EVENT);
      returnMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), processEventNode));
      ProcessEventUtils.getProcessEventNodeWithConfigInformation(processEventNode, returnMap,
          configMap);
      ProcessEventUtils.getReferencedDetailsForSearchFilterComponent(returnMap, configMap);
      returnMap.put(IGetProcessEventModel.CONFIG_DETAILS, configMap);
    }
    catch (NotFoundException e) {
      throw new ProcessEventNotFoundException();
    }
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetProcessEvent/*" };
  }
}
