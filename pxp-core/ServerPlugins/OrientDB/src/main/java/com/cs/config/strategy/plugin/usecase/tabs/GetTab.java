package com.cs.config.strategy.plugin.usecase.tabs;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class GetTab extends AbstractOrientPlugin {
  
  public GetTab(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTab/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String tabId = (String) requestMap.get(IIdParameterModel.ID);
    Vertex tabNode = UtilClass.getVertexById(tabId, VertexLabelConstants.TAB);
    Map<String, Object> responseMap = TabUtils.prepareTabResponseMap(tabNode);
    return responseMap;
  }
}
