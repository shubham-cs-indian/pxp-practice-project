package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveReadPermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.klassinstance.AbstractNewGetVariantInstancesInTableViewService;
import org.springframework.stereotype.Service;

@Service
public class GetMarketVariantInstancesInTableViewService extends AbstractNewGetVariantInstancesInTableViewService<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel>
    implements IGetMarketVariantInstancesInTableViewService {
  
  @Override
  protected IGetVariantInstancesInTableViewModel executeInternal(
      IGetVariantInstanceInTableViewRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    try {
      return super.executeInternal(getKlassInstanceTreeStrategyModel);
    }
    catch (UserNotHaveReadPermission e) {
      throw new UserNotHaveReadPermissionForArticle();
    }
    catch (KlassNotFoundException e) {
      throw new MarketKlassNotFoundException();
    }
    
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.MARKET_INSTANCE_BASE_TYPE;
  }
  
  @Override
  protected String getModuleId()
  {
    return Constants.TARGET_MODULE;
  }
}
