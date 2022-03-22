package com.cs.config.strategy.plugin.usecase.textasset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.klass.util.DeleteKlassUtil;
import com.cs.config.strategy.plugin.usecase.references.utils.ReferenceUtils;
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

public class DeleteTextAssets extends AbstractOrientPlugin {
  
  public DeleteTextAssets(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> idsToDelete = new ArrayList<String>();
    idsToDelete = (List<String>) requestMap.get("ids");
    HashMap<String, Object> response = new HashMap<>();

    List<String> deletedIds = new ArrayList<>();
    Set<Vertex> nodesToDelete = new HashSet<>();
    
    Set<Edge> relationshipsToDelete = new HashSet<>();
    for (String id : idsToDelete) {
      Vertex textAssetNode = UtilClass.getVertexByIndexedId(id,
          VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
      AuditLogUtils.fillAuditLoginfo(response, textAssetNode, Entities.CLASSES, Elements.TEXT_ASSET);

      if (textAssetNode != null) {
        Iterator<Edge> iterator = textAssetNode
            .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
            .iterator();
        
        while (iterator.hasNext()) {
          relationshipsToDelete.add(iterator.next());
        }
        String rid = (String) textAssetNode.getId()
            .toString();
        
        Iterable<Vertex> iterable = UtilClass.getGraph()
            .command(new OCommandSQL(
                "select from(traverse in('Child_Of') from " + rid + "  strategy BREADTH_FIRST)"))
            .execute();
        
        for (Vertex childKlassNode : iterable) {
          List<String> selfAndChildTextAssetIds = new ArrayList<>();
          String childKlassIdstring = (String) childKlassNode
              .getProperty(CommonConstants.CODE_PROPERTY);
          deletedIds.add(childKlassIdstring);
          Iterator<Edge> i = childKlassNode
              .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
              .iterator();
          selfAndChildTextAssetIds.add(childKlassIdstring);
          
          while (i.hasNext()) {
            Edge childOfRelations = i.next();
            relationshipsToDelete.add(childOfRelations);
            selfAndChildTextAssetIds.add(childKlassIdstring);
            DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildTextAssetIds, nodesToDelete,
                relationshipsToDelete, VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
          }
          DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildTextAssetIds, nodesToDelete,
              relationshipsToDelete, VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
        }
      }
    }
    
    Map<String, Object> relationshipMap = RelationshipUtils
        .getRelationshipsAndReferencesForKlassIds(VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET,
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
    
    HashMap<String, Object> successMap = new HashMap<>();
    
    successMap.put(IBulkDeleteSuccessKlassResponseModel.IDS, deletedIds);
    successMap.put(IBulkDeleteSuccessKlassResponseModel.DELETED_RELATIONSIP_IDS, deletedRelationshipIds);
    successMap.put(IBulkDeleteSuccessKlassResponseModel.DELETED_NATURE_RELATIONSHIP_IDS,
        deletedNatureRelationshipIds);
    response.put(IBulkDeleteKlassResponseModel.SUCCESS, successMap);
    
    UtilClass.getGraph()
        .commit();
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteTextAssets/*" };
  }
}
