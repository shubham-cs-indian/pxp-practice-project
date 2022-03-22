package com.cs.ui.config.controller.usecase.attribute;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.attribute.IGetAttributeList;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAttributeListController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAttributeList getAttributeList;
  
  @RequestMapping(value = "/attributelist", method = RequestMethod.GET)
  public IRESTModel execute(@RequestParam(required = false, defaultValue = "all") String mode)
      throws Exception
  {
    IIdParameterModel dataModel = new IdParameterModel(mode);
    return createResponse(getAttributeList.execute(dataModel));
  }
}
