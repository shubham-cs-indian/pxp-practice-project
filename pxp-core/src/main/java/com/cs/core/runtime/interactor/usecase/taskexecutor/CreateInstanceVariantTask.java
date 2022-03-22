package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantRequestNewStrategyModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;

@Component
public class CreateInstanceVariantTask extends
    AbstractRuntimeInteractor<ICreateVariantRequestNewStrategyModel, ISaveStrategyInstanceResponseModel>
    implements ICreateInstanceVariantTask {
  
  /*@Autowired
  protected ICreateInstanceVariantStrategy createAssetInstanceVariantStrategy;
  
  @Autowired
  protected ICreateInstanceVariantStrategy createArticleInstanceVariantStrategy;
  
  @Autowired
  protected ICreateInstanceVariantStrategy createMarketInstanceVariantStrategy;
  
  @Autowired
  protected ICreateInstanceVariantStrategy createTextAssetInstanceVariantStrategy;
  
  @Autowired
  protected ICreateInstanceVariantStrategy createSupplierInstanceVariantStrategy;
  
  @Autowired
  protected ICreateInstanceVariantStrategy createPromotionInstanceVariantStrategy;
  
  @Autowired
  protected ICreateInstanceVariantStrategy createVirtualCatalogInstanceVariantStrategy;*/
  
  @Autowired
  protected KlassInstanceUtils klassInstanceUtils;
  
  @Override
  public ISaveStrategyInstanceResponseModel executeInternal(
      ICreateVariantRequestNewStrategyModel dataModel) throws Exception
  {
    /*
      ISaveStrategyInstanceResponseModel createVariantResponseModel = getStrategyToExecute(dataModel.getBaseType()).execute(dataModel);
      IUpdateSearchableInstanceModel updateSearchableInstanceModel = createVariantResponseModel.getUpdateSearchableDocumentData();
      klassInstanceUtils.updateAssociatedSearchableInstances(updateSearchableInstanceModel);
      return createVariantResponseModel;
    */
    return null;
  }
  
  /*private ICreateInstanceVariantStrategy getStrategyToExecute(String baseType)
  {
    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        return createArticleInstanceVariantStrategy;
  
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        return createAssetInstanceVariantStrategy;
  
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        return createMarketInstanceVariantStrategy;
  
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        return createTextAssetInstanceVariantStrategy;
  
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        return createSupplierInstanceVariantStrategy;
  
      case Constants.VIRTUAL_CATALOG_INSTANCE_BASE_TYPE:
        return createVirtualCatalogInstanceVariantStrategy;
    }
    return null;
  }*/
  
}
