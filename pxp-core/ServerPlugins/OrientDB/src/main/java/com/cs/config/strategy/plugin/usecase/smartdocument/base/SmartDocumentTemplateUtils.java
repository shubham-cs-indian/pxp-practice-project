package com.cs.config.strategy.plugin.usecase.smartdocument.base;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SmartDocumentTemplateUtils {
  
  public static Map<String, Object> getSmartDocumentTemplateById(String smartDocumentTemplateId)
      throws Exception
  {
    Vertex smartDocumentTemplateVertex = UtilClass.getVertexById(smartDocumentTemplateId,
        VertexLabelConstants.SMART_DOCUMENT_TEMPLATE);
    Map<String, Object> smartDocumentTemplateMap = UtilClass
        .getMapFromNode(smartDocumentTemplateVertex);
    return smartDocumentTemplateMap;
  }
  
  public static List<Map<String, Object>> getSmartDocumentPresetsByTemplateId(
      String smartDocumentTemplateId) throws Exception
  {
    List<Map<String, Object>> smartDocumentPresetsList = new ArrayList<>();
    Vertex vertex = UtilClass.getVertexById(smartDocumentTemplateId,
        VertexLabelConstants.SMART_DOCUMENT_TEMPLATE);
    Iterable<Vertex> iterator = vertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.SMART_DOCUMENT_TEMPLATE_PRESET_LINK);
    for (Vertex smartDocumentPreset : iterator) {
      smartDocumentPresetsList.add(
          UtilClass.getMapFromVertex(Arrays.asList(IIdLabelTypeModel.ID, IIdLabelTypeModel.LABEL,
              IIdLabelTypeModel.TYPE, IIdLabelTypeModel.CODE), smartDocumentPreset));
    }
    return smartDocumentPresetsList;
  }
  
  public static List<Map<String, Object>> getAllSmartDocumentTemplates() throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> smartDocumentTemplates = graph
        .getVerticesOfClass(VertexLabelConstants.SMART_DOCUMENT_TEMPLATE);
    List<Map<String, Object>> smartDocumentTemplatesList = new ArrayList<>();
    
    for (Vertex smartDocumentTemplate : smartDocumentTemplates) {
      smartDocumentTemplatesList.add(UtilClass.getMapFromVertex(
          Arrays.asList(IIdLabelModel.ID, IIdLabelModel.LABEL), smartDocumentTemplate));
    }
    return smartDocumentTemplatesList;
  }
}
