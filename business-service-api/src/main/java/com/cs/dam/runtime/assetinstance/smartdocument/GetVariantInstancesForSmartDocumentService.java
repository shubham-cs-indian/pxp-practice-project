package com.cs.dam.runtime.assetinstance.smartdocument;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.usecase.smartdocument.GetConfigDetailsToFetchDataForSmartDocumentRequestModel;
import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentRequestModel;
import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentResponseModel;
import com.cs.core.config.strategy.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentStrategy;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.smartdocument.GetInstancesForSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetEntityForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetInstancesForSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;
import com.cs.core.runtime.interactor.model.variants.GetVariantInstancesInTableViewStrategyModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewRequestStrategyModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.dam.runtime.smartdocument.IGetVariantInstancesForSmartDocumentService;
import com.cs.dam.runtime.util.AssetInstanceUtils;

@Service
public class GetVariantInstancesForSmartDocumentService extends AbstractGetInstancesForSmartDocumentService
    implements IGetVariantInstancesForSmartDocumentService {
  
  @Autowired
  protected RDBMSComponentUtils                                  rdbmsComponentUtils;
  
  @Autowired
  protected VariantInstanceUtils                                 variantInstanceUtils;
  
  @Autowired
  protected IGetConfigDetailsToFetchDataForSmartDocumentStrategy getConfigDetailsToFetchDataForSmartDocumentStrategy;
  
  @Override
  public IListModel<ISmartDocumentKlassInstanceDataModel> executeInternal(
      IGetEntityForSmartDocumentRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IGetConfigDetailsToFetchDataForSmartDocumentResponseModel getConfigDetails(
      IGetEntityForSmartDocumentRequestModel dataModel) throws Exception
  {
    IGetConfigDetailsToFetchDataForSmartDocumentRequestModel configRequestModel = new GetConfigDetailsToFetchDataForSmartDocumentRequestModel();
    configRequestModel.setEntityId(dataModel.getEntityId());
    configRequestModel.setPresetId(dataModel.getPresetId());
    IGetConfigDetailsToFetchDataForSmartDocumentResponseModel returnModel = getConfigDetailsToFetchDataForSmartDocumentStrategy
        .execute(configRequestModel);
    
    return returnModel;
  }
  
  @Override
  protected IGetInstancesForSmartDocumentResponseModel getInstancesForSmartDocument(
      IGetEntityForSmartDocumentRequestModel dataModel,
      IGetConfigDetailsToFetchDataForSmartDocumentResponseModel configDetails) throws Exception
  {
    IGetInstancesForSmartDocumentResponseModel response = new GetInstancesForSmartDocumentResponseModel();
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    IBaseEntityDTO baseEntityDTO = localeCatalogDao.getEntityByID(dataModel.getInstanceId());
    dataModel.setInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    IGetVariantInstancesInTableViewRequestStrategyModel requestModel = prepareRequestModel(
        dataModel);
    String searchExpression = variantInstanceUtils
        .generateSearchExpression(requestModel.getContextId(), requestModel);
    List<IBaseEntityDTO> klassInstances = getAllChildrenForGivenContext(requestModel,
        searchExpression);
    fillInstancesImageAttribute(klassInstances, response);
    response.setKlassInstances(klassInstances);
    
    return response;
  }
  
  protected static IGetVariantInstancesInTableViewRequestStrategyModel prepareRequestModel(
      IGetEntityForSmartDocumentRequestModel dataModel)
  {
    IGetVariantInstancesInTableViewRequestStrategyModel requestModel = new GetVariantInstancesInTableViewStrategyModel();
    requestModel.setContextId(dataModel.getEntityId());
    requestModel.setParentId(dataModel.getInstanceId());
    requestModel.setSize(dataModel.getSize());
    requestModel.setFrom(dataModel.getFrom());
    requestModel.setTimeRange(new InstanceTimeRange());
    
    return requestModel;
  }
  
  // Return base entity DTO's list of context children.
  protected List<IBaseEntityDTO> getAllChildrenForGivenContext(
      IGetVariantInstancesInTableViewRequestStrategyModel requestModel, String searchExpression)
      throws Exception
  {
    int size = requestModel.getSize();
    IRDBMSOrderedCursor<IBaseEntityDTO> contextualChildren = rdbmsComponentUtils
        .getLocaleCatlogDAO()
        .getAllEntitiesBySearchExpression(searchExpression, true);
    
    return contextualChildren.getNext(requestModel.getFrom(), size);
  }
  
  protected void fillInstancesImageAttribute(List<IBaseEntityDTO> klassInstances,
      IGetInstancesForSmartDocumentResponseModel returnMap) throws Exception
  {
    Map<String, IAssetInformationModel> instancesImageAttribute = returnMap
        .getInstancesImageAttribute();
    for (IBaseEntityDTO klassInstance : klassInstances) {
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(klassInstance);
      Set<IBaseEntityDTO> contextualLinkedEntities = baseEntityDAO.getContextualLinkedEntities();
      
      for (IBaseEntityDTO contextualLinkedEntity : contextualLinkedEntities) {
        long defaultImageIID = contextualLinkedEntity.getDefaultImageIID();
        if (defaultImageIID != 0) {
          IBaseEntityDAO assetEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(defaultImageIID);
          IBaseEntityDTO assetEntityDTO = assetEntityDAO.getBaseEntityDTO();
          IAssetInformationModel assetInfoModel = AssetInstanceUtils
              .getAssetInformationModel(assetEntityDTO, assetEntityDTO.getEntityExtension());
          instancesImageAttribute.put(contextualLinkedEntity.getBaseEntityID(), assetInfoModel);
        }
      }
    }
  }
}
