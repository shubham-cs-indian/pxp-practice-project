package com.cs.pim.runtime.interactor.usecase.dynamiccollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.instancetree.IGetNewInstanceTreeService;
import com.cs.core.runtime.interactor.model.collections.IDynamicCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IGetDynamicCollectionModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.dynamiccollection.ISaveDynamicCollectionService;

@Service
public class SaveDynamicCollection extends AbstractRuntimeInteractor<IDynamicCollectionModel, IGetDynamicCollectionModel>
    implements ISaveDynamicCollection {
  
  @Autowired
  protected ISaveDynamicCollectionService saveDynamicCollectionService;
  
  @Autowired
  private IGetNewInstanceTreeService      getNewInstanceTreeService;
  
  @Override
  public IGetDynamicCollectionModel executeInternal(IDynamicCollectionModel model) throws Exception
  {
    IGetDynamicCollectionModel responseModel = saveDynamicCollectionService.execute(model);
    IGetInstanceTreeRequestModel getRequestModel = model.getGetRequestModel();
    if(getRequestModel != null ) {
      IGetNewInstanceTreeResponseModel getResponseModel = getNewInstanceTreeService.execute(getRequestModel);
      responseModel.setKlassInstances(getResponseModel.getChildren());
      responseModel.setTotalContents(getResponseModel.getTotalContents());
    }
    return responseModel;

  }
  
}
