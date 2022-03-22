package com.cs.ui.runtime.controller.usecase.taskinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetArticleInstanceForTasksTab;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping(value = "/runtime")
public class GetInstanceForTaskTabController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetArticleInstanceForTasksTab getArticleInstanceForTasksTab;
  
  @RequestMapping(value = "/klassinstances/tasktab/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id,@RequestParam(required = false, defaultValue = "false")  Boolean getAll,
      @RequestParam(required = false, defaultValue = "false") Boolean count,
      @RequestBody GetInstanceRequestModel model) throws Exception
  {
    model.setIsGetAll(getAll);
    model.setId(id);
    return createResponse(getArticleInstanceForTasksTab.execute(model));
  }
}
