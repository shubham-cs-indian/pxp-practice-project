package com.cs.ui.config.controller.usecase.tag;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.tag.GetTagModel;
import com.cs.core.config.interactor.model.tag.IGetTagModel;
import com.cs.core.config.interactor.usecase.tag.IGetTag;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/config")
public class GetTagController extends BaseController implements IConfigController {
  
  @Autowired
  IGetTag getTag;
  
  @RequestMapping(value = "/tags/{id}", method = RequestMethod.GET)
  public IRESTModel getTag(@PathVariable String id,
      @RequestParam(defaultValue = "all", required = false) String mode) throws Exception
  {
    IGetTagModel dataModel = new GetTagModel(id, mode);
    return createResponse(getTag.execute(dataModel));
  }
}
