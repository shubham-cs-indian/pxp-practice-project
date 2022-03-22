package com.cs.config.strategy.plugin.usecase.rulelist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.getentityconfiguration.GetEntityConfigurationUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.rulelist.IRuleListStrategySaveModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class SaveRuleList extends AbstractOrientPlugin {
  
  public SaveRuleList(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    HashMap<String, Object> ruleListMap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    ruleListMap = (HashMap<String, Object>) requestMap.get("ruleList");
    
//    if (ValidationUtils.validateUserInfo(ruleListMap)) {
    String ruleListId = (String) ruleListMap.get(CommonConstants.ID_PROPERTY);
    Vertex ruleListNode = UtilClass.getVertexByIndexedId(ruleListId,
        VertexLabelConstants.RULE_LIST);
    isRuleListModified(ruleListNode, ruleListMap, returnMap);
    UtilClass.saveNode(ruleListMap, ruleListNode,
        Arrays.asList(IGetKlassEntityWithoutKPModel.AUDIT_LOG_INFO));
    List<Map<String, Object>> returnMapList = new ArrayList<>();
    List<Map<String, Object>> dataRuleList = fillRuleCodeForRuleListCode(ruleListId, returnMapList);
    returnMap.put(IRuleListStrategySaveModel.DATA_RULE_LIST, dataRuleList);
    returnMap.putAll(UtilClass.getMapFromNode(ruleListNode));
//    }
    UtilClass.getGraph()
        .commit();
    List<String> klassIds = new ArrayList<>();
    /* String ruleListId = (String) returnMap.get(CommonConstants.ID_PROPERTY);*/
    ruleListNode = UtilClass.getVertexByIndexedId(ruleListId, VertexLabelConstants.RULE_LIST);
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
    AuditLogUtils.fillAuditLoginfo(returnMap, ruleListNode, Entities.RULELIST, Elements.UNDEFINED);
    returnMap.put("klassIds", klassIds);
    return returnMap;
  }
  
  private void isRuleListModified(Vertex ruleListNode, HashMap<String, Object> ruleListMap, HashMap<String, Object> returnMap)
  {
    if(!ruleListMap.get(CommonConstants.LIST_PROPERTY).equals(ruleListNode.getProperty(CommonConstants.LIST_PROPERTY))) {
      returnMap.put(IRuleListStrategySaveModel.IS_RULE_LIST_MODIFIED, true);
   }
  }

  protected List<Map<String, Object>> fillRuleCodeForRuleListCode(String id, List<Map<String, Object>> returnMapList) throws Exception
  {
    Vertex ruleListNode = (Vertex) UtilClass.getVertexByIndexedId(id, VertexLabelConstants.RULE_LIST);
    String nodeId = ruleListNode.getId().toString();
    String query = GetEntityConfigurationUtils.getThreeLevelQuery(nodeId, Direction.IN,
        Direction.IN, Direction.IN, RelationshipLabelConstants.HAS_RULE_LIST,
        RelationshipLabelConstants.RULE_LINK, RelationshipLabelConstants.ATTRIBUTE_DATA_RULE);
    Iterable<Vertex> listOfRule = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    for (Vertex ruleCode : listOfRule) {
      Vertex dataRuleNode = UtilClass.getVertexByIndexedId(ruleCode.getProperty(CommonConstants.CODE_PROPERTY),
          VertexLabelConstants.DATA_RULE);
      Map<String, Object> dataRuleMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleNode);
      GetDataRuleUtils.fillConfigDetails(dataRuleMap);
      returnMapList.add(dataRuleMap);
    }
    return returnMapList;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveRuleList/*" };
  }
}
