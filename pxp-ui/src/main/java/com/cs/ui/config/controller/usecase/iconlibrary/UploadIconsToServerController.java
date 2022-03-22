package com.cs.ui.config.controller.usecase.iconlibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.MultiPartFileInfoModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.iconlibrary.IUploadMultipleIconsRequestModel;
import com.cs.core.config.interactor.model.iconlibrary.UploadMultipleIconsRequestModel;
import com.cs.core.config.interactor.usecase.iconlibrary.IUploadIconsToServer;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * Bulk icon upload controller.
 * Accepts Multipart request with stringified format of codesMap
 * @author pranav.huchche
 *
 */

@RestController
@RequestMapping(value = "/config")
public class UploadIconsToServerController extends BaseController {
  
  @Autowired
  IUploadIconsToServer bulkCreateAndSaveIcons;
  
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/icons/upload", method = RequestMethod.POST)
  public IBulkUploadResponseAssetModel uploadIconsHandler(MultipartHttpServletRequest request)
      throws Exception
  {
    List<IMultiPartFileInfoModel> multiPartFileInfoModelList = new ArrayList<>();
    String stringifiedCodesMap = request.getParameter(IUploadMultipleIconsRequestModel.FILE_KEY_VS_CODE_MAP);
    String stringifiedNamesMap = request.getParameter(IUploadMultipleIconsRequestModel.FILE_KEY_VS_NAME_MAP);
    
    // Get codesMap
    Map<String, Object> fileKeyVsCodeMap = ObjectMapperUtil.readValue(stringifiedCodesMap, HashMap.class);
    
    // Get nameMap
    Map<String, Object> fileKeyVsNameMap = ObjectMapperUtil.readValue(stringifiedNamesMap, HashMap.class);
    
    // Iterate over file map and prepare multipart file info list
    for (Entry<String, MultipartFile> entry : request.getFileMap().entrySet()) {
      MultiPartFileInfoModel multiPartFileInfoModel = new MultiPartFileInfoModel();
      multiPartFileInfoModel.setKey(entry.getKey());
      multiPartFileInfoModel.setBytes(entry.getValue().getBytes());
      multiPartFileInfoModel.setOriginalFilename(entry.getValue().getOriginalFilename());
      multiPartFileInfoModelList.add(multiPartFileInfoModel);
    }
    
    // Prepare UploadMultipleIconsRequestModel and call service layer
    IUploadMultipleIconsRequestModel requestModel = new UploadMultipleIconsRequestModel();
    requestModel.setMultiPartFileInfoList(multiPartFileInfoModelList);
    requestModel.setFileKeyVsCodeMap(fileKeyVsCodeMap);
    requestModel.setFileKeyVsNameMap(fileKeyVsNameMap);
    
    return bulkCreateAndSaveIcons.execute(requestModel);
  }
}
