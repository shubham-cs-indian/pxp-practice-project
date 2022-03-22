package com.cs.ui.config.controller.usecase.tagtype;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.tagtype.IGetAllTagTypes;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllTagTypesController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAllTagTypes getAllTagTypes;
  
  @RequestMapping(value = "/tagTypes", method = RequestMethod.GET)
  public IRESTModel getTagTypes() throws Exception
  {
    return createResponse(getAllTagTypes.execute(null));
  }
}
