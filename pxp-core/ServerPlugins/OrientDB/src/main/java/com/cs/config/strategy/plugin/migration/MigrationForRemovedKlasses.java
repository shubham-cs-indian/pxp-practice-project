package com.cs.config.strategy.plugin.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.klass.util.DeleteKlassUtil;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class MigrationForRemovedKlasses extends AbstractOrientPlugin {
  
  public MigrationForRemovedKlasses(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|MigrationForRemovedKlasses/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> removedKlassVertexLabel = Arrays.asList("DTPDocumentTemplate", "DTPPublicationTemplate", "DTPProductTemplate",
        "DTPTemplate", "DTPPublication");
    
    // "Makeup" ,"Marketing_Article", "Marketing_Bundle","Marketing_Content",
    // "PresetTemplate"
    OrientGraph graph = UtilClass.getGraph();
    for (String vertexLabel : removedKlassVertexLabel) {
        List<String> ids = new ArrayList<>();
        String query = " select cid from " + vertexLabel;
        Iterable<Vertex> iterable = graph.command(new OCommandSQL(query)).execute();
        for (Vertex vertex : iterable) {
          ids.add(vertex.getProperty("cid"));
        }
        
        if (!ids.isEmpty())
          deleteDTPTemplate(ids, vertexLabel);
   
      }
     
    
    deleteMarketingContent();
    deleteCampaign();
    
    String query = "select cid from Target where"
        + " type ='com.cs.config.interactor.entity.concrete.klass.Persona' or type ='com.cs.config.interactor.entity.concrete.klass.Situation'";
    
    List<String> ids = new ArrayList<>();
    Iterable<Vertex> iterable = graph.command(new OCommandSQL(query)).execute();
    for (Vertex vertex : iterable) {
      ids.add(vertex.getProperty("cid"));
    }
    
    if (!ids.isEmpty())
      deleteKlasses("Target", ids);
    
    query = "select cid from Promotion";
    ids = new ArrayList<>();
    iterable = graph.command(new OCommandSQL(query)).execute();
    for (Vertex vertex : iterable) {
      ids.add(vertex.getProperty("cid"));
    }
    if (!ids.isEmpty())
      deleteKlasses("Promotion", ids);
    
    query = "delete vertex Indesign_Server";
    graph.command(new OCommandSQL(query)).execute();
    query = " delete vertex Indesign_Load_Balancer";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "delete vertex Reference";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    query = "delete vertex Klass_Reference";
    graph.command(new OCommandSQL(query)).execute();
    graph.commit();
    
    UtilClass.getDatabase().commit();
    
    query = "drop class Reference ";
    graph.command(new OCommandSQL(query)).execute();
    
    query = "drop class Klass_Reference ";
    graph.command(new OCommandSQL(query)).execute();
    query = "drop class Makeup ";
    graph.command(new OCommandSQL(query)).execute();
    
    query = "drop class Marketing_Article ";
    graph.command(new OCommandSQL(query)).execute();
    
    query = "drop class PromotionalBundle ";
    graph.command(new OCommandSQL(query)).execute();
    
    query = "drop class PromotionalSet ";
    graph.command(new OCommandSQL(query)).execute();
    
    query = "drop class PromotionalComposition ";
    graph.command(new OCommandSQL(query)).execute();
    
    query = "drop class Marketing_Bundle ";
    graph.command(new OCommandSQL(query)).execute();

    query = "drop class PresetTemplate ";
    graph.command(new OCommandSQL(query)).execute();
    
    query = "drop class Marketing_Content ";
    graph.command(new OCommandSQL(query)).execute();
    
    query = "drop class Channel_Campaign ";
    graph.command(new OCommandSQL(query)).execute();

    query = "drop class Marketing_Program_Campaign ";
    graph.command(new OCommandSQL(query)).execute();
    
    query = "drop class Campaign ";
    graph.command(new OCommandSQL(query)).execute();
    
    query = "drop class Promotion ";
    graph.command(new OCommandSQL(query)).execute();
   
    for(String vertexLabel: removedKlassVertexLabel) {
      query = "drop class " + vertexLabel;
      graph.command(new OCommandSQL(query)).execute();
    }
    
    return null;
  }
  
  public Object deleteKlasses(String vertexConstant, List<String> ids) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<String> deletedIds = new ArrayList<>();
    Set<Vertex> nodesToDelete = new HashSet<>();
    Set<Edge> relationshipEdgesToDelete = new HashSet<>();
    
    Map<String, Object> relationshipMap = RelationshipUtils.getRelationshipsAndReferencesForKlassIds(vertexConstant, ids);
    
    Set<Vertex> klassNodesToDelete = new HashSet<>();
    DeleteKlassUtil.deleteNestedHierarchyWithConnectedNodes(ids, deletedIds, nodesToDelete, relationshipEdgesToDelete, vertexConstant,
        klassNodesToDelete);
    
    Iterable<Vertex> relationshipVerticesToDelete = (Iterable<Vertex>) relationshipMap.get("relationshipsToDelete");
    
    List<String> deletedNatureRelationshipIds = new ArrayList<>();
    List<String> deletedRelationshipIds = new ArrayList<>();
    RelationshipUtils.deleteRelationships(relationshipVerticesToDelete, deletedNatureRelationshipIds, deletedRelationshipIds,
        new ArrayList<>());
    
    HashMap<String, Object> response = new HashMap<>();
    
    for (Edge relationship : relationshipEdgesToDelete) {
      if (relationship != null) {
        relationship.remove();
      }
    }
    
    for (Vertex node : nodesToDelete) {
      node.remove();
    }
    graph.commit();
    return response;
  }
  
  public void deleteDTPTemplate(List<String> idsToDelete, String vertexName) throws Exception
  {
    
    List<String> deletedIds = new ArrayList<>();
    Set<Vertex> nodesToDelete = new HashSet<>();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    HashMap<String, Object> response = new HashMap<>();
    
    String dtpPublicationType = vertexName;
    Set<Edge> relationshipsToDelete = new HashSet<>();
    for (String id : idsToDelete) {
      Vertex dtpPublicationNode = UtilClass.getVertexByIndexedId(id, dtpPublicationType);
      Iterator<Edge> iterator = dtpPublicationNode.getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
          .iterator();
      
      while (iterator.hasNext()) {
        relationshipsToDelete.add(iterator.next());
      }
      String rid = (String) dtpPublicationNode.getId().toString();
      
      Iterable<Vertex> iterable = UtilClass.getGraph().command(new OCommandSQL("select from(traverse in('"
          + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from " + rid + "  strategy BREADTH_FIRST)")).execute();
      
      for (Vertex childKlassNode : iterable) {
        List<String> selfAndChildDtpTemplatesIds = new ArrayList<>();
        String childKlassIdstring = (String) childKlassNode.getProperty(CommonConstants.CID_PROPERTY);
        deletedIds.add(childKlassIdstring);
        
        Iterator<Edge> relationshipIterator = childKlassNode.getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
            .iterator();
        selfAndChildDtpTemplatesIds.add(childKlassIdstring);
        
        while (relationshipIterator.hasNext()) {
          Edge childOfRelations = relationshipIterator.next();
          relationshipsToDelete.add(childOfRelations);
          selfAndChildDtpTemplatesIds.add(childKlassIdstring);
          DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildDtpTemplatesIds, nodesToDelete, relationshipsToDelete,
              dtpPublicationType);
        }
        DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildDtpTemplatesIds, nodesToDelete, relationshipsToDelete,
            dtpPublicationType);
      }
    }
    
    Map<String, Object> relationshipMap = RelationshipUtils.getRelationshipsAndReferencesForKlassIds(dtpPublicationType, idsToDelete);
    Iterable<Vertex> relationshipVerticesToDelete = (Iterable<Vertex>) relationshipMap.get("relationshipsToDelete");
    
    List<String> deletedNatureRelationshipIds = new ArrayList<>();
    List<String> deletedRelationshipIds = new ArrayList<>();
    RelationshipUtils.deleteRelationships(relationshipVerticesToDelete, deletedNatureRelationshipIds, deletedRelationshipIds,
        new ArrayList<>());
    
    for (Edge relationship : relationshipsToDelete) {
      // TODO : Figure out issue and remove condition
      if (relationship != null) {
        relationship.remove();
      }
    }
    
    for (Vertex node : nodesToDelete) {
      node.remove();
    }
    
    UtilClass.getGraph().commit();
  }
  
  public void deleteMarketingContent() throws Exception
  {
    List<String> ids = new ArrayList<>();
    String query = " select cid from " + "Marketing_Content";
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> iterable1 = graph.command(new OCommandSQL(query)).execute();
    for (Vertex vertex : iterable1) {
      ids.add(vertex.getProperty("cid"));
    }
    
    List<String> deletedIds = new ArrayList<>();
    Set<Vertex> nodesToDelete = new HashSet<>();
    if (ids.isEmpty())
      return;
    Set<Edge> relationshipsToDelete = new HashSet<>();
    for (String id : ids) {
      Vertex marketingContentNode = UtilClass.getVertexByIndexedId(id, "Marketing_Content");
      if (marketingContentNode != null) {
        Iterator<Edge> iterator = marketingContentNode.getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
            .iterator();
        
        while (iterator.hasNext()) {
          relationshipsToDelete.add(iterator.next());
        }
        String rid = (String) marketingContentNode.getId().toString();
        
        Iterable<Vertex> iterable = UtilClass.getGraph().command(new OCommandSQL("select from(traverse in('"
            + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from " + rid + "  strategy BREADTH_FIRST)")).execute();
        
        for (Vertex childKlassNode : iterable) {
          List<String> selfAndChildMarketingContentIds = new ArrayList<>();
          String childKlassIdstring = (String) childKlassNode.getProperty(CommonConstants.CID_PROPERTY);
          deletedIds.add(childKlassIdstring);
          
          Iterator<Edge> relationshipIterator = childKlassNode.getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
              .iterator();
          selfAndChildMarketingContentIds.add(childKlassIdstring);
          
          while (relationshipIterator.hasNext()) {
            Edge childOfRelations = relationshipIterator.next();
            relationshipsToDelete.add(childOfRelations);
            selfAndChildMarketingContentIds.add(childKlassIdstring);
            DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildMarketingContentIds, nodesToDelete, relationshipsToDelete,
                "Marketing_Content");
          }
          DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildMarketingContentIds, nodesToDelete, relationshipsToDelete,
              "Marketing_Content");
        }
      }
    }
    
    Map<String, Object> relationshipMap = RelationshipUtils.getRelationshipsAndReferencesForKlassIds("Marketing_Content", ids);
    Iterable<Vertex> relationshipVerticesToDelete = (Iterable<Vertex>) relationshipMap.get("relationshipsToDelete");
    
    List<String> deletedNatureRelationshipIds = new ArrayList<>();
    List<String> deletedRelationshipIds = new ArrayList<>();
    RelationshipUtils.deleteRelationships(relationshipVerticesToDelete, deletedNatureRelationshipIds, deletedRelationshipIds,
        new ArrayList<>());
    
    for (Edge relationship : relationshipsToDelete) {
      if (relationship != null) {
        relationship.remove();
      }
    }
    for (Vertex node : nodesToDelete) {
      node.remove();
    }
 
    graph.commit();
    
  }
  
  // delete data from vertex "Campaign" "Marketing_Program_Campaign"
  // "Channel_Campaign"
  public void deleteCampaign() throws Exception
  {
    List<String> ids = new ArrayList<>();
    String query = " select cid from Campaign";
    Iterable<Vertex> iterable1 = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    for (Vertex vertex : iterable1) {
      ids.add(vertex.getProperty("cid"));
    }
    
    if (ids.isEmpty())
      return;
    
    OrientGraph graph = UtilClass.getGraph();
    List<String> deletedIds = new ArrayList<>();
    Set<Vertex> nodesToDelete = new HashSet<>();
    
    Set<Edge> relationshipsToDelete = new HashSet<>();
    for (String id : ids) {
      Vertex campaignNode = UtilClass.getVertexByIndexedId(id, "Campaign");
      Iterator<Edge> iterator = campaignNode.getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF).iterator();
      
      while (iterator.hasNext()) {
        relationshipsToDelete.add(iterator.next());
      }
      String rid = (String) campaignNode.getId().toString();
      
      Iterable<Vertex> iterable = graph
          .command(new OCommandSQL("select from(traverse in('Child_Of') from " + rid + "  strategy BREADTH_FIRST)")).execute();
      
      for (Vertex childKlassNode : iterable) {
        List<String> selfAndChildCampaignIds = new ArrayList<>();
        String childKlassIdstring = (String) childKlassNode.getProperty(CommonConstants.CID_PROPERTY);
        deletedIds.add(childKlassIdstring);
        Iterator<Edge> i = childKlassNode.getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF).iterator();
        selfAndChildCampaignIds.add(childKlassIdstring);
        
        while (i.hasNext()) {
          Edge childOfRelations = i.next();
          relationshipsToDelete.add(childOfRelations);
          selfAndChildCampaignIds.add(childKlassIdstring);
          DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildCampaignIds, nodesToDelete, relationshipsToDelete, "Campaign");
        }
        DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildCampaignIds, nodesToDelete, relationshipsToDelete, "Campaign");
      }
    }
    
    Map<String, Object> relationshipMap = RelationshipUtils.getRelationshipsAndReferencesForKlassIds("Campaign", ids);
    
    Iterable<Vertex> relationshipVerticesToDelete = (Iterable<Vertex>) relationshipMap.get("relationshipsToDelete");
    
    List<String> deletedNatureRelationshipIds = new ArrayList<>();
    List<String> deletedRelationshipIds = new ArrayList<>();
    RelationshipUtils.deleteRelationships(relationshipVerticesToDelete, deletedNatureRelationshipIds, deletedRelationshipIds,
        new ArrayList<>());
    
    List<String> natureReferenceIds = new ArrayList<>();
    List<String> referenceIds = new ArrayList<>();
    
    for (Edge relationship : relationshipsToDelete) {
      // TODO : Figure out issue and remove condition
      if (relationship != null) {
        relationship.remove();
      }
    }
    for (Vertex node : nodesToDelete) {
      node.remove();
    }
    graph.commit();
  }
  
}
