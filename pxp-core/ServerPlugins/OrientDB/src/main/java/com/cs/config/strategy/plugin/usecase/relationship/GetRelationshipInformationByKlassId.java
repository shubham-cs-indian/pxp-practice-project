package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.IRelationshipInformationModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetRelationshipInformationByKlassId extends AbstractOrientPlugin {
  
  public GetRelationshipInformationByKlassId(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<>();
    
    String klassId = (String) requestMap.get(IIdParameterModel.ID);
    
    Vertex klassNode = UtilClass.getVertexById(klassId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    
    Iterable<Vertex> kNRNodes = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    
    Iterable<Vertex> kRNodes = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    
    List<Map<String, Object>> relationshipInfoList = new ArrayList<>();
    
    for (Vertex kNRNode : kNRNodes) {
      Map<String, Object> kNRMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, CommonConstants.RELATIONSHIP_SIDE),
          kNRNode);
      String sourceSideId = (String) kNRMap.get(CommonConstants.ID_PROPERTY);
      Map<String, Object> relationshipSide = (Map<String, Object>) kNRMap
          .get(CommonConstants.RELATIONSHIP_SIDE);
      String targetSideId = (String) relationshipSide
          .get(IKlassRelationshipSide.TARGET_RELATIONSHIP_MAPPING_ID);
      
      for (Vertex nRNode : kNRNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_PROPERTY)) {
        Map<String, Object> nRMap = UtilClass
            .getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY, IRelationship.SIDE1,
                IRelationship.SIDE2, IKlassNatureRelationship.RELATIONSHIP_TYPE), nRNode);
        String relationshipId = (String) nRMap.get(IRelationship.ID);
        Map<String, Object> side1Map = (Map<String, Object>) nRMap.get(IRelationship.SIDE1);
        Map<String, Object> side2Map = (Map<String, Object>) nRMap.get(IRelationship.SIDE2);
        
        Map<String, Object> relatinshipInfo = new HashMap<>();
        
        relatinshipInfo.put(IRelationshipInformationModel.RELATIONSHIP_ID, relationshipId);
        relatinshipInfo.put(IRelationshipInformationModel.RELATIONSHIP_SIDE_1_ID,
            side1Map.get(IRelationshipSide.KLASS_ID));
        relatinshipInfo.put(IRelationshipInformationModel.RELATIONSHIP_SIDE_2_ID,
            side2Map.get(IRelationshipSide.KLASS_ID));
        relatinshipInfo.put(IRelationshipInformationModel.RELATIONSHIP_TYPE,
            nRMap.get(IKlassNatureRelationship.RELATIONSHIP_TYPE));
        relatinshipInfo.put(IRelationshipInformationModel.SOURCE_SIDE_ID, sourceSideId);
        relatinshipInfo.put(IRelationshipInformationModel.TARGET_SIDE_ID, targetSideId);
        relationshipInfoList.add(relatinshipInfo);
      }
    }
    
    for (Vertex kRNode : kRNodes) {
      for (Vertex relationshipNode : kRNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_PROPERTY)) {
        
        String type = relationshipNode.getProperty(CommonConstants.TYPE);
        if (type == null || !type.equals(CommonConstants.RELATIONSHIP)) {
          continue;
        }
        
        Map<String, Object> relationshipMap = UtilClass
            .getMapFromVertex(
                Arrays.asList(CommonConstants.CODE_PROPERTY, IRelationship.SIDE1,
                    IRelationship.SIDE2, IKlassNatureRelationship.RELATIONSHIP_TYPE),
                relationshipNode);
        String relationshipId = (String) relationshipMap.get(IRelationship.ID);
        Map<String, Object> side1Map = (Map<String, Object>) relationshipMap
            .get(IRelationship.SIDE1);
        Map<String, Object> side2Map = (Map<String, Object>) relationshipMap
            .get(IRelationship.SIDE2);
        
        Map<String, Object> relatinshipInfo = new HashMap<>();
        
        relatinshipInfo.put(IRelationshipInformationModel.RELATIONSHIP_ID, relationshipId);
        relatinshipInfo.put(IRelationshipInformationModel.RELATIONSHIP_SIDE_1_ID,
            side1Map.get(IRelationshipSide.ID));
        relatinshipInfo.put(IRelationshipInformationModel.RELATIONSHIP_SIDE_2_ID,
            side2Map.get(IRelationshipSide.ID));
        String relationshipType = (String) relationshipMap
            .get(IKlassNatureRelationship.RELATIONSHIP_TYPE);
        if (relationshipType == null) {
          relationshipType = "";
        }
        relatinshipInfo.put(IRelationshipInformationModel.RELATIONSHIP_TYPE, relationshipType);
        relationshipInfoList.add(relatinshipInfo);
      }
    }
    
    responseMap.put(IListModel.LIST, relationshipInfoList);
    
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRelationshipInformationByKlassId/*" };
  }
}
