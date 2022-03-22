package com.cs.core.config.interactor.usecase.grideditpropertylist;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;

public interface IGetAllGridEditablePropertyListWithAvaliableSequence extends
    IGetConfigInteractor<IGetGridEditPropertyListRequestModel, IGetGridEditPropertyListSuccessModel> {
  
}
