package com.cs.dam.runtime.assetinstance;

import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveReadPermissionForAsset;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.klassinstance.AbstractGetInstanceForCustomTabService;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceCustomTabStrategy;
import com.cs.dam.runtime.util.AssetInstanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAssetInstanceForCustomTabService extends AbstractGetInstanceForCustomTabService<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel>
    implements IGetAssetInstanceForCustomTabService {
  
  @Autowired
  protected IGetConfigDetailsForAssetInstanceCustomTabStrategy getConfigDetailsForAssetInstanceCustomTab;
  
  @Override
  protected IGetKlassInstanceCustomTabModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    try {
      return super.executeInternal(getKlassInstanceTreeStrategyModel);
    }
    catch (UserNotHaveReadPermission e) {
      throw new UserNotHaveReadPermissionForAsset();
    }
    catch (KlassNotFoundException e) { // TODO: handle this exception from
      // orient db plugin
      throw new AssetKlassNotFoundException();
    }
  }
  
  @Override
  protected IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel model) throws Exception
  {
    return getConfigDetailsForAssetInstanceCustomTab.execute(model);
  }
  
  @Override
  protected IGetKlassInstanceCustomTabModel executeGetKlassInstance(
      IGetInstanceRequestModel instanceRequestModel,
      IGetConfigDetailsForCustomTabModel configDetails, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    IGetKlassInstanceCustomTabModel executeGetKlassInstance = super.executeGetKlassInstance(
        instanceRequestModel, configDetails, baseEntityDAO);
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
    IAssetInstance assetInstance = (IAssetInstance) executeGetKlassInstance.getKlassInstance();
    AssetInstanceUtils.fillEntityExtensionInAssetCoverFlowAttribute(assetInstance, baseEntityDTO,
        entityExtension);
    return executeGetKlassInstance;
  }
}
