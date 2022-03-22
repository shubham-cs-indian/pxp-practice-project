package com.cs.core.config.interactor.usecase.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.attribute.IGetAllAttributesService;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllAttributes
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<IAttributeModel>>
    implements IGetAllAttributes {
  
  @Autowired
  IGetAllAttributesService getAttributesService;
  
  @Override
  public IListModel<IAttributeModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAttributesService.execute(dataModel);
  }
}
