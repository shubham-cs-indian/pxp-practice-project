package com.cs.core.config.strategy.usecase.configdata;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.user.IGetRolesOrUsersDataByPartnerIdModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetRolesOrUsersDataByPartnerIdStrategy extends OrientDBBaseStrategy
    implements IGetRolesOrUsersDataByPartnerIdStrategy {
  
  @Override
  public IGetConfigDataResponseModel execute(IGetRolesOrUsersDataByPartnerIdModel model) throws Exception
  {
    return super.execute(GET_ROLES_OR_USERS_DATA_BY_PARTNER_ID, model, GetConfigDataResponseModel.class);
  }
  
}
