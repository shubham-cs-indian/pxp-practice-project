package com.cs.core.runtime.interactor.usecase.marketinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.exception.marketinstance.UserNotHaveCreatePermissionForMarket;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.interactor.usecase.variant.AbstractCreateInstanceVariant;
import com.cs.core.runtime.interactor.usecase.variant.marketinstance.ICreateMarketInstanceVariant;

@Service
public class CreateMarketInstanceVariant
    extends AbstractCreateInstanceVariant<ICreateVariantModel, IGetKlassInstanceModel>
    implements ICreateMarketInstanceVariant {
  
  @Autowired
  protected Long marketKlassCounter;
  
  @Override
  public IGetKlassInstanceModel executeInternal(ICreateVariantModel model) throws Exception
  {
    try {
      return super.executeInternal(model);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForMarket();
    }
    catch (KlassNotFoundException e) { // TODO: handle this exception from
                                       // orient db plugin
      throw new MarketKlassNotFoundException();
    }
  }
  
  @Override
  protected Long getCounter()
  {
    return marketKlassCounter++;
  }
  
  @Override
  protected String getModuleEntityType()
  {
    return CommonConstants.MARKET_INSTANCE_MODULE_ENTITY;
  }
  
  @Override
  protected BaseType getBaseType()
  {
    return BaseType.TARGET;
  }
}
