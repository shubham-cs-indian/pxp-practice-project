/*
 * (SetStandardAttributesDisabled) : This migration script dated 30-08-2016 is
 * to update flat field attributes to isDisabled true.
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orient_Migration_Script_30_08_2016 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_30_08_2016(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    HashMap<String, Object> response = new HashMap<>();
    List<String> attributeIdsToUpdate = new ArrayList<>();
    attributeIdsToUpdate.add("createdbyattribute");
    attributeIdsToUpdate.add("createdonattribute");
    attributeIdsToUpdate.add("lastmodifiedattribute");
    attributeIdsToUpdate.add("lastmodifiedbyattribute");
    
    for (String attributeId : attributeIdsToUpdate) {
      Vertex attributeVertex = UtilClass.getVertexById(attributeId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      
      Iterable<Vertex> sectionElementVertices = attributeVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
      for (Vertex sectionElementVertex : sectionElementVertices) {
        sectionElementVertex.setProperty("isDisabled", true);
      }
    }
    
    UtilClass.getGraph()
        .commit();
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
    return new String[] { "POST|Orient_Migration_Script_30_08_2016/*" };
  }
}
