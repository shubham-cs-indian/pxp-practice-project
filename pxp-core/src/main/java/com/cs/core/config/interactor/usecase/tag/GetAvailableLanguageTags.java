package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.businessapi.tag.IGetAvailableLanguageTagsService;
import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAvailableLanguageTags extends AbstractGetConfigService<IIdParameterModel, IListModel<IGetEntityModel>> implements IGetAvailableLanguageTags {
  
  @Autowired
  protected IGetAvailableLanguageTagsService getAvailableLanguageTagsService;
  
  @Override
  public IListModel<IGetEntityModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAvailableLanguageTagsService.execute(dataModel);
  }
}
