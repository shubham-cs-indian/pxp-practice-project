package com.cs.ui.runtime.controller.usecase.goldenrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.goldenrecord.SaveGoldenRecordRequestModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ISaveGoldenRecord;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class SaveGoldenRecordController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected ISaveGoldenRecord saveGoldenRecord;
  
  @RequestMapping(value = "/goldenrecord", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveGoldenRecordRequestModel klassInstanceModel)
      throws Exception
  {
    return createResponse(saveGoldenRecord.execute(klassInstanceModel));
  }
}
