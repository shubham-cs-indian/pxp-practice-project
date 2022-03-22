package com.cs.ui.config.controller.usecase.attributiontaxonomy;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.attributiontaxonomy.SaveMasterTaxonomyModel;
import com.cs.core.config.interactor.usecase.attributiontaxonomy.ISaveMasterTaxonomy;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveMasterTaxonomyController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveMasterTaxonomy saveMasterTaxonomy;
  
  @RequestMapping(value = "/attributiontaxonomy", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveMasterTaxonomyModel model) throws Exception
  {
    return createResponse(saveMasterTaxonomy.execute(model));
  }
}
