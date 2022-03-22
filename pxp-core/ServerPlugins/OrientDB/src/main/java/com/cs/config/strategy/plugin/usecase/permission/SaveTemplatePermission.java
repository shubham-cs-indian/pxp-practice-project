/** */
package com.cs.config.strategy.plugin.usecase.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.template.IHeaderPermission;
import com.cs.core.config.interactor.entity.template.IPropertyCollectionPermission;
import com.cs.core.config.interactor.entity.template.IPropertyPermission;
import com.cs.core.config.interactor.entity.template.IRelationshipPermission;
import com.cs.core.config.interactor.model.template.ISaveTemplatePermissionModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class SaveTemplatePermission extends AbstractOrientPlugin {
  
  public SaveTemplatePermission(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveTemplatePermission/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    managePropertyCollectionPermission(requestMap);
    managePropertyPermission(requestMap);
    manageRelationshipPermission(requestMap);
    manageHeaderPermission(requestMap);
    
    manageAllowedTemplates(requestMap);
    String entityId = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_ID);
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(CommonConstants.ID_PROPERTY, entityId);
    return returnMap;
  }
  
  private void manageAllowedTemplates(Map<String, Object> requestMap) throws Exception
  {
    String entityId = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_ID);
    String entityType = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_TYPE);
    Vertex entityNode = UtilClass.getVertexById(entityId,
        EntityUtil.getVertexLabelByEntityType(entityType));
    String roleId = (String) requestMap.get(ISaveTemplatePermissionModel.ROLE_ID);
    Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    
    List<String> addedTemplates = (List<String>) requestMap
        .get(ISaveTemplatePermissionModel.ADDED_ALLOWED_TEMPLATES);
    List<String> deletedTemplates = (List<String>) requestMap
        .get(ISaveTemplatePermissionModel.DELETED_ALLOWED_TEMPLATES);
    
    Map<String, Object> templateMap = new HashMap<>();
    templateMap.put(CommonConstants.ROLE_ID_PROPERY, roleId);
    templateMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.TEMPLATE_PERMISSION, CommonConstants.CODE_PROPERTY);
    
    for (String addedTemplateId : addedTemplates) {
      templateMap.put(ISaveTemplatePermissionModel.TEMPLATE_ID, addedTemplateId);
      Vertex templateNode = UtilClass.getVertexById(addedTemplateId, VertexLabelConstants.TEMPLATE);
      Vertex templatePermissionNode = UtilClass.createNode(templateMap, vertexType,
          new ArrayList<>());
      
      entityNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE_PERMISSION,
          templatePermissionNode);
      roleNode.addEdge(RelationshipLabelConstants.HAS_ROLE_TEMPLATE_PERMISSION,
          templatePermissionNode);
      templatePermissionNode.addEdge(RelationshipLabelConstants.HAS_ALLOWED_TEMPLATE, templateNode);
    }
    
    for (String deletedTemplateId : deletedTemplates) {
      String query = "Select From " + VertexLabelConstants.TEMPLATE_PERMISSION + " WHERE "
          + CommonConstants.ENTITY_ID_PROPERTY + " = '" + entityId + "' AND "
          + CommonConstants.ROLE_ID_PROPERY + " = '" + roleId + "' AND "
          + ISaveTemplatePermissionModel.TEMPLATE_ID + " = '" + deletedTemplateId + "'";
      
      Iterable<Vertex> templatePermissionVertices = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      
      for (Vertex vertex : templatePermissionVertices) {
        vertex.remove();
      }
    }
  }
  
  /**
   * @param requestMap
   * @throws Exception
   */
  private void manageHeaderPermission(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> headerPermission = (Map<String, Object>) requestMap
        .get(ISaveTemplatePermissionModel.HEADER_PERMISSION);
    if (headerPermission == null || headerPermission.isEmpty()) {
      return;
    }
    
    String entityId = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_ID);
    String roleId = (String) requestMap.get(ISaveTemplatePermissionModel.ROLE_ID);
    headerPermission.put(IPropertyCollectionPermission.ENTITY_ID, entityId);
    headerPermission.put(IPropertyCollectionPermission.ROLE_ID, roleId);
    
    String id = (String) headerPermission.get(IHeaderPermission.ID);
    Vertex headerPermissionNode = UtilClass.getVertexByIdWithoutException(id,
        VertexLabelConstants.HEADER_PERMISSION);
    if (headerPermissionNode == null) {
      // If node not found create new one
      String entityType = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_TYPE);
      
      Vertex entityNode = UtilClass.getVertexById(entityId,
          EntityUtil.getVertexLabelByEntityType(entityType));
      Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      
      OrientGraph graph = UtilClass.getGraph();
      OrientVertexType vertexType = graph.getVertexType(VertexLabelConstants.HEADER_PERMISSION);
      Vertex permissionNode = UtilClass.createNode(headerPermission, vertexType,
          new ArrayList<String>());
      
      entityNode.addEdge(RelationshipLabelConstants.HAS_HEADER_PERMISSION, permissionNode);
      roleNode.addEdge(RelationshipLabelConstants.HAS_ROLE_HEADER_PERMISSION, permissionNode);
    }
    else {
      UtilClass.saveNode(headerPermission, headerPermissionNode, new ArrayList<String>());
    }
  }
  
  /**
   * @param requestMap
   * @throws Exception
   */
  private void managePropertyCollectionPermission(Map<String, Object> requestMap) throws Exception
  {
    // klass or taxonomy id
    String entityId = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_ID);
    String roleId = (String) requestMap.get(ISaveTemplatePermissionModel.ROLE_ID);
    String entityType = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_TYPE);
    
    // get klass or taxonomy node
    Vertex entityNode = UtilClass.getVertexById(entityId,
        EntityUtil.getVertexLabelByEntityType(entityType));
    Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    
    List<Map<String, Object>> modifiedPropertyCollectionPermission = (List<Map<String, Object>>) requestMap
        .get(ISaveTemplatePermissionModel.MODIFIED_PROPERTY_COLLECTION_PERMISSION);
    for (Map<String, Object> pCPermissionMap : modifiedPropertyCollectionPermission) {
      String propertyCollectionId = (String) pCPermissionMap
          .get(IPropertyCollectionPermission.PROPERTY_COLLECTION_ID);
      
      Boolean canEdit = (Boolean) pCPermissionMap.get(IPropertyCollectionPermission.CAN_EDIT);
      Boolean isVisible = (Boolean) pCPermissionMap.get(IPropertyCollectionPermission.IS_VISIBLE);
      Vertex propertyCollection = UtilClass.getVertexById(propertyCollectionId,
          VertexLabelConstants.PROPERTY_COLLECTION);
      createOrDeletePropertyCollectionPermission(entityNode, roleNode, propertyCollection, canEdit,
          VertexLabelConstants.PROPERTY_COLLECTION_CAN_EDIT_PERMISSION);
      createOrDeletePropertyCollectionPermission(entityNode, roleNode, propertyCollection,
          isVisible, VertexLabelConstants.PROPERTY_COLLECTION_CAN_READ_PERMISSION);
    }
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
  
  /**
   * @param requestMap
   * @throws Exception
   */
  private void managePropertyPermission(Map<String, Object> requestMap) throws Exception
  {
    String entityId = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_ID);
    String roleId = (String) requestMap.get(ISaveTemplatePermissionModel.ROLE_ID);
    String entityType = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_TYPE);
    
    Vertex entityNode = UtilClass.getVertexById(entityId,
        EntityUtil.getVertexLabelByEntityType(entityType));
    Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    
    List<Map<String, Object>> modifiedPropertyPermission = (List<Map<String, Object>>) requestMap
        .get(ISaveTemplatePermissionModel.MODIFIED_PROPERTY_PERMISSION);
    for (Map<String, Object> propertyPermissionMap : modifiedPropertyPermission) {
      String id = (String) propertyPermissionMap.get(IPropertyPermission.PROPERTY_ID);
      String type = (String) propertyPermissionMap.get(IPropertyPermission.TYPE);
      Vertex propertyNode = UtilClass.getVertexById(id,
          EntityUtil.getVertexLabelByEntityType(type));
      
      Boolean canEdit = (Boolean) propertyPermissionMap.get(IPropertyPermission.CAN_EDIT);
      Boolean isVisible = (Boolean) propertyPermissionMap.get(IPropertyPermission.IS_VISIBLE);
      
      GlobalPermissionUtils.createOrDeletePropertyPermission(entityNode, roleNode, propertyNode,
          VertexLabelConstants.PROPERTY_CAN_READ_PERMISSION, isVisible);
      GlobalPermissionUtils.createOrDeletePropertyPermission(entityNode, roleNode, propertyNode,
          VertexLabelConstants.PROPERTY_CAN_EDIT_PERMISSION, canEdit);
    }
  }
  
  /**
   * @param requestMap
   * @throws Exception
   */
  private void manageRelationshipPermission(Map<String, Object> requestMap) throws Exception
  {
    String entityId = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_ID);
    String roleId = (String) requestMap.get(ISaveTemplatePermissionModel.ROLE_ID);
    String entityType = (String) requestMap.get(ISaveTemplatePermissionModel.ENTITY_TYPE);
    
    Vertex entityNode = UtilClass.getVertexById(entityId,
        EntityUtil.getVertexLabelByEntityType(entityType));
    Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    
    List<Map<String, Object>> modifiedRelationshipPermission = (List<Map<String, Object>>) requestMap
        .get(ISaveTemplatePermissionModel.MODIFIED_RELATIONSHIP_PERMISSION);
    for (Map<String, Object> relationshipPermissionMap : modifiedRelationshipPermission) {
      String id = (String) relationshipPermissionMap.get(IRelationshipPermission.RELATIONSHIP_ID);
      Vertex relationshipNode = UtilClass.getVertexById(id, VertexLabelConstants.ROOT_RELATIONSHIP);
      
      Boolean canAdd = (Boolean) relationshipPermissionMap.get(IRelationshipPermission.CAN_ADD);
      Boolean canDelete = (Boolean) relationshipPermissionMap
          .get(IRelationshipPermission.CAN_DELETE);
      Boolean isVisible = (Boolean) relationshipPermissionMap
          .get(IRelationshipPermission.IS_VISIBLE);
      Boolean canEditContext = (Boolean) relationshipPermissionMap.get(IRelationshipPermission.CAN_EDIT);
      
      GlobalPermissionUtils.createOrDeleteRelationshipPermission(entityNode, relationshipNode, roleNode,
          VertexLabelConstants.RELATIONSHIP_CAN_READ_PERMISSION, isVisible);
      GlobalPermissionUtils.createOrDeleteRelationshipPermission(entityNode, relationshipNode, roleNode,
          VertexLabelConstants.RELATIONSHIP_CAN_ADD_PERMISSION, canAdd);
      GlobalPermissionUtils.createOrDeleteRelationshipPermission(entityNode, relationshipNode, roleNode,
          VertexLabelConstants.RELATIONSHIP_CAN_DELETE_PERMISSION, canDelete);
     
      if(canEditContext != null)
        GlobalPermissionUtils.createOrDeleteRelationshipPermission(entityNode, relationshipNode, roleNode,
              VertexLabelConstants.RELATIONSHIP_CONTEXT_CAN_EDIT_PERMISSION, canEditContext);
    }
  }
}
