package com.cs.ui.config.controller.usecase.configdata;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.tag.GetTagTagValuesRequestModel;
import com.cs.core.config.interactor.usecase.tagtype.IGetTagTagValuesInteractor;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetTagTagValuesController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetTagTagValuesInteractor getTagTagValuesInteractor;
  
  @RequestMapping(value = "/gettagtagvalues", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetTagTagValuesRequestModel model) throws Exception
  {
    return createResponse(getTagTagValuesInteractor.execute(model));
  }
}
