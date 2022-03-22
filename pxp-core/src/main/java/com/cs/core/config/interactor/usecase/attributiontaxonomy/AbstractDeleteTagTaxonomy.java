package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import java.util.ArrayList;
import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.runtime.interactor.exception.delete.InstanceExistsForTaxonomyException;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

public abstract class AbstractDeleteTagTaxonomy<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractDeleteConfigInteractor<P, R> {
  
  protected abstract IBulkDeleteReturnModel executeDeleteAttributionTaxonomy(
      IIdsListParameterModel p) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    List<String> taxonomyCodes = new ArrayList<String>();
    taxonomyCodes.addAll(((IIdsListParameterModel) model).getIds());
    List<Long> classifierIIDs = new ArrayList<>();
    for(String taxonomy :taxonomyCodes) {
      classifierIIDs.add( ConfigurationDAO.instance().getClassifierByCode(taxonomy).getClassifierIID());
    }
    
    if (!classifierIIDs.isEmpty()) {
      List<Long> baseentityiid = RDBMSUtils.getBaseentityIIDsForTaxonomies(classifierIIDs);
      if (!baseentityiid.isEmpty()) {
        throw new InstanceExistsForTaxonomyException();
      }
    }
    
    IBulkDeleteReturnModel response = executeDeleteAttributionTaxonomy(
        (IIdsListParameterModel) model);
    
    IExceptionModel failure = response.getFailure();
    // TODO: Need to get this from rdbms call in future
    /*taxonomyIdsHavingInstances
    .forEach(typeId -> ExceptionUtil.addFailureDetailsToFailureObject(failure,
        new InstanceExistsForTaxonomyException(), typeId, null));*/
    
    return (R) response;
  }
}
