package com.cs.core.config.interactor.usecase.systemstatictranslation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.hidden.HiddenResponseModel;
import com.cs.core.config.interactor.model.hidden.IGetPropertyTranslationsHiddenModel;
import com.cs.core.config.interactor.model.hidden.IGetTranslationsResponseHiddenModel;
import com.cs.core.config.interactor.model.hidden.IHiddenResponseModel;
import com.cs.core.config.interactor.model.translations.GetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.systemstatictranslation.IExportSystemStaticTranslationStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.HiddenConstants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Service
public class ExportSystemStaticTranslation
    extends AbstractGetConfigInteractor<IModel, IHiddenResponseModel>
    implements IExportSystemStaticTranslation {
  
  @Autowired
  protected IExportSystemStaticTranslationStrategy exportSystemStaticTranslationStrategy;
  
  @Autowired
  protected String                                 defaultLanguage;
  
  @Autowired
  protected String                                 hotFolderPath;
  
  Map<String, Object>                              content__de_DE;
  Map<String, Object>                              content__en_UK;
  Map<String, Object>                              content__en_US;
  Map<String, Object>                              content__es_ES;
  Map<String, Object>                              content__fr_FR;
  
  Map<String, Object>                              dashboard__de_DE;
  Map<String, Object>                              dashboard__en_UK;
  Map<String, Object>                              dashboard__en_US;
  Map<String, Object>                              dashboard__es_ES;
  Map<String, Object>                              dashboard__fr_FR;
  
  Map<String, Object>                              home__de_DE;
  Map<String, Object>                              home__en_UK;
  Map<String, Object>                              home__en_US;
  Map<String, Object>                              home__es_ES;
  Map<String, Object>                              home__fr_FR;
  
  Map<String, Object>                              login__de_DE;
  Map<String, Object>                              login__en_UK;
  Map<String, Object>                              login__en_US;
  Map<String, Object>                              login__es_ES;
  Map<String, Object>                              login__fr_FR;
  
  Map<String, Object>                              setting__de_DE;
  Map<String, Object>                              setting__en_UK;
  Map<String, Object>                              setting__en_US;
  Map<String, Object>                              setting__es_ES;
  Map<String, Object>                              setting__fr_FR;
  
  Map<String, Object>                              view__de_DE;
  Map<String, Object>                              view__en_UK;
  Map<String, Object>                              view__en_US;
  Map<String, Object>                              view__es_ES;
  Map<String, Object>                              view__fr_FR;
  
  Map<String, Map<String, Object>>                 dataMap;
  
  @Override
  public IHiddenResponseModel executeInternal(IModel dataModel) throws Exception
  {
    processForTranslation();
    IHiddenResponseModel responseModel = new HiddenResponseModel();
    responseModel.setResponse(CommonConstants.SUCCESS);
    return responseModel;
  }
  
  private void processForTranslation() throws Exception
  {
    initializeData();
    IGetTranslationsRequestModel dataModel = prepareRequestModel();
    processOfDataFetchAndStoring(dataModel);
    writeDataIntoSingleMap();
    writeTranslationDataIntoFile();
  }
  
  private void writeDataIntoSingleMap()
  {
    dataMap.put(HiddenConstants.CONTENT + Seperators.FIELD_LANG_SEPERATOR + Constants.GERMAN,
        content__de_DE);
    // dataMap.put(HiddenConstants.CONTENT + Seperators.FIELD_LANG_SEPERATOR +
    // Constants.ENGLISH_UK, content__en_UK);
    dataMap.put(HiddenConstants.CONTENT + Seperators.FIELD_LANG_SEPERATOR + Constants.ENGLISH_US,
        content__en_US);
    dataMap.put(HiddenConstants.CONTENT + Seperators.FIELD_LANG_SEPERATOR + Constants.SPANISH,
        content__es_ES);
    dataMap.put(HiddenConstants.CONTENT + Seperators.FIELD_LANG_SEPERATOR + Constants.FRENCH,
        content__fr_FR);
    
    dataMap.put(HiddenConstants.DASHBOARD + Seperators.FIELD_LANG_SEPERATOR + Constants.GERMAN,
        dashboard__de_DE);
    // dataMap.put(HiddenConstants.DASHBOARD + Seperators.FIELD_LANG_SEPERATOR +
    // Constants.ENGLISH_UK, dashboard__en_UK);
    dataMap.put(HiddenConstants.DASHBOARD + Seperators.FIELD_LANG_SEPERATOR + Constants.ENGLISH_US,
        dashboard__en_US);
    dataMap.put(HiddenConstants.DASHBOARD + Seperators.FIELD_LANG_SEPERATOR + Constants.SPANISH,
        dashboard__es_ES);
    dataMap.put(HiddenConstants.DASHBOARD + Seperators.FIELD_LANG_SEPERATOR + Constants.FRENCH,
        dashboard__fr_FR);
    
    dataMap.put(HiddenConstants.HOME + Seperators.FIELD_LANG_SEPERATOR + Constants.GERMAN,
        home__de_DE);
    // dataMap.put(HiddenConstants.HOME + Seperators.FIELD_LANG_SEPERATOR +
    // Constants.ENGLISH_UK,
    // home__en_UK);
    dataMap.put(HiddenConstants.HOME + Seperators.FIELD_LANG_SEPERATOR + Constants.ENGLISH_US,
        home__en_US);
    dataMap.put(HiddenConstants.HOME + Seperators.FIELD_LANG_SEPERATOR + Constants.SPANISH,
        home__es_ES);
    dataMap.put(HiddenConstants.HOME + Seperators.FIELD_LANG_SEPERATOR + Constants.FRENCH,
        home__fr_FR);
    
    dataMap.put(HiddenConstants.LOGIN + Seperators.FIELD_LANG_SEPERATOR + Constants.GERMAN,
        login__de_DE);
    // dataMap.put(HiddenConstants.LOGIN + Seperators.FIELD_LANG_SEPERATOR +
    // Constants.ENGLISH_UK,
    // login__en_UK);
    dataMap.put(HiddenConstants.LOGIN + Seperators.FIELD_LANG_SEPERATOR + Constants.ENGLISH_US,
        login__en_US);
    dataMap.put(HiddenConstants.LOGIN + Seperators.FIELD_LANG_SEPERATOR + Constants.SPANISH,
        login__es_ES);
    dataMap.put(HiddenConstants.LOGIN + Seperators.FIELD_LANG_SEPERATOR + Constants.FRENCH,
        login__fr_FR);
    
    dataMap.put(HiddenConstants.SETTING + Seperators.FIELD_LANG_SEPERATOR + Constants.GERMAN,
        setting__de_DE);
    // dataMap.put(HiddenConstants.SETTING + Seperators.FIELD_LANG_SEPERATOR +
    // Constants.ENGLISH_UK, setting__en_UK);
    dataMap.put(HiddenConstants.SETTING + Seperators.FIELD_LANG_SEPERATOR + Constants.ENGLISH_US,
        setting__en_US);
    dataMap.put(HiddenConstants.SETTING + Seperators.FIELD_LANG_SEPERATOR + Constants.SPANISH,
        setting__es_ES);
    dataMap.put(HiddenConstants.SETTING + Seperators.FIELD_LANG_SEPERATOR + Constants.FRENCH,
        setting__fr_FR);
    
    dataMap.put(HiddenConstants.VIEW + Seperators.FIELD_LANG_SEPERATOR + Constants.GERMAN,
        view__de_DE);
    // dataMap.put(HiddenConstants.VIEW + Seperators.FIELD_LANG_SEPERATOR +
    // Constants.ENGLISH_UK,
    // view__en_UK);
    dataMap.put(HiddenConstants.VIEW + Seperators.FIELD_LANG_SEPERATOR + Constants.ENGLISH_US,
        view__en_US);
    dataMap.put(HiddenConstants.VIEW + Seperators.FIELD_LANG_SEPERATOR + Constants.SPANISH,
        view__es_ES);
    dataMap.put(HiddenConstants.VIEW + Seperators.FIELD_LANG_SEPERATOR + Constants.FRENCH,
        view__fr_FR);
  }
  
  private void writeTranslationDataIntoFile() throws Exception
  {
    String absolutePath = new File(hotFolderPath).getAbsolutePath();
    absolutePath = absolutePath + "//" + HiddenConstants.STATIC_TRANSLATION_EXPORT;
    createOrCheckDirectoryExist(absolutePath);
    writeFileIntoDirectory(absolutePath);
  }
  
  private void writeFileIntoDirectory(String absolutePath) throws Exception
  {
    String writeValueAsString = null;
    Set<String> keyOfLanguageFileNames = dataMap.keySet();
    for (String keyOfLanguageFileName : keyOfLanguageFileNames) {
      Map<String, Object> languageData = dataMap.get(keyOfLanguageFileName);
      writeValueAsString = ObjectMapperUtil.writeValueAsString(languageData);
      writeValueIntoFile(absolutePath, keyOfLanguageFileName, writeValueAsString);
    }
  }
  
  private void writeValueIntoFile(String absolutePath, String fileName, String writeValueAsString)
  {
    BufferedWriter bufferedWriter = null;
    FileWriter fileWriter = null;
    
    try {
      fileWriter = new FileWriter(
          absolutePath + "//" + fileName + HiddenConstants.JSON_FILE_FORMAT);
      bufferedWriter = new BufferedWriter(fileWriter, 4096);
      bufferedWriter.write(writeValueAsString);
    }
    catch (IOException e) {
      RDBMSLogger.instance().exception(e);
    }
    finally {
      try {
        if (bufferedWriter != null)
          bufferedWriter.close();
        
        if (fileWriter != null)
          fileWriter.close();
        
      }
      catch (IOException ex) {
        RDBMSLogger.instance().exception(ex);
      }
    }
  }
  
  private void createOrCheckDirectoryExist(String absolutePath)
  {
    File file = new File(absolutePath);
    if (!file.exists()) {
      file.mkdir();
    }
    else {
      File[] listFiles = file.listFiles();
      for (File fileToDelete : listFiles) {
        fileToDelete.delete();
      }
    }
  }
  
  private void processOfDataFetchAndStoring(IGetTranslationsRequestModel dataModel) throws Exception
  {
    Long size = 0l;
    Long from = 0l;
    Long responseCount = 0l;
    Long currentCountValue = 0l;
    Boolean loopBreak;
    
    do {
      loopBreak = false;
      size = new Long(dataModel.getSize());
      from = new Long(dataModel.getFrom());
      
      IGetTranslationsResponseHiddenModel responseData = exportSystemStaticTranslationStrategy
          .execute(dataModel);
      
      responseCount = responseData.getCount();
      currentCountValue = from + size;
      if (currentCountValue <= responseCount) {
        from += size;
        dataModel.setFrom(from.intValue());
        loopBreak = true;
      }
      
      processResponseData(responseData);
    }
    while (loopBreak);
  }
  
  private void processResponseData(IGetTranslationsResponseHiddenModel responseData)
  {
    List<IGetPropertyTranslationsHiddenModel> translationData = responseData.getData();
    for (IGetPropertyTranslationsHiddenModel iGetPropertyTranslationsModel : translationData) {
      List<String> screens = iGetPropertyTranslationsModel.getScreens();
      
      for (String screen : screens) {
        saveTranslationDataForScreen(screen, iGetPropertyTranslationsModel);
      }
    }
  }
  
  private void saveTranslationDataForScreen(String screen,
      IGetPropertyTranslationsHiddenModel iGetPropertyTranslationsModel)
  {
    String id = iGetPropertyTranslationsModel.getId();
    switch (screen) {
      case HiddenConstants.CONTENT:
        content__de_DE.put(id, iGetPropertyTranslationsModel.getLabel__de_DE());
        content__en_UK.put(id, iGetPropertyTranslationsModel.getLabel__en_UK());
        content__en_US.put(id, iGetPropertyTranslationsModel.getLabel__en_US());
        content__es_ES.put(id, iGetPropertyTranslationsModel.getLabel__es_ES());
        content__fr_FR.put(id, iGetPropertyTranslationsModel.getLabel__fr_FR());
        break;
      
      case HiddenConstants.DASHBOARD:
        dashboard__de_DE.put(id, iGetPropertyTranslationsModel.getLabel__de_DE());
        dashboard__en_UK.put(id, iGetPropertyTranslationsModel.getLabel__en_UK());
        dashboard__en_US.put(id, iGetPropertyTranslationsModel.getLabel__en_US());
        dashboard__es_ES.put(id, iGetPropertyTranslationsModel.getLabel__es_ES());
        dashboard__fr_FR.put(id, iGetPropertyTranslationsModel.getLabel__fr_FR());
        break;
      
      case HiddenConstants.HOME:
        home__de_DE.put(id, iGetPropertyTranslationsModel.getLabel__de_DE());
        home__en_UK.put(id, iGetPropertyTranslationsModel.getLabel__en_UK());
        home__en_US.put(id, iGetPropertyTranslationsModel.getLabel__en_US());
        home__es_ES.put(id, iGetPropertyTranslationsModel.getLabel__es_ES());
        home__fr_FR.put(id, iGetPropertyTranslationsModel.getLabel__fr_FR());
        break;
      
      case HiddenConstants.LOGIN:
        login__de_DE.put(id, iGetPropertyTranslationsModel.getLabel__de_DE());
        login__en_UK.put(id, iGetPropertyTranslationsModel.getLabel__en_UK());
        login__en_US.put(id, iGetPropertyTranslationsModel.getLabel__en_US());
        login__es_ES.put(id, iGetPropertyTranslationsModel.getLabel__es_ES());
        login__fr_FR.put(id, iGetPropertyTranslationsModel.getLabel__fr_FR());
        break;
      
      case HiddenConstants.SETTING:
        setting__de_DE.put(id, iGetPropertyTranslationsModel.getLabel__de_DE());
        setting__en_UK.put(id, iGetPropertyTranslationsModel.getLabel__en_UK());
        setting__en_US.put(id, iGetPropertyTranslationsModel.getLabel__en_US());
        setting__es_ES.put(id, iGetPropertyTranslationsModel.getLabel__es_ES());
        setting__fr_FR.put(id, iGetPropertyTranslationsModel.getLabel__fr_FR());
        break;
      
      case HiddenConstants.VIEW:
        view__de_DE.put(id, iGetPropertyTranslationsModel.getLabel__de_DE());
        view__en_UK.put(id, iGetPropertyTranslationsModel.getLabel__en_UK());
        view__en_US.put(id, iGetPropertyTranslationsModel.getLabel__en_US());
        view__es_ES.put(id, iGetPropertyTranslationsModel.getLabel__es_ES());
        view__fr_FR.put(id, iGetPropertyTranslationsModel.getLabel__fr_FR());
        break;
    }
  }
  
  private void initializeData()
  {
    dataMap = new HashMap<>();
    
    content__de_DE = new HashMap<>();
    content__en_UK = new HashMap<>();
    content__en_US = new HashMap<>();
    content__es_ES = new HashMap<>();
    content__fr_FR = new HashMap<>();
    
    dashboard__de_DE = new HashMap<>();
    dashboard__en_UK = new HashMap<>();
    dashboard__en_US = new HashMap<>();
    dashboard__es_ES = new HashMap<>();
    dashboard__fr_FR = new HashMap<>();
    
    home__de_DE = new HashMap<>();
    home__en_UK = new HashMap<>();
    home__en_US = new HashMap<>();
    home__es_ES = new HashMap<>();
    home__fr_FR = new HashMap<>();
    
    login__de_DE = new HashMap<>();
    login__en_UK = new HashMap<>();
    login__en_US = new HashMap<>();
    login__es_ES = new HashMap<>();
    login__fr_FR = new HashMap<>();
    
    setting__de_DE = new HashMap<>();
    setting__en_UK = new HashMap<>();
    setting__en_US = new HashMap<>();
    setting__es_ES = new HashMap<>();
    setting__fr_FR = new HashMap<>();
    
    view__de_DE = new HashMap<>();
    view__en_UK = new HashMap<>();
    view__en_US = new HashMap<>();
    view__es_ES = new HashMap<>();
    view__fr_FR = new HashMap<>();
  }
  
  private IGetTranslationsRequestModel prepareRequestModel()
  {
    IGetTranslationsRequestModel dataModel = new GetTranslationsRequestModel();
    dataModel.setSize(1000);
    dataModel.setFrom(0);
    dataModel.setLanguages(Constants.SUPPORTED_LANGUAGES);
    dataModel.setSearchLanguage(defaultLanguage);
    dataModel.setSearchText("");
    dataModel.setSearchField("");
    dataModel.setSortBy(CommonConstants.LABEL_PROPERTY);
    dataModel.setSortLanguage(defaultLanguage);
    dataModel.setSortOrder("asc");
    return dataModel;
  }
}
