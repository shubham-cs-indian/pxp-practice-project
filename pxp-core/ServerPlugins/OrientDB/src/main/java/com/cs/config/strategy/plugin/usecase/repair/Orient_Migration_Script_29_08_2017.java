package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Kshitij
 *         <p>
 *         This migration script checks if a KR or KNR node is attached to more
 *         than one Relationship_Extension nodes. If yes, all the extra
 *         Relationship_Extension nodes are DELETED.
 */
public class Orient_Migration_Script_29_08_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_29_08_2017(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_29_08_2017/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> deletedKRAndKNRIds = new ArrayList<>();
    List<String> kRIds = deleteRelationshipExtensionNodes(VertexLabelConstants.KLASS_RELATIONSHIP);
    deletedKRAndKNRIds.addAll(kRIds);
    List<String> kNRIds = deleteRelationshipExtensionNodes(VertexLabelConstants.KLASS_RELATIONSHIP);
    deletedKRAndKNRIds.addAll(kNRIds);
    return deletedKRAndKNRIds;
  }
  
  private List<String> deleteRelationshipExtensionNodes(String vertexLabel)
  {
    List<String> kRIds = new ArrayList<>();
    // Get all KR nodes where more than 1 Relationship_Extension nodes are
    // attached
    String query = "select from " + vertexLabel + " where outE('"
        + "Has_Relationship_Extension" + "').size() > 1;";
    Iterable<Vertex> kRNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex kRNode : kRNodes) {
      Boolean isNature = kRNode.getProperty(ISectionRelationship.IS_NATURE);
      if (!isNature) {
        continue;
      }
      Iterable<Vertex> relationshipExtensionNodes = kRNode.getVertices(Direction.OUT,
          "Has_Relationship_Extension");
      Iterator<Vertex> iterator = relationshipExtensionNodes.iterator();
      // Skip the first Relationship_Extension node
      if (iterator.hasNext()) {
        iterator.next();
      }
      // Delete remaining Relationship_Extension nodes
      while (iterator.hasNext()) {
        Vertex relationshipExtensionNode = iterator.next();
        relationshipExtensionNode.remove();
        kRIds.add(UtilClass.getCodeNew(kRNode));
      }
    }
    return kRIds;
  }
}
