package com.cs.ui.runtime.controller.usecase.articleinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.usecase.instance.IGetInstanceTree;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/runtime")
public class GetInstanceTreeController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetInstanceTree getInstanceTreePostgre;
  
  @PostMapping(value = "/instancetree/{id}")
  public IRESTModel execute(@PathVariable String id,
      @RequestParam(required = false, defaultValue = "false") Boolean isLoadMore,
      @RequestParam(required = false, defaultValue = "false") Boolean getAllChildren,
      @RequestBody GetKlassInstanceTreeStrategyModel model) throws Exception
  {
    RDBMSLogger.instance()
        .debug("****|START | Get All Entity | Usecase|****|****");
    long starTime = System.currentTimeMillis();
    model.setId(id);
    model.setIsLoadMore(isLoadMore);
    model.setGetAllChildren(getAllChildren);
    IRESTModel createResponse = createResponse(getInstanceTreePostgre.execute(model));
    RDBMSLogger.instance()
        .debug("Get All Entity|REST|" + this.getClass()
            .getSimpleName() + "|execute|Get All Entity over all time| %d ms",
            System.currentTimeMillis() - starTime);
    RDBMSLogger.instance()
        .debug("****|END| Get All Entity | Usecase|****|****");
    return createResponse;
  }
}
