package com.cs.config.strategy.plugin.usecase.smartdocument.template;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.smartdocument.base.SmartDocumentTemplateUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.smartdocument.template.ISmartDocumentTemplate;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateSmartDocumentTemplate extends AbstractOrientPlugin {
  
  protected static final List<String> fieldsToExclude = Arrays.asList(
      ISmartDocumentTemplate.LAST_MODIFIED_BY, ISmartDocumentTemplate.VERSION_ID,
      ISmartDocumentTemplate.VERSION_TIMESTAMP);
  
  public CreateSmartDocumentTemplate(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateSmartDocumentTemplate/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String smartDocumentTemplateId = (String) requestMap.get(CommonConstants.CODE_PROPERTY);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SMART_DOCUMENT_TEMPLATE, CommonConstants.CODE_PROPERTY);
    if (smartDocumentTemplateId == null || smartDocumentTemplateId.isEmpty()) {
      smartDocumentTemplateId = UtilClass.getUniqueSequenceId(vertexType);
      requestMap.put(CommonConstants.CODE_PROPERTY, smartDocumentTemplateId);
    }
    
    UtilClass.createNode(requestMap, vertexType, fieldsToExclude);
    return SmartDocumentTemplateUtils.getSmartDocumentTemplateById(smartDocumentTemplateId);
  }
}
