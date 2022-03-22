package com.cs.config.strategy.plugin.usecase.globalpermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.relation.RoleNotFoundException;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesModel;
import com.cs.core.config.interactor.model.globalpermissions.ISaveGlobalPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.permission.GlobalPermissionNotFoundException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public abstract class AbstractCreateOrSaveGlobalPermission extends AbstractOrientPlugin{

  public AbstractCreateOrSaveGlobalPermission(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  public static final List<String> GLOBAL_PERMISSION_FIELDS_TO_EXCLUDE = Arrays
      .asList(IGlobalPermission.ENTITY_ID, IGlobalPermission.ROLE_ID);
  
  protected Map<String, Object> saveOrCreateGlobalPermission(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<Map<String, Object>> list = (List<Map<String, Object>>) requestMap.get(ISaveGlobalPermissionModel.LIST);
    String roleId = (String) requestMap.get(ISaveGlobalPermissionModel.ROLE_ID);
    Map<String,Object> functionPermission = (Map<String,Object>) requestMap.get(ISaveGlobalPermissionModel.FUNCTION_PERMISSION);
    if(functionPermission !=null && !functionPermission.isEmpty()) {
      handleFunctionPermission(roleId, functionPermission);
    }
    List<Map<String, Object>> responseList = new ArrayList<>();
    
    for (Map<String, Object> request : list) {
      String entityId = (String) request.get(ICreateOrSaveGlobalPermissionModel.ENTITY_ID);
      Map<String, Object> globalPermissionMap = (Map<String, Object>) request
          .get(ICreateOrSaveGlobalPermissionModel.GLOBAL_PERMISSION);
      String entityType = (String) globalPermissionMap.get(IGlobalPermission.TYPE);
      if (VertexLabelConstants.ENTITY_TYPE_TASK.equals(entityType)) {
        entityType = CommonConstants.GLOBAL_PERMISSION_ENTITY_TASKS;
      }
      Boolean canRead = (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_READ);
      if (!canRead) {
        globalPermissionMap.put(IGlobalPermission.CAN_CREATE, false);
        globalPermissionMap.put(IGlobalPermission.CAN_EDIT, false);
        globalPermissionMap.put(IGlobalPermission.CAN_DELETE, false);
        globalPermissionMap.put(IGlobalPermission.CAN_DOWNLOAD, false);
      }
      
      Boolean canCreate = (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_CREATE);
      Boolean canDelete = (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_DELETE);
      Boolean canEdit = (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_EDIT);
      Boolean canDownload = (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_DOWNLOAD);
      
      Vertex canCreatePermissionVertex = GlobalPermissionUtils.getGlobalPermissionVertexIfExist(
          roleId, entityId, VertexLabelConstants.GLOBAL_CAN_CREATE_PERMISSIONS);
      if (!canCreate && canCreatePermissionVertex == null) {
        canCreatePermissionVertex = createGlobalPermissionNode(roleId, entityId, entityType,
            VertexLabelConstants.GLOBAL_CAN_CREATE_PERMISSIONS);
      }
      else if (canCreate && canCreatePermissionVertex != null) {
        canCreatePermissionVertex.remove();
      }
      
      Vertex canDeletePermissionVertex = GlobalPermissionUtils.getGlobalPermissionVertexIfExist(
          roleId, entityId, VertexLabelConstants.GLOBAL_CAN_DELETE_PERMISSIONS);
      if (!canDelete && canDeletePermissionVertex == null) {
        canDeletePermissionVertex = createGlobalPermissionNode(roleId, entityId, entityType,
            VertexLabelConstants.GLOBAL_CAN_DELETE_PERMISSIONS);
      }
      else if (canDelete && canDeletePermissionVertex != null) {
        canDeletePermissionVertex.remove();
      }
      
      if (!entityType.equals(CommonConstants.ENTITY_KLASS_TYPE)
          && !entityType.equals(CommonConstants.TAXONOMY) && !entityType.equals(CommonConstants.ATTRIBUTION_TAXONOMY)) {
        Vertex canEditPermissionVertex = GlobalPermissionUtils.getGlobalPermissionVertexIfExist(roleId, entityId,
            VertexLabelConstants.GLOBAL_CAN_EDIT_PERMISSIONS);
        if (!canEdit && canEditPermissionVertex == null) {
          canEditPermissionVertex = createGlobalPermissionNode(roleId, entityId, entityType,
              VertexLabelConstants.GLOBAL_CAN_EDIT_PERMISSIONS);
        }
        else if (canEdit && canEditPermissionVertex != null) {
          canEditPermissionVertex.remove();
        }
        
        Vertex canReadPermissionVertex = GlobalPermissionUtils.getGlobalPermissionVertexIfExist(roleId, entityId,
            VertexLabelConstants.GLOBAL_CAN_READ_PERMISSIONS);
        if (!canRead && canReadPermissionVertex == null) {
          canReadPermissionVertex = createGlobalPermissionNode(roleId, entityId, entityType,
              VertexLabelConstants.GLOBAL_CAN_READ_PERMISSIONS);
        }
        else if (canRead && canReadPermissionVertex != null) {
          canReadPermissionVertex.remove();
        }
      }
      
      Vertex canDownloadPermissionVertex = GlobalPermissionUtils.getGlobalPermissionVertexIfExist(
          roleId, entityId, VertexLabelConstants.ASSET_CAN_DOWNLOAD_PERMISSIONS);
      if (!canDownload && canDownloadPermissionVertex == null) {
        canDownloadPermissionVertex = createGlobalPermissionNode(roleId, entityId, entityType,
            VertexLabelConstants.ASSET_CAN_DOWNLOAD_PERMISSIONS);
      }
      else if (canDownload && canDownloadPermissionVertex != null) {
        canDownloadPermissionVertex.remove();
      }
      
      graph.commit();
      
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put(IGetGlobalPermissionWithAllowedTemplatesModel.ID, entityId);
      responseMap.put(IGetGlobalPermissionWithAllowedTemplatesModel.GLOBAL_PERMISSION,
          globalPermissionMap);
      responseList.add(responseMap);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(ICreateOrSaveGlobalPermissionResponseModel.GLOBAL_PERMISSION_WITH_ALLOWED_TEMPLATES, responseList);
    returnMap.put(ICreateOrSaveGlobalPermissionResponseModel.FUNCTION_PERMISSION, functionPermission);
    
    return returnMap;
  }
  
  private void handleFunctionPermission(String roleId, Map<String, Object> functionPermission)
      throws Exception
  {
    Boolean canClone = (Boolean) functionPermission.get(IFunctionPermissionModel.CAN_CLONE);
    Boolean canGridEdit = (Boolean) functionPermission.get(IFunctionPermissionModel.CAN_GRID_EDIT);
    Boolean canBulkEdit = (Boolean) functionPermission.get(IFunctionPermissionModel.CAN_BULK_EDIT);
    Boolean canTransfer = (Boolean) functionPermission.get(IFunctionPermissionModel.CAN_TRANSFER);
    Boolean canExport = (Boolean) functionPermission.get(IFunctionPermissionModel.CAN_EXPORT);
    Boolean canShare = (Boolean) functionPermission.get(IFunctionPermissionModel.CAN_SHARE);
    Boolean canImport = (Boolean) functionPermission.get(IFunctionPermissionModel.CAN_IMPORT);
    
    Vertex roleNode = UtilClass.getVertexByIndexedId(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    if (canClone != null) {
      saveFunctionPermission(canClone, VertexLabelConstants.CAN_CLONE_PERMISSION,
          RelationshipLabelConstants.HAS_CLONE_PERMISSION, roleNode);
    }
    if (canGridEdit != null) {
      saveFunctionPermission(canGridEdit, VertexLabelConstants.CAN_GRID_EDIT_PERMISSION,
          RelationshipLabelConstants.HAS_GRID_EDIT_PERMISSION, roleNode);
    }
    if (canBulkEdit != null) {
      saveFunctionPermission(canBulkEdit, VertexLabelConstants.CAN_BULK_EDIT_PERMISSION,
          RelationshipLabelConstants.HAS_BULK_EDIT_PERMISSION, roleNode);
    }
    if (canTransfer != null) {
      saveFunctionPermission(canTransfer, VertexLabelConstants.CAN_TRANSFER_PERMISSION,
          RelationshipLabelConstants.HAS_TRANSFER_PERMISSION, roleNode);
    }
    if (canExport != null) {
      saveFunctionPermission(canExport, VertexLabelConstants.CAN_EXPORT_PERMISSION,
          RelationshipLabelConstants.HAS_EXPORT_PERMISSION, roleNode);
    }
    if (canShare != null) {
      saveFunctionPermission(canShare, VertexLabelConstants.CAN_SHARE_PERMISSION,
          RelationshipLabelConstants.HAS_SHARE_PERMISSION, roleNode);
    }
    
    if (canImport != null) {
      saveFunctionPermission(canImport, VertexLabelConstants.CAN_IMPORT_PERMISSION,
          RelationshipLabelConstants.HAS_IMPORT_PERMISSION, roleNode);
    }
    
    // Handle bulk edit functions permissions.
    handleBulkEditFunctionsPermissions(functionPermission, roleNode);
  }

  /**
   * Handle bulk edit function permissions for properties, taxonomies and
   * classes separately.
   * 
   * @param functionPermission
   * @param roleNode
   * @throws Exception
   */
  private void handleBulkEditFunctionsPermissions(Map<String, Object> functionPermission, Vertex roleNode) 
      throws Exception
  {
    Boolean canBulkEditProperties = (Boolean) functionPermission.get(IFunctionPermissionModel.CAN_BULK_EDIT_PROPERTIES);
    Boolean canBulkEditTaxonomies = (Boolean) functionPermission.get(IFunctionPermissionModel.CAN_BULK_EDIT_TAXONOMIES);
    Boolean canBulkEditClasses = (Boolean) functionPermission.get(IFunctionPermissionModel.CAN_BULK_EDIT_CLASSES);
    
    if (canBulkEditProperties != null) {
      saveFunctionPermission(canBulkEditProperties, VertexLabelConstants.CAN_BULK_EDIT_PROPERTIES_PERMISSION,
          RelationshipLabelConstants.HAS_BULK_EDIT_PROPERTIES_PERMISSION, roleNode);
    }
    
    if (canBulkEditTaxonomies != null) {
      saveFunctionPermission(canBulkEditTaxonomies, VertexLabelConstants.CAN_BULK_EDIT_TAXONOMIES_PERMISSION,
          RelationshipLabelConstants.HAS_BULK_EDIT_TAXONOMIES_PERMISSION, roleNode);
    }
    
    if (canBulkEditClasses != null) {
      saveFunctionPermission(canBulkEditClasses, VertexLabelConstants.CAN_BULK_EDIT_CLASSES_PERMISSION,
          RelationshipLabelConstants.HAS_BULK_EDIT_CLASSES_PERMISSION, roleNode);
    }
  }
  
  private Vertex createGlobalPermissionNode(String roleId, String entityId, String entityType,
      String vertexLabel) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(vertexLabel);
    Map<String, Object> globalPermissionMap = new HashMap<>();
    globalPermissionMap.put(IGlobalPermission.ROLE_ID, roleId);
    globalPermissionMap.put(IGlobalPermission.ENTITY_ID, entityId);
    globalPermissionMap.put(IGlobalPermission.TYPE, entityType);
    Vertex globalPermissionNode = UtilClass.createNode(globalPermissionMap, vertexType,
        new ArrayList<>());
    
    // link entity
    String entityVertexLabel = getEntityLabelByType(entityType);
    Vertex entityNode = UtilClass.getVertexById(entityId, entityVertexLabel);
    entityNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION,
        globalPermissionNode);
    
    // link role
    try {
      Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      roleNode.addEdge(RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS,
          globalPermissionNode);
    }
    catch (NotFoundException ex) {
      throw new RoleNotFoundException();
    }
    
    return globalPermissionNode;
  }
  
  protected void prepareResponse(String id, String entityId, String entityType,
      Map<String, Object> responseMap) throws Exception
  {
    Vertex globalPermissionNode = null;
    try {
      globalPermissionNode = UtilClass.getVertexById(id,
          VertexLabelConstants.KLASS_TAXONOMY_GLOBAL_PERMISSIONS);
    }
    catch (NotFoundException e) {
      throw new GlobalPermissionNotFoundException();
    }
    Set<String> allowedTemplateIds = new HashSet<>();
    Iterable<Vertex> allowedTemplates = globalPermissionNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_ALLOWED_TEMPLATE);
    for (Vertex allowedTemplate : allowedTemplates) {
      allowedTemplateIds.add(allowedTemplate.getProperty(CommonConstants.CODE_PROPERTY));
    }
    Iterator<Vertex> defaultTemplateIterator = globalPermissionNode
        .getVertices(Direction.OUT,
            RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_DEFAULT_TEMPLATE)
        .iterator();
    String defaultTemplateId = null;
    if (defaultTemplateIterator.hasNext()) {
      Vertex defaultTemplateNode = defaultTemplateIterator.next();
      defaultTemplateId = defaultTemplateNode.getProperty(CommonConstants.CODE_PROPERTY);
    }
    
    // in response sending entityId against id field
    responseMap.put(IGetGlobalPermissionWithAllowedTemplatesModel.ID, entityId);
    responseMap.put(IGetGlobalPermissionWithAllowedTemplatesModel.GLOBAL_PERMISSION,
        UtilClass.getMapFromNode(globalPermissionNode));
    responseMap.put(IGetGlobalPermissionWithAllowedTemplatesModel.ALLOWED_TEMPLATES,
        allowedTemplateIds);
    responseMap.put(IGetGlobalPermissionWithAllowedTemplatesModel.DEFAULT_TEMPLATE,
        defaultTemplateId);
  }
  
  protected String getEntityLabelByType(String type)
  {
    switch (type) {
      case CommonConstants.GLOBAL_PERMISSION_ENTITY_KLASS:
        return VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS;
      
      case CommonConstants.GLOBAL_PERMISSION_ENTITY_TAXONOMY:
      case CommonConstants.ATTRIBUTION_TAXONOMY:
        return VertexLabelConstants.ROOT_KLASS_TAXONOMY;
      
      case CommonConstants.GLOBAL_PERMISSION_ENTITY_CONTEXT:
        return VertexLabelConstants.VARIANT_CONTEXT;
      
      case CommonConstants.GLOBAL_PERMISSION_ENTITY_TASKS:
        return VertexLabelConstants.ENTITY_TYPE_TASK;
      
      default:
        return null;
    }
  }
  
  private void saveFunctionPermission(Boolean canFunction, String VertexClass, String EdgeClass,
      Vertex roleNode) throws Exception
  {
    if (canFunction) {
      Iterable<Edge> functionPermissionForRoleNodes = roleNode.getEdges(Direction.OUT, EdgeClass);
      Iterator<Edge> edgeIterator = functionPermissionForRoleNodes.iterator();
      while (edgeIterator.hasNext()) {
        Edge hasFunctionEdge = edgeIterator.next();
        Vertex canFunctionVertex = hasFunctionEdge.getVertex(Direction.IN);
        canFunctionVertex.remove();
      }
    }
    else {
      Iterable<Edge> functionPermissionForRoleNodes = roleNode.getEdges(Direction.OUT, EdgeClass);
      if (!functionPermissionForRoleNodes.iterator()
          .hasNext()) {
        OrientVertexType canFunctionVertexType = UtilClass.getOrCreateVertexType(VertexClass);
        Vertex canFunctionPermissionNode = UtilClass.createNode(new HashMap<String, Object>(),
            canFunctionVertexType, new ArrayList<>());
        roleNode.addEdge(EdgeClass, canFunctionPermissionNode);
      }
    }
  }
}
