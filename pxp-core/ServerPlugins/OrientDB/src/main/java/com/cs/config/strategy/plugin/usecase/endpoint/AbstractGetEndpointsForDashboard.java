package com.cs.config.strategy.plugin.usecase.endpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.endpoint.IEndpointBasicInfoModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndointsInfoModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public abstract class AbstractGetEndpointsForDashboard extends AbstractOrientPlugin {
  
  public AbstractGetEndpointsForDashboard(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected Object getEndpointsByDashboardIdOrSystemId(Map<String, Object> requestMap)
      throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    String userId = (String) requestMap.get(IDataIntegrationRequestModel.USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    fillFunctionPermission(roleNode, returnMap);
    String dashboardRelationship = RelationshipLabelConstants.HAS_DASHBOARD_TAB;
    if (roleNode.getProperty(IRole.ROLE_TYPE)
        .equals(CommonConstants.SYSTEM_ADMIN_ROLE_TYPE)) {
      dashboardRelationship = RelationshipLabelConstants.HAS_SYSTEM;
    }
    String roleRId = roleNode.getId()
        .toString();
    String dashboardId = (String) requestMap.get(IDataIntegrationRequestModel.DASHBOARD_TAB_ID);
    
    Long from = Long.valueOf(requestMap.get(IDataIntegrationRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IDataIntegrationRequestModel.SIZE)
        .toString());
    
    List<Map<String, Object>> endpointsList = new ArrayList<>();
    
    String countQuery = genrateCountQuery(dashboardRelationship, dashboardId, roleRId,
        RelationshipLabelConstants.HAS_ENDPOINT);
    Long count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    String query = null;
    if (count > 0) {
      query = genrateQuery(RelationshipLabelConstants.HAS_ENDPOINT, dashboardRelationship, roleRId,
          dashboardId, from, size);
    }
    else {
      Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
      String organizationRid = organizationNode.getId()
          .toString();
      countQuery = genrateCountQuery(dashboardRelationship, dashboardId, organizationRid,
          RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS);
      count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
      if (count > 0) {
        query = genrateQuery(RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS,
            dashboardRelationship, organizationRid, dashboardId, from, size);
      }
      else {
        query = "select from " + VertexLabelConstants.ENDPOINT + " where out('"
            + dashboardRelationship + "').code in['" + dashboardId + "']" + " order by "
            + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"
            + " skip " + from + " limit " + size;
        countQuery = "select count(*) from " + VertexLabelConstants.ENDPOINT + " where out('"
            + dashboardRelationship + "').code in['" + dashboardId + "']";
        count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
      }
    }
    
    Iterable<Vertex> vertices = null;
    if (count > 0) {
      vertices = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex endpoint : vertices) {
        fillEndpointsInRespectiveList(endpointsList, endpoint);
      }
    }
    returnMap.put(IGetEndointsInfoModel.ENDPOINTS, endpointsList);
    returnMap.put(IGetEndointsInfoModel.TOTAL_COUNT, count);
    return returnMap;
  }
  
  private String genrateCountQuery(String dashboardRelationship, String dashboardId,
      String roleOrOrganizationRId, String availableEndpointRelationship)
  {
    String countQuery = "select count(*) from (select expand(out('" + availableEndpointRelationship
        + "')) from " + roleOrOrganizationRId + ")";
    return countQuery;
  }
  
  private String genrateQuery(String hasOrHasAvailableEndpoints, String dashboardRelationship,
      String roleOrOrganizationRId, String dashboardId, Long from, Long size)
  {
    String query = "select from (select expand(out('" + hasOrHasAvailableEndpoints + "')) from "
        + roleOrOrganizationRId + ") where out ('" + dashboardRelationship + "').code contains "
        + EntityUtil.quoteIt(dashboardId) + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc " + " skip "
        + from + " limit " + size;
    
    return query;
  }
  
  private void fillEndpointsInRespectiveList(List<Map<String, Object>> onboardEndpointsList,
      Vertex endpoint)
  {
    Map<String, Object> endpointMap = new HashMap<>();
    endpointMap.put(IIdLabelTypeModel.ID, endpoint.getProperty(CommonConstants.CODE_PROPERTY));
    endpointMap.put(IIdLabelTypeModel.TYPE, endpoint.getProperty(IEndpointModel.ENDPOINT_TYPE));
    endpointMap.put(IIdLabelTypeModel.LABEL,
        UtilClass.getValueByLanguage(endpoint, IEndpointModel.LABEL));
    endpointMap.put(IConfigEntityInformationModel.CODE, endpoint.getProperty(IEndpoint.CODE));
    endpointMap.put(IEndpointBasicInfoModel.PHYSICAL_CATALOG_ID, ((List<String>)endpoint.getProperty(IEndpoint.PHYSICAL_CATALOGS)).get(0));
    UtilClass.fetchIconInfo(endpoint, endpointMap);
    
    onboardEndpointsList.add(endpointMap);
  }
  
  protected void fillFunctionPermission(Vertex roleNode, Map<String,Object> returnMap) throws Exception
  {
  }
}
