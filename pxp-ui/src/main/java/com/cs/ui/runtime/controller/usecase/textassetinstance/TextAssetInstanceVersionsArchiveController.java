package com.cs.ui.runtime.controller.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.model.version.MoveKlassInstanceVersionsModel;
import com.cs.pim.runtime.interactor.usecase.textassetinstance.ITextAssetInstanceVersionsArchive;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class TextAssetInstanceVersionsArchiveController extends BaseController implements IRuntimeController {
  
  @Autowired
  ITextAssetInstanceVersionsArchive textAssetInstanceVersionsArchive;
  
  @RequestMapping(value = "/textassetinstances/versionsarchive/{id}", method = RequestMethod.POST)
  public IMoveKlassInstanceVersionsSuccessModel execute(@PathVariable String id, @RequestBody MoveKlassInstanceVersionsModel model)
      throws Exception
  {
    model.setInstanceId(id);
    
    return textAssetInstanceVersionsArchive.execute(model);
  }
}