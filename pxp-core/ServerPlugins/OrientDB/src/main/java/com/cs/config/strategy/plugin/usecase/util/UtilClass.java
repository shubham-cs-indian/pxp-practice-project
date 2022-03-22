package com.cs.config.strategy.plugin.usecase.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.CRC32;

import org.apache.commons.lang.StringUtils;

import com.cs.config.strategy.plugin.model.ILanguageModel;
import com.cs.config.strategy.plugin.model.LanguageModel;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.duplicatename.DuplicateNameException;
import com.cs.core.config.interactor.exception.icon.IconNotFoundException;
import com.cs.core.config.interactor.model.asset.IIconModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityPaginationModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grid.IWorkflowGridFilterModel;
import com.cs.core.config.interactor.model.translations.IStandardTranslationModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.repository.language.LanguageRepository;
import com.orientechnologies.orient.core.collate.OCaseInsensitiveCollate;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentEmbedded;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.metadata.sequence.OSequence;
import com.orientechnologies.orient.core.metadata.sequence.OSequence.SEQUENCE_TYPE;
import com.orientechnologies.orient.core.metadata.sequence.OSequenceLibrary;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType.OrientVertexProperty;

@SuppressWarnings("unchecked")
public class UtilClass {
  
  public static ThreadLocal<Map<String, String>>       threadLocalAddedSectionElementIdMapping      = new ThreadLocal<>();
  public static ThreadLocal<Map<String, String>>       threadLocalAddedSectionPermissionIdMapping   = new ThreadLocal<>();
  public static ThreadLocal<Map<String, String>>       threadLocalAddedSectionIdMapping             = new ThreadLocal<>();
  public static ThreadLocal<OrientGraph>               threadLocalGraph                             = new ThreadLocal<>();
  public static ThreadLocal<Map<String, Object>>       threadLocalReferencedKlassIdStructureMapping = new ThreadLocal<>();
  public static ThreadLocal<Set<Vertex>>               threadLocalKlassNodesForVersionIncrement     = new ThreadLocal<>();
  public static ThreadLocal<ODatabaseDocumentEmbedded> threadLocalDatabase                          = new ThreadLocal<>();
  
  public static ThreadLocal<ILanguageModel>            threadLocalLanguage                          = new ThreadLocal<>();
  
  public static void removeAllFromThreadLocals()
  {
    threadLocalAddedSectionElementIdMapping.remove();
    threadLocalAddedSectionPermissionIdMapping.remove();
    threadLocalAddedSectionIdMapping.remove();
    threadLocalGraph.remove();
    threadLocalReferencedKlassIdStructureMapping.remove();
    threadLocalKlassNodesForVersionIncrement.remove();
    threadLocalLanguage.remove();
  }
  
  public static void setGraph(OrientGraph graph)
  {
    threadLocalGraph.set(graph);
  }
  
  public static OrientGraph getGraph()
  {
    return threadLocalGraph.get();
  }
  
  public static void setLanguage(ILanguageModel language)
  {
    ILanguageModel languageModel = threadLocalLanguage.get();
    if (languageModel == null) {
      languageModel = new LanguageModel();
    }
    threadLocalLanguage.set(language);
  }
  
  public static ILanguageModel getLanguage()
  {
    return threadLocalLanguage.get();
  }
  
  public static void setDatabase(ODatabaseDocumentEmbedded database)
  {
    threadLocalDatabase.set(database);
  }
  
  public static ODatabaseDocumentEmbedded getDatabase()
  {
    return threadLocalDatabase.get();
  }
  
  public static Map<String, String> getSectionElementIdMap()
  {
    return threadLocalAddedSectionElementIdMapping.get();
  }
  
  public static void setSectionElementIdMap(Map<String, String> map)
  {
    threadLocalAddedSectionElementIdMapping.set(map);
  }
  
  public static Map<String, String> getSectionPermissionIdMap()
  {
    return threadLocalAddedSectionPermissionIdMapping.get();
  }
  
  public static void setSectionPermissionIdMap(Map<String, String> map)
  {
    threadLocalAddedSectionPermissionIdMapping.set(map);
  }
  
  public static Map<String, String> getSectionIdMap()
  {
    return threadLocalAddedSectionIdMapping.get();
  }
  
  public static void setSectionIdMap(Map<String, String> map)
  {
    threadLocalAddedSectionIdMapping.set(map);
  }
  
  public static Map<String, Object> getReferencedKlassIdStructureMapping()
  {
    return threadLocalReferencedKlassIdStructureMapping.get();
  }
  
  public static void setReferencedKlassIdStructureMapping(Map<String, Object> map)
  {
    threadLocalReferencedKlassIdStructureMapping.set(map);
  }
  
  public static Set<Vertex> getNodesForVersionIncrement()
  {
    return threadLocalKlassNodesForVersionIncrement.get();
  }
  
  public static void setNodesForVersionIncrement(Set<Vertex> klassNodesForVersionIncrement)
  {
    threadLocalKlassNodesForVersionIncrement.set(klassNodesForVersionIncrement);
  }
  
  public static void addNodesForVersionIncrement(Vertex vertex)
  {
    Set<Vertex> set = threadLocalKlassNodesForVersionIncrement.get();
    if (set == null) {
      set = new HashSet<Vertex>();
    }
    set.add(vertex);
    threadLocalKlassNodesForVersionIncrement.set(set);
  }
  
  public static HashMap<String, Object> getPropertiesMapFromNode(Vertex node)
  {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.putAll(UtilClass.getMapFromNode(node));
    return map;
  }
  
  public static Vertex createDuplicateNode(Vertex nodeToDuplicate)
  {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.putAll(UtilClass.getMapFromNode(nodeToDuplicate));
    map.remove(CommonConstants.ID_PROPERTY);
    map.remove(CommonConstants.CODE_PROPERTY);
    OrientVertexType vertexType = getOrCreateVertexType(nodeToDuplicate.getProperty("@class")
        .toString(), CommonConstants.CODE_PROPERTY);
    Vertex duplicatedNode = createNode(map, vertexType);
    
    return duplicatedNode;
  }
  
  /** Creates a duplicate Node of the given node. 
   * @param nodeToDuplicate source Node
   * @param propertiesToExclude List of properties that that you wish to exclude in duplicate Node.
   * @return Duplicate Node
   * @throws Exception 
   */
  public static Vertex createDuplicateNode(Vertex nodeToDuplicate, List<String> propertiesToExclude) throws Exception
  {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.putAll(UtilClass.getMapFromNode(nodeToDuplicate));
    map.remove(CommonConstants.ID_PROPERTY);
    map.remove(CommonConstants.CODE_PROPERTY);

    OrientVertexType vertexType = getOrCreateVertexType(nodeToDuplicate.getProperty("@class")
        .toString(), CommonConstants.CODE_PROPERTY);
    Vertex duplicatedNode = createNode(map, vertexType, propertiesToExclude);
    
    return duplicatedNode;
  }
  
  @Deprecated
  public static Vertex saveNode(Map<String, Object> map, Vertex vertex)
  {
    String userLanguage = UtilClass.getLanguage()
        .getUiLanguage();
    
    for (String key : map.keySet()) {
      if (key.equals(CommonConstants.ID_PROPERTY)||key.equals(CommonConstants.CODE_PROPERTY)) {
        // do nothing
      }
      else {
        Object propertyValue = map.get(key);
        if (IStandardTranslationModel.TRNASLATION_FIELDS.contains(key)) {
          key = key + Seperators.FIELD_LANG_SEPERATOR + userLanguage;
        }
        if (propertyValue != null)
          vertex.setProperty(key, propertyValue);
      }
    }
    incrementVersionIdOfNode(vertex);
    return vertex;
  }
  
  public static List<String> deleteNode(List<String> list, String type)
  {
    OrientGraph graph = UtilClass.getGraph();
    Vertex vertex = null;
    List<String> listVertices = new ArrayList<String>();
    
    for (String key : list) {
      Iterable<Vertex> iterable = graph.getVertices(type,
          new String[] { CommonConstants.CODE_PROPERTY }, new Object[] { key });
      for (Vertex v : iterable) {
        vertex = v;
      }
      graph.removeVertex(vertex);
      listVertices.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    return listVertices;
  }
  
  @Deprecated
  public static Vertex createNode(Map<String, Object> map, OrientVertexType vertexType)
  {
    OrientGraph graph = UtilClass.getGraph();
    Vertex vertex = null;
    
    vertex = graph.addVertex("class:" + vertexType);
    // String itemId = (String) map.get(CommonConstants.ID_PROPERTY);
    String codeId = (String) map.get(CommonConstants.CODE_PROPERTY);
    if (StringUtils.isEmpty(codeId)) {
      codeId = UtilClass.getUniqueSequenceId(vertexType);
    }
    
    vertex.setProperty(CommonConstants.CODE_PROPERTY, codeId);
    
    /*if (itemId != null && !itemId.equals("")) {
      vertex.setProperty(CommonConstants.CODE_PROPERTY, itemId);
      if (codeId != null && !codeId.equals("")) {
        vertex.setProperty(CommonConstants.CODE_PROPERTY, codeId);
      }
      else {
        vertex.setProperty(CommonConstants.CODE_PROPERTY, itemId);
      }
    }*/
    
    // set versionId as 0 so that after incremnt in save it became 1
    map.put(CommonConstants.VERSION_ID, 0L);
    if (map.get(CommonConstants.CODE_PROPERTY) == null || map.get(CommonConstants.CODE_PROPERTY)
        .equals("")) {
      map.put(CommonConstants.CODE_PROPERTY, vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    vertex = UtilClass.saveNode(map, vertex);
    
    return vertex;
  }
  
  public static void validateOnType(List<String> types, String type, Boolean isNullAllowed)
      throws InvalidTypeException
  {
    if (isNullAllowed) {
      if (!(type == null || type.isEmpty())) {
        if (!(types.contains(type))) {
          throw new InvalidTypeException();
        }
      }
    }
    else if (type == null || type.isEmpty() || (!(types.contains(type)))) {
      throw new InvalidTypeException();
    }
  }
  
  @SuppressWarnings("rawtypes")
  public static OrientVertexType getOrCreateVertexType(String vertexType, String... indexFields)
  {
    OrientGraph graph = UtilClass.getGraph();
    OrientVertexType vtype = null;
    
    vtype = graph.getVertexType(vertexType);
    if (vtype == null) {
      vtype = graph.createVertexType(vertexType);
      for (String field : indexFields) {
        graph.createKeyIndex(field, Vertex.class, new Parameter("type", "UNIQUE"),
            new Parameter("class", vertexType));
      }
      /*graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
      new Parameter("type", "UNIQUE"), new Parameter("class", vertexType));*/
    }
    
    return vtype;
  }
  
  public static void createProperty(OrientVertexType vertexType, String propertyName)
  {
    OProperty property = vertexType.getProperty(propertyName);
    if (property == null) {
      vertexType.createProperty(propertyName, OType.LONG);
    }
  }
  
  public static void createVertexProperty(OrientVertexType vertexType)
  {
    OProperty property = vertexType
        .getProperty(EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY));
    if (property == null) {
      OrientVertexProperty vertexProperty = vertexType.createProperty(
          EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY), OType.STRING);
      vertexProperty.setCollate(new OCaseInsensitiveCollate());
    }
  }
  
  public static Vertex getNode(HashMap<String, Object> map, String type)
  {
    OrientGraph graph = UtilClass.getGraph();
    Vertex vertex = null;
    
    Iterable<Vertex> iterable = graph.getVertices(type,
        new String[] { CommonConstants.CODE_PROPERTY },
        new Object[] { map.get(CommonConstants.ID_PROPERTY) });
    for (Vertex v : iterable) {
      vertex = v;
    }
    
    return vertex;
  }
  
  public static List<HashMap<String, Object>> getAllNodesById(HashMap<String, Object> map,
      String type)
  {
    OrientGraph graph = UtilClass.getGraph();
    List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    
    OrientVertexType vtype = graph.getVertexType(type);
    if (vtype == null) {
      return list;
    }
    
    Iterable<Vertex> iterable = graph.getVerticesOfClass(type);
    List<String> listIds = (List<String>) map.get("ids");
    iterable = graph.getVerticesOfClass(type);
    
    for (Vertex vertex : iterable) {
      
      if (listIds.contains(vertex.getProperty(CommonConstants.CODE_PROPERTY))) {
        
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        fillMapFromVertex(vertex, returnMap);
        /*for (String key : vertex.getPropertyKeys()) {
          if (key.equals(CommonConstants.CODE_PROPERTY)) {
            returnMap.put(CommonConstants.ID_PROPERTY, vertex.getProperty(key));
          }
          else {
            returnMap.put(key, getValueByLanguage(vertex, key, defaultLanguage, userLanguage));
          }
        }*/
        list.add(returnMap);
      }
    }
    
    return list;
  }
  
  public static Edge duplicateRelationshipBetweenToNodes(Vertex duplicatedSectionElementNode,
      Vertex elementEntityNode, Edge relationshipToDuplicate)
  {
    
    Edge duplicatedRelationship = elementEntityNode.addEdge(relationshipToDuplicate.getLabel(),
        duplicatedSectionElementNode);
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    for (String key : relationshipToDuplicate.getPropertyKeys()) {
      if (key.equals(CommonConstants.CODE_PROPERTY)) {
        map.put("id", relationshipToDuplicate.getProperty(key));
      }
      else {
        map.put(key, relationshipToDuplicate.getProperty(key));
      }
    }
    
    UtilClass.saveEdge(map, duplicatedRelationship);
    return duplicatedRelationship;
  }
  
  public static HashMap<String, Object> getMapFromNode(Vertex node)
  {
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    fillMapFromVertex(node, returnMap);
    return returnMap;
  }
  
  public static HashMap<String, Object> getMapFromEdge(Edge edge)
  {
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    for (String key : edge.getPropertyKeys()) {
      if (key.equals(CommonConstants.CODE_PROPERTY)) {
        returnMap.put(CommonConstants.ID_PROPERTY, edge.getProperty(key));
        returnMap.put(key, edge.getProperty(key));
      }
      else {
        returnMap.put(key, edge.getProperty(key));
      }
    }
    return returnMap;
  }
  
  private static void saveEdge(HashMap<String, Object> map, Edge duplicatedRelationship)
  {
    
    for (String key : map.keySet()) {
      if (key.equals(CommonConstants.ID_PROPERTY)) {
        // do nothing
      }
      else {
        Object propertyValue = map.get(key);
        if (propertyValue != null)
          duplicatedRelationship.setProperty(key, propertyValue);
      }
    }
  }
  
  public static OrientEdgeType getOrCreateEdgeType(String edgeType, String... indexFields)
  {
    OrientGraph graph = UtilClass.getGraph();
    OrientEdgeType vtype = null;
    
    vtype = graph.getEdgeType(edgeType);
    if (vtype == null) {
      vtype = graph.createEdgeType(edgeType);
      // no need of index on edge..
      // graph.createKeyIndex("code", Vertex.class, new Parameter("type",
      // "UNIQUE"),
      // new Parameter("class", edgeType));
    }
    
    return vtype;
  }
  
  public static Vertex getVertexById(String id, String entityLabel) throws Exception
  {
    return getVertexByCode(id, entityLabel);
  }
  
  public static Vertex getVertexByCode(String code, String entityLabel) throws Exception
  {
    OrientGraph graph = getGraph();
    Iterable<Vertex> vertices = graph.getVertices(entityLabel,
        new String[] { CommonConstants.CODE_PROPERTY }, new String[] { code });
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    Vertex vertex = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return vertex;
  }
  
  public static Vertex getClassifierByIID(Long iid, String entityLabel) throws Exception
  {
    OrientGraph graph = getGraph();
    Iterable<Vertex> vertices = graph.getVertices(entityLabel,
        new String[] { IKlass.CLASSIFIER_IID }, new Long[] { iid });
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    Vertex vertex = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return vertex;
  }
  
  public static Iterable<Vertex> getVerticesByIds(Collection<String> ids, String entityLabel)
  {
    if (ids.size() > 0) {
      OrientGraph graph = getGraph();
      /*String indexLabel = entityLabel + "." + CommonConstants.CODE_PROPERTY;
      String query = "SELECT FROM (SELECT EXPAND(rid) FROM INDEX: " + indexLabel + ") WHERE " + CommonConstants.CODE_PROPERTY
      */
      String query = "SELECT FROM " + entityLabel + " where " + CommonConstants.CODE_PROPERTY
          + " IN [";
      for (String id : ids) {
        query += "\"" + id + "\",";
      }
      query = query.substring(0, query.length() - 1) + "]";
      Iterable<Vertex> vertices = graph.command(new OCommandSQL(query))
          .execute();
      
      return vertices;
    }
    else {
      return new ArrayList<>();
    }
  }
  
  public static Iterable<Vertex> getVerticesByIdsInSortedOrder(Collection<String> ids,
      String entityLabel, String orderField, String orderBy) throws Exception
  {
    if (ids.size() > 0) {
      OrientGraph graph = getGraph();
      String query = "SELECT FROM " + entityLabel + " where " + CommonConstants.CODE_PROPERTY
          + " IN [";
      for (String id : ids) {
        query += "\"" + id + "\",";
      }
      query = query.substring(0, query.length() - 1) + "] order by " + orderField + " " + orderBy;
      
      Iterable<Vertex> vertices = graph.command(new OCommandSQL(query))
          .execute();
      return vertices;
    }
    else {
      return new ArrayList<>();
    }
  }
  
  public static Vertex getSectionElement(Vertex sectionElementPositionNode, Vertex klassNode)
      throws Exception
  {
    Iterable<Edge> sectionElementEdges = sectionElementPositionNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ELEMENT_OF);
    String klassId = (String) klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    Vertex sectionElementNode = null;
    for (Edge sectionElementEdge : sectionElementEdges) {
      List<String> utilizingKlassIds = (List<String>) sectionElementEdge
          .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      if (utilizingKlassIds.contains(klassId)) {
        sectionElementNode = sectionElementEdge.getVertex(Direction.OUT);
      }
    }
    return sectionElementNode;
  }
  
  public static Vertex getRoleFromSectionPermissionNode(Vertex sectionPermissionNode)
      throws Exception
  {
    Iterable<Vertex> roleNodes = sectionPermissionNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF);
    Iterator<Vertex> iterator = roleNodes.iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    
    Vertex sectionElement = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return sectionElement;
  }
  
  public static Vertex getRoleFromSectionElementPermissionNode(Vertex sectionElementPermissionNode)
      throws Exception
  {
    Iterable<Vertex> roleNodes = sectionElementPermissionNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF);
    Iterator<Vertex> iterator = roleNodes.iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    
    Vertex sectionElement = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return sectionElement;
  }
  
  public static String getRecursivelyfromMap(Map<String, String> sectionElementIdMap,
      String sectionElementId)
  {
    String key = sectionElementId;
    while (sectionElementIdMap.containsKey(key)) {
      key = sectionElementIdMap.get(key);
    }
    return key;
  }
  
  public static Vertex getElementFromSectionElement(Vertex sectionElement)
      throws MultipleVertexFoundException
  {
    Iterable<Vertex> entities = sectionElement.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
    Iterator<Vertex> iterator = entities.iterator();
    Vertex entity = null;
    if (iterator.hasNext()) {
      entity = iterator.next();
    }
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return entity;
  }
  
  public static Map<String, Object> getMapFromVertex(List<String> fieldsToFetch, Vertex vertex)
  {
    return getMapFromVertex(fieldsToFetch, vertex, null);
  }
  
  public static Map<String, Object> getMapFromVertex(List<String> fieldsToFetch, Vertex vertex,
      String language)
  {
    Map<String, Object> entityMap = new HashMap<>();
    if (fieldsToFetch.size() == 0) {
      fillMapFromVertex(vertex, entityMap);
    }
    else {
      language = getCurrentLanguage(vertex);
      for (String fieldToFetch : fieldsToFetch) {
        entityMap.put(fieldToFetch, getValueByLanguage(vertex, fieldToFetch, language));
      }
      if (fieldsToFetch.contains(CommonConstants.ID_PROPERTY)) {
        entityMap.put(CommonConstants.ID_PROPERTY,
            getValueByLanguage(vertex, CommonConstants.CODE_PROPERTY, language));
      }
    }
    
    if (entityMap.get(CommonConstants.CODE_PROPERTY) != null) {
      entityMap.put(IEntity.ID, entityMap.get(CommonConstants.CODE_PROPERTY));
      // entityMap.remove(CommonConstants.CODE_PROPERTY);
    }
    
    // Fetch Icon details
    if (fieldsToFetch != null && fieldsToFetch.contains(CommonConstants.ICON_PROPERTY)) {
      fetchIconInfo(vertex, entityMap);
    }
    
    entityMap.remove(CommonConstants.DEFAULT_LABEL);
    
    return entityMap;
  }
  
  public static Vertex saveNode(Map<String, Object> map, Vertex vertex,
      List<String> fieldsToExclude) throws Exception
  {
    String userLanguage = UtilClass.getLanguage()
        .getUiLanguage();
    
    
    for (String key : map.keySet()) {
      if (key.equals(CommonConstants.ID_PROPERTY) || fieldsToExclude.contains(key) || key.equals(CommonConstants.CODE_PROPERTY) 
          || (key.equals(CommonConstants.ICON_PROPERTY) && !(VertexLabelConstants.USER_TYPES.contains(vertex.getProperty("@class"))))) {
        continue;
      }
      Object propertyValue = map.get(key);
      if (IStandardTranslationModel.TRNASLATION_FIELDS.contains(key)) {
        key = key + Seperators.FIELD_LANG_SEPERATOR + userLanguage;
      }
      if (propertyValue != null) {
        vertex.setProperty(key, propertyValue);
      }
    }
    incrementVersionIdOfNode(vertex);
    String iconId = (String) map.get(CommonConstants.ICON_PROPERTY);
    if (iconId != null && !(VertexLabelConstants.USER_TYPES.contains(vertex.getProperty("@class")))) {
      linkOrUnlinkIconToVertex(iconId, vertex);
    }
    return vertex;
  }
  
  public static Vertex createNode(Map<String, Object> map, OrientVertexType vertexType,
      List<String> fieldsToExclude) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    Vertex vertex = graph.addVertex("class:" + vertexType);
    // String itemId = (String) map.get(CommonConstants.ID_PROPERTY);
    String codeId = (String) map.get(CommonConstants.CODE_PROPERTY);
    
    map.remove(CommonConstants.ID_PROPERTY);
    
    if (StringUtils.isEmpty(codeId)) {
      codeId = UtilClass.getUniqueSequenceId(vertexType);
    }
    
    vertex.setProperty(CommonConstants.CODE_PROPERTY, codeId);
    
    /*if (itemId != null && !itemId.equals("")) {
      vertex.setProperty(CommonConstants.CODE_PROPERTY, itemId);
      if (codeId != null && !codeId.equals("")) {
        vertex.setProperty(CommonConstants.CODE_PROPERTY, codeId);
      }
      else {
        vertex.setProperty(CommonConstants.CODE_PROPERTY, itemId);
      }
    }*/
    // set versionId as 0 so that after incremnt in save it became 1
    map.put(CommonConstants.VERSION_ID, 0L);
    if (map.get(CommonConstants.CODE_PROPERTY) == null || map.get(CommonConstants.CODE_PROPERTY)
        .equals("")) {
      map.put(CommonConstants.CODE_PROPERTY, vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    vertex = UtilClass.saveNode(map, vertex, fieldsToExclude);
    
    // Default Label handling
    String label = (String) map.get(IStandardTranslationModel.LABEL);
    if (label != null && !label.isEmpty()) {
      vertex.setProperty(CommonConstants.DEFAULT_LABEL, label);
    }
    return vertex;
  }
  
  public static Edge saveEdge(Map<String, Object> map, Edge edge, List<String> fieldsToExclude)
  {
    for (String key : map.keySet()) {
      if (key.equals(CommonConstants.ID_PROPERTY) || fieldsToExclude.contains(key)) {
        continue;
      }
      Object propertyValue = map.get(key);
      if (propertyValue != null)
        edge.setProperty(key, propertyValue);
    }
    return edge;
  }
  
  public static void deleteVertices(Iterable<Vertex> verticesToDelete)
  {
    for (Vertex vertexToDelete : verticesToDelete) {
      vertexToDelete.remove();
    }
  }
  
  public static String getCodeNew(Vertex vertex)
  {
    return vertex.getProperty(CommonConstants.CODE_PROPERTY);
  }
  
  public static String getCode(Vertex vertex)
  {
    return vertex.getProperty(CommonConstants.CODE_PROPERTY);
  }
  
  public static Long getPropertyIID(Vertex vertex)
  {
    return vertex.getProperty(CommonConstants.PROPERTY_IID);
  }
  
  public static List<String> getCodes(List<Vertex> vertices)
  {
    List<String> codes = new ArrayList<>();
    for (Vertex vertex : vertices) {
      codes.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return codes;
  }
  
  public static List<String> getCodes(Iterable<Vertex> vertices)
  {
    List<String> codes = new ArrayList<>();
    for (Vertex vertex : vertices) {
      codes.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return codes;
  }
  
  public static List<Long> getPropertyIIDs(Iterable<Vertex> vertices)
  {
    List<Long> propertyIds = new ArrayList<>();
    for (Vertex vertex : vertices) {
      propertyIds.add(vertex.getProperty(CommonConstants.PROPERTY_IID)); // long 
    }
    return propertyIds;
  }
  
  public static String generateUniqueId(String... ids)
  {
    StringBuilder sb = new StringBuilder();
    for (String id : ids) {
      CRC32 crc = new CRC32();
      crc.update(id.getBytes());
      String str = Long.toHexString(crc.getValue());
      sb.append(str);
    }
    return UUID.nameUUIDFromBytes(sb.toString()
        .getBytes())
        .toString();
  }
  
  public static String generateUniqueId(List<String> ids)
  {
    StringBuilder sb = new StringBuilder();
    for (String id : ids) {
      CRC32 crc = new CRC32();
      crc.update(id.getBytes());
      String str = Long.toHexString(crc.getValue());
      sb.append(str);
    }
    return UUID.nameUUIDFromBytes(sb.toString()
        .getBytes())
        .toString();
  }
  
  public static void createVertexPropertyAndApplyFulLTextIndex(OrientVertexType vertexType,
      String... args)
  {
    for (String propertyName : args) {
      if (propertyName.equals(CommonConstants.LABEL_PROPERTY)) {
        continue;
      }
      else {
        OProperty property = vertexType.getProperty(propertyName);
        if (property == null) {
          createPropertyAndApplyFullTextIndex(vertexType, propertyName);
        }
      }
    }
  }
  
  public static void createPropertyAndApplyFullTextIndex(OrientVertexType vertexType,
      String propertyName)
  {
    OrientVertexProperty vertexProperty = vertexType.createProperty(propertyName, OType.STRING);
    vertexProperty.setCollate(new OCaseInsensitiveCollate());
    // vertexProperty.createIndex(INDEX_TYPE.FULLTEXT);
  }
  
  /** @param vertex */
  public static void incrementVersionIdOfNode(Vertex vertex)
  {
    Number versionNumber = vertex.getProperty(CommonConstants.VERSION_ID);
    if (versionNumber != null) {
      Long versionId = versionNumber.longValue();
      vertex.setProperty(CommonConstants.VERSION_ID, ++versionId);
    }
  }
  
  /**
   * Checks whether given string is null or empty
   *
   * @param string
   * @return true if string is null or empty; false otherwise
   * @author Kshitij
   */
  public static boolean isStringNullOrEmpty(String string)
  {
    return string == null || string.isEmpty();
  }
  
  public static List<String> getSuperClasses(Vertex childKlassNode)
  {
    List<String> superVertexLabels = new ArrayList<>();
    Collection<OClass> superClasses = ((OrientVertex) childKlassNode).getType()
        .getAllSuperClasses();
    for (OClass oClass : superClasses) {
      superVertexLabels.add(oClass.getName());
    }
    return superVertexLabels;
  }
  
  private static void fillMapFromVertex(Vertex vertex, Map<String, Object> entityMap)
  {
    String language = getCurrentLanguage(vertex);
    
    Set<String> languageKeys = new HashSet<String>();
    for (String key : vertex.getPropertyKeys()) {
      Object value = vertex.getProperty(key);

      if (key.equals(CommonConstants.CID_PROPERTY)) {
        continue;
      }

      if (key.equals(CommonConstants.CODE_PROPERTY)) {
        entityMap.put(CommonConstants.ID_PROPERTY, value);
        entityMap.put(key, value);
        continue;
      }
      
      if (!key.contains(Seperators.FIELD_LANG_SEPERATOR)) {
        entityMap.put(key, value);
        continue;
      }
      
      Integer _Index = key.indexOf(Seperators.FIELD_LANG_SEPERATOR);
      String keyWithoutLangSuffix = key.substring(0, _Index);
      languageKeys.add(keyWithoutLangSuffix);
    }
    
    for (String key : languageKeys) {
      
      if (language == null) { // default handling
        continue;
      }
      
      String value = vertex.getProperty(key + Seperators.FIELD_LANG_SEPERATOR + language);
      if (value == null || value.equals("")) {
        // if translations not present for user language, return blank
        value = key.equals(IStandardTranslationModel.LABEL) ? vertex.getProperty(CommonConstants.DEFAULT_LABEL) : "";
      }
      entityMap.put(key, value);
    }
    
    entityMap.remove(CommonConstants.DEFAULT_LABEL);
    
    // Fetch Icon details
    fetchIconInfo(vertex, entityMap);
  }

  /**
   * TODO :: This might be a temporary code to ensure tag value nodes labels and
   * klass and taxonomy node labels sent in data lang and others in UI lang.
   *
   * @param vertex
   * @return
   */
  private static String getCurrentLanguage(Vertex vertex)
  {
    String vertexType = vertex.getProperty("@class");
    String language = null;
    
    Boolean isTagValueVertex = vertexType.equals(VertexLabelConstants.ENTITY_TAG)
        && vertex.getProperty(ITag.TAG_TYPE) == null;
    Boolean isTaxonomyVertex = vertexType.equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
    if (isTagValueVertex || isTaxonomyVertex) {
      String taxonomyType = vertex.getProperty(ITaxonomy.TAXONOMY_TYPE);
      if (taxonomyType != null && taxonomyType.equals(CommonConstants.MINOR_TAXONOMY)) {
        language = UtilClass.getLanguage()
            .getUiLanguage();
      }
      else {
        language = UtilClass.getLanguage()
            .getDataLanguage();
      }
    }
    else {
      language = UtilClass.getLanguage()
          .getUiLanguage();
    }
    return language;
  }
  
  public static Object getValueByLanguage(Vertex vertex, String key)
  {
    String userLanguage = UtilClass.getLanguage()
        .getUiLanguage();
    return getValueByLanguage(vertex, key, userLanguage);
  }
  
  public static Object getValueByLanguage(Vertex vertex, String key, String language)
  {
    String userLanguage = UtilClass.getCurrentLanguage(vertex);
    
    if (!IStandardTranslationModel.TRNASLATION_FIELDS.contains(key)) // non translation field handling
    {
      return vertex.getProperty(key);
    }
    
    String newkey = key + Seperators.FIELD_LANG_SEPERATOR + userLanguage;
    Object propertyValue = vertex.getProperty(newkey);
    if (propertyValue == null || propertyValue.equals("")) // if user lang field empty then consider default lan
    {
      if (key.equals(IStandardTranslationModel.LABEL)) {
        return vertex.getProperty(CommonConstants.DEFAULT_LABEL);
      }
      propertyValue = "";
    }
    return propertyValue;
  }
  
  public static Vertex getInternalOrganization(OrientGraph graph) throws Exception
  {
    String query = "select from " + VertexLabelConstants.ORGANIZATION + " where "
        + IOrganization.TYPE + " = 'internal'";
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = searchResults.iterator();
    Vertex organization = iterator.next();
    if (iterator.hasNext()) {
      throw new Exception();
    }
    return organization;
  }
  
  public static Vertex getVertexByIdWithoutException(String id, String entityLabel) throws Exception
  {
    OrientGraph graph = getGraph();
    Iterable<Vertex> vertices = graph.getVertices(entityLabel,
        new String[] { CommonConstants.CODE_PROPERTY }, new String[] { id });
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      return null;
    }
    
    Vertex vertex = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return vertex;
  }
  
  public static String getIndexFromVertexType(String vertexType)
  {
    switch (vertexType) {
      case VertexLabelConstants.PROPERTY_CAN_EDIT_PERMISSION:
        return Constants.PROPERTY_CAN_EDIT_PERMISSION_INDEX;
      
      case VertexLabelConstants.PROPERTY_CAN_READ_PERMISSION:
        return Constants.PROPERTY_CAN_READ_PERMISSION_INDEX;
      
      case VertexLabelConstants.PROPERTY_COLLECTION_CAN_EDIT_PERMISSION:
        return Constants.PROPERTY_COLLECTION_CAN_EDIT_PERMISSION_INDEX;
      
      case VertexLabelConstants.PROPERTY_COLLECTION_CAN_READ_PERMISSION:
        return Constants.PROPERTY_COLLECTION_CAN_READ_PERMISSION_INDEX;
      
      case VertexLabelConstants.RELATIONSHIP_CAN_ADD_PERMISSION:
        return Constants.RELATIONSHIP_CAN_ADD_PERMISSION_INDEX;
      
      case VertexLabelConstants.RELATIONSHIP_CAN_DELETE_PERMISSION:
        return Constants.RELATIONSHIP_CAN_DELETE_PERMISSION_INDEX;
        
      case VertexLabelConstants.RELATIONSHIP_CONTEXT_CAN_EDIT_PERMISSION:
        return Constants.RELATIONSHIP_CONTEXT_CAN_EDIT_PERMISSION_INDEX;
      
      case VertexLabelConstants.RELATIONSHIP_CAN_READ_PERMISSION:
        return Constants.RELATIONSHIP_CAN_READ_PERMISSION_INDEX;
      
      case VertexLabelConstants.GLOBAL_CAN_CREATE_PERMISSIONS:
        return Constants.GLOBAL_CAN_CREATE_PERMISSIONS_INDEX;
      
      case VertexLabelConstants.GLOBAL_CAN_DELETE_PERMISSIONS:
        return Constants.GLOBAL_CAN_DELETE_PERMISSIONS_INDEX;
      
      case VertexLabelConstants.GLOBAL_CAN_EDIT_PERMISSIONS:
        return Constants.GLOBAL_CAN_EDIT_PERMISSIONS_INDEX;
      
      case VertexLabelConstants.GLOBAL_CAN_READ_PERMISSIONS:
        return Constants.GLOBAL_CAN_READ_PERMISSIONS_INDEX;
      
      case VertexLabelConstants.TEMPLATE_PERMISSION:
        return Constants.TEMPLATE_PERMISSION_INDEX;
      
      case VertexLabelConstants.ASSET_CAN_DOWNLOAD_PERMISSIONS:
        return Constants.ASSET_CAN_DOWNLOAD_PERMISSIONS_INDEX;
      
    }
    
    return null;
  }
  
  public static List<String> getListOfIds(String query)
  {
    List<String> list = new ArrayList<>();
    Iterable<Vertex> resultVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    getListOfIds(resultVertices, list);
    return list;
  }
  
  public static void getListOfIds(Iterable<Vertex> resultVertices, List<String> list)
  {
    for (Vertex vertex : resultVertices) {
      /*
       * @Author Abhaypratap: isStandardProperty not Checkced
       */
      // Boolean isStandard = (Boolean)
      // vertex.getProperty(CommonConstants.IS_STANDARD);
      // if (isStandard != null && !isStandard) {
      list.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
      // }
    }
  }
  
  public static StringBuilder getTypeQueryWithoutANDOperator(List<String> types, String typeField)
  {
    StringBuilder query = new StringBuilder();
    if (types == null || types.isEmpty()) {
      return query;
    }
    query.append(typeField + " in " + EntityUtil.quoteIt(types) + " ");
    return query;
  }
  
  public static StringBuilder getTypeNotInQuery(List<String> types, String typeField)
  {
    StringBuilder query = new StringBuilder();
    if (types == null || types.isEmpty()) {
      return query;
    }
    query.append(typeField + " not in " + EntityUtil.quoteIt(types) + " ");
    return query;
  }
  
  
  public static StringBuilder getTypeQuery(List<String> types, String typeField)
  {
    StringBuilder query = new StringBuilder();
    if (types == null || types.isEmpty()) {
      return query;
    }
    query.append(" and " + typeField + " in " + EntityUtil.quoteIt(types) + " ");
    return query;
  }
  
  public static Vertex getVertexByIndexedId(String id, String entityLabel) throws Exception
  {
    String indexLabel = entityLabel + "." + CommonConstants.CODE_PROPERTY;
    String query = "SELECT EXPAND(rid) FROM INDEX: " + indexLabel + " WHERE KEY = "
        + EntityUtil.quoteIt(id);
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    
    Vertex vertex = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return vertex;
  }
  
  public static Iterable<Vertex> getVerticesByIndexedIds(Collection<String> ids, String entityLabel)
      throws Exception
  {
    if (ids.size() > 0) {
      OrientGraph graph = getGraph();
      String indexLabel = entityLabel + "." + CommonConstants.CODE_PROPERTY;
      String query = "SELECT EXPAND(rid) FROM INDEX: " + indexLabel + " WHERE KEY IN [";
      for (String id : ids) {
        query += "\"" + id + "\",";
      }
      query = query.substring(0, query.length() - 1) + "]";
      Iterable<Vertex> vertices = graph.command(new OCommandSQL(query))
          .execute();
      
      return vertices;
    }
    else {
      return new ArrayList<>();
    }
  }
  
  public static void fillConditionForLanguageAttributeQuery(List<String> conditions,
      Map<String, Object> entityRequestInfo)
  {
    Boolean isLanguageDependent = (Boolean) entityRequestInfo
        .get(IGetConfigDataEntityPaginationModel.IS_LANGUAGE_DEPENDENT);
    Boolean isLanguageIndependent = (Boolean) entityRequestInfo
        .get(IGetConfigDataEntityPaginationModel.IS_LANGUAGE_INDEPENDENT);
    
    if (isLanguageDependent != null && isLanguageDependent.equals(true)) {
      String isLanguageDependentQuery = IAttribute.IS_TRANSLATABLE + " = " + true;
      conditions.add(isLanguageDependentQuery);
    }
    if (isLanguageIndependent != null && isLanguageIndependent.equals(true)) {
      String isLanguageIndependentQuery = IAttribute.IS_TRANSLATABLE + " = " + false;
      conditions.add(isLanguageIndependentQuery);
    }
  }
  
  public static Vertex getVertexFromIterator(Iterator<Vertex> iterator, Boolean flag)
      throws Exception
  {
    Vertex vertex = null;
    if (!iterator.hasNext()) {
      if (flag) {
        throw new NotFoundException();
      }
      else {
        return vertex;
      }
    }
    
    vertex = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return vertex;
  }
  
  /**
   * Returns the number of elements remaining in {@code iterator}. The iterator
   * will be left exhausted: its {@code hasNext()} method will return
   * {@code false}.
   */
  public static int size(Iterator<?> iterator)
  {
    int count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    return count;
  }
  
  /** Returns the number of elements in {@code iterable}. */
  public static int size(Iterable<?> iterable)
  {
    return (iterable instanceof Collection) ? ((Collection<?>) iterable).size()
        : UtilClass.size(iterable.iterator());
  }
  
  public static void isLanguageInheritanceHierarchyPresent(Map<String, Object> mapToReturn,
      List<String> languageCodes) throws Exception
  {
    if (languageCodes.size() > 1) {
      Iterator<Vertex> languageInheritedVerticesCount = LanguageRepository
          .getlanguageInheritedVerticesCount(languageCodes)
          .iterator();
      Long countLanguageHierarchy = languageInheritedVerticesCount.next()
          .getProperty("count");
      
      if (countLanguageHierarchy > 0) {
        mapToReturn.put(IGetConfigDetailsForCustomTabModel.IS_LANGUAGE_HIERARCHY_PRESENT, true);
      }
    }
  }
  
  /**
   * This function will copy edges from one node to another
   *
   * @param source
   * @param destination
   * @param relationshipLabel
   * @throws Exception
   */
  public static void copyEdgeWithoutProperties(Vertex source, Vertex destination,
      String relationshipLabel, Direction inOrOut) throws Exception
  {
    Iterable<Vertex> linkedVertices = source.getVertices(inOrOut, relationshipLabel);
    for (Vertex linkedVertex : linkedVertices) {
      destination.addEdge(relationshipLabel, linkedVertex);
    }
  }
  
  public static Vertex copyPropertiesFromNodeToNewNodeWithoutCode(Vertex source,
      OrientVertexType vertexType, List<String> propertiesToCopy) throws Exception
  {
    Map<String, Object> propertiesMap = new HashMap<>();
    propertiesMap.put(CommonConstants.ID_PROPERTY, UtilClass.getUniqueSequenceId(vertexType));
    for (String property : propertiesToCopy) {
      propertiesMap.put(property, source.getProperty(property));
    }
    Vertex destination = UtilClass.createNode(propertiesMap, vertexType, new ArrayList<>());
    return destination;
  }
  
  public static Iterable<Vertex> getVerticesFromQuery(String query)
  {
    return UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
  }
  
  public static Iterable<Edge> getEdgesFromQuery(String query)
  {
    return UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
  }
  
  public static OSequence getOrCreateSequence()
  {
    ODatabaseDocumentEmbedded database = UtilClass.getDatabase();
    OSequenceLibrary sequenceLibrary = database.getMetadata()
        .getSequenceLibrary();
    OSequence sequence = sequenceLibrary.getSequence("idseq");
    if (sequence == null) {
      sequenceLibrary.createSequence("idseq", SEQUENCE_TYPE.ORDERED,
          new OSequence.CreateParams().setStart((long) 100000)
              .setIncrement(1));
      sequence = sequenceLibrary.getSequence("idseq");
    }
    
    return sequence;
  }
  
  /**
   * @param vertexType
   *          used to define prefix of the sequence
   * @return
   */
  public static String getUniqueSequenceId(OrientVertexType vertexType)
  {
    OSequence seq = UtilClass.getOrCreateSequence();
    Long nextSequence = seq.next();
    String prefix = "A_"; // Prefix for Auto generated sequence
    if (vertexType != null) {
      prefix = vertexType.getName()
          .substring(0, 3);
    }
    
    return prefix + nextSequence;
  }
  
  public static String getCId(Vertex vertex)
  {
    return vertex.getProperty(CommonConstants.CODE_PROPERTY);
  }
  
  public static List<String> getCIds(List<Vertex> vertices)
  {
    List<String> cids = new ArrayList<>();
    for (Vertex vertex : vertices) {
      cids.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return cids;
  }
  
  public static List<String> getCIds(Iterable<Vertex> vertices)
  {
    List<String> cids = new ArrayList<>();
    for (Vertex vertex : vertices) {
      cids.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return cids;
  }
  
  public static StringBuilder getWorkflowAndEntityTypeQuery(String workflowType, String entityType)
  {
    StringBuilder query = new StringBuilder();
    if (workflowType == null) {
      return query;
    }
    query.append(IConfigGetAllRequestModel.WORKFLOW_TYPE + " = " + EntityUtil.quoteIt(workflowType));
    if (entityType != null) {
      query.append(" and " + IConfigGetAllRequestModel.ENTITY_TYPE + " = " + EntityUtil.quoteIt(entityType) + " ");
    }
    return query;
  }
  
  public static boolean fetchEdgeBetweenTwoNodes(Vertex vertex1, Vertex vertex2, String vertexLabel)
  {
    String vertexLabelQuery = "outE()";
    if(!vertexLabel.isEmpty()) {
      vertexLabelQuery = "outE('"+ vertexLabel +"')";
    }
    String query = "select from (select expand("+ vertexLabelQuery+ ") from "+ vertex1.getId() +") where in = " + vertex2.getId();
    Iterable<Edge> edges = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    return edges.iterator().hasNext();
  }
  
  public static StringBuilder getWorkflowAndEntityTypeQuery(List<String> groupOfWrolflowType,
      List<String> groupOfEventType, List<String> groupOfPhysicalCatalogIds,
      List<String> groupOfTriggeringType, List<String> groupOfTimerDefinitionType,
      List<Boolean> groupOfisExecutable, String timerStartExpression, List<String> groupOfOrganizationIds)
  {
    StringBuilder query = new StringBuilder();
    if (groupOfWrolflowType != null && !groupOfWrolflowType.isEmpty()) {
      query.append(IWorkflowGridFilterModel.WORKFLOW_TYPE + " in "
          + EntityUtil.quoteIt(groupOfWrolflowType));
      
    }
    if (groupOfEventType != null && !groupOfEventType.isEmpty()) {
      
      query.append(((query.length() != 0) ? " and " + IWorkflowGridFilterModel.EVENT_TYPE
          : IWorkflowGridFilterModel.EVENT_TYPE) + " in " + EntityUtil.quoteIt(groupOfEventType)
          + " ");
    }
    if (groupOfPhysicalCatalogIds != null && !groupOfPhysicalCatalogIds.isEmpty()) {
      
      query.append(((query.length() != 0) ? " and " + IWorkflowGridFilterModel.PHYSICAL_CATALOG_IDS
          : IWorkflowGridFilterModel.PHYSICAL_CATALOG_IDS) + " in "
          + EntityUtil.quoteIt(groupOfPhysicalCatalogIds) + " ");
    }
    if (groupOfTriggeringType != null && !groupOfTriggeringType.isEmpty()) {
      query.append(((query.length() != 0) ? " and " + IWorkflowGridFilterModel.TRIGGERINGTYPE
          : IWorkflowGridFilterModel.TRIGGERINGTYPE) + " in "
          
          + EntityUtil.quoteIt(groupOfTriggeringType) + " ");
    }
    if (groupOfTimerDefinitionType != null && !groupOfTimerDefinitionType.isEmpty()) {
      query.append(((query.length() != 0) ? " and " + IWorkflowGridFilterModel.TIMER_DEFINITION_TYPE
          : IWorkflowGridFilterModel.TIMER_DEFINITION_TYPE) + " in "
          + EntityUtil.quoteIt(groupOfTimerDefinitionType) + " ");
    }
    if (timerStartExpression != null && !timerStartExpression.isEmpty()) {
      query
          .append(((query.length() != 0) ? " and " + IWorkflowGridFilterModel.TIMER_START_EXPRESSION
              : IWorkflowGridFilterModel.TIMER_START_EXPRESSION) 
              
						+ " like '%" + timerStartExpression.toUpperCase() + "%'"  + " ");
    }
    if (groupOfisExecutable != null && !groupOfisExecutable.isEmpty()) {
      
      query.append(((query.length() != 0) ? " and isExecutable " : "isExecutable") + " in [");
      int counter = 1;
      for (Boolean isExecutable : groupOfisExecutable) {
        if (counter == groupOfisExecutable.size()) {
          query.append(isExecutable);
        }
        else {
          query.append(isExecutable + ",");
        }
        counter++;
      }
      query.append(" ]");
    }
    if (groupOfOrganizationIds != null && !groupOfOrganizationIds.isEmpty()) {
      query.append(((query.length() != 0) ? " and " + IProcessEvent.ORGANIZATIONS_IDS
          : IProcessEvent.ORGANIZATIONS_IDS) + " in " + EntityUtil.quoteIt(groupOfOrganizationIds)
          + " ");
    }
 
    return query;
  }
  
	public static Vertex copyPropertiesFromNodeToNewNodeWithoutCID(Vertex source, OrientVertexType vertexType,
			List<String> propertiesToCopy) throws Exception {
		Map<String, Object> propertiesMap = new HashMap<>();
		propertiesMap.put(CommonConstants.ID_PROPERTY, UUID.randomUUID().toString());
		for (String property : propertiesToCopy) {
			propertiesMap.put(property, source.getProperty(property));
		}
		Vertex destination = UtilClass.createNode(propertiesMap, vertexType, new ArrayList<>());
		return destination;
	}
	
	/**
	 * Creates/removes an edge between entity node and icon node. Direction out.
	 * @param iconId
	 * @param vertex
	 * @throws Exception 
	 */
	private static void linkOrUnlinkIconToVertex(String iconId, Vertex vertex) throws Exception
  {
	  try {
      if (!iconId.isEmpty()) {
        if (!isIconEdgeExists(vertex, iconId)) {
          Vertex iconNode = UtilClass.getVertexById(iconId, VertexLabelConstants.ENTITY_TYPE_ICON);
          // Unlink previously linked icon before linking new icon node.
          unlinkIcon(vertex);
          vertex.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_ICON, iconNode);
        }
      }
      else {
        unlinkIcon(vertex);
      }
    }
    catch (NotFoundException e) {
      throw new IconNotFoundException(e);
    }    
  }

  /**
   * Unlinks icon from the entity node.
   * @param vertex
   */
  private static void unlinkIcon(Vertex vertex)
  {
    Iterable<Edge> iconEdges = vertex.getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_ICON);
    for (Edge iconEdge : iconEdges) {
      iconEdge.remove();
    }
  }
	
	/**
   * Fetches Icon details.
   * @param vertex
   * @param entityMap
   */
  public static void fetchIconInfo(Vertex vertex, Map<String, Object> entityMap)
  {
    Iterable<Edge> iconEdges = vertex.getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_ICON);
    for (Edge iconEdge : iconEdges) {
      entityMap.put(CommonConstants.ICON_PROPERTY, iconEdge.getVertex(Direction.IN).getProperty(IIconModel.CODE));
      entityMap.put(IIconModel.ICON_KEY, iconEdge.getVertex(Direction.IN).getProperty(IIconModel.ICON_KEY));
    }
  }
  
  /**
   * Checks icon edge exist or not.
   * @param vertex
   * @param iconId
   * @return
   */
  private static Boolean isIconEdgeExists(Vertex vertex, String iconId)
  {
    Iterable<Edge> iconEdges = vertex.getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_ICON);
    for (Edge iconEdge : iconEdges) {
      if (iconId.equals(iconEdge.getVertex(Direction.IN).getProperty(IIconModel.CODE))) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * check icon is present or not if not then set null
   * @param map
   * @param icon
   * @throws Exception
   */
  public static void validateIconExistsForImport(Map<String, Object> map) throws Exception
  {
    String icon = (String) map.get(CommonConstants.ICON_PROPERTY);
    if(StringUtils.isNotEmpty(icon)) {
      try {
        UtilClass.getVertexByCode(icon, VertexLabelConstants.ENTITY_TYPE_ICON);
      }catch(NotFoundException n) {
        map.put(CommonConstants.ICON_PROPERTY, null);
      }
    }
  }
  
  public static OrientVertexType getOrCreateVertexProperty(OrientVertexType vertexNode, String property, OType propertyType) {
    if (vertexNode.getProperty(property) == null) {
      vertexNode.createProperty(property, propertyType);
    }
    return vertexNode;
  }

  public static OrientVertexType createOrRebuildIndex(OrientVertexType vertexNode, String indexName, INDEX_TYPE indexType, final String... fields) {
    OIndex<?> index = vertexNode.getClassIndex(VertexLabelConstants.ROOT_KLASS_TAXONOMY + ".isRootMajorTaxonomy");
    if (index == null) {
     index = vertexNode.createIndex(indexName, indexType, fields);
    }
    if (index.isAutomatic()) {
      index.rebuild();
    }
    return vertexNode;
  }
  
    /**
   * Check name is present or not for entity type
   * to handle duplicate name
   * @param name
   * @param entityName
   * @return
   * @throws DuplicateNameException 
   */
  public static void checkDuplicateName(String name, String entityName) throws DuplicateNameException
  {
    String query = "select from " + entityName + " where " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " = "
        + EntityUtil.quoteIt(name);
    Iterable<Vertex> duplicateNameVertices = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    if (duplicateNameVertices.iterator().hasNext()) {
      throw new DuplicateNameException();
    }
  }
}
