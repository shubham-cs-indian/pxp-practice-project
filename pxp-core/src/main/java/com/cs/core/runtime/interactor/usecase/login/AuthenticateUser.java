package com.cs.core.runtime.interactor.usecase.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.swift.IAuthenticateAssetServerStrategy;
import com.cs.core.config.strategy.usecase.user.IAuthenticateUserStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;


// TODO: move to base package
@Service
public class AuthenticateUser extends AbstractGetConfigInteractor<IUserModel, IUserModel>
    implements IAuthenticateUser {
  
  @Autowired
  protected IAuthenticateUserStrategy        authenticateUserStrategy;
  
  @Autowired
  protected IAuthenticateAssetServerStrategy authenticateAssetServerStrategy;
  
  @Autowired
  protected IAssetServerDetailsModel         assetServerDetails;
  
  @Override
  public IUserModel executeInternal(IUserModel model) throws Exception
  {
    // Authenticate Asset Server
    try {
      IAssetServerDetailsModel assetServerDetailsFromStrategy = authenticateAssetServerStrategy
          .execute(null);
/*
      assetServerDetails.setStorageURL(assetServerDetailsFromStrategy.getStorageURL());
      assetServerDetails.setAuthToken(assetServerDetailsFromStrategy.getAuthToken());
*/
    }
    catch (Exception e) {
      RDBMSLogger.instance().info(
          "++++++++++++++++++++++++++++++++SWIFT NOT CONNECTED++++++++++++++++++++++++++++++++++++++++++");
    }
    // Authenticate User
    return authenticateUserStrategy.execute(model);
  }
}
