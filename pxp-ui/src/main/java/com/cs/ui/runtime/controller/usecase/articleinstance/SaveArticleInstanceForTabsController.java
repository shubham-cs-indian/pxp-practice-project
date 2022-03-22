package com.cs.ui.runtime.controller.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.klassinstance.ArticleInstanceSaveModel;
import com.cs.pim.runtime.strategy.usecase.articleinstance.ISaveArticleInstanceForTabs;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class SaveArticleInstanceForTabsController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  ISaveArticleInstanceForTabs saveArticleInstanceForTabs;
  
  @RequestMapping(value = "/klassinstances/tabs", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArticleInstanceSaveModel klassInstanceModels,
      @RequestParam(required = false) String versionClassId) throws Exception
  {
    RDBMSLogger.instance()
        .debug("****|START | Save Article Instance For custom Tab | Usecase|****|****");
    long starTime = System.currentTimeMillis();
    IRESTModel createResponse = createResponse(
        saveArticleInstanceForTabs.execute(klassInstanceModels));
    RDBMSLogger.instance()
        .debug("Save Article Instance For custom Tab|REST|" + this.getClass()
            .getSimpleName() + "|execute|Save Article Instance For custom Tab over all time| %d ms",
            System.currentTimeMillis() - starTime);
    RDBMSLogger.instance()
        .debug("****|END| Save Article Instance For custom Tab | Usecase|****|****");
    return createResponse;
  }
}
