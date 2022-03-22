package com.cs.core.config.strategy.usecase.grideditpropertylist;

import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllGridEditablePropertyListWithAvaliableSequenceStrategy extends
    IConfigStrategy<IGetGridEditPropertyListRequestModel, IGetGridEditPropertyListSuccessModel> {
  
}
