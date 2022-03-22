package com.cs.ui.config.controller.usecase.klass;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.GetKlassesByBaseTypeModel;
import com.cs.core.config.interactor.usecase.klass.IGetKlassesListByBaseType;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetKlassesListByBaseTypeController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetKlassesListByBaseType getKlassesListByBaseType;
  
  @RequestMapping(value = "/klasseslistbybasetype", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetKlassesByBaseTypeModel getAllModel) throws Exception
  {
    return createResponse(getKlassesListByBaseType.execute(getAllModel));
  }
}
