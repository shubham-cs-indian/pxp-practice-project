package com.cs.ui.runtime.controller.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.usecase.assetserver.UploadAssetDataToServer;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
public class UploadAssetDataToServerController extends BaseController {
  
  @Autowired
  UploadAssetDataToServer     uploadAssetDataToServer;
  
  @RequestMapping(value = "/assets/data/upload", method = RequestMethod.POST)
  public IVoidModel uploadMultipleFileHandler(@RequestParam(required = false) Integer batchSize)
      throws Exception
  {
    return uploadAssetDataToServer.execute(batchSize);
  }
}
