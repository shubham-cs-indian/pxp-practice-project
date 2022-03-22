package com.cs.ui.runtime.controller.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.instancetree.GetKlassTaxonomyTreeForRelationshipRequestModel;
import com.cs.core.runtime.interactor.usecase.instancetree.IGetKlassTaxonomyTreeForRelationship;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetKlassTaxonomyTreeForRelationshipController extends BaseController {
  
  @Autowired
  protected IGetKlassTaxonomyTreeForRelationship getKlassTaxonomyTreeForRelationship;
  
  @RequestMapping(value = "/relationship/quicklist/klasstaxonomytree", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetKlassTaxonomyTreeForRelationshipRequestModel model) throws Exception
  {
    return createResponse(getKlassTaxonomyTreeForRelationship.execute(model));
  }
  
}
