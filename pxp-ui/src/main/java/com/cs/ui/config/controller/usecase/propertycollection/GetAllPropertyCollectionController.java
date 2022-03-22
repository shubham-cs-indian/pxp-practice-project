package com.cs.ui.config.controller.usecase.propertycollection;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.propertycollection.GetAllPropertyCollectionRequestModel;
import com.cs.core.config.interactor.usecase.propertycollection.IGetAllPropertyCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/config")
public class GetAllPropertyCollectionController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetAllPropertyCollection getAllPropertyCollection;
  
  @RequestMapping(value = "/propertycollections/getall", method = RequestMethod.POST)
  public IRESTModel execute(@RequestParam(required = false) Boolean isForXRay,
      @RequestBody GetAllPropertyCollectionRequestModel model) throws Exception
  {
    model.setIsForXRay(isForXRay);
    return createResponse(getAllPropertyCollection.execute(model));
  }
}
