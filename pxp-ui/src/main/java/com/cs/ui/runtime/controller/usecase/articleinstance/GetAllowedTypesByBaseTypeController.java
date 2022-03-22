package com.cs.ui.runtime.controller.usecase.articleinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.typeswitch.AllowedTypesRequestModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetAllowedTypesByBaseType;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAllowedTypesByBaseTypeController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetAllowedTypesByBaseType getAllowedTypesByBaseType;
  
  @RequestMapping(value = "/allowedtypes", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody AllowedTypesRequestModel model) throws Exception
  {
    return createResponse(getAllowedTypesByBaseType.execute(model));
  }
}
