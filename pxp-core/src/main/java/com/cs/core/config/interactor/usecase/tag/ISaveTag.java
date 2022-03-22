package com.cs.core.config.interactor.usecase.tag;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkSaveTagResponseModel;
import com.cs.core.config.interactor.model.tag.ISaveTagModel;

public interface ISaveTag
    extends IGetConfigInteractor<IListModel<ISaveTagModel>, IBulkSaveTagResponseModel> {
  
}
