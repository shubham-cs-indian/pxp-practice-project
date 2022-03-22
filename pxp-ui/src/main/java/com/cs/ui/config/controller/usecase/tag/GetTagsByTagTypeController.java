package com.cs.ui.config.controller.usecase.tag;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.tag.IGetTagsListByTagType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetTagsByTagTypeController extends BaseController implements IConfigController {
  
  @Autowired
  IGetTagsListByTagType getTagsListByTagType;
  
  @RequestMapping(value = "/tagListByTagType/{tagType}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String tagType) throws Exception
  {
    IIdParameterModel model = new IdParameterModel();
    model.setType(tagType);
    return createResponse(getTagsListByTagType.execute(model));
  }
}
