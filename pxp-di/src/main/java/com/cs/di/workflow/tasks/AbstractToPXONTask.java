package com.cs.di.workflow.tasks;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.Constants;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.asset.services.PropertyRecordBuilder;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.asset.UploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.GetConfigDataEntityPaginationModel;
import com.cs.core.config.interactor.model.configdetails.GetConfigDataEntityRequestModel;
import com.cs.core.config.interactor.model.configdetails.GetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityPaginationModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.marketingbundle.IKlassesAndTaxonomiesModel;
import com.cs.core.config.interactor.model.marketingbundle.KlassesAndTaxonomiesModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.interactor.usecase.assetserver.IUploadMultipleAssetsToServer;
import com.cs.core.config.interactor.usecase.tag.IGetAllAssetExtensions;
import com.cs.core.config.strategy.usecase.attribute.OrientDBGetAttributesByIdsStrategy;
import com.cs.core.config.strategy.usecase.configdata.IGetConfigDataStrategy;
import com.cs.core.config.strategy.usecase.relationship.IGetRootRelationshipSideInfo;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTOBuilder;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.config.interactor.model.task.IGetConfigDetailsByCodesResponseModel;
import com.cs.di.config.strategy.usecase.task.IGetConfigDetailsByCodesStrategy;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;

import coms.cs.core.config.businessapi.variantcontext.IGetVariantContextService;

public abstract class AbstractToPXONTask extends AbstractTransformationTask {
  
  public static final String                       RECEIVED_DATA         = "RECEIVED_DATA";
  public static final String                       PXON                  = "PXON";
  public static final String                       FAILED_FILES          = "FAILED_FILES";
  volatile String                                  defaultLanguageCode   = null;
  static final List<String>   VALID_ACTION_TYPES  = Arrays.asList(CREATE, UPDATE, DELETE);
  public static Map<String, IGetRelationshipModel> relationshipConfigMap = new HashMap<>();
  public static final Map<String, String>          baseTypeMap           = new HashMap<>() {
    {
      put(Constants.PROJECT_KLASS_TYPE, IBaseEntityIDDTO.BaseType.ARTICLE.name());
      put(Constants.ASSET_KLASS_TYPE, IBaseEntityIDDTO.BaseType.ASSET.name());
      put(Constants.MARKET_KLASS_TYPE, IBaseEntityIDDTO.BaseType.TARGET.name());
      put(Constants.TEXT_ASSET_KLASS_TYPE, IBaseEntityIDDTO.BaseType.TEXT_ASSET.name());
      put(Constants.SUPPLIER_KLASS_TYPE, IBaseEntityIDDTO.BaseType.SUPPLIER.name());
      //TODO: PXPFDEV-21454: Deprecate Virtual Catalog 
      //put(Constants.VIRTUAL_CATALOG_KLASS_TYPE, IBaseEntityIDDTO.BaseType.VIRTUAL_CATALOG.name());
    }
  };
  
  @Autowired
  IUploadMultipleAssetsToServer      uploadAssetToServer;
  
  @Autowired
  IGetAllAssetExtensions             getAllAssetExtensions;
  
  @Autowired
  IGetConfigDetailsByCodesStrategy   getConfigDetailsByCodesStrategy;
  
  @Autowired
  IGetConfigDataStrategy             getConfigDataStrategy;
   
  @Autowired
  OrientDBGetAttributesByIdsStrategy getAttributesByIdsStrategy;
  
  @Autowired
  protected TransactionThreadData    transactionThread;

  @Autowired
  protected IGetRootRelationshipSideInfo getRootRelationshipSideInfo;
  
  @Autowired
  protected IGetVariantContextService    getVariantContextService;

  protected abstract void generatePXON(WorkflowTaskModel model);
  
  protected abstract DiDataType getDataType();
  
  protected Map<String, BaseType>  baseEntityIDWithBaseType = new HashMap<>();
  
  @Override
  public void transform(WorkflowTaskModel model)
  {
    transactionThread.setTransactionData((TransactionData) model.getWorkflowModel()
        .getTransactionData());
    defaultLanguageCode = model.getWorkflowModel().getTransactionData().getDataLanguage();
    generatePXON(model);
  }
  
  /**
   * Write entities to PXON file
   * 
   * @param entities
   * @param executionStatusTable
   * @return
   */
  protected String writePXONToFile(List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    String filePath = null;
    String filename = "inboundPXON_" + System.currentTimeMillis() + ".pxon";
    if (AbstractBGPTask.BGP_SHARED_DIRECTORY == null) {
      executionStatusTable.addError(MessageCode.GEN019);
    }
    else {
      filePath = AbstractBGPTask.BGP_SHARED_DIRECTORY + filename;
    }
    
    FileWriter myWriter = null;
    try {
      myWriter = new FileWriter(filePath);
      myWriter.write('[');
      int entityCount = 0;
      for (; entityCount < entities.size() - 1; entityCount++) {
        myWriter.write(entities.get(entityCount) + ',');
      }
      myWriter.write(entities.get(entityCount) + ']');
    }
    catch (IOException e) {
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE },
          MessageCode.GEN018, new String[] { filePath });
    }
    finally {
      if (myWriter != null) {
        try {
          myWriter.close();
        }
        catch (IOException e) {
          RDBMSLogger.instance()
              .exception(e);
        }
      }
    }
    
    return filename;
  }
  
  /**
   * Prepare relationship map
   * 
   * @param list
   * @param relationships
   * @param diDataType
   */
  protected void prepareRelationshipsMap(List<Map<String, Object>> list,
      Map<String, List<Map<String, Object>>> relationships, DiDataType diDataType)
  {
    if (!CollectionUtils.isEmpty(list)) {
      for (Map<String, Object> relation : list) {
        String side1Id = (String) relation
            .get(DiDataType.EXCEL.equals(diDataType) ? SIDE1_ID_COLUMN : SIDE1_ID);
        List<Map<String, Object>> side1RelList = relationships.get(side1Id);
        if (side1RelList == null) {
          side1RelList = new ArrayList<>();
          relationships.put(side1Id, side1RelList);
        }
        side1RelList.add(relation);
      }
    }
  }
  
  /**
   * Generate DTOs For Relationships (Nature and Non Nature)
   * 
   * @param baseEntityDTO
   * @param relationships
   * @param baseEntity
   * @param diDataType
   * @param executionStatusTable
   * @throws Exception
   */
  protected void updateDTORelations(IBaseEntityDTO baseEntityDTO,
      List<Map<String, Object>> relationships, IBaseEntityDAO baseEntity, DiDataType diDataType,
      WorkflowTaskModel workflowTaskModel, Map<String, ILocaleCatalogDAO> localCatalogDAOMap,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable) throws Exception
  {
    for (Map<String, Object> relation : relationships) {
      try {
        long side2IID = 0L;
        String side2 = (String) relation
            .get(DiDataType.EXCEL.equals(diDataType) ? SIDE2_ID_COLUMN : SIDE2_ID);
        ILocaleCatalogDAO localeCatalogDAO = DiUtils.getLocaleCatalogDAO(localCatalogDAOMap,
            defaultLanguageCode, workflowTaskModel);
        IBaseEntityDTO side2BaseEntity = localeCatalogDAO.getEntityByID(side2);
        if (side2BaseEntity == null) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID },
              MessageCode.GEN020, new String[] { side2 });
        }
        else {
          side2IID = side2BaseEntity.getBaseEntityIID();
        }
        updateBaseEntityRelationships(baseEntityDTO, baseEntity, relation, side2IID, diDataType);
      }
      catch (Exception e) {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
            new String[] { SIDE1_ID + " - "
                + (DiDataType.EXCEL.equals(diDataType) ? (String) relation.get(SIDE1_ID_COLUMN)
                    : (String) relation.get(SIDE1_ID)) });
      }
    }
  }
  
  /**
   * Generate DTOs For Base Entity Relationships (Nature and Non Nature)
   * 
   * @param baseEntityDTO
   * @param baseEntity
   * @param relationDataObject
   * @param side2IID
   * @param diDataType
   * @throws Exception
   */
  protected void updateBaseEntityRelationships(IBaseEntityDTO baseEntityDTO, IBaseEntityDAO baseEntity,
      Map<String, Object> relationDataObject, long side2IID, DiDataType diDataType) throws Exception
  {
    String side2IdColumn = (String) relationDataObject.get(DiDataType.EXCEL.equals(diDataType) ? SIDE2_ID_COLUMN : SIDE2_ID);
    String relationshipCodeColumn = (String) relationDataObject
        .get(DiDataType.EXCEL.equals(diDataType) ? RELATIONSHIP_CODES_COLUMN : RELATIONSHIP_ID);
    String contextCodeColumn = null;
    Map<String, Object> contextualInputData = null;
    if (DiDataType.EXCEL.equals(diDataType)) {
      contextCodeColumn = (String) relationDataObject.get(CONTEXT_ID_COLUMN);
      contextualInputData = relationDataObject;
    }
    else {
      Map<String, Object> relationContext = (Map<String, Object>) relationDataObject.get(OPTIONAL);
      if (!relationContext.isEmpty()) {
        contextualInputData = (Map<String, Object>) relationContext.get(CONTEXT);
        contextCodeColumn = (String) contextualInputData.get(CONTEXT_ID);
      }
    }
    
    IEntityRelationDTO tempRelation = null;
    IRelationsSetDTO relationSet = null;
    Set<IEntityRelationDTO> relations = new HashSet<>();
    
    /*Check relationship exists on baseEntityDTO for newly created relationship
    otherwise check that relationship is already exist or not.*/
    RelationSide relationSide = getRelationSide(baseEntityDTO, relationshipCodeColumn);
    IPropertyDTO relationProperty = DiUtils.getPropertyByCode(relationshipCodeColumn);
    relationProperty.setRelationSide(relationSide);
    IPropertyRecordDTO newRelationshipProperty = baseEntityDTO.getPropertyRecord(relationProperty.getPropertyIID());
    if (newRelationshipProperty == null) {
      Set<IPropertyRecordDTO> otherPropertyRecord = new HashSet<>(baseEntityDTO.getPropertyRecords());
      baseEntity.loadPropertyRecords(relationProperty);
      IPropertyRecordDTO existingRelationshipProperty = baseEntityDTO
          .getPropertyRecord(relationProperty.getPropertyIID());
      if (existingRelationshipProperty == null) {
        relationSet = baseEntity.newEntityRelationsSetDTOBuilder(relationProperty, relationSide).build();
      }
      else {
        relationSet = (IRelationsSetDTO) existingRelationshipProperty;
      }
      baseEntityDTO.getPropertyRecords().addAll(otherPropertyRecord);
    }
    else {
      relationSet = (IRelationsSetDTO) newRelationshipProperty;
    }
    relations = relationSet.getRelations();
    
    // Check if relationship already exists with particular side2 instance.
    boolean isRelationshipAlreadyExists = false;
    for (IEntityRelationDTO rel : relations) {
      if (side2IdColumn.equals(rel.getOtherSideEntityID())) {
        isRelationshipAlreadyExists = true;
        tempRelation = rel;
        break;
      }
    }
    
    // Get or set the action
    String action = (String) relationDataObject.get(DiDataType.EXCEL.equals(diDataType) ? ACTION : "action");
    if (!DiValidationUtil.isBlank(action)) {
      action = action.toUpperCase();
      if (!VALID_ACTION_TYPES.contains(action)) {
        return;
      }
    }
    else if (isRelationshipAlreadyExists) {
      action = UPDATE;
    }
    else {
      action = CREATE;
    }
    if (CREATE.equals(action) && isRelationshipAlreadyExists) {
      RDBMSLogger.instance().info("Relationship " + relationshipCodeColumn + " already exists between " + side2IdColumn + " and "
          + baseEntityDTO.getBaseEntityID());
      return;
    }
    else if (UPDATE.equals(action) || DELETE.equals(action)) {
      if (isRelationshipAlreadyExists) {
        relations.remove(tempRelation);
      }
      else {
        RDBMSLogger.instance().info("Relationship" + relationshipCodeColumn + " doesnot exist. Hence, cannot be updated or deleted");
        return;
      }
      if (DELETE.equals(action)) {
        return;
      }
    }
    
    IEntityRelationDTO entityRelationDTO = baseEntity.newEntityRelationDTOBuilder().contextCode(contextCodeColumn)
        .OtherSideEntityID(side2IdColumn).build();
    if (side2IID != 0) {
      entityRelationDTO.setOtherSideEntityIID(side2IID);
    }
    IContextualDataDTO contextualObject = entityRelationDTO.getContextualObject();
    DiTransformationUtils.prepareContexualInfo(contextualInputData, contextualObject, diDataType);
    
    //check for duplicate context
    boolean isRelationshipContextAlreadyExists = false;
    for (IEntityRelationDTO rel : relations) {
      if (!rel.getContextualObject().getContextTagValues().isEmpty()
          && rel.getContextualObject().getContextTagValues().equals(contextualObject.getContextTagValues())) {
        isRelationshipContextAlreadyExists = true;
        break;
      }
    }
    if (getIsDuplicateVariantAllowed(contextualObject.getContextCode()) || !isRelationshipContextAlreadyExists) {
      relationSet.getRelations().add(entityRelationDTO);
      baseEntityDTO.getPropertyRecords().add(relationSet);
    }
  }

  /**
   * To compare and return on which side of relationShip , the baseentity is present i.e. side1 or side2.
   *
   * @param baseEntityDTO
   * @param relationshipCodeColumn
   * @return
   * @throws Exception
   */
  private RelationSide getRelationSide(IBaseEntityDTO baseEntityDTO, String relationshipCodeColumn)  throws Exception{
    IGetRelationshipModel relationshipModel;
    if(relationshipConfigMap.containsKey(relationshipCodeColumn)){
      relationshipModel = relationshipConfigMap.get(relationshipCodeColumn);
    }else{
      relationshipModel = getRootRelationshipSideInfo.execute(new IdParameterModel(relationshipCodeColumn));
      relationshipConfigMap.put(relationshipCodeColumn,relationshipModel);
    }
    String relationshipSide1KlassID = relationshipModel.getEntity().getSide1().getKlassId();
    String baseEntityKlassID = baseEntityDTO.getNatureClassifier().getCode();
    String baseEntityBaseType = baseEntityDTO.getBaseType().name();
    String relationshipSide1BaseType = baseTypeMap.get(relationshipModel.getConfigDetails().getReferencedKlasses().get(relationshipSide1KlassID).getType());
    RelationSide relationSide = (relationshipSide1KlassID.equals(baseEntityKlassID)
        || baseEntityBaseType.equals(relationshipSide1BaseType)) ? RelationSide.SIDE_1 : RelationSide.SIDE_2;
    return relationSide;
  }

  /**
   * generatePXONForRelationships
   * 
   * @param entities
   * @param relationshipsMap
   * @param executionStatusTable
   * @param workflowTaskModel
   * @param localCatalogDAOMap
   * @param relationshipsMap2
   * @param defaultLanguageCode
   * @param diDataType
   */
  protected void generatePXONForRelationships(List<String> entities,
      Map<String, List<Map<String, Object>>> relationshipsMap,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      WorkflowTaskModel workflowTaskModel, Map<String, ILocaleCatalogDAO> localCatalogDAOMap,
      String defaultLanguageCode, DiDataType diDataType)
  {
    if (!CollectionUtils.isEmpty(relationshipsMap)) {
      for (Entry<String, List<Map<String, Object>>> relationships : relationshipsMap.entrySet()) {
        try {
          ILocaleCatalogDAO localeCatalogDAO = DiUtils.getLocaleCatalogDAO(localCatalogDAOMap,
              defaultLanguageCode, workflowTaskModel);
          IBaseEntityDTO side1BaseEntity = localeCatalogDAO.getEntityByID(relationships.getKey());
          for (Map<String, Object> relation : relationships.getValue()) {
            try {
              String side1 = (String) relation
                  .get(DiDataType.EXCEL.equals(diDataType) ? SIDE1_ID_COLUMN : SIDE1_ID);
              String side2 = (String) relation
                  .get(DiDataType.EXCEL.equals(diDataType) ? SIDE2_ID_COLUMN : SIDE2_ID);
              
              if (side1BaseEntity == null) {
                executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID },
                    MessageCode.GEN020, new String[] { side1 });
              }
              IBaseEntityDTO side2BaseEntity = localeCatalogDAO.getEntityByID(side2);
              if (side2BaseEntity == null) {
                executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID },
                    MessageCode.GEN020, new String[] { side2 });
              }
              updateBaseEntityRelationships(side1BaseEntity,
                  localeCatalogDAO.openBaseEntity(side1BaseEntity), relation,
                  side2BaseEntity.getBaseEntityIID(), diDataType);
            }
            catch (Exception e) {
              executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID },
                  MessageCode.GEN014,
                  new String[] { SIDE1_ID + " - "
                      + (DiDataType.EXCEL.equals(diDataType)
                          ? (String) relation.get(SIDE1_ID_COLUMN)
                          : (String) relation.get(SIDE1_ID)) });
            }
          }
          entities.add(side1BaseEntity.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID },
              MessageCode.GEN014, new String[] { SIDE1_ID + " - " + relationships.getKey() });
        }
      }
    }
  }
  
  /**
   * get Config Data For Klass And Taxonomy
   * 
   * @param KlassesIds
   * @param taxonomyList
   * @param executionStatusTable
   * @param nonNatureKlasses
   * @param natureKlasses
   * @return
   * @throws Exception
   */
  
  protected IGetConfigDetailsByCodesResponseModel getConfigDataForKlassAndTaxonomy(
      List<String> KlassesIds, List<String> taxonomyList,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      List<String> nonNatureKlasses, List<String> natureKlasses) throws Exception
  {
    // Separate Nature and nonNature classes
    IKlassesAndTaxonomiesModel klassesAndTaxonomiesModel = new KlassesAndTaxonomiesModel();
    klassesAndTaxonomiesModel.setKlassIds(KlassesIds);
    klassesAndTaxonomiesModel.setTaxonomyIds(taxonomyList);
    IGetConfigDetailsByCodesResponseModel responseModel = getConfigDetailsByCodesStrategy
        .execute(klassesAndTaxonomiesModel);
    Map<String, Object> klassDetails = responseModel.getKlass();
    for (String klassId : klassesAndTaxonomiesModel.getKlassIds()) {
      Map<String, Object> klassInfo = (Map<String, Object>) klassDetails.get(klassId);
      if (klassInfo != null && klassInfo.get(CommonConstants.IS_NATURE)
          .equals(Boolean.TRUE)) {
        natureKlasses.add(klassId);
      }
      else {
        nonNatureKlasses.add(klassId);
      }
    }
    if (natureKlasses.size() > 1 || CollectionUtils.isEmpty(natureKlasses)) {
      executionStatusTable.addError(MessageCode.GEN013);
      throw new Exception();
    }
    return responseModel;
  }
  
  /**
   * Upload asset to swift server.
   * 
   * @param klassId
   * @param filePath
   * @param fileSource
   * @param assetUploadType
   * @return
   */
  protected IBulkUploadResponseAssetModel uploadAssetToServer(String klassId, String filePath,
      String fileSource, StringBuilder assetUploadType)
  {
    IBulkUploadResponseAssetModel bulkUploadResponseAssetModel = null;
    if (filePath != null && !filePath.trim()
        .equals("") && fileSource != null && !fileSource.trim()
            .equals("")) {
      try {
        List<IMultiPartFileInfoModel> multiPartFileInfoModelList = new ArrayList<>();
        IMultiPartFileInfoModel multiPartFileInfoModel = DiTransformationUtils
            .getAssetDataFromFile(filePath, fileSource);
        String eligibleAssetExtensionType = checkEligibleAssetExtensionTypes(
            multiPartFileInfoModel);
        if (eligibleAssetExtensionType == null) {
          throw new Exception();
        }
        assetUploadType.append(eligibleAssetExtensionType);
        multiPartFileInfoModelList.add(multiPartFileInfoModel);
        IUploadAssetModel uploadAssetModel = new UploadAssetModel();
        uploadAssetModel.setMode(null);
        uploadAssetModel.setMultiPartFileInfoList(multiPartFileInfoModelList);
        uploadAssetModel.setKlassId(klassId);
        uploadAssetModel.setIsUploadedFromInstance(Boolean.TRUE);
        bulkUploadResponseAssetModel = uploadAssetToServer.execute(uploadAssetModel);
      }
      catch (Exception e) {
        RDBMSLogger.instance()
            .exception(e);
      }
    }
    return bulkUploadResponseAssetModel;
  }
  
  /**
   * Check eligible asset extension type
   * 
   * @param multiPartFileInfoModel
   * @return
   * @throws Exception
   */
  private String checkEligibleAssetExtensionTypes(IMultiPartFileInfoModel multiPartFileInfoModel)
      throws Exception
  {
    Map<String, List<String>> assetExtensions = getAllAssetExtensions
        .execute(new IdsListParameterModel())
        .getAssetExtensions();
    String originalFilename = multiPartFileInfoModel.getOriginalFilename();
    String extension = "." + originalFilename.split(Pattern.quote("."))[1];
    for (Entry<String, List<String>> eligibleExtensionTypes : assetExtensions.entrySet()) {
      if (eligibleExtensionTypes.getValue()
          .contains(extension)) {
        return eligibleExtensionTypes.getKey();
      }
    }
    return null;
  }
  
  /**
   * Get all variantContext's code and type
   * 
   * @return
   */
  protected Map<String, String> getAllVariantContexts()
  {
    IGetConfigDataRequestModel model = new GetConfigDataRequestModel();
    model.setSearchColumn(ITransformationTask.LABEL);
    model.setSearchText("");
    IGetConfigDataEntityRequestModel entities = new GetConfigDataEntityRequestModel();
    IGetConfigDataEntityPaginationModel variantContexts = new GetConfigDataEntityPaginationModel();
    variantContexts.setFrom(0L);
    variantContexts.setSize(1000L);
    entities.setVariantContexts(variantContexts);
    model.setEntities(entities);
    try {
      IGetConfigDataResponseModel responseModel = getConfigDataStrategy.execute(model);
      IGetConfigDataEntityResponseModel variantContextsResponce = responseModel
          .getVariantContexts();
      List<IConfigEntityInformationModel> list = variantContextsResponce.getList();
      return list.stream()
          .collect(Collectors.toMap(IConfigEntityInformationModel::getCode,
              IConfigEntityInformationModel::getType));
    }
    catch (Exception e) {
      RDBMSLogger.instance()
          .exception(e);
    }
    return null;
  }
  
  /**
   * Get Is Duplicate Variant Allowed
   * 
   * @return
   */
  protected Boolean getIsDuplicateVariantAllowed(String code)
  {
    try {
      return getVariantContextService.execute(new IdParameterModel(code)).getIsDuplicateVariantAllowed();
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      return false;
    }
  }
  
  /**
   * upload Asset On Server
   * 
   * @param natureKlasses
   * @param baseEntity
   * @param fileSource
   * @param filePath
   * @return
   * @throws Exception
   */
  
  protected Map<String, String> uploadAssetOnServer(List<String> natureKlasses,
      IBaseEntityDTO baseEntity, String fileSource, String filePath) throws Exception
  {
    StringBuilder assetUploadType = new StringBuilder();
    IBulkUploadResponseAssetModel uploadedAssetDetails = uploadAssetToServer(natureKlasses.get(0),
        filePath, fileSource, assetUploadType);
    Map<String, String> assetMetadataAttributes = new HashMap<>();
    if (uploadedAssetDetails != null) {
      IAssetKeysModel assetKeysModel = uploadedAssetDetails.getSuccess()
          .getAssetKeysModelList()
          .get(0);
      DiTransformationUtils.prepareAssetExtenstionData(baseEntity, assetKeysModel,
          assetUploadType.toString());
      assetMetadataAttributes
          .putAll(DiTransformationUtils.convertAssetMetadataToMap(assetKeysModel));
    }
    return assetMetadataAttributes;
  }
  
  /**
   * Prepare embedded baseEntityDTO Also set parent and master parent info
   * 
   * @param variantsMap
   * @param variant
   * @param executionStatusTable
   * @param entitiesToBeCreated
   * @param natureKlassDTO
   * @param localeCatalogDAO
   * @param diDataType
   * @param workflowModel 
   * @param configDataForKlassAndTaxonomy 
   * @return
   * @throws Exception
   */
  protected IBaseEntityDTO getEmbeddedBaseEntityDTO(
      Map<String, List<Map<String, Object>>> variantsMap, Map<String, Object> variant,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      List<String> entitiesToBeCreated, IClassifierDTO natureKlassDTO,
      ILocaleCatalogDAO localeCatalogDAO, DiDataType diDataType, WorkflowModel workflowModel, IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy) throws Exception
  {
    String id, parentId, topParentId, contextId;
    if (DiDataType.EXCEL.equals(diDataType)) {
      id = (String) variant.remove(ID_COLUMN);
      parentId = (String) variant.remove(PARENT_ID_COLUMN);
      topParentId = (String) variant.remove(MASTER_ID_COLUMN);
      contextId = (String) variant.remove(CONTEXT_ID_COLUMN);
      if(StringUtils.isEmpty(contextId)) {
        Map<String, String> klassDetails = (Map<String, String>) configDataForKlassAndTaxonomy.getKlass().get(natureKlassDTO.getCode());
        contextId = klassDetails.get(CommonConstants.CONFIG_CONTEXT_ID);
      }
    }
    else {
      parentId = (String) variant.get(PARENT_ID);
      topParentId = (String) variant.get(CONTENT_ID);
      id = (String) variant.get(VARIANT_ID);
      Map<String, Object> contextualInputData = (Map<String, Object>) variant.get(CONTEXT);
      if (!MapUtils.isEmpty(contextualInputData) && !StringUtils.isEmpty((String) contextualInputData.get(CONTEXT_ID))) {
        contextId = (String) contextualInputData.get(CONTEXT_ID);
      }
      else {
        Map<String, String> klassDetails = (Map<String, String>) configDataForKlassAndTaxonomy.getKlass().get(natureKlassDTO.getCode());
        contextId = klassDetails.get(CommonConstants.CONFIG_CONTEXT_ID);
      }
    }
    
    IBaseEntityDTO baseEntity = null;
    // Create Context
    Map<String, String> variantContexts = getAllVariantContexts();
    IContextDTO contextDTO = RDBMSUtils.getContextDTO(contextId,
        PropertyRecordBuilder.getContextTypeByType(variantContexts.get(contextId)));
    
    // If parent id is not mentioned then select master id as parent id
    IBaseEntityDTO parentBaseEntityDTO = null;
    if (StringUtils.isBlank(parentId) && !StringUtils.isBlank(topParentId)) {
      parentId = topParentId;
    }
    
    if (!parentId.isBlank()) {
      parentBaseEntityDTO = localeCatalogDAO.getEntityByID(parentId);
    }
    else {
      executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN016, new String[] { id });
      return null;
    }
    
    boolean isVariantAlreadyExists = false;
    if (!StringUtils.isBlank(id)) {
      baseEntity = localeCatalogDAO.getEntityByID(id);
      if (baseEntity != null) {
        isVariantAlreadyExists = true;
      }
    }
    
    // Get or set the action
    String action = (String) variant.get(DiDataType.EXCEL.equals(diDataType) ? ACTION : "action");
    if (action != null) {
      action = action.toUpperCase();
      // check for invalid action in action column.
      if (!Arrays.asList(CREATE, UPDATE).contains(action)) {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN050, new String[] { action, id });
        return null;
      }
    }
    else if (action == null) {
      action = isVariantAlreadyExists ? UPDATE : CREATE;
    }
  
    // Validate the actions
    if (CREATE.equals(action) && isVariantAlreadyExists) {
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN048, new String[] { id });
      return null;
    }
    else if (UPDATE.equals(action) && !isVariantAlreadyExists) {
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN049, new String[] { id });
      return null;
    }
    
    if (baseEntity == null) {
      // if embedded variant is not present then create new variant
      IBaseEntityDTOBuilder baseEntityDTOBuilder ;
      
      if (parentBaseEntityDTO != null) {
        baseEntityDTOBuilder = localeCatalogDAO
        .newBaseEntityDTOBuilder(id, parentBaseEntityDTO.getBaseType(), natureKlassDTO).contextDTO(contextDTO);
        baseEntityDTOBuilder = baseEntityDTOBuilder.parentIID(0, EmbeddedType.CONTEXTUAL_CLASS)
            .parentID(parentBaseEntityDTO.getBaseEntityID());
      }
      else if (entitiesToBeCreated.contains(parentId) || variantsMap.containsKey(parentId)) {
        // set dummy parent id
        baseEntityDTOBuilder = localeCatalogDAO
            .newBaseEntityDTOBuilder(id, baseEntityIDWithBaseType.get(parentId), natureKlassDTO).contextDTO(contextDTO);
        baseEntityDTOBuilder = baseEntityDTOBuilder.parentIID(0, EmbeddedType.CONTEXTUAL_CLASS).parentID(parentId);
      }
      else {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN016, new String[] { parentId, id });
        return null;
      }
      // set top Parent
      if (StringUtils.isNotBlank(parentId) && StringUtils.isNotBlank(topParentId)) {
        IBaseEntityDTO topParent = localeCatalogDAO.getEntityByID(topParentId);
        if (topParent != null) {
          baseEntityDTOBuilder = baseEntityDTOBuilder.topParentID(topParent.getBaseEntityID());
        }
        else if (entitiesToBeCreated.contains(topParentId) || variantsMap.containsKey(topParentId)) {
          baseEntityDTOBuilder = baseEntityDTOBuilder.topParentID(topParentId);
        }
        else {
          executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN016, new String[] { topParentId, id });
        }
      }
      ITransactionData transactionData = (ITransactionData) workflowModel.getTransactionData();
      baseEntityDTOBuilder.endpointCode(transactionData.getEndpointId());
      baseEntity = baseEntityDTOBuilder.build();
    }
    return baseEntity;
  }
  
  /**
   * This method prepare attributeId VS isLangugeDependent map which is used to
   * differentiate language dependent and language independent attributes
   * 
   * @param languageDependentAttribute
   * @param attributes
   * @throws Exception
   */
  public void prepareLanguageDependentMapforAttribute(
      Map<String, Boolean> languageDependentAttribute, Map<String, String> attributes)
      throws Exception
  {
    if (!attributes.keySet()
        .isEmpty()) {
      IIdsListParameterModel idList = new IdsListParameterModel();
      idList.setIds(new ArrayList<>(attributes.keySet()));
      Collection<? extends IAttribute> list = getAttributesByIdsStrategy.execute(idList)
          .getList();
      for (IAttribute attribute : list) {
        languageDependentAttribute.put(attribute.getCode(), attribute.getIsTranslatable());
      }
    }
  }
  
  /**
   * @param workflowTaskModel
   * @param baseType
   * @param id
   * @param natureKlass
   * @param localeCatalogDAO
   * @return
   * @throws RDBMSException
   */
  public IBaseEntityDTO getOrCreateBaseEntity(WorkflowTaskModel workflowTaskModel, BaseType baseType, String id,
      IClassifierDTO natureKlass, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException
  {
    ITransactionData transactionData = (ITransactionData) workflowTaskModel.getWorkflowModel().getTransactionData();
    String endpointId = transactionData.getEndpointId();
    endpointId = endpointId == null ? "null" : endpointId;
    String organizationId = transactionData.getOrganizationId();
    String targetOrgCode = organizationId.equals(IStandardConfig.STANDARD_ORGANIZATION_CODE)
        ? IStandardConfig.STANDARD_ORGANIZATION_RCODE : organizationId;
    long entityIID = localeCatalogDAO.getEntityIID(id, transactionData.getPhysicalCatalogId(), targetOrgCode, endpointId);
    
    IBaseEntityDTO baseEntity = entityIID != -1 ? localeCatalogDAO.getEntityByIID(entityIID)
        : localeCatalogDAO.newBaseEntityDTOBuilder(id, baseType, natureKlass).endpointCode(transactionData.getEndpointId()).build();
    return baseEntity;
  }
}
