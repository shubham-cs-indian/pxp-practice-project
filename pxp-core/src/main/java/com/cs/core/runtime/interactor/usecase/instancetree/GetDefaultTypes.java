package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.instancetree.IGetDefaultTypesService;
import com.cs.core.runtime.interactor.model.instancetree.IDefaultTypesRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetDefaultTypesResponseModel;

@Service
public class GetDefaultTypes extends
    AbstractGetConfigInteractor<IDefaultTypesRequestModel, IListModel<IGetDefaultTypesResponseModel>>
    implements IGetDefaultTypes {
  
  @Autowired
  protected IGetDefaultTypesService getDefaultTypesGetService;
  
  @Override
  public IListModel<IGetDefaultTypesResponseModel> executeInternal(IDefaultTypesRequestModel model)
      throws Exception
  {
    return getDefaultTypesGetService.execute(model);
  }
}
