package com.cs.core.config.strategy.usecase.variant;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantWithNumberModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllVariantsStrategy
    extends IConfigStrategy<IIdParameterModel, IListModel<IVariantWithNumberModel>> {
  
}
