package com.cs.core.config.attributiontaxonomy;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.attributiontaxonomy.*;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.klass.IGetTaxonomyHierarchyIdsStrategy;
import com.cs.core.config.taxonomy.TaxonomyValidations;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.runtime.defaultklassinstance.ICreateDefaultInstanceService;
import com.cs.core.runtime.interactor.model.configuration.IIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IIIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.TypesTaxonomiesModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract class AbstractBulkCreateTagTaxonomy<P extends IModel, R extends IBulkCreateTaxonomyResponseModel>
    extends AbstractCreateConfigService<P, R> {
  
  protected abstract R executeBulkCreateAttributionTaxonomy(
      IListModel<ICreateMasterTaxonomyModel> model) throws Exception;

  @Autowired
  protected IGetTaxonomyHierarchyIdsStrategy getTaxonomyHierarchyIdsStrategy;
  
  @Autowired
  protected ICreateDefaultInstanceService    createDefaultInstanceService;

  @Override
  protected R executeInternal(P model) throws Exception
  {
    R returnModel = (R) new BulkCreateTaxonomyResponseModel();
    Collection<? extends ICreateMasterTaxonomyModel> list = ((IListModel<ICreateMasterTaxonomyModel>) model).getList();
    List<String> taxonomyList = new ArrayList<>();
    List<ICreateMasterTaxonomyModel> validTaxonomy = new ArrayList<>();
    for (ICreateMasterTaxonomyModel articleTaxonomyModel : list) {
      try {
        String taxonomyId = articleTaxonomyModel.getTaxonomyId();
        if (taxonomyId == null || taxonomyId.equals("")) {
          taxonomyId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAXONOMY.getPrefix());
          articleTaxonomyModel.setTaxonomyId(taxonomyId);

          if (StringUtils.isEmpty(articleTaxonomyModel.getCode())) {
            if(StringUtils.isEmpty(articleTaxonomyModel.getTagValueId())){
              articleTaxonomyModel.setCode(articleTaxonomyModel.getTaxonomyId());
            }
            else{
              articleTaxonomyModel.setCode(articleTaxonomyModel.getTagValueId());
            }
          }
          TaxonomyValidations.validate(articleTaxonomyModel);
          IIIDsListModel idslist;
          if (!articleTaxonomyModel.getParentTaxonomyId().equals("-1")) {
            idslist = getTaxonomyHierarchyIdsStrategy.execute(new IdLabelCodeModel(articleTaxonomyModel.getParentTaxonomyId()));
          }
          else {
            idslist = new IIDsListModel();
            idslist.setIids(List.of(-1L));
          }
          IClassifierDTO createClassifier = ConfigurationDAO.instance()
              .createTaxonomyClassifier(articleTaxonomyModel.getCode(), ClassifierType.TAXONOMY, idslist.getIids());
          articleTaxonomyModel.setClassifierIID(createClassifier.getClassifierIID());
          taxonomyList.add(articleTaxonomyModel.getCode());
        }
        // Adding tag value in RDBMS pxp.tagvalueconfig
        String parentId = articleTaxonomyModel.getParentTagId();
        String code = articleTaxonomyModel.getCode();
        RDBMSUtils.createTagValue(code, parentId);
        validTaxonomy.add(articleTaxonomyModel);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(returnModel.getFailure(), e, null, null);
      }
    }

    ((IListModel<ICreateMasterTaxonomyModel>) model).setList(validTaxonomy);
    R bulkCreate = executeBulkCreateAttributionTaxonomy((IListModel<ICreateMasterTaxonomyModel>) model);
    bulkCreate.getFailure().getExceptionDetails().addAll(returnModel.getFailure().getExceptionDetails());
    bulkCreate.getFailure().getDevExceptionDetails().addAll(returnModel.getFailure().getDevExceptionDetails());

    ITypesTaxonomiesModel typesTaxonomiesModel = new TypesTaxonomiesModel();
    IBulkCreateTaxonomyListResponseModel success = (BulkCreateTaxonomyListResponseModel) bulkCreate.getSuccess();
    typesTaxonomiesModel.setTaxonomyIds(success.getList().stream().map(x-> x.getEntity().getId()).collect(Collectors.toList()));
    createDefaultInstanceService.execute(typesTaxonomiesModel);
    return  bulkCreate;
  }
}
