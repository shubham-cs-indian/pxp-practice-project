package com.cs.ui.config.controller.usecase.asset;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.cs.core.config.interactor.usecase.asset.ISaveAsset;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveAssetController extends BaseController implements IConfigController {
  
  @Autowired
  ISaveAsset saveAsset;
  
  @RequestMapping(value = "/assets", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody AbstractKlassSaveModel assetModel) throws Exception
  {
    return createResponse(saveAsset.execute(assetModel));
  }
}
