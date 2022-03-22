package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Kshitij
 *         <p>
 *         This migration script checks if an extension klass has been selected
 *         for a relationship or nature relationship. If it is not, it will get
 *         all the KR & KNR nodes of this relationship and will DELETE all the
 *         Relationship_Extension nodes attached to it.
 */
public class Orient_Migration_Script_Relationship_Extension_Delete extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_Relationship_Extension_Delete(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Relationship_Extension_Delete/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> relationshipIds = new ArrayList<>();
    // Get all relationship & nature relationship nodes
    Iterable<Vertex> relationshipNodes = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ROOT_RELATIONSHIP);
    for (Vertex relationshipNode : relationshipNodes) {
      // Get extension klass node linked to this relationship node
      Iterable<Vertex> klassNodes = relationshipNode.getVertices(Direction.OUT,
          "Has_Extension");
      if (!klassNodes.iterator()
          .hasNext()) { // If there is no extension klass linked to this
        // relationship node
        // Get all KR or KNR nodes for this relationship node
        Iterable<Vertex> kRNodes = relationshipNode.getVertices(Direction.IN,
            RelationshipLabelConstants.HAS_PROPERTY);
        for (Vertex kRNode : kRNodes) {
          // Get all Relationship_Extension nodes for this KR node
          Iterable<Vertex> relationshipExtensionNodes = kRNode.getVertices(Direction.OUT,
              "Has_Relationship_Extension");
          // Delete all Relationship_Extension nodes
          for (Vertex relationshipExtensionNode : relationshipExtensionNodes) {
            relationshipExtensionNode.remove();
            relationshipIds.add(UtilClass.getCodeNew(relationshipNode));
          }
        }
      }
    }
    UtilClass.getGraph()
        .commit();
    return relationshipIds;
  }
}
