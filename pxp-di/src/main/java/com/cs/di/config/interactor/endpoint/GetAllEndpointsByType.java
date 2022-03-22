package com.cs.di.config.interactor.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IGetAllEndpointsByTypeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.di.config.businessapi.endpoint.IGetAllEndpointsByTypeService;

@Service
public class GetAllEndpointsByType extends
    AbstractGetConfigInteractor<IGetAllEndpointsByTypeRequestModel, IListModel<IIdLabelCodeModel>>
    implements IGetAllEndpointsByType {

  @Autowired
  protected IGetAllEndpointsByTypeService getAllEndpointsByTypeService;

  @Override
  public IListModel<IIdLabelCodeModel> executeInternal(IGetAllEndpointsByTypeRequestModel dataModel)
      throws Exception
  {
    return getAllEndpointsByTypeService.execute(dataModel);
  }

}
