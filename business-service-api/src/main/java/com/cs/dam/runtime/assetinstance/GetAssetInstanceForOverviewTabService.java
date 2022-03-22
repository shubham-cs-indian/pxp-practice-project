package com.cs.dam.runtime.assetinstance;

import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveReadPermissionForAsset;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.klassinstance.AbstractGetInstanceForCustomTabService;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceOverviewTabStrategy;
import com.cs.dam.runtime.util.AssetInstanceUtils;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetAssetInstanceForOverviewTabService extends AbstractGetInstanceForCustomTabService<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel>
    implements IGetAssetInstanceForOverviewTabService {

  @Autowired
  protected IGetConfigDetailsForAssetInstanceOverviewTabStrategy getConfigDetailsForAssetInstanceOverviewTab;

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
    return getConfigDetailsForAssetInstanceOverviewTab.execute(model);
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

  protected void fillEntityInformation(IBaseEntityDAO baseEntityDAO, IGetKlassInstanceCustomTabModel returnModel) throws Exception
  {
    IAssetInstance assetInstance = (IAssetInstance) returnModel.getKlassInstance();
    IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDAO.getBaseEntityDTO(), rdbmsComponentUtils);
    IEventInstanceSchedule eventSchedule = klassInstanceInformationModel.getEventSchedule();
    ConfigurationDAO configDAO = ConfigurationDAO.instance();

    List<IPropertyDTO> propertyDTO = new ArrayList<>();
    propertyDTO.add(configDAO.getPropertyByCode(SystemLevelIds.START_DATE_ATTRIBUTE));
    propertyDTO.add(configDAO.getPropertyByCode(SystemLevelIds.END_DATE_ATTRIBUTE));

    long baseEntityIID = baseEntityDAO.getBaseEntityDTO().getBaseEntityIID();
    Map<Long, Set<IPropertyRecordDTO>> propertyRecordsForEntities = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getPropertyRecordsForEntities(Set.of(baseEntityIID),
            propertyDTO.toArray(new IPropertyDTO[0]));

    Set<IPropertyRecordDTO> iPropertyRecordDTOS = propertyRecordsForEntities.computeIfAbsent(baseEntityIID, k -> new HashSet<>());
    for (IPropertyRecordDTO propertyInstance : iPropertyRecordDTOS) {
      IPropertyDTO property = propertyInstance.getProperty();
      IValueRecordDTO value = (IValueRecordDTO) propertyInstance;
      if ((property.getCode()).equals(SystemLevelIds.START_DATE_ATTRIBUTE)) {
        eventSchedule.setStartTime((long) value.getAsNumber());
      }
      else {
        eventSchedule.setEndTime((long) value.getAsNumber());
      }
    }
    
    Map<String, IAssetAttributeInstanceInformationModel> referencedAssets = new HashMap<>();
    List<IAssetAttributeInstanceInformationModel> referencedAssetsList = new ArrayList<>();
    long defaultImageIID = baseEntityDAO.getBaseEntityDTO().getDefaultImageIID();
    if (defaultImageIID != 0l) {
      IBaseEntityDTO baseEntityDTOByIID = rdbmsComponentUtils.getLocaleCatlogDAO().getBaseEntityDTOByIID(defaultImageIID);
      referencedAssetsList = BaseEntityUtils.fillAssetInfoModel(baseEntityDTOByIID);
    }
    
    for (IAssetAttributeInstanceInformationModel referencedAsset : referencedAssetsList) {
      referencedAssets.put(referencedAsset.getAssetInstanceId(), referencedAsset);
      returnModel.setReferencedAssets(referencedAssets);
    }
    
    assetInstance.setEventSchedule(klassInstanceInformationModel.getEventSchedule());
  }
}
