package com.cs.ui.config.controller.usecase.configdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.strategy.usecase.tag.IGetTagValuesByTagIdsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetConfigTagValuesDataController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetTagValuesByTagIdsStrategy getConfigData;
  
  @RequestMapping(value = "/configdata/tagValues", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdsListParameterModel model) throws Exception
  {
    return createResponse(getConfigData.execute(model));
  }
}
