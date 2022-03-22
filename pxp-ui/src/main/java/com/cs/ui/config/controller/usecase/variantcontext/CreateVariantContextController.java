package com.cs.ui.config.controller.usecase.variantcontext;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.variantcontext.CreateVariantContextModel;
import com.cs.core.config.interactor.usecase.variantcontext.ICreateVariantContext;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateVariantContextController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateVariantContext createVariantContext;
  
  @RequestMapping(value = "/variantcontexts", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateVariantContextModel createVariantcontextmodel)
      throws Exception
  {
    
    return createResponse(createVariantContext.execute(createVariantcontextmodel));
  }
}
