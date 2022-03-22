package com.cs.dam.runtime.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.klassinstance.AbstractSaveRelationshipInstances;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceCustomTabStrategy;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceOverviewTabStrategy;
import com.cs.dam.runtime.util.AssetInstanceUtils;

@Service
public class SaveAssetInstanceRelationshipsService
    extends AbstractSaveRelationshipInstances<ISaveRelationshipInstanceModel, IGetKlassInstanceModel>
    implements ISaveAssetInstanceRelationshipsService {
  
  @Autowired
  IGetConfigDetailsForAssetInstanceCustomTabStrategy   getConfigDetailsForAssetInstanceCustomTabStrategy;
  
  @Autowired
  IGetConfigDetailsForAssetInstanceOverviewTabStrategy getConfigDetailsForAssetInstanceOverviewTabStrategy;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISaveRelationshipInstanceModel klassInstancesModel) throws Exception
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
  protected IGetConfigDetailsForCustomTabModel getEntitySpecificConfigDetailsForResponse(ISaveRelationshipInstanceModel dataModel,
      IMulticlassificationRequestModel multiclassificationRequestModel) throws Exception
  {
    if (dataModel.getTabId().equals(SystemLevelIds.OVERVIEW_TAB)) {
      return getConfigDetailsForAssetInstanceOverviewTabStrategy.execute(multiclassificationRequestModel);
    }

    return getConfigDetailsForAssetInstanceCustomTabStrategy.execute(multiclassificationRequestModel);
  }
  
  @Override
  protected IGetKlassInstanceModel executeGetKlassInstance(IBaseEntityDAO baseEntityDAO, IGetConfigDetailsModel configDetails,
      String tabType) throws Exception
  {
    IGetKlassInstanceModel returnModel = super.executeGetKlassInstance(baseEntityDAO, configDetails, tabType);
    IAssetInstance assetInstance = (IAssetInstance) returnModel.getKlassInstance();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
    AssetInstanceUtils.fillEntityExtensionInAssetCoverFlowAttribute(assetInstance, baseEntityDTO, entityExtension);
    return returnModel;
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.SAVEASSETRELATIONSHIP;
  }
  
}
