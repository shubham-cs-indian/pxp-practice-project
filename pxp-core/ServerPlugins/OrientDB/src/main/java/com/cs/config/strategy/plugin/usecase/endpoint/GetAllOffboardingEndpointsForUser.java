package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IGetOffboardingEndpointsByUserRequestModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllOffboardingEndpointsForUser extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IIdLabelModel.LABEL);
  
  public GetAllOffboardingEndpointsForUser(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllOffboardingEndpointsForUser/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IGetOffboardingEndpointsByUserRequestModel.ID);
    String physicalCatalogId = (String) requestMap
        .get(IGetOffboardingEndpointsByUserRequestModel.PHYSICAL_CATALOG_ID);
    Integer from = (Integer) requestMap.get(IGetOffboardingEndpointsByUserRequestModel.FROM);
    Integer size = (Integer) requestMap.get(IGetOffboardingEndpointsByUserRequestModel.SIZE);
    String searchText = (String) requestMap
        .get(IGetOffboardingEndpointsByUserRequestModel.SEARCH_TEXT);
    
    Vertex userNode = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    Vertex roleNode = RoleUtils.getRoleFromUser(userNode);
    Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
    Iterable<Edge> roleEndpointEdges = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_ENDPOINT);
    Integer roleEndpoints = UtilClass.size(roleEndpointEdges);
    
    Iterable<Edge> organisationEndpointEdges = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS);
    Integer organisationEndpoints = UtilClass.size(organisationEndpointEdges);
    
    List<Map<String, Object>> endpointList = new ArrayList<>();
    if (roleEndpoints > 0) {
      Iterable<Vertex> endpointVertices = prepareQueryAndGetResultForOffboardingEndpoint(
          RelationshipLabelConstants.HAS_ENDPOINT, roleNode, from, size, searchText);
      
      for (Vertex endpointNode : endpointVertices) {
        String endpointType = endpointNode.getProperty(IEndpoint.ENDPOINT_TYPE);
        if (!endpointType.equals(CommonConstants.OFFBOARDING_ENDPOINT)) {
          continue;
        }
        if (checkIfValidEndpoint(endpointNode, physicalCatalogId)) {
          endpointList.add(UtilClass.getMapFromVertex(fieldsToFetch, endpointNode));
        }
      }
    }
    else if (organisationEndpoints > 0) {
      Iterable<Vertex> endpointVertices = prepareQueryAndGetResultForOffboardingEndpoint(
          RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS, organizationNode, from, size,
          searchText);
      for (Vertex endpointNode : endpointVertices) {
        String endpointType = endpointNode.getProperty(IEndpoint.ENDPOINT_TYPE);
        if (!endpointType.equals(CommonConstants.OFFBOARDING_ENDPOINT)) {
          continue;
        }
        if (checkIfValidEndpoint(endpointNode, physicalCatalogId)) {
          endpointList.add(UtilClass.getMapFromVertex(fieldsToFetch, endpointNode));
        }
      }
    }
    else {
      String label = EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY);
      String query = "select from " + VertexLabelConstants.ENDPOINT + " where "
          + IEndpoint.ENDPOINT_TYPE + "= '" + CommonConstants.OFFBOARDING_ENDPOINT + "'" + " AND "
          + label + " like '%" + searchText + "%' order by " + label + " asc " + "skip " + from
          + " limit " + size;
      Iterable<Vertex> resultIterable = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex endpointNode : resultIterable) {
        if (checkIfValidEndpoint(endpointNode, physicalCatalogId)) {
          endpointList.add(UtilClass.getMapFromVertex(fieldsToFetch, endpointNode));
        }
      }
    }
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IListModel.LIST, endpointList);
    return returnMap;
  }
  
  private Iterable<Vertex> prepareQueryAndGetResultForOffboardingEndpoint(String edgeLabel,
      Vertex roleNode, Integer from, Integer size, String searchText)
  {
    String label = EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY);
    
    String query = "select from (select expand(out('" + edgeLabel + "')) from " + roleNode.getId()
        + ") where " + IEndpoint.ENDPOINT_TYPE + "= '" + CommonConstants.OFFBOARDING_ENDPOINT + "'"
        + " AND " + label + " like '%" + searchText + "%' " + " order by " + label + " ASC skip "
        + from + " limit " + size;
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    return searchResults;
  }
  
  private Boolean checkIfValidEndpoint(Vertex endpointNode, String physicalCatalogId)
  {
    List<String> physicalCatalogs = endpointNode.getProperty(IEndpoint.PHYSICAL_CATALOGS);
    if (physicalCatalogs != null && !physicalCatalogs.isEmpty()) {
      if ((physicalCatalogId.equals(IStandardConfig.StandardCatalog.pim.toString())
          || physicalCatalogId.equals(IStandardConfig.StandardCatalog.offboarding.toString()))
          && physicalCatalogs.contains(CommonConstants.DATA_INTEGRATION)) {
        return true;
      }
    }
    return false;
  }
}
