package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * Fix related to update of relationship side labels. Updating relationship side
 * id with random id. This fix won't work for self relationship. Date:
 * 16-09-2016
 */
public class Orient_Migration_Script_16_09_2016 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_16_09_2016(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> response = new HashMap<>();
    
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP))
        .execute();
    // relationshipNode
    for (Vertex relationshipNode : iterable) {
      Map<String, Object> side1 = relationshipNode.getProperty(CommonConstants.RELATIONSHIP_SIDE_1);
      String side1KlassId = (String) side1.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID);
      String side1Id = UtilClass.getUniqueSequenceId(null);
      side1.put(CommonConstants.ID_PROPERTY, side1Id);
      
      Map<String, Object> side2 = relationshipNode.getProperty(CommonConstants.RELATIONSHIP_SIDE_2);
      String side2KlassId = (String) side2.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID);
      String side2Id = UtilClass.getUniqueSequenceId(null);
      side2.put(CommonConstants.ID_PROPERTY, side2Id);
      
      if (!side1KlassId.equals(side2KlassId)) {
        relationshipNode.setProperty(CommonConstants.RELATIONSHIP_SIDE_1, side1);
        relationshipNode.setProperty(CommonConstants.RELATIONSHIP_SIDE_2, side2);
        Iterator<Vertex> sectionElementIterator = relationshipNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
            .iterator();
        Vertex sectionElement = null;
        while (sectionElementIterator.hasNext()) {
          sectionElement = sectionElementIterator.next();
          HashMap<String, Object> relationshipSide = sectionElement
              .getProperty(CommonConstants.RELATIONSHIP_SIDE_PROPERTY);
          if (relationshipSide.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID)
              .equals(side1KlassId)) {
            sectionElement.setProperty(CommonConstants.RELATIONSHIP_SIDE_PROPERTY, side1);
          }
          else if (relationshipSide.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID)
              .equals(side2KlassId)) {
            sectionElement.setProperty(CommonConstants.RELATIONSHIP_SIDE_PROPERTY, side2);
          }
        }
      }
    }
    UtilClass.getGraph()
        .commit();
    response.put("status", "SUCCESS");
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_16_09_2016/*" };
  }
}
