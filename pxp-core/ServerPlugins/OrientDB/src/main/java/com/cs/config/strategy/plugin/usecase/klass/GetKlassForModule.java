package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetKlassForModule extends AbstractOrientPlugin {
  
  public GetKlassForModule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    Map<String, Object> rootNode = new HashMap<>();
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    Map<String, Object> getKlassMap = null;
    List<Map<String, Object>> childKlasses = new ArrayList<>();
    if (map.get("id")
        .equals("pim")) {
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      rootNode.put(CommonConstants.TYPE_PROPERTY, CommonConstants.PROJECT_KLASS_TYPE);
      childKlasses = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> resultIterable = graph
          .command(new OCommandSQL("select from klass order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      for (Vertex klassNode : resultIterable) {
        childKlasses.add(UtilClass.getMapFromNode(klassNode));
      }
      
      getKlassMap = new HashMap<String, Object>();
      getKlassMap.put("list", childKlasses);
    }
    else if (map.get("id")
        .equals("mam")) {
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      rootNode.put(CommonConstants.TYPE_PROPERTY, CommonConstants.PROJECT_KLASS_TYPE);
      childKlasses = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> resultIterable = graph
          .command(new OCommandSQL("select from asset order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      for (Vertex klassNode : resultIterable) {
        childKlasses.add(UtilClass.getMapFromNode(klassNode));
      }
      getKlassMap = new HashMap<String, Object>();
      getKlassMap.put("list", childKlasses);
    }
    else if (map.get("id")
        .equals("target")) {
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      rootNode.put(CommonConstants.TYPE_PROPERTY, CommonConstants.PROJECT_KLASS_TYPE);
      childKlasses = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> resultIterable = graph
          .command(new OCommandSQL("select from target order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      for (Vertex klassNode : resultIterable) {
        childKlasses.add(UtilClass.getMapFromNode(klassNode));
      }
      getKlassMap = new HashMap<String, Object>();
      getKlassMap.put("list", childKlasses);
    }
    else {
      childKlasses = new ArrayList<>();
      getKlassMap = new HashMap<String, Object>();
      getKlassMap.put("list", childKlasses);
    }
    return getKlassMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassForModule/*" };
  }
  
  protected void getKlassByType(Map<String, Object> rootNode, Map<String, Object> getKlassMap,
      String type, OrientGraph graph, List<Map<String, Object>> childKlasses)
  {
  }
}
