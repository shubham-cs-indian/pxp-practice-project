package com.cs.ui.runtime.controller.usecase.taxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.taxonomy.GetTaxonomyTreeForVariantLinkedInstancesQuicklistModel;
import com.cs.core.runtime.interactor.usecase.taxonomy.IGetTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklist;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklist getTaxonomyTreeByLeafIdsForVariantQuicklist;
  
  @RequestMapping(value = "/variant/linkedinstances/quicklist/gettreebyleafids", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetTaxonomyTreeForVariantLinkedInstancesQuicklistModel model) throws Exception
  {
    return createResponse(getTaxonomyTreeByLeafIdsForVariantQuicklist.execute(model));
  }
  
}
