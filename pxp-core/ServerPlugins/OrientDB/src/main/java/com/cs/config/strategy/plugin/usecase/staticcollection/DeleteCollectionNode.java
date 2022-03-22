package com.cs.config.strategy.plugin.usecase.staticcollection;

import com.cs.config.strategy.plugin.usecase.klass.util.DeleteKlassUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class DeleteCollectionNode extends AbstractOrientPlugin {
  
  public DeleteCollectionNode(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteCollectionNode/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<String> idsToDelete = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    List<String> deletedIds = new ArrayList<>();
    Set<Vertex> nodesToDelete = new HashSet<>();
    Set<Edge> relationshipsToDelete = new HashSet<>();
    
    DeleteKlassUtil.deleteNestedHierarchyWithConnectedNodes(idsToDelete, deletedIds, nodesToDelete,
        relationshipsToDelete, VertexLabelConstants.COLLECTION, new HashSet<>());
    
    /* HashMap<String, Object> relationshipIdsMap = RelationshipUtils
    .getRelationshipsForDeletedKlasses(idsToDelete);*/
    
    for (Edge relationship : relationshipsToDelete) {
      if (relationship != null) {
        relationship.remove();
      }
    }
    for (Vertex node : nodesToDelete) {
      node.remove();
    }
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkDeleteReturnModel.SUCCESS, deletedIds);
    graph.commit();
    return responseMap;
  }
}
