package com.cs.ui.config.controller.usecase.article;

import com.cs.core.runtime.interactor.usecase.taxonomy.IGetArticleTaxonomyList;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetArticleTaxonomyListController extends BaseController {
  
  @Autowired
  IGetArticleTaxonomyList getArticleTaxonomyList;
  
  @RequestMapping(value = "/articletaxonomylist", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    
    return createResponse(getArticleTaxonomyList.execute(null));
  }
}
