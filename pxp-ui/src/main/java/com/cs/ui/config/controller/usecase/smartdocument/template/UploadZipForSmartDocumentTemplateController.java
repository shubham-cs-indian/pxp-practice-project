package com.cs.ui.config.controller.usecase.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.asset.AssetUploadDataModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;
import com.cs.core.config.interactor.usecase.smartdocument.template.IUploadZipForSmartDocumentTemplate;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * This controller is used to upload zip for Smart Document Template.
 * @author vannya.kalani
 *
 */
@RestController
@RequestMapping(value = "/config")
public class UploadZipForSmartDocumentTemplateController extends BaseController
    implements IConfigController {
  
  @Autowired
  IUploadZipForSmartDocumentTemplate uploadZipForSmartDocumentTemplate;

  @PostMapping(value = "/smartdocument/uploadtemplate")
  public @ResponseBody IRESTModel uploadMultipleFileHandler(
      @RequestParam(required = false) String mode, String fileUploadId,
      MultipartHttpServletRequest request) throws Exception
  {
    IAssetUploadDataModel uploadAssetModel = new AssetUploadDataModel();
    MultipartFile entry = request.getFileMap()
        .get("smartDocumentTemplate");
    uploadAssetModel.setAssetKey(fileUploadId);
    uploadAssetModel.setAssetBytes(entry.getBytes());
    
    return createResponse(uploadZipForSmartDocumentTemplate.execute(uploadAssetModel));
  }
}
