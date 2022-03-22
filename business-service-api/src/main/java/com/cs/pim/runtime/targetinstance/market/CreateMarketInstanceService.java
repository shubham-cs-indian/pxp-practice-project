package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.abstrct.AbstractCreateInstanceService;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.exception.marketinstance.UserNotHaveCreatePermissionForMarket;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CreateMarketInstanceService
    extends AbstractCreateInstanceService<ICreateInstanceModel, IKlassInstanceInformationModel>
    implements ICreateMarketInstanceService {
  
  @Autowired
  protected Long marketKlassCounter;
  
  @Override
  protected IKlassInstanceInformationModel executeInternal(ICreateInstanceModel model)
      throws Exception
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
  
  @Override
  protected void fillEntityExtensionForCreation(IBaseEntityDTO baseEntityDTO) throws Exception
  {
    Map<String, Object> subType = new HashMap<>();
    subType.put("subType", Constants.MARKET_INSTANCE_BASE_TYPE);
    baseEntityDTO.setEntityExtension(ObjectMapperUtil.writeValueAsString(subType));
  }
}
