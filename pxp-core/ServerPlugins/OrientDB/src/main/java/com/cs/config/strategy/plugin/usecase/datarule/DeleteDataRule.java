package com.cs.config.strategy.plugin.usecase.datarule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.datarule.util.DeleteDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class DeleteDataRule extends AbstractOrientPlugin {
  
  public DeleteDataRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<String> dataRuleIds = new ArrayList<String>();
    dataRuleIds = (List<String>) map.get(CommonConstants.IDS_PROPERTY);
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();
    
    OrientGraph graph = UtilClass.getGraph();
    List<String> deletedIds = new ArrayList<>();
    for (String id : dataRuleIds) {
      Vertex dataRuleNode = UtilClass.getVertexById(id, VertexLabelConstants.DATA_RULE);
      AuditLogUtils.fillAuditLoginfo(auditLogInfoList, dataRuleNode, Entities.RULES, Elements.UNDEFINED);
      DeleteDataRuleUtils.deleteLinkedNodes(dataRuleNode);
      dataRuleNode.remove();
      deletedIds.add(id);
    }
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("success", deletedIds);
    responseMap.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditLogInfoList);
    
    graph.commit();
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteDataRules/*" };
  }
}
