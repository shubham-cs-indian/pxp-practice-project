package com.cs.ui.config.controller.usecase.tag;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.tag.AbstractTagModel;
import com.cs.core.config.interactor.usecase.tag.ICreateTag;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateTagController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateTag createTag;
  
  @RequestMapping(value = "/tags", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody AbstractTagModel dataTransferModel) throws Exception
  {
    
    return createResponse(createTag.execute(dataTransferModel).getTagResponseModel().get(0));
  }
}
