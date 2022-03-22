package com.cs.dam.runtime.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.abstrct.AbstractCreateTranslatableInstanceService;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceCustomTabStrategy;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceOverviewTabStrategy;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceTasksTabStrategy;
import com.cs.dam.runtime.util.AssetInstanceUtils;

@Component
public class CreateTranslatableAssetInstanceService extends AbstractCreateTranslatableInstanceService<IAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ICreateTranslatableAssetInstanceService {
  
  @Autowired
  protected Long                                                 assetKlassCounter;
  
  @Autowired
  protected IGetConfigDetailsForAssetInstanceOverviewTabStrategy getConfigDetailsForAssetInstanceOverviewTabStrategy;
  
  @Autowired
  protected IGetConfigDetailsForAssetInstanceCustomTabStrategy   getConfigDetailsForAssetInstanceCustomTab;
  
  @Autowired
  protected IGetConfigDetailsForAssetInstanceTasksTabStrategy    getConfigDetailsForAssetInstanceTasksTabStrategy;

  protected IGetKlassInstanceModel executeInternal(IAssetInstanceSaveModel klassInstancesModel) throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new AssetKlassNotFoundException(e);
    }
    return response;
  }
  
  @Override
  protected IGetKlassInstanceModel executeGetKlassInstance(IGetConfigDetailsModel configDetails,
      IKlassInstanceSaveModel saveInstanceModel, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IGetKlassInstanceModel executeGetKlassInstance = super.executeGetKlassInstance(configDetails,
        saveInstanceModel, baseEntityDAO);
    
    AssetInstanceUtils.fillAssetInformation(executeGetKlassInstance, baseEntityDAO);
    return executeGetKlassInstance;
  }
  
  @Override
  public IGetConfigDetailsModel getConfigDetails(IMulticlassificationRequestModel model, IKlassInstanceSaveModel saveModel)
      throws Exception
  {
    switch (saveModel.getTabType()) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        String tabId = saveModel.getTabId();
        if (tabId.equals(SystemLevelIds.OVERVIEW_TAB)) {
          return getConfigDetailsForAssetInstanceOverviewTabStrategy.execute(model);
        }
        else {
          return getConfigDetailsForAssetInstanceCustomTab.execute(model);
        }
      case CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE:
        return getConfigDetailsForAssetInstanceTasksTabStrategy.execute(model);
      case CommonConstants.TEMPLATE_TIME_LINE_TAB_BASETYPE:
        return getConfigDetailsForAssetInstanceCustomTab.execute(model);
      default:
        return null;
    }
  }
  
  @Override
  protected Long getCounter()
  {
    return assetKlassCounter++;
  }

  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.SAVEASSET;
  }
}