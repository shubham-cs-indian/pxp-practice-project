package com.cs.core.runtime.variant.marketinstance;

import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.variant.AbstractBulkSaveInstanceVariants;
import com.cs.pim.runtime.targetinstance.market.IGetMarketVariantInstancesInTableViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkSaveMarketInstanceVariantsService
    extends AbstractBulkSaveInstanceVariants<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel>
    implements IBulkSaveMarketInstanceVariantsService {

  @Autowired
  protected IGetMarketVariantInstancesInTableViewService getMarketVariantInstancesInTableViewService;

  @Override
  public IBulkSaveKlassInstanceVariantsResponseModel execute(IBulkSaveInstanceVariantsModel dataModel) throws Exception
  {
    IBulkSaveKlassInstanceVariantsResponseModel response = null;
    try {
      response = super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new MarketKlassNotFoundException(e);
    }
    return response;
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.MARKET_INSTANCE_BASE_TYPE;
  }

  @Override
  protected IGetVariantInstancesInTableViewModel executeGetTableView(IGetVariantInstanceInTableViewRequestModel tableViewRequestModel)
      throws Exception
  {
    return getMarketVariantInstancesInTableViewService.execute(tableViewRequestModel);
  }
  
}
