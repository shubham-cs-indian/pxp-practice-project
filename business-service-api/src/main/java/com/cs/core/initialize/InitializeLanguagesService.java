package com.cs.core.initialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.language.GetOrCreateLanguageModel;
import com.cs.core.config.interactor.model.language.IGetOrCreateLanguageModel;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.config.interactor.model.language.LanguageModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.language.IGetOrCreateLanguageStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
public class InitializeLanguagesService implements IInitializeLanguageService {
  
  @Autowired
  protected IGetOrCreateLanguageStrategy                getOrCreateLanguageStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initializeLanguages();
    initializeLanguageTranslations();
  }
  
  private void initializeLanguageTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.LANGUAGE_TRANSLATIONS_JSON,
            CommonConstants.LANGUAGE);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeLanguages()
      throws IOException, JsonParseException, JsonMappingException, Exception
  {
    IGetOrCreateLanguageModel languages = new GetOrCreateLanguageModel();
    
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.LANGUAGES_JSON);
    List<ILanguageModel> languageList = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<LanguageModel>>()
        {
        });
    stream.close();
    
    for (ILanguageModel language : languageList) {
      ValidationUtils.validateLanguage(language);
      ConfigurationDAO.instance().createLanguageConfig(language.getCode(), "-1");
    }
    
    languages.setLanguages(languageList);
    getOrCreateLanguageStrategy.execute(languages);
  }
}
