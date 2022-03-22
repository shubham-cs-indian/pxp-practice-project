package com.cs.dam.runtime.taskexecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.AutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IAutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.variantcontext.GetConfigDetailsForAutoCreateTIVRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVResponseModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableWrapperModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ContentIdentifierModel;
import com.cs.core.runtime.interactor.model.configuration.IContentIdentifierModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.variants.ICreateImageVariantsModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForAutoCreateTIVStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.utils.threadPoolExecutor.ThreadPoolExecutorUtil;
import com.cs.utils.dam.AssetUtils;
import com.cs.utils.dam.CreateImageVariantUtils;

@Component
public class GetAssetAndCreateVariantService extends
    AbstractRuntimeService<ITechnicalImageVariantWithAutoCreateEnableWrapperModel, IIdParameterModel>
    implements IGetAssetAndCreateVariantService {
  
  @Autowired
  protected TransactionThreadData           transactionThreadData;
  
  @Autowired
  protected IGetConfigDetailsForAutoCreateTIVStrategy getConfigDetailsForAutoCreateTIVStrategy;
  
  @Autowired
  protected ThreadPoolExecutorUtil threadPoolTaskExecutorUtil;
  
  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Override
  protected IIdParameterModel executeInternal(
      ITechnicalImageVariantWithAutoCreateEnableWrapperModel model) throws Exception
  {
    String path = null;
    List<String> thumbnailPathList = new ArrayList<>();
    String mainAssetThumbnailPath = model.getThumbnailPath();
    if(mainAssetThumbnailPath != null) {
      thumbnailPathList.add(mainAssetThumbnailPath);
    }
    IAssetInformationModel attribute = model.getAttribute();
    try {
      
      TransactionData transactionData = transactionThreadData.getTransactionData();
      List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutoCreateEnableModel = model
          .getTechnicalImageVariantWithAutoCreateEnable();
      
      String instanceId = model.getInstanceId();
      path = model.getMainAssetInstanceSourcePath();
      IContentIdentifierModel requestTransactionData = new ContentIdentifierModel(
          transactionData.getOrganizationId(), transactionData.getPhysicalCatalogId(),
          transactionData.getPortalId(), transactionData.getLogicalCatalogId(),
          transactionData.getSystemId(), transactionData.getEndpointId());
      
      List<String> contextIds = new ArrayList<>();
      for (ITechnicalImageVariantWithAutoCreateEnableModel contextWithAutoCreateEnableModel : contextsWithAutoCreateEnableModel) {
        contextIds.add(contextWithAutoCreateEnableModel.getId());
      }
      
      IGetConfigDetailsForAutoCreateTIVRequestModel getConfigDetailsForAutoCreateTIVRequestModel = new GetConfigDetailsForAutoCreateTIVRequestModel();
      getConfigDetailsForAutoCreateTIVRequestModel.setContextIds(contextIds);
      getConfigDetailsForAutoCreateTIVRequestModel.setOrganizationId(transactionData.getOrganizationId());
      getConfigDetailsForAutoCreateTIVRequestModel.setEndpointId(transactionData.getEndpointId());
      getConfigDetailsForAutoCreateTIVRequestModel
          .setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
      getConfigDetailsForAutoCreateTIVRequestModel.setPortalId(transactionData.getPortalId());
      getConfigDetailsForAutoCreateTIVRequestModel.setBaseType(Constants.ASSET_INSTANCE_BASE_TYPE);
      IGetConfigDetailsForAutoCreateTIVResponseModel configDetailsResponse = getConfigDetailsForAutoCreateTIVStrategy
            .execute(getConfigDetailsForAutoCreateTIVRequestModel);
      Map<String, IGetConfigDetailsForCreateVariantModel> configDetailsMap = configDetailsResponse.getConfigDetailsMap();
      
      for (ITechnicalImageVariantWithAutoCreateEnableModel contextWithAutoCreateEnableModel : contextsWithAutoCreateEnableModel) {
        IGetConfigDetailsForCreateVariantModel configDetailsForCreateVariantModel = configDetailsMap.get(contextWithAutoCreateEnableModel.getId());
        int numberOfVersionsToMaintain = configDetailsForCreateVariantModel.getNumberOfVersionsToMaintain();
        String fileName = FilenameUtils.getBaseName(attribute.getFileName());
        String parentId = model.getParentId();
        if (contextWithAutoCreateEnableModel.getType().equals(CommonConstants.IMAGE_VARIANT)) {
          List<ICreateImageVariantsModel> createVariantModelList = CreateImageVariantUtils
              .createVariantInstances(contextWithAutoCreateEnableModel, attribute, path, instanceId,
                  transactionData.getParentTransactionId(), fileName,
                  configDetailsMap.get(contextWithAutoCreateEnableModel.getId()), requestTransactionData,
                  parentId, model.getAssetConfigurationModel(), new ArrayList<String>(), new ArrayList<String>(), mainAssetThumbnailPath);
          for (ICreateImageVariantsModel createVariantModel : createVariantModelList) {
            String thumbnailPath = createVariantModel.getThumbnailPath();
            if (thumbnailPath != null) {
              thumbnailPathList.add(thumbnailPath);
            }
            submitBGPVariantInstanceTask(createVariantModel, instanceId, numberOfVersionsToMaintain);
          }
        }
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    finally {
      AssetUtils.deleteFileAndDirectory(path);
      thumbnailPathList.forEach(thumbnailPath -> {
        AssetUtils.deleteFileAndDirectory(thumbnailPath);
      });
    }
    
    return null;
  }
  
  /**
   * Prepare Auto Create Variant instance model and submit bgp to create the variant
   * Added this task as BGP process because issue in creating parent revision for multiple variants
   * try to create same revision number for parent in different transaction
   * @param createVariantModel
   * @param instanceId 
   * @param numberOfVersionsToMaintain 
   * @throws Exception 
   * @throws NumberFormatException 
   */
  @SuppressWarnings("unchecked")
  private void submitBGPVariantInstanceTask(ICreateVariantModel createVariantModel, String instanceId, int numberOfVersionsToMaintain)
      throws NumberFormatException, Exception
  {
    IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(Long.valueOf(instanceId));
   // rdbmsComponentUtils.createNewRevision(baseEntityDTO, numberOfVersionsToMaintain);
    AutoCreateVariantDTO autoCreateVariantDTO = new AutoCreateVariantDTO();
    autoCreateVariantDTO.setBaseEntity(baseEntityDTO);
    autoCreateVariantDTO.setConfigData(ObjectMapperUtil.convertValue(createVariantModel, JSONObject.class));
    autoCreateVariantDTO.setTransaction(transactionThreadData.getTransactionData());
    BGPDriverDAO.instance().submitBGPProcess(transactionThreadData.getTransactionData().getUserName(), IAutoCreateVariantDTO.CREATE_VARIANT_SERVICE, "",
        IBGProcessDTO.BGPPriority.HIGH, autoCreateVariantDTO.toJSONContent());
  }
  
}
