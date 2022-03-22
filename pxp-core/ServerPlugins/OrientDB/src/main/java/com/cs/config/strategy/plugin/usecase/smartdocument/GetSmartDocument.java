package com.cs.config.strategy.plugin.usecase.smartdocument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetSmartDocument extends AbstractOrientPlugin {
  
  public GetSmartDocument(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSmartDocument/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Vertex smartDocumentVertex = UtilClass.getVertexById(SystemLevelIds.SMART_DOCUMENT_ID,
        VertexLabelConstants.SMART_DOCUMENT);
    Map<String, Object> smartDocumentReturnMap = UtilClass.getMapFromNode(smartDocumentVertex);
    
    Iterable<Vertex> smartDocumentTemplates = graph
        .getVerticesOfClass(VertexLabelConstants.SMART_DOCUMENT_TEMPLATE);
    List<Map<String, Object>> smartDocumentTemplatesList = new ArrayList<>();
    
    for (Vertex smartDocumentTemplate : smartDocumentTemplates) {
      smartDocumentTemplatesList.add(
          UtilClass.getMapFromVertex(Arrays.asList(IIdLabelTypeModel.ID, IIdLabelTypeModel.LABEL,
              IIdLabelTypeModel.TYPE, IIdLabelTypeModel.CODE), smartDocumentTemplate));
    }
    
    smartDocumentReturnMap.put(IGetSmartDocumentWithTemplateModel.SMART_DOCUMENT_TEMPLATES,
        smartDocumentTemplatesList);
    return smartDocumentReturnMap;
  }
}
