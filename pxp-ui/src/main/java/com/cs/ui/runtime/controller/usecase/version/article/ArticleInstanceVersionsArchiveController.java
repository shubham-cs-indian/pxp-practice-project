package com.cs.ui.runtime.controller.usecase.version.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.model.version.MoveKlassInstanceVersionsModel;
import com.cs.pim.runtime.interactor.version.IArticleInstanceVersionsArchive;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class ArticleInstanceVersionsArchiveController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IArticleInstanceVersionsArchive articleInstanceVersionsArchive;
  
  @RequestMapping(value = "/articleinstances/versionsarchive/{id}", method = RequestMethod.POST)
    public IMoveKlassInstanceVersionsSuccessModel execute(@PathVariable String id,
	    @RequestBody MoveKlassInstanceVersionsModel model) throws Exception
  {
    model.setInstanceId(id);
    
    return articleInstanceVersionsArchive.execute(model);
  }
}
