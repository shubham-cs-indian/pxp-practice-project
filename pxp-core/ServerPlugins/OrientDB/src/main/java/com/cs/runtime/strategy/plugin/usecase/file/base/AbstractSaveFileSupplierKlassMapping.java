package com.cs.runtime.strategy.plugin.usecase.file.base;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.runtime.strategy.plugin.usecase.file.util.FileUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentEmbedded;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractSaveFileSupplierKlassMapping
    extends OServerCommandAuthenticatedDbAbstract {
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    try {
      ODatabaseDocumentEmbedded database = (ODatabaseDocumentEmbedded) getProfiledDatabaseInstance(
          iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      String requestBody = iRequest.content.toString();
      Map<String, Object> requestMap = ObjectMapperUtil.readValue(requestBody, HashMap.class);
      Map<String, Object> supplierKlassMappingMap = (Map<String, Object>) requestMap.get("mapping");
      Map<String, Object> supplierIdMap = new HashMap<>();
      supplierIdMap.put("supplierId", supplierKlassMappingMap.get("supplierId"));
      supplierIdMap.put("fileId", supplierKlassMappingMap.get("fileId"));
      execute(graph, supplierKlassMappingMap);
      graph.commit();
      
      Map<String, Object> responseMap = FileUtil.geSupplierKlassMapping(supplierIdMap);
      
      ResponseCarrier.successResponse(iResponse, responseMap);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    
    return false;
  }
  
  @SuppressWarnings({ "unchecked" })
  protected Map<String, Object> execute(OrientGraph graph,
      Map<String, Object> supplierKlassMappingMap) throws Exception
  {
    List<Map<String, Object>> fields = (List<Map<String, Object>>) supplierKlassMappingMap
        .get("fields");
    Map<String, Object> columnNamesMap = new HashMap<String, Object>();
    List<String> columnNamesList = new ArrayList<String>();
    List<String> mappedAttributesList = new ArrayList<String>();
    for (Map<String, Object> field : fields) {
      Map<String, Object> fieldMap = new HashMap<String, Object>();
      String mappedElementId = (String) field.get("mappedElementId");
      mappedElementId = mappedElementId == null ? "" : mappedElementId;
      fieldMap.put("mappedElementId", mappedElementId);
      fieldMap.put("isIgnored", (Boolean) field.get("isIgnored"));
      columnNamesMap.put((String) field.get("columnName"), fieldMap);
      columnNamesList.add((String) field.get("columnName"));
      if (!mappedElementId.equals("")) {
        mappedAttributesList.add(mappedElementId);
      }
    }
    String supplierId = (String) supplierKlassMappingMap.get("supplierId");
    Vertex supplierNode = UtilClass.getVertexById(supplierId, VertexLabelConstants.ONBOARDING_USER);
    
    Vertex mappingNode = FileUtil.getMappingNodeFromSupplierNode(supplierNode);
    
    updateColumnMapping(columnNamesMap, columnNamesList, mappingNode);
    
    createColumnMapping(columnNamesMap, columnNamesList, mappingNode);
    
    return FileUtil.geSupplierKlassMapping(supplierKlassMappingMap);
  }
  
  @SuppressWarnings({ "rawtypes" })
  private void createColumnMapping(Map<String, Object> columnNamesMap, List<String> columnNamesList,
      Vertex mappingNode) throws Exception
  {
    OrientVertexType columnMappingVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.COLUMN_MAPPING, CommonConstants.CODE_PROPERTY);
    for (String columnName : columnNamesList) {
      Map<String, Object> columnMappingNode = new HashMap<>();
      columnMappingNode.put("columnName", columnName);
      columnMappingNode.put(CommonConstants.CODE_PROPERTY,
          UtilClass.getUniqueSequenceId(columnMappingVertexType));
      Vertex columnNode = UtilClass.createNode(columnMappingNode, columnMappingVertexType,
          new ArrayList<>());
      Edge edge = mappingNode.addEdge(RelationshipLabelConstants.HAS_COLUMN_MAPPING, columnNode);
      Map columnNameMap = (Map) columnNamesMap.get(columnName);
      edge.setProperty(CommonConstants.IS_IGNORED_PROPERTY, columnNameMap.get("isIgnored"));
      String mappedElementId = columnNameMap.get("mappedElementId")
          .toString();
      mappedElementId = mappedElementId == null ? "" : mappedElementId;
      if (!mappedElementId.equals("")) {
        Vertex attributeNode = UtilClass.getVertexById(mappedElementId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        Iterable<Edge> mappedElementEdges = attributeNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_COLUMN_MAPPING);
        for (Edge mappedElementEdge : mappedElementEdges) {
          mappedElementEdge.remove();
        }
        attributeNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, columnNode);
      }
    }
  }
  
  @SuppressWarnings({ "rawtypes" })
  private void updateColumnMapping(Map<String, Object> columnNamesMap, List<String> columnNamesList,
      Vertex mappingNode) throws Exception
  {
    Iterable<Vertex> columnMappings = mappingNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_COLUMN_MAPPING);
    Iterator<Vertex> iterator = columnMappings.iterator();
    while (iterator.hasNext()) {
      Vertex columnNode = iterator.next();
      String columnName = columnNode.getProperty("columnName");
      if (columnNamesList.contains(columnName)) {
        Iterable<Edge> columnMappingEdges = columnNode.getEdges(Direction.IN,
            RelationshipLabelConstants.HAS_COLUMN_MAPPING);
        Map columnNameMap = (Map) columnNamesMap.get(columnName);
        for (Edge columnMappingEdge : columnMappingEdges) {
          columnMappingEdge.setProperty(CommonConstants.IS_IGNORED_PROPERTY,
              columnNameMap.get("isIgnored"));
        }
        Iterable<Edge> mappedToEntityEdges = columnNode.getEdges(Direction.IN,
            RelationshipLabelConstants.MAPPED_TO_ENTITY);
        for (Edge mappedToEntityEdge : mappedToEntityEdges) {
          mappedToEntityEdge.remove();
        }
        
        String mappedElementId = columnNameMap.get("mappedElementId")
            .toString();
        mappedElementId = mappedElementId == null ? "" : mappedElementId;
        if (!mappedElementId.equals("")) {
          Vertex attributeNode = UtilClass.getVertexById(mappedElementId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          Iterable<Edge> mappedElementEdges = attributeNode.getEdges(Direction.OUT,
              RelationshipLabelConstants.MAPPED_TO_ENTITY);
          for (Edge mappedElementEdge : mappedElementEdges) {
            mappedElementEdge.remove();
          }
          attributeNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, columnNode);
        }
        
        columnNamesList.remove(columnName);
      }
    }
  }
}
