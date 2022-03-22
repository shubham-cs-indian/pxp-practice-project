package com.cs.config.strategy.plugin.usecase.ssoconfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.ssoconfiguration.util.SSOConfigurationUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.exception.sso.SSOConfigurationNotFoundException;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeResponseModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.ISSOConfigurationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class SaveSSOConfiguration extends AbstractOrientPlugin {
  
  public SaveSSOConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveSSOConfiguration/*" };
  }
  
  private static final List<String> fieldsToExclude = Arrays.asList(ILanguage.CODE);
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfSSOConfigurations = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSaveSSOConfiguration = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    OrientGraph graph = UtilClass.getGraph();
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();
    
    for (Map<String, Object> ssoConfiguration : listOfSSOConfigurations) {
      try {
        Map<String, Object> ssoReturnMap = new HashMap<>();
        
        String id = (String) ssoConfiguration.get(ISSOConfigurationModel.ID);
        Vertex ssoConfigurationNode = null;
        try {
          ssoConfigurationNode = UtilClass.getVertexByIndexedId(id,
              VertexLabelConstants.SSO_CONFIGURATION);
        }
        catch (NotFoundException e) {
          throw new SSOConfigurationNotFoundException(e);
        }
        
        /** To get organizationId* */
        Iterable<Vertex> organizationResult = graph
            .command(new OCommandSQL("select expand(in('"
                + RelationshipLabelConstants.ORGANIZATION_SSO_LINK + "')) from "
                + VertexLabelConstants.SSO_CONFIGURATION + " where code = '" + id + "'"))
            .execute();
        Vertex organizationNode = null;
        if (organizationResult.iterator()
            .hasNext()) {
          organizationNode = organizationResult.iterator()
              .next();
        }
        
        /** Check domain name already exists* */
        String domain = (String) ssoConfiguration.get(ISSOConfigurationModel.DOMAIN);
        SSOConfigurationUtils.checkDomainExistanceInSSOConfiguration(domain,
            organizationNode.getProperty(ISSOConfigurationModel.ID));
        
        Vertex saveNode = UtilClass.saveNode(ssoConfiguration, ssoConfigurationNode,
            fieldsToExclude);
        ssoReturnMap = UtilClass.getMapFromNode(saveNode);
        listOfSuccessSaveSSOConfiguration.add(ssoReturnMap);
        AuditLogUtils.fillAuditLoginfo(auditLogInfoList, ssoConfigurationNode, Entities.SSO_SETTING,
            Elements.UNDEFINED, domain);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetGridSSOConfigurationResponseModel.SSO_CONFIGURATION_LIST,
        listOfSuccessSaveSSOConfiguration);
    
    graph.commit();
    Map<String, Object> bulkSaveSSOResponse = new HashMap<String, Object>();
    bulkSaveSSOResponse.put(IBulkSaveAttributeResponseModel.SUCCESS, responseMap);
    bulkSaveSSOResponse.put(IBulkSaveAttributeResponseModel.FAILURE, failure);
    bulkSaveSSOResponse.put(IBulkSaveAttributeResponseModel.AUDIT_LOG_INFO, auditLogInfoList);
    return bulkSaveSSOResponse;
  }
}
