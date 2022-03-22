package com.cs.ui.runtime.controller.usecase.taxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.taxonomy.GetTaxonomyTreeModel;
import com.cs.core.runtime.interactor.usecase.taxonomy.IGetTaxonomyHierarchy;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetTaxonomyHierarchyController extends BaseController {
  
  @Autowired
  protected IGetTaxonomyHierarchy getTaxonomyHierarchy;
  
  @RequestMapping(value = "/taxonomyhierarchy", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetTaxonomyTreeModel model) throws Exception
  {
    
    return createResponse(getTaxonomyHierarchy.execute(model));
  }
  
}
