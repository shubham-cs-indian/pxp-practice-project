package com.cs.core.config.interactor.usecase.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.attribute.IGetAttributeListService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IAttributeInfoModel;

@Service
public class GetAttributeList
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<IAttributeInfoModel>>
    implements IGetAttributeList {
  
  @Autowired
  IGetAttributeListService getAttributeListService;
  
  @Override
  public IListModel<IAttributeInfoModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAttributeListService.execute(dataModel);
  }
}
