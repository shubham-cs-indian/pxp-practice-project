package com.cs.core.config.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.ICreateMasterTaxonomyStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetTaxonomyHierarchyIdsStrategy;
import com.cs.core.config.taxonomy.TaxonomyValidations;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.runtime.defaultklassinstance.ICreateDefaultInstanceService;
import com.cs.core.runtime.interactor.model.configuration.IIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IIIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.TypesTaxonomiesModel;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateMasterTaxonomyService
    extends AbstractCreateTagTaxonomy<ICreateMasterTaxonomyModel, IGetMasterTaxonomyWithoutKPModel>
    implements ICreateMasterTaxonomyService {
  
  @Autowired
  protected ICreateMasterTaxonomyStrategy createMasterTaxonomyStrategy;

  @Autowired
  protected IGetTaxonomyHierarchyIdsStrategy getTaxonomyHierarchyIdsStrategy;
  
  @Autowired
  protected ICreateDefaultInstanceService    createDefaultInstanceService;
  
  @Override
  protected IGetMasterTaxonomyWithoutKPModel executeInternal(ICreateMasterTaxonomyModel model)
      throws Exception
  {
    return super.executeInternal(model);
  }
  
  @Override
  protected IGetMasterTaxonomyWithoutKPModel executeCreateArticleTaxonomy(
      ICreateMasterTaxonomyModel articleTaxonomyModel) throws Exception
  {
    if (StringUtils.isEmpty(articleTaxonomyModel.getCode())) {
      articleTaxonomyModel.setCode(articleTaxonomyModel.getTaxonomyId());
    }

    String parentCode = articleTaxonomyModel.getParentTaxonomyId();
    TaxonomyValidations.validate(articleTaxonomyModel);
    IIIDsListModel idslist;
    if (!parentCode.equals("-1")) {
      idslist = getTaxonomyHierarchyIdsStrategy.execute(new IdLabelCodeModel(parentCode));
    } else {
      idslist = new IIDsListModel();
      idslist.setIids(List.of(-1L));
    }
    IClassifierDTO createClassifier = ConfigurationDAO.instance()
        .createTaxonomyClassifier(articleTaxonomyModel.getCode(), ClassifierType.TAXONOMY, idslist.getIids());
    articleTaxonomyModel.setClassifierIID(createClassifier.getClassifierIID());
    IGetMasterTaxonomyWithoutKPModel execute = createMasterTaxonomyStrategy.execute(articleTaxonomyModel);
    
    ITypesTaxonomiesModel typesTaxonomiesModel = new TypesTaxonomiesModel();
    typesTaxonomiesModel.getTaxonomyIds().add(articleTaxonomyModel.getCode());
    createDefaultInstanceService.execute(typesTaxonomiesModel);
    
    return execute;
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.CREATEATTRIBUTIONTAXONOMY;
  }
}
