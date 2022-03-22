package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
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

public class GetKlassWithoutKP extends AbstractOrientPlugin {
  
  public GetKlassWithoutKP(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassWithoutKP/*" };
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
      rootNodeMap.put(IKlass.ID, "-1");
      rootNodeMap.put(IKlass.TYPE, CommonConstants.PROJECT_KLASS_TYPE);
      rootNodeMap.put(IKlass.CHILDREN, childKlasses);
      
      Iterable<Vertex> klassesIterable = graph
          .command(new OCommandSQL("SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_KLASS
              + " where outE('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
              + "').size() = 0 " + " and " + CommonConstants.CODE_PROPERTY + " not in "
              + EntityUtil.quoteIt(SystemLevelIds.KLASSES_TO_EXCLUDE_FROM_CONFIG_SCREEN)
              + " order by " + EntityUtil.getLanguageConvertedField(IKlass.LABEL) + " asc"))
          .execute();
      
      for (Vertex klassVertex : klassesIterable) {
        HashMap<String, Object> klassEntityMap = new HashMap<String, Object>();
        klassEntityMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), klassVertex));
        childKlasses.add(klassEntityMap);
      }
      returnMap.put(IGetKlassEntityWithoutKPModel.ENTITY, rootNodeMap);
    }
    else {
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexByIndexedId(klassId, VertexLabelConstants.ENTITY_TYPE_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      returnMap = KlassGetUtils.getKlassEntityReferencesMap(klassNode, false);
      KlassGetUtils.fillReferencedConfigDetails(returnMap, klassNode);
    }
    return returnMap;
  }
}
