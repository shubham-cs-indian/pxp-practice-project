package com.cs.ui.runtime.controller.usecase.assetinstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.asset.MultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.UploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.usecase.assetserver.IUploadMultipleAssetsInForeground;
import com.cs.core.config.interactor.usecase.assetserver.IUploadMultipleAssetsToServer;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
public class UploadAssetToServerController extends BaseController {
  
  @Autowired
  IUploadMultipleAssetsToServer     uploadMultipleAssetsToServer;
  
  @Autowired
  IUploadMultipleAssetsToServer     uploadAssetToServer;
  
  @Autowired
  IUploadMultipleAssetsInForeground uploadMultipleAssetsInForeground;
  
  @RequestMapping(value = "/assets/upload", method = RequestMethod.POST)
  public IBulkUploadResponseAssetModel uploadMultipleFileHandler(
      @RequestParam(required = false) String mode,
      @RequestParam(required = false) Boolean isUploadedFromInstance,
      @RequestParam(required = true) String klassId, MultipartHttpServletRequest request)
      throws Exception
  {
    
    IUploadAssetModel uploadAssetModel = getUploadAssetFileModel(mode, klassId, request);

    if (isUploadedFromInstance != null) {
      uploadAssetModel.setIsUploadedFromInstance(isUploadedFromInstance);
      // task attachments not process through multiple Assets upload BGP
      if (isUploadedFromInstance == true || CommonConstants.SWIFT_CONTAINER_ATTACHMENT.equalsIgnoreCase(mode)) {
        return uploadAssetToServer.execute(uploadAssetModel);
      }
      else {
        String collectionIds = request.getParameter("collectionIds");
        if(StringUtils.isNotEmpty(collectionIds)) {
          List<String> collectionIdList = Arrays.asList(collectionIds.split(","));
          uploadAssetModel.setCollectionIds(collectionIdList);
        }
        return uploadMultipleAssetsToServer.execute(uploadAssetModel);
      }
    }
    else {
      //configuration images not process through multiple Assets upload BGP
      if(CommonConstants.MODE_CONFIG.equalsIgnoreCase(uploadAssetModel.getMode())) {
        return uploadAssetToServer.execute(uploadAssetModel);
      }else {
        return uploadMultipleAssetsToServer.execute(uploadAssetModel);
      }
    }
  }

  /**
   * This is multipart request to upload multiple assets in foreground
   * @param mode
   * @param collectionId
   * @param klassId
   * @param request
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/assets/uploadinforeground", method = RequestMethod.POST)
  public IBulkUploadResponseAssetModel uploadMultipleFileHandler(
      @RequestParam(required = false) String mode,
      @RequestParam(required = true) String klassId,
      MultipartHttpServletRequest request) throws Exception
  {
    IUploadAssetModel uploadAssetModel = getUploadAssetFileModel(mode, klassId, request);
    String collectionIds = request.getParameter("collectionIds");
    if(StringUtils.isNotEmpty(collectionIds)) {
      List<String> collectionIdList = Arrays.asList(collectionIds.split(","));
      uploadAssetModel.setCollectionIds(collectionIdList);
    }
    
    return uploadMultipleAssetsInForeground.execute(uploadAssetModel);
  }
  
  // This is private method to accept request and returns uploadAssetModel
  private IUploadAssetModel getUploadAssetFileModel(String mode, String klassId,
      MultipartHttpServletRequest request) throws IOException
  {
    IUploadAssetModel uploadAssetModel = new UploadAssetModel();
    List<IMultiPartFileInfoModel> multiPartFileInfoModelList = new ArrayList<>();
    
    // Extract data from 'MultipartHttpServletRequest' and add to List of 'MultiPartFileInfoModel'
    for (Entry<String, MultipartFile> entry : request.getFileMap().entrySet()) {
      MultiPartFileInfoModel multiPartFileInfoModel = new MultiPartFileInfoModel();
      multiPartFileInfoModel.setKey(entry.getKey());
      multiPartFileInfoModel.setBytes(entry.getValue().getBytes());
      multiPartFileInfoModel.setOriginalFilename(entry.getValue().getOriginalFilename());
      multiPartFileInfoModelList.add(multiPartFileInfoModel);
    }
    
    uploadAssetModel.setMode(mode);
    uploadAssetModel.setMultiPartFileInfoList(multiPartFileInfoModelList);
    uploadAssetModel.setKlassId(klassId);
    uploadAssetModel.setAdditionalParameterMap(request.getParameterMap());
    
    return uploadAssetModel;
  }
}
