package com.cs.klass.config.strategy.usecase.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.klass.IGetChildKlassIdsInHierarchyModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetChildKlassIdsInHierarchy extends AbstractOrientPlugin {
  
  public GetChildKlassIdsInHierarchy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetChildKlassIdsInHierarchy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> typeVsklasses = new HashMap<>();
    Set<String> klassTypes = new HashSet<>();

    klassIds.forEach(klassId -> {
      List<Long> klassList = new ArrayList<>();
      typeVsklasses.put(klassId, klassList);
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexByIndexedId(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        klassTypes.add(klassNode.getProperty(IKlass.TYPE));
        String query = "select from (traverse in('"
            + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from " + klassNode.getId()
            + "STRATEGY BREADTH_FIRST)";
        
        Iterable<Vertex> resultKlassNodes = UtilClass.getGraph()
            .command(new OCommandSQL(query))
            .execute();
        
        resultKlassNodes.forEach(childKlassNode -> {
          klassTypes.add(childKlassNode.getProperty(IKlass.TYPE));
          klassList.add(childKlassNode.getProperty("classifierIID"));
        });
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    });
    
    mapToReturn.put(IGetChildKlassIdsInHierarchyModel.TYPE_ID_VS_CHILD_KLASS_IDS, typeVsklasses);
    mapToReturn.put(IGetChildKlassIdsInHierarchyModel.KLASS_TYPES, klassTypes);
    return mapToReturn;
  }
}
