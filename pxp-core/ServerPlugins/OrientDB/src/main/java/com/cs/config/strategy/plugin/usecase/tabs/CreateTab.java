package com.cs.config.strategy.plugin.usecase.tabs;

import java.util.ArrayList;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class CreateTab extends AbstractOrientPlugin {
  
  public CreateTab(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateTab/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex tabNode = TabUtils.createTabNode(requestMap, new ArrayList<String>());
    
    Map<String, Object> responseMap = TabUtils.prepareTabResponseMap(tabNode);
    
    AuditLogUtils.fillAuditLoginfo(responseMap, tabNode, Entities.PROPERTY_GROUPS_MENU_ITEM_TITLE,
        Elements.TABS_MENU_ITEM_TILE);
    
    UtilClass.getGraph()
        .commit();
    
    return responseMap;
  }
}
