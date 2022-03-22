package com.cs.core.config.interactor.usecase.tab;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;

public interface ISaveTab extends ISaveConfigInteractor<ISaveTabModel, IGetTabModel> {
  
}
