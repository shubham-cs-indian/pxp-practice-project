package com.cs.ui.config.controller.usecase.klass;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.klass.IGetAllKlasses;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllKlassByCategoryController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAllKlasses getAllKlasses;
  
  @RequestMapping(value = "/klassesByCategory/{rootId}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String rootId) throws Exception
  {
    IIdParameterModel idModel = new IdParameterModel(rootId);
    return createResponse(getAllKlasses.execute(idModel));
  }
}
