package com.cs.core.config.businessapi.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.tag.IGetAvailableLanguageTagsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAvailableLanguageTagsService extends AbstractGetAvailableTaxonomyTags<IIdParameterModel, IListModel<IGetEntityModel>>
    implements IGetAvailableLanguageTagsService {
  
  @Autowired
  protected IGetAvailableLanguageTagsStrategy getAvailableLanguageTagsStrategy;
  
  @Override
  public IListModel<IGetEntityModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IListModel<IGetEntityModel> executeGetAvailableTaxonomyTabs(IIdParameterModel model) throws Exception
  {
    return getAvailableLanguageTagsStrategy.execute(model);
  }
}
