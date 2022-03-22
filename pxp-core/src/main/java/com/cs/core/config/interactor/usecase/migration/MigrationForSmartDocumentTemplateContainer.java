package com.cs.core.config.interactor.usecase.migration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.DAMConstants;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.strategy.usecase.smartdocument.template.IGetAllSmartDocumentTemplateStrategy;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * This class is for Migrating existing templates from document container to new
 * smart document template container.
 * 
 * @author jamil.ahmad
 *
 */
@Service
public class MigrationForSmartDocumentTemplateContainer implements IMigrationForSmartDocumentTemplateContainer {
  
  @Autowired
  IGetAllSmartDocumentTemplateStrategy getAllSmartDocumentTemplateStrategy;
  
  @Override
  public IVoidModel execute(IVoidModel dataModel) throws Exception
  {
    IListModel<ISmartDocumentTemplateModel> smatDocumentsTemplateList = getAllSmartDocumentTemplateStrategy.execute(new IdParameterModel());
    for (ISmartDocumentTemplateModel smatDocumentsTemplate : smatDocumentsTemplateList.getList()) {
      migrateDocumentTOSDTemplate(smatDocumentsTemplate);
    }
    return null;
  }
  
  /**
   * Migrate template from document container to SDTemplate container and delete
   * from document container for single zipTampleteId.
   * 
   * @param smatDocumentsTemplate
   * @throws CSInitializationException
   * @throws IOException
   * @throws PluginException
   */
  private void migrateDocumentTOSDTemplate(ISmartDocumentTemplateModel smatDocumentsTemplate) throws CSInitializationException, IOException, PluginException
  {
    Map<String, String> assetServerDetails = new HashMap<>();
    IJSONContent authenticateSwiftServer;
    authenticateSwiftServer = CSDAMServer.instance().getServerInformation();
    assetServerDetails.put("storageUrl", authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
    assetServerDetails.put("authToken", authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, ""));
    
    Map<String, Object> assetFromSwift = getDocumentFromSwiftServer(assetServerDetails, smatDocumentsTemplate);
    if (assetFromSwift != null) {
      uploadSDTemplateToServer(assetServerDetails, smatDocumentsTemplate, assetFromSwift);
      deleteDocumentFromServer(assetServerDetails, smatDocumentsTemplate);
    }
  }
  

  /**
   * Get zip document asset from Document container in swift server using
   * zipTemplateId.
   * 
   * @param assetServerDetails
   * @param smatDocumentsTemplate
   * @return
   * @throws CSInitializationException
   * @throws IOException
   * @throws PluginException
   */
  private Map<String, Object> getDocumentFromSwiftServer(Map<String, String> assetServerDetails,
      ISmartDocumentTemplateModel smatDocumentsTemplate) throws CSInitializationException, IOException, PluginException
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_KEY, smatDocumentsTemplate.getZipTemplateId());
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_SERVER_DETAILS, assetServerDetails);
    requestMap.put(IGetAssetDetailsRequestModel.CONTAINER, DAMConstants.SWIFT_CONTAINER_DOCUMENT);
    requestMap.put(IGetAssetDetailsRequestModel.REQUEST_HEADERS, new HashMap<>());
    return CSDAMServer.instance().getAsset(requestMap);
  }
  
  /**
   * Upload zip document asset to SDTemplate container in swift server using
   * zipTemplateId.
   * 
   * @param assetServerDetails
   * @param smatDocumentsTemplate
   * @param assetFromSwift
   * @throws CSInitializationException
   * @throws IOException
   * @throws PluginException
   */
  @SuppressWarnings("unchecked")
  private void uploadSDTemplateToServer(Map<String, String> assetServerDetails, ISmartDocumentTemplateModel smatDocumentsTemplate,
      Map<String, Object> assetFromSwift) throws CSInitializationException, IOException, PluginException
  {
    InputStream inputStream = (InputStream) assetFromSwift.get(IGetAssetDetailsResponseModel.INPUT_STREAM);
    Map<String, String> responseHeaders = (Map<String, String>) assetFromSwift.get(IGetAssetDetailsResponseModel.RESPONSE_HEADERS);
    Map<String, Object> assetDataMap = fillAssetDataToUpload(assetServerDetails, smatDocumentsTemplate, responseHeaders);
    Map<String, Object> uploadRequestMap = fillRequestMapToUpload(assetServerDetails, smatDocumentsTemplate, inputStream, assetDataMap);
    CSDAMServer.instance().uploadAssetToSharedContainer(uploadRequestMap);
  }
  
  /**
   * Delete Zip document asset from Document container.
   * @param assetServerDetails
   * @param smatDocumentsTemplate
   * @return
   * @throws IOException
   * @throws PluginException
   * @throws CSInitializationException
   */
  private int deleteDocumentFromServer(Map<String, String> assetServerDetails, ISmartDocumentTemplateModel smatDocumentsTemplate) throws IOException, PluginException, CSInitializationException
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_KEY, smatDocumentsTemplate.getZipTemplateId());
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_SERVER_DETAILS, assetServerDetails);
    requestMap.put(IGetAssetDetailsRequestModel.CONTAINER, DAMConstants.SWIFT_CONTAINER_DOCUMENT);
    requestMap.put(IGetAssetDetailsRequestModel.REQUEST_HEADERS, new HashMap<>());
    return CSDAMServer.instance().deleteAssetFromSwiftServer(requestMap);
  }
  
  private Map<String, Object> fillRequestMapToUpload(Map<String, String> assetServerDetails, ISmartDocumentTemplateModel smatDocumentsTemplate, InputStream inputStream,
      Map<String, Object> assetDataMap) throws CSInitializationException, IOException
  {
    Map<String, Object> uploadRequestMap = new HashMap<>();
    uploadRequestMap.put("storageUrl", assetServerDetails.get("storageUrl"));
    uploadRequestMap.put(IGetAssetDetailsRequestModel.CONTAINER, DAMConstants.SWIFT_CONTAINER_SD_TEMPLATES);
    uploadRequestMap.put(IAssetInformationModel.ASSET_OBJECT_KEY, smatDocumentsTemplate.getZipTemplateId());
    uploadRequestMap.put("assetData", assetDataMap);
    uploadRequestMap.put("assetBytes", IOUtils.toByteArray(inputStream));
    return uploadRequestMap;
  }
  
  private Map<String, Object> fillAssetDataToUpload(Map<String, String> assetServerDetails, ISmartDocumentTemplateModel smatDocumentTemplate, Map<String, String> responseHeaders)
  {
    Map<String, Object> assetDataMap = new HashMap<>();
    String thumbContentType = DAMConstants.CONTENT_TYPE_APPLICATION + DAMConstants.ZIP;
    String zipTemplateId = smatDocumentTemplate.getZipTemplateId();
    String fileName = responseHeaders.get(DAMConstants.REQUEST_HEADER_OBJECT_META_NAME);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_AUTH_TOKEN, assetServerDetails.get("authToken"));
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_NAME, fileName);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_FORMAT, DAMConstants.ZIP);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_CONTENT_TYPE, thumbContentType);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_CONTENT_TYPE, thumbContentType);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_TYPE, thumbContentType);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_THUMB, zipTemplateId);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_PREVIEW_ID, zipTemplateId);
    
    return assetDataMap;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
