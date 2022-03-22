package com.cs.config.strategy.plugin.usecase.base.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAddedPropertiesDiff extends AbstractOrientPlugin {
  
  public GetAddedPropertiesDiff(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAddedPropertiesDiff/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> collectionIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    List<Map<String, Object>> addedPropertiesDiffList = new ArrayList<>();
    Map<String, Object> mapToReturn = new HashMap<>();
    for (String collectionId : collectionIds) {
      List<String> klassAndChildrenIds = new ArrayList<>();
      klassAndChildrenIds.add(collectionId);
      Vertex collectionNode = UtilClass.getVertexById(collectionId,
          VertexLabelConstants.COLLECTION);
      Iterable<Vertex> klassPropertyNodes = collectionNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      klassPropertyNodes.forEach(klassPropertyNode -> {
        Map<String, Object> addedPropertyMap = new HashMap<>();
        KlassUtils.fillAddedPropertyMap(addedPropertyMap, klassPropertyNode,
            VertexLabelConstants.COLLECTION);
        if (!addedPropertyMap.isEmpty()) {
          addedPropertyMap.put(IDefaultValueChangeModel.KLASS_AND_CHILDRENIDS, klassAndChildrenIds);
          addedPropertiesDiffList.add(addedPropertyMap);
        }
      });
    }
    mapToReturn.put(IListModel.LIST, addedPropertiesDiffList);
    return mapToReturn;
  }
}
