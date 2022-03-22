package com.cs.di.config.strategy.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.translations.AbstractSaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.SaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.usecase.translations.ISavePropertiesTranslations;
import com.cs.core.config.interactor.usecase.translations.ISaveRelationshipTranslations;
import com.cs.core.config.interactor.usecase.translations.ISaveStaticTranslations;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
@Service("translationAPIStrategy")
public class TranslationAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  protected ISavePropertiesTranslations   savePropertiesTranslations;
  
  @Autowired
  protected ISaveStaticTranslations       saveStaticTranslations;
  
  @Autowired
  protected ISaveRelationshipTranslations saveRelationshipTranslations;
  
  public static final String              ENTITY_TYPE  = "entityType";
  private static final ObjectMapper       objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
      false);
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> tagData, IConfigAPIRequestModel configModel) throws Exception
  {
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    return null;
  }
  
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    String entityType = inputData.get(ENTITY_TYPE).toString();
    switch (entityType) {
      case CommonConstants.STATIC_TRANSLATION:
        AbstractSaveTranslationsRequestModel staticModel = objectMapper.readValue(configModel.getData(),
            AbstractSaveTranslationsRequestModel.class);
        return saveStaticTranslations.execute(staticModel);
      case CommonConstants.ENDPOINT:
      case CommonConstants.SYSTEM:
      case CommonConstants.LANGUAGE:
      case CommonConstants.TEMPLATE:
      case CommonConstants.TASK:
      case CommonConstants.DASHBOARD_TAB:
      case CommonConstants.TAB:
      case CommonConstants.RULE:
      case CommonConstants.RULE_LIST:
      case CommonConstants.PROPERTY_COLLECTION:
      case CommonConstants.PROCESS:
      case CommonConstants.MAPPING:
      case CommonConstants.KEYPERFORMANCEINDEX:
      case CommonConstants.HIERARCHY_TAXONOMY:
      case CommonConstants.GOLDEN_RECORDS:
      case CommonConstants.ARTICLE_ENTITY:
      case CommonConstants.ASSET_ENTITY:
      case CommonConstants.TARGET:
      case CommonConstants.SUPPLIER_ENTITY:
      case CommonConstants.TEXT_ASSET_ENTITY:        
      //case CommonConstants.VIRTUAL_CATALOG_ENTITY: PXPFDEV-21454: Deprecate Virtual Catalog 
      case CommonConstants.ATTRIBUTE:
      case CommonConstants.ROLE:
      case CommonConstants.TAG:
      case CommonConstants.ATTRIBUTION_TAXONOMY:
      case CommonConstants.CONTEXT:
      case CommonConstants.ORGANIZATION:
        AbstractSaveTranslationsRequestModel dynamicModel = objectMapper.readValue(configModel.getData(),
            AbstractSaveTranslationsRequestModel.class);
        return savePropertiesTranslations.execute(dynamicModel);
      case CommonConstants.RELATIONSHIP:
        SaveRelationshipTranslationsRequestModel dynamicRelationshipModel = objectMapper.readValue(configModel.getData(),
            SaveRelationshipTranslationsRequestModel.class);
        return saveRelationshipTranslations.execute(dynamicRelationshipModel);
      default:
        break;
    }
    return null;
  }
}
