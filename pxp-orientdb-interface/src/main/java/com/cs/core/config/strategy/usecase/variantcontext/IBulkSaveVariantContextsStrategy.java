package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.IBulkSaveVariantContextsResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGridEditVariantContextInformationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkSaveVariantContextsStrategy extends
    IConfigStrategy<IListModel<IGridEditVariantContextInformationModel>, IBulkSaveVariantContextsResponseModel> {
  
}
