package com.cs.core.config.business.tagtype;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkCreateTagValuesResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

public interface IBulkCreateTagValuesService extends ICreateConfigService<IListModel<ITagModel>, IBulkCreateTagValuesResponseModel> {
  
}
