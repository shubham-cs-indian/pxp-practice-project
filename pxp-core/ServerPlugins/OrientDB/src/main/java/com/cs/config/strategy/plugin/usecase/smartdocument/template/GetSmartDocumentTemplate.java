package com.cs.config.strategy.plugin.usecase.smartdocument.template;

import com.cs.config.strategy.plugin.usecase.smartdocument.base.SmartDocumentTemplateUtils;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.Map;

public class GetSmartDocumentTemplate extends AbstractOrientPlugin {
  
  public GetSmartDocumentTemplate(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSmartDocumentTemplate/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String smartDocumentTemplateId = (String) requestMap
        .get(IGetSmartDocumentTemplateWithPresetModel.ID);
    returnMap = SmartDocumentTemplateUtils.getSmartDocumentTemplateById(smartDocumentTemplateId);
    returnMap.put(IGetSmartDocumentTemplateWithPresetModel.SMART_DOCUMENT_PRESETS,
        SmartDocumentTemplateUtils.getSmartDocumentPresetsByTemplateId(smartDocumentTemplateId));
    return returnMap;
  }
}
