package com.cs.core.config.interactor.usecase.grideditpropertylist;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;
import com.cs.core.config.interactor.model.grideditpropertylist.ISaveGridEditablePropertyListModel;

public interface ISaveGridEditablePropertyList extends
    ISaveConfigInteractor<ISaveGridEditablePropertyListModel, IGetGridEditPropertyListSuccessModel> {
  
}
