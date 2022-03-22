package com.cs.core.config.interactor.usecase.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.businessapi.attribute.ICreateAttributeService;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.ICreateAttributeResponseModel;

@Service
public class CreateAttribute
    extends AbstractCreateConfigInteractor<IAttributeModel, ICreateAttributeResponseModel>
    implements ICreateAttribute {
  
  @Autowired
  ICreateAttributeService createAttributeService;
  
  @Override
  public ICreateAttributeResponseModel executeInternal(IAttributeModel attributeModel) throws Exception
  {
    return createAttributeService.execute(attributeModel);
  }
}
