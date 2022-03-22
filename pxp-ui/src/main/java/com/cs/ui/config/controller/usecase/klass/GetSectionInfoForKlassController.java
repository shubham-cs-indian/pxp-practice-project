package com.cs.ui.config.controller.usecase.klass;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.GetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.usecase.klass.IGetSectionInfoForKlass;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetSectionInfoForKlassController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetSectionInfoForKlass getSectionInfoForKlass;
  
  @RequestMapping(value = "/klasses/getSectionInfoForKlass", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetSectionInfoForTypeRequestModel getEntityModel)
      throws Exception
  {
    return createResponse(getSectionInfoForKlass.execute(getEntityModel));
  }
}
