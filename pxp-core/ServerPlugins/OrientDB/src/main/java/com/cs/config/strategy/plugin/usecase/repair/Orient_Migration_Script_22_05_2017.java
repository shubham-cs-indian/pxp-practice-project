/*
 * (update side property in knr nodes) :
 *
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.Iterator;
import java.util.Map;

public class Orient_Migration_Script_22_05_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_22_05_2017(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    Iterator<Vertex> knrIterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.KLASS_RELATIONSHIP)
        .iterator();
    
    while (knrIterator.hasNext()) {
      Vertex knrNode = knrIterator.next();
      Boolean isNature = knrNode.getProperty(ISectionRelationship.IS_NATURE);
      if (!isNature) {
        continue;
      }
      knrNode.setProperty(CommonConstants.SIDE_PROPERTY, CommonConstants.RELATIONSHIP_SIDE_2);
      Iterable<Vertex> relationshipNodes = knrNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex relationshipNode : relationshipNodes) {
        Iterable<Vertex> krNodes = relationshipNode.getVertices(Direction.IN,
            RelationshipLabelConstants.HAS_PROPERTY);
        for (Vertex krNode : krNodes) {
          if (knrNode.equals(krNode)) {
            continue;
          }
          krNode.setProperty(CommonConstants.SIDE_PROPERTY, CommonConstants.RELATIONSHIP_SIDE_1);
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
    return new String[] { "POST|Orient_Migration_Script_22_05_2017/*" };
  }
}
