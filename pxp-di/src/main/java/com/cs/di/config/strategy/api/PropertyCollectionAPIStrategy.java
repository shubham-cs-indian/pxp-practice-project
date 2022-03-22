package com.cs.di.config.strategy.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.PropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.SavePropertyCollectionModel;
import com.cs.core.config.strategy.usecase.propertycollection.ICreatePropertyCollectionStrategy;
import com.cs.core.config.strategy.usecase.propertycollection.ISavePropertyCollectionStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PropertyCollectionAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  protected ICreatePropertyCollectionStrategy createPropertyCollectionStrategy;
  
  @Autowired
  protected ISavePropertyCollectionStrategy   savePropertyCollectionStrategy;
  
  private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    return null;
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    IPropertyCollectionModel createPropertyCollectionModel = objectMapper.readValue(configModel.getData(), PropertyCollectionModel.class);
    if (DiValidationUtil.isBlank(createPropertyCollectionModel.getCode())) {
      createPropertyCollectionModel.setCode(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.PROPERTYCOLLECTION.getPrefix()));
    }
    IGetPropertyCollectionModel responsemodel = createPropertyCollectionStrategy.execute(createPropertyCollectionModel);
    return responsemodel;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    ISavePropertyCollectionModel savePropertyCollectionModel = objectMapper.readValue(configModel.getData(), SavePropertyCollectionModel.class);
    savePropertyCollectionModel.setId(savePropertyCollectionModel.getCode());
    return savePropertyCollectionStrategy.execute(savePropertyCollectionModel);
  }
  
}
