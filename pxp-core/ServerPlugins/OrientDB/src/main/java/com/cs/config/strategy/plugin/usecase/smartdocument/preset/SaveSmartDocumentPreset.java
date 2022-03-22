package com.cs.config.strategy.plugin.usecase.smartdocument.preset;

import com.cs.config.strategy.plugin.usecase.smartdocument.base.SaveSmartDocumentPresetUtils;
import com.cs.config.strategy.plugin.usecase.smartdocument.base.SmartDocumentPresetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.smartdocument.preset.ISaveSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SaveSmartDocumentPreset extends AbstractOrientPlugin {
  
  public SaveSmartDocumentPreset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveSmartDocumentPreset/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> smartDocumentPresetModel = (Map<String, Object>) requestMap
        .get(ISaveSmartDocumentPresetModel.SMART_DCOUMENT_PRESET);
    String smartDocumentPresetId = (String) smartDocumentPresetModel
        .get(ISmartDocumentPresetModel.ID);
    Vertex smartDocumentVertex = UtilClass.getVertexById(smartDocumentPresetId,
        VertexLabelConstants.SMART_DOCUMENT_PRESET);
    
    SaveSmartDocumentPresetUtils.manageLinkedProperties(smartDocumentVertex, requestMap);
    SaveSmartDocumentPresetUtils.handleAttributeRules(smartDocumentVertex, requestMap);
    SaveSmartDocumentPresetUtils.handleTagRules(smartDocumentVertex, requestMap);
    SaveSmartDocumentPresetUtils.manageFlatProperties(smartDocumentVertex, requestMap);
    return SmartDocumentPresetUtils.getSmartDocumentPresetById(smartDocumentPresetId);
  }
}
