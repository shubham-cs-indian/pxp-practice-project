package com.cs.ui.config.controller.usecase.variantcontext;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.variantcontext.SaveVariantContextModel;
import com.cs.core.config.interactor.usecase.variantcontext.ISaveVariantContext;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveVariantContextController extends BaseController implements IConfigController {
  
  @Autowired
  ISaveVariantContext saveVariantContext;
  
  @RequestMapping(value = "/variantcontexts", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveVariantContextModel model) throws Exception
  {
    return createResponse(saveVariantContext.execute(model));
  }
}
