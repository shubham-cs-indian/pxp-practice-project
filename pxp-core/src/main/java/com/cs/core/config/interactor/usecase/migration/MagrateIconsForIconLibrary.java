package com.cs.core.config.interactor.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.asset.AssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.GetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.strategy.usecase.migration.IMigrateIconsForIconLibraryStrategy;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 *
 * Description:- Passing swift server details to strategy. 
 * @author rahul.sehrawat
 *
 */
@Service
public class MagrateIconsForIconLibrary implements IMigrateIconsForIconLibrary {
  
  @Autowired
  IMigrateIconsForIconLibraryStrategy migrateIconsForIconLibraryStrategy;
  
  @Override
  public IVoidModel execute(IVoidModel dataModel) throws Exception
  {
    IGetAssetDetailsRequestModel assetRequestModel = fillModelForSwift();
    migrateIconsForIconLibraryStrategy.execute(assetRequestModel);
    
    return null;
  }

  protected IGetAssetDetailsRequestModel fillModelForSwift() throws CSInitializationException, PluginException {
    IAssetServerDetailsModel assetServerDetails = new AssetServerDetailsModel();
    IGetAssetDetailsRequestModel swiftModel = new GetAssetDetailsRequestModel();
    IJSONContent authenticateSwiftServer = CSDAMServer.instance().getServerInformation();
    assetServerDetails.setStorageURL(authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
    assetServerDetails.setAuthToken(authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, ""));
    swiftModel.setAssetServerDetails(assetServerDetails);
    
    return swiftModel;
   }

  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
