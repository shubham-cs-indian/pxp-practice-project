package com.cs.config.strategy.plugin.usecase.translations;

import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.translations.ISaveBasicTranslationRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SaveStaticTranslations extends AbstractOrientPlugin {
  
  public SaveStaticTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveStaticTranslations/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> languages = (List<String>) requestMap.get(ISaveTranslationsRequestModel.LANGAUGES);
    
    List<Map<String, Object>> list = (List<Map<String, Object>>) requestMap
        .get(ISaveBasicTranslationRequestModel.DATA);
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> returnList = new ArrayList<>();
    
    TranslationsUtils.saveTranslations(VertexLabelConstants.UI_TRANSLATIONS, languages, list,
        failure, returnList);
    
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> responseMap = new HashMap<String, Object>();
    responseMap.put(ISaveTranslationsResponseModel.SUCCESS, returnList);
    responseMap.put(ISaveTranslationsResponseModel.FAILURE, failure);
    return responseMap;
  }
}
