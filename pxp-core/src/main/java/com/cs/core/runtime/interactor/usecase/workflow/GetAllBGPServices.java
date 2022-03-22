package com.cs.core.runtime.interactor.usecase.workflow;

import com.cs.core.runtime.bgp.IGetAllBGPServicesService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllBGPServices extends AbstractRuntimeInteractor<IModel, IIdsListParameterModel>
    implements IGetAllBGPServices {

  @Autowired
  protected IGetAllBGPServicesService getAllBGPServicesService;
  @Override
  public IIdsListParameterModel executeInternal(IModel dataModel) throws Exception

  {
    return getAllBGPServicesService.execute(dataModel);
  }
  
}
