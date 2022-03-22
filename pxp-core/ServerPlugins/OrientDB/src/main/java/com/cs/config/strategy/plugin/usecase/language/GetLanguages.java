package com.cs.config.strategy.plugin.usecase.language;

import com.cs.config.strategy.plugin.model.ILanguageModel;
import com.cs.config.strategy.plugin.model.LanguageModel;
import com.cs.config.strategy.plugin.usecase.language.util.LanguageUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.language.IGetLanguagesRequestModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesResponseModel;
import com.cs.repository.language.LanguageRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetLanguages extends AbstractOrientPlugin {
  
  public GetLanguages(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetLanguages/*" };
  }
  
  private static final List<String> fieldsToFetch = Arrays.asList(ILanguage.LABEL, ILanguage.CODE,
      ILanguage.ICON, ILanguage.ID, ILanguage.DATE_FORMAT, ILanguage.NUMBER_FORMAT,
      ILanguage.LOCALE_ID, ILanguage.ABBREVIATION);
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Boolean isGetDataLanguages = (Boolean) requestMap
        .get(IGetLanguagesRequestModel.IS_GET_DATA_LANGUAGES);
    Boolean isGetUILanguages = (Boolean) requestMap
        .get(IGetLanguagesRequestModel.IS_GET_DATA_LANGUAGES);
    
    if (!isGetDataLanguages && !isGetUILanguages) {
      return new HashMap<>();
    }
    
    Vertex defaultLanguageVertex = LanguageUtil.getDefaultLanguageVertex(true);
    
    // if data language null set dataLanguage as default language
    String dataLanguage = (String) requestMap.get(IGetLanguagesRequestModel.DATA_LANGUAGE);
    String defaultLanguage = defaultLanguageVertex.getProperty(ILanguage.CODE);
    dataLanguage = dataLanguage != null && !dataLanguage.isEmpty() ? dataLanguage : defaultLanguage;
    
    // if ui language null set ui language as default language
    String uiLanguage = (String) requestMap.get(IGetLanguagesRequestModel.UI_LANGUAGE);
    uiLanguage = uiLanguage != null && !uiLanguage.isEmpty() ? uiLanguage : defaultLanguage;
    
    ILanguageModel languageModel = new LanguageModel();
    languageModel.setDataLanguage(dataLanguage);
    languageModel.setUiLanguage(uiLanguage);
    UtilClass.setLanguage(languageModel);
    
    List<Map<String, Object>> dataLanguages = null;
    if (isGetDataLanguages) {
      dataLanguages = getLanguages(ILanguage.IS_DATA_LANGUAGE, dataLanguage);
    }
    
    List<Map<String, Object>> uiLanguages = null;
    if (isGetUILanguages) {
      uiLanguages = getLanguages(ILanguage.IS_USER_INTERFACE_LANGUAGE, uiLanguage);
    }
    
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IGetLanguagesResponseModel.DATA_LANGUAGES, dataLanguages);
    mapToReturn.put(IGetLanguagesResponseModel.UI_LANGUAGES, uiLanguages);
    mapToReturn.put(IGetLanguagesResponseModel.DEFAULT_LANGUAGE, defaultLanguage);
    
    return mapToReturn;
  }
  
  private List<Map<String, Object>> getLanguages(String key, String dataLanguage) throws Exception
  {
    List<Map<String, Object>> languages = new ArrayList<>();
    Iterable<Vertex> languageVertices = LanguageRepository.getDataOrUILanguages(key);
    for (Vertex language : languageVertices) {
      Map<String, Object> languageMap = UtilClass.getMapFromVertex(fieldsToFetch, language,
          dataLanguage);
      languages.add(languageMap);
    }
    return languages;
  }
}
