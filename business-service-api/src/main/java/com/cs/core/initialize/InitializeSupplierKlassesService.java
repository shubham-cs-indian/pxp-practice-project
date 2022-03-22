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
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.supplier.IGetOrCreateSupplierStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
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
public class InitializeSupplierKlassesService implements IInitializeSupplierKlassesService {
  
  @Autowired
  protected IGetOrCreateSupplierStrategy                getOrCreateSupplierStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Autowired
  protected ICreateDefaultInstanceService               createDefaultInstaceService;
  
  @Override
  public void execute() throws Exception
  {
    initializeSupplierKlass();
    initializeSupplierKlassTranslations();
  }
  
  private void initializeSupplierKlassTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.SUPPLIER_KLASS_TRANSLATIONS_JSON,
            CommonConstants.SUPPLIER_ENTITY);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeSupplierKlass()
      throws IOException, JsonParseException, JsonMappingException, Exception
  {
    InputStream stream = InitializeSupplierKlassesService.class.getClassLoader()
        .getResourceAsStream(InitializeDataConstants.SUPPLIER_KLASSES_JSON);
    List<ISupplierModel> supplierKlasses = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<AbstractKlassModel>>()
        {
        });
    stream.close();
    
    for (ISupplierModel supplierKlass : supplierKlasses) {
      ValidationUtils.validateSupplierKlass(supplierKlass);
      RDBMSUtils.newConfigurationDAO().createStandardClassifier(supplierKlass.getClassifierIID(), supplierKlass.getCode(),
          ClassifierType.CLASS);
      
    }
    
    IListModel<ISupplierModel> suppliersList = new ListModel<>();
    suppliersList.setList(supplierKlasses);
    
    getOrCreateSupplierStrategy.execute(suppliersList);
    
    ILocaleCatalogDAO defaultLocaleCatalogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO();
    for (ISupplierModel supplierKlass : supplierKlasses) {
      IBaseEntityDTO entity = defaultLocaleCatalogDAO.getEntityByID(supplierKlass.getCode());
      // create default instance of klass if not exist for given type
      if (entity == null) {
        ITypesTaxonomiesModel typesTaxonomiesModel = new TypesTaxonomiesModel();
        typesTaxonomiesModel.getTypes().add(supplierKlass.getCode());
        createDefaultInstaceService.execute(typesTaxonomiesModel);
      }
    }
  }
}
