package com.cs.core.config.interactor.usecase.tagtype;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkCreateTagValuesResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

public interface IBulkCreateTagValues
    extends ICreateConfigInteractor<IListModel<ITagModel>, IBulkCreateTagValuesResponseModel> {
  
}
