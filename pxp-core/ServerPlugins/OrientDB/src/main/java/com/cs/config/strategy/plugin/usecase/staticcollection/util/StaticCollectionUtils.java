package com.cs.config.strategy.plugin.usecase.staticcollection.util;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionInfoModel;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StaticCollectionUtils {
  
  public static void getStaticCollectionDetailsMap(Vertex collectionNode,
      Map<String, Object> returnMap) throws Exception
  {
    Iterable<Vertex> childVertices = collectionNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    List<String> ids = new ArrayList<>();
    for (Vertex childNode : childVertices) {
      ids.add(childNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    KlassUtils.addSectionsToKlassEntityMap(collectionNode, returnMap, false);
    returnMap.put(IGetStaticCollectionInfoModel.CHILDREN_IDS, ids);
  }
}
