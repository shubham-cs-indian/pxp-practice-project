package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreateVariantContextsStrategy extends
    IConfigStrategy<IListModel<ISaveVariantContextModel>, IListModel<ISaveVariantContextModel>> {
  
}
