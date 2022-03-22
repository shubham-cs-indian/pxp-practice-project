package com.cs.config.strategy.plugin.usecase.relationshipInheritance;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.relationship.IModifiedRelationshipPropertyModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetConfigDetailsForRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetMergedCouplingTypeForRelationshipInheritance extends AbstractOrientPlugin {
  
  public GetMergedCouplingTypeForRelationshipInheritance(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetMergedCouplingTypeForRelationshipInheritance/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel.TYPES);
    List<String> relationshipIds = (List<String>) requestMap
        .get(IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel.RELATIONSHIP_IDS);
    Map<String, Object> relationshipIdVsCouplingType = new HashMap<>();
    Map<String, Object> returnMap = new HashMap<>();
    
    Iterable<Vertex> natureKRNodes = RelationshipRepository
        .getOtherSideEligibleNatureKRNodesFromKlassIds(klassIds);
    
    for (Vertex natureKRNode : natureKRNodes) {
      Iterable<Edge> inheritanceRelationshipEdges = natureKRNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP);
      for (Edge inheritanceRelationshipEdge : inheritanceRelationshipEdges) {
        Vertex relationshipNode = inheritanceRelationshipEdge.getVertex(Direction.IN);
        String relationshipId = UtilClass.getCodeNew(relationshipNode);
        if (!relationshipIds.contains(relationshipId)) {
          continue;
        }
        String couplingType = inheritanceRelationshipEdge
            .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
        Map<String, String> couplingTypeModel = (Map<String, String>) relationshipIdVsCouplingType
            .get(relationshipId);
        if (couplingTypeModel == null) {
          couplingTypeModel = new HashMap<>();
          couplingTypeModel.put(IIdAndCouplingTypeModel.ID, relationshipId);
          couplingTypeModel.put(IIdAndCouplingTypeModel.COUPLING_TYPE, couplingType);
          relationshipIdVsCouplingType.put(relationshipId, couplingTypeModel);
        }
        else {
          String oldCouplingType = couplingTypeModel.get(IIdAndCouplingTypeModel.COUPLING_TYPE);
          Integer couplingNumber = EntityUtil.compareCoupling(oldCouplingType, couplingType);
          if (couplingNumber > 0) {
            couplingTypeModel.put(IIdAndCouplingTypeModel.COUPLING_TYPE, couplingType);
          }
        }
      }
    }
    
    returnMap.put(IGetConfigDetailsForRelationshipInheritanceModel.MERGED_COUPLING_TYPES,
        relationshipIdVsCouplingType);
    return returnMap;
  }
}
