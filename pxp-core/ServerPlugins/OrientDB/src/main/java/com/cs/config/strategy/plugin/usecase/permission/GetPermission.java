package com.cs.config.strategy.plugin.usecase.permission;

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
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.permission.IPermission;
import com.cs.core.config.interactor.entity.template.IPropertyPermission;
import com.cs.core.config.interactor.exception.propertycollection.PropertyCollectionNotFoundException;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.exception.template.TemplateNotFoundException;
import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionRequestModel;
import com.cs.core.config.interactor.model.permission.IReferencedPropertyCollectionForPermissionModel;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class GetPermission extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE);
  public static final List<String> fieldsToFetchForPropertyCollection = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE, CommonConstants.PROPERTY_SEQUENCE_IDS);
  
  public GetPermission(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetPermission/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<String, Object>();
    
    String roleId = (String) requestMap.get(IGetPermissionRequestModel.ROLE_ID);
    String id = (String) requestMap.get(IGetPermissionRequestModel.ID);
    String entityType = (String) requestMap.get(IGetPermissionRequestModel.ENTITY_TYPE);
    
    Map<String, Object> referencedPC = new HashMap<String, Object>();
    Map<String, Object> referencedRelationship = new HashMap<String, Object>();
    List<Map<String, Object>> allowedTemplates = getAllowedTemplates(roleId, id, entityType);
    
    Map<String, Object> permissionMap = getPermission(roleId, id, entityType, referencedPC,
        referencedRelationship);
    responseMap.put(IGetPermissionModel.PERMISSION, permissionMap);
    responseMap.put(IGetPermissionModel.REFERENCED_PROPERTY_COLLECTIONS, referencedPC);
    responseMap.put(IGetPermissionModel.REFERENCED_RELATIONSHIPS, referencedRelationship);
    responseMap.put(IGetPermissionModel.ALLOWED_TEMPLATES, allowedTemplates);
    return responseMap;
  }
  
  private List<Map<String, Object>> getAllowedTemplates(String roleId, String id, String entityType)
      throws Exception
  {
    List<Map<String, Object>> allowedTemplates = new ArrayList<>();
    
    String query = "Select From " + VertexLabelConstants.TEMPLATE_PERMISSION + " WHERE "
        + CommonConstants.ENTITY_ID_PROPERTY + " = '" + id + "' AND "
        + CommonConstants.ROLE_ID_PROPERY + " = '" + roleId + "'";
    
    Iterable<Vertex> templatePermissionVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE);
    for (Vertex vertex : templatePermissionVertices) {
      Map<String, Object> mapFromVertex = new HashMap<>();
      Iterator<Vertex> iterator = vertex
          .getVertices(Direction.OUT,
              RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_ALLOWED_TEMPLATE)
          .iterator();
      if (!iterator.hasNext()) {
        throw new TemplateNotFoundException();
      }
      Vertex templateNode = iterator.next();
      mapFromVertex = UtilClass.getMapFromVertex(fieldsToFetch, templateNode);
      allowedTemplates.add(mapFromVertex);
    }
    return allowedTemplates;
  }
  
  private Map<String, Object> getPermission(String roleId, String id, String entityType,
      Map<String, Object> referencedPC, Map<String, Object> referencedRelationship) throws Exception
  {
    String vertexType = EntityUtil.getVertexLabelByEntityType(entityType);
    
    Map<String, Object> permission = new HashMap<String, Object>();
    
    Vertex entityNode = UtilClass.getVertexById(id, vertexType);
    
    Map<String, Object> pCPermissionMap = getPropertyCollectionPermission(entityNode, roleId,
        referencedPC);
    permission.put(IPermission.PROPERTY_COLLECTION_PERMISSION, pCPermissionMap);
    
    Map<String, Object> propertyPermissionMap = new HashMap<>();
    Map<String, Object> relationshipPermissionMap = new HashMap<>();
    Map<String, Object> referencePermissionMap = new HashMap<>();
    
    fillPropertyAndRelationshipPermission(entityNode, roleId, propertyPermissionMap,
        relationshipPermissionMap, referencedRelationship, referencePermissionMap);
    
    permission.put(IPermission.PROPERTY_PERMISSION, propertyPermissionMap);
    permission.put(IPermission.RELATIONSHIP_PERMISSION, relationshipPermissionMap);
    
    // TODO: CHANGE BELOW LINE TO REFERENCE PERMISSION
    permission.put(IPermission.REFERENCE_PERMISSIONS, referencePermissionMap);
    
    if (!entityType.equals(CommonConstants.TAXONOMY)) {
      Map<String, Object> headerPermission = GlobalPermissionUtils.getHeaderPermission(entityNode,
          roleId);
      permission.put(IPermission.HEADER_PERMISSION, headerPermission);
    }
    
    Map<String, Object> globalPermission = GlobalPermissionUtils
        .getKlassAndTaxonomyPermissionMap(roleId, id);
    permission.put(IPermission.GLOBAL_PERMISSION, globalPermission);
    
    return permission;
  }
  
  /**
   * @param dataIdsMap
   * @param id
   * @param roleId
   * @param referencedPC
   * @return
   * @throws PropertyCollectionNotFoundException
   */
  private Map<String, Object> getPropertyCollectionPermission(Vertex entityNode, String roleId,
      Map<String, Object> referencedPC) throws Exception
  {
    String id = UtilClass.getCodeNew(entityNode);
    Map<String, Object> propertyCollectionPermissionMap = new HashMap<String, Object>();
    
    Iterable<Vertex> vertices = entityNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    for (Vertex sectionNode : vertices) {
      Vertex pCNode = KlassUtils.getPropertyCollectionNodeFromKlassSectionNode(sectionNode);
      String propertyCollectionId = UtilClass.getCodeNew(pCNode);
      Map<String, Object> propertyCollectionPermission = GlobalPermissionUtils
          .getPropertyCollectionPermissionMap(propertyCollectionId, roleId, id);
      
      propertyCollectionPermissionMap.put(propertyCollectionId, propertyCollectionPermission);
      
      Map<String, Object> propertyCollectionMap = UtilClass.getMapFromVertex(fieldsToFetchForPropertyCollection, pCNode);
      propertyCollectionMap.put(IReferencedPropertyCollectionForPermissionModel.ELEMENT_IDS, propertyCollectionMap.remove(CommonConstants.PROPERTY_SEQUENCE_IDS));
      referencedPC.put(propertyCollectionId, propertyCollectionMap);
    }
    
    return propertyCollectionPermissionMap;
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
      Map<String, Object> propertyPermissionMap, Map<String, Object> relationshipPermissionMap,
      Map<String, Object> referencedRelationship, Map<String, Object> referencesPermissionMap) throws Exception
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
        relationshipPermissionMap.put(propertyId, relationshipPermission);
        referencedRelationship.put(propertyId,  
            UtilClass.getMapFromVertex(fieldsToFetch, propertyNode));
      }
      else {
        Map<String, Object> propertyPermission = GlobalPermissionUtils
            .getPropertyPermissionMap(propertyId, roleId, entityId);
        propertyPermission.put(IPropertyPermission.TYPE, propertyType);
        propertyPermissionMap.put(propertyId, propertyPermission);
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
      relationshipPermissionMap.put(propertyId, relationshipPermission);
      Map<String, Object> relationshipMap = UtilClass.getMapFromVertex(fieldsToFetch,
          natureRelationshipVertex);
      relationshipMap.put(IIdLabelTypeModel.TYPE,
          natureRelationshipVertex.getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE));
      referencedRelationship.put(propertyId, relationshipMap);
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

}
