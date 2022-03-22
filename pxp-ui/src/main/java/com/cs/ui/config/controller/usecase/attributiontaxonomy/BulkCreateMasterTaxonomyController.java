package com.cs.ui.config.controller.usecase.attributiontaxonomy;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.attributiontaxonomy.CreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.usecase.attributiontaxonomy.IBulkCreateMasterTaxonomy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/config")
public class BulkCreateMasterTaxonomyController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IBulkCreateMasterTaxonomy createMasterTaxonomy;
  
  @RequestMapping(value = "/attributiontaxonomy/bulk", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody ArrayList<CreateMasterTaxonomyModel> model)
      throws Exception
  {
    IListModel<ICreateMasterTaxonomyModel> listModel = new ListModel<>(model);
    return createResponse(createMasterTaxonomy.execute(listModel));
  }
}
