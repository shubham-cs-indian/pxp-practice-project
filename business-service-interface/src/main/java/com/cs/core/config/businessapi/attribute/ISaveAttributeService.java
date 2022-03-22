package com.cs.core.config.businessapi.attribute;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeResponseModel;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;

public interface ISaveAttributeService extends ISaveConfigService<IListModel<ISaveAttributeModel>, IBulkSaveAttributeResponseModel> {
  
}
