package com.cs.core.config.interactor.usecase.tagtype;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;

public interface IGetAllTagTypes
    extends IGetConfigInteractor<ITagTypeModel, IListModel<ITagTypeModel>> {
  
}
