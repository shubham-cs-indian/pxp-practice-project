package com.cs.ui.runtime.controller.usecase.taxonomy.inheritance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.entity.datarule.TaxonomyInheritanceRequestModel;
import com.cs.core.runtime.interactor.usecase.taxonomy.inheritance.IFetchContentTaxonomiesForTaxonomyInheritance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class FetchContentTaxonomiesForTaxonomyInheritanceController extends BaseController implements IRuntimeController{
  
  @Autowired
  protected IFetchContentTaxonomiesForTaxonomyInheritance fetchContentTaxonomiesForTaxonomyInheritance;
  
  @RequestMapping(value = "/fetchtaxonomiesinfo" , method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody TaxonomyInheritanceRequestModel model) throws Exception
  {
    return createResponse(fetchContentTaxonomiesForTaxonomyInheritance.execute(model));
  }
}