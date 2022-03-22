package com.cs.ui.runtime.controller.usecase.goldenrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instancetree.GetInstanceTreeRequestModel;
import com.cs.pim.runtime.goldenrecord.IGetGoldenRecordRuleBucketInstances;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetGoldenRecordRuleBucketInstancesController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetGoldenRecordRuleBucketInstances getGoldenRecordRuleBucketInstances;
  
  @RequestMapping(value = "goldenrecordbucketinstances/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetInstanceTreeRequestModel model) throws Exception
  {
    return createResponse(getGoldenRecordRuleBucketInstances.execute(model));
  }
}
