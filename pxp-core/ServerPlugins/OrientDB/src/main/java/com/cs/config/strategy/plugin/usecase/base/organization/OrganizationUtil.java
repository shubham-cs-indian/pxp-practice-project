package com.cs.config.strategy.plugin.usecase.base.organization;

import java.util.*;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.organization.OrganizationNotFoundException;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.interactor.model.organization.IReferencedEndpointModel;
import com.cs.core.config.interactor.model.organization.IReferencedSystemModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class OrganizationUtil {
  
  public static Map<String, Object> getOrganizationMapToReturn(String organizationId)
      throws Exception
  {
    return getOrganizationMapToReturn(
        UtilClass.getVertexById(organizationId, VertexLabelConstants.ORGANIZATION));
  }
  
  public static Map<String, Object> getOrganizationMapToReturn(Vertex organizationVertex)
  {
    return UtilClass.getMapFromVertex(new ArrayList<>(), organizationVertex);
  }
  
  public static void fillReferencedData(Map<String, Object> mapToReturn, Vertex organizationVertex)
      throws Exception
  {
    fillKlassIdsAndReferencedKlasses(mapToReturn, organizationVertex);
    
    fillTaxonomyIdsAndReferencedTaxonomies(mapToReturn, organizationVertex);
    
    fillRoleIdsAndReferencedRoles(mapToReturn, organizationVertex);
    
    fillEndpointsAndReferencedEndpoints(mapToReturn, organizationVertex);
    
    fillSystemsAndReferencedSystems(mapToReturn, organizationVertex);
  }
  
  private static void fillSystemsAndReferencedSystems(Map<String, Object> mapToReturn,
      Vertex organizationVertex)
  {
    List<String> systemIds = new ArrayList<>();
    Map<String, Object> referencedSystems = new HashMap<>();
    Iterable<Vertex> systemVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_SYSTEM_ORAGNIZATION);
    for (Vertex systemVertice : systemVertices) {
      Map<String, String> referencedSystem = new HashMap<>();
      String systemId = UtilClass.getCodeNew(systemVertice);
      referencedSystem.put(IReferencedSystemModel.ID, systemId);
      referencedSystem.put(IReferencedSystemModel.LABEL,
          (String) UtilClass.getValueByLanguage(systemVertice, IReferencedSystemModel.LABEL));
      referencedSystem.put(IReferencedSystemModel.CODE,
          systemVertice.getProperty(CommonConstants.CODE_PROPERTY));
      referencedSystems.put(systemId, referencedSystem);
      systemIds.add(systemId);
    }
    mapToReturn.put(IGetOrganizationModel.REFERENCED_SYSTEMS, referencedSystems);
    Map<String, Object> organizationMap = (Map<String, Object>) mapToReturn
        .get(IGetOrganizationModel.ORGANIZATION);
    organizationMap.put(IOrganizationModel.SYSTEMS, systemIds);
  }
  
  private static void fillEndpointsAndReferencedEndpoints(Map<String, Object> mapToReturn,
      Vertex organizationVertex)
  {
    Map<String, Object> organizationMap = (Map<String, Object>) mapToReturn
        .get(IGetOrganizationModel.ORGANIZATION);
    Map<String, Object> referencedEndpoints = new HashMap<>();
    List<String> endpointIds = new ArrayList<>();
    String systemId = null;
    Iterable<Vertex> endpointVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS);
    for (Vertex endpointVertex : endpointVertices) {
      String endpointId = UtilClass.getCodeNew(endpointVertex);
      endpointIds.add(endpointId);
      Iterable<Vertex> systemVertices = endpointVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_SYSTEM);
      for (Vertex systemVertex : systemVertices) {
        systemId = UtilClass.getCodeNew(systemVertex);
      }
      Map<String, String> referencedEndpoint = new HashMap<>();
      referencedEndpoint.put(IReferencedEndpointModel.ID, endpointId);
      referencedEndpoint.put(IReferencedEndpointModel.LABEL,
          (String) UtilClass.getValueByLanguage(endpointVertex, CommonConstants.LABEL_PROPERTY));
      referencedEndpoint.put(IReferencedEndpointModel.SYSTEM_ID, systemId);
      referencedEndpoint.put(IReferencedEndpointModel.CODE,
          endpointVertex.getProperty(CommonConstants.CODE_PROPERTY));
      referencedEndpoints.put(endpointId, referencedEndpoint);
    }
    organizationMap.put(IOrganizationModel.ENDPOINT_IDS, endpointIds);
    mapToReturn.put(IGetOrganizationModel.REFERENCED_ENDPOINTS, referencedEndpoints);
  }
  
  private static void fillRoleIdsAndReferencedRoles(Map<String, Object> mapToReturn,
      Vertex organizationVertex)
  {
    Map<String, Object> organizationMap = (Map<String, Object>) mapToReturn
        .get(IGetOrganizationModel.ORGANIZATION);
    List<String> roleIds = (List<String>) organizationMap.get(IOrganizationModel.ROLE_IDS);
    if (roleIds == null) {
      roleIds = new ArrayList<String>();
      organizationMap.put(IOrganizationModel.ROLE_IDS, roleIds);
    }
    Map<String, Object> referencedRoles = new HashMap<>();
    String rid = (String) organizationVertex.getId()
        .toString();
    String query = "select expand(out('" + RelationshipLabelConstants.ORGANIZATION_ROLE_LINK
        + "')) from " + rid + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    Iterable<Vertex> roleVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    for (Vertex roleVertex : roleVertices) {
      if ((boolean) roleVertex.getProperty(IRole.IS_BACKGROUND_ROLE)) {
        continue;
      }
      String roleId = UtilClass.getCodeNew(roleVertex);
      roleIds.add(roleId);
      Map<String, Object> roleMap = new HashMap<>();
      List<String> fieldsToFetch = Arrays.asList(IConfigEntityInformationModel.CODE,
          IConfigEntityInformationModel.LABEL, CommonConstants.CODE_PROPERTY,
          IConfigEntityInformationModel.ICON);
      
      Map<String, Object> mapFromVertex = UtilClass.getMapFromVertex(fieldsToFetch, roleVertex);
      
      roleMap.putAll(mapFromVertex);
      referencedRoles.put(roleId, roleMap);
    }
    mapToReturn.put(IGetOrganizationModel.REFERENCED_ROLES, referencedRoles);
  }
  
  private static void fillTaxonomyIdsAndReferencedTaxonomies(Map<String, Object> mapToReturn,
      Vertex organizationVertex) throws Exception
  {
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    Iterable<Vertex> taxonomyVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES);
    Map<String, Object> organizationMap = (Map<String, Object>) mapToReturn
        .get(IGetOrganizationModel.ORGANIZATION);
    List<String> taxonomyIds = (List<String>) organizationMap.get(IOrganizationModel.TAXONOMY_IDS);
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<String>();
      organizationMap.put(IOrganizationModel.TAXONOMY_IDS, taxonomyIds);
    }
    for (Vertex taxonomyVertex : taxonomyVertices) {
      String taxonomyId = UtilClass.getCodeNew(taxonomyVertex);
      taxonomyIds.add(taxonomyId);
      Map<String, Object> taxonomyMap = new HashMap<>();
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      referencedTaxonomies.put(taxonomyId, taxonomyMap);
    }
    mapToReturn.put(IGetOrganizationModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
  }
  
  private static void fillKlassIdsAndReferencedKlasses(Map<String, Object> mapToReturn,
      Vertex organizationVertex) throws Exception
  {
    Iterable<Vertex> klassVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_KLASSES);
    Map<String, Object> organizationMap = (Map<String, Object>) mapToReturn
        .get(IGetOrganizationModel.ORGANIZATION);
    List<String> klassIds = (List<String>) organizationMap.get(IOrganizationModel.KLASS_IDS);
    Map<String, Object> referenceKlasses = new HashMap<>();
    mapToReturn.put(IGetOrganizationModel.REFERENCED_KLASSES, referenceKlasses);
    if (klassIds == null) {
      klassIds = new ArrayList<String>();
      organizationMap.put(IOrganizationModel.KLASS_IDS, klassIds);
    }
    for (Vertex klassVertex : klassVertices) {
      String klassId = UtilClass.getCodeNew(klassVertex);
      klassIds.add(klassId);
      
      Map<String, Object> klassMap = new HashMap<>();
      
      List<String> fieldsToFetch = Arrays.asList(IConfigEntityInformationModel.CODE,
          IConfigEntityInformationModel.ICON, IConfigEntityInformationModel.ID,
          IConfigEntityInformationModel.LABEL, IKlassModel.NATURE_TYPE);
      Map<String, Object> mapFromVertex = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
      klassMap.putAll(mapFromVertex);
      klassMap.put(IConfigEntityInformationModel.TYPE, klassMap.remove(IKlassModel.NATURE_TYPE));
      klassMap.put(IConfigTaxonomyHierarchyInformationModel.BASETYPE,
          klassVertex.getProperty(IConfigEntityInformationModel.TYPE));
      referenceKlasses.put(klassId, klassMap);
    }
  }
  
  private static String fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId()
        .toString();
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
        ITaxonomy.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
        IReferencedArticleTaxonomyModel.CODE);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where code <> '" + UtilClass.getCodeNew(taxonomyVertex) + "'";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex childNode : resultIterable) {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, childNode));
    }
    
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    
    List<String> fieldsToBeFetched = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IReferencedArticleTaxonomyModel.LABEL, IReferencedArticleTaxonomyModel.CODE, IReferencedArticleTaxonomyModel.ICON);
    Map<String, Object> mapFromVertex = UtilClass.getMapFromVertex(fieldsToBeFetched,
        taxonomyVertex);
    taxonomyMap.putAll(mapFromVertex);
    
    /*  taxonomyMap.put(IReferencedArticleTaxonomyModel.ID, UtilClass.getCode(taxonomyVertex));
      taxonomyMap.put(IReferencedArticleTaxonomyModel.LABEL, (String) UtilClass.getValueByLanguage(taxonomyVertex, CommonConstants.LABEL_PROPERTY));
    */
    return MultiClassificationUtils.fillParentsData(fieldsToFetch, taxonomiesParentMap,
        taxonomyVertex);
  }
  
  public static Map<String, Object> getOrganizationMapToReturnWithReferencedData(
      String organizationId) throws Exception
  {
    Vertex organizationVertex = null;
    try {
      organizationVertex = UtilClass.getVertexById(organizationId,
          VertexLabelConstants.ORGANIZATION);
    }
    catch (NotFoundException e) {
      throw new OrganizationNotFoundException();
    }
    return getOrganizationMapToReturnWithReferencedData(organizationVertex);
  }
  
  public static Map<String, Object> getOrganizationMapToReturnWithReferencedData(
      Vertex organizationVertex) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    Map<String, Object> organizationMap = getOrganizationMapToReturn(organizationVertex);
    mapToReturn.put(IGetOrganizationModel.ORGANIZATION, organizationMap);
    fillReferencedData(mapToReturn, organizationVertex);
    return mapToReturn;
  }
  
  /**
   * @author Lokesh
   * @param roleNode
   * @return
   * @throws Exception
   */
  public static Vertex getOrganizationNodeForRole(Vertex roleNode) throws Exception
  {
    Iterator<Vertex> iterator = roleNode
        .getVertices(Direction.IN, RelationshipLabelConstants.ORGANIZATION_ROLE_LINK)
        .iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    return iterator.next();
  }

  public static void manageDeletedTaxonomies(List<String> deletedTaxonomyIds, Vertex organizationVertex)
  {
    if(deletedTaxonomyIds.isEmpty()) {
      return;
    }
    List<Edge> edgesToRemove = new ArrayList<>();
    Iterable<Edge> availableTaxonomyEdges = organizationVertex.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES);
    Set<String> taxonomyIdAndChildTaxonomyIds = new HashSet<>();
    for (Edge availableTaxonomyEdge : availableTaxonomyEdges) {
      Vertex taxonomyVertex = availableTaxonomyEdge.getVertex(Direction.IN);
      if (deletedTaxonomyIds.contains(UtilClass.getCodeNew(taxonomyVertex))) {
        edgesToRemove.add(availableTaxonomyEdge);
        String query = "select from(traverse in('"
            + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from "
            + taxonomyVertex.getId() + " strategy BREADTH_FIRST)";
        Iterable<Vertex> taxonomyVertices = UtilClass.getGraph()
            .command(new OCommandSQL(query))
            .execute();
        for (Vertex childTaxonomyVertex : taxonomyVertices) {
          taxonomyIdAndChildTaxonomyIds.add(UtilClass.getCodeNew(childTaxonomyVertex));
        }
      }
    }

    List<String> roleRids = new ArrayList<>();
    Iterable<Vertex> roleVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
    for (Vertex roleVertex : roleVertices) {
      Iterable<Edge> hasTargetTaxonomiesEdges = roleVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
      for (Edge hasTargetTaxonomiesEdge : hasTargetTaxonomiesEdges) {
        Vertex taxonomyVertex = hasTargetTaxonomiesEdge.getVertex(Direction.IN);
        if (taxonomyIdAndChildTaxonomyIds.contains(UtilClass.getCodeNew(taxonomyVertex))) {
          edgesToRemove.add(hasTargetTaxonomiesEdge);
        }
      }
      roleRids.add(roleVertex.getId()
          .toString());
    }

    for (Edge edge : edgesToRemove) {
      edge.remove();
    }

    if(!roleRids.isEmpty()) {
      String query = "select expand(out('"
          + RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS + "') [entityId in "
          + EntityUtil.quoteIt(deletedTaxonomyIds) + "]) from " + roleRids.toString();

      Iterable<Vertex> permissionVertices = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex permissionVertex : permissionVertices) {
        permissionVertex.remove();
      }
    }
  }

  public static void manageAddedTaxonomies(List<String> addedTaxonomyIds, Vertex organizationVertex)
      throws Exception
  {
    if(addedTaxonomyIds.isEmpty()) {
      return;
    }
    Set<String> taxonomyAndItsChildren = new HashSet<>();
    if (!addedTaxonomyIds.isEmpty()) {
      Iterable<Vertex> taxonomyVertices = organizationVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES);
      for (Vertex taxonomyVertex : taxonomyVertices) {
        taxonomyAndItsChildren.add(UtilClass.getCodeNew(taxonomyVertex));
      }
    }

    Set<String> addedtaxonomyIdsAndItsChildren = new HashSet<>();
    for (String addedTaxonomyId : addedTaxonomyIds) {
      Vertex taxonomyVertex = UtilClass.getVertexById(addedTaxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      organizationVertex.addEdge(RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES,
          taxonomyVertex);
      addedtaxonomyIdsAndItsChildren.add(addedTaxonomyId);
      addedtaxonomyIdsAndItsChildren.addAll(TaxonomyUtil.getAllChildTaxonomyIds(taxonomyVertex));
    }
    if (!addedtaxonomyIdsAndItsChildren.isEmpty() && taxonomyAndItsChildren.isEmpty()) {
      List<String> roleRids = new ArrayList<String>();
      taxonomyAndItsChildren.addAll(addedtaxonomyIdsAndItsChildren);
      Iterable<Vertex> roleVertices = organizationVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
      for (Vertex roleVertex : roleVertices) {
        roleRids.add(roleVertex.getId()
            .toString());
        Iterable<Edge> hasTargetTaxonomiesEdges = roleVertex.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
        for (Edge hasTargetTaxonomiesEdge : hasTargetTaxonomiesEdges) {
          Vertex taxonomyVertex = hasTargetTaxonomiesEdge.getVertex(Direction.IN);
          if (!taxonomyAndItsChildren.contains(UtilClass.getCodeNew(taxonomyVertex))) {
            hasTargetTaxonomiesEdge.remove();
          }
        }
      }

      GlobalPermissionUtils.removeOtherPermissionNodes(taxonomyAndItsChildren, roleRids, true);
    }
  }

  public static void manageDeletedEndpoints(List<String> deletedEndpointIds, Vertex organizationVertex)
  {
    List<Edge> edgesToRemove = new ArrayList<>();
    Iterable<Edge> availableEndpointEdges = organizationVertex.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS);
    for (Edge availableEndpointEdge : availableEndpointEdges) {
      Vertex endpointVertex = availableEndpointEdge.getVertex(Direction.IN);
      if (deletedEndpointIds.contains(UtilClass.getCodeNew(endpointVertex))) {
        edgesToRemove.add(availableEndpointEdge);
      }
    }

    Iterable<Vertex> roleVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
    for (Vertex roleVertex : roleVertices) {
      Iterable<Edge> hasTargetEndpointsEdges = roleVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_ENDPOINT);
      for (Edge hasTargetEndpointsEdge : hasTargetEndpointsEdges) {
        Vertex endpointVertex = hasTargetEndpointsEdge.getVertex(Direction.IN);
        if (deletedEndpointIds.contains(UtilClass.getCodeNew(endpointVertex))) {
          edgesToRemove.add(hasTargetEndpointsEdge);
        }
      }
    }
    for (Edge edge : edgesToRemove) {
      edge.remove();
    }
  }


  public static void manageAddedEndpoints(Vertex organizationVertex, List<String> addedEndpointIds)
      throws Exception
  {
    if (addedEndpointIds.isEmpty()) {
      return;
    }

    for (String addedEndpointId : addedEndpointIds) {
      Vertex endpointVertex = UtilClass.getVertexById(addedEndpointId,
          VertexLabelConstants.ENDPOINT);
      organizationVertex.addEdge(RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS,
          endpointVertex);
    }

    Iterable<Vertex> organizationRoles = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
    Iterable<Vertex> organizationEndPoints = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS);
    List<String> organizationEndPointIds = new ArrayList<>();
    for (Vertex organizationEndPoint : organizationEndPoints) {
      organizationEndPointIds.add(UtilClass.getCodeNew(organizationEndPoint));
    }

    for (Vertex organizationRole : organizationRoles) {
      Iterable<Edge> roleEndpPointEdges = organizationRole.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_ENDPOINT);
      for (Edge roleEndpPointEdge : roleEndpPointEdges) {
        Vertex endPointVertex = roleEndpPointEdge.getVertex(Direction.IN);
        if (!organizationEndPointIds.contains(UtilClass.getCodeNew(endPointVertex))) {
          roleEndpPointEdge.remove();
        }
      }
    }
  }

  public static void updatePhysicalCatalogsInRole(Vertex organizationVertex,
      List<String> organizationPhysicalCatalogs)
  {
    Iterable<Vertex> roleVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
    for (Vertex roleVertex : roleVertices) {
      List<String> rolePhysicalCatalogs = roleVertex.getProperty(IRole.PHYSICAL_CATALOGS);
      rolePhysicalCatalogs.retainAll(organizationPhysicalCatalogs);
      roleVertex.setProperty(IRole.PHYSICAL_CATALOGS, rolePhysicalCatalogs);
    }
  }

  public static void manageAddedKlasses(List<String> addedKlassIds, Vertex organizationVertex)
      throws Exception
  {
    if (addedKlassIds.isEmpty()) {
      return;
    }
    Iterator<Vertex> availableKlassVertices = organizationVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_AVAILABLE_KLASSES)
        .iterator();
    if (!availableKlassVertices.hasNext()) {
      List<String> roleRids = new ArrayList<String>();
      Iterable<Vertex> roleVertices = organizationVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
      for (Vertex roleVertex : roleVertices) {
        roleRids.add(roleVertex.getId()
            .toString());
      }
      if(!roleRids.isEmpty()) {
        GlobalPermissionUtils.removeOtherPermissionNodes(new HashSet<>(addedKlassIds), roleRids,
            false);
      }
    }

    Set<String> availableKlassIds = new HashSet<>();
    while (availableKlassVertices.hasNext()) {
      availableKlassIds.add(UtilClass.getCodeNew(availableKlassVertices.next()));
    }

    addedKlassIds.removeAll(availableKlassIds);

    for (String addedKlassId : addedKlassIds) {
      Vertex klassVertex = UtilClass.getVertexById(addedKlassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      organizationVertex.addEdge(RelationshipLabelConstants.HAS_AVAILABLE_KLASSES, klassVertex);
    }

    availableKlassIds.addAll(addedKlassIds);

    if (!addedKlassIds.isEmpty()) {
      Set<String> roleIds = new HashSet<>();
      Iterable<Vertex> roleVertices = organizationVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
      for (Vertex roleVertex : roleVertices) {
        Iterable<Edge> hasTargetKlassEdges = roleVertex.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_TARGET_KLASSES);
        for (Edge hasTargetKlassesEdge : hasTargetKlassEdges) {
          Vertex klassVertex = hasTargetKlassesEdge.getVertex(Direction.IN);
          if (!availableKlassIds.isEmpty()
              && !availableKlassIds.contains(UtilClass.getCodeNew(klassVertex))) {
            hasTargetKlassesEdge.remove();
            roleIds.add(UtilClass.getCodeNew(roleVertex));
          }
        }
      }
    }
  }

  public static void manageDeletedKlasses(List<String> deletedKlassIds, Vertex organizationVertex)
  {
    if (deletedKlassIds.isEmpty()) {
      return;
    }

    Iterable<Edge> availableKlassEdges = organizationVertex.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_KLASSES);
    for (Edge edge : availableKlassEdges) {
      Vertex klassVertex = edge.getVertex(Direction.IN);
      if (deletedKlassIds.contains(UtilClass.getCodeNew(klassVertex))) {
        edge.remove();
      }
    }

    List<String> roleRids = new ArrayList<>();
    Iterable<Vertex> roleVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
    for (Vertex roleVertex : roleVertices) {
      Iterable<Edge> hasTargetKlassEdges = roleVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_TARGET_KLASSES);
      for (Edge hasTargetKlassesEdge : hasTargetKlassEdges) {
        Vertex klassVertex = hasTargetKlassesEdge.getVertex(Direction.IN);
        if (deletedKlassIds.contains(UtilClass.getCodeNew(klassVertex))) {
          hasTargetKlassesEdge.remove();
        }
      }
      roleRids.add(roleVertex.getId()
          .toString());
    }

    if(!roleRids.isEmpty()) {
      String query = "select expand(out('"
          + RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS + "') [entityId in "
          + EntityUtil.quoteIt(deletedKlassIds) + "]) from " + roleRids.toString();

      Iterable<Vertex> permissionVertices = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex permissionVertex : permissionVertices) {
        permissionVertex.remove();
      }
    }
  }

  public static void manageDeletedSystemIds(List<String> deletedSystemIds, Vertex organizationVertex,
      List<String> exsitingOrganizationSystemIds)
  {
    List<Edge> edgesToRemove = new ArrayList<>();
    List<String> deletedSystemIdsClone = new ArrayList<>(deletedSystemIds);
    Iterable<Edge> availableSystemEdges = organizationVertex.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_SYSTEM_ORAGNIZATION);
    for (Edge availableSystemEdge : availableSystemEdges) {
      Vertex systemVertex = availableSystemEdge.getVertex(Direction.IN);
      String systemId = UtilClass.getCodeNew(systemVertex);
      if (deletedSystemIdsClone.contains(systemId)) {
        edgesToRemove.add(availableSystemEdge);
        exsitingOrganizationSystemIds.remove(systemId);
        deletedSystemIdsClone.remove(systemId);
      }

      if (deletedSystemIdsClone.isEmpty()) {
        break;
      }
    }

    // remove systems from role
    deletedSystemIdsClone = new ArrayList<>(deletedSystemIds);
    Iterable<Vertex> roleVertices = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
    for (Vertex roleVertex : roleVertices) {
      Iterable<Edge> hasTargetSystemsEdges = roleVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_SYSTEM_ROLE);
      for (Edge hasTargetSystemsEdge : hasTargetSystemsEdges) {
        Vertex systemVertex = hasTargetSystemsEdge.getVertex(Direction.IN);
        String systemId = UtilClass.getCodeNew(systemVertex);
        if (deletedSystemIds.contains(systemId)) {
          edgesToRemove.add(hasTargetSystemsEdge);
          deletedSystemIdsClone.remove(systemId);
        }

        if (deletedSystemIds.isEmpty()) {
          break;
        }
      }
    }

    for (Edge edge : edgesToRemove) {
      edge.remove();
    }
  }

  public static void manageAddedSystemIds(Vertex organizationVertex, List<String> addedSystemIds,
      List<String> exsitingOrganizationSystemIds) throws Exception
  {
    if (addedSystemIds.isEmpty()) {
      return;
    }

    for (String addedSystemId : addedSystemIds) {
      if (exsitingOrganizationSystemIds.contains(addedSystemId)) {
        continue;
      }
      Vertex systemVertex = UtilClass.getVertexById(addedSystemId, VertexLabelConstants.SYSTEM);
      organizationVertex.addEdge(RelationshipLabelConstants.HAS_SYSTEM_ORAGNIZATION, systemVertex);
      exsitingOrganizationSystemIds.add(addedSystemId);
    }

    Iterable<Vertex> organizationRoles = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.ORGANIZATION_ROLE_LINK);
    for (Vertex organizationRole : organizationRoles) {
      Iterable<Edge> roleSystemEdges = organizationRole.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_SYSTEM_ROLE);
      for (Edge roleSystemEdge : roleSystemEdges) {
        Vertex systemVertex = roleSystemEdge.getVertex(Direction.IN);
        if (!exsitingOrganizationSystemIds.contains(UtilClass.getCodeNew(systemVertex))) {
          roleSystemEdge.remove();
        }
      }
    }
  }

}
