package com.cs.ui.runtime.controller.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.instancetree.GetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.usecase.instancetree.IGetKlassTaxonomyTree;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetKlassTaxonomyTreeController extends BaseController {
  
  @Autowired
  protected IGetKlassTaxonomyTree getKlassTaxonomyTree;
  
  @RequestMapping(value = "/klasstaxonomytree/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetKlassTaxonomyTreeRequestModel model) throws Exception
  {
    return createResponse(getKlassTaxonomyTree.execute(model));
  }
  
}
