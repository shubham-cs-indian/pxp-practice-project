
package com.cs.core.config.interactor.usecase.taxonomy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.runtime.interactor.exception.delete.InstanceExistsForTaxonomyException;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Component
public abstract class AbstractDeleteKlassTaxonomy<P extends IIdsListParameterModel, R extends IBulkDeleteReturnModel>
    extends AbstractDeleteConfigInteractor<P, R> {
  
  protected abstract R executeDeleteArticleTaxonomy(P model) throws Exception;
  
  @Autowired
  protected IGetEntityConfigurationStrategy      getTaxonomyEntityConfigurationStrategy;

  @Override
  protected R executeInternal(P model) throws Exception
  {

    IGetEntityConfigurationResponseModel getEntityResponse = getTaxonomyEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(model.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();
    boolean hasChildDependency = getEntityResponse.isHasChildDependency();

    if (!referenceData.keySet()
        .isEmpty() || hasChildDependency) {
      throw new EntityConfigurationDependencyException();
    }

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
    
    IBulkDeleteReturnModel response = executeDeleteArticleTaxonomy(model);
    
    /*IExceptionModel failure = response.getFailure();
    taxonomyIdsHavingInstances.forEach(typeId ->
      ExceptionUtil.addFailureDetailsToFailureObject(failure, new InstanceExistsForTaxonomyException(), typeId, null)
    );*/
    return (R) response;
  }
}
