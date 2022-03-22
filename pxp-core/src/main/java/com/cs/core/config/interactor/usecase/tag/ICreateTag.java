package com.cs.core.config.interactor.usecase.tag;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.tag.ICreateTagResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

public interface ICreateTag extends ICreateConfigInteractor<ITagModel, ICreateTagResponseModel> {
  
}
