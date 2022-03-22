package com.cs.core.config.businessapi.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.tag.IGetTagsListByTagTypeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTagsListByTagTypeService extends AbstractGetConfigService<IIdParameterModel, IListModel<IGetEntityModel>>
    implements IGetTagsListByTagTypeService {
  
  @Autowired
  IGetTagsListByTagTypeStrategy getTagsListByTagTypeStrategy;
  
  @Override
  public IListModel<IGetEntityModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getTagsListByTagTypeStrategy.execute(dataModel);
  }
}
