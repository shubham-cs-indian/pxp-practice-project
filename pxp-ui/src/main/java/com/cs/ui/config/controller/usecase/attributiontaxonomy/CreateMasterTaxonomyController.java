package com.cs.ui.config.controller.usecase.attributiontaxonomy;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.attributiontaxonomy.CreateMasterTaxonomyModel;
import com.cs.core.config.interactor.usecase.attributiontaxonomy.ICreateMasterTaxonomy;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateMasterTaxonomyController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateMasterTaxonomy createMasterTaxonomy;
  
  @RequestMapping(value = "/attributiontaxonomy", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateMasterTaxonomyModel model) throws Exception
  {
    return createResponse(createMasterTaxonomy.execute(model));
  }
}
