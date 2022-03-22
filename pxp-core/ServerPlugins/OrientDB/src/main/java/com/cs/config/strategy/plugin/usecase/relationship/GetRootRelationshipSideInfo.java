package com.cs.config.strategy.plugin.usecase.relationship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.model.relationship.IConfigDetailsForRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetRootRelationshipSideInfo extends AbstractOrientPlugin {
  
  private static final List<String> fieldsToFetch = Arrays.asList(IConfigEntityInformationModel.ID,
      IConfigEntityInformationModel.CODE, IConfigEntityInformationModel.ICON,
      IConfigEntityInformationModel.LABEL, IConfigEntityInformationModel.TYPE);
  
  public GetRootRelationshipSideInfo(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
 
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex relationshipNode = null;
    HashMap<String, Object> returnMap = new HashMap<>();
    
    try {
      relationshipNode = UtilClass.getVertexById((String) requestMap.get(IIdParameterModel.ID),
          VertexLabelConstants.ROOT_RELATIONSHIP);
    }
    catch (NotFoundException e) {
      throw new RelationshipNotFoundException();
    }
    //prepare side1 and side2 information
    Map<String, Object> relationshipEntityMap = new HashMap<>();
    Map<String, Object> side1 = (Map<String, Object>) relationshipNode
        .getProperty(IRelationship.SIDE1);
    
    Map<String, Object> side2 = (Map<String, Object>) relationshipNode
        .getProperty(IRelationship.SIDE2);
    relationshipEntityMap.put(IRelationship.SIDE1, side1);
    relationshipEntityMap.put(IRelationship.SIDE2, side2);
    returnMap.put(IGetRelationshipModel.ENTITY, relationshipEntityMap);
    
    //Prepare config details for side1 and side2 klass Id.
    Map<String, Object> referencedKlasses = new HashMap<>();
    prepareConfigDetails(side1, referencedKlasses);
    prepareConfigDetails(side2, referencedKlasses);
    
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigDetailsForRelationshipModel.REFERENCED_KLASSES, referencedKlasses);
    returnMap.put(IGetRelationshipModel.CONFIG_DETAILS, configDetails);
    return returnMap;
  }
  
  /**
   * @param side1
   * @param referencedKlasses
   * @throws Exception
   */
  private void prepareConfigDetails(Map<String, Object> side1,
      Map<String, Object> referencedKlasses) throws Exception
  {
    String klassId = (String) side1.get(IRelationshipSide.KLASS_ID);
    Vertex klassVertex = UtilClass.getVertexById(klassId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    Map<String, Object> mapFromVertex = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
    referencedKlasses.put(klassId, mapFromVertex);
  }
  
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRootRelationshipSideInfo/*" };
  }
}
