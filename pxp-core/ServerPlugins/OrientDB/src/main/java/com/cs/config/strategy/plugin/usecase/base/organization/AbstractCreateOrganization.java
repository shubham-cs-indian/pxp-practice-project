package com.cs.config.strategy.plugin.usecase.base.organization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.validationontype.InvalidOrganisationTypeException;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public abstract class AbstractCreateOrganization extends AbstractOrientPlugin {
  
  private static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList(IOrganizationModel.ROLE_IDS,
      IOrganizationModel.TAXONOMY_IDS, IOrganizationModel.ENDPOINT_IDS,
      IOrganizationModel.KLASS_IDS, IOrganizationModel.ORIGINAL_INSTANCE_ID,
      IOrganizationModel.IS_ONBOARDING_REQUEST);
  
  public AbstractCreateOrganization(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ORGANIZATION,
        CommonConstants.CODE_PROPERTY);
    Map<String, Object> returnMap = new HashMap<>();
    
    try {
      UtilClass.validateOnType(Constants.ORGANISATION_TYPE_LIST,
          (String) requestMap.get(IOrganizationModel.TYPE), true);
    }
    catch (InvalidTypeException e) {
      throw new InvalidOrganisationTypeException(e);
    }
    
    Vertex organizationNode = UtilClass.createNode(requestMap, vertexType, FIELDS_TO_EXCLUDE);
    UtilClass.getGraph()
        .commit();
    String organizationId = (String) requestMap.get(CommonConstants.CODE_PROPERTY);
    returnMap = OrganizationUtil.getOrganizationMapToReturnWithReferencedData(organizationId);
    AuditLogUtils.fillAuditLoginfo(returnMap, organizationNode, Entities.PARTNERS, Elements.UNDEFINED);
    return returnMap;
  }
}
