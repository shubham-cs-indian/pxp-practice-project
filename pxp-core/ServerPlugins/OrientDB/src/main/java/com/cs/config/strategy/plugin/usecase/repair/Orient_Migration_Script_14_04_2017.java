/*
 * (update side property in kr nodes) :
 *
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Iterator;
import java.util.Map;

public class Orient_Migration_Script_14_04_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_14_04_2017(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    Iterator<Vertex> relationshipIterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP)
        .iterator();
    while (relationshipIterator.hasNext()) {
      Vertex relationshipNode = relationshipIterator.next();
      Map<String, Object> side1 = relationshipNode.getProperty(CommonConstants.RELATIONSHIP_SIDE_1);
      String klassId = (String) side1.get(IRelationshipSide.KLASS_ID);
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      Vertex kRNode = KlassUtils.getRespectiveKlassPropertyNode(klassNode, relationshipNode);
      kRNode.setProperty(CommonConstants.SIDE_PROPERTY, CommonConstants.RELATIONSHIP_SIDE_1);
      Map<String, Object> side2 = relationshipNode.getProperty(CommonConstants.RELATIONSHIP_SIDE_2);
      String klassId2 = (String) side2.get(IRelationshipSide.KLASS_ID);
      
      // self relationship..
      if (klassId.equals(klassId2)) {
        continue;
      }
      Vertex klassNode2 = null;
      try {
        klassNode2 = UtilClass.getVertexById(klassId2, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      Vertex kRNode2 = KlassUtils.getRespectiveKlassPropertyNode(klassNode2, relationshipNode);
      kRNode2.setProperty(CommonConstants.SIDE_PROPERTY, CommonConstants.RELATIONSHIP_SIDE_2);
    }
    
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_14_04_2017/*" };
  }
}
