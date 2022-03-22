package com.cs.config.strategy.plugin.usecase.target;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.target.util.TargetUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTargetWithoutKP extends AbstractOrientPlugin {
  
  public GetTargetWithoutKP(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTargetWithoutKP/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> rootNodeMap = new HashMap<>();
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> returnMap = new HashMap<>();
    String klassId = (String) requestMap.get(IIdParameterModel.ID);
    
    if (klassId.equals("-1")) {
      List<Map<String, Object>> childKlasses = new ArrayList<>();
      rootNodeMap.put(CommonConstants.ID_PROPERTY, "-1");
      rootNodeMap.put(CommonConstants.TYPE_PROPERTY, CommonConstants.MARKET_KLASS_TYPE);
      rootNodeMap.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> i = graph
          .command(new OCommandSQL("SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_TARGET
              + " where outE('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
              + "').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex klassNode : i) {
        HashMap<String, Object> klassEntityMap = new HashMap<String, Object>();
        klassEntityMap.putAll(UtilClass.getMapFromNode(klassNode));
        childKlasses.add(klassEntityMap);
      }
      returnMap.put(IGetKlassEntityWithoutKPModel.ENTITY, rootNodeMap);
    }
    else {
      Vertex targetNode = null;
      try {
        targetNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_TARGET);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      rootNodeMap = TargetUtils.getTargetEntityMap(targetNode, false);
      returnMap.put(IGetKlassEntityWithoutKPModel.ENTITY, rootNodeMap);
      KlassGetUtils.fillReferencedConfigDetails(returnMap, targetNode);
    }
    return returnMap;
  }
}
