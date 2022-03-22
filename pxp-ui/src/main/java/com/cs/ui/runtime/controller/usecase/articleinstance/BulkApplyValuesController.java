package com.cs.ui.runtime.controller.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.BulkApplyValueRequestModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IBulkApplyValues;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping(value = "/runtime")
public class BulkApplyValuesController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IBulkApplyValues bulkApplyValues;
  
  @RequestMapping(value = "/bulkapplyvalues", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody BulkApplyValueRequestModel requestModel) throws Exception
  {
    return createResponse(bulkApplyValues.execute(requestModel));
  }
}
