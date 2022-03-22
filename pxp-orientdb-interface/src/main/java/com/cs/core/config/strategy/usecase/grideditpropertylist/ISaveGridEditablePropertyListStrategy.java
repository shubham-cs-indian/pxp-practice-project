package com.cs.core.config.strategy.usecase.grideditpropertylist;

import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;
import com.cs.core.config.interactor.model.grideditpropertylist.ISaveGridEditablePropertyListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveGridEditablePropertyListStrategy extends
    IConfigStrategy<ISaveGridEditablePropertyListModel, IGetGridEditPropertyListSuccessModel> {
  
}
