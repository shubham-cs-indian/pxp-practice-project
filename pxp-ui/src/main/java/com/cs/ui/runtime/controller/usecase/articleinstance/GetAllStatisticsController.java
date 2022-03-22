package com.cs.ui.runtime.controller.usecase.articleinstance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.statistics.GetAllStatisticsRequestModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetAllStatistics;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAllStatisticsController extends BaseController implements IRuntimeController {

  @Autowired
  protected IGetAllStatistics getAllStatistics;

  @RequestMapping(value = "/getallstatistics", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetAllStatisticsRequestModel model) throws Exception 
  {
    return createResponse(getAllStatistics.execute(model));
  }
}