package com.cs.ui.config.controller.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.core.config.interactor.usecase.tag.IGetGridTags;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetGridTagsController extends BaseController implements IConfigController {
  
  @Autowired
  private IGetGridTags getGridTags;
  
  @RequestMapping(value = "/tags/grid", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ConfigGetAllRequestModel getAllModel) throws Exception
  {
    return createResponse(getGridTags.execute(getAllModel));
  }
}
