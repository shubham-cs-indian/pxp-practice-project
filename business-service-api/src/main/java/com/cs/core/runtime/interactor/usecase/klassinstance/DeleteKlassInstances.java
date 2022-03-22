package com.cs.core.runtime.interactor.usecase.klassinstance;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.Constants;
import com.cs.constants.DAMConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.ProductDeleteDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IProductDeleteDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveDeletePermissionForArticle;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveDeletePermissionForAsset;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.exception.marketinstance.UserNotHaveDeletePermissionForMarket;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveDeletePermission;
import com.cs.core.runtime.interactor.exception.supplierinstance.UserNotHaveDeletePermissionForSupplier;
import com.cs.core.runtime.interactor.exception.textassetinstance.UserNotHaveDeletePermissionForTextAsset;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.DeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstanceDetails;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstancesRequestModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.strategy.usecase.klassinstance.IDeleteKlassInstances;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.dam.AssetUtils;

@Component
public class DeleteKlassInstances implements IDeleteKlassInstances {
  
  @Autowired
  protected RDBMSComponentUtils             rdbmsComponentUtils;
  
  @Autowired
  protected TransactionThreadData           transactionThreadData;
  
  @Autowired
  protected IFetchAssetConfigurationDetails fetchAssetConfigurationDetails;
  
  @Autowired
  protected ISessionContext                 context;
  
  @Autowired
  protected GoldenRecordUtils               goldenRecordUtils;
  
  @Autowired
  protected KlassInstanceUtils              klassInstanceUtils;
  
  
  @Override
  public IDeleteInstancesResponseModel execute(IDeleteKlassInstancesRequestModel deleteRequest) throws Exception
  {
    IDeleteInstancesResponseModel deleteResponseModel = new DeleteInstancesResponseModel();
    Boolean isDeleteFromArchive = deleteRequest.getIsDeleteFromArchive();
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();

    List<String> successfulIds = new ArrayList<>();
    deleteResponseModel.setSuccess(successfulIds);
    IExceptionModel failure = new ExceptionModel();

    if (isDeleteFromArchive) {
      List<String> baseEntityIIDs = new ArrayList<>();
      for (IDeleteInstanceDetails instanceInformation : deleteRequest.getAllDeleteInstanceDetails()) {
        baseEntityIIDs.add(instanceInformation.getId());
      }

      List<IBaseEntityDTO> baseEntitiesFromArchive = localeCatlogDAO.getBaseEntitiesFromArchive(baseEntityIIDs);
      for (IBaseEntityDTO baseEntity : baseEntitiesFromArchive) {
        try {
          long starTime2 = System.currentTimeMillis();
          if (baseEntity.getBaseType().equals(BaseType.ASSET)) {
            moveAssetToPurge(baseEntity.getEntityExtension());
          }
          deleteChildrensFromArchive(baseEntity, localeCatlogDAO);
          localeCatlogDAO.deleteFromArchive(baseEntity.getBaseEntityIID()); // deleting main entity.

          RDBMSLogger.instance().debug("NA|RDBMS|" + this.getClass().getSimpleName() + "|executeInternal|delete| %d ms",
              System.currentTimeMillis() - starTime2);

          successfulIds.add(Long.toString(baseEntity.getBaseEntityIID()));
          
          localeCatlogDAO.postDeleteUpdate(baseEntity);
        }
        catch (Exception cause) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, cause, Long.toString(baseEntity.getBaseEntityIID()), null);
          throw cause;
        }
      }
    }
    else {
      
      IProductDeleteDTO entryData = new ProductDeleteDTO();
      List<Long> linkedArticlesWithoutDefaultImage = new ArrayList<>();
      for (IDeleteInstanceDetails instanceInformation : deleteRequest.getAllDeleteInstanceDetails()) {
        long starTime1 = System.currentTimeMillis();
        long baseEntityIID = Long.parseLong((String) instanceInformation.getId());
        IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
        try {
          Boolean canDelete = deleteRequest.getHasDeletePermission();
          if (canDelete == null) {
            String classifierCode = (String) instanceInformation.getClassifierCode();
            canDelete = deleteRequest.getGlobalPermissions().get(classifierCode).getCanDelete();
          }
          if (!canDelete) {
            throw new UserNotHaveDeletePermission();
          }
          if (Constants.ASSET_INSTANCE_BASE_TYPE.equalsIgnoreCase((String) instanceInformation.getBaseType())) {
            preProcessData(baseEntityDAO);
            linkedArticlesWithoutDefaultImage.addAll(rdbmsComponentUtils.getLocaleCatlogDAO().getArticlesWithGivenDefaultImage(Collections.singletonList(instanceInformation.getId())));
          }
          
          List<Long> sourceBaseEntityIIDs = getGoldenRecordSourceIds(baseEntityDAO, instanceInformation.getId()); 
          
          RDBMSLogger.instance().debug("NA|RDBMS|" + this.getClass().getSimpleName() + "|executeInternal|openBaseEntity| %d ms",
              System.currentTimeMillis() - starTime1);
          long starTime2 = System.currentTimeMillis();
          klassInstanceUtils.deleteCoupledRecord(baseEntityIID);
         
          Boolean isDeleteFromDICatalog = transactionThreadData.getTransactionData().getPhysicalCatalogId().equals(Constants.DATA_INTEGRATION_CATALOG_IDS);
          baseEntityDAO.delete(isDeleteFromDICatalog);
          
          // delete task instances
          localeCatlogDAO.openTaskDAO().deleteTasksByBaseEntityIID(baseEntityIID);
          RDBMSLogger.instance().debug("NA|RDBMS|" + this.getClass().getSimpleName() + "|executeInternal|delete| %d ms",
              System.currentTimeMillis() - starTime2);
          successfulIds.add((String) instanceInformation.getId());

          entryData.getBaseEntityIIDs().add(baseEntityIID);
          localeCatlogDAO.postDeleteUpdate(baseEntityDAO.getBaseEntityDTO());
          
          if (!sourceBaseEntityIIDs.isEmpty()) {
            entryData.getBaseEntityIIDs().addAll(sourceBaseEntityIIDs);
            entryData.getSourceEntityIIDs().addAll(sourceBaseEntityIIDs);
          }
          
          goldenRecordUtils.initiateEvaluateGoldenRecordBucket(baseEntityDAO.getBaseEntityDTO(), true);
        }
        catch (UserNotHaveDeletePermission e) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure,
              getUserNotHaveDeletePermissionException((String) instanceInformation.getBaseType()),
              (String) instanceInformation.getId(), null);
        }
        catch (Exception cause) {
          throw cause;
        }
      }
      entryData.setCatalogCode(localeCatlogDAO.getLocaleCatalogDTO().getCatalogCode());
      entryData.setLocaleID(rdbmsComponentUtils.getDataLanguage());
      entryData.setOrganizationCode(transactionThreadData.getTransactionData().getOrganizationId());
      
      handleDefaultImage(linkedArticlesWithoutDefaultImage);
      BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), "PRODUCT_DELETE_HANDLER", "", IBGProcessDTO.BGPPriority.HIGH,
          new JSONContent(entryData.toJSON()));
    }
    deleteResponseModel.setFailure(failure);
    
    return deleteResponseModel;
  }
  
  public void preProcessData(IBaseEntityDAO baseEntityDAO) throws RDBMSException, Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel();
    idParameterModel.setType(DAMConstants.ASSET_KLASS);
    IAssetConfigurationDetailsResponseModel assetModel = fetchAssetConfigurationDetails.execute(idParameterModel);
    
    if (!assetModel.getDetectDuplicate()) {
      // discarding duplicate detection in case of DetectDuplicate is false
      return;
    }
    
    Map<String, Object> transactionDataMap = new HashMap<>();
    TransactionData transactionData = transactionThreadData.getTransactionData();
    transactionDataMap.put(ITransactionData.ENDPOINT_ID, transactionData.getEndpointId());
    transactionDataMap.put(ITransactionData.ORGANIZATION_ID, transactionData.getOrganizationId());
    transactionDataMap.put(ITransactionData.PHYSICAL_CATALOG_ID, transactionData.getPhysicalCatalogId());
    
    Set<Long> baseEntityIIds = new HashSet<>();
    Set<Long> duplicateAssetIIds = new HashSet<>();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    baseEntityDAO.getChildren(EmbeddedType.CONTEXTUAL_CLASS);
    baseEntityIIds.add(baseEntityDTO.getBaseEntityIID());
    baseEntityIIds.addAll(baseEntityDTO.getChildrenIIDs());
    
    // Mark isDuplicate column of entities to false if no longer duplicate.
    for (long baseEntityIId : baseEntityIIds) {
      IBaseEntityDAO entityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIId);
      String hash = entityDAO.getBaseEntityDTO().getHashCode();
      long duplicateAssetIId = AssetUtils.handleDuplicate(hash, entityDAO, transactionDataMap, baseEntityIId);
      if (duplicateAssetIId != 0 && duplicateAssetIId != 1) {
        duplicateAssetIIds.add(duplicateAssetIId);
      }
    }
    if (!duplicateAssetIIds.isEmpty()) {
      baseEntityDAO.markAssetsDuplicateByIIds(duplicateAssetIIds, false);
      for (Long duplicateAssetIId : duplicateAssetIIds) {
        rdbmsComponentUtils.getLocaleCatlogDAO()
            .postUsecaseUpdate(duplicateAssetIId, IEventDTO.EventType.ELASTIC_UPDATE);
      }
    }
    
  }

  protected UserNotHaveDeletePermission getUserNotHaveDeletePermissionException(String baseType)
  {
    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        return new UserNotHaveDeletePermissionForArticle();
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        return new UserNotHaveDeletePermissionForAsset();
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        return new UserNotHaveDeletePermissionForMarket();
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        return new UserNotHaveDeletePermissionForSupplier();
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        return new UserNotHaveDeletePermissionForTextAsset();
    }
    return null;
  }
    
  private void handleDefaultImage(List<Long> linkedArticlesWithoutDefaultImage) throws Exception
  {
    if(linkedArticlesWithoutDefaultImage.isEmpty())
      return;
    
    IProductDeleteDTO defaultImageHandlerDTO = new ProductDeleteDTO();
    ILocaleCatalogDTO localeCatalogDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    defaultImageHandlerDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    defaultImageHandlerDTO.setLocaleID(rdbmsComponentUtils.getDataLanguage());
    defaultImageHandlerDTO.setBaseEntityIIDs(linkedArticlesWithoutDefaultImage);
    defaultImageHandlerDTO.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    defaultImageHandlerDTO.setUserId(rdbmsComponentUtils.getUserID());
    
    BGPDriverDAO.instance()
    .submitBGPProcess(rdbmsComponentUtils.getUserID(), "ASSIGN_DEFAULT_IMAGE_ON_ASSET_DELETION",
        "", IBGProcessDTO.BGPPriority.HIGH, new JSONContent(defaultImageHandlerDTO.toJSON()));
  }
  
  private void deleteChildrensFromArchive(IBaseEntityDTO baseEntity, ILocaleCatalogDAO localeCatalogDAO)
      throws RDBMSException
  {
    Set<Long> iidsToDelete = baseEntity.getChildrenIIDs();
   
    if(!iidsToDelete.isEmpty()) {
      List<String> iids = iidsToDelete.stream().map(String::valueOf).collect(Collectors.toList());
      List<IBaseEntityDTO> entities = localeCatalogDAO.getBaseEntitiesFromArchive(iids);
      for(IBaseEntityDTO entity : entities) {
        if (entity.getBaseType().equals(BaseType.ASSET)) {
          moveAssetToPurge(entity.getEntityExtension());
        }
        deleteChildrensFromArchive(entity, localeCatalogDAO);
        localeCatalogDAO.deleteFromArchive(entity.getBaseEntityIID());
        localeCatalogDAO.postDeleteUpdate(entity);
      }
    }
  }
  
  /** Put entry into assetstobepurged
   * @param entityExtension
   * @throws RDBMSException
   */
  public void moveAssetToPurge(IJSONContent entityExtension) throws RDBMSException
  {
    String container = entityExtension.getInitField("type", "");
    String assetObjectKey = entityExtension.getInitField("assetObjectKey", "");
    String thumbKey = entityExtension.getInitField("thumbKey", "");
    String previewImageKey = entityExtension.getInitField("previewImageKey", "");
    
    if (!container.isEmpty() && !assetObjectKey.isEmpty()) {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        String Q_INSERT_INTO_PURGED_ASSET= "insert into pxp.assetstobepurged (assetObjectKey,thumbKey,previewImageKey,type) values (?,?,?,?)";
        PreparedStatement ps = currentConn.prepareStatement(Q_INSERT_INTO_PURGED_ASSET);
        ps.setString(1, assetObjectKey);
        ps.setString(2, thumbKey.isEmpty() ? null : thumbKey);
        ps.setString(3, previewImageKey.isEmpty() ? null : previewImageKey);
        ps.setString(4, container);
        ps.executeUpdate();
      });
    }
  }
  
  private List<Long> getGoldenRecordSourceIds(IBaseEntityDAO baseEntityDAO, String baseEntityIID)
      throws RDBMSException
  {
    List<Long> sourceBaseEntityIIds = new ArrayList<Long>();
    Set<IClassifierDTO> otherClassifiers = baseEntityDAO.getBaseEntityDTO()
        .getOtherClassifiers();
    
    for (IClassifierDTO otherClassifier : otherClassifiers) {
      if (otherClassifier.getCode().equals(SystemLevelIds.GOLDEN_ARTICLE_KLASS)) {
        GoldenRecordBucketDAO dao = new GoldenRecordBucketDAO();
        sourceBaseEntityIIds = dao.getBaseEntityIidsLinkedWithGoldenRecordArticle(baseEntityIID);
        sourceBaseEntityIIds.remove(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID());
      }
    }
    
    return sourceBaseEntityIIds;
  }
}
