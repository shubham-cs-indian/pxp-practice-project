package com.cs.config.strategy.plugin.usecase.relationship;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.relationship.util.GetRelationshipUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.ISaveRelationshipSide;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IRemovedContextInfoModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipStrategyResponseModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class SaveRelationship extends AbstractSaveRelationship {
  
  public SaveRelationship(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    HashMap<String, Object> relationshipMap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<>();
    HashMap<String, Object> relationshipResponseMap = new HashMap<>();
    Map<String, Map<String, Object>> sideInfoForDataTransfer = new HashMap<>();
    Map<String, Object> removedContextInfo = new HashMap<>();
    
    relationshipMap = (HashMap<String, Object>) requestMap.get("relationship");
    prepareDataForConfigCleanup(relationshipMap, removedContextInfo);
    
    Vertex relationshipNode = updateRelationshipFromMap(relationshipMap, sideInfoForDataTransfer);

    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> relationshipEntityMap = RelationshipUtils
        .getRelationshipEntityMap(relationshipNode);
    relationshipResponseMap.put(IGetRelationshipModel.ENTITY, relationshipEntityMap);
    
    GetRelationshipUtils.fillConfigDetails(relationshipNode, relationshipResponseMap);
    
    AuditLogUtils.fillAuditLoginfo(returnMap, relationshipNode,
        Entities.RELATIONSHIPS, Elements.RELATIONSHIPS);
    
    returnMap.put(ISaveRelationshipStrategyResponseModel.RELATIONSHIP_RESPONSE, relationshipResponseMap);
    returnMap.put(ISaveRelationshipStrategyResponseModel.RELATIONSHIP_DATA_FOR_TRANSFER, sideInfoForDataTransfer.values());
    returnMap.put(ISaveRelationshipStrategyResponseModel.REMOVED_CONTEXT_INFO, removedContextInfo);
    
    return returnMap;
  }


  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveRelationship/*" };
  }
  
  private void prepareDataForConfigCleanup(HashMap<String, Object> relationshipMap, Map<String, Object> removedContextInfo)
  {
    Map<String, Object> side1 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
    fillContextData(removedContextInfo, side1, IRelationship.SIDE1);
    Map<String, Object> side2 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    fillContextData(removedContextInfo, side2, IRelationship.SIDE2);
    removedContextInfo.put(IRemovedContextInfoModel.RELATIONSHIP_PROPERTYID, relationshipMap.get(ISaveRelationshipModel.PROPERTY_IID));
  }
  
  private void fillContextData(Map<String, Object> removedContextInfo, Map<String, Object> sideMap, String side)
  {
    if ((String) sideMap.get(ISaveRelationshipSide.DELETED_CONTEXT) != null) {
      removedContextInfo.put(side.equals(IRelationship.SIDE1) ? IRemovedContextInfoModel.REMOVED_SIDE1_CONTEXTID
          : IRemovedContextInfoModel.REMOVED_SIDE2_CONTEXTID, (String) sideMap.get(ISaveRelationshipSide.DELETED_CONTEXT));
    }
  }
}
