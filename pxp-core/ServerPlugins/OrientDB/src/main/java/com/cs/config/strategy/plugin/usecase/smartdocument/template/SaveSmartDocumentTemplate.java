package com.cs.config.strategy.plugin.usecase.smartdocument.template;

import com.cs.config.strategy.plugin.usecase.smartdocument.base.SmartDocumentTemplateUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.smartdocument.template.ISmartDocumentTemplate;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveSmartDocumentTemplate extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToExclude = Arrays.asList(
      ISmartDocumentTemplate.LAST_MODIFIED_BY, ISmartDocumentTemplate.VERSION_ID,
      ISmartDocumentTemplate.VERSION_TIMESTAMP);
  
  public SaveSmartDocumentTemplate(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveSmartDocumentTemplate/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Vertex smartDocumentTemplateVertex = UtilClass.getVertexById(
        (String) requestMap.get(ISmartDocumentTemplateModel.ID),
        VertexLabelConstants.SMART_DOCUMENT_TEMPLATE);
    UtilClass.saveNode(requestMap, smartDocumentTemplateVertex, fieldsToExclude);
    String smartDocumentTemplateId = (String) requestMap.get(ISmartDocumentTemplateModel.ID);
    returnMap = SmartDocumentTemplateUtils.getSmartDocumentTemplateById(smartDocumentTemplateId);
    returnMap.put(IGetSmartDocumentTemplateWithPresetModel.SMART_DOCUMENT_PRESETS,
        SmartDocumentTemplateUtils.getSmartDocumentPresetsByTemplateId(smartDocumentTemplateId));
    return returnMap;
  }
}
