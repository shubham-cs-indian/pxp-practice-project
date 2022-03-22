package com.cs.core.config.interactor.usecase.grideditpropertylist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.grideditpropertylist.IGetAllGridEditablePropertiesService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertiesResponseModel;

@Service
public class GetAllGridEditableProperties extends
    AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetGridEditPropertiesResponseModel> implements IGetAllGridEditableProperties {
  
  @Autowired
  protected IGetAllGridEditablePropertiesService getAllGridEditablePropertiesService;
  
  @Override
  public IGetGridEditPropertiesResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllGridEditablePropertiesService.execute(dataModel);
  }
}
