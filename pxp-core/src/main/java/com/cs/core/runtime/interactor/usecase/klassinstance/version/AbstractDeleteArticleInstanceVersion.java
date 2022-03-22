package com.cs.core.runtime.interactor.usecase.klassinstance.version;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.model.version.MoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

public abstract class AbstractDeleteArticleInstanceVersion<P extends IMoveKlassInstanceVersionsModel, R extends IMoveKlassInstanceVersionsSuccessModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected RDBMSComponentUtils                                          rdbmsComponentUtils;
  
  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P dataModel) throws Exception
  {
    IRevisionDAO revisionDAO = rdbmsComponentUtils.getRevisionDAO();
    revisionDAO.deleteBaseEntityRevisions(Long.parseLong(dataModel.getInstanceId()), dataModel.getVersionNumbers(), dataModel.getIsDeleteFromArchival());
    IMoveKlassInstanceVersionsSuccessModel successModel = new MoveKlassInstanceVersionsSuccessModel();
    successModel.setVersionNumbers(dataModel.getVersionNumbers());
    
    return (R) successModel;
  }
}
