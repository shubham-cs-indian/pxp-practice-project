package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllowedTypes extends AbstractOrientPlugin {
  
  public GetAllowedTypes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map = (HashMap<String, Object>) requestMap.get("map");
    OrientGraph graph = UtilClass.getGraph();
    
    List<String> allowedTypes = new ArrayList<String>();
    String mode = (String) map.get("mode");
    String standardKlassId = (String) map.get("standardKlassId");
    String nodeLabel = getNodelLabel(mode);
    
    Vertex rootNode = UtilClass.getVertexById(standardKlassId, nodeLabel);
    String rid = (String) rootNode.getId()
        .toString();
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where (isAbstract = \"false\" or isAbstract is null) ";
    query += "order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
        + " asc";
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNode : resultIterable) {
      allowedTypes.add(klassNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    Map<String, Object> listModel = new HashMap<>();
    listModel.put("list", allowedTypes);
    
    return listModel;
  }
  
  protected String getNodelLabel(String mode)
  {
    String nodeLabel = null;
    switch (mode) {
      case "klass":
        nodeLabel = VertexLabelConstants.ENTITY_TYPE_KLASS;
        break;
      case "asset":
        nodeLabel = VertexLabelConstants.ENTITY_TYPE_ASSET;
        break;
      case "target":
        nodeLabel = VertexLabelConstants.ENTITY_TYPE_TARGET;
        break;
      case "textasset":
        nodeLabel = VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
        break;
      case "supplier":
        nodeLabel = VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
        break;
      default:
        break;
    }
    return nodeLabel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllowedTypes/*" };
  }
}
