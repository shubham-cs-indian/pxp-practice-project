package com.cs.ui.runtime.controller.usecase.goldenrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.goldenrecord.IdBucketIdModel;
import com.cs.core.runtime.interactor.usecase.goldenrecord.IGetSourceInfoForBucket;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetSourceInfoForBucketController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetSourceInfoForBucket getSourceInfoForBucket;
  
  @RequestMapping(value = "/getsourceinfoforbucket", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdBucketIdModel requestModel) throws Exception
  {
    return createResponse(getSourceInfoForBucket.execute(requestModel));
  }
}
