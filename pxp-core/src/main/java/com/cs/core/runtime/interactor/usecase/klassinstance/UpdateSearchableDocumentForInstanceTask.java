package com.cs.core.runtime.interactor.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import org.springframework.stereotype.Component;

@Component
public class UpdateSearchableDocumentForInstanceTask
    extends AbstractRuntimeInteractor<IUpdateSearchableInstanceRequestModel, IModel>
    implements IUpdateSearchableDocumentForInstance {
  
  /*@Autowired
  protected IUpdateSearchableDocumentForInstanceStrategy updateArticleInstanceSearchableDocumentStrategy;
  
  @Autowired
  protected IUpdateSearchableDocumentForInstanceStrategy updateAssetInstanceSearchableDocumentStrategy;
  
  @Autowired
  protected IUpdateSearchableDocumentForInstanceStrategy updateMarketInstanceSearchableDocumentStrategy;
  
  @Autowired
  protected IUpdateSearchableDocumentForInstanceStrategy updateSupplierInstanceSearchableDocumentStrategy;
  
  @Autowired
  protected IUpdateSearchableDocumentForInstanceStrategy updateTextAssetInstanceSearchableDocumentStrategy;
  
  @Autowired
  protected IUpdateSearchableDocumentForInstanceStrategy updateVirtualCatalogInstanceSearchableDocumentStrategy;*/
  
  @Override
  protected IModel executeInternal(IUpdateSearchableInstanceRequestModel model) throws Exception
  {
    /*String baseType = model.getSearchablePropertyInstancesInformation().getBaseType();
    
    IUpdateSearchableDocumentForInstanceStrategy strategy = getUpdateSearchableStrategyByBaseType(
        baseType);
    strategy.execute(model);*/
    return null;
  }
  
  /*private IUpdateSearchableDocumentForInstanceStrategy getUpdateSearchableStrategyByBaseType(
      String baseType)
  {
    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        return updateArticleInstanceSearchableDocumentStrategy;
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        return updateAssetInstanceSearchableDocumentStrategy;
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        return updateMarketInstanceSearchableDocumentStrategy;
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        return updateTextAssetInstanceSearchableDocumentStrategy;
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        return updateSupplierInstanceSearchableDocumentStrategy;
      case Constants.VIRTUAL_CATALOG_INSTANCE_BASE_TYPE:
        return updateVirtualCatalogInstanceSearchableDocumentStrategy;
      default:
        return null;
    }
  }*/
  
}
