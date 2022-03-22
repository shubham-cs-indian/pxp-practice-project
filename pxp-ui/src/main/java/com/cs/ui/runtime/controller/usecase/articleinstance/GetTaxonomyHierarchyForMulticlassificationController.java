package com.cs.ui.runtime.controller.usecase.articleinstance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.instance.IdPaginationModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetTaxonomyHierarchyForMulticlassification;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetTaxonomyHierarchyForMulticlassificationController extends BaseController {
  
  @Autowired
  protected IGetTaxonomyHierarchyForMulticlassification getTaxonomyHierarchy;
  
  @RequestMapping(value = "/taxonomyhierarchy/multiclassification", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdPaginationModel model) throws Exception
  {
    return createResponse(getTaxonomyHierarchy.execute(model));
  }
  
}   