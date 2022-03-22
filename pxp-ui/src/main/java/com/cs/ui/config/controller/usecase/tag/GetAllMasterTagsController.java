package com.cs.ui.config.controller.usecase.tag;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.tag.IGetAllMasterTags;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllMasterTagsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetAllMasterTags getAllMasterTags;
  
  @RequestMapping(value = "/mastertags", method = RequestMethod.GET)
  public IRESTModel getTagTypes() throws Exception
  {
    return createResponse(getAllMasterTags.execute(new IdParameterModel()));
  }
}
