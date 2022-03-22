package com.cs.core.config.interactor.usecase.variantcontext;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.variantcontext.IBulkSaveVariantContextsResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGridEditVariantContextInformationModel;

public interface IBulkSaveVariantContexts extends
    IConfigInteractor<IListModel<IGridEditVariantContextInformationModel>, IBulkSaveVariantContextsResponseModel> {
  
}
