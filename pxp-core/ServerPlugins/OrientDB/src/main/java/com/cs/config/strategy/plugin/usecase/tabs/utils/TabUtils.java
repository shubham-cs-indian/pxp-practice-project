package com.cs.config.strategy.plugin.usecase.tabs.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.IGetTabEntityModel;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class TabUtils {
  
  public static final List<String> contextTypeToSkip = Arrays.asList(
      CommonConstants.ATTRIBUTE_VARIANT_CONTEXT, CommonConstants.PRODUCT_VARIANT,
      CommonConstants.RELATIONSHIP_VARIANT);
  
  public static void manageAddedAndDeletedTab(Vertex requestVertex,
      Map<String, Object> adddedTabMap, String deletedTabId, String type) throws Exception
  {
    if (deletedTabId != null && !deletedTabId.equals("")) {
      manageDeletedTab(requestVertex, deletedTabId);
      linkAddedOrDefaultTab(requestVertex, adddedTabMap, type);
    }
    else {
      manageAddedTab(requestVertex, adddedTabMap);
    }
  }
  
  public static void linkAddedOrDefaultTab(Vertex requestVertex, Map<String, Object> tabMap,
      String type) throws Exception
  {
    if (type.equals(CommonConstants.CONTEXT)
        && contextTypeToSkip.contains(requestVertex.getProperty(IVariantContext.TYPE))) {
      return;
    }
    if (tabMap == null) {
      tabMap = new HashMap<>();
      tabMap.put(IAddedTabModel.IS_NEWLY_CREATED, false);
      tabMap.put(IAddedTabModel.ID, getStandardTabId(type, requestVertex.getProperty(IKlass.TYPE)));
    }
    TabUtils.manageAddedTab(requestVertex, tabMap);
  }
  
  public static void linkAddedOrDefaultTab(String tabId, Vertex requestVertex, String type)
      throws Exception
  {
    if (type.equals(CommonConstants.CONTEXT)
        && contextTypeToSkip.contains(requestVertex.getProperty(IVariantContext.TYPE))) {
      return;
    }
    Map<String, Object> tabMap = new HashMap<>();
    tabMap.put(IAddedTabModel.IS_NEWLY_CREATED, false);
    if (tabId == null) {
      tabMap.put(IAddedTabModel.ID, getStandardTabId(type, requestVertex.getProperty(IKlass.TYPE)));
    }
    else {
      tabMap.put(IAddedTabModel.ID, tabId);
    }
    TabUtils.manageAddedTab(requestVertex, tabMap);
  }
  
  public static void manageAddedTab(Vertex requestVertex, Map<String, Object> tabMap)
      throws Exception
  {
    if (tabMap == null) {
      return;
    }
    String requestVertexId = UtilClass.getCodeNew(requestVertex);
    Iterator<Edge> iterator = requestVertex
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TAB)
        .iterator();
    if (iterator.hasNext()) {
      removeExistingTab(iterator.next(), requestVertexId);
    }
    Vertex tabNode;
    Boolean isNewlyCreated = (Boolean) tabMap.get(IAddedTabModel.IS_NEWLY_CREATED);
    if (isNewlyCreated) {
      tabNode = createTabNode(tabMap, Arrays.asList(IAddedTabModel.IS_NEWLY_CREATED));
    }
    else {
      String id = (String) tabMap.get(IAddedTabModel.ID);
      tabNode = UtilClass.getVertexById(id, VertexLabelConstants.TAB);
    }
    List<String> entityList = tabNode.getProperty(ITab.PROPERTY_SEQUENCE_LIST);
    entityList.add(requestVertexId);
    tabNode.setProperty(ITab.PROPERTY_SEQUENCE_LIST, entityList);
    
    requestVertex.addEdge(RelationshipLabelConstants.HAS_TAB, tabNode);
  }
  
  private static void removeExistingTab(Edge hasTabEdge, String requestVertexId)
  {
    Vertex tabNode = hasTabEdge.getVertex(Direction.IN);
    List<String> entityList = tabNode.getProperty(ITab.PROPERTY_SEQUENCE_LIST);
    entityList.remove(requestVertexId);
    tabNode.setProperty(ITab.PROPERTY_SEQUENCE_LIST, entityList);
    
    hasTabEdge.remove();
  }
  
  public static Vertex createTabNode(Map<String, Object> tabMap, List<String> fieldsToExclude)
      throws Exception
  {
    Integer sequenceNumber = (Integer) tabMap.remove(ICreateTabModel.SEQUENCE);
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.TAB,
        CommonConstants.CODE_PROPERTY);
    
    List<String> entityList = (List<String>) tabMap.get(ITab.PROPERTY_SEQUENCE_LIST);
    if (entityList == null) {
      entityList = new ArrayList<String>();
      tabMap.put(ITab.PROPERTY_SEQUENCE_LIST, entityList);
    }
    
    Vertex tabNode = UtilClass.createNode(tabMap, vertexType, fieldsToExclude);
    // add in tab sequence list
    updateTabSequence(UtilClass.getCodeNew(tabNode), sequenceNumber);
    return tabNode;
  }
  
  public static Vertex getTabNode(Vertex requestVertex) throws Exception
  {
    Iterator<Vertex> iterator = requestVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TAB)
        .iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    Vertex tabNode = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return tabNode;
  }
  
  public static Map<String, Object> getMapFromConnectedTabNode(Vertex requestVertex,
      List<String> fieldToFetch) throws Exception
  {
    if (contextTypeToSkip.contains(requestVertex.getProperty(IVariantContext.TYPE))) {
      return null;
    }
    Vertex tabNode = getTabNode(requestVertex);
    return UtilClass.getMapFromVertex(fieldToFetch, tabNode);
  }
  
  public static void manageDeletedTab(Vertex requestVertex, String requestTabId) throws Exception
  {
    if (requestTabId == null || requestTabId.equals("")) {
      return;
    }
    Iterator<Edge> edges = requestVertex.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TAB)
        .iterator();
    if (!edges.hasNext()) {
      throw new NotFoundException();
    }
    Edge hasTabEdge = edges.next();
    Vertex tabNode = hasTabEdge.getVertex(Direction.IN);
    String tabId = UtilClass.getCodeNew(tabNode);
    if (!requestTabId.equals(tabId)) {
      throw new NotFoundException();
    }
    List<String> entityList = tabNode.getProperty(ITab.PROPERTY_SEQUENCE_LIST);
    entityList.remove(UtilClass.getCodeNew(requestVertex));
    tabNode.setProperty(ITab.PROPERTY_SEQUENCE_LIST, entityList);
    
    hasTabEdge.remove();
  }
  
  public static final Map<String, Object> prepareTabResponseMap(Vertex tabNode) throws Exception
  {
    List<String> fieldToFetch = Arrays.asList(IGetTabEntityModel.LABEL, IGetTabEntityModel.ICON,
        IGetTabEntityModel.CODE, IGetTabEntityModel.PROPERTY_SEQUENCE_LIST);
    Map<String, Object> tabMap = UtilClass.getMapFromVertex(fieldToFetch, tabNode);
    
    String tabId = (String) tabMap.get(IGetTabEntityModel.ID);
    
    Integer sequenceNumber = getTabSequence(tabId);
    tabMap.put(IGetTabEntityModel.SEQUENCE, sequenceNumber);
    
    // fill referenced properties (PC, context and relationships)
    Map<String, Object> referencedProperties = new HashMap<String, Object>();
    Iterable<Vertex> vertices = tabNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_TAB);
    for (Vertex propertyNode : vertices) {
      String propertyId = UtilClass.getCodeNew(propertyNode);
      Map<String, Object> propertyMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IIdLabelTypeModel.LABEL), propertyNode);
      String entityType = EntityUtil.getEntityTypeByOrientClassType(
          propertyNode.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY));
      propertyMap.put(IIdLabelTypeModel.TYPE, entityType);
      referencedProperties.put(propertyId, propertyMap);
    }
    
    Map<String, Object> responseMap = new HashMap<String, Object>();
    responseMap.put(IGetTabModel.TAB, tabMap);
    responseMap.put(IGetTabModel.REFERENCED_PROPERTIES, referencedProperties);
    return responseMap;
  }
  
  public static Integer getTabSequence(String tabId) throws Exception
  {
    if (tabId.equals(SystemLevelIds.OVERVIEW_TAB)) {
      return -1;
    }
    Vertex tabSequenceNode = getOrCreateTabSequenceNode();
    List<String> sequenceList = tabSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    Integer sequenceNumber = sequenceList.indexOf(tabId);
    return sequenceNumber;
  }
  
  public static Vertex getOrCreateTabSequenceNode() throws Exception
  {
    Vertex tabSequenceNode;
    try {
      tabSequenceNode = UtilClass.getVertexById(SystemLevelIds.TAB_SEQUENCE_NODE_ID,
          VertexLabelConstants.TAB_SEQUENCE);
    }
    catch (NotFoundException e) {
      OrientVertexType vertexType = UtilClass
          .getOrCreateVertexType(VertexLabelConstants.TAB_SEQUENCE, CommonConstants.CODE_PROPERTY);
      Map<String, Object> sequenceMap = new HashMap<>();
      sequenceMap.put(CommonConstants.CODE_PROPERTY, SystemLevelIds.TAB_SEQUENCE_NODE_ID);
      sequenceMap.put(CommonConstants.SEQUENCE_LIST, new ArrayList<String>());
      tabSequenceNode = UtilClass.createNode(sequenceMap, vertexType, new ArrayList<>());
    }
    return tabSequenceNode;
  }
  
  public static void updateTabOnEntityDelete(Vertex entityNode) throws Exception
  {
    if (contextTypeToSkip.contains(entityNode.getProperty(IVariantContext.TYPE))) {
      return;
    }
    Vertex tabNode = getTabNode(entityNode);
    
    List<String> entityList = tabNode.getProperty(ITab.PROPERTY_SEQUENCE_LIST);
    entityList.remove(UtilClass.getCodeNew(entityNode));
    tabNode.setProperty(ITab.PROPERTY_SEQUENCE_LIST, entityList);
  }
  
  public static final String getStandardTabId(String entityType, String vertexType)
  {
    switch (entityType) {
      case CommonConstants.PROPERTY_COLLECTION:
        return SystemLevelIds.PROPERTY_COLLECTION_TAB;
      case CommonConstants.NATURE_RELATIONSHIP:
        return SystemLevelIds.OVERVIEW_TAB;
      case CommonConstants.RELATIONSHIP:
        return SystemLevelIds.RELATIONSHIP_TAB;
      case CommonConstants.CONTEXT:
        if (CommonConstants.IMAGE_VARIANT.equals(vertexType)) {
          return SystemLevelIds.RENDITION_TAB;
        }
        return SystemLevelIds.CONTEXT_TAB;
    }
    return null;
  }
  
  public static void addDefaultDashboardTab(Vertex entityNode, String defaultTabId) throws Exception
  {
    Vertex dashboardtab = UtilClass.getVertexByIndexedId(defaultTabId,
        VertexLabelConstants.DASHBOARD_TAB);
    entityNode.addEdge(RelationshipLabelConstants.HAS_DASHBOARD_TAB, dashboardtab);
  }
  
  public static String getDashboardTabNode(Vertex requestVertex) throws Exception
  {
    Iterator<Vertex> iterator = requestVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_DASHBOARD_TAB)
        .iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    Vertex tabNode = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return UtilClass.getCodeNew(tabNode);
  }
  
  public static void fillReferencedDashboardTabs(List<String> dashboardTabIds,
      Map<String, Object> referencedDashboardTabs) throws Exception
  {
    for (String dashboardTabId : dashboardTabIds) {
      Map<String, Object> dashboardTabMap = new HashMap<>();
      Vertex systemNode = null;
      try {
        systemNode = UtilClass.getVertexById(dashboardTabId, VertexLabelConstants.DASHBOARD_TAB);
      }
      catch (NotFoundException e) {
        continue;
      }
      dashboardTabMap.put(IConfigEntityInformationModel.ID, dashboardTabId);
      dashboardTabMap.put(IConfigEntityInformationModel.LABEL,
          UtilClass.getValueByLanguage(systemNode, IMapping.LABEL));
      dashboardTabMap.put(IConfigEntityInformationModel.CODE,
          systemNode.getProperty(IMapping.CODE));
      dashboardTabMap.put(IConfigEntityInformationModel.ICON, null);
      dashboardTabMap.put(IConfigEntityInformationModel.TYPE,
          systemNode.getProperty(IMapping.TYPE));
      referencedDashboardTabs.put(dashboardTabId, dashboardTabMap);
    }
  }
  
  public static void manageDeletedDashboardTabId(Map<String, Object> entityMap, Vertex entityNode)
      throws Exception
  {
    String deletedDashboardTabId = (String) entityMap
        .remove(ISaveEndpointModel.DELETED_DASHBOARD_TAB_ID);
    if (deletedDashboardTabId == null || deletedDashboardTabId.equals("")) {
      return;
    }
    Set<Edge> edgesToRemove = new HashSet<>();
    Iterable<Edge> hasDashboardTabEdges = entityNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_DASHBOARD_TAB);
    for (Edge hasDashboardTabEdge : hasDashboardTabEdges) {
      Vertex dashboardTabVertex = hasDashboardTabEdge.getVertex(Direction.IN);
      if (UtilClass.getCodeNew(dashboardTabVertex)
          .equals(deletedDashboardTabId)) {
        edgesToRemove.add(hasDashboardTabEdge);
      }
    }
    for (Edge edgeToRemove : edgesToRemove) {
      edgeToRemove.remove();
    }
  }
  
  public static void manageAddedDashboardTabId(Map<String, Object> entityMap, Vertex entityNode)
      throws Exception
  {
    String addedDashboardTabId = (String) entityMap
        .remove(ISaveEndpointModel.ADDED_DASHBOARD_TAB_ID);
    if (addedDashboardTabId == null || addedDashboardTabId.equals("")) {
      return;
    }
    Vertex dashboardTabVertex = UtilClass.getVertexById(addedDashboardTabId,
        VertexLabelConstants.DASHBOARD_TAB);
    entityNode.addEdge(RelationshipLabelConstants.HAS_DASHBOARD_TAB, dashboardTabVertex);
  }
  
  public static Vertex createDashboardTabNode(Map<String, Object> tabMap,
      List<String> fieldsToExclude) throws Exception
  {
    OrientVertexType vertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.DASHBOARD_TAB, CommonConstants.CODE_PROPERTY);
    
    Vertex tabNode = UtilClass.createNode(tabMap, vertexType, fieldsToExclude);
    return tabNode;
  }
  
  public static void updateTabSequence(String tabId, Integer newTabSequence) throws Exception
  {
    Vertex tabSequenceNode = TabUtils.getOrCreateTabSequenceNode();
    List<String> sequenceList = tabSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    sequenceList.remove(tabId);
    if (newTabSequence != null && newTabSequence < sequenceList.size()) {
      sequenceList.add(newTabSequence, tabId);
    }
    else {
      sequenceList.add(tabId);
    }
    tabSequenceNode.setProperty(CommonConstants.SEQUENCE_LIST, sequenceList);
  }
}
