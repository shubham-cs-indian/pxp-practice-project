package com.cs.core.config.auditlog;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.constants.DAMConstants;
import com.cs.core.config.auditlog.IDeleteAuditLogExportService;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idao.IAuditLogExportDAO;
import com.cs.core.runtime.interactor.exception.assetserver.AssetObjectDeleteException;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeleteAuditLogExportService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteAuditLogExportService {
  
  public static final String AUTH_TOKEN  = "authToken";
  public static final String STORAGE_URL = "storageUrl";
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    List<String> idsToRemove = model.getIds();
    List<String> successIds = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    IAuditLogExportDAO auditLogExportDAO = RDBMSUtils.newUserSessionDAO().newAuditLogExportDAO();
    
    List<String> failedAssetIds = auditLogExportDAO.getFailedAuditLogExportAssetIds(quoteIt(idsToRemove));
    idsToRemove.removeAll(failedAssetIds);
    
    Map<String, Object> requestMap = prepareMapForDeletingAssets();
    idsToRemove.forEach(id -> deleteAsset(requestMap, id, successIds, failure));
    deleteFromAuditLogExport(successIds, auditLogExportDAO, failedAssetIds);
    
    return getResponseModel(successIds, failure);
  }

  private String quoteIt(List<String> idsToRemove)
  {
    return idsToRemove.isEmpty() ? null : idsToRemove.stream().collect(Collectors.joining("','", "('", "')"));
  }

  private Map<String, Object> prepareMapForDeletingAssets() throws CSInitializationException, PluginException
  {
    Map<String, Object> requestMap = new HashMap<>();
    Map<String, String> assetServerDetails = new HashMap<>();
    Map<String, Object> assetDataMap = new HashMap<>();
    fillAssetServerDetails(requestMap, assetServerDetails, CSDAMServer.instance().getServerInformation());
    assetDataMap.put(DAMConstants.REQUEST_HEADER_AUTH_TOKEN, assetServerDetails.get(AUTH_TOKEN));
    requestMap.put(IGetAssetDetailsRequestModel.REQUEST_HEADERS, assetDataMap);
    requestMap.put(IGetAssetDetailsRequestModel.CONTAINER, DAMConstants.SWIFT_CONTAINER_DOCUMENT);
    return requestMap;
  }

  private IBulkDeleteReturnModel getResponseModel(List<String> successIds, IExceptionModel failure)
  {
    IBulkDeleteReturnModel response = new BulkDeleteReturnModel();
    response.setSuccess(successIds);
    response.setFailure(failure);
    return response;
  }

  private void deleteFromAuditLogExport(List<String> successIds,
      IAuditLogExportDAO auditLogExportDAO, List<String> failedAssetIds) throws RDBMSException
  {
    if ((successIds != null && !successIds.isEmpty())
        || (failedAssetIds != null && !failedAssetIds.isEmpty())) {
      successIds.addAll(failedAssetIds);
      auditLogExportDAO.deleteMultipleAuditLogExportTrackers(quoteIt(successIds));
    }
  }

  private void deleteAsset(Map<String, Object> requestMap, String id, List<String> successIds,
      IExceptionModel failure)
  {
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_KEY, id);
    try {
      int responseCode = CSDAMServer.instance().deleteAssetFromSwiftServer(requestMap);
      
      if (responseCode == HttpStatus.SC_NO_CONTENT) {
        successIds.add(id);
      }
      else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
        reAuthenticateAndDelete(id, successIds, requestMap);
      }
      else {
        throw new AssetObjectDeleteException();
      }
    }
    catch (Exception exception) {
      successIds.add(id);
      ExceptionUtil.addFailureDetailsToFailureObject(failure, exception, null,
          "Asset Not Found for key:" + id);
    }
  }

  @SuppressWarnings("unchecked")
  private void reAuthenticateAndDelete(String id, List<String> successIds,
      Map<String, Object> requestMap) throws Exception
  
  {
    CSDAMServer CSDAMInstance = CSDAMServer.instance();
    CSDAMInstance.connect();
    Map<String, String> assetServerDetails = (Map<String, String>) requestMap.get(IGetAssetDetailsRequestModel.ASSET_SERVER_DETAILS);
    fillAssetServerDetails(requestMap, assetServerDetails, CSDAMInstance.getServerInformation());
    int responseCode = CSDAMInstance.deleteAssetFromSwiftServer(requestMap);
    if (responseCode == 204) {
      successIds.add(id);
    }
  }

  private void fillAssetServerDetails(Map<String, Object> requestMap,
      Map<String, String> assetServerDetails, IJSONContent serverInformation)
  {
    assetServerDetails.put("storageUrl", serverInformation.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
    assetServerDetails.put("authToken", serverInformation.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, ""));
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_SERVER_DETAILS, assetServerDetails);
  }
}
