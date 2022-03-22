package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
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

@SuppressWarnings("unchecked")
public class GetKlassWithGlobalPermission extends AbstractOrientPlugin {
  
  public GetKlassWithGlobalPermission(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassWithGlobalPermission/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> rootNodeMap = new HashMap<>();
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    Map<String, Object> getKlassMap = new HashMap<>();
    if (map.get(IIdParameterModel.ID)
        .equals("-1")) {
      rootNodeMap.put(CommonConstants.ID_PROPERTY, "-1");
      rootNodeMap.put(CommonConstants.TYPE_PROPERTY, CommonConstants.PROJECT_KLASS_TYPE);
      List<Map<String, Object>> childKlasses = new ArrayList<>();
      rootNodeMap.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
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
      getKlassMap.put(IGetKlassWithGlobalPermissionModel.KLASS, rootNodeMap);
    }
    else {
      try {
        Vertex klassNode = UtilClass.getVertexByIndexedId((String) map.get("id"),
            VertexLabelConstants.ENTITY_TYPE_KLASS);
        getKlassMap = KlassUtils.getKlassEntityReferencesMap(klassNode, false);
        KlassUtils.addGlobalPermission(klassNode, getKlassMap);
        KlassUtils.fillReferencedContextDetails(getKlassMap);
        KlassUtils.fillReferencedTaskDetails(getKlassMap);
        KlassUtils.fillReferencedDataRuleDetails(getKlassMap);
        Map<String, Object> klassMap = (Map<String, Object>) getKlassMap
            .get(IGetKlassWithGlobalPermissionModel.KLASS);
        KlassUtils.fillContextKlassesDetails(getKlassMap, klassMap, klassNode);
        KlassUtils.fillTabDetailsAssociatedWithNatureRelationship(getKlassMap);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
    
    return getKlassMap;
  }
}
