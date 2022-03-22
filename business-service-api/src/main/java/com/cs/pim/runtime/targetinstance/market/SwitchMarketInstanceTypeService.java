package com.cs.pim.runtime.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.exception.marketinstance.UserNotHaveCreatePermissionForMarket;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConifgDetailsForTypeSwitchOfInstanceStrategy;
import com.cs.core.runtime.interactor.usecase.taxonomy.NewSwitchInstanceTypeService;

@Service
public class SwitchMarketInstanceTypeService
    extends NewSwitchInstanceTypeService<IKlassInstanceTypeSwitchModel, IGetKlassInstanceModel>
    implements ISwitchMarketInstanceTypeService {
  
  @Autowired
  protected IGetConifgDetailsForTypeSwitchOfInstanceStrategy getConifgDetailsForTypeSwitchOfInstanceStrategy;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IKlassInstanceTypeSwitchModel typeSwitchModel)
      throws Exception
  {
    try {
      return super.executeInternal(typeSwitchModel);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForMarket(e);
    }
    catch (KlassNotFoundException e) { // TODO: handle this exception from
                                       // orient db plugin
      throw new MarketKlassNotFoundException(e);
    }
  }
  
  @Override
  protected IGetConfigDetailsForCustomTabModel getMultiClassificationDetails(
      IConfigDetailsForSwitchTypeRequestModel requestModel) throws Exception
  {
    return getConifgDetailsForTypeSwitchOfInstanceStrategy.execute(requestModel);
  }
  
}
