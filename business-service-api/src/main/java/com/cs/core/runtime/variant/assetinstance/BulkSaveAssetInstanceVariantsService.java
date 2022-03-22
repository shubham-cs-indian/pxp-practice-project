package com.cs.core.runtime.variant.assetinstance;

import com.cs.constants.Constants;
import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.variant.AbstractBulkSaveInstanceVariants;
import com.cs.dam.runtime.assetinstance.IGetAssetVariantInstancesInTableViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkSaveAssetInstanceVariantsService
    extends AbstractBulkSaveInstanceVariants<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel>
    implements IBulkSaveAssetInstanceVariantsService {

  @Autowired
  protected IGetAssetVariantInstancesInTableViewService getAssetVariantInstancesInTableViewService;

  @Override
  public IBulkSaveKlassInstanceVariantsResponseModel execute(IBulkSaveInstanceVariantsModel dataModel) throws Exception
  {
    IBulkSaveKlassInstanceVariantsResponseModel response = null;
    try {
      response = super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new AssetKlassNotFoundException(e);
    }
    return response;
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.ASSET_INSTANCE_BASE_TYPE;
  }

  @Override
  protected IGetVariantInstancesInTableViewModel executeGetTableView(IGetVariantInstanceInTableViewRequestModel tableViewRequestModel)
      throws Exception
  {
    return getAssetVariantInstancesInTableViewService.execute(tableViewRequestModel);
  }
  
}
