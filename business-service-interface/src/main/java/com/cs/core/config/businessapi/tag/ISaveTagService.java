package com.cs.core.config.businessapi.tag;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkSaveTagResponseModel;
import com.cs.core.config.interactor.model.tag.ISaveTagModel;

public interface ISaveTagService extends ISaveConfigService<IListModel<ISaveTagModel>, IBulkSaveTagResponseModel> {
  
}
