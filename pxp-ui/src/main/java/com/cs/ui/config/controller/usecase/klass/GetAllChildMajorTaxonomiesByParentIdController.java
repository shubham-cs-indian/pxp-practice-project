package com.cs.ui.config.controller.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.GetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.usecase.klass.IGetAllChildMajorTaxonomiesByParentId;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetAllChildMajorTaxonomiesByParentIdController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetAllChildMajorTaxonomiesByParentId   getAllChildMajorTaxonomiesByParentId;
  
  @RequestMapping(value = "/allchildmajortaxonomies", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetChildMajorTaxonomiesRequestModel model) throws Exception
  {
    return createResponse(getAllChildMajorTaxonomiesByParentId.execute(model));
  }
}