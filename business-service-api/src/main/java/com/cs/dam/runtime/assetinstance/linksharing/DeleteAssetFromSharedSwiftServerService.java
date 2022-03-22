package com.cs.dam.runtime.assetinstance.linksharing;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cs.constants.DAMConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.exception.assetserver.AssetObjectNotFoundException;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.services.CSDAMServer;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 *
 * @author mrunali.dhenge
 *
 *         Delete Asset from shared container of swift server
 *        
 */
@Service
public class DeleteAssetFromSharedSwiftServerService extends AbstractRuntimeService<IIdParameterModel, IIdParameterModel>
    implements IDeleteAssetFromSharedSwiftServerService {
  
  @SuppressWarnings("unchecked")
  @Override
  protected IIdParameterModel executeInternal(IIdParameterModel model) throws Exception
  {
    Map<String, Object> assetDeleteRequestDetails = new HashMap<String, Object>();
    Map<String, String> assetServerDetails = new HashMap<String, String>();
    Map<String, Object> assetDataMap = new HashMap<>();
    
    fillAssetServerDetails(assetServerDetails);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_AUTH_TOKEN, assetServerDetails.get("authToken"));
    fillAssetRequestMapDetails(model, assetDeleteRequestDetails, assetServerDetails, assetDataMap);
    
    int response = CSDAMServer.instance().deleteAssetFromSwiftServer(assetDeleteRequestDetails);
    if (response == 404) {
      throw new AssetObjectNotFoundException();
    }
    if (response == 401) {
      CSDAMServer.instance().connect();
      Map<String, Object> serverDetails = (Map<String, Object>) assetDeleteRequestDetails.get("assetServerDetails");
      serverDetails.put("storageUrl",
          CSDAMServer.instance().getServerInformation().getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
      serverDetails.put("authToken",
          CSDAMServer.instance().getServerInformation().getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
      CSDAMServer.instance().deleteAssetFromSwiftServer(assetDeleteRequestDetails);
    }
    return null;
  }
  
  /**
   *
   * @param model
   * @param assetDeleteRequestDetails
   * @param assetServerDetails
   * @param assetDataMap
   * @throws CSInitializationException
   *
   *         Fill request map to send delete request
   */
  private void fillAssetRequestMapDetails(IIdParameterModel model, Map<String, Object> assetDeleteRequestDetails,
      Map<String, String> assetServerDetails, Map<String, Object> assetDataMap) throws CSInitializationException
  {
    assetDeleteRequestDetails.put(IGetAssetDetailsRequestModel.ASSET_KEY, model.getId());
    if (StringUtils.isEmpty(model.getType())) {
      assetDeleteRequestDetails.put(IGetAssetDetailsRequestModel.CONTAINER, CSProperties.instance()
          .getString("linksharing.asset.sharecontainer"));
    }
    else {
      assetDeleteRequestDetails.put(IGetAssetDetailsRequestModel.CONTAINER, model.getType());
    }   
    assetDeleteRequestDetails.put(IGetAssetDetailsRequestModel.ASSET_SERVER_DETAILS, assetServerDetails);
    assetDeleteRequestDetails.put(IGetAssetDetailsRequestModel.REQUEST_HEADERS, assetDataMap);
  }
  
  /**
   *
   * @param assetServerDetails
   * @throws CSInitializationException fill asset details for authentication
   * @throws PluginException
   */
  private void fillAssetServerDetails(Map<String, String> assetServerDetails) throws CSInitializationException, PluginException
  {
    IJSONContent authenticateSwiftServer = CSDAMServer.instance().getServerInformation();
    assetServerDetails.put("storageUrl", authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
    assetServerDetails.put("authToken", authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, ""));
  }
  
}
