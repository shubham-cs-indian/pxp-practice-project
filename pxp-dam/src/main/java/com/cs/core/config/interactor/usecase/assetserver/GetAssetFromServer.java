package com.cs.core.config.interactor.usecase.assetserver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.AssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.GetAssetDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsStrategyModel;
import com.cs.core.config.interactor.usecase.asset.IGetAssetFromServer;
import com.cs.core.config.strategy.usecase.swift.IAuthenticateAssetServerStrategy;
import com.cs.core.config.strategy.usecase.swift.IGetAssetFromServerStrategy;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class GetAssetFromServer
    extends AbstractGetConfigInteractor<IGetAssetDetailsRequestModel, IGetAssetDetailsResponseModel>
    implements IGetAssetFromServer {
  
  @Autowired
  IAssetServerDetailsModel         assetServerDetails;
  
  @Autowired
  IGetAssetFromServerStrategy      getAssetFromServerStrategy;
  
  @Autowired
  IAuthenticateAssetServerStrategy authenticateAssetServerStrategy;
  
  /*@Autowired
  IGetFilenameByAssetKeyStrategy   getFilenameByAssetKey;*/
  
  @Override
  public IGetAssetDetailsResponseModel executeInternal(IGetAssetDetailsRequestModel dataModel)
      throws Exception
  {
    dataModel.setAssetServerDetails(assetServerDetails);
    IGetAssetDetailsStrategyModel assetContentsModel = getAssetFromServerStrategy
        .execute(dataModel);
    IGetAssetDetailsResponseModel fileModel = new GetAssetDetailsResponseModel();
    String download = dataModel.getDownload();
    Integer responseCode = assetContentsModel.getResponseCode();
    String assetKey = dataModel.getAssetKey();
    if (responseCode == 200 || responseCode == 206) {
      fillGetAssetDetailsResponseModel(assetContentsModel, fileModel, download, assetKey);
      
      return fileModel;
    }
    else if (responseCode == 401) {
      IAssetServerDetailsModel assetServerDetailsFromStrategy = authenticateAssetServerStrategy
          .execute(null);
      assetServerDetails = new AssetServerDetailsModel(assetServerDetailsFromStrategy);
      dataModel.setAssetServerDetails(assetServerDetails);
      assetContentsModel = getAssetFromServerStrategy.execute(dataModel);
      fillGetAssetDetailsResponseModel(assetContentsModel, fileModel, download, assetKey);
      
      return fileModel;
    }
    return null;
  }
  
  private void fillGetAssetDetailsResponseModel(IGetAssetDetailsStrategyModel assetContentsModel,
      IGetAssetDetailsResponseModel fileModel, String download, String assetObjectKey)
      throws Exception
  {
    Map<String, String> responseHeaders = assetContentsModel.getResponseHeaders();
    // FIXME: in case of config icons, download by random UUID as name
    // (temporary)
    Boolean shouldDownload = download != null && !download.equalsIgnoreCase("icon");
    String contentDisposition = null;
    
    if (shouldDownload) {
      contentDisposition = CommonConstants.CONTENT_ATTACHMENT + download + "\"";
    }
    else {
      String contentType = responseHeaders.get(CommonConstants.HEADER_CONTENT_TYPE);
      String fileName = responseHeaders.get(CommonConstants.HEADER_FILENAME);
      if(fileName!=null && !fileName.isEmpty()) {
        String file = fileName.substring(0, fileName.indexOf("."));
        contentDisposition = CommonConstants.CONTENT_INLINE + file;
      } else {
        String randomID = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix());
        contentDisposition = CommonConstants.CONTENT_INLINE + randomID;
      }
      String extension = "";
      
      if (contentType != null) {
        if (contentType.equalsIgnoreCase("application/octet-stream")) {
          extension = "pdf";
          responseHeaders.put(CommonConstants.HEADER_CONTENT_TYPE, "application/pdf");
        }
        else if(contentType.equalsIgnoreCase("application/.xlsx")){
          extension = "xlsx";
        }
        else {
          extension = contentType.split("/")[1];
        }
        contentDisposition = contentDisposition + "." + extension + "\"";
      }
    }
    
    fileModel.setContentDisposition(contentDisposition);
    fileModel.setInputStream(assetContentsModel.getInputStream());
    fileModel.setResponseHeaders(responseHeaders);
  }
}
