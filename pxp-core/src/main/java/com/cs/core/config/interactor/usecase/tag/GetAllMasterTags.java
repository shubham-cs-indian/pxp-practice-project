package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.tag.IGetAllMasterTagsService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllMasterTags
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<ITagModel>>
    implements IGetAllMasterTags {
  
  @Autowired
  protected IGetAllMasterTagsService getAllMasterTagsService;
  
  @Override
  public IListModel<ITagModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllMasterTagsService.execute(dataModel);
  }
}
