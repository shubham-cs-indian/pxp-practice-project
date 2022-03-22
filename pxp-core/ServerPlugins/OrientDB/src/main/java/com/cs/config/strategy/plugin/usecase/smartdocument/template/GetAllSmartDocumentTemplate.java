package com.cs.config.strategy.plugin.usecase.smartdocument.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * Plugin for getting all smartdocument templates
 * 
 * @author jamil.ahmad
 *
 */
public class GetAllSmartDocumentTemplate extends AbstractOrientPlugin {
  
  public GetAllSmartDocumentTemplate(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllSmartDocumentTemplate/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> dataList = new ArrayList<>();
    Map<String, Object> responseMap = new HashMap<>();
    String query = "select *  from " + VertexLabelConstants.SMART_DOCUMENT_TEMPLATE;
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query)).execute();
    for (Vertex iconNode : searchResults) {
      Map<String, Object> dataMap = new HashMap<>();
      dataMap.putAll(UtilClass.getMapFromNode(iconNode));
      dataList.add(dataMap);
    }
    responseMap.put(IListModel.LIST, dataList);
    return responseMap;
  }
}
