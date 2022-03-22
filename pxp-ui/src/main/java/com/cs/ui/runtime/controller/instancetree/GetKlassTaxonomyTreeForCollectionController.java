package com.cs.ui.runtime.controller.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.instancetree.GetKlassTaxonomyTreeForCollectionRequestModel;
import com.cs.core.runtime.interactor.usecase.instancetree.IGetKlassTaxonomyTreeForCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetKlassTaxonomyTreeForCollectionController extends BaseController {
  
  @Autowired
  protected IGetKlassTaxonomyTreeForCollection getKlassTaxonomyTreeForCollection;
  
  @RequestMapping(value = "/collection/klasstaxonomytree", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetKlassTaxonomyTreeForCollectionRequestModel model) throws Exception
  {
    return createResponse(getKlassTaxonomyTreeForCollection.execute(model));
  }
  
}
