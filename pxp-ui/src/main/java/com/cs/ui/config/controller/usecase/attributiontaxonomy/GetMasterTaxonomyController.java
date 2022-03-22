package com.cs.ui.config.controller.usecase.attributiontaxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.taxonomy.GetTaxonomyRequestModel;
import com.cs.core.config.interactor.usecase.attributiontaxonomy.IGetMasterTaxonomy;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetMasterTaxonomyController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetMasterTaxonomy getMasterTaxonomy;
  
  @RequestMapping(value = "/attributiontaxonomy/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetTaxonomyRequestModel model)
      throws Exception
  {
    return createResponse(getMasterTaxonomy.execute(model));
  }
}
