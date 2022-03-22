package com.cs.ui.runtime.controller.usecase.articleinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.IGetArticleInstanceForCustomTab;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/runtime")
public class GetArticleInstanceForCustomTabController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetArticleInstanceForCustomTab getArticleInstanceForCustomTab;
  
  @RequestMapping(value = "/klassinstances/customtab/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id, @RequestBody GetInstanceRequestModel model)
      throws Exception
  {
    RDBMSLogger.instance()
        .debug("****|START | Get Article Instance For Custom Tab | Usecase|****|****");
    long starTime = System.currentTimeMillis();
    model.setId(id);
    IRESTModel createResponse = createResponse(getArticleInstanceForCustomTab.execute(model));
    RDBMSLogger.instance()
        .debug("Get Article Instance For Custom Tab|REST|" + this.getClass()
            .getSimpleName() + "|execute|Get Article Instance For Custom Tab over all time| %d ms",
            System.currentTimeMillis() - starTime);
    RDBMSLogger.instance()
        .debug("****|END| Get Article Instance For Custom Tab | Usecase|****|****");
    return createResponse;
  }
}
