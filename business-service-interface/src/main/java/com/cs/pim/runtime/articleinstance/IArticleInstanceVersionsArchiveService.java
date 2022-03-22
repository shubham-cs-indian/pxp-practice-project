package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;

public interface IArticleInstanceVersionsArchiveService extends
  IRuntimeService<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel> {
  
}

