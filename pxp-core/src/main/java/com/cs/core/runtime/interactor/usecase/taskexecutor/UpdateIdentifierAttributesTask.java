package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configuration.base.IUpdateDuplicateStatusResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.strategy.usecase.klassinstance.IUpdateKlassInstanceDuplicateStatusStrategy;

@Component
public class UpdateIdentifierAttributesTask extends
    AbstractRuntimeInteractor<IPropertyInstanceUniquenessEvaluationForPropagationModel, IIdAndTypeModel>
    implements IUpdateIdentifierAttributesTask {
  
  /*@Autowired
  protected IUpdateKlassInstanceDuplicateStatusStrategy          updateArticleInstanceDuplicateStatusStrategy;
  
  @Autowired
  protected IUpdateKlassInstanceDuplicateStatusStrategy          updateAssetInstanceDuplicateStatusStrategy;
  
  @Autowired
  protected IUpdateKlassInstanceDuplicateStatusStrategy          updateMarketInstanceDuplicateStatusStrategy;
  
  @Autowired
  protected IUpdateKlassInstanceDuplicateStatusStrategy          updatePromotionInstanceDuplicateStatusStrategy;
  
  @Autowired
  protected IUpdateKlassInstanceDuplicateStatusStrategy          updateSupplierInstanceDuplicateStatusStrategy;
  
  @Autowired
  protected IUpdateKlassInstanceDuplicateStatusStrategy          updateTextAssetInstanceDuplicateStatusStrategy;
  
  @Autowired
  protected IUpdateKlassInstanceDuplicateStatusStrategy          updateVirtualCatalogInstanceDuplicateStatusStrategy;*/
  
  @Autowired
  protected Boolean    isKafkaLoggingEnabled;

  @Override
  public IIdAndTypeModel executeInternal(
      IPropertyInstanceUniquenessEvaluationForPropagationModel dataModel) throws Exception
  {
    try {
      String baseType = dataModel.getBaseType();
      IUpdateKlassInstanceDuplicateStatusStrategy strategy = getStrategyToExecuteByBaseType(
          baseType);
      IUpdateDuplicateStatusResponseModel updateDuplicateStatusResponseModel = strategy
          .execute(dataModel);
      
      IUpdateSearchableInstanceModel updateSearchableInstanceModel = updateDuplicateStatusResponseModel
          .getUpdateSearchableDocumentData();
      
      updateIdentifierStatusForSearchableInstance(updateSearchableInstanceModel);
      
      return (IIdAndTypeModel) updateDuplicateStatusResponseModel;
    }
    catch (Throwable ex) {
      if (isKafkaLoggingEnabled) {
        //TODO: BGP
      }
    }
    return null;
  }
  
  private void updateIdentifierStatusForSearchableInstance(
      IUpdateSearchableInstanceModel updateSearchableInstanceModel) throws Exception
  {
    //TODO: BGP
  }
  
  private IUpdateKlassInstanceDuplicateStatusStrategy getStrategyToExecuteByBaseType(
      String baseType)
  {
    switch (baseType) {
      /*case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        return updateArticleInstanceDuplicateStatusStrategy;
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        return updateAssetInstanceDuplicateStatusStrategy;
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        return updateMarketInstanceDuplicateStatusStrategy;
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        return updateTextAssetInstanceDuplicateStatusStrategy;
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        return updateSupplierInstanceDuplicateStatusStrategy;
      case Constants.VIRTUAL_CATALOG_INSTANCE_BASE_TYPE:
        return updateVirtualCatalogInstanceDuplicateStatusStrategy;*/
      default:
        return null;
    }
  }
}
