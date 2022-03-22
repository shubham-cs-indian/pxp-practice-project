package com.cs.ui.runtime.controller.usecase.goldenrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.goldenrecord.CreateGoldenRecordRequestModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IAutoCreateGoldenRecord;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class AutoCreateGoldenRecordController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IAutoCreateGoldenRecord autoCreateGoldenRecord;
  
  @RequestMapping(value = "/goldenrecord/auto", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateGoldenRecordRequestModel klassInstanceModel)
      throws Exception
  {
    return createResponse(autoCreateGoldenRecord.execute(klassInstanceModel));
  }
}
