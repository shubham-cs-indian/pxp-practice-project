package com.cs.config.strategy.plugin.usecase.repair;

/*
 * Author: Arshad Creates collection nodes for all static collections present in
 * elastic. Input: static collection ids from elastic
 */

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.collections.ICollectionNodeModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orient_Migration_Script_20_04_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_20_04_2017(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings({ "unchecked", "unused" })
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Map<String, Object> returnMap = null;
    List<String> collectionIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    
    returnMap = new HashMap<String, Object>();
    List<String> successIds = new ArrayList<String>();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.COLLECTION,
        CommonConstants.CODE_PROPERTY);
    for (String collectionId : collectionIds) {
      Map<String, Object> collectionMap = new HashMap<String, Object>();
      collectionMap.put(ICollectionNodeModel.ID, collectionId);
      Vertex s = UtilClass.createNode(collectionMap, vertexType, new ArrayList<>());
      successIds.add(collectionId);
    }
    
    UtilClass.getGraph()
        .commit();
    returnMap.put(IIdsListParameterModel.IDS, successIds);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_20_04_2017/*" };
  }
}
