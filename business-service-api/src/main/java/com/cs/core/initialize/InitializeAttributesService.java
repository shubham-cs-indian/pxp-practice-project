package com.cs.core.initialize;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IConfigMap;
import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.attribute.IGetOrCreateAttributeStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeAttributesService implements IInitializeAttributesService {
  
  @Autowired
  protected IGetOrCreateAttributeStrategy               getOrCreateAttributesStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initializeAttributes();
    initializeAttributeTranslations();
  }
  
  private void initializeAttributeTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.ATTRIBUTE_TRANSLATIONS_JSON,
            CommonConstants.ATTRIBUTE);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  /**
   * Create property in RDBMS then create property in OrientDB with collected
   * propertyIID from RDBMS
   *
   * @throws Exception
   */
  private void initializeAttributes() throws Exception
  {
    List<IAttribute> attributeList = getAttributes();
    
    IListModel<IAttribute> attributes = new ListModel<>();
    attributes.setList(attributeList);
    
    getOrCreateAttributesStrategy.execute(attributes);
  }
  
  private List<IAttribute> getAttributes() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.ATTRIBUTES_JSON);
    List<IAttribute> attributeList = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<IAttribute>>()
        {
        });
    stream.close();
    
    IConfigurationDAO configurationDAO = RDBMSAppDriverManager.getDriver().newConfigurationDAO();
    for (IAttribute attribute : attributeList) {
      ValidationUtils.validateAttribute(attribute);
        configurationDAO.createStandardProperty(attribute.getPropertyIID(), attribute.getCode(),
            IConfigMap.getPropertyType(attribute.getType()));
      }
    
    return attributeList;
  }
}
