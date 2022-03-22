package com.cs.ui.config.controller.usecase.collectionsstatics;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.GetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.usecase.collection.IGetSectionInfoForCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetSectionInfoForCollectionController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetSectionInfoForCollection getSectionInfoForCollection;
  
  @RequestMapping(value = "/collection/getsectioninfoforcollection", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetSectionInfoForTypeRequestModel getEntityModel)
      throws Exception
  {
    return createResponse(getSectionInfoForCollection.execute(getEntityModel));
  }
}
