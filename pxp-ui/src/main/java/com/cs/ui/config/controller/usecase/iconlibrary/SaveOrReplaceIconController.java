package com.cs.ui.config.controller.usecase.iconlibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cs.core.config.interactor.model.asset.IIconResponseModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.MultiPartFileInfoModel;
import com.cs.core.config.interactor.model.iconlibrary.ISaveOrReplaceIconRequestModel;
import com.cs.core.config.interactor.model.iconlibrary.SaveOrReplaceIconRequestModel;
import com.cs.core.config.interactor.usecase.iconlibrary.ISaveOrReplaceIcon;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * This controller is used to update icon information.
 * @author pranav.huchche
 */

@RestController
@RequestMapping(value = "/config")
public class SaveOrReplaceIconController extends BaseController {
  
  @Autowired
  ISaveOrReplaceIcon saveOrReplaceIcon;
  
  @RequestMapping(value = "/icons/save", method = RequestMethod.POST)
  public IIconResponseModel saveOrReplaceIconHandler(MultipartHttpServletRequest request)
      throws Exception
  {
    List<IMultiPartFileInfoModel> multiPartFileInfoModelList = new ArrayList<>();
    String iconCode = request.getParameter(ISaveOrReplaceIconRequestModel.ICON_CODE);
    String iconName = request.getParameter(ISaveOrReplaceIconRequestModel.ICON_NAME);
    
    // Iterate over file map and prepare multipart file info list
    for (Entry<String, MultipartFile> entry : request.getFileMap().entrySet()) {
      MultiPartFileInfoModel multiPartFileInfoModel = new MultiPartFileInfoModel();
      multiPartFileInfoModel.setKey(entry.getKey());
      multiPartFileInfoModel.setBytes(entry.getValue().getBytes());
      multiPartFileInfoModel.setOriginalFilename(entry.getValue().getOriginalFilename());
      multiPartFileInfoModelList.add(multiPartFileInfoModel);
    }
    
    // Prepare UploadMultipleIconsRequestModel and call service layer
    ISaveOrReplaceIconRequestModel requestModel = new SaveOrReplaceIconRequestModel();
    requestModel.setMultiPartFileInfoList(multiPartFileInfoModelList);
    requestModel.setIconCode(iconCode);
    requestModel.setIconName(iconName);
    
    return saveOrReplaceIcon.execute(requestModel);
  }
}
