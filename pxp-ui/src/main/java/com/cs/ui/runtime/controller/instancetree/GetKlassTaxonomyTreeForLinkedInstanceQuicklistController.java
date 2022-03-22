package com.cs.ui.runtime.controller.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.instancetree.GetKlassTaxonomyTreeForLIQRequestModel;
import com.cs.core.runtime.interactor.usecase.instancetree.IGetKlassTaxonomyTreeForLinkedInstanceQuicklist;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetKlassTaxonomyTreeForLinkedInstanceQuicklistController extends BaseController {
  
  @Autowired
  protected IGetKlassTaxonomyTreeForLinkedInstanceQuicklist getKlassTaxonomyTreeForLinkedInstanceQuicklist;
  
  @RequestMapping(value = "/linkedinstance/quicklist/klasstaxonomytree", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetKlassTaxonomyTreeForLIQRequestModel model) throws Exception
  {
    return createResponse(getKlassTaxonomyTreeForLinkedInstanceQuicklist.execute(model));
  }
  
}
