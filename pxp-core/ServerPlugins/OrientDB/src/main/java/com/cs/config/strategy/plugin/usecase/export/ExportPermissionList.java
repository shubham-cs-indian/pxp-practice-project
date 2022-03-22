package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.permission.IPermission;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.entity.template.IPropertyPermission;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.exception.role.RoleNotFoundException;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class ExportPermissionList extends AbstractOrientPlugin {
  
  public ExportPermissionList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE);
  
  //PXPFDEV-21215 : Deprecated - Taxonomy Hierarchies
  //TODO: PXPFDEV-21454: Deprecate Virtual Catalog 
  public static final List<String> entityTypes   = Arrays.asList(CommonConstants.ARTICLE_ENTITY, CommonConstants.ASSET_ENTITY,
      CommonConstants.SUPPLIER_ENTITY, CommonConstants.TEXT_ASSET_ENTITY, //CommonConstants.VIRTUAL_CATALOG_ENTITY, 
      CommonConstants.MASTER_TAXONOMY, CommonConstants.TASK);
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportPermissionList/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    
    @SuppressWarnings("unchecked")
    List<String> itemCodes = (List<String>) requestMap.get("itemCodes");
    
    Map<String, Object> responseMap = new HashMap<String, Object>();
    String roleId = itemCodes.get(0);
    validateRole(roleId);
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    for (String entityType : entityTypes) {
      String query = prepareQuery(roleId, entityType);
      
      Iterable<Vertex> resultIterable = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      
      for (Vertex vertex : resultIterable) {
        String entityId = UtilClass.getCodeNew(vertex);
        Map<String, Object> permission = getPermission(roleId, entityId, entityType);
        list.add(permission);
      }
      
    }
    responseMap.put("list", list);
    return responseMap;
  }
  
  private void validateRole(String roleId) throws Exception
  {
    
    try {
      Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      
    }
    catch (NotFoundException ex) {
      throw new RoleNotFoundException();
    }
    
  }

  private Map<String, Object> getPermission(String roleId, String entityId, String entityType)
      throws Exception
  {
    String vertexType = getVertexTypeByEntityType(entityType);
    
    Map<String, Object> permission = new HashMap<String, Object>();
    
    Vertex entityNode = UtilClass.getVertexById(entityId, vertexType);
    
    List<Map<String, Object>> propertyPermissions = new ArrayList<>();
    List<Map<String, Object>> relationshipPermissions = new ArrayList<>();
    
    fillPropertyAndRelationshipPermission(entityNode, roleId, propertyPermissions,
        relationshipPermissions);
    permission.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
    permission.put("permissionType", entityType);
    permission.put(CommonConstants.ROLE_ID_PROPERY, roleId);
    
    permission.put("propertyPermissions", propertyPermissions);
    permission.put("relationshipPermissions", relationshipPermissions);
    //PXPFDEV-21215 : Deprecated - Taxonomy Hierarchies
    if (!(entityType.equals(CommonConstants.TASK) || entityType.equals(CommonConstants.MASTER_TAXONOMY))) {
      Map<String, Object> headerPermission = GlobalPermissionUtils.getHeaderPermission(entityNode, roleId);
      headerPermission.remove(CommonConstants.CODE_PROPERTY);
      headerPermission.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      permission.put(IPermission.HEADER_PERMISSION, headerPermission);
    }
    
    Map<String, Object> globalPermission = GlobalPermissionUtils.getKlassAndTaxonomyPermission(entityId, roleId);    
    globalPermission.remove(CommonConstants.CODE_PROPERTY);
    globalPermission.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
    permission.put(IPermission.GLOBAL_PERMISSION, globalPermission);
    
    return permission;
  }
  
  /**
   * @param dataIdsMap
   * @param templateId
   * @param roleId
   * @param referencedRelationship
   * @param referencedReferences
   * @param referencedReferences
   * @param propertyIdVsPropertyType
   * @return
   */
  private void fillPropertyAndRelationshipPermission(Vertex entityNode, String roleId,
      List<Map<String, Object>> propertyPermissions,
      List<Map<String, Object>> relationshipPermissions) throws Exception
  {
    
    String entityId = UtilClass.getCodeNew(entityNode);
    Iterable<Vertex> vertices = entityNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex kPNode : vertices) {
      String propertyType = kPNode.getProperty(CommonConstants.TYPE);
      Vertex propertyNode = KlassUtils.getPropertyNodeFromKlassProperty(kPNode);
      String propertyId = UtilClass.getCodeNew(propertyNode);
      if (propertyType.equals(CommonConstants.RELATIONSHIP)) {
        Boolean isRelationshipWithContext = checkIsRelationshipWithContext(kPNode, false);
        Map<String, Object> relationshipPermission = GlobalPermissionUtils
            .getRelationshipPermissionMap(propertyId, roleId, entityId, isRelationshipWithContext);
        relationshipPermission.remove(IPropertyPermission.ROLE_ID);
        relationshipPermission.remove(CommonConstants.CODE_PROPERTY);
        
        relationshipPermissions.add(relationshipPermission);
      }
      else {
        Map<String, Object> propertyPermission = GlobalPermissionUtils
            .getPropertyPermissionMap(propertyId, roleId, entityId);
        propertyPermission.put("propertyType", propertyType);
        propertyPermission.remove(IPropertyPermission.ROLE_ID);
        propertyPermission.remove(CommonConstants.CODE_PROPERTY);
        propertyPermissions.add(propertyPermission);
      }
    }
    Iterable<Vertex> klassRelationshipNatureVertices = entityNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    for (Vertex vertex : klassRelationshipNatureVertices) {
      Iterator<Vertex> natureRelationshipIterator = vertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      if (!natureRelationshipIterator.hasNext()) {
        throw new RelationshipNotFoundException();
      }
      Vertex natureRelationshipVertex = natureRelationshipIterator.next();
      if (natureRelationshipIterator.hasNext()) {
        throw new MultipleLinkFoundException();
      }
      String propertyId = UtilClass.getCodeNew(natureRelationshipVertex);
      Boolean isRelationshipWithContext = checkIsRelationshipWithContext(vertex, true);
      Map<String, Object> relationshipPermission = GlobalPermissionUtils
          .getRelationshipPermissionMap(propertyId, roleId, entityId, isRelationshipWithContext);
      relationshipPermission.remove(IPropertyPermission.ROLE_ID);
      relationshipPermission.remove(CommonConstants.CODE_PROPERTY);
      relationshipPermissions.add(relationshipPermission);
      Map<String, Object> relationshipMap = UtilClass.getMapFromVertex(fieldsToFetch,
          natureRelationshipVertex);
      relationshipMap.put(IIdLabelTypeModel.TYPE,
          natureRelationshipVertex.getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE));
    }
  }
  
  private Boolean checkIsRelationshipWithContext(Vertex vertex, Boolean isNatureRelationship)
  {
    if (isNatureRelationship) {
      return vertex.getEdges(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
          .iterator()
          .hasNext();
    }
    else {
      return vertex.getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF)
          .iterator()
          .hasNext();
    }
  }
  
  private String prepareQuery(String roleId, String entityType) throws Exception
  {
    
    String vertexLabel = getVertexTypeByEntityType(entityType);
    
    String query = "";
    String conditionQuery = "";
    String queryToExcludeKlasses = "";
    
    query += "SELECT FROM " + vertexLabel;
    
    if (vertexLabel.equals(VertexLabelConstants.ENTITY_TYPE_KLASS)) {
      queryToExcludeKlasses = CommonConstants.CODE_PROPERTY + " NOT IN "
          + EntityUtil.quoteIt(SystemLevelIds.KLASSES_TO_EXCLUDE_FROM_CONFIG_SCREEN);
    }
    
    if (entityType.equals(CommonConstants.MASTER_TAXONOMY)) {
      conditionQuery += ITaxonomy.TAXONOMY_TYPE + " = '" + CommonConstants.MAJOR_TAXONOMY + "' OR "
                     + ITaxonomy.TAXONOMY_TYPE + " = '" + CommonConstants.MINOR_TAXONOMY + "'";
    }

     StringBuilder finalConditionQueryBuilder = EntityUtil
        .getConditionQuery(new StringBuilder(conditionQuery), new StringBuilder(queryToExcludeKlasses));
    
    String finalConditionQuery = finalConditionQueryBuilder.toString();
    if (!finalConditionQuery.isEmpty()) {
      query += finalConditionQuery;
    }
    return query;
  }
  private String getVertexTypeByEntityType(String entityType)
  {
    switch (entityType) {
      case CommonConstants.ARTICLE_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_KLASS;
      case CommonConstants.ASSET_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_ASSET;
      case CommonConstants.TARGET:
        return VertexLabelConstants.ENTITY_TYPE_TARGET;
      case CommonConstants.SUPPLIER_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
      case CommonConstants.TEXT_ASSET_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
     // TODO: PXPFDEV-21451: Deprecate Virtual Catalog  
      /*case CommonConstants.VIRTUAL_CATALOG_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_VIRTUAL_CATALOG;*/
      // TODO: PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
      /*case CommonConstants.HIERARCHY_TAXONOMY:
        return VertexLabelConstants.HIERARCHY_TAXONOMY;*/
      case CommonConstants.MASTER_TAXONOMY:
        return VertexLabelConstants.ATTRIBUTION_TAXONOMY;
      case CommonConstants.TASK:
        return VertexLabelConstants.ENTITY_TYPE_TASK;
      default:
        break;
    }
    return null;
  }
}