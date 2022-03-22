package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import org.springframework.stereotype.Component;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Component
public abstract class AbstractCreateTagTaxonomy<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractCreateConfigInteractor<P, R> {
  
  protected abstract R executeCreateArticleTaxonomy(ICreateMasterTaxonomyModel model)
      throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    String taxonomyId = ((ICreateMasterTaxonomyModel) model).getTaxonomyId();
    if (taxonomyId == null || taxonomyId.equals("")) {
      taxonomyId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAXONOMY.getPrefix());
      ((ICreateMasterTaxonomyModel) model).setTaxonomyId(taxonomyId);
    }
    return (R) executeCreateArticleTaxonomy((ICreateMasterTaxonomyModel) model);
  }
}
