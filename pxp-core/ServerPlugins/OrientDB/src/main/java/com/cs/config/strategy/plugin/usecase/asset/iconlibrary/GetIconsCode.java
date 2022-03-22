package com.cs.config.strategy.plugin.usecase.asset.iconlibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetIconsCode extends AbstractOrientPlugin {
  
  public GetIconsCode(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    List<String> iconList = new ArrayList<>();
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ICON ;
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query)).execute();
    
    for (Vertex iconNode : searchResults) {
      iconList.add((String) UtilClass.getMapFromNode(iconNode).get(CommonConstants.CODE_PROPERTY));
    }
    returnMap.put("code", iconList);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetIconsCode/*" };
  }
}
