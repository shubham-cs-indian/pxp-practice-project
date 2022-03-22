package com.cs.ui.runtime.controller.usecase.goldenrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.goldenrecord.bucket.IGetGoldenRecordMatchedInstances;
import com.cs.core.runtime.interactor.model.goldenrecord.GetBucketInstancesRequestModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetGoldenRecordMatchedInstancesController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetGoldenRecordMatchedInstances getGoldenRecordMatchedInstances;
  
  @RequestMapping(value = "/getgoldenrecordmatchedinstances", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetBucketInstancesRequestModel requestModel) throws Exception
  {
    return createResponse(getGoldenRecordMatchedInstances.execute(requestModel));
  }
}