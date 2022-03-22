package com.cs.ui.config.controller.usecase.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.GetChildMajorTaxonomiesRequestModel;
import com.cs.core.runtime.interactor.usecase.taxonomy.IGetImmediateChildMajorTaxonomiesByParentId;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetImmediateChildMajorTaxonomiesByParentIdController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetImmediateChildMajorTaxonomiesByParentId getImmediateChildMajorTaxonomiesByParentId;
  
  @RequestMapping(value = "/immediatechildmajortaxonomies", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetChildMajorTaxonomiesRequestModel model)
      throws Exception
  {
    return createResponse(getImmediateChildMajorTaxonomiesByParentId.execute(model));
  }
}
