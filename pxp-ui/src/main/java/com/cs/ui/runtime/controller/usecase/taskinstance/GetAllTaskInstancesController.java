package com.cs.ui.runtime.controller.usecase.taskinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.usecase.taskinstance.IGetAllTaskInstances;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAllTaskInstancesController extends BaseController implements IRuntimeController {
  @Autowired
  protected IGetAllTaskInstances getTaskInstance;
  
  @RequestMapping(value = "/taskinstances/getall/{id}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String id, @RequestParam String physicalCatalogId) throws Exception
  {
    IIdParameterModel requestModel = new IdParameterModel(id);
    requestModel.setType(physicalCatalogId);
    return createResponse(getTaskInstance.execute(requestModel));
  }
}
