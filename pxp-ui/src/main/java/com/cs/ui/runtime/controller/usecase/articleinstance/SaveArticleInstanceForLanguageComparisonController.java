package com.cs.ui.runtime.controller.usecase.articleinstance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.klassinstance.ArticleInstanceSaveModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ISaveArticleInstanceForLanguageComparison;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class SaveArticleInstanceForLanguageComparisonController extends BaseController implements IRuntimeController {
  
  @Autowired
  ISaveArticleInstanceForLanguageComparison saveArticleInstanceForLanguageComparison;
  
  @RequestMapping(value = "/klassinstances/languagecomparison", method = RequestMethod.POST)
  public IRESTModel execute(
      @RequestBody ArticleInstanceSaveModel klassInstanceModels,
      @RequestParam(required=false) String versionClassId) throws Exception
  {
    return createResponse(saveArticleInstanceForLanguageComparison.execute(klassInstanceModels));
  }
}