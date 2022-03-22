package com.cs.core.config.businessapi.attribute;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.ICreateAttributeResponseModel;
import com.cs.core.config.strategy.usecase.attribute.ICreateAttributeStrategy;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class CreateAttributeService extends AbstractCreateConfigService<IAttributeModel, ICreateAttributeResponseModel>
    implements ICreateAttributeService {
  
  @Autowired
  ICreateAttributeStrategy createAttributeStrategy;
  
  @Autowired
  protected AttributeValidations attributeValidations;
  
  @Override
  public ICreateAttributeResponseModel executeInternal(IAttributeModel attributeModel) throws Exception
  {
    String code = attributeModel.getCode();
    if (StringUtils.isEmpty(code)) {
      code = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix());
    }
    
    attributeModel.setCode(code);
    attributeModel.setId(code);
    attributeValidations.validateAttributeCreation(attributeModel);
    IPropertyDTO createProperty = RDBMSUtils.createProperty(attributeModel.getId(), attributeModel.getCode(), attributeModel.getType());
    
    attributeModel.setPropertyIID(createProperty.getPropertyIID());
    return createAttributeStrategy.execute(attributeModel);
  }
}
