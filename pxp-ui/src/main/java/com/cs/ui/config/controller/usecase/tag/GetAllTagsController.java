package com.cs.ui.config.controller.usecase.tag;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.tag.IGetAllTags;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllTagsController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAllTags getAllTag;
  
  @RequestMapping(value = "/tags", method = RequestMethod.GET)
  public IRESTModel getTag() throws Exception
  {
    return createResponse(getAllTag.execute(null));
  }
}
