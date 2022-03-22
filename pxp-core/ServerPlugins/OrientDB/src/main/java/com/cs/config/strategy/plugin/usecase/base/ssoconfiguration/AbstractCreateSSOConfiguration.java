package com.cs.config.strategy.plugin.usecase.base.ssoconfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.ssoconfiguration.util.SSOConfigurationUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.organization.OrganizationNotFoundException;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public abstract class AbstractCreateSSOConfiguration extends AbstractOrientPlugin {
  
  public AbstractCreateSSOConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, Object> create(Map<String, Object> map, String label) throws Exception
  {
    HashMap<String, Object> ssoSetting = (HashMap<String, Object>) map
        .get(CommonConstants.SSO_SETTING);
    
    
    OrientGraph graph = UtilClass.getGraph();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(label,
        CommonConstants.CODE_PROPERTY);
    
    String organizationId = (String) ssoSetting
        .remove(ICreateSSOConfigurationModel.ORGANIZATION_ID);
    String domain = (String) ssoSetting.get(ICreateSSOConfigurationModel.DOMAIN);
    
    /** Check domain name already exists* */
    SSOConfigurationUtils.checkDomainExistanceInSSOConfiguration(domain, organizationId);
    
    Vertex SSONode = UtilClass.createNode(ssoSetting, vertexType, new ArrayList<>());
    Map<String, Object> mapToReturn = UtilClass.getMapFromNode(SSONode);
    
    if (organizationId != null) {
      Vertex organization = null;
      try {
        organization = UtilClass.getVertexByIndexedId(organizationId,
            VertexLabelConstants.ORGANIZATION);
      }
      catch (NotFoundException e) {
        throw new OrganizationNotFoundException();
      }
      organization.addEdge(RelationshipLabelConstants.ORGANIZATION_SSO_LINK, SSONode);
    }
    
    AuditLogUtils.fillAuditLoginfo(mapToReturn, SSONode, Entities.SSO_SETTING, Elements.UNDEFINED, domain);
   
    graph.commit();
    return mapToReturn;
  }
}
