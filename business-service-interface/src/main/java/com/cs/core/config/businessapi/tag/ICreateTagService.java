package com.cs.core.config.businessapi.tag;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.tag.ICreateTagResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

public interface ICreateTagService extends ICreateConfigService<ITagModel, ICreateTagResponseModel> {
  
}
