package com.cs.dam.runtime.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.taskinstance.GetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForTasksTabModel;
import com.cs.core.runtime.klassinstance.AbstractGetInstanceForTaskTabService;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceTasksTabStrategy;
import com.cs.dam.runtime.util.AssetInstanceUtils;

@Service
public class GetAssetInstanceForTasksTabService extends AbstractGetInstanceForTaskTabService<IGetInstanceRequestModel, IGetTaskInstanceResponseModel>
    implements IGetAssetInstanceForTasksTabService {
  
  /*@Autowired
  protected IGetAssetInstanceForTasksTabStrategy              getAssetInstanceForTasksTabStrategy;
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy                     getAssetInstanceTypeStrategy;
  
   */
  @Autowired
  protected IGetConfigDetailsForAssetInstanceTasksTabStrategy getConfigDetailsForAssetInstanceTasksTab;
  
  @Override
  protected IGetTaskInstanceResponseModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    try {
      return super.executeInternal(getKlassInstanceTreeStrategyModel);
    }
    catch (KlassNotFoundException e) { 
      throw new AssetKlassNotFoundException(e);
    }
    
  }
  
  /*@Override
  protected IGetTaskInstanceResponseModel executeGetKlassInstance(
      IGetInstanceRequestStrategyModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    return getAssetInstanceForTasksTabStrategy.execute(getKlassInstanceTreeStrategyModel);
  }
  
  @Override
  protected IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel(id);
    return getAssetInstanceTypeStrategy.execute(idParameterModel);
  }
*/
  
  @Override
  protected IGetConfigDetailsForTasksTabModel getConfigDetails(
      IMulticlassificationRequestModel model) throws Exception
  {
    return getConfigDetailsForAssetInstanceTasksTab.execute(model);
  }

  
  @Override
  protected IGetTaskInstanceResponseModel executeGetKlassInstance(
      IGetInstanceRequestStrategyModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    IGetTaskInstanceResponseModel getTaskInstanceResponseModel = new GetTaskInstanceResponseModel();
    return getTaskInstanceResponseModel;
  }
  
  @Override
  protected IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception
  {
    IKlassInstanceTypeModel klassInstanceTypeModel = new KlassInstanceTypeModel();
    IIdParameterModel idParameterModel = new IdParameterModel(id);
    return klassInstanceTypeModel;
  }

  @Override
  protected void getKlassInstance(IGetInstanceRequestModel getKlassInstanceTreeStrategyModel,
      IGetConfigDetailsForTasksTabModel configDetails, IBaseEntityDAO baseEntityDAO,
      IGetTaskInstanceResponseModel returnModel) throws Exception
  {
    super.getKlassInstance(getKlassInstanceTreeStrategyModel, configDetails, baseEntityDAO, returnModel);
    IAssetInstance assetInstance = (IAssetInstance) returnModel.getKlassInstance();
    returnModel.getReferencedElements();
    
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    AssetInstanceUtils.fillEntityExtensionInAssetCoverFlowAttribute(assetInstance, baseEntityDTO, baseEntityDTO.getEntityExtension());
  }
 
}
