package com.cs.core.initialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.PropertyCollection;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.usecase.propertycollection.IGetOrCreatePropertyCollectionStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
public class InitializePropertyCollectionsService implements IInitializePropertyCollectionService {
  
  @Autowired
  IGetOrCreatePropertyCollectionStrategy                getOrCreatePropertyCollectionStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initializePropertyCollections();
    initializePropertyCollectionTranslations();
  }
  
  private void initializePropertyCollectionTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(
            InitializeDataConstants.PROPERTY_COLLECTION_TRANSLATIONS_JSON,
            CommonConstants.PROPERTY_COLLECTION);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializePropertyCollections()
      throws IOException, JsonParseException, JsonMappingException, Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.PROPERTY_COLLECTIONS_JSON);
    List<IPropertyCollection> propertyCollections = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<PropertyCollection>>()
        {
        });
    stream.close();
    
    for (IPropertyCollection propertyCollection : propertyCollections) {
      ValidationUtils.validatePropertyCollection(propertyCollection);
    }
    
    IListModel<IPropertyCollection> propertyCollectionsListModel = new ListModel<>();
    propertyCollectionsListModel.setList(propertyCollections);
    getOrCreatePropertyCollectionStrategy.execute(propertyCollectionsListModel);
  }
}
