package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import java.util.Collection;
import java.util.List;

import com.cs.core.config.strategy.usecase.klass.IGetTaxonomyHierarchyIdsStrategy;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.runtime.interactor.model.configuration.IIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IIIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Component
public abstract class AbstractBulkCreateTagTaxonomy<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractCreateConfigInteractor<P, R> {
  
  protected abstract R executeBulkCreateAttributionTaxonomy(
      IListModel<ICreateMasterTaxonomyModel> model) throws Exception;

  @Autowired
  protected IGetTaxonomyHierarchyIdsStrategy getTaxonomyHierarchyIdsStrategy;

  @Override
  protected R executeInternal(P model) throws Exception
  {
    Collection<? extends ICreateMasterTaxonomyModel> list = ((IListModel<ICreateMasterTaxonomyModel>) model)
        .getList();
    for (ICreateMasterTaxonomyModel articleTaxonomyModel : list) {
      String taxonomyId = articleTaxonomyModel.getTaxonomyId();
      if (taxonomyId == null || taxonomyId.equals("")) {
        taxonomyId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAXONOMY.getPrefix());
        articleTaxonomyModel.setTaxonomyId(taxonomyId);
        
        if (StringUtils.isEmpty(articleTaxonomyModel.getCode())) {
          articleTaxonomyModel.setCode(articleTaxonomyModel.getTaxonomyId());
        }
        IIIDsListModel idslist;
        if (!articleTaxonomyModel.getParentTaxonomyId().equals("-1")) {
          idslist = getTaxonomyHierarchyIdsStrategy.execute(new IdLabelCodeModel(articleTaxonomyModel.getParentTaxonomyId()));
        } else {
          idslist = new IIDsListModel();
          idslist.setIids(List.of(-1l));
        }
        IClassifierDTO createClassifier = ConfigurationDAO.instance()
            .createTaxonomyClassifier(articleTaxonomyModel.getCode(), ClassifierType.TAXONOMY, idslist.getIids());
        articleTaxonomyModel.setClassifierIID(createClassifier.getClassifierIID());
      }
    }
    return (R) executeBulkCreateAttributionTaxonomy((IListModel<ICreateMasterTaxonomyModel>) model);
  }
}
