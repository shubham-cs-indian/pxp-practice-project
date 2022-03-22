/*
 * (FlatFieldUpdateSectionElementDisabled) : This migration script dated
 * 02-09-2016 is to update all flat field attributes and sections which they
 * contained in Orient so that isDisabled property will be set true.
 *
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.Map;

public class Orient_Migration_Script_Repair_Id_In_Self_Relationships extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_Repair_Id_In_Self_Relationships(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    
    // get all self relationships
    String query = "select from relationship where side1.klassId = side2.klassId";
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> selfRelationships = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex relationshipVertex : selfRelationships) {
      Map<String, Object> relationshipSide1 = relationshipVertex
          .getProperty(CommonConstants.RELATIONSHIP_SIDE_1);
      String randomId = UtilClass.getUniqueSequenceId(null);
      relationshipSide1.put(IRelationshipSide.ID, randomId);
      relationshipVertex.setProperty(CommonConstants.RELATIONSHIP_SIDE_1, relationshipSide1);
      
      System.out.println("\n\n\nrelationship Node repaired " + relationshipVertex);
      
      Iterable<Vertex> krVertices = relationshipVertex.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex krVertex : krVertices) {
        String side = krVertex.getProperty(CommonConstants.SIDE_PROPERTY);
        if (side.equals(CommonConstants.RELATIONSHIP_SIDE_2)) {
          Map<String, Object> relationshipSide = krVertex
              .getProperty(CommonConstants.RELATIONSHIP_SIDE);
          relationshipSide.put(CommonConstants.ID_PROPERTY, randomId);
          krVertex.setProperty(CommonConstants.RELATIONSHIP_SIDE, relationshipSide);
          System.out.println("kr Node repaired " + krVertex);
          System.out.println("id generated " + randomId);
        }
      }
    }
    
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Repair_Id_In_Self_Relationships/*" };
  }
}
