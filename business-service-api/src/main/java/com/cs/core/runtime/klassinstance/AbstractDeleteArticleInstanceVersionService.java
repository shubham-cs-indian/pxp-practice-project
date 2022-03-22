package com.cs.core.runtime.klassinstance;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.model.version.MoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

public abstract class AbstractDeleteArticleInstanceVersionService<P extends IMoveKlassInstanceVersionsModel, R extends IMoveKlassInstanceVersionsSuccessModel>
    extends AbstractRuntimeService<P, R> {
  
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
