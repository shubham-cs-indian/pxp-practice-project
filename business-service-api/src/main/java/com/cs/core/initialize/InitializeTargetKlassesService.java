package com.cs.core.initialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.base.interactor.model.portal.IPortalModel;
import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.target.IGetOrCreateMarketStrategy;
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
public class InitializeTargetKlassesService implements IInitializeTargetKlassesService {
  
  @Autowired
  protected IGetOrCreateMarketStrategy                  GetOrCreateTargetStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Autowired
  protected List<IPortalModel>                          portalModels;
  
  @Autowired
  protected ICreateDefaultInstanceService               createDefaultInstaceService;
  
  @Override
  public void execute() throws Exception
  {
    initializeTargetKlasses();
    initializeTargetTranslations();
  }
  
  private void initializeTargetTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.TARGET_KLASS_TRANSLATIONS_JSON,
            CommonConstants.TARGET);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeTargetKlasses()
      throws IOException, JsonParseException, JsonMappingException, Exception
  {
    List<ITargetModel> targetKlasses = new ArrayList<ITargetModel>();
    
    Set<String> allowedEntities = new HashSet<>();
    for (IPortalModel iPortalModel : portalModels) {
      allowedEntities.addAll(iPortalModel.getAllowedEntities());
    }
    
    for (String allowedEntity : allowedEntities) {
      switch (allowedEntity) {
        case "MarketInstance":
          targetKlasses
              .addAll(initializeTargetKlassesByJson(InitializeDataConstants.MARKET_KLASSES_JSON));
          break;
      }
    }
    
    IListModel<ITargetModel> targetsList = new ListModel<>();
    targetsList.setList(targetKlasses);
    GetOrCreateTargetStrategy.execute(targetsList);    
    ILocaleCatalogDAO defaultLocaleCatalogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO();
    for (ITargetModel targetKlass : targetKlasses) {
      IBaseEntityDTO entity = defaultLocaleCatalogDAO.getEntityByID(targetKlass.getCode());
      if (entity == null) {
        ITypesTaxonomiesModel typesTaxonomiesModel = new TypesTaxonomiesModel();
        typesTaxonomiesModel.getTypes().add(targetKlass.getCode());
        createDefaultInstaceService.execute(typesTaxonomiesModel);
      }
    }
    
  }
  
  private List<ITargetModel> initializeTargetKlassesByJson(String fileName) throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(fileName);
    List<ITargetModel> targetKlasses = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<AbstractKlassModel>>()
        {
        });
    stream.close();
    IConfigurationDAO configurationDAO = RDBMSUtils.newConfigurationDAO();
    for (ITargetModel targetKlass : targetKlasses) {
      ValidationUtils.validateTargetKlass(targetKlass);
      configurationDAO.createStandardClassifier(targetKlass.getClassifierIID(), targetKlass.getCode(),
          ClassifierType.CLASS);
    }
    
    return targetKlasses;
  }
}
