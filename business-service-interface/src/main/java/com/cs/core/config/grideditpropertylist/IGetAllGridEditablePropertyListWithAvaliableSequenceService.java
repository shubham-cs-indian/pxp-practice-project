package com.cs.core.config.grideditpropertylist;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;

public interface IGetAllGridEditablePropertyListWithAvaliableSequenceService
    extends IGetConfigService<IGetGridEditPropertyListRequestModel, IGetGridEditPropertyListSuccessModel> {
  
}
