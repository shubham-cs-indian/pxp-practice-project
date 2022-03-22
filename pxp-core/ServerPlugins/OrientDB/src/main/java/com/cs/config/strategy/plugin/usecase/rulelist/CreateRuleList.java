package com.cs.config.strategy.plugin.usecase.rulelist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateRuleList extends AbstractOrientPlugin {
  
  public CreateRuleList(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    map = (HashMap<String, Object>) requestMap.get("ruleList");
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(CommonConstants.RULE_LIST,
        CommonConstants.CODE_PROPERTY);
    
    Vertex ruleListNode = UtilClass.createNode(map, vertexType, Arrays.asList(IGetKlassEntityWithoutKPModel.AUDIT_LOG_INFO));
    
    returnMap = UtilClass.getMapFromNode(ruleListNode);
    AuditLogUtils.fillAuditLoginfo(returnMap, ruleListNode, Entities.RULELIST, Elements.UNDEFINED);
    UtilClass.getGraph()
        .commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateRuleList/*" };
  }
}
