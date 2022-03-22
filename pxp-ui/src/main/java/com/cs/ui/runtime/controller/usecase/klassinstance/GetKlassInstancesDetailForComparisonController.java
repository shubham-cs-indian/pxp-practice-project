package com.cs.ui.runtime.controller.usecase.klassinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.klassinstance.GetInstancesDetailForComparisonModel;
import com.cs.pim.runtime.strategy.usecase.articleinstance.IGetKlassInstancesDetailsForComparison;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetKlassInstancesDetailForComparisonController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetKlassInstancesDetailsForComparison getKlassInstancesDetailForComparison;
  
  @RequestMapping(value = "/klassinstancescomparison", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetInstancesDetailForComparisonModel model) throws Exception
  {
    return createResponse(getKlassInstancesDetailForComparison.execute(model));
  }
}
