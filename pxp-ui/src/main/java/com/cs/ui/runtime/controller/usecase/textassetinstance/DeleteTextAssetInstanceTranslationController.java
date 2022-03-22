package com.cs.ui.runtime.controller.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.languageinstance.DeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IDeleteTextAssetInstanceTranslation;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping(value = "/runtime")
public class DeleteTextAssetInstanceTranslationController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IDeleteTextAssetInstanceTranslation deleteTranslation;
  
  @RequestMapping(value = "textasset/translations", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody DeleteTranslationRequestModel model) throws Exception
  {
    return createResponse(deleteTranslation.execute(model));
  }
}
