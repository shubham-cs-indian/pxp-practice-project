package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.tag.IGetAvailableMasterTagsService;
import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAvailableMasterTags
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<IGetEntityModel>>
    implements IGetAvailableMasterTags {
  
  @Autowired
  protected IGetAvailableMasterTagsService getAvailableMasterTagsService;
  
  @Override
  public IListModel<IGetEntityModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAvailableMasterTagsService.execute(dataModel);
  }
}
