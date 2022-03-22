package com.cs.config.strategy.plugin.usecase.smartdocument;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.smartdocument.base.SmartDocumentTemplateUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.smartdocument.ISmartDocument;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class SaveSmartDocument extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToExclude = Arrays.asList(ISmartDocument.LAST_MODIFIED_BY,
      ISmartDocument.VERSION_ID, ISmartDocument.VERSION_TIMESTAMP);
  
  public SaveSmartDocument(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveSmartDocument/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex smartDocumentVertex = UtilClass.getVertexById(SystemLevelIds.SMART_DOCUMENT_ID,
        VertexLabelConstants.SMART_DOCUMENT);
    
    UtilClass.saveNode(requestMap, smartDocumentVertex, fieldsToExclude);
    
    smartDocumentVertex = UtilClass.getVertexById(SystemLevelIds.SMART_DOCUMENT_ID,
        VertexLabelConstants.SMART_DOCUMENT);
    Map<String, Object> smartDocumentMap = UtilClass.getMapFromNode(smartDocumentVertex);
    smartDocumentMap.put(IGetSmartDocumentWithTemplateModel.SMART_DOCUMENT_TEMPLATES,
        SmartDocumentTemplateUtils.getAllSmartDocumentTemplates());
    return smartDocumentMap;
  }
}
