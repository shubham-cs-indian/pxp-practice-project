package com.cs.pim.runtime.interactor.version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractRestoreInstanceVersions;
import com.cs.pim.runtime.articleinstance.IRestoreArticleInstanceVersionsService;

@Service
public class RestoreArticleInstanceVersions extends AbstractRestoreInstanceVersions<IMoveKlassInstanceVersionsModel,IMoveKlassInstanceVersionsSuccessModel>
    implements IRestoreArticleInstanceVersions {
  
  @Autowired
  protected IRestoreArticleInstanceVersionsService restoreArticleInstanceVersionsService;

  @Override
  public IMoveKlassInstanceVersionsSuccessModel executeInternal(IMoveKlassInstanceVersionsModel dataModel) throws Exception
  {
    return restoreArticleInstanceVersionsService.execute(dataModel);
  }

}
