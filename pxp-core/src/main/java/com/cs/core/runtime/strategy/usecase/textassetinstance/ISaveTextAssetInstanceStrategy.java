package com.cs.core.runtime.strategy.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.instance.ISaveInstanceStrategyListModel;
import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface ISaveTextAssetInstanceStrategy
    extends IRuntimeStrategy<ISaveInstanceStrategyListModel, ISaveStrategyInstanceResponseModel> {
  
}
