package com.cs.di.config.strategy.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterPropertyEntity;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.model.language.CreateLanguageModel;
import com.cs.core.config.interactor.model.language.LanguageModel;
import com.cs.core.config.interactor.usecase.language.ICreateLanguage;
import com.cs.core.config.interactor.usecase.language.IGetLanguage;
import com.cs.core.config.interactor.usecase.language.ISaveLanguage;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LanguageAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  protected ICreateLanguage  createLanguage;
  @Autowired
  protected IGetLanguage     getLanguage;
  @Autowired
  protected ISaveLanguage    saveLanguage;
  
  public static final String ENTITY_MAP            = "entity";
  public static final String DEFAULT_NUMBER_FORMAT = "###,###,###.##";
  public static final String DEFAULT_DATE_FORMAT   = "DD/MM/YYYY HH:mm:ss";
  
  private static final ObjectMapper mapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    IIdParameterModel parameterModel = new IdParameterModel(code);
    return getLanguage.execute(parameterModel);
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    CreateLanguageModel createLanguageModel = mapper.convertValue(inputData, CreateLanguageModel.class);
    createLanguageModel.setNumberFormat(DEFAULT_NUMBER_FORMAT);
    createLanguageModel.setDateFormat(DEFAULT_DATE_FORMAT);
    return createLanguage.execute(createLanguageModel);
  }
  
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    Map<String, Object> entityMap = (Map<String, Object>) getData.get(ENTITY_MAP);
    LanguageModel langSaveModel = new LanguageModel();
    langSaveModel.setLabel((String) updateEntity(inputData, entityMap, IConfigMasterPropertyEntity.LABEL));
    langSaveModel.setIcon((String) updateEntity(inputData, entityMap, IConfigMasterPropertyEntity.ICON));
    langSaveModel.setLocaleId((String) updateEntity(inputData, entityMap, ILanguage.LOCALE_ID));
    langSaveModel.setAbbreviation((String) updateEntity(inputData, entityMap, ILanguage.ABBREVIATION));
    langSaveModel.setNumberFormat((String) updateEntity(inputData, entityMap, ILanguage.NUMBER_FORMAT));
    langSaveModel.setDateFormat((String) updateEntity(inputData, entityMap, ILanguage.DATE_FORMAT));
    langSaveModel.setIsDataLanguage((Boolean) updateEntity(inputData, entityMap, ILanguage.IS_DATA_LANGUAGE));
    langSaveModel.setIsUserInterfaceLanguage((Boolean) updateEntity(inputData, entityMap, ILanguage.IS_USER_INTERFACE_LANGUAGE));
    langSaveModel.setIsDefaultLanguage((Boolean) updateEntity(inputData, entityMap, ILanguage.IS_DEFAULT_LANGUAGE));
    
    langSaveModel.setId((String) entityMap.get(IConfigMasterPropertyEntity.ID));
    langSaveModel.setCode((String) entityMap.get(IConfigMasterPropertyEntity.CODE));
    langSaveModel.setIsStandard((Boolean) entityMap.get(IConfigMasterPropertyEntity.IS_STANDARD)); 
    langSaveModel.setParent(fetchParentForUpdate(entityMap));
    langSaveModel.setChildren((List<? extends ITreeEntity>) entityMap.get(CreateLanguageModel.CHILDREN));
    return saveLanguage.execute(langSaveModel);
  }
  
  /**
   * updating fields
   * 
   * @param inputData
   * @param getData
   * @param key
   * @return
   */
  Object updateEntity(Map<String, Object> inputData, Map<String, Object> getData, String key)
  {
    if (inputData.containsKey(key)) {
      return inputData.get(key);
    }
    else {
      return getData.get(key);
    }
  }
  
  /**
   * @param fetch parent details from get call
   * @return
   */
  private ITreeEntity fetchParentForUpdate(Map<String, Object> entityMap)
  {
    ITreeEntity parent = new CreateLanguageModel();
    Map<String, Object> parentMap = (Map<String, Object>) entityMap.get(CreateLanguageModel.PARENT);
    parent.setId((String) parentMap.get(CommonConstants.ID_PROPERTY));
    return parent;
  }
}
