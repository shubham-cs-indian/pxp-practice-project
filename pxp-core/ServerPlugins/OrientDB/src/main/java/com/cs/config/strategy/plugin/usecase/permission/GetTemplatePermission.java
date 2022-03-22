package com.cs.config.strategy.plugin.usecase.permission;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyCollectionPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyPermissions;
import com.cs.core.config.interactor.entity.template.IHeaderPermission;
import com.cs.core.config.interactor.entity.template.ITabPermission;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.template.ITemplatePermission;
import com.cs.core.config.interactor.entity.template.ITemplateTab;
import com.cs.core.config.interactor.exception.propertycollection.PropertyCollectionNotFoundException;
import com.cs.core.config.interactor.model.template.IGetTemplatePermissionModel;
import com.cs.core.config.interactor.model.template.IPermissionRequestModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetTemplatePermission extends AbstractOrientPlugin {
  
  public GetTemplatePermission(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTemplatePermission/*" };
  }
  
  private static final String PROPERTY_COLLECTION_IDS = "propertyCollectionIds";
  private static final String ENTITY_ID_TYPE_MAP      = "entityIdTypeMap";
  private static final String RELATIONSHIP_IDS        = "relationshipIds";
  private static final String NATURE_RELATIONSHIP_IDS = "natureRelationshipIds";
  private static final String HEADER_ID               = "headerId";
  private static final String TAB_IDS                 = "tabIds";
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> permissionMap = new HashMap<String, Object>();
    
    Map<String, Object> dataIdsMap = new HashMap<String, Object>();
    dataIdsMap.put(PROPERTY_COLLECTION_IDS, new HashSet<String>());
    dataIdsMap.put(ENTITY_ID_TYPE_MAP, new HashMap<String, String>());
    dataIdsMap.put(RELATIONSHIP_IDS, new HashSet<String>());
    dataIdsMap.put(NATURE_RELATIONSHIP_IDS, new HashSet<String>());
    dataIdsMap.put(TAB_IDS, new HashSet<String>());
    
    String roleId = (String) requestMap.get(IPermissionRequestModel.ROLE_ID);
    String templateId = (String) requestMap.get(IPermissionRequestModel.TEMPLATE_ID);
    Vertex templateNode = UtilClass.getVertexById(templateId, VertexLabelConstants.TEMPLATE);
    
    Map<String, Object> templateDetails = getTemplateDetailsMap(templateNode, dataIdsMap);
    permissionMap.put(IGetTemplatePermissionModel.TEMPLATE_DETAILS, templateDetails);
    
    Map<String, Object> templatePermission = getTemplatePermission(dataIdsMap, roleId, templateId);
    permissionMap.put(IGetTemplatePermissionModel.TEMPLATE_PERMISSIONS, templatePermission);
    
    return permissionMap;
  }
  
  private Map<String, Object> getTemplatePermission(Map<String, Object> dataIdsMap, String roleId,
      String templateId) throws Exception
  {
    Map<String, Object> templatePermission = new HashMap<String, Object>();
    
    Map<String, Object> pCPermissionMap = getPropertyCollectionPermission(templateId, roleId,
        dataIdsMap);
    templatePermission.put(ITemplatePermission.PROPERTY_COLLECTION_PERMISSION, pCPermissionMap);
    
    Map<String, Object> propertyPermissionMap = getPropertyPermission(templateId, roleId,
        dataIdsMap);
    templatePermission.put(ITemplatePermission.PROPERTY_PERMISSION, propertyPermissionMap);
    
    Map<String, Object> relationshipPermissionMap = getRelationshipPermission(templateId, roleId,
        dataIdsMap);
    templatePermission.put(ITemplatePermission.RELATIONSHIP_PERMISSION, relationshipPermissionMap);
    
    String headerId = (String) dataIdsMap.get(HEADER_ID);
    Map<String, Object> headerPermission = GlobalPermissionUtils.getHeaderPermission(headerId,
        roleId, templateId);
    headerPermission.put(IHeaderPermission.ENTITY_ID, headerId);
    templatePermission.put(ITemplatePermission.HEADER_PERMISSION, headerPermission);
    
    Map<String, Object> tabPermission = getTabsPermissions(templateId, roleId, dataIdsMap);
    templatePermission.put(ITemplatePermission.TAB_PERMISSION, tabPermission);
    
    return templatePermission;
  }
  
  /**
   * @param templateId
   * @param roleId
   * @param dataIdsMap
   * @return
   * @throws Exception
   */
  private Map<String, Object> getTabsPermissions(String templateId, String roleId,
      Map<String, Object> dataIdsMap) throws Exception
  {
    Map<String, Object> tabPermissionMap = new HashMap<String, Object>();
    Set<String> tabIds = (Set<String>) dataIdsMap.get(TAB_IDS);
    for (String tabId : tabIds) {
      Map<String, Object> tabPermission = GlobalPermissionUtils.getTabPermission(tabId, roleId,
          templateId);
      
      Vertex tabNode = UtilClass.getVertexById(tabId, VertexLabelConstants.TEMPLATE_TAB);
      String type = tabNode.getProperty(ITemplateTab.BASE_TYPE);
      
      tabPermission.put(ITabPermission.ENTITY_ID, tabId);
      tabPermission.put(ITabPermission.TYPE, type);
      
      tabPermissionMap.put(tabId, tabPermission);
    }
    return tabPermissionMap;
  }
  
  /**
   * @param dataIdsMap
   * @param templateId
   * @param roleId
   * @return
   * @throws PropertyCollectionNotFoundException
   */
  private Map<String, Object> getPropertyCollectionPermission(String templateId, String roleId,
      Map<String, Object> dataIdsMap) throws Exception
  {
    Map<String, Object> propertyCollectionPermissionMap = new HashMap<String, Object>();
    Set<String> pCIds = (Set<String>) dataIdsMap.get(PROPERTY_COLLECTION_IDS);
    Map<String, String> entityIdTypeMap = (Map<String, String>) dataIdsMap.get(ENTITY_ID_TYPE_MAP);
    for (String propertyCollectionId : pCIds) {
      Map<String, Object> propertyCollectionPermission = GlobalPermissionUtils
          .getPropertyCollectionPermission(propertyCollectionId, roleId, templateId);
      
      propertyCollectionPermission.put(IPropertyCollectionPermissions.ENTITY_ID,
          propertyCollectionId);
      
      propertyCollectionPermissionMap.put(propertyCollectionId, propertyCollectionPermission);
      Vertex propertyCollectionNode = UtilClass.getVertexById(propertyCollectionId,
          VertexLabelConstants.PROPERTY_COLLECTION);
      Iterator<Edge> iterator = propertyCollectionNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
          .iterator();
      while (iterator.hasNext()) {
        Edge entityTo = iterator.next();
        String entityType = entityTo.getProperty(CommonConstants.TYPE_PROPERTY);
        Vertex entityNode = entityTo.getVertex(Direction.OUT);
        entityIdTypeMap.put(UtilClass.getCodeNew(entityNode), entityType);
      }
    }
    return propertyCollectionPermissionMap;
  }
  
  /**
   * @param dataIdsMap
   * @param templateId
   * @param roleId
   * @return
   */
  private Map<String, Object> getPropertyPermission(String templateId, String roleId,
      Map<String, Object> dataIdsMap) throws Exception
  {
    Map<String, Object> propertyPermissionMap = new HashMap<String, Object>();
    Map<String, String> entityIdTypeMap = (Map<String, String>) dataIdsMap.get(ENTITY_ID_TYPE_MAP);
    for (String entityId : entityIdTypeMap.keySet()) {
      Map<String, Object> propertyPermission = GlobalPermissionUtils.getPropertyPermission(entityId,
          roleId, templateId);
      propertyPermission.put(IPropertyPermissions.ENTITY_ID, entityId);
      propertyPermission.put(IPropertyPermissions.TYPE, entityIdTypeMap.get(entityId));
      propertyPermissionMap.put(entityId, propertyPermission);
    }
    return propertyPermissionMap;
  }
  
  /**
   * @param dataIdsMap
   * @param templateId
   * @param roleId
   * @return
   * @throws PropertyCollectionNotFoundException
   */
  private Map<String, Object> getRelationshipPermission(String templateId, String roleId,
      Map<String, Object> dataIdsMap) throws Exception
  {
    Map<String, Object> relationshipPermissionMap = new HashMap<String, Object>();
    Set<String> relationshipIds = (Set<String>) dataIdsMap.get(RELATIONSHIP_IDS);
    for (String relationshipId : relationshipIds) {
      Map<String, Object> relationshipPermission = GlobalPermissionUtils
          .getRelationshipPermission(relationshipId, roleId, templateId);
      relationshipPermission.put(IPropertyPermissions.ENTITY_ID, relationshipId);
      relationshipPermissionMap.put(relationshipId, relationshipPermission);
    }
    return relationshipPermissionMap;
  }
  
  /**
   * @param templateNode
   * @param dataIdsMap
   * @return
   * @throws Exception
   */
  private Map<String, Object> getTemplateDetailsMap(Vertex templateNode,
      Map<String, Object> dataIdsMap) throws Exception
  {
    Map<String, Object> templateDetailsMap = UtilClass.getMapFromVertex(new ArrayList<>(),
        templateNode);
    
    populateHeaderData(templateNode, templateDetailsMap, dataIdsMap);
    
    List<Map<String, Object>> tabsList = new ArrayList<>();
    Iterable<Vertex> tabs = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TEMPLATE_TAB);
    
    Set<String> tabIds = (Set<String>) dataIdsMap.get(TAB_IDS);
    
    for (Vertex tabNode : tabs) {
      String baseType = tabNode.getProperty(ITemplateTab.BASE_TYPE);
      Map<String, Object> tabMap = UtilClass.getMapFromVertex(new ArrayList<>(), tabNode);
      
      String tabId = (String) tabMap.get(ITemplateTab.ID);
      tabIds.add(tabId);
      
      /*switch (baseType) {
        case CommonConstants.TEMPLATE_HOME_TAB_BASETYPE:
          //GetTemplateUtils.populateHomeTabData(templateNode, tabMap);
      
          Set<String> pCIds = (Set<String>) dataIdsMap.get(PROPERTY_COLLECTION_IDS);
          List<String> propertyCollections = (List<String>) tabMap.get(ITemplateHomeTab.PROPERTY_COLLECTIONS);
          pCIds.addAll(propertyCollections);
      
          Set<String> natureRelationshipIds = (Set<String>) dataIdsMap.get(NATURE_RELATIONSHIP_IDS);
          List<String> natureRelationships = (List<String>) tabMap.get(ITemplateHomeTab.NATURE_RELATIONSHIPS);
          natureRelationshipIds.addAll(natureRelationships);
      
          break;
        case CommonConstants.TEMPLATE_RELATIONSHIP_TAB_BASETYPE:
          //GetTemplateUtils.populateRelationshipTabData(templateNode, tabMap);
      
          Set<String> relationshipIds = (Set<String>) dataIdsMap.get(RELATIONSHIP_IDS);
          List<String> relatioships = (List<String>) tabMap.get(ITemplateRelationshipTab.RELATIONSHIPS);
          relationshipIds.addAll(relatioships);
      
          break;
      }*/
      tabsList.add(tabMap);
    }
    templateDetailsMap.put(ITemplate.TABS, tabsList);
    
    return templateDetailsMap;
  }
  
  /**
   * Description read header node and return the info.
   *
   * @param templateNode
   * @param templateMapToReturn
   * @param dataIdsMap
   * @throws Exception
   */
  public static void populateHeaderData(Vertex templateNode,
      Map<String, Object> templateMapToReturn, Map<String, Object> dataIdsMap) throws Exception
  {
    /*Vertex headerNode = TemplateUtils.getHeaderNodeFromTemplate(templateNode);
    Map<String,Object> headerMap = UtilClass.getMapFromVertex(new ArrayList<>(), headerNode);
    String headerId = (String) headerMap.remove(CommonConstants.ID_PROPERTY);
    dataIdsMap.put(HEADER_ID, headerId);
    templateMapToReturn.put(ITemplate.HEADER_VISIBILITY,headerMap);
    */
  }
}
