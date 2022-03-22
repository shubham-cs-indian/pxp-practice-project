package com.cs.ui.config.controller.usecase.attributiontaxonomy;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.taxonomy.GetTaxonomyRequestModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;
import com.cs.core.config.interactor.usecase.attributiontaxonomy.IGetKlassAndAttributionTaxonomy;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetKlassAndAttributionTaxonomyController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetKlassAndAttributionTaxonomy getKlassAndAttributionTaxonomy;
  
  @RequestMapping(value = "/klassandattributiontaxonomy/{id}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String id) throws Exception
  {
    IGetTaxonomyRequestModel parameterModel = new GetTaxonomyRequestModel();
    parameterModel.setId(id);
    return createResponse(getKlassAndAttributionTaxonomy.execute(parameterModel));
  }
}
