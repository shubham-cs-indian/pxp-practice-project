package com.cs.di.config.strategy.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.relationship.CreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;
import com.cs.core.config.interactor.model.relationship.SaveRelationshipModel;
import com.cs.core.config.interactor.usecase.relationship.ICreateRelationship;
import com.cs.core.config.interactor.usecase.relationship.IGetRelationship;
import com.cs.core.config.interactor.usecase.relationship.ISaveRelationship;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
@Service
public class RelationshipAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  protected ICreateRelationship createRelationshipInteractor;
  
  @Autowired
  ISaveRelationship             saveRelationshipInteractor;
  
  @Autowired
  protected IGetRelationship    getRelationshipInteractor;
  
  private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    IIdParameterModel getEntityModel = new IdParameterModel(code);
    return getRelationshipInteractor.execute(getEntityModel);
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    ICreateRelationshipModel createRelationshipModel = objectMapper.readValue(configModel.getData(), CreateRelationshipModel.class);
    if (DiValidationUtil.isBlank(createRelationshipModel.getCode())) {
      createRelationshipModel.setCode(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix()));
    }
    return createRelationshipInteractor.execute(createRelationshipModel);
  }
  
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    ISaveRelationshipModel saveRelationshipModel = objectMapper.readValue(configModel.getData(),SaveRelationshipModel.class);
    saveRelationshipModel.setId(saveRelationshipModel.getCode());
    return saveRelationshipInteractor.execute(saveRelationshipModel);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
