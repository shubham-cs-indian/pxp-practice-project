package com.cs.dam.runtime.assetinstance;

import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveReadPermissionForAsset;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IRowIdParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.klassinstance.AbstractNewGetVariantInstancesInTableViewService;
import com.cs.dam.runtime.util.AssetInstanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAssetVariantInstancesInTableviewService extends AbstractNewGetVariantInstancesInTableViewService<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel>
    implements IGetAssetVariantInstancesInTableViewService {
  
  @Autowired
  RDBMSComponentUtils rdbmsComponentUtils;
  
  @Override
  protected IGetVariantInstancesInTableViewModel executeInternal(
      IGetVariantInstanceInTableViewRequestModel getAssetInstanceTreeStrategyModel) throws Exception
  {
    try {
      IGetVariantInstancesInTableViewModel executeInternal = super.executeInternal(getAssetInstanceTreeStrategyModel);
        for(IRowIdParameterModel row : executeInternal.getRows()) {
          String id = row.getId();
          IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(Long.parseLong(id));
        IImageAttributeInstance imageAttributeInstance = AssetInstanceUtils
            .fillImageAttributeInstance(baseEntityDTO);
        if (imageAttributeInstance != null) {
          IAssetInformationModel assetInformation = fillAssetInformation(imageAttributeInstance);
          row.setAssetInformation(assetInformation);
        }
      }
      return executeInternal;
    }
    catch (UserNotHaveReadPermission e) {
      throw new UserNotHaveReadPermissionForAsset();
    }
    catch (KlassNotFoundException e) {
      throw new AssetKlassNotFoundException();
    }
  }

  private IAssetInformationModel fillAssetInformation(
      IImageAttributeInstance imageAttributeInstance)
  {
    IAssetInformationModel assetInformation = new AssetInformationModel();
    assetInformation.setAssetObjectKey(imageAttributeInstance.getAssetObjectKey());
    assetInformation.setFileName(imageAttributeInstance.getFileName());
    assetInformation.setHash(imageAttributeInstance.getHash());
    assetInformation.setLastModified(imageAttributeInstance.getLastModified());
    assetInformation.setPreviewImageKey(imageAttributeInstance.getPreviewImageKey());
    assetInformation.setProperties(imageAttributeInstance.getProperties());
    assetInformation.setThumbKey(imageAttributeInstance.getThumbKey());
    assetInformation.setType(imageAttributeInstance.getType());
    return assetInformation;
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.ASSET_INSTANCE_BASE_TYPE;
  }
  
  @Override
  protected String getModuleId()
  {
    return Constants.MAM_MODULE;
  }
  
}
