package com.cs.core.config.interactor.usecase.tag;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

public interface IGetAllTags extends IGetConfigInteractor<ITagModel, IListModel<ITagModel>> {
  
}
