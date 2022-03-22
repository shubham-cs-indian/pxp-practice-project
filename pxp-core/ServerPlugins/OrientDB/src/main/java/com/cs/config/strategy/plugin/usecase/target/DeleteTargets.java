package com.cs.config.strategy.plugin.usecase.target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.klass.util.DeleteKlassUtil;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.interactor.model.klass.IBulkDeleteSuccessKlassResponseModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class DeleteTargets extends AbstractOrientPlugin {
  
  public DeleteTargets(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> idsToDelete = (List<String>) requestMap.get("ids");
    List<Map<String, Object>> auditInfoList = new ArrayList<>();  
    
    List<String> deletedIds = new ArrayList<>();
    Set<Vertex> nodesToDelete = new HashSet<>();
    
    Set<Edge> relationshipsToDelete = new HashSet<>();
    for (String id : idsToDelete) {
      Vertex targetNode = UtilClass.getVertexByIndexedId(id,
          VertexLabelConstants.ENTITY_TYPE_TARGET);
      
      if (targetNode != null) {
        Iterator<Edge> iterator = targetNode
            .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
            .iterator();
        Edge childOfRelationship = null;
        while (iterator.hasNext()) {
          childOfRelationship = iterator.next();
          childOfRelationship.remove();
        }
        String rid = (String) targetNode.getId()
            .toString();
        
        Iterable<Vertex> iterable = UtilClass.getGraph()
            .command(new OCommandSQL(
                "select from(traverse in('Child_Of') from " + rid + "  strategy BREADTH_FIRST)"))
            .execute();
        
        List<String> selfAndChildTargetIds = new ArrayList<>();
        
        for (Vertex childKlassNode : iterable) {
          
          String childKlassIdstring = (String) childKlassNode
              .getProperty(CommonConstants.CODE_PROPERTY);
          deletedIds.add(childKlassIdstring);
          Iterator<Edge> i = childKlassNode
              .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
              .iterator();
          selfAndChildTargetIds.add(childKlassIdstring);
          
          while (i.hasNext()) {
            Edge childOfRelations = i.next();
            relationshipsToDelete.add(childOfRelations);
            selfAndChildTargetIds.add(childKlassIdstring);
            DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildTargetIds, nodesToDelete,
                relationshipsToDelete, VertexLabelConstants.ENTITY_TYPE_TARGET);
          }
          DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildTargetIds, nodesToDelete,
              relationshipsToDelete, VertexLabelConstants.ENTITY_TYPE_TARGET);
        }
        AuditLogUtils.fillAuditLoginfo(auditInfoList, targetNode, Entities.CLASSES, Elements.MARKET);
      }
    }
    
    Map<String, Object> relationshipMap = RelationshipUtils
        .getRelationshipsAndReferencesForKlassIds(VertexLabelConstants.ENTITY_TYPE_TARGET,
            idsToDelete);
    Iterable<Vertex> relationshipVerticesToDelete = (Iterable<Vertex>) relationshipMap
        .get("relationshipsToDelete");
    
    List<String> deletedNatureRelationshipIds = new ArrayList<>();
    List<String> deletedRelationshipIds = new ArrayList<>();
    RelationshipUtils.deleteRelationships(relationshipVerticesToDelete,
        deletedNatureRelationshipIds, deletedRelationshipIds, new ArrayList<>());
    
    for (Edge relationship : relationshipsToDelete) {
      // TODO : Figure out issue and remove condition
      if (relationship != null) {
        relationship.remove();
      }
    }
    for (Vertex node : nodesToDelete) {
      node.remove();
    }
    
    HashMap<String, Object> response = new HashMap<>();
    HashMap<String, Object> successMap = new HashMap<>();
    
    successMap.put(IBulkDeleteSuccessKlassResponseModel.IDS, deletedIds);
    successMap.put(IBulkDeleteSuccessKlassResponseModel.DELETED_NATURE_RELATIONSHIP_IDS,
        deletedNatureRelationshipIds);
    response.put(IBulkDeleteKlassResponseModel.SUCCESS, successMap);
    response.put(IBulkDeleteKlassResponseModel.AUDIT_LOG_INFO, auditInfoList);
    UtilClass.getGraph()
        .commit();
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteTargets/*" };
  }
}
