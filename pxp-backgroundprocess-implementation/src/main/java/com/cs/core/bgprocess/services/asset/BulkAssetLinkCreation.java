package com.cs.core.bgprocess.services.asset;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.cs.constants.DAMConstants;
import com.cs.core.asset.iservices.IBulkCreateAssetsLinks;
import com.cs.core.asset.services.BulkCreateAssetsLinks;
import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.dto.BulkAssetLinkCreationDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IBulkAssetLinkCreationDTO;
import com.cs.core.bgprocess.services.data.AbstractBaseEntityProcessing;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.asset.dao.AssetMiscDAO;
import com.cs.core.rdbms.asset.dto.AssetMiscDTO;
import com.cs.core.rdbms.asset.idao.IAssetMiscDAO;
import com.cs.core.rdbms.asset.idto.IAssetMiscDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.services.CSDAMServer;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class BulkAssetLinkCreation extends AbstractBaseEntityProcessing {
  
  IBulkAssetLinkCreationDTO   assetLinkCreationDTO = new BulkAssetLinkCreationDTO();
  
  private static final String STORAGE_URL          = "storageUrl";
  private static final String NOT_PRESENT          = "NotPresent";
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    // Create fake base entity iids in order to benefit from
    // AbstractBaseEntityProcessing
    BGProcessDTO preJobData = (BGProcessDTO) initialProcessData;
    assetLinkCreationDTO.fromJSON(preJobData.getEntryData()
        .toString());
    super.initBeforeStart(initialProcessData, userSession);
  }
  
  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs) throws RDBMSException, PluginException, CSInitializationException
  {
    IAssetMiscDAO assetMiscDao = AssetMiscDAO.getInstance();
    long uploadedAssetCount = 0;
    IBulkCreateAssetsLinks bulkCreateAssetLinks = new BulkCreateAssetsLinks(
        assetLinkCreationDTO.getDataLanguage(), assetLinkCreationDTO.getPhysicalCatalogId(),
        assetLinkCreationDTO.getOrganizationId());
    Collection<IBaseEntityDTO> getAssetEntities = bulkCreateAssetLinks
        .getAssetEntities(assetLinkCreationDTO, userSession);
    Map<String, String> assetServerDetails = new HashMap<>();
    IJSONContent authenticateSwiftServer;
    
    jobData.getLog().info("Link Generation triggered for %s asset(s).", getAssetEntities.size());
    
    try {
      authenticateSwiftServer = CSDAMServer.instance().getServerInformation();
      assetServerDetails.put(STORAGE_URL,
          authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
      assetServerDetails.put("authToken",
          authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, ""));
      
      for (IBaseEntityDTO entity : getAssetEntities) {
        batchIIDs.add(entity.getBaseEntityIID());
        
        try {
          IJSONContent entityExtension = entity.getEntityExtension();
          String assetObjectKey = entityExtension.getInitField(IAssetInformationModel.ASSET_OBJECT_KEY, "");
          long baseEntityId = entity.getBaseEntityIID();
          String existingSharedObjectId = assetMiscDao.getAssetMiscRecordById(baseEntityId);
          
          if ((!StringUtils.isEmpty(existingSharedObjectId) && !NOT_PRESENT.equalsIgnoreCase(existingSharedObjectId))
              || StringUtils.isEmpty(assetObjectKey))
          {
            jobData.getLog().warn("Cannot generate shared link for %s. Reason: Link already exist/Asset is not present.", entity.getBaseEntityName());            
            continue;
          }
          
          String parentInstanceId = Long.toString(entity.getParentIID());
          long assetInstanceId = 0;
          long renditionInstanceId = 0;
          if (StringUtils.isEmpty(parentInstanceId) || parentInstanceId.equals("0")) {
            assetInstanceId = baseEntityId;
          }
          else {
            assetInstanceId = entity.getParentIID();
            renditionInstanceId = baseEntityId;
          }
          
          String sharedObjectId = UUID.randomUUID().toString();
          Map<String, Object> assetFromSwift = getAssetToShareFromSwift(assetServerDetails, entityExtension);
          if (assetFromSwift != null) {
            uploadAssetToServer(assetServerDetails, entity, entityExtension, assetFromSwift, sharedObjectId);
            storeSharedObjectId(assetInstanceId, renditionInstanceId, sharedObjectId, existingSharedObjectId, baseEntityId);
          }
          uploadedAssetCount++;
          jobData.getLog().info("Link Generated successfully: %s", entity.getBaseEntityName());
        }
        catch (Exception e) {
          jobData.getLog().error("Failure in Link Generation for " + entity.getBaseEntityName() + " :: " + e.getMessage());
        }
      }
    }
    catch (CSInitializationException e) {
      jobData.setStatus(BGPStatus.ENDED_EXCEPTION);
      throw new CSInitializationException("Failure in Link Generation :: ", e);
    }
    
    jobData.getEntryData().setLongArrayField(BaseEntityPlanDTO.BASE_ENTITY_IIDS, batchIIDs);
    jobData.getLog().info("Link Generated successfully for %s asset(s).", uploadedAssetCount);
  }
  
  private Map<String, Object> getAssetToShareFromSwift(Map<String, String> assetServerDetails,
      IJSONContent entityExtension) throws CSInitializationException, IOException, PluginException
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_KEY,
        entityExtension.getInitField(IAssetInformationModel.ASSET_OBJECT_KEY, ""));
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_SERVER_DETAILS, assetServerDetails);
    requestMap.put(IGetAssetDetailsRequestModel.CONTAINER,
        entityExtension.getInitField(IAssetInformationModel.TYPE, ""));
    requestMap.put(IGetAssetDetailsRequestModel.REQUEST_HEADERS, new HashMap<>());
    return CSDAMServer.instance().getAsset(requestMap);
  }
  
  private void uploadAssetToServer(Map<String, String> assetServerDetails, IBaseEntityDTO entity,
      IJSONContent entityExtension, Map<String, Object> assetFromSwift, String sharedObjectId)
      throws CSInitializationException, IOException, PluginException
  {
    InputStream inputStream = (InputStream) assetFromSwift.get("inputStream");
    Map<String, Object> assetDataMap = fillAssetData(assetServerDetails, entity, entityExtension);
    Map<String, Object> uploadRequestMap = fillRequestMapToUpload(assetServerDetails,
        sharedObjectId, inputStream, assetDataMap);
    CSDAMServer.instance()
        .uploadAssetToSharedContainer(uploadRequestMap);
  }
  
  private Map<String, Object> fillRequestMapToUpload(Map<String, String> assetServerDetails,
      String sharedObjectId, InputStream inputStream, Map<String, Object> assetDataMap)
      throws CSInitializationException, IOException
  {
    Map<String, Object> uploadRequestMap = new HashMap<>();
    uploadRequestMap.put(STORAGE_URL, assetServerDetails.get(STORAGE_URL));
    uploadRequestMap.put(IGetAssetDetailsRequestModel.CONTAINER, CSProperties.instance()
        .getString("linksharing.asset.sharecontainer"));
    uploadRequestMap.put(IAssetInformationModel.ASSET_OBJECT_KEY, sharedObjectId);
    uploadRequestMap.put("assetData", assetDataMap);
    uploadRequestMap.put("assetBytes", IOUtils.toByteArray(inputStream));
    return uploadRequestMap;
  }
  
  private void storeSharedObjectId(long assetInstanceId, long renditionInstanceId,
      String sharedObjectId, String existingSharedObjectId, long baseEntityId) throws RDBMSException
  {
    IAssetMiscDAO assetMiscDao = AssetMiscDAO.getInstance();
    IAssetMiscDTO assetMiscDTO = new AssetMiscDTO();
    
    if (StringUtils.isEmpty(existingSharedObjectId)) {
      // update sharedObjectId if asset exists in assetMisc table
      assetMiscDao.updateAssetMiscRecordWithSharedObjectId(sharedObjectId, baseEntityId);
    }
    else if (existingSharedObjectId.equalsIgnoreCase(NOT_PRESENT)) {
      // insert record into the assetmisc table
      assetMiscDTO.setAssetInstanceIId(assetInstanceId);
      assetMiscDTO.setRenditionInstanceIId(renditionInstanceId);
      assetMiscDTO.setSharedObjectId(sharedObjectId);
      assetMiscDTO.setSharedTimeStamp(System.currentTimeMillis());
      assetMiscDao.insertAssetMiscRecord(assetMiscDTO);
    }
  }
  
  private Map<String, Object> fillAssetData(Map<String, String> assetServerDetails,
      IBaseEntityDTO entity, IJSONContent entityExtension)
  {
    Map<String, Object> assetDataMap = new HashMap<>();
    IJSONContent content = null;
    IJSONContent properties = entityExtension.getInitField(IAssetInformationModel.PROPERTIES,
        content);
    String extension = properties.getInitField("extension", "");
    String thumbContentType = getContentType(
        entityExtension.getInitField(IAssetInformationModel.TYPE, "")) + extension;
    assetDataMap.put(DAMConstants.REQUEST_HEADER_AUTH_TOKEN, assetServerDetails.get("authToken"));
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_NAME, entity.getBaseEntityName());
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_FORMAT, extension);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_CONTENT_TYPE, thumbContentType);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_CONTENT_TYPE, thumbContentType);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_TYPE, DAMConstants.TYPE_ORIGINAL);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_ORIGINAL,
        entityExtension.getInitField(IAssetInformationModel.ASSET_OBJECT_KEY, ""));
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_THUMB,
        entityExtension.getInitField(IAssetInformationModel.THUMB_KEY, ""));
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_PREVIEW_ID,
        entityExtension.getInitField(IAssetInformationModel.PREVIEW_IMAGE_KEY, ""));
    return assetDataMap;
  }
  
  private String getContentType(String type)
  {
    switch (type) {
      case "Image":
        return DAMConstants.CONTENT_TYPE_IMAGE;
      case "Video":
        return DAMConstants.CONTENT_TYPE_VIDEO;
      case "Document":
        return DAMConstants.CONTENT_TYPE_APPLICATION;
    }
    return null;
  }
  
}
