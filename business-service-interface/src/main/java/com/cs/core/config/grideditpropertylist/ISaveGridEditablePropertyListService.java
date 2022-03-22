package com.cs.core.config.grideditpropertylist;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;
import com.cs.core.config.interactor.model.grideditpropertylist.ISaveGridEditablePropertyListModel;

public interface ISaveGridEditablePropertyListService
    extends ISaveConfigService<ISaveGridEditablePropertyListModel, IGetGridEditPropertyListSuccessModel> {
  
}
