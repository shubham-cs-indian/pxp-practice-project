package com.cs.core.initialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.textasset.IGetOrCreateTextAssetStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.defaultklassinstance.ICreateDefaultInstanceService;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.TypesTaxonomiesModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
public class InitializeTextAssetKlassesService implements IInitializeTextAssetKlassesService {
  
  @Autowired
  protected IGetOrCreateTextAssetStrategy               getOrCreateTextAssetStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Autowired
  protected ICreateDefaultInstanceService               createDefaultInstaceService;
  
  @Override
  public void execute() throws Exception
  {
    initializeTextAssetKlasses();
    initializeTextAssetKlassTranslations();
  }
  
  private void initializeTextAssetKlassTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.TEXT_ASSET_KLASS_TRANSLATIONS_JSON,
            CommonConstants.TEXT_ASSET_ENTITY);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeTextAssetKlasses()
      throws IOException, JsonParseException, JsonMappingException, Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.TEXT_ASSET_KLASSES_JSON);
    List<ITextAssetModel> textAssetKlasses = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<AbstractKlassModel>>()
        {
        });
    stream.close();
    IConfigurationDAO configurationDAO = RDBMSUtils.newConfigurationDAO();
    for (ITextAssetModel textAssetKlass : textAssetKlasses) {
      ValidationUtils.validateTextAssetKlass(textAssetKlass);
      configurationDAO.createStandardClassifier(textAssetKlass.getClassifierIID(), textAssetKlass.getCode(), ClassifierType.CLASS);
    }
    
    IListModel<ITextAssetModel> textAssetList = new ListModel<>();
    textAssetList.setList(textAssetKlasses);
    getOrCreateTextAssetStrategy.execute(textAssetList);
    
    ILocaleCatalogDAO defaultLocaleCatalogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO();
    for (ITextAssetModel textAssetKlass : textAssetKlasses) {
      IBaseEntityDTO entity = defaultLocaleCatalogDAO.getEntityByID(textAssetKlass.getCode());
      if (entity == null) {
        ITypesTaxonomiesModel typesTaxonomiesModel = new TypesTaxonomiesModel();
        typesTaxonomiesModel.getTypes().add(textAssetKlass.getCode());
        createDefaultInstaceService.execute(typesTaxonomiesModel);
      }
    }
  }
}
