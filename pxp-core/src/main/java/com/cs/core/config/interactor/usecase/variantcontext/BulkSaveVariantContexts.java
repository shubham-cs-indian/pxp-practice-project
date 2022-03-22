package com.cs.core.config.interactor.usecase.variantcontext;

import coms.cs.core.config.businessapi.variantcontext.IBulkSaveVariantContextsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.IBulkSaveVariantContextsResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGridEditVariantContextInformationModel;
import com.cs.core.config.strategy.usecase.variantcontext.IBulkSaveVariantContextsStrategy;

@Service
public class BulkSaveVariantContexts extends
    AbstractSaveConfigInteractor<IListModel<IGridEditVariantContextInformationModel>, IBulkSaveVariantContextsResponseModel>
    implements IBulkSaveVariantContexts {
  
  @Autowired
  protected IBulkSaveVariantContextsService bulkSaveVariantContextsService;
  
  @Override
  public IBulkSaveVariantContextsResponseModel executeInternal(
      IListModel<IGridEditVariantContextInformationModel> dataModel) throws Exception
  {
    return bulkSaveVariantContextsService.execute(dataModel);
  }
}
