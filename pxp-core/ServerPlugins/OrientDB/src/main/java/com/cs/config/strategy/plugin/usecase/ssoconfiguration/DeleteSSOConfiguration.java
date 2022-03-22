package com.cs.config.strategy.plugin.usecase.ssoconfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.sso.ISSOConfigurationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class DeleteSSOConfiguration extends AbstractOrientPlugin {
  
  public DeleteSSOConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteSSOConfiguration/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> ssoConfigurationIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    OrientGraph graph = UtilClass.getGraph();
    List<String> deletedIds = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();
    
    for (String id : ssoConfigurationIds) {
      try {
        Vertex SSONode = UtilClass.getVertexById(id, VertexLabelConstants.SSO_CONFIGURATION);
        String deleteQuery = "delete vertex from " + VertexLabelConstants.SSO_CONFIGURATION
            + " where code = '" + id + "'";
        UtilClass.getGraph()
            .command(new OCommandSQL(deleteQuery))
            .execute();
        deletedIds.add(id);
        AuditLogUtils.fillAuditLoginfo(auditLogInfoList, SSONode, Entities.SSO_SETTING, Elements.UNDEFINED, SSONode.getProperty(ISSOConfigurationModel.DOMAIN));
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkDeleteReturnModel.SUCCESS, deletedIds);
    responseMap.put(IBulkDeleteReturnModel.FAILURE, failure);
    responseMap.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditLogInfoList);
    graph.commit();
    return responseMap;
  }
}
