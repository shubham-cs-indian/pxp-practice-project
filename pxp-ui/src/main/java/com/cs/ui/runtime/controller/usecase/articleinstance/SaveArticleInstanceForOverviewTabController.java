package com.cs.ui.runtime.controller.usecase.articleinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.klassinstance.ArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.ISaveArticleInstanceForOverviewTab;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/runtime")
public class SaveArticleInstanceForOverviewTabController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  ISaveArticleInstanceForOverviewTab saveArticleInstanceForOverviewTab;
  
  @RequestMapping(value = "/klassinstances/tabs/overviewtab", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArticleInstanceSaveModel klassInstanceModels,
      @RequestParam(required = false) String versionClassId) throws Exception
  {
    RDBMSLogger.instance().debug("****|START | Save Article Instance For Overview Tab | Usecase|****|****");
    long starTime = System.currentTimeMillis();
    IRESTModel createResponse = createResponse(saveArticleInstanceForOverviewTab.execute(klassInstanceModels));
    RDBMSLogger.instance().debug("Save Article Instance For Overview Tab|REST|" + this.getClass().getSimpleName()
        + "|execute|Save Article Instance For Overview Tab over all time| %d ms", System.currentTimeMillis() - starTime);
    RDBMSLogger.instance().debug("****|END| Save Article Instance For Overview Tab | Usecase|****|****");
    return createResponse;
  }
}
