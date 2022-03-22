package com.cs.core.config.grideditpropertylist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertiesResponseModel;
import com.cs.core.config.strategy.usecase.grideditpropertylist.IGetAllGridEditablePropertiesStrategy;

@Service
public class GetAllGridEditablePropertiesService
    extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetGridEditPropertiesResponseModel>
    implements IGetAllGridEditablePropertiesService {
  
  @Autowired
  protected IGetAllGridEditablePropertiesStrategy getAllGridEditablePropertiesStrategy;
  
  @Override
  public IGetGridEditPropertiesResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllGridEditablePropertiesStrategy.execute(dataModel);
  }
}
