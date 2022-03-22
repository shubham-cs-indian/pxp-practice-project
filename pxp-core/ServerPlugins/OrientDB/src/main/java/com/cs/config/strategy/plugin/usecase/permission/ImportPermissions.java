package com.cs.config.strategy.plugin.usecase.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.globalpermission.AbstractCreateOrSaveGlobalPermission;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.template.IPropertyCollectionPermission;
import com.cs.core.config.interactor.entity.template.IPropertyPermission;
import com.cs.core.config.interactor.entity.template.IRelationshipPermission;
import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.ISaveGlobalPermissionModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.config.interactor.model.template.ISaveTemplatePermissionModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.di.workflow.constants.DiConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class ImportPermissions extends AbstractCreateOrSaveGlobalPermission {
  
  public ImportPermissions(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ImportPermissions/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> permissions = (List<Map<String, Object>>) requestMap.get("list");
    List<Map<String, Object>> successfullyImportedPermissions = new ArrayList<>();
    List<Map<String, Object>> failedPermissions = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    for (Map<String, Object> permission : permissions) {
      try {
        // set entity type and entity id
        permission.put(ISaveTemplatePermissionModel.ENTITY_ID, (String) permission.get(CommonConstants.CODE_PROPERTY));
        permission.put(ISaveTemplatePermissionModel.ENTITY_TYPE, (String) permission.get(DiConstants.PERMISSION_TYPE));
       
        manageGlobalPermission(permission);
        manageHeaderPermission(permission);
        managePropertyCollectionPermission(permission);
        managePropertyPermission(permission);
        manageRelationshipPermission(permission);
        
        UtilClass.getGraph().commit();
        
        Map<String, Object> successMap = new HashMap<>();
        successMap.put(CommonConstants.CODE_PROPERTY, permission.get(CommonConstants.CODE_PROPERTY));
        successMap.put(DiConstants.PERMISSION_TYPE, permission.get(DiConstants.PERMISSION_TYPE));
        successfullyImportedPermissions.add(successMap);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) permission.get(CommonConstants.CODE_PROPERTY));
        
        Map<String, Object> failedKlassMap = new HashMap<>();
        failedKlassMap.put(ISummaryInformationModel.ID, permission.get(CommonConstants.CODE_PROPERTY));
        failedKlassMap.put(DiConstants.PERMISSION_TYPE, (String) permission.get(DiConstants.PERMISSION_TYPE));
        failedPermissions.add(failedKlassMap);
      }
    }
    
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, successfullyImportedPermissions);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedPermissions);
    return result;
  }
  
  /**
   * @param permission
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void managePropertyCollectionPermission(Map<String, Object> permission) throws Exception
  {
    String entityId = (String) permission.get(CommonConstants.CODE_PROPERTY);
    String roleId = (String) permission.get(ISaveTemplatePermissionModel.ROLE_ID);
    String entityType = (String) permission.get(ISaveTemplatePermissionModel.ENTITY_TYPE);
    
    Vertex entityNode = UtilClass.getVertexById(entityId, EntityUtil.getVertexLabelByEntityType(entityType));
    Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    
    Iterator<Vertex> klassSectionNodes = entityNode.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF).iterator();
    while(klassSectionNodes.hasNext()) {
      Iterator<Vertex> pcNodes = klassSectionNodes.next().getVertices(Direction.IN, RelationshipLabelConstants.PROPERTY_COLLECTION_OF).iterator();
      if(pcNodes.hasNext()) {
        Vertex propertyCollection = pcNodes.next();
        List<String> propertyIds = new ArrayList<>();
        propertyIds.addAll(propertyCollection.getProperty(IPropertyCollection.ATTRIBUTE_IDS));
        propertyIds.addAll(propertyCollection.getProperty(IPropertyCollection.TAG_IDS));
        
        List<Map<String, Object>> modifiedPropertyPermission = (List<Map<String, Object>>) permission.get(DiConstants.PROPERTY_PERMISSIONS);
        Boolean isVisible = checkPermissionOfPC(modifiedPropertyPermission, IPropertyPermission.IS_VISIBLE, entityNode, new ArrayList<>(propertyIds));
        Boolean canEdit = checkPermissionOfPC(modifiedPropertyPermission, IPropertyPermission.CAN_EDIT, entityNode, new ArrayList<>(propertyIds));
        
        createOrDeletePropertyCollectionPermission(entityNode, roleNode, propertyCollection, canEdit,
            VertexLabelConstants.PROPERTY_COLLECTION_CAN_EDIT_PERMISSION);
        createOrDeletePropertyCollectionPermission(entityNode, roleNode, propertyCollection,
            isVisible, VertexLabelConstants.PROPERTY_COLLECTION_CAN_READ_PERMISSION);
      }
    }
  }

  /**
   * Manage global permission here
   * 
   * @param permission
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void manageGlobalPermission(Map<String, Object> permission) throws Exception
  {
    Map<String, Object> globalPermission = (Map<String, Object>) permission.get(DiConstants.GLOBAL_PERMISSION);
    if (globalPermission == null || globalPermission.isEmpty()) {
      return;
    }
    
    globalPermission.put(IGlobalPermission.TYPE, getVertexTypeByEntityType((String) permission.get(DiConstants.PERMISSION_TYPE)));
    
    if(globalPermission.get(IGlobalPermission.CAN_READ) == null) {
      globalPermission.put(IGlobalPermission.CAN_READ, true);
    }
    
    if(globalPermission.get(IGlobalPermission.CAN_EDIT) == null) {
      globalPermission.put(IGlobalPermission.CAN_EDIT, true);
    }
    
    if(globalPermission.get(IGlobalPermission.CAN_DOWNLOAD) == null) {
      globalPermission.put(IGlobalPermission.CAN_DOWNLOAD, true);
    }
    
    HashMap<String, Object> globalPermissionMap = new HashMap<>();
    globalPermissionMap.put(ISaveTemplatePermissionModel.ENTITY_ID, (String) permission.get(ISaveTemplatePermissionModel.ENTITY_ID));
    globalPermissionMap.put(ICreateOrSaveGlobalPermissionModel.GLOBAL_PERMISSION, globalPermission);
   
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(ISaveGlobalPermissionModel.LIST, Arrays.asList(globalPermissionMap));
    requestMap.put(ISaveGlobalPermissionModel.FUNCTION_PERMISSION, new HashMap<>());
    requestMap.put(ISaveTemplatePermissionModel.ROLE_ID, (String) permission.get(ISaveTemplatePermissionModel.ROLE_ID));
    saveOrCreateGlobalPermission(requestMap);
  }
 
  
  /**
   * Manage general information permissions
   * 
   * @param requestMap
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void manageHeaderPermission(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> headerPermission = (Map<String, Object>) requestMap.get(ISaveTemplatePermissionModel.HEADER_PERMISSION);
    if (headerPermission == null || headerPermission.isEmpty()) {
      return;
    }
    
    String entityId = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_ID);
    String roleId = (String) requestMap.get(ISaveTemplatePermissionModel.ROLE_ID);
    String entityType = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_TYPE);
    headerPermission.put(IPropertyCollectionPermission.ENTITY_ID, entityId);
    headerPermission.put(IPropertyCollectionPermission.ROLE_ID, roleId);
    
    Vertex entityNode = UtilClass.getVertexById(entityId, EntityUtil.getVertexLabelByEntityType(entityType));
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> resultIterable = graph
        .command(new OCommandSQL("Select from " + VertexLabelConstants.HEADER_PERMISSION + " where " + CommonConstants.ENTITY_ID_PROPERTY + " = '"
            + entityId + "' and " + CommonConstants.ROLE_ID_PROPERY + " = '" + roleId +"'"))
        .execute();
    Vertex headerPermissionNode = UtilClass.getVertexFromIterator(resultIterable.iterator(), false);
    if (headerPermissionNode == null) {
      // If node not found create new one
      Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      
      OrientVertexType vertexType = graph.getVertexType(VertexLabelConstants.HEADER_PERMISSION);
      Vertex permissionNode = UtilClass.createNode(headerPermission, vertexType, new ArrayList<String>());
      
      entityNode.addEdge(RelationshipLabelConstants.HAS_HEADER_PERMISSION, permissionNode);
      roleNode.addEdge(RelationshipLabelConstants.HAS_ROLE_HEADER_PERMISSION, permissionNode);
    }
    else {
      UtilClass.saveNode(headerPermission, headerPermissionNode, new ArrayList<String>());
    }
  }
  
  /**
   * Manage property Permission
   * 
   * @param requestMap
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void managePropertyPermission(Map<String, Object> requestMap) throws Exception
  {
    String entityId = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_ID);
    String roleId = (String) requestMap.get(ISaveTemplatePermissionModel.ROLE_ID);
    String entityType = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_TYPE);
    
    Vertex entityNode = UtilClass.getVertexById(entityId, EntityUtil.getVertexLabelByEntityType(entityType));
    Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    
    List<Map<String, Object>> modifiedPropertyPermission = (List<Map<String, Object>>) requestMap.get(DiConstants.PROPERTY_PERMISSIONS);
    for (Map<String, Object> propertyPermissionMap : modifiedPropertyPermission) {
      String id = (String) propertyPermissionMap.get(IPropertyPermission.PROPERTY_ID);
      String type = (String) propertyPermissionMap.get("propertyType");
      Vertex propertyNode = UtilClass.getVertexById(id, EntityUtil.getVertexLabelByEntityType(type));
      
      Boolean canEdit = (Boolean) propertyPermissionMap.get(IPropertyPermission.CAN_EDIT);
      Boolean isVisible = (Boolean) propertyPermissionMap.get(IPropertyPermission.IS_VISIBLE);
      
      GlobalPermissionUtils.createOrDeletePropertyPermission(entityNode, roleNode, propertyNode, VertexLabelConstants.PROPERTY_CAN_READ_PERMISSION, isVisible);
      GlobalPermissionUtils.createOrDeletePropertyPermission(entityNode, roleNode, propertyNode, VertexLabelConstants.PROPERTY_CAN_EDIT_PERMISSION, canEdit);
    }
  }
  
  /**
   * Manage Relationship permission
   * @param requestMap
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void manageRelationshipPermission(Map<String, Object> requestMap) throws Exception
  {
    String entityId = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_ID);
    String roleId = (String) requestMap.get(ISaveTemplatePermissionModel.ROLE_ID);
    String entityType = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_TYPE);
    
    Vertex entityNode = UtilClass.getVertexById(entityId, EntityUtil.getVertexLabelByEntityType(entityType));
    Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    
    List<Map<String, Object>> modifiedRelationshipPermission = (List<Map<String, Object>>) requestMap.get(DiConstants.RELATIONSHIP_PERMISSIONS);
    for (Map<String, Object> relationshipPermissionMap : modifiedRelationshipPermission) {
      String id = (String) relationshipPermissionMap.get(CommonConstants.CODE_PROPERTY);
      Vertex relationshipNode = UtilClass.getVertexById(id, VertexLabelConstants.ROOT_RELATIONSHIP);
      
      Boolean canAdd = (Boolean) relationshipPermissionMap.get(IRelationshipPermission.CAN_ADD);
      Boolean canDelete = (Boolean) relationshipPermissionMap.get(IRelationshipPermission.CAN_DELETE);
      Boolean isVisible = (Boolean) relationshipPermissionMap.get(IRelationshipPermission.IS_VISIBLE);
      Boolean canEditContext = (Boolean) relationshipPermissionMap.get(IRelationshipPermission.CAN_EDIT);
      
      GlobalPermissionUtils.createOrDeleteRelationshipPermission(entityNode, relationshipNode, roleNode, VertexLabelConstants.RELATIONSHIP_CAN_READ_PERMISSION,
          isVisible);
      GlobalPermissionUtils.createOrDeleteRelationshipPermission(entityNode, relationshipNode, roleNode, VertexLabelConstants.RELATIONSHIP_CAN_ADD_PERMISSION,
          canAdd);
      GlobalPermissionUtils.createOrDeleteRelationshipPermission(entityNode, relationshipNode, roleNode, VertexLabelConstants.RELATIONSHIP_CAN_DELETE_PERMISSION,
          canDelete);
      
      if (canEditContext != null)
        GlobalPermissionUtils.createOrDeleteRelationshipPermission(entityNode, relationshipNode, roleNode,
            VertexLabelConstants.RELATIONSHIP_CONTEXT_CAN_EDIT_PERMISSION, canEditContext);
    }
  }
  
  private String getVertexTypeByEntityType(String entityType)
  {
    switch (entityType) {
      case CommonConstants.ARTICLE_ENTITY:
      case CommonConstants.ASSET_ENTITY:
      case CommonConstants.TARGET:
      case CommonConstants.SUPPLIER_ENTITY:
      case CommonConstants.TEXT_ASSET_ENTITY:
        return CommonConstants.ENTITY_KLASS_TYPE;
      case CommonConstants.ATTRIBUTION_TAXONOMY:
      case CommonConstants.MASTER_TAXONOMY:
        return CommonConstants.TAXONOMY;
      case CommonConstants.TASK:
        return VertexLabelConstants.ENTITY_TYPE_TASK;
      default:
        break;
    }
    return null;
  }
  
  /**
   * Check the permission of properties and accordingly set the permission for the property collection
   * 
   * @param modifiedPropertiesPermission
   * @param permissionLabel
   * @param entityNode
   * @param propertyIds
   * @return
   */
  private Boolean checkPermissionOfPC(List<Map<String, Object>> modifiedPropertiesPermission, String permissionLabel,
      Vertex entityNode, List<String> propertyIds)
  {
    // Check excel input properties permission and accordingly set the permission for property collection
    boolean isPermssion = false;
    for (Map<String, Object> modifiedPropertyPermission : modifiedPropertiesPermission) {
      String id = (String) modifiedPropertyPermission.get(IPropertyPermission.PROPERTY_ID);
      if (propertyIds.contains(id)) {
        propertyIds.remove(id);
        if (IPropertyPermission.IS_VISIBLE.equals(permissionLabel) && (Boolean) modifiedPropertyPermission.get(permissionLabel)) {
          isPermssion = true;
        }
        else if (IPropertyPermission.CAN_EDIT.equals(permissionLabel)
            && (Boolean) modifiedPropertyPermission.get(IPropertyPermission.IS_VISIBLE)
            && (Boolean) modifiedPropertyPermission.get(permissionLabel)) {
          isPermssion = true;
        }
      }
    }
    
    // Check existing  properties permission and accordingly set the permission for property collection
    Iterable<Vertex> propertiesPermssionNode = entityNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY_PERMISSION);
    for(Vertex propertyPermission : propertiesPermssionNode) {
      String propertyId = propertyPermission.getProperty(IPropertyPermission.PROPERTY_ID);
      if(propertyIds.contains(propertyId)) {
        propertyIds.remove(propertyId);
      }
    }
    if(!propertyIds.isEmpty())
      return true;
    
    return isPermssion;
  }
  
  private void createOrDeletePropertyCollectionPermission(Vertex entityNode, Vertex roleNode,
      Vertex propertyCollection, Boolean hasPermission, String vertexType)
      throws Exception
  {
    String entityId = UtilClass.getCodeNew(entityNode);
    String roleId = UtilClass.getCodeNew(roleNode);
    String propertyCollectionId = UtilClass.getCodeNew(propertyCollection);
    
    Vertex propertyCollectionPermission = GlobalPermissionUtils
        .getPropertyCollectionPermissionVertexIfExist(propertyCollectionId, roleId, entityId,
            vertexType);
    if (hasPermission && propertyCollectionPermission != null) {
      propertyCollectionPermission.remove();
    }
    else if (!hasPermission && propertyCollectionPermission == null) {
      Map<String, Object> permissionMap = new HashMap<String, Object>();
      permissionMap.put(IPropertyCollectionPermission.ENTITY_ID, entityId);
      permissionMap.put(IPropertyCollectionPermission.PROPERTY_COLLECTION_ID, propertyCollectionId);
      permissionMap.put(IPropertyCollectionPermission.ROLE_ID, roleId);
      
      OrientGraph graph = UtilClass.getGraph();
      OrientVertexType orientVvertexType = graph.getVertexType(vertexType);
      propertyCollectionPermission = UtilClass.createNode(permissionMap, orientVvertexType,
          new ArrayList<String>());
      
      entityNode.addEdge(RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_PERMISSION,
          propertyCollectionPermission);
      roleNode.addEdge(RelationshipLabelConstants.HAS_ROLE_PROPERTY_COLLECTION_PERMISSION,
          propertyCollectionPermission);
      propertyCollectionPermission.addEdge(
          RelationshipLabelConstants.IS_PROPERTY_COLLECTION_PERMISSION_OF, propertyCollection);
    }
  }
}
