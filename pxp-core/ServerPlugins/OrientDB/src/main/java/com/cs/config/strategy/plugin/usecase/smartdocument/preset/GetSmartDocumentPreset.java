package com.cs.config.strategy.plugin.usecase.smartdocument.preset;

import com.cs.config.strategy.plugin.usecase.smartdocument.base.SmartDocumentPresetUtils;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetSmartDocumentPreset extends AbstractOrientPlugin {
  
  public GetSmartDocumentPreset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSmartDocumentPreset/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String smartDocumentPresetId = (String) requestMap.get(IIdParameterModel.ID);
    return SmartDocumentPresetUtils.getSmartDocumentPresetById(smartDocumentPresetId);
  }
}
