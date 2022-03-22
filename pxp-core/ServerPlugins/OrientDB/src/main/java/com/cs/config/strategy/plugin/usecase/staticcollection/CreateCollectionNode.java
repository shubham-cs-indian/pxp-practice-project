package com.cs.config.strategy.plugin.usecase.staticcollection;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.staticcollection.CollectionNodeNotFoundException;
import com.cs.core.config.interactor.model.collections.ICollectionNodeModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateCollectionNode extends AbstractOrientPlugin {
  
  public CreateCollectionNode(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateCollectionNode/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String parentId = (String) requestMap.get(ICollectionNodeModel.PARENT_ID);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.COLLECTION,
        CommonConstants.CODE_PROPERTY);
    Map<String, Object> collectionMap = new HashMap<>();
    collectionMap.put(CommonConstants.ID_PROPERTY, requestMap.get(ICollectionNodeModel.ID));
    collectionMap.put(ICollectionNodeModel.LABEL, requestMap.get(ICollectionNodeModel.LABEL));
    List<String> fieldsToExclude = new ArrayList<>();
    // fieldsToExclude.add(ICollectionNodeModel.LABEL);
    Vertex collectionNode = UtilClass.createNode(collectionMap, vertexType, fieldsToExclude);
    // If parent exists create link between parent & child
    if (parentId != null && !parentId.equals("-1")) {
      Vertex parentNode = null;
      try {
        parentNode = UtilClass.getVertexById(parentId, VertexLabelConstants.COLLECTION);
      }
      catch (NotFoundException e) {
        throw new CollectionNodeNotFoundException();
      }
      
      collectionNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentNode);
      KlassUtils.inheritParentKlassData(collectionNode, parentNode);
    }
    graph.commit();
    Map<String, Object> returnMap = new HashMap<>();
    returnMap = UtilClass.getMapFromVertex(Arrays.asList(CommonConstants.ID_PROPERTY),
        collectionNode);
    return returnMap;
  }
}
