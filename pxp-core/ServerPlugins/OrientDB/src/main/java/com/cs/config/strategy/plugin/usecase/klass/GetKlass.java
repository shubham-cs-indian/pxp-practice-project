package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
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

public class GetKlass extends AbstractOrientPlugin {
  
  public GetKlass(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    Map<String, Object> rootNode = new HashMap<>();
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> getKlassMap = null;
    if (map.get("id")
        .equals("-1")) {
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      rootNode.put(CommonConstants.TYPE_PROPERTY, CommonConstants.PROJECT_KLASS_TYPE);
      List<Map<String, Object>> childKlasses = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> i = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_KLASS
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex klassNode : i) {
        HashMap<String, Object> klassEntityMap = new HashMap<String, Object>();
        klassEntityMap.putAll(UtilClass.getMapFromNode(klassNode));
        childKlasses.add(klassEntityMap);
      }
      getKlassMap = new HashMap<String, Object>();
      getKlassMap.put("klass", rootNode);
    }
    else {
      try {
        Vertex klassNode = UtilClass.getVertexByIndexedId((String) map.get("id"),
            VertexLabelConstants.ENTITY_TYPE_KLASS);
        getKlassMap = KlassUtils.getKlassEntityReferencesMap(klassNode, false);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
    
    return getKlassMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlass/*" };
  }
}
