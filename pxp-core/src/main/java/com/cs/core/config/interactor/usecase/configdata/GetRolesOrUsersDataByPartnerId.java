package com.cs.core.config.interactor.usecase.configdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.user.IGetRolesOrUsersDataByPartnerIdModel;
import com.cs.core.config.strategy.usecase.configdata.IGetRolesOrUsersDataByPartnerIdStrategy;

@Service
public class GetRolesOrUsersDataByPartnerId extends
    AbstractGetConfigInteractor<IGetRolesOrUsersDataByPartnerIdModel, IGetConfigDataResponseModel>
    implements IGetRolesOrUsersDataByPartnerId {
  
  @Autowired
  protected IGetRolesOrUsersDataByPartnerIdStrategy getRolesOrUsersDataByPartnerIdStrategy;
  
  @Override
  public IGetConfigDataResponseModel executeInternal(IGetRolesOrUsersDataByPartnerIdModel dataModel)
      throws Exception
  {
    return getRolesOrUsersDataByPartnerIdStrategy.execute(dataModel);
  }
  
}
