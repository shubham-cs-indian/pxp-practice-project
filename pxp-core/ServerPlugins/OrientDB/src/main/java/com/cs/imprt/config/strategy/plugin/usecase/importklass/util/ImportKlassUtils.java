package com.cs.imprt.config.strategy.plugin.usecase.importklass.util;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.importklass.ImportKlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImportKlassUtils extends KlassUtils {
  
  public static Map<String, Object> getKlassMap(Vertex klassNode, Set<Vertex> linkedKlassNodes)
      throws Exception
  {
    HashMap<String, Object> klassEntityMap = null;
    if (klassNode != null) {
      klassEntityMap = new HashMap<>();
      
      klassEntityMap.putAll(UtilClass.getMapFromNode(klassNode));
      klassEntityMap.put(CommonConstants.NOTIFICATION_SETTINGS,
          new HashMap<String, Map<String, Boolean>>());
      
      OrientVertexType vertexType = ((OrientVertex) klassNode).getType();
      addChildrenInfoToKlassEntityMap(klassNode, klassEntityMap, vertexType.getName());
      KlassUtils.addSectionsToKlassEntityMap(klassNode, klassEntityMap);
      
      addTaxonomySettingToEntityMap(klassNode, klassEntityMap);
      addAllowedTypes(klassNode, klassEntityMap);
      addDataRules(klassNode, klassEntityMap);
    }
    return klassEntityMap;
  }
  
  public static Map<String, Object> getKlassEntityMap(Vertex klassNode) throws Exception
  {
    return KlassUtils.getKlassMap(klassNode, null);
  }
  
  public static HashMap<String, Object> getKlassEntityReferencesMap(Vertex klassNode,
      boolean shouldGetReferencedKlasses) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    Set<Vertex> linkedKlassNodes = null;
    if (shouldGetReferencedKlasses) {
      linkedKlassNodes = new HashSet<>();
    }
    HashMap<String, Object> klassReturnMap = new HashMap<>();
    Map<String, Object> klassEntityMap = getKlassMap(klassNode, linkedKlassNodes);
    
    klassReturnMap.put("klass", klassEntityMap);
    // System.out.println("\n\n ith paryant
    // "+klassEntityMap.get("klassViewSetting"));
    
    // for structure
    
    if (klassEntityMap != null && shouldGetReferencedKlasses) {
      List<Map<String, Object>> referencedKlasses = new ArrayList<>();
      
      for (Vertex referencedKlassNode : linkedKlassNodes) {
        Map<String, Object> linkedKlassMap = new HashMap<>(
            UtilClass.getMapFromNode(referencedKlassNode));
        linkedKlassMap.put(CommonConstants.NOTIFICATION_SETTINGS,
            new HashMap<String, Map<String, Boolean>>());
        KlassUtils.addSectionsToKlassEntityMap(referencedKlassNode, linkedKlassMap);
        referencedKlasses.add(linkedKlassMap);
      }
      klassReturnMap.put("referencedKlasses", referencedKlasses);
    }
    
    return klassReturnMap;
  }
  
  public static Vertex createKlassNode(Map<String, Object> klassMap, OrientVertexType vertexType)
  {
    Vertex klassNode = null;
    if (klassMap.get(CommonConstants.ID_PROPERTY) != null) {
      String klassId = klassMap.get(CommonConstants.ID_PROPERTY)
          .toString();
      klassNode = UtilClass.createNode(klassMap, vertexType);
      klassNode.setProperty(CommonConstants.CODE_PROPERTY, klassId);
    }
    else {
      klassNode = UtilClass.createNode(klassMap, vertexType);
    }
    return klassNode;
  }
  
  public static void createParentChildLink(Vertex klassNode, String vertexType,
      Map<String, Object> klassMap, boolean shouldInherit) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> parentKlassMap = (Map<String, Object>) klassMap.get("parent");
    
    if ((parentKlassMap != null)) {
      String parentId = (String) parentKlassMap.get(CommonConstants.ID_PROPERTY);
      
      if (!parentId.equals("-1")) {
        Vertex parentNode = UtilClass.getVertexByIndexedId(parentId, vertexType);
        /*
         * parentNode.addEdge(RelationshipConstants.ALLOWED_TYPES, klassNode);
         * Iterable<Vertex> parentVertices =
         * parentNode.getVertices(Direction.OUT,
         * RelationshipConstants.RELATIONSHIPLABEL_CHILD_OF); for (Vertex
         * parentVertex : parentVertices) {
         * parentVertex.addEdge(RelationshipConstants.ALLOWED_TYPES, klassNode);
         * }
         */
        String rid = (String) parentNode.getId()
            .toString();
        Iterable<Vertex> i = graph
            .command(new OCommandSQL(
                "select from(traverse out('Child_Of') from " + rid + " strategy DEPTH_FIRST)"))
            .execute();
        for (Vertex node : i) {
          node.addEdge(RelationshipLabelConstants.ALLOWED_TYPES, klassNode);
        }
        if (parentNode == null) {
          throw new ImportKlassNotFoundException();
        }
        klassNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentNode);
        
        if (shouldInherit) {
          inheritParentKlassData(klassNode, parentNode);
        }
      }
    }
  }
  
  public static void inheritParentKlassData(Vertex childKlassNode, Vertex parentKlassNode)
      throws Exception
  {
    inheritSectionsOfParentKlass(childKlassNode, parentKlassNode);
  }
}
