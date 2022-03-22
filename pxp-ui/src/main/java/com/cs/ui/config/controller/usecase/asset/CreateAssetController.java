package com.cs.ui.config.controller.usecase.asset;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.usecase.asset.ICreateAsset;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateAssetController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateAsset createAsset;
  
  @RequestMapping(value = "/assets", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody AbstractKlassModel assetModel) throws Exception
  {
    return createResponse(createAsset.execute((IAssetModel) assetModel));
  }
}
