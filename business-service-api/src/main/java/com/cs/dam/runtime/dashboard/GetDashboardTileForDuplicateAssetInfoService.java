package com.cs.dam.runtime.dashboard;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.attribute.IBooleanReturnModel;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.dashboard.AbstractGetDashboardTileInformationService;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.dashboard.DashboardInformationResponseModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.dashboard.IGetConfigDetailsForDashboardTileInformationStrategy;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.config.strategy.usecase.duplicatedetection.IGetDuplicateDetectionStatusStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Get duplicate assets for dam dashboard
 * @author mrunali.dhenge
 *
 */

@Service
public class GetDashboardTileForDuplicateAssetInfoService extends AbstractGetDashboardTileInformationService<IDashboardInformationRequestModel, IDashboardInformationResponseModel>
    implements IGetDashboardTileForDuplicateAssetInfoService {
  
  @Autowired
  IGetDuplicateDetectionStatusStrategy                           getDuplicateDetectionStatusStrategy;
  
  @Autowired
  protected IGetConfigDetailsForDashboardTileInformationStrategy getConfigDetailsForDashboardTileInformationStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                  rdbmsComponentUtils;
  
  @Autowired
  protected TransactionThreadData                                transactionThread;
  
  @Override
  protected IDashboardInformationResponseModel executeInternal(
      IDashboardInformationRequestModel model) throws Exception
  {
    IDashboardInformationResponseModel dashboardInformationResponseModel = new DashboardInformationResponseModel();
    IVoidModel voidModel = new VoidModel();
    
    // Check whether duplicate detection is on or off
    IBooleanReturnModel duplicateDetectionStatus = getDuplicateDetectionStatusStrategy.execute(voidModel);
    if (duplicateDetectionStatus.getDuplicateStatus()) {
      dashboardInformationResponseModel = super.executeInternal(model);
    }
    return dashboardInformationResponseModel;
  }
  
  /**
   * Get Config Details for dam dashboard information
   */
  @Override
  protected IConfigDetailsForInstanceTreeGetModel getConfigDetails(IIdParameterModel model)
      throws Exception
  {
    return getConfigDetailsForDashboardTileInformationStrategy.execute(model);
  }
  
  @Override
  protected String generateSearchExpression(IDashboardInformationRequestModel dataModel)
      throws CSInitializationException
  {
    return null;
  }
  
  /**
   * Get recently generated duplicate Asset baseentityDTOs
   */
  @Override
  protected List<IBaseEntityDTO> getAssetBaseEntityDTOs(IDashboardInformationRequestModel model,
      IDashboardInformationResponseModel responseModel, ILocaleCatalogDAO localeCatalogDao)
      throws CSInitializationException, RDBMSException
  {
    
    List<IBaseEntityDTO> duplicateBaseEntitiesDTOs = new ArrayList<IBaseEntityDTO>();
    ITransactionData transactionData = transactionThread.getTransactionData();
    String targetOrganizationCode = transactionData.getOrganizationId();
    String targetOrgCode = targetOrganizationCode.equals(IStandardConfig.STANDARD_ORGANIZATION_CODE)
        ? IStandardConfig.STANDARD_ORGANIZATION_RCODE: targetOrganizationCode;
    ISortModel sortData = model.getSortOptions().get(0);
    String sortOrder = sortData.getSortOrder();
    
    // Get list of recently created Duplicate baseEntityiids
    List<String> duplicateAssetIds = localeCatalogDao.getDuplicateAssetInstances(model.getSize(),
        transactionData.getPhysicalCatalogId(), targetOrgCode, transactionData.getEndpointId(),
        sortOrder);
    if (!duplicateAssetIds.isEmpty()) {
      duplicateBaseEntitiesDTOs = localeCatalogDao
          .getAllEntitiesByIIDList(String.join(",", duplicateAssetIds), null, true);
    }
    responseModel.setTotalContents(duplicateBaseEntitiesDTOs.size());
    return duplicateBaseEntitiesDTOs;
    
  }
}
