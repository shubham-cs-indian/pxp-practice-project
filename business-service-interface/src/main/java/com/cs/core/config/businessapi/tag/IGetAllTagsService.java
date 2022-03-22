package com.cs.core.config.businessapi.tag;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

public interface IGetAllTagsService extends IGetConfigService<ITagModel, IListModel<ITagModel>> {
  
}
