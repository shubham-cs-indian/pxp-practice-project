package com.cs.core.config.interactor.usecase.attribute;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeResponseModel;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;

public interface ISaveAttribute
    extends ISaveConfigInteractor<IListModel<ISaveAttributeModel>, IBulkSaveAttributeResponseModel> {
  
}
