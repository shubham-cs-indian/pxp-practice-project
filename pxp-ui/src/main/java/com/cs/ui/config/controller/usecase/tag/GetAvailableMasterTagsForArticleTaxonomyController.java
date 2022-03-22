package com.cs.ui.config.controller.usecase.tag;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.datarule.GetAllowedMasterTagsForHierarchyTaxonomyRequestModel;
import com.cs.core.config.interactor.usecase.tag.IGetAvailableMasterTagsForArticleTaxonomy;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAvailableMasterTagsForArticleTaxonomyController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetAvailableMasterTagsForArticleTaxonomy getAvailableMasterTagsForArticleTaxonomy;
  
  @RequestMapping(value = "/allowedmastertags/articletaxonomy", method = RequestMethod.POST)
  public IRESTModel getTagTypes(
      @RequestBody GetAllowedMasterTagsForHierarchyTaxonomyRequestModel idParameterModel)
      throws Exception
  {
    return createResponse(getAvailableMasterTagsForArticleTaxonomy.execute(idParameterModel));
  }
}
