package com.cs.config.strategy.plugin.usecase.relationshipInheritance;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.relationship.IModifiedRelationshipPropertyModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedNatureRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.cs.runtime.strategy.plugin.usecase.relationshipInheritance.util.RelationshipInheritanceUtil;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetRelationshipInheritanceInfoOnNatureRelationshipChange extends AbstractOrientPlugin {
  
  public GetRelationshipInheritanceInfoOnNatureRelationshipChange(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRelationshipInheritanceInfoOnNatureRelationshipChange/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> referencedRelationshipsMap = new HashMap<>();
    Map<String, Object> referencedNatureRelationshipsMap = new HashMap<>();
    Map<String, Object> returnMap = new HashMap<>();
    List<String> changedRelationshipIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    
    for (String changedRelationshipId : changedRelationshipIds) {
      
      Map<String, Object> relationshipIdVSCouplingInfo = new HashMap<>();
      Vertex natureRelationshipNode = UtilClass.getVertexByIndexedId(changedRelationshipId,
          VertexLabelConstants.NATURE_RELATIONSHIP);
      RelationshipInheritanceUtil.getNatureRelationshipMapForRelationshipInheritance(
          referencedNatureRelationshipsMap, natureRelationshipNode);
      Map<String, Object> referencedNatureRelationshipMap = (Map<String, Object>) referencedNatureRelationshipsMap
          .get(changedRelationshipId);
      referencedNatureRelationshipMap.put(
          IReferencedNatureRelationshipInheritanceModel.PROPAGABLE_RELATIONSHIP_IDS_COUPLING_TYPE,
          relationshipIdVSCouplingInfo);
      Iterable<Edge> relationshipInheritanceEdges = RelationshipRepository
          .getRelationshipInheritanceEdgesFromNatureRelationshipNode(natureRelationshipNode);
      
      for (Edge relationshipInheritanceEdge : relationshipInheritanceEdges) {
        Vertex relationshipNode = relationshipInheritanceEdge.getVertex(Direction.IN);
        String relationshipId = UtilClass.getCodeNew(relationshipNode);
        
        try {
          RelationshipInheritanceUtil.getRelationshipMapForRelationshipInheritance(
              referencedRelationshipsMap, relationshipId);
        }
        catch (NotFoundException e) {
          continue;
        }
        
        String couplingType = relationshipInheritanceEdge
            .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
        Map<String, Object> couplingInfo = new HashMap<>();
        couplingInfo.put(IIdAndCouplingTypeModel.ID, relationshipId);
        couplingInfo.put(IIdAndCouplingTypeModel.COUPLING_TYPE, couplingType);
        relationshipIdVSCouplingInfo.put(relationshipId, couplingInfo);
      }
    }
    returnMap.put(
        IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel.REFERENCED_RELATIONSHIP,
        referencedRelationshipsMap);
    returnMap.put(
        IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationshipsMap);
    return returnMap;
  }
}
