package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ExportOrganizationList extends AbstractOrientPlugin {
  
  public ExportOrganizationList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> responseMap = new HashMap<>();
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    
    List<String> organizationCodes = (List<String>) requestMap.get("itemCodes");
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(organizationCodes, IOrganization.CODE);
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    
    String query = "select from " + VertexLabelConstants.ORGANIZATION + condition + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query)).execute();

    for (Vertex organizationNode : resultIterable) {
      list.add(getOrganizationMap(organizationNode));
    }
    
    responseMap.put("list", list);
    
    return responseMap;
  }
  
  private static Map<String, Object> getOrganizationMap(
      Vertex organizationVertex) throws Exception
  {
    Map<String, Object> organizationMap = OrganizationUtil.getOrganizationMapToReturn(organizationVertex);
    fillOrganizationData(organizationMap, organizationVertex);
    return organizationMap;
  }
  
  private static void fillOrganizationData(Map<String, Object> organizationMap, Vertex organizationVertex)
	      throws Exception
  {
    fillKlassesData(organizationMap, organizationVertex);
    
    fillTaxonomiesData(organizationMap, organizationVertex);
    
    fillRolesData(organizationMap, organizationVertex);
    
    fillEndpointsData(organizationMap, organizationVertex);
    
    fillSystemsData(organizationMap, organizationVertex);
  }
  
  private static void fillKlassesData(Map<String, Object> organizationMap,
      Vertex organizationVertex) throws Exception
  {
    Iterable<Vertex> klassVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_KLASSES);
    List<String> klassIds = (List<String>) organizationMap.get(IOrganizationModel.KLASS_IDS);
    if (klassIds == null) {
      klassIds = new ArrayList<String>();
      organizationMap.put(IOrganizationModel.KLASS_IDS, klassIds);
    }
    for (Vertex klassVertex : klassVertices) {
      String klassId = UtilClass.getCodeNew(klassVertex);
      klassIds.add(klassId);
    }
  }
  
  private static void fillTaxonomiesData(Map<String, Object> organizationMap,
      Vertex organizationVertex) throws Exception
  {
    Iterable<Vertex> taxonomyVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES);
    List<String> taxonomyIds = (List<String>) organizationMap.get(IOrganizationModel.TAXONOMY_IDS);
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<String>();
      organizationMap.put(IOrganizationModel.TAXONOMY_IDS, taxonomyIds);
    }
    for (Vertex taxonomyVertex : taxonomyVertices) {
      String taxonomyId = UtilClass.getCodeNew(taxonomyVertex);
      taxonomyIds.add(taxonomyId);
    }
  }
  
  private static void fillRolesData(Map<String, Object> organizationMap,
      Vertex organizationVertex)
  {
    String rid = (String) organizationVertex.getId()
        .toString();
    String query = "select expand(out('" + RelationshipLabelConstants.ORGANIZATION_ROLE_LINK
        + "')) from " + rid + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    Iterable<Vertex> roleVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    List<Map<String, Object>> roles = new ArrayList<>();
    for (Vertex roleVertex : roleVertices) {
      if ((boolean) roleVertex.getProperty(IRole.IS_BACKGROUND_ROLE)) {
        continue;
      }
      Map<String, Object> roleMap = new HashMap<>();
      List<String> fieldsToFetch = Arrays.asList(IConfigEntityInformationModel.CODE,
          IConfigEntityInformationModel.LABEL, CommonConstants.CODE_PROPERTY,
          IConfigEntityInformationModel.ICON,
          IRole.ENTITIES, IRole.PHYSICAL_CATALOGS, IRole.PORTALS,
          IRole.ROLE_TYPE, IRole.IS_DASHBOARD_ENABLE, IRole.TYPE,IRole.IS_READ_ONLY, IRole.IS_STANDARD);
      
      Map<String, Object> mapFromVertex = UtilClass.getMapFromVertex(fieldsToFetch, roleVertex);
      roleMap.putAll(mapFromVertex);
      
      Iterable<Vertex> systemVertices = roleVertex.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_SYSTEM_ROLE);
      List<String> systemIds = new ArrayList<>();
      for (Vertex systemVertex : systemVertices) {
        String systemId = UtilClass.getCodeNew(systemVertex);
        systemIds.add(systemId);
      }
      roleMap.put(IRole.SYSTEMS, systemIds);
      
      Iterable<Vertex> targetKlassVertices = roleVertex.getVertices(Direction.OUT,
  	        RelationshipLabelConstants.HAS_TARGET_KLASSES);
      List<String> targetKlassIds = new ArrayList<>();
      for (Vertex targetKlassVertex : targetKlassVertices) {
        String targetKlassId = UtilClass.getCodeNew(targetKlassVertex);
        targetKlassIds.add(targetKlassId);
      }
      roleMap.put(IRole.TARGET_KLASSES, targetKlassIds);
      
      Iterable<Vertex> targetTaxonomyVertices = roleVertex.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
      List<String> targetTaxonomyIds = new ArrayList<>();
      for (Vertex targetTaxonomyVertex : targetTaxonomyVertices) {
        String targetTaxonomyId = UtilClass.getCodeNew(targetTaxonomyVertex);
        targetTaxonomyIds.add(targetTaxonomyId);
      }
      roleMap.put(IRole.TARGET_TAXONOMIES, targetTaxonomyIds);
      
      Iterable<Vertex> userVertices = roleVertex.getVertices(Direction.IN,
  	        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
      List<String> userIds = new ArrayList<>();
      for (Vertex userVertex : userVertices) {
        String userId = UtilClass.getCodeNew(userVertex);
        userIds.add(userId);
      }
      roleMap.put(IRole.USERS, userIds);
      
      Iterable<Vertex> kpiVertices = roleVertex.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_KPI_ROLE);
      List<String> kpiIds = new ArrayList<>();
      for (Vertex kpiVertex : kpiVertices) {
        String kpiId = UtilClass.getCodeNew(kpiVertex);
        kpiIds.add(kpiId);
      }
      roleMap.put(IRole.KPIS, kpiIds);
      
      Iterable<Vertex> endpointVertices = roleVertex.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_ENDPOINT);
      List<String> endpointIds = new ArrayList<>();
      for (Vertex endpointVertex : endpointVertices) {
        String endpointId = UtilClass.getCodeNew(endpointVertex);
        endpointIds.add(endpointId);
      }
      roleMap.put(IRole.ENDPOINTS, endpointIds);
      
      roles.add(roleMap);
    }
    organizationMap.put("roles", roles);
  }
  
  private static void fillEndpointsData(Map<String, Object> organizationMap,
      Vertex organizationVertex)
  {
    List<String> endpointIds = new ArrayList<>();
    Iterable<Vertex> endpointVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS);
    for (Vertex endpointVertex : endpointVertices) {
      String endpointId = UtilClass.getCodeNew(endpointVertex);
      endpointIds.add(endpointId);
    }
    organizationMap.put(IOrganizationModel.ENDPOINT_IDS, endpointIds);
  }
  
  private static void fillSystemsData(Map<String, Object> organizationMap,
      Vertex organizationVertex)
  {
    List<String> systemIds = new ArrayList<>();
    Iterable<Vertex> systemVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_SYSTEM_ORAGNIZATION);
    for (Vertex systemVertice : systemVertices) {
      String systemId = UtilClass.getCodeNew(systemVertice);
      systemIds.add(systemId);
    }
    organizationMap.put(IOrganizationModel.SYSTEMS, systemIds);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportOrganizationList/*" };
  }
}
