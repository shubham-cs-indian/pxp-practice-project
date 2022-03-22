package com.cs.ui.runtime.controller.usecase.taxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.taxonomy.GetTaxonomyTreeForQuicklistModel;
import com.cs.core.runtime.interactor.usecase.taxonomy.IGetTaxonomyTreeByLeafIdsForRelationshipQuicklist;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetTaxonomyTreeByLeafIdsForRelationshipController extends BaseController {
  
  @Autowired
  protected IGetTaxonomyTreeByLeafIdsForRelationshipQuicklist getTaxonomyTreeByLeafIdsForRelationshipQuicklist;
  
  @RequestMapping(value = "/relationship/quicklist/gettreebyleafids", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetTaxonomyTreeForQuicklistModel model) throws Exception
  {
    return createResponse(getTaxonomyTreeByLeafIdsForRelationshipQuicklist.execute(model));
  }
  
}
