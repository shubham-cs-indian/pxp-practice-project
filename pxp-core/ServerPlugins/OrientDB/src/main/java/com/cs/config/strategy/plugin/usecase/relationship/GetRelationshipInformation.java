package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.model.IRelationshipInformationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

// @SuppressWarnings("unchecked")
public class GetRelationshipInformation extends AbstractOrientPlugin {
  
  public GetRelationshipInformation(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String relationshipId = (String) requestMap.get(IIdParameterModel.ID);
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    try {
      Vertex relationshipNode = UtilClass.getVertexByIndexedId(relationshipId,
          VertexLabelConstants.ROOT_RELATIONSHIP);
      String relationshipType = relationshipNode
          .getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE);
      Map<String, Object> side1Map = relationshipNode.getProperty(IRelationship.SIDE1);
      Map<String, Object> side2Map = relationshipNode.getProperty(IRelationship.SIDE2);
      
      returnMap.put(IRelationshipInformationModel.RELATIONSHIP_ID, relationshipId);
      returnMap.put(IRelationshipInformationModel.RELATIONSHIP_SIDE_1_ID,
          side1Map.get(IRelationshipSide.ID));
      returnMap.put(IRelationshipInformationModel.RELATIONSHIP_SIDE_2_ID,
          side2Map.get(IRelationshipSide.ID));
      returnMap.put(IRelationshipInformationModel.RELATIONSHIP_TYPE, relationshipType);
    }
    catch (NotFoundException e) {
      throw new RelationshipNotFoundException();
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRelationshipInformation/*" };
  }
}
