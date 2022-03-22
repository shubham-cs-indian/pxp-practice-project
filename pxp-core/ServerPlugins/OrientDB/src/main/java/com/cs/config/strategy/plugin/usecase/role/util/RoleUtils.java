package com.cs.config.strategy.plugin.usecase.role.util;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.endpoint.util.EndpointUtils;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.user.User;
import com.cs.core.config.interactor.exception.role.RoleNotFoundException;
import com.cs.core.config.interactor.exception.role.UserAlreadyExistInRoleException;
import com.cs.core.config.interactor.exception.role.UserDoesntExistInAnyRoleException;
import com.cs.core.config.interactor.exception.role.UserExistInMultipleRoleException;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.governancerule.IKeyPerformanceIndexLimitedInfoModel;
import com.cs.core.config.interactor.model.role.IGetRoleModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

import java.util.*;

@SuppressWarnings("unchecked")
public class RoleUtils {
  
  public static HashMap<String, Object> getDefaultGlobalPermissionMap()
  {
    HashMap<String, Object> defaultGlobalPermissionMap = new HashMap<String, Object>();
    defaultGlobalPermissionMap.put("canRead", true);
    defaultGlobalPermissionMap.put("canDelete", false);
    defaultGlobalPermissionMap.put("canCreate", false);
    defaultGlobalPermissionMap.put("canEdit", false);
    return defaultGlobalPermissionMap;
  }
  
  public static HashMap<String, Object> getRoleEntityMap(Vertex roleNode)
  {
    
    HashMap<String, Object> roleMap = new HashMap<String, Object>();
    roleMap.putAll(UtilClass.getMapFromNode(roleNode));
    addUsersToRole(roleMap, roleNode);
    addGlobalPermissionToRole(roleMap, roleNode);
    addEndpointsToRole(roleMap, roleNode);
    addKPIsToRole(roleMap, roleNode);
    addSystemToRole(roleMap, roleNode);
    return roleMap;
  }
  
  /**
   * Adding System to role
   *
   * @param roleMap
   *          : Role Map contains the info related Role
   * @param roleNode
   */
  private static void addSystemToRole(HashMap<String, Object> roleMap, Vertex roleNode)
  {
    List<String> systemIdsIds = new ArrayList<>();
    Iterable<Edge> systemRelationships = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_SYSTEM_ROLE);
    
    for (Edge systemEdge : systemRelationships) {
      Vertex systemnode = systemEdge.getVertex(Direction.IN);
      systemIdsIds.add(UtilClass.getCodeNew(systemnode));
    }
    roleMap.put(IRole.SYSTEMS, systemIdsIds);
  }
  
  private static void addKPIsToRole(HashMap<String, Object> roleMap, Vertex roleNode)
  {
    List<String> kPIIds = new ArrayList<>();
    Iterable<Edge> kPIInRelationships = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_KPI_ROLE);
    
    for (Edge kPIEdge : kPIInRelationships) {
      Vertex kPINode = kPIEdge.getVertex(Direction.IN);
      kPIIds.add(UtilClass.getCodeNew(kPINode));
    }
    roleMap.put(IRole.KPIS, kPIIds);
  }
  
  public static void addTargetTaxonomies(Map<String, Object> roleMap, Vertex roleNode,
      Map<String, Object> returnMap) throws Exception
  {
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    returnMap.put(IGetRoleModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    List<String> targetTaxonomies = new ArrayList<String>();
    roleMap.put(IRole.TARGET_TAXONOMIES, targetTaxonomies);
    Iterable<Edge> hasTargetTaxonomies = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
    for (Edge hasTargetTaxonomy : hasTargetTaxonomies) {
      Vertex targetTaxonomyNode = hasTargetTaxonomy.getVertex(Direction.IN);
      String taxonomyId = UtilClass.getCodeNew(targetTaxonomyNode);
      targetTaxonomies.add(taxonomyId);
      
      Map<String, Object> taxonomyMap = new HashMap<>();
      TaxonomyUtil.fillTaxonomiesChildrenAndParentData(taxonomyMap, targetTaxonomyNode);
      referencedTaxonomies.put(taxonomyId, taxonomyMap);
    }
  }
  
  public static void addTargetKlasses(Map<String, Object> roleMap, Vertex roleNode,
      Map<String, Object> returnMap)
  {
    List<String> targetKlasses = new ArrayList<String>();
    List<Map<String, Object>> referencedKlasses = new ArrayList<>();
    returnMap.put(IGetRoleModel.REFERENCED_KLASSES, referencedKlasses);
    roleMap.put(IRole.TARGET_KLASSES, targetKlasses);
    Iterable<Edge> hasTargetKlasses = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_TARGET_KLASSES);
    for (Edge hasTargetKlass : hasTargetKlasses) {
      Vertex targetKlassNode = hasTargetKlass.getVertex(Direction.IN);
      targetKlasses.add(UtilClass.getCodeNew(targetKlassNode));
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IConfigEntityInformationModel.LABEL,
              IConfigEntityInformationModel.CODE, IConfigEntityInformationModel.ICON, IConfigEntityInformationModel.TYPE),
          targetKlassNode);
      referencedKlasses.add(klassMap);
    }
  }
  
  public static void addReferencedEndPoints(Map<String, Object> roleMap, Vertex roleNode,
      Map<String, Object> returnMap) throws Exception
  {
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IEndpointModel.ENDPOINT_TYPE, IEndpointModel.LABEL);
    List<String> endpoints = (List<String>) roleMap.get(IRoleModel.ENDPOINTS);
    Map<String, Object> referencedEndpoints = new HashMap<>();
    for (String endpoint : endpoints) {
      Vertex endpointNode = UtilClass.getVertexById(endpoint, VertexLabelConstants.ENDPOINT);
      Map<String, Object> referencedEndpoint = UtilClass.getMapFromVertex(fieldsToFetch,
          endpointNode);
      referencedEndpoint.put(IConfigEntityInformationModel.TYPE,
          referencedEndpoint.remove(IEndpointModel.ENDPOINT_TYPE));
      referencedEndpoints.put(endpoint, referencedEndpoint);
    }
    returnMap.put(IGetRoleStrategyModel.REFERENCED_ENDPOINTS, referencedEndpoints);
  }
  
  public static void addGlobalPermissionToRole(Map<String, Object> roleMap, Vertex roleNode)
  {
    
    HashMap<String, Map<String, Object>> globalPermissionMap = new HashMap<String, Map<String, Object>>();
    
    Iterable<Edge> globalPermissionForRelationships = roleNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_GLOBAL_PERMISSION_FOR);
    
    for (Edge globalPermissionForRelationship : globalPermissionForRelationships) {
      
      Vertex globalPermissionNode = globalPermissionForRelationship.getVertex(Direction.OUT);
      
      Iterator<Edge> iterator = globalPermissionNode
          .getEdges(Direction.IN,
              RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION)
          .iterator();
      if (iterator.hasNext()) {
        Edge hasGlobalPermission = iterator.next();
        Vertex klassNode = hasGlobalPermission.getVertex(com.tinkerpop.blueprints.Direction.OUT);
        String klassId = (String) klassNode.getProperty(CommonConstants.CODE_PROPERTY);
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.putAll(UtilClass.getMapFromNode(globalPermissionNode));
        
        globalPermissionMap.put(klassId, map);
      }
    }
    roleMap.put("globalPermission", globalPermissionMap);
  }
  
  public static void addUsersToRole(Map<String, Object> roleMap, Vertex roleNode)
  {
    
    List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
    Iterable<Edge> userInRelationships = roleNode.getEdges(com.tinkerpop.blueprints.Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    
    for (Edge relationship : userInRelationships) {
      Vertex userNode = relationship.getVertex(com.tinkerpop.blueprints.Direction.OUT);
      Map<String, Object> userMap = new HashMap<String, Object>();
      userMap.putAll(UtilClass.getMapFromNode(userNode));
      UserUtils.getPreferredLanguages(userMap, userNode);
      users.add(userMap);
    }
    roleMap.put("users", users);
  }
  
  public static Map<String, Object> createRole(Map<String, Object> role,
      ODatabaseDocumentTx database) throws Exception
  {
    OrientGraph graph = new OrientGraph(database);
    Map<String, Object> roleMap = new HashMap<>();
    UtilClass.setGraph(graph);
    OrientVertexType vertextype = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_ROLE, CommonConstants.CODE_PROPERTY);
    Vertex roleNode = UtilClass.createNode(role, vertextype);
    Map<String, Object> roleMapToReturn = new HashMap<String, Object>();
    roleMap.putAll(UtilClass.getMapFromNode(roleNode));
    
    roleMapToReturn.put("role", roleMap);
    roleMapToReturn.put("articles",
        KlassGetUtils.getKlassesList("article", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("collections",
        KlassGetUtils.getKlassesList("collection", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("sets",
        KlassGetUtils.getKlassesList("set", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("assets",
        KlassGetUtils.getKlassesList("asset_asset", VertexLabelConstants.ENTITY_TYPE_ASSET));
    roleMapToReturn.put("collectionAssets",
        KlassGetUtils.getKlassesList("collection_asset", VertexLabelConstants.ENTITY_TYPE_ASSET));
    roleMapToReturn.put("markets",
        KlassGetUtils.getKlassesList("market", VertexLabelConstants.ENTITY_TYPE_TARGET));
    roleMapToReturn.put("collectionTargets",
        KlassGetUtils.getKlassesList("collection_target", VertexLabelConstants.ENTITY_TYPE_TARGET));
    roleMapToReturn.put("tasks",
        KlassGetUtils.getKlassesList("task", VertexLabelConstants.ENTITY_TYPE_TASK));
    
    graph.commit();
    return roleMapToReturn;
  }
  
  public static void addUsersToRole(List<String> userIds, String roleId) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    try {
      Vertex roleNode = UtilClass.getVertexByIndexedId(roleId,
          VertexLabelConstants.ENTITY_TYPE_ROLE);
      for (String userId : userIds) {
        Vertex userNode = UtilClass.getVertexByIndexedId(userId,
            VertexLabelConstants.ENTITY_TYPE_USER);
        userNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN, roleNode);
      }
    }
    catch (NotFoundException e) {
      throw new RoleNotFoundException();
    }
    graph.commit();
  }
  
  public static Set<String> getAllowedEntities(Vertex userNode)
  {
    Set<String> entities = new HashSet<String>(Arrays.asList(
        CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY,
        CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY,
        CommonConstants.ASSET_INSTANCE_MODULE_ENTITY, CommonConstants.MARKET_INSTANCE_MODULE_ENTITY,
        CommonConstants.TEXT_ASSET_INSTANCE_MODULE_ENTITY,
        CommonConstants.FILE_INSTANCE_MODULE_ENTITY,
        CommonConstants.SUPPLIER_INSTANCE_MODULE_ENTITY));
    
    return entities;
  }
  
  private static void addEndpointsToRole(Map<String, Object> roleMap, Vertex roleNode)
  {
    List<String> endpointIds = new ArrayList<>();
    Iterable<Edge> endpointInRelationships = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_ENDPOINT);
    
    for (Edge endpointEdge : endpointInRelationships) {
      if (!(Boolean) endpointEdge.getProperty(CommonConstants.ENDPOINT_OWNER)) {
        Vertex endpointNode = endpointEdge.getVertex(com.tinkerpop.blueprints.Direction.IN);
        endpointIds.add(UtilClass.getCodeNew(endpointNode));
      }
    }
    roleMap.put(IRole.ENDPOINTS, endpointIds);
  }
  
  public static void deleteEndpoint(Vertex roleNode)
  {
    Iterable<Edge> endpointInRelationships = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_ENDPOINT);
    for (Edge endpointEdge : endpointInRelationships) {
      if ((Boolean) endpointEdge.getProperty(CommonConstants.ENDPOINT_OWNER)) {
        Vertex endpointNode = endpointEdge.getVertex(Direction.IN);
        EndpointUtils.deleteEndpointMappings(endpointNode);
        endpointNode.remove();
      }
    }
  }
  
  public static void deleteGlobalPermissions(Vertex roleNode)
  {
    Iterable<Vertex> globalPermissionVerticesOfEntity = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
    for (Vertex permissionNode : globalPermissionVerticesOfEntity) {
      permissionNode.remove();
    }
    
    Iterable<Vertex> globalPermissionVerticesOfRole = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_PROPERTY_ENTITY_GLOBAL_PERMISSIONS);
    for (Vertex permissionNode : globalPermissionVerticesOfRole) {
      permissionNode.remove();
    }
  }
  
  public static void deleteSectionElementNodesAttached(Vertex roleNode)
  {
    Iterator<Edge> iterator = roleNode
        .getEdges(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    while (iterator.hasNext()) {
      Edge hasProperty = iterator.next();
      Vertex klassPropertyNode = hasProperty.getVertex(Direction.OUT);
      KlassUtils.removeLinkbetweenSectionElementAndNotificationSetting(klassPropertyNode);
      klassPropertyNode.remove();
    }
  }
  
  public static Vertex getRoleFromUser(String userId) throws Exception
  {
    Vertex userVertex = UtilClass.getVertexByIndexedId(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    
    return getRoleFromUser(userVertex);
  }
  
  public static Vertex getRoleFromUser(Vertex userNode) throws Exception
  {
    Iterable<Vertex> roleNodes = userNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    Iterator<Vertex> iterator = roleNodes.iterator();
    Vertex roleNode = null;
    if (!iterator.hasNext()) {
      throw new UserDoesntExistInAnyRoleException();
    }
    
    roleNode = iterator.next();
    if (iterator.hasNext()) {
      throw new UserExistInMultipleRoleException();
    }
    
    return roleNode;
  }
  
  public static String getRoleIdFromUser(String userId) throws Exception
  {
    Vertex userVertex = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    Vertex userInRole = getRoleFromUser(userVertex);
    return UtilClass.getCodeNew(userInRole);
  }
  
  /**
   * @author Lokesh
   * @param propertyNode
   */
  public static void deletePropertyPermissionNodesByPropertyNode(Vertex propertyNode)
  {
    Iterable<Vertex> permissionIterable = propertyNode.getVertices(Direction.IN,
        RelationshipLabelConstants.IS_PROPERTY_PERMISSION_OF);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  }
  
  /**
   * delete all permission nodes associated with that role
   *
   * @author Lokesh
   * @param roleNode
   */
  public static void deleteAllPermissionNodesForRole(Vertex roleNode)
  {
    // delete delete property permission attached to role
    Iterable<Vertex> permissionIterable = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_ROLE_PROPERTY_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
    
    // delete property collection permission attached to role
    permissionIterable = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_ROLE_PROPERTY_COLLECTION_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
    
    // delete header permission attached to role
    permissionIterable = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_ROLE_HEADER_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
    
    // delete relationship permission attached to role
    permissionIterable = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_ROLE_RELATIONSHIP_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
    
    // delete template permission node attached to role
    permissionIterable = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_ROLE_TEMPLATE_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  }
  
  public static void addReferencedKPIs(Map<String, Object> roleMap, Vertex roleNode,
      Map<String, Object> returnMap) throws Exception
  {
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IKeyPerformanceIndexLimitedInfoModel.LABEL, IKeyPerformanceIndexLimitedInfoModel.CODE);
    List<String> kPIs = (List<String>) roleMap.get(IRoleModel.KPIS);
    Map<String, Object> referencedKPIs = new HashMap<>();
    for (String kPI : kPIs) {
      Vertex kPINode = UtilClass.getVertexById(kPI, VertexLabelConstants.GOVERNANCE_RULE_KPI);
      Map<String, Object> referencedKPI = UtilClass.getMapFromVertex(fieldsToFetch, kPINode);
      referencedKPIs.put(kPI, referencedKPI);
    }
    returnMap.put(IGetRoleStrategyModel.REFERENCED_KPIS, referencedKPIs);
  }
  
  public static void addReferencedSystems(Map<String, Object> roleMap, Vertex roleNode,
      Map<String, Object> returnMap) throws Exception
  {
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IKeyPerformanceIndexLimitedInfoModel.LABEL, CommonConstants.CODE_PROPERTY);
    List<String> systems = (List<String>) roleMap.get(IRoleModel.SYSTEMS);
    Map<String, Object> referencedSystems = new HashMap<>();
    for (String system : systems) {
      Vertex systemNode = UtilClass.getVertexById(system, VertexLabelConstants.SYSTEM);
      Map<String, Object> referencedSystem = UtilClass.getMapFromVertex(fieldsToFetch, systemNode);
      referencedSystems.put(system, referencedSystem);
    }
    returnMap.put(IGetRoleStrategyModel.REFERENCED_SYSTEMS, referencedSystems);
  }

  /**
   * ** remove edges between role and systems
   *
   * @param deletedSystemIds
   * @param roleNode
   */
  public static void manageDeletedSystemIds(List<String> deletedSystemIds, Vertex roleNode)
  {
    List<Edge> relationshipsToRemove = new ArrayList<Edge>();
    Iterable<Edge> systemInRelationships = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_SYSTEM_ROLE);
    for (Edge systemInRelationship : systemInRelationships) {
      Vertex systemNode = systemInRelationship.getVertex(Direction.IN);
      String systemId = UtilClass.getCodeNew(systemNode);
      if (deletedSystemIds.contains(systemId)) {
        relationshipsToRemove.add(systemInRelationship);
        deletedSystemIds.remove(systemId);
      }
      if (deletedSystemIds.isEmpty()) {
        break;
      }
    }
    for (Edge relationshipToRemove : relationshipsToRemove) {
      relationshipToRemove.remove();
    }
  }

  /**
   * **
   *
   * <p>
   * Add edges between systems and role
   *
   * @param addedSystemIds
   * @param roleNode
   * @throws Exception
   */
  public static void manageAddedSystemIds(List<String> addedSystemIds, Vertex roleNode) throws Exception
  {
    for (String addedSystemId : addedSystemIds) {
      Vertex kPINode = UtilClass.getVertexById(addedSystemId, VertexLabelConstants.SYSTEM);
      roleNode.addEdge(RelationshipLabelConstants.HAS_SYSTEM_ROLE, kPINode);
    }
  }

  public static void manageDeletedKPIs(List<String> deletedKPIs, Vertex roleNode)
  {
    List<Edge> relationshipsToRemove = new ArrayList<Edge>();
    Iterable<Edge> kPIInRelationships = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_KPI_ROLE);
    for (Edge kPIInRelationship : kPIInRelationships) {
      Vertex endpointNode = kPIInRelationship.getVertex(Direction.IN);
      if (deletedKPIs.contains(endpointNode.getProperty(CommonConstants.CODE_PROPERTY))) {
        relationshipsToRemove.add(kPIInRelationship);
      }
      if (relationshipsToRemove.size() == deletedKPIs.size()) {
        break;
      }
    }
    for (Edge relationshipToRemove : relationshipsToRemove) {
      relationshipToRemove.remove();
    }
  }

  public static void manageAddedKPIs(List<String> addedKPIs, Vertex roleNode) throws Exception
  {
    for (String addedKPIId : addedKPIs) {
      Vertex kPINode = UtilClass.getVertexById(addedKPIId,
          VertexLabelConstants.GOVERNANCE_RULE_KPI);
      roleNode.addEdge(RelationshipLabelConstants.HAS_KPI_ROLE, kPINode);
    }
  }

  public static void manageAddedTargetTaxonomies(List<String> addedTargetTaxonomyIds, Vertex roleNode)
      throws Exception
  {
    Iterator<Vertex> existingTaxonomyNodes = roleNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TARGET_TAXONOMIES)
        .iterator();
    if (!existingTaxonomyNodes.hasNext() && !addedTargetTaxonomyIds.isEmpty()) {
      GlobalPermissionUtils.removeOtherPermissionNodes(addedTargetTaxonomyIds,
          Arrays.asList(roleNode.getId()
              .toString()),
          true);
    }
    for (String klassId : addedTargetTaxonomyIds) {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      roleNode.addEdge(RelationshipLabelConstants.HAS_TARGET_TAXONOMIES, klassNode);
    }
  }

  public static void manageDeletedTargetTaxonomies(List<String> deletedTargetTaxonomyIds, Vertex roleNode)
  {
    if (deletedTargetTaxonomyIds.isEmpty()) {
      return;
    }
    List<Edge> relationshipsToRemove = new ArrayList<Edge>();
    Iterable<Edge> hasTargetTaxonomies = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
    for (Edge hasTargetKlass : hasTargetTaxonomies) {
      Vertex targetTaxonomyNode = hasTargetKlass.getVertex(Direction.IN);
      if (deletedTargetTaxonomyIds
          .contains(targetTaxonomyNode.getProperty(CommonConstants.CODE_PROPERTY))) {
        relationshipsToRemove.add(hasTargetKlass);
      }
      if (relationshipsToRemove.size() == deletedTargetTaxonomyIds.size()) {
        break;
      }
    }
    for (Edge relationshipToRemove : relationshipsToRemove) {
      relationshipToRemove.remove();
    }

    String query = "select expand(out('"
        + RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS + "') [entityId in "
        + EntityUtil.quoteIt(deletedTargetTaxonomyIds) + "]) from " + roleNode.getId();

    Iterable<Vertex> permissionVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex permissionVertex : permissionVertices) {
      permissionVertex.remove();
    }
  }

  /**
   * check if user already present in any role and throw exception if yes
   *
   * @author Lokesh
   * @param userNode
   * @throws Exception
   */
  public static void userLinkCheck(Vertex userNode) throws Exception
  {
    Iterator<Edge> iterator = (Iterator<Edge>) userNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    if (iterator.hasNext()) {
      throw new UserAlreadyExistInRoleException();
    }
  }


  public static void manageDeletedUsers(List<String> deletedUsers, Vertex roleNode, List<String> deletedUserNames)
  {
    List<Edge> relationshipsToRemove = new ArrayList<Edge>();
    Iterable<Edge> UserInRelationships = roleNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    for (Edge UserInRelationship : UserInRelationships) {
      Vertex userNode = UserInRelationship.getVertex(Direction.OUT);
      if (deletedUsers.contains(userNode.getProperty(CommonConstants.CODE_PROPERTY))) {
        relationshipsToRemove.add(UserInRelationship);
      }
      if (relationshipsToRemove.size() == deletedUsers.size()) {
        break;
      }
    }
    for (Edge relationshipToRemove : relationshipsToRemove) {
      Vertex userNode = relationshipToRemove.getVertex(Direction.OUT);
      deletedUserNames.add((String)userNode.getProperty(User.USER_NAME));
      relationshipToRemove.remove();
    }
  }

  public static void manageAddedUsers(List<String> userIds, Vertex roleNode, String label)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    for (String userId : userIds) {
      Vertex userNode = UtilClass.getVertexByIndexedId(userId, label);
      userLinkCheck(userNode);
      userNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN, roleNode);
    }
  }

  public static void manageDeletedEndpoints(List<String> deletedEnpointIds, Vertex roleNode)
  {
    List<Edge> relationshipsToRemove = new ArrayList<Edge>();
    Iterable<Edge> endpointInRelationships = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_ENDPOINT);
    for (Edge endpointInRelationship : endpointInRelationships) {
      Vertex endpointNode = endpointInRelationship.getVertex(Direction.IN);
      if (endpointNode.getProperty(IEndpoint.ENDPOINT_TYPE) != null && deletedEnpointIds
          .contains(endpointNode.getProperty(CommonConstants.CODE_PROPERTY))) {
        relationshipsToRemove.add(endpointInRelationship);
      }
      if (relationshipsToRemove.size() == deletedEnpointIds.size()) {
        break;
      }
    }
    for (Edge relationshipToRemove : relationshipsToRemove) {
      relationshipToRemove.remove();
    }
  }

  public static void manageAddedTargetKlasses(List<String> addedTargetKlassIds, Vertex roleNode)
      throws Exception
  {
    if (addedTargetKlassIds.isEmpty()) {
      return;
    }
    Iterator<Vertex> existingKlassNodes = roleNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TARGET_KLASSES)
        .iterator();
    if (!existingKlassNodes.hasNext()) {
      GlobalPermissionUtils.removeOtherPermissionNodes(addedTargetKlassIds,
          Arrays.asList(roleNode.getId()
              .toString()),
          false);
    }
    for (String klassId : addedTargetKlassIds) {
      Vertex klassNode = UtilClass.getVertexById(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      roleNode.addEdge(RelationshipLabelConstants.HAS_TARGET_KLASSES, klassNode);
    }
  }

  public static void manageDeletedTargetKlasses(List<String> deletedTargetKlassIds, Vertex roleNode)
  {
    if (deletedTargetKlassIds.isEmpty()) {
      return;
    }

    List<Edge> relationshipsToRemove = new ArrayList<Edge>();
    Iterable<Edge> hasTargetKlasses = roleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_TARGET_KLASSES);
    for (Edge hasTargetKlass : hasTargetKlasses) {
      Vertex targetKlassNode = hasTargetKlass.getVertex(Direction.IN);
      if (deletedTargetKlassIds
          .contains(targetKlassNode.getProperty(CommonConstants.CODE_PROPERTY))) {
        relationshipsToRemove.add(hasTargetKlass);
      }
      if (relationshipsToRemove.size() == deletedTargetKlassIds.size()) {
        break;
      }
    }
    for (Edge relationshipToRemove : relationshipsToRemove) {
      relationshipToRemove.remove();
    }

    String query = "select expand(out('"
        + RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS + "') [entityId in "
        + EntityUtil.quoteIt(deletedTargetKlassIds) + "]) from " + roleNode.getId();

    Iterable<Vertex> permissionVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex permissionVertex : permissionVertices) {
      permissionVertex.remove();
    }
  }
}
