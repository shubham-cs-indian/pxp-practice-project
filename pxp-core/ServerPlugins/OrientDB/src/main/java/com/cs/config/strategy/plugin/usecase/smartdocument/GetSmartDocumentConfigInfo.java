package com.cs.config.strategy.plugin.usecase.smartdocument;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.smartdocument.base.SmartDocumentPresetUtils;
import com.cs.config.strategy.plugin.usecase.smartdocument.base.SmartDocumentTemplateUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.smartdocument.ISmartDocument;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentInfoModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetSmartDocumentConfigInfo extends AbstractOrientPlugin {
  
  public GetSmartDocumentConfigInfo(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSmartDocumentConfigInfo/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    Map<String, Object> smartDocumentPresetMap = SmartDocumentPresetUtils
        .getSmartDocumentPresetById((String) requestMap.get(IIdParameterModel.ID));
    String smartDocumentTemplateId = (String) smartDocumentPresetMap
        .get(ISmartDocumentPresetModel.SMART_DOCUMENT_TEMPLATE_ID);
    Map<String, Object> smartDocumentTemplateMap = SmartDocumentTemplateUtils
        .getSmartDocumentTemplateById(smartDocumentTemplateId);
    Vertex smartDocumentVertex = UtilClass.getVertexById(SystemLevelIds.SMART_DOCUMENT_ID,
        VertexLabelConstants.SMART_DOCUMENT);
    Map<String, Object> smartDocumentReturnMap = UtilClass.getMapFromNode(smartDocumentVertex);
    
    String currentUiLang = UtilClass.getLanguage()
        .getUiLanguage();
    String presetLangCode = (String) smartDocumentPresetMap
        .get(IGetSmartDocumentPresetModel.LANGUAGE_CODE);
    presetLangCode = presetLangCode.isEmpty() ? UtilClass.getLanguage()
        .getDataLanguage() : presetLangCode;
    UtilClass.getLanguage()
        .setUiLanguage(presetLangCode);
    Vertex smartDocumentPresetVertex = UtilClass.getVertexById(
        (String) requestMap.get(IIdParameterModel.ID), VertexLabelConstants.SMART_DOCUMENT_PRESET);
    String presetLabel = (String) UtilClass.getValueByLanguage(smartDocumentPresetVertex,
        IGetSmartDocumentPresetModel.LABEL);
    if (!presetLabel.isEmpty()) {
      smartDocumentPresetMap.put(IGetSmartDocumentPresetModel.LABEL, presetLabel);
    }
    else {
      String code = (String) smartDocumentPresetMap.get(CommonConstants.CODE_PROPERTY);
      smartDocumentPresetMap.put(IGetSmartDocumentPresetModel.LABEL, code);
    }
    UtilClass.getLanguage()
        .setUiLanguage(currentUiLang);
    
    returnMap.put(IGetSmartDocumentInfoModel.RENDERER_LICENCE_KEY,
        smartDocumentReturnMap.get(ISmartDocument.RENDERER_LICENCE_KEY));
    returnMap.put(IGetSmartDocumentInfoModel.ZIP_TEMPLATE_ID,
        smartDocumentTemplateMap.get(ISmartDocumentTemplateModel.ZIP_TEMPLATE_ID));
    returnMap.put(IGetSmartDocumentInfoModel.SMART_DOCUMENT_PRESET, smartDocumentPresetMap);
    return returnMap;
  }
}
