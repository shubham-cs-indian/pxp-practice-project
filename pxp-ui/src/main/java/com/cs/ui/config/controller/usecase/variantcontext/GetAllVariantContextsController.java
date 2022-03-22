package com.cs.ui.config.controller.usecase.variantcontext;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.core.config.interactor.usecase.variantcontext.IGetAllVariantContext;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllVariantContextsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetAllVariantContext getAllVariantContext;
  
  @RequestMapping(value = "/variantcontexts/getall", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ConfigGetAllRequestModel configDataRequestModel)
      throws Exception
  {
    return createResponse(getAllVariantContext.execute(configDataRequestModel));
  }
}
