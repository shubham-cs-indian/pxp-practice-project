package com.cs.ui.config.controller.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.usecase.attributiontaxonomy.IDeleteMasterTaxonomy;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class DeleteMasterTaxonomyController extends BaseController {
  
  @Autowired
  protected IDeleteMasterTaxonomy deleteMasterTaxonomy;
  
  @RequestMapping(value = "/attributiontaxonomy", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody IdsListParameterModel model) throws Exception
  {
    return createResponse(deleteMasterTaxonomy.execute(model));
  }
}
