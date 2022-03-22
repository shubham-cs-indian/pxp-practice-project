/*
 * (RepairTreeTypeOptionData) : This migration script dated 26-08-2016 is to
 * update all klass nodes to remove property tree type option and create n link
 * vertex for the same..
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Orient_Migration_Script_26_08_2016 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_26_08_2016(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getOrCreateVertexType(VertexLabelConstants.TREE_TYPE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.TREE_TYPE_OPTION_LINK,
        CommonConstants.CODE_PROPERTY);
    
    Map<String, Object> response = new HashMap<>();
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.TREE_TYPE);
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      Iterable<Vertex> klassVertices = UtilClass.getGraph()
          .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_KLASS);
      for (Vertex klassVertex : klassVertices) {
        String treeTypeOption = klassVertex
            .removeProperty(CommonConstants.TREE_TYPE_OPTION_PROPERTY);
        linkKlassToNewTreeTypeVertex(treeTypeOption, klassVertex);
      }
      
      Iterable<Vertex> assetVertices = UtilClass.getGraph()
          .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ASSET);
      for (Vertex assetVertex : assetVertices) {
        String treeTypeOption = assetVertex
            .removeProperty(CommonConstants.TREE_TYPE_OPTION_PROPERTY);
        linkKlassToNewTreeTypeVertex(treeTypeOption, assetVertex);
      }
      
      Iterable<Vertex> targetVertices = UtilClass.getGraph()
          .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TARGET);
      for (Vertex targetVertex : targetVertices) {
        String treeTypeOption = targetVertex
            .removeProperty(CommonConstants.TREE_TYPE_OPTION_PROPERTY);
        linkKlassToNewTreeTypeVertex(treeTypeOption, targetVertex);
      }
      
      Iterable<Vertex> taskVertices = UtilClass.getGraph()
          .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK);
      for (Vertex taskVertex : taskVertices) {
        String treeTypeOption = taskVertex
            .removeProperty(CommonConstants.TREE_TYPE_OPTION_PROPERTY);
        linkKlassToNewTreeTypeVertex(treeTypeOption, taskVertex);
      }
      
      UtilClass.getGraph()
          .commit();
      
      response.put("status", "SUCCESS");
    }
    else {
      response.put("status", "ALREADY REPAIRED");
    }
    return response;
  }
  
  public static void linkKlassToNewTreeTypeVertex(String newTreeTypeOption, Vertex klassNode)
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.TREE_TYPE,
        CommonConstants.CODE_PROPERTY);
    Map<String, Object> treeTypeMap = new HashMap<String, Object>();
    treeTypeMap.put(CommonConstants.TREE_TYPE_OPTION_PROPERTY, newTreeTypeOption);
    
    Vertex treeTypeNode = UtilClass.createNode(treeTypeMap, vertexType);
    Edge treeTypeOptionLink = klassNode.addEdge(RelationshipLabelConstants.TREE_TYPE_OPTION_LINK,
        treeTypeNode);
    treeTypeOptionLink.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_26_08_2016/*" };
  }
}
