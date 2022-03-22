package com.cs.ui.runtime.controller.usecase.articleinstance.variant;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.VariantLinkedInstancesQuickListModel;
import com.cs.pim.runtime.strategy.usecase.articleinstance.IGetVariantLinkedInstancesQuickList;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/runtime")
public class GetVariantEntitiesQuickListController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IGetVariantLinkedInstancesQuickList getVariantEntitiesQuickList;
  
  @RequestMapping(value = "/variant/entitiesquicklist", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody VariantLinkedInstancesQuickListModel deleteVariantsModel)
      throws Exception
  {
    
    return createResponse(getVariantEntitiesQuickList.execute(deleteVariantsModel));
  }
}
