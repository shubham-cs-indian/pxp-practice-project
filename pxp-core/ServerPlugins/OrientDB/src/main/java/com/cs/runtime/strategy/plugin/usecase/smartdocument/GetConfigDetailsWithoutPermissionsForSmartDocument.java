package com.cs.runtime.strategy.plugin.usecase.smartdocument;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.model.smartdocument.IMulticlassificationRequestModelForSmartDocument;
import com.cs.runtime.strategy.plugin.usecase.klassinstance.GetConfigDetailsWithoutPermissions;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetConfigDetailsWithoutPermissionsForSmartDocument
    extends GetConfigDetailsWithoutPermissions {
  
  public GetConfigDetailsWithoutPermissionsForSmartDocument(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsWithoutPermissionsForSmartDocument/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String selectedLanguage = (String) requestMap.get(IMulticlassificationRequestModelForSmartDocument.SELECTED_LANGUAGE);
    selectedLanguage = (!selectedLanguage.isEmpty()) ? selectedLanguage : UtilClass.getLanguage().getDataLanguage();
    UtilClass.getLanguage().setDataLanguage(selectedLanguage);
    UtilClass.getLanguage().setUiLanguage(selectedLanguage);
    return getConfigDetailWithoutPermissions(requestMap);
  }
}
