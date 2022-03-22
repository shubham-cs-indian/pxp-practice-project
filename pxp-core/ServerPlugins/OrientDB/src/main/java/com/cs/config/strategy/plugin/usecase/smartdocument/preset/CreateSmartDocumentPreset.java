package com.cs.config.strategy.plugin.usecase.smartdocument.preset;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.strategy.plugin.usecase.smartdocument.base.SmartDocumentPresetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateSmartDocumentPreset extends AbstractOrientPlugin {
  
  public CreateSmartDocumentPreset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateSmartDocumentPreset/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String smartDocumentPresetId = (String) requestMap.get(CommonConstants.CODE_PROPERTY);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SMART_DOCUMENT_PRESET, CommonConstants.CODE_PROPERTY);
    
    if (StringUtils.isEmpty(smartDocumentPresetId)) {
      smartDocumentPresetId = UtilClass.getUniqueSequenceId(vertexType);
      requestMap.put(CommonConstants.CODE_PROPERTY, smartDocumentPresetId);
    }
    
    String smartDocumentTemplateId = (String) requestMap
        .get(ISmartDocumentPresetModel.SMART_DOCUMENT_TEMPLATE_ID);
    
    Vertex smartDocumentTemplate = UtilClass.getVertexById(smartDocumentTemplateId,
        VertexLabelConstants.SMART_DOCUMENT_TEMPLATE);
    
    Vertex smartDocumentPresetNode = UtilClass.createNode(requestMap, vertexType,
        new ArrayList<>());
    
    smartDocumentTemplate.addEdge(RelationshipLabelConstants.SMART_DOCUMENT_TEMPLATE_PRESET_LINK,
        smartDocumentPresetNode);
    
    return SmartDocumentPresetUtils.getSmartDocumentPresetById(smartDocumentPresetId);
  }
}
