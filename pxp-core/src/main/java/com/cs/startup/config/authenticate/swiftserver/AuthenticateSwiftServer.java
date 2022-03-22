package com.cs.startup.config.authenticate.swiftserver;

import javax.annotation.PostConstruct;

import com.cs.core.rdbms.driver.RDBMSLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.strategy.usecase.swift.IAuthenticateAssetServerStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Component
public class AuthenticateSwiftServer extends AbstractGetConfigInteractor<IModel, IModel>
    implements IAuthenticateSwiftServer {
  
  @Autowired
  protected IAuthenticateAssetServerStrategy authenticateAssetServerStrategy;
  
  @Autowired
  protected IAssetServerDetailsModel         assetServerDetails;
  
  @Override
  public IModel executeInternal(IModel dataModel) throws Exception
  {
    return null;
  }
  
  @PostConstruct
  public IModel execute() throws Exception
  {
    try {
      IAssetServerDetailsModel assetServerDetailsFromStrategy = authenticateAssetServerStrategy.execute(null);
      assetServerDetails.setAuthToken(assetServerDetailsFromStrategy.getAuthToken());
      assetServerDetails.setStorageURL(assetServerDetailsFromStrategy.getStorageURL());
    }
    catch (Exception e) {
      RDBMSLogger.instance().info("++++++++++++++++++++++++++++++++SWIFT NOT CONNECTED++++++++++++++++++++++++++++++++++++++++++");
    }
    return null;
  }
}