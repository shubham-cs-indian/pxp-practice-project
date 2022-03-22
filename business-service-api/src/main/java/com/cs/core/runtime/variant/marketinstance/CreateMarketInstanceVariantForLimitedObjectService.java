package com.cs.core.runtime.variant.marketinstance;

import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.exception.marketinstance.UserNotHaveCreatePermissionForMarket;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.variant.abstrct.AbstractCreateInstanceVariantForLimitedObjectService;
import com.cs.pim.runtime.targetinstance.market.IGetMarketVariantInstancesInTableViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateMarketInstanceVariantForLimitedObjectService extends AbstractCreateInstanceVariantForLimitedObjectService<ICreateVariantForLimitedObjectRequestModel, IGetVariantInstancesInTableViewModel>
    implements ICreateMarketInstanceVariantForLimitedObjectService {
  
  @Autowired
  protected Long marketKlassCounter;

  @Autowired
  protected IGetMarketVariantInstancesInTableViewService getMarketVariantInstancesInTableViewService;
  
  @Override
  public IGetVariantInstancesInTableViewModel execute(
      ICreateVariantForLimitedObjectRequestModel dataModel) throws Exception
  {
    try {
      return super.executeInternal(dataModel);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForMarket();
    }
    catch (KlassNotFoundException e) {
      throw new MarketKlassNotFoundException();
    }
  }
  
  @Override
  protected Long getCounter()
  {
    return marketKlassCounter++;
  }
  
  @Override
  protected BaseType getBaseType()
  {
    return BaseType.TARGET;
  }
  
  @Override
  protected String getModuleEntityType()
  {
    return Constants.MARKET_INSTANCE_MODULE_ENTITY;
  }
  
  @Override
  protected String getStringBaseType()
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
