package com.cs.config.strategy.plugin.usecase.rulelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.rulelist.IBulkDeleteRuleListReturnModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class DeleteRuleList extends AbstractOrientPlugin {
  
  public DeleteRuleList(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> ruleListIds = new ArrayList<String>();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    
    ruleListIds = (List<String>) requestMap.get("ids");
    
    List<String> deletedIds = new ArrayList<>();
    List<String> failureIds = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    Map<String, List<String>> klassRuleListAssociationMap = new HashMap<>();
    for (String id : ruleListIds) {
      Vertex ruleListNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.RULE_LIST);
      List<String> klassIds = getAllKlassesAssociatedWithRuleList(ruleListNode);
      updateKlassRuleListAssociationMap(klassRuleListAssociationMap, id, klassIds);
      DataRuleUtils.deleteRuleNodesLinkedToEntityNode(ruleListNode,
          RelationshipLabelConstants.HAS_RULE_LIST);
      AuditLogUtils.fillAuditLoginfo(auditInfoList, ruleListNode, Entities.RULELIST, Elements.UNDEFINED);
      ruleListNode.remove();
      deletedIds.add(id);
    }
    Map<String, Object> responseMap = new HashMap<>();
    Map<String, Object> successMap = new HashMap<>();
    successMap.put("ids", deletedIds);
    List<String> actualKlassIds = removeKlassIdsOfFailedRuleListDelete(failureIds,
        klassRuleListAssociationMap);
    successMap.put("klassIds", actualKlassIds);
    responseMap.put("success", successMap);
    responseMap.put("failure", failure);
    responseMap.put(IBulkDeleteRuleListReturnModel.AUDIT_LOG_INFO, auditInfoList);
    UtilClass.getGraph()
        .commit();
    return responseMap;
  }
  
  private List<String> removeKlassIdsOfFailedRuleListDelete(List<String> failureIds,
      Map<String, List<String>> klassRuleListAssociationMap)
  {
    for (String failureId : failureIds) {
      for (Map.Entry<String, List<String>> entry : klassRuleListAssociationMap.entrySet()) {
        if (entry.getValue()
            .contains(failureId)) {
          entry.getValue()
              .remove(failureId);
        }
      }
    }
    List<String> actualKlassIds = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : klassRuleListAssociationMap.entrySet()) {
      if (!entry.getValue()
          .isEmpty()) {
        actualKlassIds.add(entry.getKey());
      }
    }
    return actualKlassIds;
  }
  
  private void updateKlassRuleListAssociationMap(
      Map<String, List<String>> klassRuleListAssociationMap, String id, List<String> klassIds)
  {
    for (String klassId : klassIds) {
      List<String> ruleListAssociation = klassRuleListAssociationMap.get(klassId);
      if (ruleListAssociation == null) {
        List<String> newRuleListAssociation = new ArrayList<>();
        newRuleListAssociation.add(id);
        klassRuleListAssociationMap.put(klassId, newRuleListAssociation);
      }
      else {
        ruleListAssociation.add(id);
        klassRuleListAssociationMap.put(klassId, ruleListAssociation);
      }
    }
  }
  
  private List<String> getAllKlassesAssociatedWithRuleList(Vertex ruleListNode)
  {
    List<String> klassIds = new ArrayList<>();
    Iterable<Vertex> dataRuleNodes = ruleListNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_RULE_LIST);
    for (Vertex dataRuleNode : dataRuleNodes) {
      Iterable<Vertex> intermediatoryNodes = dataRuleNode.getVertices(Direction.IN,
          RelationshipLabelConstants.RULE_LINK);
      for (Vertex intermediatoryNode : intermediatoryNodes) {
        Iterable<Vertex> actualDataRuleNodes = intermediatoryNode.getVertices(Direction.IN,
            RelationshipLabelConstants.ATTRIBUTE_DATA_RULE);
        for (Vertex actualDataRuleNode : actualDataRuleNodes) {
          Iterable<Vertex> klassNodes = actualDataRuleNode.getVertices(Direction.IN,
              RelationshipLabelConstants.DATA_RULES);
          for (Vertex klassNode : klassNodes) {
            klassIds.add(klassNode.getProperty(CommonConstants.CODE_PROPERTY));
          }
        }
      }
    }
    return klassIds;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteRuleList/*" };
  }
}
