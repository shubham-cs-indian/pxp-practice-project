package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;

public interface IRollbackArticleInstanceVersionService extends IRuntimeService<IKlassInstanceVersionRollbackModel,IGetKlassInstanceModel>{
  
}
