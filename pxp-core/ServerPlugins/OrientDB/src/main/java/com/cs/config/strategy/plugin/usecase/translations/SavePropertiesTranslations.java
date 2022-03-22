package com.cs.config.strategy.plugin.usecase.translations;

import com.cs.config.strategy.plugin.usecase.translations.abstrct.AbstractSavePropertiesTranslations;
import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.List;
import java.util.Map;

public class SavePropertiesTranslations extends AbstractSavePropertiesTranslations {
  
  public SavePropertiesTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SavePropertiesTranslations/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }
  
  @Override
  protected void saveTranslations(String vertexLabel, List<String> languages,
      List<Map<String, Object>> list, IExceptionModel failure, List<Map<String, Object>> returnList)
  {
    TranslationsUtils.saveTranslations(vertexLabel, languages, list, failure, returnList);
  }
}
