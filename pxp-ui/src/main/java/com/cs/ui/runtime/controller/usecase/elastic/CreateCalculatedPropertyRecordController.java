package com.cs.ui.runtime.controller.usecase.elastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.elastic.DocumentRegenerationModel;
import com.cs.core.runtime.interactor.usecase.elastic.ICreateCalculatedPropertyRecord;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class CreateCalculatedPropertyRecordController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected ICreateCalculatedPropertyRecord createCalculatedPropertyRecord;
  
  @RequestMapping(value = "/calcpropertyrecord", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody DocumentRegenerationModel model) throws Exception
  {
    return createResponse(createCalculatedPropertyRecord.execute(model));
  }
}
