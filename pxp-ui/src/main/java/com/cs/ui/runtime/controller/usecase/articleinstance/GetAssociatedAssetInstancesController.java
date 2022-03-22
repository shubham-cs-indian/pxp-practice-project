package com.cs.ui.runtime.controller.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetAssociatedAssetInstances;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAssociatedAssetInstancesController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetAssociatedAssetInstances getAssociatedAssetInstances;
  
  @RequestMapping(value = "/klassinstance/assets", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdParameterModel klassIds) throws Exception
  {
    return createResponse(getAssociatedAssetInstances.execute(klassIds));
  }
}
