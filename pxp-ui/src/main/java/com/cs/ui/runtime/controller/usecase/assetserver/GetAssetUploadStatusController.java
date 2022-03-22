package com.cs.ui.runtime.controller.usecase.assetserver;

import com.cs.core.config.interactor.model.assetstatus.AssetUploadStatusCheckModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusCheckModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusResultModel;
import com.cs.core.config.interactor.usecase.asset.IGetAssetUploadStatus;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetAssetUploadStatusController extends BaseController {
  
  @Autowired
  IGetAssetUploadStatus getAssetUploadStatus;
  
  @RequestMapping(value = "/asset/upload/status/{container}/{key}", method = RequestMethod.GET)
  public IAssetUploadStatusResultModel getAssetUploadStatus(@PathVariable String container,
      @PathVariable String key) throws Exception
  {
    IAssetUploadStatusCheckModel model = new AssetUploadStatusCheckModel(container, key);
    return getAssetUploadStatus.execute(model);
  }
}
