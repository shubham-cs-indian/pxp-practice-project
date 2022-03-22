package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.klass.util.DeleteKlassUtil;
import com.cs.config.strategy.plugin.usecase.references.utils.ReferenceUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.interactor.model.klass.IBulkDeleteSuccessKlassResponseModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class DeleteKlasses extends AbstractOrientPlugin {
  
  public DeleteKlasses(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<String> idsToDelete = new ArrayList<String>();
    idsToDelete = (List<String>) map.get("ids");
    
    OrientGraph graph = UtilClass.getGraph();
    List<String> deletedIds = new ArrayList<>();
    Set<Vertex> nodesToDelete = new HashSet<>();
    Set<Edge> relationshipEdgesToDelete = new HashSet<>();
    
    Map<String, Object> relationshipMap = RelationshipUtils
        .getRelationshipsAndReferencesForKlassIds(VertexLabelConstants.ENTITY_TYPE_KLASS,
            idsToDelete);
    
    Set<Vertex> klassNodesToDelete = new HashSet<>();
    DeleteKlassUtil.deleteNestedHierarchyWithConnectedNodes(idsToDelete, deletedIds, nodesToDelete,
        relationshipEdgesToDelete, VertexLabelConstants.ENTITY_TYPE_KLASS, klassNodesToDelete);
    
    Iterable<Vertex> relationshipVerticesToDelete = (Iterable<Vertex>) relationshipMap
        .get("relationshipsToDelete");
    
    List<String> deletedNatureRelationshipIds = new ArrayList<>();
    List<String> deletedRelationshipIds = new ArrayList<>();
    RelationshipUtils.deleteRelationships(relationshipVerticesToDelete,
        deletedNatureRelationshipIds, deletedRelationshipIds, new ArrayList<>());
    
    HashMap<String, Object> response = new HashMap<>();
    
    for (Edge relationship : relationshipEdgesToDelete) {
      if (relationship != null) {
        relationship.remove();
      }
    }
    
    for (Vertex node : klassNodesToDelete) {
      AuditLogUtils.fillAuditLoginfo(response, node, Entities.CLASSES,
          Elements.ARTICLE);
      node.remove();
      nodesToDelete.remove(node);
    }
    
    for (Vertex node : nodesToDelete) {
      node.remove();
    }
    
    
    HashMap<String, Object> successMap = new HashMap<>();
    successMap.put(IBulkDeleteSuccessKlassResponseModel.IDS, deletedIds);
    successMap.put(IBulkDeleteSuccessKlassResponseModel.DELETED_RELATIONSIP_IDS,
        deletedRelationshipIds);
    successMap.put(IBulkDeleteSuccessKlassResponseModel.DELETED_NATURE_RELATIONSHIP_IDS,
        deletedNatureRelationshipIds);
    response.put(IBulkDeleteKlassResponseModel.SUCCESS, successMap);
    
    graph.commit();
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteKlasses/*" };
  }
}
