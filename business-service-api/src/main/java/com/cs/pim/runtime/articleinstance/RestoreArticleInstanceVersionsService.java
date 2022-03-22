package com.cs.pim.runtime.articleinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.abstrct.versions.AbstractRestoreInstanceVersionsService;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;

@Service
public class RestoreArticleInstanceVersionsService
    extends AbstractRestoreInstanceVersionsService<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements IRestoreArticleInstanceVersionsService {
  
  @Override
  public IMoveKlassInstanceVersionsSuccessModel executeInternal(IMoveKlassInstanceVersionsModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
}