package com.cs.ui.runtime.controller.usecase.articleinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.model.configdetails.ConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.usecase.getTaxonomyHierarchyForSelectedTaxonomy.IGetTaxonomyHierarchyForSelectedTaxonomy;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class GetTaxonomyHierarchyForSelectedTaxonomyController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetTaxonomyHierarchyForSelectedTaxonomy getTaxonomyHierarchyForSelectedTaxonomy;
  
  @RequestMapping(value = "/taxonomyhierarchyforselectedtaxonomy", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ConfigDetailsForSwitchTypeRequestModel model)
      throws Exception
  {
    return createResponse(getTaxonomyHierarchyForSelectedTaxonomy.execute(model));
  }
}
