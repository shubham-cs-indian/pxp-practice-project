package com.cs.ui.runtime.controller.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInstanceSaveModel;
import com.cs.dam.runtime.interactor.usecase.assetinstance.ICreateTranslatableAssetInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
@RestController
@RequestMapping(value = "/runtime")
public class CreateTranslatableAssetInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  ICreateTranslatableAssetInstance createTranslatableAssetInstance;
  
  @RequestMapping(value = "/createtranslatableassetinstances", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody AssetInstanceSaveModel klassInstanceModel) throws Exception
  {
    return createResponse(createTranslatableAssetInstance.execute(klassInstanceModel));
  }
}
