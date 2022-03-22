package com.cs.config.strategy.plugin.usecase.organization;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.OnboardingRoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import org.apache.commons.collections.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportOrganizations extends AbstractOrientPlugin {

  private final List<String> fieldsToExclude = List.of(ConfigTag.roles.toString(), ConfigTag.taxonomyIds.toString(),
      ConfigTag.endpointIds.toString(), ConfigTag.klassIds.toString(), ConfigTag.systems.toString());

  private final List<String> rolesFieldsToExclude = List.of(ConfigTag.roles.toString(), ConfigTag.endpoints.toString(), ConfigTag.kpis.toString(),
  ConfigTag.systems.toString(), ConfigTag.targetKlasses.toString(), ConfigTag.targetTaxonomies.toString());

  public ImportOrganizations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override public String[] getNames()
  {
    return new String[] { "POST|ImportOrganizations/*" };
  }

  @Override protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> organizations = (List<Map<String, Object>>) requestMap.get("list");

    List<Map<String, Object>> importedOrganizations = new ArrayList<>();
    List<Map<String, Object>> failedOrganizations = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    int physicalCatalogIdsConstantSize         = Constants.PHYSICAL_CATALOG_IDS.size();
    int portalIdsConstantSize                  = Constants.PORTALS_IDS.size();

    for (Map<String, Object> organization : organizations) {
      try {
        List<String> organizationPhysicalCatalogs = cleanOrganizationPxonInput(organization, physicalCatalogIdsConstantSize, portalIdsConstantSize);
    	
        String organizationCode = (String) organization.get(ConfigTag.code.toString());
        Vertex organizationNode = null;
        try {
          organizationNode = UtilClass.getVertexById(organizationCode, VertexLabelConstants.ORGANIZATION);
          UtilClass.saveNode(organization, organizationNode, fieldsToExclude);
        }
        catch (NotFoundException e) {
          OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ORGANIZATION, CommonConstants.CODE_PROPERTY);
          UtilClass.validateIconExistsForImport(organization);
          organizationNode = UtilClass.createNode(organization, vertexType, fieldsToExclude);
        }
        //manage all the linked elements
        manageRoles(organization, organizationNode, organizationPhysicalCatalogs);
        manageTaxonomies(organization, organizationNode);
        manageKlasses(organization, organizationNode);
        manageEndpoints(organization, organizationNode);
        manageSystems(organization, organizationNode);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, (String) organization.get(ConfigTag.code.toString()),
            (String) organization.get(ConfigTag.label.toString()));
        addToResult(failedOrganizations, organization);
      }
      addToResult(importedOrganizations, organization);
    }
    UtilClass.getGraph().commit();

    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, importedOrganizations);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedOrganizations);
    return result;
  }
  
  private List<String> cleanOrganizationPxonInput(Map<String, Object> organization, int physicalCatalogIdsConstantSize, int portalIdsConstantSize) {
    List<String> organizationPhysicalCatalogs = (List<String>) organization.get(ConfigTag.physicalCatalogs.toString());
    List<String> organizationPortals          = (List<String>) organization.get(ConfigTag.portals.toString());
  
    organizationPhysicalCatalogs.retainAll(Constants.PHYSICAL_CATALOG_IDS);
    organizationPortals.retainAll(Constants.PORTALS_IDS);
  
    if (organizationPhysicalCatalogs.size() == physicalCatalogIdsConstantSize) {
      organizationPhysicalCatalogs = new ArrayList<String>();
      organization.put(ConfigTag.physicalCatalogs.toString(), organizationPhysicalCatalogs);
    }
  
    if (organizationPortals.size() == portalIdsConstantSize) {
      organization.put(ConfigTag.portals.toString(), new ArrayList<String>());
    }
    
    return organizationPhysicalCatalogs;
  }

  private void manageRoles(Map<String, Object> organization, Vertex organizationVertex, List<String> organizationPhysicalCatalogs) throws Exception
  {
    int portalIdsConstantSize      = Constants.PORTALS_IDS.size();
    int moduleEntitiesConstantSize = CommonConstants.MODULE_ENTITIES.size();  
    
    List<Map<String, Object>> roles = (List<Map<String, Object>>) organization.get(ConfigTag.roles.toString());
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_ROLE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> role : roles) {
      cleanRolePxonInput(role, organizationPhysicalCatalogs, portalIdsConstantSize, moduleEntitiesConstantSize);
    	
      Vertex roleNode = null;
      try {
        roleNode = UtilClass.getVertexById((String) role.get(ConfigTag.code.toString()), VertexLabelConstants.ENTITY_TYPE_ROLE);
        UtilClass.saveNode(role, roleNode, rolesFieldsToExclude);
      }
      catch (NotFoundException e) {
        roleNode = UtilClass.createNode(role, vertexType, rolesFieldsToExclude);
        organizationVertex.addEdge(RelationshipLabelConstants.ORGANIZATION_ROLE_LINK, roleNode);
      }

      manageUsersForRoles(roleNode, role);
      manageEndpointsForRoles(roleNode, role);
      manageKPIs(roleNode, role);
      manageSystemsForRole(roleNode, role);
      manageUserKlasses(role, roleNode);
      manageUserTaxonomies(role, roleNode);
    }
  }
  
  private void cleanRolePxonInput(Map<String, Object> role, List<String> organizationPhysicalCatalogs, int portalIdsConstantSize, int moduleEntitiesConstantSize)
  {
    List<String> rolePhysicalCatalogs = (List<String>) role.get(ConfigTag.physicalCatalogs.toString());
    List<String> rolePortals = (List<String>) role.get(ConfigTag.portals.toString());
    List<String> roleEntities = (List<String>) role.get(ConfigTag.entities.toString());
    
    if (organizationPhysicalCatalogs.size() == 0) {
      rolePhysicalCatalogs.retainAll(Constants.PHYSICAL_CATALOG_IDS);
    } else {
      rolePhysicalCatalogs.retainAll(organizationPhysicalCatalogs);
    }
    rolePhysicalCatalogs.retainAll(Constants.PHYSICAL_CATALOG_IDS);
    rolePortals.retainAll(Constants.PORTALS_IDS);
    roleEntities.retainAll(CommonConstants.MODULE_ENTITIES);
    
    if (rolePhysicalCatalogs.size() == organizationPhysicalCatalogs.size()) {
      role.put(ConfigTag.physicalCatalogs.toString(), new ArrayList<String>());
    }
    
    if (rolePortals.size() == portalIdsConstantSize) {
      role.put(ConfigTag.portals.toString(), new ArrayList<String>());
    }
  
    if (roleEntities.size() == moduleEntitiesConstantSize) {
      role.put(ConfigTag.entities.toString(), new ArrayList<String>());
    }
    
    role.put(ConfigTag.isBackgroundRole.toString(), false);
    role.put(ConfigTag.isMultiselect.toString(), true);
    role.put(ConfigTag.isMandatory.toString(), false);
  }

  private void manageSystemsForRole(Vertex roleNode, Map<String, Object> role) throws Exception
  {
    List<String> existingSystems = new ArrayList<>();
    Iterable<Vertex> systems = roleNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_SYSTEM_ROLE);
    for (Vertex system : systems) {
      existingSystems.add(UtilClass.getCodeNew(system));
    }
    List<String> systemsToImport = (List<String>) role.get(ConfigTag.systems.toString());
    List<String> addedSystems = ListUtils.subtract(systemsToImport, existingSystems);
    List<String> deletedSystems = ListUtils.subtract(existingSystems,systemsToImport);

    RoleUtils.manageAddedSystemIds(addedSystems, roleNode);
    RoleUtils.manageDeletedSystemIds(deletedSystems, roleNode);
  }

  private void manageKPIs(Vertex roleNode,Map<String, Object> role) throws Exception
  {
    List<String> existingKPICodes = new ArrayList<>();
    Iterable<Vertex> kpis = roleNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_KPI_ROLE);
    for (Vertex kpi : kpis) {
      existingKPICodes.add(UtilClass.getCodeNew(kpi));
    }
    List<String> kpisToLink = (List<String>) role.get(ConfigTag.kpis.toString());
    List<String> addedKPIs = ListUtils.subtract(kpisToLink, existingKPICodes);
    List<String> deletedKPIs = ListUtils.subtract(existingKPICodes, kpisToLink);

    RoleUtils.manageAddedKPIs(addedKPIs, roleNode);
    RoleUtils.manageDeletedKPIs(deletedKPIs, roleNode);
  }

  private void manageUsersForRoles(Vertex roleNode,Map<String, Object> role) throws Exception
  {
    List<String> existingUsersIds = new ArrayList<>();
    Iterable<Vertex> users = roleNode.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    for (Vertex user : users) {
      existingUsersIds.add(UtilClass.getCodeNew(user));
    }

    List<String> usersToImport = (List<String>) role.get(ConfigTag.users.toString());
    List<String> addedUsers = ListUtils.subtract(usersToImport, existingUsersIds);
    List<String> deletedUsers = ListUtils.subtract(existingUsersIds,usersToImport);

    RoleUtils.manageAddedUsers(addedUsers, roleNode, VertexLabelConstants.ENTITY_TYPE_USER);
    RoleUtils.manageDeletedUsers(deletedUsers, roleNode, new ArrayList<String>());

  }

  private void manageEndpointsForRoles(Vertex roleNode, Map<String, Object> role) throws Exception
  {
    List<String> existingEndpointIds = new ArrayList<>();
    Iterable<Vertex> endpoints = roleNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_ENDPOINT);
    for (Vertex endpoint : endpoints) {
      existingEndpointIds.add(UtilClass.getCodeNew(endpoint));
    }
    List<String> endpointToImport = (List<String>) role.get(ConfigTag.endpoints.toString());
    List<String> addedEndpoints = ListUtils.subtract(endpointToImport, existingEndpointIds);
    List<String> deletedEndpoints = ListUtils.subtract(existingEndpointIds, endpointToImport);

    OnboardingRoleUtils.manageAddedEndpoints(addedEndpoints, roleNode);
    RoleUtils.manageDeletedEndpoints(deletedEndpoints, roleNode);
  }

  private void manageTaxonomies(Map<String, Object> requestMap, Vertex organizationVertex) throws Exception
  {
    List<String> taxonomyIds = (List<String>) requestMap.get(ConfigTag.taxonomyIds.toString());
    List<String> existingTaxonomyIds = new ArrayList<>();
    Iterable<Vertex> taxonomies = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES);
    for (Vertex taxonomy : taxonomies) {
      existingTaxonomyIds.add(UtilClass.getCodeNew(taxonomy));
    }

    List<String> addedTaxonomyIds = ListUtils.subtract(taxonomyIds, existingTaxonomyIds);
    List<String> deletedTaxonomyIds = ListUtils.subtract(existingTaxonomyIds, taxonomyIds);

    OrganizationUtil.manageAddedTaxonomies(addedTaxonomyIds, organizationVertex);
    OrganizationUtil.manageDeletedTaxonomies(deletedTaxonomyIds, organizationVertex);
  }

  private void manageKlasses(Map<String, Object> requestMap, Vertex organizationVertex) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap.get(ConfigTag.klassIds.toString());
    List<String> existingKlassIds = new ArrayList<>();
    Iterable<Vertex> klasses = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_KLASSES);
    for (Vertex klass : klasses) {
      existingKlassIds.add(UtilClass.getCodeNew(klass));
    }

    List<String> addedKlassIds = ListUtils.subtract(klassIds, existingKlassIds);
    List<String> deletedKlassIds = ListUtils.subtract(existingKlassIds, klassIds);

    OrganizationUtil.manageAddedKlasses(addedKlassIds, organizationVertex);
    OrganizationUtil.manageDeletedKlasses(deletedKlassIds, organizationVertex);
  }

  private void manageSystems(Map<String, Object> requestMap, Vertex organizationVertex) throws Exception
  {
    List<String> systemIds = (List<String>) requestMap.get(ConfigTag.systems.toString());

    Iterable<Vertex> organizationSystems = organizationVertex.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_SYSTEM_ORAGNIZATION);
    List<String> existingSystemIds = new ArrayList<>();
    for (Vertex organizationEndPoint : organizationSystems) {
      existingSystemIds.add(UtilClass.getCodeNew(organizationEndPoint));
    }

    List<String> deletedSystemIds = ListUtils.subtract(existingSystemIds, systemIds);
    List<String> addedSystemIds = ListUtils.subtract(systemIds, existingSystemIds);

    if (deletedSystemIds.isEmpty() && addedSystemIds.isEmpty()) {
      return;
    }

    OrganizationUtil.manageDeletedSystemIds(deletedSystemIds, organizationVertex, existingSystemIds);
    OrganizationUtil.manageAddedSystemIds(organizationVertex, addedSystemIds, existingSystemIds);
  }

  private void manageEndpoints(Map<String, Object> requestMap, Vertex organizationVertex) throws Exception
  {
    List<String> endpointIds = (List<String>) requestMap.get(ConfigTag.endpointIds.toString());
    List<String> existingEndpointIds = new ArrayList<>();
    Iterable<Vertex> endpoints = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS);
    for (Vertex organizationEndPoint : endpoints) {
      existingEndpointIds.add(UtilClass.getCodeNew(organizationEndPoint));
    }

    List<String> addedEndpointIds = ListUtils.subtract(endpointIds, existingEndpointIds);
    List<String> deletedEndpointIds = ListUtils.subtract(existingEndpointIds, endpointIds);

    OrganizationUtil.manageAddedEndpoints(organizationVertex, addedEndpointIds);
    OrganizationUtil.manageDeletedEndpoints(deletedEndpointIds, organizationVertex);
  }

  public void addToResult(List<Map<String, Object>> failedTabs, Map<String, Object> organization)
  {
    Map<String, Object> failedImportMap = new HashMap<>();
    failedImportMap.put(ISummaryInformationModel.ID, organization.get(ConfigTag.code.toString()));
    failedImportMap.put(ISummaryInformationModel.LABEL, organization.get(ConfigTag.label.toString()));
    failedTabs.add(failedImportMap);
  }

  private void manageUserTaxonomies(Map<String, Object> role, Vertex roleNode)
      throws Exception
  {
    List<String> taxonomyCodes = (List<String>) role.get(ConfigTag.targetTaxonomies.toString());
    List<String> existingTaxonomyCodes = new ArrayList<>();
    Iterable<Vertex> taxonomies = roleNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
    for (Vertex taxonomy : taxonomies) {
      existingTaxonomyCodes.add(UtilClass.getCodeNew(taxonomy));
    }
    List<String> addedTargetTaxonomies = ListUtils.subtract(taxonomyCodes, existingTaxonomyCodes);
    List<String> deletedTargetTaxonomies = ListUtils.subtract(existingTaxonomyCodes, taxonomyCodes);
    RoleUtils.manageAddedTargetTaxonomies(addedTargetTaxonomies, roleNode);
    RoleUtils.manageDeletedTargetTaxonomies(deletedTargetTaxonomies, roleNode);
  }

  private void manageUserKlasses(Map<String, Object> role, Vertex roleNode)
      throws Exception
  {
    List<String> klassCodes = (List<String>) role.get(ConfigTag.targetKlasses.toString());
    List<String> existingKlassCodes = new ArrayList<>();
    Iterable<Vertex> klasses = roleNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TARGET_KLASSES);
    for (Vertex klass : klasses) {
      existingKlassCodes.add(UtilClass.getCodeNew(klass));
    }
    List<String> addedTargetKlasses = ListUtils.subtract(klassCodes, existingKlassCodes);
    List<String> deletedTargetKlasses = ListUtils.subtract(existingKlassCodes, klassCodes);
    RoleUtils.manageAddedTargetKlasses(addedTargetKlasses, roleNode);
    RoleUtils.manageDeletedTargetKlasses(deletedTargetKlasses, roleNode);
  }
}
