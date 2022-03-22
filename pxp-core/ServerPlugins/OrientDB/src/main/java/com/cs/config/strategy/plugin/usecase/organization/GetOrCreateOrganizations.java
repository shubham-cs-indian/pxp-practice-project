package com.cs.config.strategy.plugin.usecase.organization;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOrCreateOrganizations extends AbstractOrientPlugin {
  
  public GetOrCreateOrganizations(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<HashMap<String, Object>> organizations = (List<HashMap<String, Object>>) requestMap
        .get("list");
    List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    for (Map<String, Object> organizationMap : organizations) {
      Vertex organizationNode = null;
      String organizationId = (String) organizationMap.get(IRole.ID);
      try {
        organizationNode = UtilClass.getVertexById(organizationId,
            VertexLabelConstants.ORGANIZATION);
      }
      catch (NotFoundException e) {
        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.ORGANIZATION, CommonConstants.CODE_PROPERTY);
        organizationNode = UtilClass.createNode(organizationMap, vertexType,
            Arrays.asList(IOrganizationModel.ROLE_IDS, IOrganizationModel.TAXONOMY_IDS,
                IOrganizationModel.ENDPOINT_IDS));
        
        List<String> roleIds = (List<String>) organizationMap.get(IOrganizationModel.ROLE_IDS);
        manageRoles(organizationNode, roleIds);
        
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.putAll(UtilClass.getMapFromNode(organizationNode));
        returnList.add(returnMap);
      }
    }
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    mapToReturn.put("list", returnList);
    UtilClass.getGraph()
        .commit();
    return mapToReturn;
  }
  
  private void manageRoles(Vertex organizationNode, List<String> roleIds) throws Exception
  {
    for (String roleId : roleIds) {
      Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      organizationNode.addEdge(RelationshipLabelConstants.ORGANIZATION_ROLE_LINK, roleNode);
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateOrganizations/*" };
  }
}
