package com.cs.core.initialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.klass.IProjectKlassModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.klass.IGetOrCreateKlassStrategy;
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
public class InitializePIMKlassesService implements IInitializePIMKlassesService {
  
  @Autowired
  protected IGetOrCreateKlassStrategy                   getOrCreateKlassStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Autowired
  protected ICreateDefaultInstanceService               createDefaultInstaceService;
  
  @Override
  public void execute() throws Exception
  {
    initializePIMKlasses();
    initializePIMKlassTranslations();
  }
  
  private void initializePIMKlassTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.ARTICLE_KLASS_TRANSLATIONS_JSON,
            CommonConstants.ARTICLE_ENTITY);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializePIMKlasses()
      throws IOException, JsonParseException, JsonMappingException, Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.ARTICLE_KLASSES_JSON);
    List<IProjectKlassModel> pimKlasses = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<AbstractKlassModel>>()
        {
        });
    stream.close();
    IConfigurationDAO configurationDAO = RDBMSUtils.newConfigurationDAO();
    for (IProjectKlassModel pimKlass : pimKlasses) {
      ValidationUtils.validatePIMKlass(pimKlass);
      configurationDAO.createStandardClassifier(pimKlass.getClassifierIID(), pimKlass.getCode(), ClassifierType.CLASS);
    }
    
    IListModel<IKlassModel> klassesList = new ListModel<>();
    klassesList.setList(pimKlasses);
    getOrCreateKlassStrategy.execute(klassesList);
    
    ILocaleCatalogDAO defaultLocaleCatalogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO();
    for (IProjectKlassModel pimKlass : pimKlasses) {
      IBaseEntityDTO entity = defaultLocaleCatalogDAO.getEntityByID(pimKlass.getCode());
      // create default instance of klass if not exist for given type
      if (entity == null) {
        ITypesTaxonomiesModel typesTaxonomiesModel = new TypesTaxonomiesModel();
        typesTaxonomiesModel.getTypes().add(pimKlass.getCode());
        createDefaultInstaceService.execute(typesTaxonomiesModel);
      }
    }
  }
}
