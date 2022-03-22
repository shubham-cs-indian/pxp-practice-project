package com.cs.ui.runtime.controller.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.textassetinstance.TextAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.usecase.textassetinstance.ICreateTranslatableTextAssetInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;



@RestController
@RequestMapping(value = "/runtime")
public class CreateTranslatableTextAssetInstanceController extends BaseController implements IRuntimeController {
 
  @Autowired
  protected ICreateTranslatableTextAssetInstance   createTranslatableTextAssetInstance;
  
  @RequestMapping(value = "/createtranslatabletextassetinstances", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody TextAssetInstanceSaveModel klassInstanceModel) throws Exception
  {
    return createResponse(createTranslatableTextAssetInstance.execute(klassInstanceModel));
  }
}
