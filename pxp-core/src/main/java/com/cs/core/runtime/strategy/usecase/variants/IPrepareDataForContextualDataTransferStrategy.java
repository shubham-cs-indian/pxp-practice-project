package com.cs.core.runtime.strategy.usecase.variants;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.runtime.interactor.model.context.IGetContextualDataTransferModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IPrepareDataForContextualDataTransferStrategy extends
    IRuntimeStrategy<IGetContextualDataTransferModel, IListModel<IContentsPropertyDiffModel>> {
  
}
