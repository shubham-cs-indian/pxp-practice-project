package com.cs.core.config.interactor.usecase.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.attribute.IGetAttributeService;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAttribute extends AbstractGetConfigInteractor<IIdParameterModel, IAttributeModel>
    implements IGetAttribute {
  
  @Autowired
  IGetAttributeService getAttributeService;
  
  @Override
  public IAttributeModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAttributeService.execute(dataModel);
  }
}
