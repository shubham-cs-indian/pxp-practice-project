package com.cs.core.config.businessapi.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.usecase.tag.IGetAllMasterTagsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllMasterTagsService extends AbstractGetConfigService<IIdParameterModel, IListModel<ITagModel>>
    implements IGetAllMasterTagsService {
  
  @Autowired
  protected IGetAllMasterTagsStrategy getAllMasterTagsStrategy;
  
  @Override
  public IListModel<ITagModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllMasterTagsStrategy.execute(dataModel);
  }
}
