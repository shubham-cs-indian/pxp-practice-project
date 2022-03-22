package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.relationship.util.GetRelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

// @SuppressWarnings("unchecked")
public class GetRootRelationship extends AbstractOrientPlugin {
  
  public GetRootRelationship(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex relationshipNode = null;
    try {
      relationshipNode = UtilClass.getVertexById((String) requestMap.get("id"),
          VertexLabelConstants.ROOT_RELATIONSHIP);
    }
    catch (NotFoundException e) {
      throw new RelationshipNotFoundException();
    }
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    if (relationshipNode != null) {
      Iterable<Vertex> hasPropertyIterator = relationshipNode.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex krNode : hasPropertyIterator) {
        String side = krNode.getProperty(CommonConstants.SIDE_PROPERTY);
        if (side != null && !side.equals("")) {
          Map<String, Object> relationshipSide = krNode
              .getProperty(CommonConstants.RELATIONSHIP_SIDE);
          GetRelationshipUtils.prepareSideMapTranslation(relationshipSide);
          returnMap.put(side, relationshipSide);
        }
      }
    }
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRootRelationship/*" };
  }
}
