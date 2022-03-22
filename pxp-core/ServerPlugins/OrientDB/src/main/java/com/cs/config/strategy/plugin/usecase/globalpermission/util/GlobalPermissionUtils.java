package com.cs.config.strategy.plugin.usecase.globalpermission.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermissionsForRole;
import com.cs.core.config.interactor.entity.globalpermissions.IKlassTaxonomyPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyCollectionPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyPermissions;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.template.IHeaderPermission;
import com.cs.core.config.interactor.entity.template.IPropertyCollectionPermission;
import com.cs.core.config.interactor.entity.template.IPropertyPermission;
import com.cs.core.config.interactor.entity.template.IRelationshipPermission;
import com.cs.core.config.interactor.entity.template.ITabPermission;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.ISaveGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.index.OCompositeKey;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class GlobalPermissionUtils {
  
  public static Map<String, Object> getTreePermissions(Vertex roleNode)
  {
    Map<String, Object> roleMapToReturn = new HashMap<String, Object>();
    Map<String, Object> treePermissionsMap = new HashMap<String, Object>();
    Iterable<Vertex> klassOrTaxonomyPermissions = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS);
    for (Vertex klassOrTaxonomyPermission : klassOrTaxonomyPermissions) {
      Iterator<Vertex> klassOrTaxonomyNodes = klassOrTaxonomyPermission
          .getVertices(Direction.IN,
              RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION)
          .iterator();
      if (klassOrTaxonomyNodes.hasNext()) {
        Vertex klassOrTaxonomyNode = klassOrTaxonomyNodes.next();
        treePermissionsMap.put(UtilClass.getCodeNew(klassOrTaxonomyNode),
            UtilClass.getMapFromNode(klassOrTaxonomyPermission));
      }
    }
    
    roleMapToReturn.put(IGlobalPermissionsForRole.ID, UtilClass.getCodeNew(roleNode));
    roleMapToReturn.put(IGlobalPermissionsForRole.TREE_PERMISSIONS, treePermissionsMap);
    
    return roleMapToReturn;
  }
  
  public static Map<String, Object> getPropertiesPermissions(String roleId,
      String propertyCollectionId) throws Exception
  {
    Map<String, Object> propertyPermissionsMap = new HashMap<String, Object>();
    Map<String, Object> returnMap = new HashMap<>();
    Vertex propertyCollectionNode = UtilClass.getVertexById(propertyCollectionId,
        VertexLabelConstants.PROPERTY_COLLECTION);
    List<Map<String, Object>> elements = getPropertyCollectionElements(propertyCollectionNode);
    for (Map<String, Object> element : elements) {
      Map<String, Object> propertyDefaultPermissionsMap = getDefaultPropertyPermissionDeprecated();
      
      String entityType = (String) element.get(CommonConstants.TYPE_PROPERTY);
      String entityLabel = null;
      switch (entityType) {
        case CommonConstants.ATTRIBUTE_PROPERTY:
          entityLabel = VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
          break;
        case CommonConstants.TAG_PROPERTY:
          entityLabel = VertexLabelConstants.ENTITY_TAG;
          break;
        case CommonConstants.ROLE_PROPERTY:
          entityLabel = VertexLabelConstants.ENTITY_TYPE_ROLE;
          break;
        case CommonConstants.RELATIONSHIP_PROPERTY:
          entityLabel = VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP;
          break;
      }
      String entityId = (String) element.get(CommonConstants.ID_PROPERTY);
      Vertex propertyNode = UtilClass.getVertexById(entityId, entityLabel);
      String propertyId = UtilClass.getCodeNew(propertyNode);
      Boolean isPermissionNodeExsit = false;
      Iterable<Edge> propertyPermissionNodeEdges = propertyNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
      for (Edge propertyPermissionNodeEdge : propertyPermissionNodeEdges) {
        if (propertyPermissionNodeEdge.getProperty("roleId")
            .equals(roleId)) {
          Vertex propertyPermission = propertyPermissionNodeEdge.getVertex(Direction.IN);
          if (propertyPermissionsMap.get(propertyId) == null) {
            propertyPermissionsMap.put(propertyId, UtilClass.getMapFromNode(propertyPermission));
          }
          isPermissionNodeExsit = true;
          break;
        }
      }
      if (!isPermissionNodeExsit) {
        if (propertyPermissionsMap.get(propertyId) == null) {
          propertyDefaultPermissionsMap.put(IPropertyPermissions.TYPE, entityType);
          propertyPermissionsMap.put(propertyId, propertyDefaultPermissionsMap);
        }
      }
    }
    
    returnMap.put(IGlobalPermissionsForRole.ID, roleId);
    returnMap.put(IGlobalPermissionsForRole.PROPERTY_PERMISSIONS, propertyPermissionsMap);
    
    return returnMap;
  }
  
  @Deprecated
  private static Map<String, Object> getDefaultPropertyPermissionDeprecated()
  {
    Map<String, Object> propertyDefaultPermissionsMap = new HashMap<String, Object>();
    propertyDefaultPermissionsMap.put(IPropertyPermissions.IS_DISABLED, false);
    return propertyDefaultPermissionsMap;
  }
  
  @Deprecated
  private static Map<String, Object> getDefaultPropertyCollectionPermissionDeprecated()
  {
    Map<String, Object> propertyCollectionDefaultPermissionsMap = new HashMap<String, Object>();
    propertyCollectionDefaultPermissionsMap.put(IPropertyCollectionPermissions.IS_COLLAPSED, false);
    propertyCollectionDefaultPermissionsMap.put(IPropertyCollectionPermissions.CAN_EDIT, true);
    propertyCollectionDefaultPermissionsMap.put(IPropertyCollectionPermissions.IS_HIDDEN, false);
    return propertyCollectionDefaultPermissionsMap;
  }
  
  private static Map<String, Object> getDefaultKlassTaxonomyPermission()
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IKlassTaxonomyPermissions.CAN_CREATE, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_READ, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_EDIT, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DELETE, true);
    return returnMap;
  }
  
  public static Map<String, Object> getGlobalPermission(String roleId, List<String> entityIds)
      throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IKlassTaxonomyPermissions.CAN_CREATE, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_READ, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_EDIT, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DELETE, false);
    Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    Iterable<Vertex> klassOrTaxonomyPermissions = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS);
    for (Vertex klassOrTaxonomyPermission : klassOrTaxonomyPermissions) {
      Iterator<Vertex> klassOrTaxonomyNodes = klassOrTaxonomyPermission
          .getVertices(Direction.IN,
              RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION)
          .iterator();
      if (klassOrTaxonomyNodes.hasNext()) {
        Vertex klassOrTaxonomyNode = klassOrTaxonomyNodes.next();
        HashMap<String, Object> klassOrTaxonomyPermissionMap = UtilClass
            .getMapFromNode(klassOrTaxonomyPermission);
        if (entityIds.contains(UtilClass.getCodeNew(klassOrTaxonomyNode))) {
          if (!(Boolean) returnMap.get(IKlassTaxonomyPermissions.CAN_CREATE)
              && (Boolean) klassOrTaxonomyPermissionMap.get(IKlassTaxonomyPermissions.CAN_CREATE)) {
            returnMap.put(IKlassTaxonomyPermissions.CAN_CREATE, true);
          }
          if (!(Boolean) returnMap.get(IKlassTaxonomyPermissions.CAN_READ)
              && (Boolean) klassOrTaxonomyPermissionMap.get(IKlassTaxonomyPermissions.CAN_READ)) {
            returnMap.put(IKlassTaxonomyPermissions.CAN_READ, true);
          }
          if (!(Boolean) returnMap.get(IKlassTaxonomyPermissions.CAN_EDIT)
              && (Boolean) klassOrTaxonomyPermissionMap.get(IKlassTaxonomyPermissions.CAN_EDIT)) {
            returnMap.put(IKlassTaxonomyPermissions.CAN_EDIT, true);
          }
          if (!(Boolean) returnMap.get(IKlassTaxonomyPermissions.CAN_DELETE)
              && (Boolean) klassOrTaxonomyPermissionMap.get(IKlassTaxonomyPermissions.CAN_DELETE)) {
            returnMap.put(IKlassTaxonomyPermissions.CAN_DELETE, true);
          }
        }
      }
    }
    
    return returnMap;
  }
  
  private static List<Map<String, Object>> getPropertyCollectionElements(
      Vertex propertyCollectionNode)
  {
    Iterator<Edge> iterator = propertyCollectionNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
        .iterator();
    List<Map<String, Object>> elements = new ArrayList<>();
    while (iterator.hasNext()) {
      Edge entityTo = iterator.next();
      String entityType = entityTo.getProperty(CommonConstants.TYPE_PROPERTY);
      Vertex entityNode = entityTo.getVertex(Direction.OUT);
      Map<String, Object> elementMap = new HashMap<>();
      elementMap.put(CommonConstants.ID_PROPERTY,
          entityNode.getProperty(CommonConstants.CODE_PROPERTY));
      elementMap.put(CommonConstants.TYPE_PROPERTY, entityType);
      elements.add(elementMap);
    }
    
    return elements;
  }
  
  public static Map<String, Object> getGlobalPermissions(Vertex roleNode,
      HashMap<String, Object> globalPermissionMap) throws Exception
  {
    String roleId = UtilClass.getCodeNew(roleNode);
    Map<String, Object> permissionsMap = getTreePermissions(roleNode);
    
    String query = "SELECT FROM " + VertexLabelConstants.PROPERTY_COLLECTION + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    Iterable<Vertex> iterator = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Map<String, Object> propertyCollectionIdPermissionMap = new HashMap<String, Object>();
    for (Vertex propertyCollectionNode : iterator) {
      String propertyCollectionId = UtilClass.getCodeNew(propertyCollectionNode);
      Map<String, Object> propertyCollectionPermission = GlobalPermissionUtils
          .getPropertyCollectionPermission(propertyCollectionId, roleId);
      propertyCollectionIdPermissionMap.put(propertyCollectionId, propertyCollectionPermission);
    }
    permissionsMap.put(ISaveGlobalPermissionsForRoleModel.PROPERTY_COLLECTION_PERMISSIONS,
        propertyCollectionIdPermissionMap);
    
    String propertyCollectionId = (String) globalPermissionMap
        .get(ISaveGlobalPermissionsForRoleModel.SELECTED_PROPERTY_COLLECTION_ID);
    if (propertyCollectionId != null) {
      permissionsMap.putAll(getPropertiesPermissions(roleId, propertyCollectionId));
    }
    
    permissionsMap.put(IGlobalPermissionsForRole.ID, roleId);
    
    return permissionsMap;
  }
  
  public static List<String> getKlassesForUser(String loginUserId)
  {
    OrientGraph graph = UtilClass.getGraph();
    
    // List<Map<String, Object>> roles = new ArrayList<>();
    List<String> klassIds = new ArrayList<String>();
    // if (loginUserId.equals(CommonConstants.ADMIN_USER_ID)) {
    if (true) {
      Iterable<Vertex> klassNodes = graph
          .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      for (Vertex klassNode : klassNodes) {
        String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
        klassIds.add(klassId);
      }
    }
    
    graph.commit();
    return klassIds;
  }
  
  @Deprecated
  public static Map<String, Object> getPropertyPermissionDeprecated(String propertyId,
      String roleId) throws NotFoundException, MultipleVertexFoundException
  {
    String query = "select from " + VertexLabelConstants.PROPERTY_ENTITY_GLOBAL_PERMISSIONS + " "
        + "where in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION
        + "').code contains \"" + propertyId + "\" " + "and in('"
        + RelationshipLabelConstants.HAS_PROPERTY_ENTITY_GLOBAL_PERMISSIONS + "').code contains \""
        + roleId + "\"";
    
    Map<String, Object> propertyPermission = null;
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      return getDefaultPropertyPermissionDeprecated();
    }
    
    if (iterator.hasNext()) {
      Vertex propertyPermissionNode = iterator.next();
      propertyPermission = UtilClass.getMapFromNode(propertyPermissionNode);
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return propertyPermission;
  }
  
  @Deprecated
  public static Map<String, Object> getPropertyCollectionPermission(String propertyCollectionId,
      String roleId) throws NotFoundException, MultipleVertexFoundException
  {
    String query = "select from " + VertexLabelConstants.PROPERTY_COLLECTION_GLOBAL_PERMISSIONS
        + " " + "where in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION
        + "').code contains \"" + propertyCollectionId + "\" and in('"
        + RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_GLOBAL_PERMISSIONS
        + "').code contains \"" + roleId + "\"";
    
    Map<String, Object> propertyCollectionPermission = null;
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      return getDefaultPropertyCollectionPermissionDeprecated();
    }
    
    if (iterator.hasNext()) {
      Vertex propertyCollectionPermissionNode = iterator.next();
      propertyCollectionPermission = UtilClass.getMapFromNode(propertyCollectionPermissionNode);
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return propertyCollectionPermission;
  }
  
  public static Map<String, Object> getKlassAndTaxonomyPermission(String klassId, String roleId)
      throws NotFoundException, MultipleVertexFoundException
  {
    OIndex<?> createIndex = UtilClass.getDatabase()
        .getMetadata()
        .getIndexManager()
        .getIndex(Constants.GLOBAL_CAN_CREATE_PERMISSIONS_INDEX);
    Iterable<ORecordId> createIterable = (Iterable<ORecordId>) createIndex
        .get(new OCompositeKey(Arrays.asList(klassId, roleId)));
    Boolean canCreate = false;
    if (createIterable == null || !createIterable.iterator()
        .hasNext()) {
      canCreate = true;
    }
    
    OIndex<?> readIndex = UtilClass.getDatabase()
        .getMetadata()
        .getIndexManager()
        .getIndex(Constants.GLOBAL_CAN_READ_PERMISSIONS_INDEX);
    Iterable<ORecordId> readIterable = (Iterable<ORecordId>) readIndex
        .get(new OCompositeKey(Arrays.asList(klassId, roleId)));
    Boolean canRead = false;
    if (readIterable == null || !readIterable.iterator()
        .hasNext()) {
      canRead = true;
    }
    
    OIndex<?> editIndex = UtilClass.getDatabase()
        .getMetadata()
        .getIndexManager()
        .getIndex(Constants.GLOBAL_CAN_EDIT_PERMISSIONS_INDEX);
    Iterable<ORecordId> editIterable = (Iterable<ORecordId>) editIndex
        .get(new OCompositeKey(Arrays.asList(klassId, roleId)));
    Boolean canEdit = false;
    if (editIterable == null || !editIterable.iterator()
        .hasNext()) {
      canEdit = true;
    }
    
    OIndex<?> deleteIndex = UtilClass.getDatabase()
        .getMetadata()
        .getIndexManager()
        .getIndex(Constants.GLOBAL_CAN_DELETE_PERMISSIONS_INDEX);
    Iterable<ORecordId> deleteIterable = (Iterable<ORecordId>) deleteIndex
        .get(new OCompositeKey(Arrays.asList(klassId, roleId)));
    Boolean canDelete = false;
    if (deleteIterable == null || !deleteIterable.iterator()
        .hasNext()) {
      canDelete = true;
    }
    
    OIndex<?> downloadIndex = UtilClass.getDatabase()
        .getMetadata()
        .getIndexManager()
        .getIndex(Constants.ASSET_CAN_DOWNLOAD_PERMISSIONS_INDEX);
    Iterable<ORecordId> downloadIterable = (Iterable<ORecordId>) downloadIndex
        .get(new OCompositeKey(Arrays.asList(klassId, roleId)));
    Boolean canDownload = false;
    if (downloadIterable == null || !downloadIterable.iterator()
        .hasNext()) {
      canDownload = true;
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IKlassTaxonomyPermissions.CAN_CREATE, canCreate);
    returnMap.put(IKlassTaxonomyPermissions.CAN_READ, canRead);
    returnMap.put(IKlassTaxonomyPermissions.CAN_EDIT, canEdit);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DELETE, canDelete);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DOWNLOAD, canDownload);
    return returnMap;
    
    /*
    OIndex<?> index = UtilClass.getDatabase().getMetadata().getIndexManager().getIndex(Constants.GLOBAL_PERMISSION_INDEX);
    Iterable<ORecordId> x = (Iterable<ORecordId>)index.get(new OCompositeKey(Arrays.asList(klassId, roleId)));
    
    Map<String, Object> klassTaxonomyPermission = null;
    Iterator<ORecordId> iterator = x.iterator();
    if (!iterator.hasNext()) {
      return getDefaultKlassTaxonomyPermission();
    }
    
    if (iterator.hasNext()) {
      ORecordId rid = iterator.next();
      Vertex klassTaxonomyPermissionNode = UtilClass.getGraph().getVertex(rid);
      klassTaxonomyPermission = UtilClass.getMapFromNode(klassTaxonomyPermissionNode);
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return klassTaxonomyPermission;
    //return getKlassAndTaxonomyPermissionMap(roleId, klassId);
    */
  }
  
  @Deprecated
  public static Map<String, Object> getPropertyCollectionPermission(String propertyCollectionId,
      String roleId, String templateId) throws NotFoundException, MultipleVertexFoundException
  {
    String query = "Select From " + VertexLabelConstants.PROPERTY_COLLECTION_CAN_READ_PERMISSION
        + " where in('" + RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_PERMISSION
        + "').code contains '" + templateId + "' and in('"
        + RelationshipLabelConstants.HAS_ROLE_PROPERTY_COLLECTION_PERMISSION + "').code contains '"
        + roleId + "' and out('" + RelationshipLabelConstants.IS_PROPERTY_COLLECTION_PERMISSION_OF
        + "').code contains '" + propertyCollectionId + "'";
    
    Map<String, Object> propertyCollectionPermission = null;
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      return getDefaultPropertyCollectionPermission();
    }
    
    if (iterator.hasNext()) {
      Vertex propertyCollectionPermissionNode = iterator.next();
      propertyCollectionPermission = UtilClass.getMapFromNode(propertyCollectionPermissionNode);
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return propertyCollectionPermission;
  }
  
  private static Map<String, Object> getDefaultPropertyCollectionPermission()
  {
    Map<String, Object> propertyCollectionDefaultPermissionsMap = new HashMap<String, Object>();
    propertyCollectionDefaultPermissionsMap.put(IPropertyCollectionPermission.IS_EXPANDED, true);
    propertyCollectionDefaultPermissionsMap.put(IPropertyCollectionPermission.CAN_EDIT, true);
    propertyCollectionDefaultPermissionsMap.put(IPropertyCollectionPermission.IS_VISIBLE, true);
    return propertyCollectionDefaultPermissionsMap;
  }
  
  @Deprecated
  public static Map<String, Object> getPropertyPermission(String propertyId, String roleId,
      String templateId) throws NotFoundException, MultipleVertexFoundException
  {
    String query = "Select From " + VertexLabelConstants.PROPERTY_CAN_READ_PERMISSION
        + " where in('" + RelationshipLabelConstants.HAS_PROPERTY_PERMISSION + "').code contains '"
        + templateId + "' and in('" + RelationshipLabelConstants.HAS_ROLE_PROPERTY_PERMISSION
        + "').code contains '" + roleId + "' and out('"
        + RelationshipLabelConstants.IS_PROPERTY_PERMISSION_OF + "').code contains '" + propertyId
        + "'";
    
    Map<String, Object> propertyPermission = null;
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      return getDefaultPropertyPermission();
    }
    
    if (iterator.hasNext()) {
      Vertex propertyPermissionNode = iterator.next();
      propertyPermission = UtilClass.getMapFromNode(propertyPermissionNode);
      propertyPermission.remove(CommonConstants.CODE_PROPERTY);
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return propertyPermission;
  }
  
  private static Map<String, Object> getDefaultPropertyPermission()
  {
    Map<String, Object> propertyDefaultPermissionsMap = new HashMap<String, Object>();
    propertyDefaultPermissionsMap.put(IPropertyPermission.CAN_EDIT, true);
    propertyDefaultPermissionsMap.put(IPropertyPermission.IS_VISIBLE, true);
    return propertyDefaultPermissionsMap;
  }
  
  @Deprecated
  public static Map<String, Object> getRelationshipPermission(String relationshipId, String roleId,
      String templateId) throws NotFoundException, MultipleVertexFoundException
  {
    String query = "Select From " + VertexLabelConstants.RELATIONSHIP_CAN_READ_PERMISSION
        + " where in('" + RelationshipLabelConstants.HAS_RELATIONSHIP_PERMISSION
        + "').code contains '" + templateId + "' and in('"
        + RelationshipLabelConstants.HAS_ROLE_RELATIONSHIP_PERMISSION + "').code contains '"
        + roleId + "' and out('" + RelationshipLabelConstants.IS_RELATIONSHIP_PERMISSION_OF
        + "').code contains '" + relationshipId + "'";
    
    Map<String, Object> relationshipPermission = null;
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      return getDefaultRelationshipPermission();
    }
    
    if (iterator.hasNext()) {
      Vertex relationshipPermissionNode = iterator.next();
      relationshipPermission = UtilClass.getMapFromNode(relationshipPermissionNode);
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return relationshipPermission;
  }
  
  private static Map<String, Object> getDefaultRelationshipPermission()
  {
    Map<String, Object> relationshipDefaultPermissionsMap = new HashMap<String, Object>();
    relationshipDefaultPermissionsMap.put(IRelationshipPermission.CAN_ADD, true);
    relationshipDefaultPermissionsMap.put(IRelationshipPermission.CAN_DELETE, true);
    relationshipDefaultPermissionsMap.put(IRelationshipPermission.IS_VISIBLE, true);
    return relationshipDefaultPermissionsMap;
  }
  
  @Deprecated
  public static Map<String, Object> getHeaderPermission(String headerId, String roleId,
      String templateId) throws NotFoundException, MultipleVertexFoundException
  {
    String query = "Select From " + VertexLabelConstants.HEADER_PERMISSION + " where in('"
        + RelationshipLabelConstants.HAS_HEADER_PERMISSION + "').code contains '" + templateId
        + "' and in('" + RelationshipLabelConstants.HAS_ROLE_HEADER_PERMISSION
        + "').code contains '" + roleId + "' and out('"
        + RelationshipLabelConstants.IS_HEADER_PERMISSION_OF + "').code contains '" + headerId
        + "'";
    
    Map<String, Object> headerPermission = null;
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      return getDefaultHeaderPermission();
    }
    
    Vertex headerPermissionNode = iterator.next();
    headerPermission = UtilClass.getMapFromNode(headerPermissionNode);
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return headerPermission;
  }
  
  private static Map<String, Object> getDefaultHeaderPermission()
  {
    Map<String, Object> headerDefaultPermissionsMap = new HashMap<String, Object>();
    
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_NAME, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_EDIT_NAME, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_ICON, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_CHANGE_ICON, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_PRIMARY_TYPE, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_EDIT_PRIMARY_TYPE, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_ADDITIONAL_CLASSES, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_ADD_CLASSES, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_DELETE_CLASSES, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_TAXONOMIES, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_ADD_TAXONOMY, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_DELETE_TAXONOMY, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_STATUS_TAGS, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_EDIT_STATUS_TAG, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_CREATED_ON, true);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_LAST_MODIFIED_BY, true);
    
    return headerDefaultPermissionsMap;
  }
  
  public static Map<String, Object> getTabPermission(String tabId, String roleId, String templateId)
      throws NotFoundException, MultipleVertexFoundException
  {
    
    Vertex tabPermissionNode = getTabPermissionNode(tabId, roleId, templateId);
    if (tabPermissionNode == null) {
      return getDefaultTabPermission();
    }
    
    Map<String, Object> tabPermission = UtilClass.getMapFromNode(tabPermissionNode);
    
    return tabPermission;
  }
  
  private static Map<String, Object> getDefaultTabPermission()
  {
    Map<String, Object> tabDefaultPermissionsMap = new HashMap<String, Object>();
    tabDefaultPermissionsMap.put(ITabPermission.IS_VISIBLE, true);
    tabDefaultPermissionsMap.put(ITabPermission.CAN_CREATE, true);
    tabDefaultPermissionsMap.put(ITabPermission.CAN_EDIT, true);
    tabDefaultPermissionsMap.put(ITabPermission.CAN_DELETE, true);
    
    return tabDefaultPermissionsMap;
  }
  
  @Deprecated
  public static Vertex getPropertyCollectionPermissionNode(String propertyCollectionId,
      String roleId, String templateId) throws MultipleVertexFoundException
  {
    String query = "Select From " + VertexLabelConstants.PROPERTY_COLLECTION_CAN_READ_PERMISSION
        + " where in('" + RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_PERMISSION
        + "').code contains '" + templateId + "' and in('"
        + RelationshipLabelConstants.HAS_ROLE_PROPERTY_COLLECTION_PERMISSION + "').code contains '"
        + roleId + "' and out('" + RelationshipLabelConstants.IS_PROPERTY_COLLECTION_PERMISSION_OF
        + "').code contains '" + propertyCollectionId + "'";
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    Vertex propertyCollectionPermissionNode = null;
    if (iterator.hasNext()) {
      propertyCollectionPermissionNode = iterator.next();
    }
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return propertyCollectionPermissionNode;
  }
  
  @Deprecated
  public static Vertex getPropertyPermissionNode(String propertyId, String roleId,
      String templateId) throws MultipleVertexFoundException
  {
    String query = "Select From " + VertexLabelConstants.PROPERTY_CAN_EDIT_PERMISSION
        + " where in('" + RelationshipLabelConstants.HAS_PROPERTY_PERMISSION + "').code contains '"
        + templateId + "' and in('" + RelationshipLabelConstants.HAS_ROLE_PROPERTY_PERMISSION
        + "').code contains '" + roleId + "' and out('"
        + RelationshipLabelConstants.IS_PROPERTY_PERMISSION_OF + "').code contains '" + propertyId
        + "'";
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    Vertex propertyPermissionNode = null;
    if (iterator.hasNext()) {
      propertyPermissionNode = iterator.next();
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return propertyPermissionNode;
  }
  
  public static Vertex getTabPermissionNode(String tabId, String roleId, String templateId)
      throws MultipleVertexFoundException
  {
    String query = "Select From " + VertexLabelConstants.TAB_PERMISSION + " where in('"
        + RelationshipLabelConstants.HAS_TAB_PERMISSION + "').code contains '" + templateId
        + "' and in('" + RelationshipLabelConstants.HAS_ROLE_TAB_PERMISSION + "').code contains '"
        + roleId + "' and out('" + RelationshipLabelConstants.IS_TAB_PERMISSION_OF
        + "').code contains '" + tabId + "'";
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    Vertex tabPermissionNode = null;
    if (iterator.hasNext()) {
      tabPermissionNode = iterator.next();
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return tabPermissionNode;
  }
  
  @Deprecated
  public static Vertex getRelationshipPermissionNode(String relationshipId, String roleId,
      String templateId) throws NotFoundException, MultipleVertexFoundException
  {
    String query = "Select From " + VertexLabelConstants.RELATIONSHIP_CAN_READ_PERMISSION
        + " where in('" + RelationshipLabelConstants.HAS_RELATIONSHIP_PERMISSION
        + "').code contains '" + templateId + "' and in('"
        + RelationshipLabelConstants.HAS_ROLE_RELATIONSHIP_PERMISSION + "').code contains '"
        + roleId + "' and out('" + RelationshipLabelConstants.IS_RELATIONSHIP_PERMISSION_OF
        + "').code contains '" + relationshipId + "'";
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    
    Vertex relationshipPermissionNode = null;
    if (iterator.hasNext()) {
      relationshipPermissionNode = iterator.next();
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return relationshipPermissionNode;
  }
  
  public static Map<String, Object> getDefaultGlobalPermission()
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IKlassTaxonomyPermissions.CAN_CREATE, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_READ, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_EDIT, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DELETE, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DOWNLOAD, false);
    return returnMap;
  }
  
  public static Map<String, Object> getDisabledGlobalPermission()
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IKlassTaxonomyPermissions.CAN_CREATE, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_READ, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_EDIT, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DELETE, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DOWNLOAD, false);
    return returnMap;
  }
  
  public static Map<String, Object> getAllRightsGlobalPermission()
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IKlassTaxonomyPermissions.CAN_CREATE, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_READ, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_EDIT, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DELETE, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DOWNLOAD, true);
    return returnMap;
  }
  
  public static Map<String, Object> getDefaultPersonalTaskGlobalPermission()
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IKlassTaxonomyPermissions.CAN_CREATE, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_READ, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_EDIT, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DELETE, true);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DOWNLOAD, true);
    return returnMap;
  }
  
  /**
   * @param referencedPermissions
   * @param tabType
   */
  @Deprecated
  public static void fillNoRolePermissions(Map<String, Object> responseMap)
  {
    Map<String, Object> referencedPermissions = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    
    Map<String, Object> globalPermissionMap = GlobalPermissionUtils.getNoRoleGlobalPermission();
    referencedPermissions.put(IReferencedTemplatePermissionModel.GLOBAL_PERMISSION,
        globalPermissionMap);
    
    Map<String, Object> headerDefaultPermissionsMap = new HashMap<String, Object>();
    
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_NAME, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_EDIT_NAME, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_ICON, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_CHANGE_ICON, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_PRIMARY_TYPE, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_EDIT_PRIMARY_TYPE, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_ADDITIONAL_CLASSES, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_ADD_CLASSES, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_DELETE_CLASSES, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_TAXONOMIES, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_ADD_TAXONOMY, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_DELETE_TAXONOMY, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_STATUS_TAGS, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.CAN_EDIT_STATUS_TAG, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_CREATED_ON, false);
    headerDefaultPermissionsMap.put(IHeaderPermission.VIEW_LAST_MODIFIED_BY, false);
    
    referencedPermissions.put(IReferencedTemplatePermissionModel.HEADER_PERMISSION,
        headerDefaultPermissionsMap);
    
    Map<String, Object> tabDefaultPermissionsMap = new HashMap<String, Object>();
    tabDefaultPermissionsMap.put(ITabPermission.IS_VISIBLE, false);
    tabDefaultPermissionsMap.put(ITabPermission.CAN_CREATE, false);
    tabDefaultPermissionsMap.put(ITabPermission.CAN_EDIT, false);
    tabDefaultPermissionsMap.put(ITabPermission.CAN_DELETE, false);
    referencedPermissions.put(IReferencedTemplatePermissionModel.TAB_PERMISSION,
        tabDefaultPermissionsMap);
    
    Map<String, Object> referencedPropertyCollection = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS);
    if (referencedPropertyCollection != null) {
      referencedPermissions.put(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS,
          referencedPropertyCollection.keySet());
      referencedPermissions.put(
          IReferencedTemplatePermissionModel.EXPANDABLE_PROPERTY_COLLECTION_IDS,
          referencedPropertyCollection.keySet());
    }
  }
  
  public static Map<String, Object> getNoRoleGlobalPermission()
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IKlassTaxonomyPermissions.CAN_CREATE, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_READ, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_EDIT, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DELETE, false);
    returnMap.put(IKlassTaxonomyPermissions.CAN_DOWNLOAD, false);
    return returnMap;
  }
  
  public static void fillEntitiesAndKlassIdsAndTaxonomyIdsHavingReadPermission(Vertex userInRole,
      Map<String, Object> returnMap) throws Exception
  {
    
    Set<String> klassIdsHavingReadPermission = new HashSet<String>();
    returnMap.put(CommonConstants.KLASS_IDS_HAVING_RP, klassIdsHavingReadPermission);
    
    Set<String> taxonomyIdsHavingReadPermission = new HashSet<String>();
    returnMap.put(CommonConstants.TAXONOMY_IDS_HAVING_RP, taxonomyIdsHavingReadPermission);
    
    Set<String> allowedEntities = new HashSet<String>();
    returnMap.put(CommonConstants.ENTITIES, allowedEntities);
    
    klassIdsHavingReadPermission.addAll(getKlassIdsHavingReadPermission(userInRole));
    taxonomyIdsHavingReadPermission.addAll(getTaxonomyIdsHavingReadPermission(userInRole));
    allowedEntities.addAll(userInRole.getProperty(IRole.ENTITIES));
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
    }
  }
  
  /**
   * fill (module)entities and all klassIds and taxonomyIds having read
   * permissions
   *
   * @author Lokesh
   * @param userId
   * @param returnMap
   * @throws Exception
   */
  public static void fillEntitiesAndKlassIdsAndTaxonomyIdsHavingReadPermission(String userId,
      Map<String, Object> returnMap) throws Exception
  {
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    
    fillEntitiesAndKlassIdsAndTaxonomyIdsHavingReadPermission(userInRole, returnMap);
  }
  
  /**
   * @author Lokesh
   * @param userInRole
   * @return
   */
  public static Set<String> getAllowedEntities(Vertex userInRole)
  {
    Set<String> allowedEntities = new HashSet<>();
    allowedEntities.addAll(userInRole.getProperty(IRole.ENTITIES));
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
    }
    return allowedEntities;
  }
  
  /**
   * returns KlassIdsHavingReadPermissions and retuns "all" if klassIds is there
   *
   * @author Lokesh
   * @param roleNode
   * @param klassIds
   * @return
   * @throws Exception
   */
  public static Set<String> getKlassIdsHavingReadPermission(Vertex roleNode) throws Exception
  {
    Set<String> klassIdsHavingReadPermission = new HashSet<String>();
    
    Iterable<Vertex> klassNodesHavingRP = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TARGET_KLASSES);
    for (Vertex klassNode : klassNodesHavingRP) {
      String klassId = UtilClass.getCodeNew(klassNode);
      klassIdsHavingReadPermission.add(klassId);
    }
    if (klassIdsHavingReadPermission.isEmpty()) {
      Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
      klassNodesHavingRP = organizationNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_AVAILABLE_KLASSES);
      for (Vertex klassNode : klassNodesHavingRP) {
        String klassId = UtilClass.getCodeNew(klassNode);
        klassIdsHavingReadPermission.add(klassId);
      }
    }
    return klassIdsHavingReadPermission;
  }
  
  public static void fillGlobalPermissionOfEntitiesForRole(String entityId,
      Map<String, Object> globalPermissionToReturn, String roleId) throws Exception
  {
    
    if ((Boolean) globalPermissionToReturn.get(IGlobalPermission.CAN_CREATE)
        && (Boolean) globalPermissionToReturn.get(IGlobalPermission.CAN_READ)
        && (Boolean) globalPermissionToReturn.get(IGlobalPermission.CAN_EDIT)
        && (Boolean) globalPermissionToReturn.get(IGlobalPermission.CAN_DELETE)) {
      return;
    }
    Map<String, Object> permissionMap = GlobalPermissionUtils
        .getKlassAndTaxonomyPermission(entityId, roleId);
    GlobalPermissionUtils.mergeGlobalPermissons(globalPermissionToReturn, permissionMap);
  }
  
  public static void mergeGlobalPermissons(Map<String, Object> returnMap,
      Map<String, Object> permissionMap)
  {
    Boolean canCreate = (Boolean) permissionMap.get(IGlobalPermission.CAN_CREATE)
        || (Boolean) returnMap.get(IGlobalPermission.CAN_CREATE);
    Boolean canRead = (Boolean) permissionMap.get(IGlobalPermission.CAN_READ)
        || (Boolean) returnMap.get(IGlobalPermission.CAN_READ);
    Boolean canEdit = (Boolean) permissionMap.get(IGlobalPermission.CAN_EDIT)
        || (Boolean) returnMap.get(IGlobalPermission.CAN_EDIT);
    Boolean canDelete = (Boolean) permissionMap.get(IGlobalPermission.CAN_DELETE)
        || (Boolean) returnMap.get(IGlobalPermission.CAN_DELETE);
    Boolean canDownload = (Boolean) permissionMap.get(IGlobalPermission.CAN_DOWNLOAD)
        || (Boolean) returnMap.get(IGlobalPermission.CAN_DOWNLOAD);
    returnMap.put(IGlobalPermission.CAN_CREATE, canCreate);
    returnMap.put(IGlobalPermission.CAN_READ, canRead);
    returnMap.put(IGlobalPermission.CAN_EDIT, canEdit);
    returnMap.put(IGlobalPermission.CAN_DELETE, canDelete);
    returnMap.put(IGlobalPermission.CAN_DOWNLOAD, canDownload);
  }
  
  public static Map<String, Object> fillDefaultHeaderPermission()
  {
    Map<String, Object> headerPermissionToMerge = new HashMap<>();
    headerPermissionToMerge.put(IHeaderPermission.VIEW_NAME, false);
    headerPermissionToMerge.put(IHeaderPermission.CAN_EDIT_NAME, false);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_ICON, false);
    headerPermissionToMerge.put(IHeaderPermission.CAN_CHANGE_ICON, false);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_PRIMARY_TYPE, false);
    headerPermissionToMerge.put(IHeaderPermission.CAN_EDIT_PRIMARY_TYPE, false);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_ADDITIONAL_CLASSES, false);
    headerPermissionToMerge.put(IHeaderPermission.CAN_ADD_CLASSES, false);
    headerPermissionToMerge.put(IHeaderPermission.CAN_DELETE_CLASSES, false);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_TAXONOMIES, false);
    headerPermissionToMerge.put(IHeaderPermission.CAN_ADD_TAXONOMY, false);
    headerPermissionToMerge.put(IHeaderPermission.CAN_DELETE_TAXONOMY, false);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_STATUS_TAGS, false);
    headerPermissionToMerge.put(IHeaderPermission.CAN_EDIT_STATUS_TAG, false);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_CREATED_ON, false);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_LAST_MODIFIED_BY, false);
    return headerPermissionToMerge;
  }
  
  /**
   * @param roleId
   * @param templateId
   * @param propertyCollectionIds
   * @param referencedPermissions
   * @param canEdit
   * @throws Exception
   */
  public static void fillPropertyCollectionPermission(String roleId, String templateId,
      Set<String> propertyCollectionIds, Map<String, Object> referencedPermissions) throws Exception
  {
    Set<String> editablePCIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_COLLECTION_IDS);
    Set<String> expandablePCIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EXPANDABLE_PROPERTY_COLLECTION_IDS);
    Set<String> visiblePCIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS);
    
    for (String pCId : propertyCollectionIds) {
      Map<String, Object> permission = getPropertyCollectionPermission(pCId, roleId, templateId);
      if ((Boolean) permission.get(IPropertyCollectionPermission.CAN_EDIT)) {
        editablePCIds.add(pCId);
      }
      if ((Boolean) permission.get(IPropertyCollectionPermission.IS_EXPANDED)) {
        expandablePCIds.add(pCId);
      }
      if ((Boolean) permission.get(IPropertyCollectionPermission.IS_VISIBLE)) {
        visiblePCIds.add(pCId);
      }
    }
  }
  
  /**
   * @param roleId
   * @param templateId
   * @param entityIds
   * @param referencedPermissions
   * @param canEdit
   * @throws Exception
   */
  public static void fillPropertyPermission(String roleId, String templateId, Set<String> entityIds,
      Map<String, Object> referencedPermissions) throws Exception
  {
    
    Set<String> editableEntityIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_IDS);
    Set<String> visibleEntityIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_IDS);
    
    for (String entityId : entityIds) {
      Map<String, Object> permission = getPropertyPermission(entityId, roleId, templateId);
      if ((Boolean) permission.get(IPropertyPermission.CAN_EDIT)) {
        editableEntityIds.add(entityId);
      }
      if ((Boolean) permission.get(IPropertyPermission.IS_VISIBLE)) {
        visibleEntityIds.add(entityId);
      }
    }
  }
  
  public static void fillRelationshipPermission(String roleId, String templateId,
      Set<String> relationshipIds, Map<String, Object> referencedPermissions) throws Exception
  {
    Set<String> canAddRelationhipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.CAN_ADD_RELATIONSHIP_IDS);
    Set<String> canDeleteRelationhipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.CAN_DELETE_RELATIONSHIP_IDS);
    Set<String> visibleRelationhipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_RELATIONSHIP_IDS);
    
    for (String relationshipId : relationshipIds) {
      Map<String, Object> permission = GlobalPermissionUtils
          .getRelationshipPermission(relationshipId, roleId, templateId);
      if ((Boolean) permission.get(IRelationshipPermission.CAN_ADD)) {
        canAddRelationhipIds.add(relationshipId);
      }
      if ((Boolean) permission.get(IRelationshipPermission.CAN_DELETE)) {
        canDeleteRelationhipIds.add(relationshipId);
      }
      if ((Boolean) permission.get(IRelationshipPermission.IS_VISIBLE)) {
        visibleRelationhipIds.add(relationshipId);
      }
    }
  }
  
  public static Set<String> fillTaxonomyHavingReadPermission(String userId) throws Exception
  {
    Set<String> taxonomyIdsHavingRP = new HashSet<>();
    Vertex userNode = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    Iterable<Vertex> roleNodes = userNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    for (Vertex roleNode : roleNodes) {
      taxonomyIdsHavingRP.addAll(getTaxonomyIdsHavingReadPermission(roleNode));
    }
    return taxonomyIdsHavingRP;
  }
  
  public static Set<String> getTaxonomyIdsHavingReadPermission(Vertex roleNode) throws Exception
  {
    Set<String> taxonomyIdsHavingRP = new HashSet<>();
    Iterable<Vertex> taxonomyNodesHavingRP = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
    for (Vertex taxonomyNode : taxonomyNodesHavingRP) {
      String taxonomyId = UtilClass.getCodeNew(taxonomyNode);
      taxonomyIdsHavingRP.add(taxonomyId);
    }
    if (taxonomyIdsHavingRP.isEmpty()) {
      Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
      taxonomyNodesHavingRP = organizationNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES);
      for (Vertex taxonomyNode : taxonomyNodesHavingRP) {
        String taxonomyId = UtilClass.getCodeNew(taxonomyNode);
        taxonomyIdsHavingRP.add(taxonomyId);
      }
    }
    return taxonomyIdsHavingRP;
  }
  
  public static void fillTaxonomyIdsHavingReadPermission(Vertex roleNode,
      Set<String> taxonomyIdsHavingRP, Set<String> allTaxonomyIdsHavingRP) throws Exception
  {
    Iterable<Vertex> taxonomyNodesHavingRP = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
    for (Vertex taxonomyNode : taxonomyNodesHavingRP) {
      String taxonomyId = UtilClass.getCodeNew(taxonomyNode);
      taxonomyIdsHavingRP.add(taxonomyId);
      allTaxonomyIdsHavingRP.addAll(TaxonomyUtil.getOwnAndAllChildrenKlassIdsSet(taxonomyNode));
    }
    if (taxonomyIdsHavingRP.isEmpty()) {
      Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
      taxonomyNodesHavingRP = organizationNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES);
      for (Vertex taxonomyNode : taxonomyNodesHavingRP) {
        String taxonomyId = UtilClass.getCodeNew(taxonomyNode);
        taxonomyIdsHavingRP.add(taxonomyId);
        allTaxonomyIdsHavingRP.addAll(TaxonomyUtil.getOwnAndAllChildrenKlassIdsSet(taxonomyNode));
      }
    }
    if (taxonomyIdsHavingRP.isEmpty()) {
      List<Vertex> rootTaxonomyVertices = TaxonomyUtil.getAllRootLevelTaxonomyNodes();
      for (Vertex rootTaxonomy : rootTaxonomyVertices) {
        allTaxonomyIdsHavingRP.addAll(TaxonomyUtil.getOwnAndAllChildrenKlassIdsSet(rootTaxonomy));
      }
    }
  }
  
  public static void removeOtherPermissionNodes(Collection<String> klassOrTaconomies,
      List<String> roleRids, Boolean isTaxonomy)
  {
    List<String> taxonomyClass = new ArrayList<>();
    taxonomyClass.add(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
    
    String query = "select expand(out('"
        + RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS + "') [entityId not in "
        + EntityUtil.quoteIt(klassOrTaconomies) + "]) from " + roleRids.toString();
    
    Iterable<Vertex> permissionVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex permissionVertex : permissionVertices) {
      
      Vertex klassOrTaxonomyNode = permissionVertex
          .getVertices(Direction.IN,
              RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION)
          .iterator()
          .next();
      String vertexClass = klassOrTaxonomyNode.getProperty("@class");
      // Following condition need because above query gives returns both klass
      // and taxonomy but we
      // have to delete only one which is decided using isTaxonomy flag
      if (taxonomyClass.contains(vertexClass) == isTaxonomy) {
        permissionVertex.remove();
      }
    }
  }
  
  /**
   * @param propertyCollectionId
   * @param roleId
   * @param entityId
   * @param vertexType
   *          property_collection_can_edit_permission,
   *          property_collection_can_read_permission
   * @return
   * @throws MultipleVertexFoundException
   */
  public static Set<Vertex> getPropertyCollectionPermissionVertexIfExist(String roleId,
      String entityId, String vertexType)
  {
    
    List<String> compositeKey = Arrays.asList(roleId, entityId);
    String query = "SELECT FROM (SELECT EXPAND (rid) FROM INDEX:"
        + UtilClass.getIndexFromVertexType(vertexType) + " WHERE key = "
        + EntityUtil.quoteIt(compositeKey) + ")";
    Iterable<Vertex> x = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Set<Vertex> propertyCollectionPermissionVertices = new HashSet<>();
    if (x == null) {
      return propertyCollectionPermissionVertices;
    }
    
    for (Vertex propertyCollectionVertex : x) {
      propertyCollectionPermissionVertices.add(propertyCollectionVertex);
    }
    
    return propertyCollectionPermissionVertices;
  }
  
  public static Vertex getPropertyCollectionPermissionVertexIfExist(String propertyCollectionId,
      String roleId, String entityId, String vertexType) throws MultipleVertexFoundException
  {
    OIndex<?> index = UtilClass.getDatabase()
        .getMetadata()
        .getIndexManager()
        .getIndex(UtilClass.getIndexFromVertexType(vertexType));
    Iterable<ORecordId> x = (Iterable<ORecordId>) index
        .get(new OCompositeKey(Arrays.asList(roleId, entityId, propertyCollectionId)));
    if (x == null) {
      return null;
    }
    
    Vertex propertyCollectionPermission = null;
    Iterator<ORecordId> iterator = x.iterator();
    if (!iterator.hasNext()) {
      return null;
    }
    
    if (iterator.hasNext()) {
      ORecordId rid = iterator.next();
      propertyCollectionPermission = UtilClass.getGraph()
          .getVertex(rid);
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return propertyCollectionPermission;
  }
  
  /**
   * @param relationshipId
   * @param roleId
   * @param entityId
   * @param vertexType
   *          property_collection_can_edit_permission,
   *          property_collection_can_read_permission
   * @return
   * @throws MultipleVertexFoundException
   */
  public static Vertex getRelationshipPermissionVertexIfExist(String relationshipId, String roleId,
      String entityId, String vertexType) throws MultipleVertexFoundException
  {
    OIndex<?> index = UtilClass.getDatabase()
        .getMetadata()
        .getIndexManager()
        .getIndex(UtilClass.getIndexFromVertexType(vertexType));
    Iterable<ORecordId> x = (Iterable<ORecordId>) index
        .get(new OCompositeKey(Arrays.asList(roleId, entityId, relationshipId)));
    if (x == null) {
      return null;
    }
    
    Vertex relationshipPermission = null;
    Iterator<ORecordId> iterator = x.iterator();
    if (!iterator.hasNext()) {
      return null;
    }
    
    if (iterator.hasNext()) {
      ORecordId rid = iterator.next();
      relationshipPermission = UtilClass.getGraph()
          .getVertex(rid);
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return relationshipPermission;
  }
  
  public static Set<Vertex> getRelationshipPermissionVertexIfExist(String roleId, String entityId,
      String vertexType)
  {
    List<String> compositeKey = Arrays.asList(roleId, entityId);
    String query = "SELECT FROM (SELECT EXPAND (rid) FROM INDEX:"
        + UtilClass.getIndexFromVertexType(vertexType) + " WHERE key = "
        + EntityUtil.quoteIt(compositeKey) + ")";
    Iterable<Vertex> x = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Set<Vertex> relationshipPermissionVertices = new HashSet<>();
    if (x == null) {
      return relationshipPermissionVertices;
    }
    
    for (Vertex relationshipPermission : x) {
      relationshipPermissionVertices.add(relationshipPermission);
    }
    
    return relationshipPermissionVertices;
  }
  
  /**
   * @param propertyId
   * @param roleId
   * @param entityId
   * @param vertexType
   *          property_collection_can_edit_permission,
   *          property_collection_can_read_permission
   * @return
   * @throws MultipleVertexFoundException
   */
  public static Vertex getPropertyPermissionVertexIfExist(String propertyId, String roleId,
      String entityId, String vertexType) throws MultipleVertexFoundException
  {
    OIndex<?> index = UtilClass.getDatabase()
        .getMetadata()
        .getIndexManager()
        .getIndex(UtilClass.getIndexFromVertexType(vertexType));
    Iterable<ORecordId> x = (Iterable<ORecordId>) index
        .get(new OCompositeKey(Arrays.asList(roleId, entityId, propertyId)));
    
    Vertex propertyCollectionPermission = null;
    if (x == null) {
      return null;
    }
    Iterator<ORecordId> iterator = x.iterator();
    if (!iterator.hasNext()) {
      return null;
    }
    
    if (iterator.hasNext()) {
      ORecordId rid = iterator.next();
      propertyCollectionPermission = UtilClass.getGraph()
          .getVertex(rid);
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return propertyCollectionPermission;
  }
  
  public static Set<Vertex> getPropertyPermissionVertexIfExist(String roleId, String entityId,
      String vertexType)
  {
    List<String> compositeKey = Arrays.asList(roleId, entityId);
    String query = "SELECT FROM (SELECT EXPAND (rid) FROM INDEX:"
        + UtilClass.getIndexFromVertexType(vertexType) + " WHERE key = "
        + EntityUtil.quoteIt(compositeKey) + ")";
    Iterable<Vertex> x = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Set<Vertex> propertyPermissionVertices = new HashSet<>();
    
    if (x == null) {
      return propertyPermissionVertices;
    }
    
    for (Vertex propertyPermission : x) {
      propertyPermissionVertices.add(propertyPermission);
    }
    
    return propertyPermissionVertices;
  }
  
  public static Map<String, Object> getPropertyPermissionMap(String propertyId, String roleId,
      String entityId) throws NotFoundException, MultipleVertexFoundException
  {
    Vertex canReadPermission = getPropertyCollectionPermissionVertexIfExist(propertyId, roleId,
        entityId, VertexLabelConstants.PROPERTY_CAN_READ_PERMISSION);
    Vertex canEditPermission = getPropertyCollectionPermissionVertexIfExist(propertyId, roleId,
        entityId, VertexLabelConstants.PROPERTY_CAN_EDIT_PERMISSION);
    
    Boolean canEdit = canEditPermission == null ? true : false;
    Boolean canRead = canReadPermission == null ? true : false;
    
    Map<String, Object> propertyPermissionMap = new HashMap<>();
    propertyPermissionMap.put(IPropertyPermission.CAN_EDIT, canEdit);
    propertyPermissionMap.put(IPropertyPermission.IS_VISIBLE, canRead);
    propertyPermissionMap.put(IPropertyPermission.ENTITY_ID, entityId);
    propertyPermissionMap.put(IPropertyPermission.ROLE_ID, roleId);
    propertyPermissionMap.put(IPropertyPermission.PROPERTY_ID, propertyId);
    
    return propertyPermissionMap;
  }
  
  public static Map<String, Object> getRelationshipPermissionMap(String relationshipId,
      String roleId, String entityId, Boolean isRelationshipWithContext) throws NotFoundException, MultipleVertexFoundException
  {
    Vertex canReadPermission = getRelationshipPermissionVertexIfExist(relationshipId, roleId,
        entityId, VertexLabelConstants.RELATIONSHIP_CAN_READ_PERMISSION);
    Vertex canCreatePermission = getRelationshipPermissionVertexIfExist(relationshipId, roleId,
        entityId, VertexLabelConstants.RELATIONSHIP_CAN_ADD_PERMISSION);
    Vertex canDeletePermission = getRelationshipPermissionVertexIfExist(relationshipId, roleId,
        entityId, VertexLabelConstants.RELATIONSHIP_CAN_DELETE_PERMISSION);
    
    Boolean canEditContext = null;
    
    if(isRelationshipWithContext) {
    Vertex canEditContextPermission = getRelationshipPermissionVertexIfExist(relationshipId, roleId,
        entityId, VertexLabelConstants.RELATIONSHIP_CONTEXT_CAN_EDIT_PERMISSION);
      canEditContext = canEditContextPermission == null ? true : false;
    }
    
    Boolean canAdd = canCreatePermission == null ? true : false;
    Boolean canRead = canReadPermission == null ? true : false;
    Boolean canDelete = canDeletePermission == null ? true : false;
    
    Map<String, Object> relationshipPermissionMap = new HashMap<>();
    relationshipPermissionMap.put(IRelationshipPermission.CAN_ADD, canAdd);
    relationshipPermissionMap.put(IRelationshipPermission.CAN_DELETE, canDelete);
    relationshipPermissionMap.put(IRelationshipPermission.CAN_EDIT, canEditContext);
    relationshipPermissionMap.put(IRelationshipPermission.IS_VISIBLE, canRead);
    relationshipPermissionMap.put(IRelationshipPermission.ENTITY_ID, entityId);
    relationshipPermissionMap.put(IRelationshipPermission.ROLE_ID, roleId);
    relationshipPermissionMap.put(IRelationshipPermission.RELATIONSHIP_ID, relationshipId);
    
    return relationshipPermissionMap;
  }
  
  public static Map<String, Object> getHeaderPermission(Vertex entityNode, String roleId)
      throws NotFoundException, MultipleVertexFoundException
  {
    Map<String, Object> headerPermission = null;
    Vertex headerPermissionNode = null;
    
    Iterable<Vertex> headerNodes = entityNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_HEADER_PERMISSION);
    for (Vertex vertex : headerNodes) {
      Iterator<Vertex> iterator = vertex
          .getVertices(Direction.IN, RelationshipLabelConstants.HAS_ROLE_HEADER_PERMISSION)
          .iterator();
      Vertex roleNode = iterator.next();
      if (UtilClass.getCodeNew(roleNode)
          .equals(roleId)) {
        headerPermissionNode = vertex;
        break;
      }
    }
    
    if (headerPermissionNode == null) {
      return getDefaultHeaderPermission();
    }
    
    headerPermission = UtilClass.getMapFromNode(headerPermissionNode);
    
    return headerPermission;
  }
  
  public static List<String> getDefaultPhysicalCatalogs()
  {
    return Constants.PHYSICAL_CATALOG_IDS;
  }
  
  public static List<String> getDefaultPortals()
  {
    return Constants.PORTALS_IDS;
  }
  
  @Deprecated
  public static Map<String, Object> getKlassAndTaxonomyPermissionMap(String roleId, String entityId)
      throws NotFoundException, MultipleVertexFoundException
  {
    Vertex globlaPermissionNode = getGlobalPermissionVertex(roleId, entityId);
    
    if (globlaPermissionNode == null) {
      return getDefaultKlassTaxonomyPermission();
    }
    
    return UtilClass.getMapFromNode(globlaPermissionNode);
  }
  
  @Deprecated
  public static Vertex getGlobalPermissionVertex(String roleId, String entityId)
      throws MultipleVertexFoundException
  {
    String query = "SELECT FROM " + VertexLabelConstants.KLASS_TAXONOMY_GLOBAL_PERMISSIONS
        + " WHERE " + IGlobalPermission.ENTITY_ID + " = '" + entityId + "' AND "
        + IGlobalPermission.ROLE_ID + " = '" + roleId + "'";
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      return null;
    }
    
    Vertex propertyCollectionPermissionNode = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return propertyCollectionPermissionNode;
  }
  
  public static Map<String, Object> getPropertyCollectionPermissionMap(String propertyCollectionId,
      String roleId, String entityId) throws NotFoundException, MultipleVertexFoundException
  {
    Vertex canReadPermission = getPropertyCollectionPermissionVertexIfExist(propertyCollectionId,
        roleId, entityId, VertexLabelConstants.PROPERTY_COLLECTION_CAN_READ_PERMISSION);
    Vertex canEditPermission = getPropertyCollectionPermissionVertexIfExist(propertyCollectionId,
        roleId, entityId, VertexLabelConstants.PROPERTY_COLLECTION_CAN_EDIT_PERMISSION);
    
    Boolean canEdit = canEditPermission == null ? true : false;
    Boolean canRead = canReadPermission == null ? true : false;
    
    Map<String, Object> propertyCollectionPermissionMap = new HashMap<>();
    propertyCollectionPermissionMap.put(IPropertyCollectionPermission.CAN_EDIT, canEdit);
    propertyCollectionPermissionMap.put(IPropertyCollectionPermission.IS_VISIBLE, canRead);
    propertyCollectionPermissionMap.put(IPropertyCollectionPermission.ROLE_ID, roleId);
    propertyCollectionPermissionMap.put(IPropertyCollectionPermission.ENTITY_ID, entityId);
    propertyCollectionPermissionMap.put(IPropertyCollectionPermission.PROPERTY_COLLECTION_ID,
        propertyCollectionId);
    return propertyCollectionPermissionMap;
  }
  
  public static Vertex getGlobalPermissionVertexIfExist(String roleId, String entityId,
      String vertexType) throws MultipleVertexFoundException
  {
    OIndex<?> index = UtilClass.getDatabase()
        .getMetadata()
        .getIndexManager()
        .getIndex(UtilClass.getIndexFromVertexType(vertexType));
    Iterable<ORecordId> iterable = (Iterable<ORecordId>) index
        .get(new OCompositeKey(Arrays.asList(entityId, roleId)));
    
    if (iterable == null) {
      return null;
    }
    
    Vertex globalPermissionVertex = null;
    Iterator<ORecordId> iterator = iterable.iterator();
    if (!iterator.hasNext()) {
      return null;
    }
    
    ORecordId rid = iterator.next();
    globalPermissionVertex = UtilClass.getGraph()
        .getVertex(rid);
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return globalPermissionVertex;
  }
  
  /**
   * delete all permission nodes associated with that role
   *
   * @author Lokesh
   * @param klassNode
   */
  public static void deleteAllPermissionNodesForKlass(Vertex klassNode)
  {
    // delete delete property permission attached to role
    Iterable<Vertex> permissionIterable = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_PROPERTY_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
    
    // delete property collection permission attached to role
    permissionIterable = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
    
    // delete header permission attached to role
    permissionIterable = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_HEADER_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
    
    // delete relationship permission attached to role
    permissionIterable = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_RELATIONSHIP_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
    
    // delete template permission node attached to klass
    permissionIterable = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TEMPLATE_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  }
  
  public static void deletePropertyPermissionNode(String entityId, String propertyId,
      String vertexLabel) throws Exception
  {
    Vertex klassVertex = UtilClass.getVertexById(entityId, vertexLabel);
    Set<Vertex> klassVertices = new HashSet<>(Arrays.asList(klassVertex));
    deletePropertyPermissionNode(klassVertices, propertyId);
  }
  
  public static void deletePropertyPermissionNode(Set<Vertex> klassVertices, String propertyId)
  {
    Set<Object> rids = new HashSet<>();
    for (Vertex klassVertex : klassVertices) {
      rids.add(klassVertex.getId());
    }
    String query = "select from (select expand (out('"
        + RelationshipLabelConstants.HAS_PROPERTY_PERMISSION + "')) from " + rids + ") where out('"
        + RelationshipLabelConstants.IS_PROPERTY_PERMISSION_OF + "').code contains "
        + EntityUtil.quoteIt(propertyId);
    Iterable<Vertex> propertyPermissionNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex propertyPermissionNode : propertyPermissionNodes) {
      propertyPermissionNode.remove();
    }
  }
  
  public static void deletePropertyCollectionPermissionNode(String entityId,
      String propertyCollectionId, String vertexLabel) throws Exception
  {
    Vertex klassVertex = UtilClass.getVertexById(entityId, vertexLabel);
    Set<Vertex> klassVertices = new HashSet<>(Arrays.asList(klassVertex));
    deletePropertyCollectionPermissionNode(klassVertices, propertyCollectionId);
  }
  
  public static void deletePropertyCollectionPermissionNode(Set<Vertex> klassVertices,
      String propertyCollectionId)
  {
    Set<Object> rids = new HashSet<>();
    for (Vertex klassVertex : klassVertices) {
      rids.add(klassVertex.getId());
    }
    String query = "select from (select expand (out('"
        + RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_PERMISSION + "')) from " + rids
        + ") where out('" + RelationshipLabelConstants.IS_PROPERTY_COLLECTION_PERMISSION_OF
        + "').code contains " + EntityUtil.quoteIt(propertyCollectionId);
    Iterable<Vertex> propertyCollectionPermissionNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex propertyPermissionNode : propertyCollectionPermissionNodes) {
      propertyPermissionNode.remove();
    }
  }
  
  public static Map<String, Boolean> getFunctionPermission(Vertex roleNode) throws Exception
  {
    Map<String, Boolean> response = new HashMap<String, Boolean>();
    String[] functionPermissionEdgeTypes = { RelationshipLabelConstants.HAS_CLONE_PERMISSION,
        RelationshipLabelConstants.HAS_GRID_EDIT_PERMISSION,
        RelationshipLabelConstants.HAS_BULK_EDIT_PERMISSION,
        RelationshipLabelConstants.HAS_TRANSFER_PERMISSION,
        RelationshipLabelConstants.HAS_EXPORT_PERMISSION,
        RelationshipLabelConstants.HAS_IMPORT_PERMISSION,
        RelationshipLabelConstants.HAS_SHARE_PERMISSION,
        RelationshipLabelConstants.HAS_BULK_EDIT_PROPERTIES_PERMISSION,
        RelationshipLabelConstants.HAS_BULK_EDIT_TAXONOMIES_PERMISSION,
        RelationshipLabelConstants.HAS_BULK_EDIT_CLASSES_PERMISSION};

    for (String edgeType : functionPermissionEdgeTypes) {
      Iterable<Edge> functionPermissionForRoleNode = roleNode.getEdges(Direction.OUT, edgeType);
      Boolean canFunction = functionPermissionForRoleNode.iterator()
          .hasNext() ? false : true;

      response.put(getFunctionPermissionKey(edgeType), canFunction);
    }

    return response;
  }

  private static String getFunctionPermissionKey(String edgeType)
  {
    switch (edgeType) {
      case RelationshipLabelConstants.HAS_CLONE_PERMISSION:
        return IFunctionPermissionModel.CAN_CLONE;
      case RelationshipLabelConstants.HAS_GRID_EDIT_PERMISSION:
        return IFunctionPermissionModel.CAN_GRID_EDIT;
      case RelationshipLabelConstants.HAS_BULK_EDIT_PERMISSION:
        return IFunctionPermissionModel.CAN_BULK_EDIT;
      case RelationshipLabelConstants.HAS_TRANSFER_PERMISSION:
        return IFunctionPermissionModel.CAN_TRANSFER;
      case RelationshipLabelConstants.HAS_EXPORT_PERMISSION:
        return IFunctionPermissionModel.CAN_EXPORT;
      case RelationshipLabelConstants.HAS_SHARE_PERMISSION:
        return IFunctionPermissionModel.CAN_SHARE;
      case RelationshipLabelConstants.HAS_IMPORT_PERMISSION:
        return IFunctionPermissionModel.CAN_IMPORT;
      case RelationshipLabelConstants.HAS_BULK_EDIT_PROPERTIES_PERMISSION:
        return IFunctionPermissionModel.CAN_BULK_EDIT_PROPERTIES;
      case RelationshipLabelConstants.HAS_BULK_EDIT_TAXONOMIES_PERMISSION:
        return IFunctionPermissionModel.CAN_BULK_EDIT_TAXONOMIES;
      case RelationshipLabelConstants.HAS_BULK_EDIT_CLASSES_PERMISSION:
        return IFunctionPermissionModel.CAN_BULK_EDIT_CLASSES;
    }
    return null;
  }
  
  public static List<String> getRootMajorTaxonomyIds() {
    List<String> majorTaxonomyIds = new ArrayList<String>();
    String query = "select expand(rid) from index:" + VertexLabelConstants.ROOT_KLASS_TAXONOMY
        + ".isRootMajorTaxonomy where key = [true, '" + Constants.MAJOR_TAXONOMY + "']";
    
    Iterable<Vertex> majorTaxonomyNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex majorTaxonomyNode : majorTaxonomyNodes) {
      String majorTaxonomyId = UtilClass.getCId(majorTaxonomyNode);
      majorTaxonomyIds.add(majorTaxonomyId);
    }
    return majorTaxonomyIds;
  }
  
  public static void createOrDeletePropertyPermission(Vertex entityNode, Vertex roleNode, Vertex propertyNode, String vertexType,
      Boolean hasPermission) throws Exception
  {
    String entityId = UtilClass.getCodeNew(entityNode);
    String roleId = UtilClass.getCodeNew(roleNode);
    String propertyId = UtilClass.getCodeNew(propertyNode);
    
    Vertex propertyPermission = GlobalPermissionUtils.getPropertyPermissionVertexIfExist(propertyId, roleId, entityId, vertexType);
    if (hasPermission && propertyPermission != null) {
      propertyPermission.remove();
    }
    else if (!hasPermission && propertyPermission == null) {
      Map<String, Object> permissionMap = new HashMap<String, Object>();
      permissionMap.put(IPropertyPermission.ENTITY_ID, entityId);
      permissionMap.put(IPropertyPermission.PROPERTY_ID, propertyId);
      permissionMap.put(IPropertyPermission.ROLE_ID, roleId);
      
      OrientGraph graph = UtilClass.getGraph();
      OrientVertexType orientVvertexType = graph.getVertexType(vertexType);
      propertyPermission = UtilClass.createNode(permissionMap, orientVvertexType, new ArrayList<String>());
      
      entityNode.addEdge(RelationshipLabelConstants.HAS_PROPERTY_PERMISSION, propertyPermission);
      roleNode.addEdge(RelationshipLabelConstants.HAS_ROLE_PROPERTY_PERMISSION, propertyPermission);
      propertyPermission.addEdge(RelationshipLabelConstants.IS_PROPERTY_PERMISSION_OF, propertyNode);
    }
  }
  
  public static void createOrDeleteRelationshipPermission(Vertex entityNode,
      Vertex relationshipNode, Vertex roleNode, String vertexType, Boolean hasPermission)
      throws Exception
  {
    String entityId = UtilClass.getCodeNew(entityNode);
    String relationshipId = UtilClass.getCodeNew(relationshipNode);
    String roleId = UtilClass.getCodeNew(roleNode);
    
    Vertex relationshipPermission = GlobalPermissionUtils
        .getRelationshipPermissionVertexIfExist(relationshipId, roleId, entityId, vertexType);
    if (hasPermission && relationshipPermission != null) {
      relationshipPermission.remove();
    }
    else if (!hasPermission && relationshipPermission == null) {
      Map<String, Object> permissionMap = new HashMap<String, Object>();
      permissionMap.put(IRelationshipPermission.ENTITY_ID, entityId);
      permissionMap.put(IRelationshipPermission.RELATIONSHIP_ID, relationshipId);
      permissionMap.put(IRelationshipPermission.ROLE_ID, roleId);
      
      OrientGraph graph = UtilClass.getGraph();
      OrientVertexType orientVvertexType = graph.getVertexType(vertexType);
      relationshipPermission = UtilClass.createNode(permissionMap, orientVvertexType,
          new ArrayList<String>());
      
      entityNode.addEdge(RelationshipLabelConstants.HAS_RELATIONSHIP_PERMISSION,
          relationshipPermission);
      roleNode.addEdge(RelationshipLabelConstants.HAS_ROLE_RELATIONSHIP_PERMISSION,
          relationshipPermission);
      relationshipPermission.addEdge(RelationshipLabelConstants.IS_RELATIONSHIP_PERMISSION_OF,
          relationshipNode);
    }
  }
}
