package com.cs.di.config.strategy.api;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.AbstractSaveAttributeModel;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.usecase.attribute.ICreateAttribute;
import com.cs.core.config.interactor.usecase.attribute.IGetAttribute;
import com.cs.core.config.interactor.usecase.attribute.ISaveAttribute;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AttributeAPIStrategy extends AbstractConfigurationAPIStrategy
    implements IConfigurationAPIStrategy {
  
  private static final ObjectMapper mapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Autowired
  private IGetAttribute    getAttribute;
  
  @Autowired
  private ICreateAttribute createAttribute;
  
  @Autowired
  private ISaveAttribute   saveAttribute;
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    IIdParameterModel getEntityModel = new IdParameterModel(code);
    return getAttribute.execute(getEntityModel);
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    AbstractAttributeModel readModel = mapper.readValue(configModel.getData(),
        AbstractAttributeModel.class);
    if (readModel.getIsStandard()) {
      throw new Exception("Can not create new Standard Attribute");
    }
    return createAttribute.execute(readModel);
  }
  
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData,
      IConfigAPIRequestModel configModel) throws Exception
  {
    AbstractSaveAttributeModel reqModel = mapper.readValue(configModel.getData(),
        AbstractSaveAttributeModel.class);
    reqModel.setPropertyIID((Long) getData.get(ISaveAttributeModel.PROPERTY_IID));
    reqModel.setIsTranslatable((Boolean) getData.get(ISaveAttributeModel.IS_TRANSLATABLE));
    IListModel<ISaveAttributeModel> attrubutelistSaveModel = new ListModel<>();
    attrubutelistSaveModel.setList(Arrays.asList(reqModel));
    return saveAttribute.execute(attrubutelistSaveModel);
  }
}
