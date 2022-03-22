package com.cs.runtime.strategy.plugin.usecase.file.util;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUtil {
  
  public static Vertex getAttributeFromColumnMapping(Vertex columnMapping) throws Exception
  {
    Vertex vertex = null;
    Iterable<Vertex> vertices = columnMapping.getVertices(Direction.IN,
        RelationshipLabelConstants.MAPPED_TO_ENTITY);
    Iterator<Vertex> iterator = vertices.iterator();
    
    if (iterator.hasNext()) {
      vertex = iterator.next();
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return vertex;
  }
  
  public static Map<String, Object> geSupplierKlassMapping(Map<String, Object> supplierKlassIdMap)
      throws Exception
  {
    String supplierId = (String) supplierKlassIdMap.remove("supplierId");
    Vertex supplierNode = UtilClass.getVertexById(supplierId, VertexLabelConstants.ONBOARDING_USER);
    
    Vertex mappingNode = getMappingNodeFromSupplierNode(supplierNode);
    
    List<Map<String, Object>> fields = new ArrayList<>();
    Iterable<Edge> hasColumnMappings = mappingNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_COLUMN_MAPPING);
    for (Edge hasColumnMapping : hasColumnMappings) {
      Vertex columnMappingNode = hasColumnMapping.getVertex(Direction.IN);
      Map<String, Object> field = UtilClass.getMapFromVertex(new ArrayList<>(), columnMappingNode);
      Vertex sectionElementNode = FileUtil.getAttributeFromColumnMapping(columnMappingNode);
      if (sectionElementNode != null) {
        field.put("mappedElementId",
            sectionElementNode.getProperty(CommonConstants.CODE_PROPERTY));
      }
      field.put("isIgnored", hasColumnMapping.getProperty("isIgnored"));
      fields.add(field);
    }
    
    supplierKlassIdMap.put("fields", fields);
    supplierKlassIdMap.put("mappedToKlass", null);
    
    return supplierKlassIdMap;
  }
  
  public static Vertex getMappingNodeFromSupplierNode(Vertex supplierNode) throws Exception
  {
    Vertex vertex = null;
    Iterable<Vertex> vertices = supplierNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_MAPPING);
    Iterator<Vertex> iterator = vertices.iterator();
    
    if (iterator.hasNext()) {
      vertex = iterator.next();
    }
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return vertex;
  }
}
