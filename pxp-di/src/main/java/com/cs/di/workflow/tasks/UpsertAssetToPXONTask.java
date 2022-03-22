package com.cs.di.workflow.tasks;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.cs.core.config.interactor.model.language.GetLanguagesRequestModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesRequestModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesResponseModel;
import com.cs.core.config.strategy.usecase.language.IGetLanguagesStrategy;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.usecase.tag.IGetAllAssetExtensions;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.di.runtime.utils.DiFileUtils;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

/**
 * @author priyaranjan.kumar
 * This Class contain all common method used for Asset to PIXON
 *         Transformation.
 *
 */
@Component("upsertAssetToPXONTask")
public class UpsertAssetToPXONTask extends AbstractToPXONTask {
  

  @Autowired
  IGetAllAssetExtensions                 getAllAssetExtensions;
  
  //for input
  public static final String             ASSET_FILES_MAP  = "ASSET_FILES_MAP";
  //for output
  public static final String             EXECUTION_STATUS = "EXECUTION_STATUS";
  public static final String             SUCCESS_FILES    = "SUCCESS_FILES";
  
  public static final String             FILE_SOURCE      = "fromFolder";
  
  public static final List<String>       INPUT_LIST       = Arrays.asList(ASSET_FILES_MAP);
  public static final List<String>       OUTPUT_LIST      = Arrays.asList(PXON, EXECUTION_STATUS, FAILED_FILES, SUCCESS_FILES);
  public static final List<WorkflowType> WORKFLOW_TYPES   = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES      = Arrays.asList(
      EventType.BUSINESS_PROCESS, EventType.INTEGRATION);
  
  
  @Override
  public List<String> getInputList()
  {
    return INPUT_LIST;
  }
  
  @Override
  public List<String> getOutputList()
  {
    return OUTPUT_LIST;
  }
  
  @Override
  public List<WorkflowType> getWorkflowTypes()
  {
    return WORKFLOW_TYPES;
  }
  
  @Override
  public List<EventType> getEventTypes()
  {
    return EVENT_TYPES;
  }
  
  @Override
  public TaskType getTaskType()
  {
    return TaskType.SERVICE_TASK;
  }
  
  @Override
  public DiDataType getDataType()
  {
    return DiDataType.ASSET;
  }
  
  /**
   * Validate input parameters
   * 
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> invalidParameters = new ArrayList<>();
    // Validate required RECEIVED_DATA
    String receivedData = (String) inputFields.get(ASSET_FILES_MAP);
    if (DiValidationUtil.isBlank(receivedData)) {
      invalidParameters.add(ASSET_FILES_MAP);
    }
    return invalidParameters;
    
    // Note: Validation not required for
    // 1. PXON as it is optional output parameter
    // 2. EXECUTION_STATUS as it is optional output parameter
    // 3. FAILED_FILES as it is optional output parameter
    // 4. SUCCESS_FILES as it is optional output parameter
  }
  
  /**
   * Generate PXON for Assets
   */
  @SuppressWarnings("unchecked")
  @Override
  public void generatePXON(WorkflowTaskModel model)
  {
    ILocaleCatalogDAO localeCatalogDAO = DiUtils.createLocaleCatalogDAO((IUserSessionDTO) model.getWorkflowModel().getUserSessionDto(),
        (ITransactionData) model.getWorkflowModel().getTransactionData());
   //Retrieving Input Data 
    Map<String, String> assetFilesMap = (Map<String, String>) DiValidationUtil.validateAndGetRequiredMap(model, ASSET_FILES_MAP);
    if (assetFilesMap == null) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003,
              new String[] { DiDataType.ASSET.name() });
    }
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    List<String> successFiles = new ArrayList<>();
    List<String> failedfiles = new ArrayList<>();
    if (assetFilesMap != null)
      try {
        generatePXONForAssets(successFiles, failedfiles, assetFilesMap, localeCatalogDAO, model.getExecutionStatusTable(),
            model.getWorkflowModel().getTransactionData().getDataLanguage());
      }
      catch (Exception exception) {
        model.getExecutionStatusTable().addError(MessageCode.GEN012);
      }
    if (!CollectionUtils.isEmpty(successFiles)) {
      // Write entities to file
      String filename = writePXONToFile(successFiles, model.getExecutionStatusTable());
      
      // Add generated file name
      model.getOutputParameters()
          .put(PXON, filename);
    }
    //Setting output parameter  
    model.getOutputParameters().put(FAILED_FILES, failedfiles);
    model.getOutputParameters().put(SUCCESS_FILES, successFiles);
  }
  
  /**
   * Generate PXON for Assets.Processed file are added to the success file list
   * and unprocessed file added in the failed file list.
   *
   * @param successFiles
   * @param failedfiles
   * @param assetData
   * @param localeCatalogDAO
   * @param executionStatusTable
   * @throws Exception
   */
  public void generatePXONForAssets(List<String> successFiles, List<String> failedfiles,
      Map<String, String> assetData, ILocaleCatalogDAO localeCatalogDAO,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,String language) throws Exception
  {
      //Loading all asset Extensions  
      Map<String, List<String>> supportedExtentions = getAllAssetExtensions
          .execute(new IdsListParameterModel())
          .getAssetExtensions();
      String parentDirectory = getParentDirectory(assetData);
      for (Entry<String, String> asset : assetData.entrySet()) {
        try {
          File file = new File(asset.getValue());
          String id = asset.getKey();
          String extension = FilenameUtils.getExtension(file.getPath());
          String baseType = getBaseType(supportedExtentions, "." + extension);
          if (baseType != null && baseType.isEmpty()) {
            break;
          }
          String filePath = file.getPath();
          StringBuilder assetUploadType = new StringBuilder();
          // Get or Create baseEntity
          IClassifierDTO natureKlass = RDBMSUtils.newConfigurationDAO().createClassifier(baseType, ClassifierType.CLASS);
          IBaseEntityDTO baseEntity = localeCatalogDAO.getEntityByID(id);
          if (baseEntity == null) {
            baseEntity = localeCatalogDAO.newBaseEntityDTOBuilder(id, IBaseEntityIDDTO.BaseType.ASSET, natureKlass).build();
          }
          IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntity);
          // Upload Asset to server and get uploaded asset details
          IBulkUploadResponseAssetModel uploadedAssetDetails = uploadAssetToServer(baseType, filePath, FILE_SOURCE, assetUploadType);
          Map<String, String> assetMetadataAttributes = new HashMap<>();
          if (uploadedAssetDetails != null) {
              IAssetKeysModel assetKeysModel = (IAssetKeysModel) uploadedAssetDetails.getSuccess().getAssetKeysModelList().get(0);
              DiTransformationUtils.prepareAssetExtenstionData(baseEntity, assetKeysModel, assetUploadType.toString());
              assetMetadataAttributes.putAll(DiTransformationUtils.convertAssetMetadataToMap(assetKeysModel));
          }
          //Preparing asset metadata for file
          for (Entry<String, String> entry : assetMetadataAttributes.entrySet()) {
            IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByCode(entry.getKey());
            IPropertyRecordDTO propertyRecord = baseEntityDAO
                .newValueRecordDTOBuilder(propertyDTO, entry.getValue()).localeID(language).build();
            baseEntity.getPropertyRecords().add(propertyRecord);
          }
          // Set name for imported asset
          IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByCode(StandardProperty.nameattribute.name());
          IPropertyRecordDTO propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(propertyDTO, id).localeID(language).build();
          baseEntity.getPropertyRecords().add(propertyRecord);
          successFiles.add(baseEntity.toPXON());
        }
        catch (Exception exception) {
          failedfiles.add(asset.getKey());
          RDBMSLogger.instance().exception(exception);
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID },
              MessageCode.GEN014, new String[] { asset.getKey() });
        }
      }
      // deleting temporary folder and its content
      DiFileUtils.deleteDirectory(Paths.get(parentDirectory), executionStatusTable);
  }

  
  /**
   * This Method return BaseType for given extension.
   * 
   * @param supportedExtentions
   *          system supported file extensions
   * @param extension
   * @return String
   */
  private String getBaseType(Map<String, List<String>> supportedExtentions, String extension)
  {
    String baseType = "";
    Optional<Entry<String, List<String>>> findFirst = supportedExtentions.entrySet()
        .stream()
        .filter(entry -> entry.getValue().contains(extension)).findFirst();
    baseType = findFirst.get().getKey();
    return baseType;
  }
  
  /**
   * This Method return BaseType for given extension.
   *
   * @param assetData
   * @return
   */
  private String getParentDirectory(Map<String, String> assetData)
  {
    if (assetData != null && !assetData.isEmpty()) {
      String next = assetData.values().iterator().next();
      File assetFile = new File(next);
      return assetFile.getAbsoluteFile().getParent();
    }
    return null;
  } 
  
}
