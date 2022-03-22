package com.cs.pim.runtime.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForOverviewTabStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.klassinstance.AbstractSaveInstanceForTabs;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;

@Service
public class SaveArticleInstanceForOverviewTabService
    extends AbstractSaveInstanceForTabs<IArticleInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveArticleInstanceForOverviewTabService {
  
  @Autowired
  protected IGetConfigDetailsForOverviewTabStrategy getConfigDetailsForOverviewTabStrategy;
  
  @Autowired
  protected VariantInstanceUtils                    variantInstanceUtils;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IArticleInstanceSaveModel klassInstancesModel)
      throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException(e);
    }
    return response;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails(IMulticlassificationRequestModel model,
      IKlassInstanceSaveModel saveModel) throws Exception
  {
    switch (saveModel.getTabType()) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        return (IGetConfigDetailsForCustomTabModel) getConfigDetailsForOverviewTabStrategy
            .execute(model);
      default:
        return super.getConfigDetails(model, saveModel);
    }
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.SAVEARTIKLE;
  }
  
  @Override
  protected IGetKlassInstanceModel executeGetKlassInstance(IGetConfigDetailsModel configDetails,
      IKlassInstanceSaveModel saveInstanceModel, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    return variantInstanceUtils.executeGetKlassInstanceForOverviewTab(configDetails, baseEntityDAO);
  }
}
