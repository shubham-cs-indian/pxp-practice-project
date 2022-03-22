package com.cs.ui.runtime.controller.usecase.contentgrid;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.gridcontent.GetContentGridRequestModel;
import com.cs.core.runtime.interactor.usecase.contentgrid.IGetContentGridView;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class GetContentGridViewController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetContentGridView getContentGrid;
  
  @RequestMapping(value = "/contentgrid", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetContentGridRequestModel requestModel) throws Exception
  {
    return createResponse(getContentGrid.execute(requestModel));
  }
}
