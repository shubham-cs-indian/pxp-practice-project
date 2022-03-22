package com.cs.core.initialize;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;
import com.cs.core.config.interactor.model.smartdocument.SmartDocumentModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.smartdocument.IGetOrCreateSmartDocumentStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class InitializeSmartDocumentService implements IInitializeSmartDocumentService {
  
  @Autowired
  protected IGetOrCreateSmartDocumentStrategy           getOrCreateSmartDocumentStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initializeSmartDocuments();
    initializeSmartDocumentTranslations();
  }
  
  private void initializeSmartDocumentTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.SMART_DOCUMENT_TRANSLATIONS_JSON,
            CommonConstants.SMART_DOCUMENT_ENTITY);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeSmartDocuments() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.SMART_DOCUMENTS_JSON);
    ISmartDocumentModel smartDocument = ObjectMapperUtil.readValue(stream,
        SmartDocumentModel.class);
    stream.close();
    
    ValidationUtils.validateSmartDocument(smartDocument);
    
    getOrCreateSmartDocumentStrategy.execute(smartDocument);
  }
}
