package com.cs.ui.runtime.controller.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetStatisticsForContent;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetStatisticsForContentController extends BaseController implements IRuntimeController {

  @Autowired
  protected IGetStatisticsForContent getStatisticsForContent;

  @RequestMapping(value = "/getstatisticsforcontent", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdAndTypeModel model) throws Exception 
  {
    return createResponse(getStatisticsForContent.execute(model));
  }
}