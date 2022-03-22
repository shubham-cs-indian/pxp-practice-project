package com.cs.config.strategy.plugin.usecase.role;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.system.ISystem;
import com.cs.core.config.interactor.exception.organization.OrganizationNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.ISystemsVsEndpointsModel;
import com.cs.core.config.interactor.model.system.ISystemModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSelectedSystemsInOrganization extends AbstractOrientPlugin {
  
  public GetSelectedSystemsInOrganization(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSelectedSystemsInOrganization/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String organizationId = (String) requestMap.get(IIdParameterModel.ID);
    
    Vertex organizationNode = null;
    try {
      organizationNode = UtilClass.getVertexById(organizationId, VertexLabelConstants.ORGANIZATION);
    }
    catch (NotFoundException e) {
      throw new OrganizationNotFoundException();
    }
    
    List<Map<String, Object>> endpointsToReturn = prepareResponse(organizationId, organizationNode);
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IListModel.LIST, endpointsToReturn);
    return returnMap;
  }
  
  private List<Map<String, Object>> prepareResponse(String organizationId, Vertex organizationNode)
      throws Exception
  {
    List<Map<String, Object>> listToReturn = new ArrayList<>();
    if (getSelectedSystemsInOrganization(organizationId, organizationNode, listToReturn)) {
      getAllSystems(listToReturn);
    }
    return listToReturn;
  }
  
  private Boolean getSelectedSystemsInOrganization(String organizationId, Vertex organizationNode,
      List<Map<String, Object>> listToReturn)
  {
    Boolean isFound = true;
    OrientGraph graph = UtilClass.getGraph();
    String query = "select expand(out('" + RelationshipLabelConstants.HAS_SYSTEM_ORAGNIZATION
        + "')) from " + organizationNode.getId();
    Iterable<Vertex> systemVertices = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex systemVertex : systemVertices) {
      Map<String, Object> systemMap = new HashMap<>();
      List<String> endPointIds = new ArrayList<>();
      systemMap.put(ISystemsVsEndpointsModel.ID, UtilClass.getCodeNew(systemVertex));
      systemMap.put(ISystemsVsEndpointsModel.LABEL,
          systemVertex.getProperty(EntityUtil.getLanguageConvertedField(ISystemModel.LABEL)));
      systemMap.put(ISystemsVsEndpointsModel.CODE, (String) systemVertex.getProperty(ISystem.CODE));
      
      systemMap.put(ISystemsVsEndpointsModel.ENDPOINT_IDS, endPointIds);
      
      String endpointquery = "select from (select expand(in('"
          + RelationshipLabelConstants.HAS_SYSTEM + "')) from  " + systemVertex.getId() + ") where "
          + "in('" + RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS + "').code in['"
          + organizationId + "']";
      
      Iterable<Vertex> endpointIterable = graph.command(new OCommandSQL(endpointquery))
          .execute();
      if (endpointIterable != null) {
        for (Vertex endpoint : endpointIterable) {
          endPointIds.add(endpoint.getProperty(CommonConstants.CODE_PROPERTY));
        }
      }
      isFound = false;
      listToReturn.add(systemMap);
    }
    return isFound;
  }
  
  private void getAllSystems(List<Map<String, Object>> listToReturn)
  {
    OrientGraph graph = UtilClass.getGraph();
    String query = "select from " + VertexLabelConstants.SYSTEM;
    Iterable<Vertex> systemVertices = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex systemVertex : systemVertices) {
      Map<String, Object> systemMap = new HashMap<>();
      List<String> endPointIds = new ArrayList<>();
      systemMap.put(ISystemsVsEndpointsModel.ID, UtilClass.getCodeNew(systemVertex));
      systemMap.put(ISystemsVsEndpointsModel.LABEL,
          systemVertex.getProperty(EntityUtil.getLanguageConvertedField(ISystemModel.LABEL)));
      systemMap.put(ISystemsVsEndpointsModel.CODE, (String) systemVertex.getProperty(ISystem.CODE));
      systemMap.put(ISystemsVsEndpointsModel.ENDPOINT_IDS, endPointIds);
      
      String endpointquery = "select expand(in('" + RelationshipLabelConstants.HAS_SYSTEM
          + "')) from  " + systemVertex.getId();
      
      Iterable<Vertex> endpointIterable = graph.command(new OCommandSQL(endpointquery))
          .execute();
      if (endpointIterable != null) {
        for (Vertex endpoint : endpointIterable) {
          endPointIds.add(endpoint.getProperty(CommonConstants.CODE_PROPERTY));
        }
      }
      listToReturn.add(systemMap);
    }
  }
}
