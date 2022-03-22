package com.cs.core.config.smartdocument.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.DAMConstants;
import com.cs.core.config.interactor.model.UploadZipForSmartDocumentTemplateResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;
import com.cs.core.config.interactor.model.asset.IUploadZipForSmartDocumentTemplateResponseModel;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * This is service class uploading zip for Smart Document Template.
 * 
 * @author vannya.kalani
 *
 */
@Service
public class UploadZipForSmartDocumentTemplateService
    extends AbstractCreateConfigService<IAssetUploadDataModel, IUploadZipForSmartDocumentTemplateResponseModel>
    implements IUploadZipForSmartDocumentTemplateService {
  
  @Override
  protected IUploadZipForSmartDocumentTemplateResponseModel executeInternal(IAssetUploadDataModel dataModel) throws Exception
  {
    IJSONContent authenticateSwiftServer = CSDAMServer.instance().getServerInformation();
    
    // If assetKey is empty then generate new with prefix SDT.
    String assetKey = dataModel.getAssetKey();
    if (StringUtils.isEmpty(assetKey)) {
      assetKey = RDBMSAppDriverManager.getDriver().newUniqueID(IStandardConfig.UniquePrefix.SMART_DOCUMENT_TEMPLATE.getPrefix());
    }
    
    // Prepare request model to upload zip to swift server.
    Map<String, Object> smartDocumentUploadDataModel = new HashMap<String, Object>();
    smartDocumentUploadDataModel.put("storageUrl", authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
    smartDocumentUploadDataModel.put("container", DAMConstants.SWIFT_CONTAINER_SD_TEMPLATES);
    smartDocumentUploadDataModel.put("assetBytes", dataModel.getAssetBytes());
    smartDocumentUploadDataModel.put("assetObjectKey", assetKey);
    
    Map<String, String> docDataMap = new HashMap<>();
    docDataMap.put(DAMConstants.REQUEST_HEADER_AUTH_TOKEN,
        authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, ""));
    String originalContentType = DAMConstants.CONTENT_TYPE_APPLICATION + "zip";
    docDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_CONTENT_TYPE, originalContentType);
    docDataMap.put(DAMConstants.REQUEST_HEADER_CONTENT_TYPE, originalContentType);
    docDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_TYPE, originalContentType);
    docDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_THUMB, assetKey);
    docDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_PREVIEW_ID, assetKey);
    docDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_NAME, assetKey + "." + "zip");
    docDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_FORMAT, "zip");
    smartDocumentUploadDataModel.put("assetData", docDataMap);
    
    // Call upload to swift server strategy.
    CSDAMServer.instance().uploadAsset(smartDocumentUploadDataModel);
    
    IUploadZipForSmartDocumentTemplateResponseModel response = new UploadZipForSmartDocumentTemplateResponseModel();
    response.setId(assetKey);
    
    return response;
  }
  
}
