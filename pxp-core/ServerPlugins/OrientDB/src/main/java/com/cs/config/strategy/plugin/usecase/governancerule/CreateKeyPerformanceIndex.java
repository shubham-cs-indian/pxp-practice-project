package com.cs.config.strategy.plugin.usecase.governancerule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.governancerule.ICreateKeyPerformanceIndexModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class CreateKeyPerformanceIndex extends AbstractOrientPlugin {
  
  public CreateKeyPerformanceIndex(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateKeyPerformanceIndex/*" };
  }
  
  protected List<String> fieldsToIgnore = Arrays
      .asList(ICreateKeyPerformanceIndexModel.GOVERNANCE_RULE_BLOCKS);
  
  @Override
  public Object execute(Map<String, Object> request) throws Exception
  {
    HashMap<String, Object> kpiMap = (HashMap<String, Object>) request.get("kpi");
    List<Map<String, Object>> governanceRuleBlocks = (List<Map<String, Object>>) kpiMap
        .get(ICreateKeyPerformanceIndexModel.GOVERNANCE_RULE_BLOCKS);
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE_KPI, CommonConstants.CODE_PROPERTY);
    Vertex kpiNode = UtilClass.createNode(kpiMap, vertexType, fieldsToIgnore);
    
    OrientVertexType ruleBlockvertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE_BLOCK, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> governanceRuleBlock : governanceRuleBlocks) {
      Vertex ruleBlockNode = UtilClass.createNode(governanceRuleBlock, ruleBlockvertexType,
          new ArrayList<>());
      ruleBlockNode.addEdge(RelationshipLabelConstants.HAS_KPI, kpiNode);
    }
    
    /*  for (String roleId : CommonConstants.RACIVS_ROLE_IDS) {
      Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      kpiNode.addEdge(RelationshipLabelConstants.HAS_RACIVS_ROLE, roleNode);
    }*/
    
    OrientVertexType drillDownVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.DRILL_DOWN, CommonConstants.CODE_PROPERTY);
    Vertex drillDownNode = UtilClass.createNode(new HashMap<>(), drillDownVertexType,
        new ArrayList<>());
    kpiNode.addEdge(RelationshipLabelConstants.HAS_DRILL_DOWN, drillDownNode);
    
    TabUtils.addDefaultDashboardTab(kpiNode, SystemLevelIds.DEFAULT_DASHBOARD_TAB_ID);
    
    OrientGraph graph = UtilClass.getGraph();
    graph.commit();
    
    Map<String, Object> returnMap = GovernanceRuleUtil.getKPIFromNode(kpiNode);
    AuditLogUtils.fillAuditLoginfo(returnMap, kpiNode, Entities.KPI, Elements.UNDEFINED);
    return returnMap;
  }
}
