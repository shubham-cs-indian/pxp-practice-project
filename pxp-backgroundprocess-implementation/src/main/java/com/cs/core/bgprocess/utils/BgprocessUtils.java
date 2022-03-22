package com.cs.core.bgprocess.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.standard.IConfigMap;
import com.cs.constants.Constants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.data.Text;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRuleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.usecase.klassinstance.DeleteKlassInstances;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.rdbms.assetpurge.dto.AssetPurgeWrapperDTO;
import com.cs.dam.rdbms.assetpurge.idto.IAssetPurgeWrapperDTO;
import com.cs.di.workflow.trigger.standard.BusinessProcessTriggerModel;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel;
import com.cs.utils.BaseEntityUtils;

public class BgprocessUtils {
  
  public static final String DELETE__KPI = "delete from pxp.baseentitykpirulelink kv where "
      + "kv.baseentityiid = ? ";

  private static final String GET_BASE_ENITY_IIDS_FROM_CLASSIFIER_IID = "Select baseentityiid from pxp.baseentity where classifieriid in (%s) and ismerged != true "
      + "UNION ALL "
      + "Select becl.baseentityiid from pxp.baseentityclassifierlink becl join pxp.baseentity be on becl.baseentityiid = be.baseentityiid and be.ismerged != true where otherclassifieriid in (%s)";
  
  public static void getBaseEntityIIDs(Set<String> classifierCodes, Set<Long> baseEntityIIDs) throws RDBMSException
  {
  
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      Set<Long> classifierIIDs= new HashSet<>();
      String classifierQuery = String.format("Select classifieriid from pxp.classifierconfig where classifiercode in (%s)",Text.join(",", classifierCodes, "'%s'"));
      PreparedStatement stmt = currentConn.prepareStatement(classifierQuery);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        classifierIIDs.add(result.getLong("classifieriid"));
      }
      
      String join = Text.join(",", classifierIIDs);
      String baseEntityIIDsQuery = String.format(GET_BASE_ENITY_IIDS_FROM_CLASSIFIER_IID, join, join);
      stmt = currentConn.prepareStatement(baseEntityIIDsQuery);
      stmt.execute();
      result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        baseEntityIIDs.add(result.getLong("baseentityiid"));
      }
    });
  }
  
  public static void deleteChildrens(List<Long> batchEntityIIDs, String userId, ILocaleCatalogDAO localeCatalogDAO)
      throws Exception, CSInitializationException, RDBMSException, CSFormatException
  {
    List<IBaseEntityDTO> childrens = new ArrayList<>();
    
    getChildrens(batchEntityIIDs, localeCatalogDAO, childrens);
    
    for (IBaseEntityDTO children : childrens) {
      deleteChildren(children, localeCatalogDAO);
    }
  }
  
  private final static String QUERY_TO_REMOVE_CHILDREN = "select baseentityiid from pxp.baseentity where parentiid IN (%s) and ismerged != true";
  public static void getChildrens(List<Long> baseEntityIIDs, ILocaleCatalogDAO localeCatalogDAO, List<IBaseEntityDTO> childrens) throws Exception {
    List<Long> childrenIIds = new ArrayList<Long>();
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      String parentiid = Text.join(",", baseEntityIIDs);
      String finalQuery = String.format(QUERY_TO_REMOVE_CHILDREN, parentiid);
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        childrenIIds.add(result.getLong("baseentityiid"));
      }
    });
    if (!childrenIIds.isEmpty()) {
      getChildrens(childrenIIds, localeCatalogDAO, childrens);
      for (Long baseEntityIID : childrenIIds) {
        childrens.add(prepareBaseEntity(baseEntityIID, localeCatalogDAO));
      }
    }
  }
  
  private static IBaseEntityDTO prepareBaseEntity(Long baseEntityIID, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException, SQLException, CSFormatException
  {
    IBaseEntityDTO baseEntityDTO = localeCatalogDAO.getEntityByIID(baseEntityIID);
    BaseEntityDAO baseEntityDAO = (BaseEntityDAO) localeCatalogDAO.openBaseEntity(baseEntityDTO);
    
    Collection<IPropertyDTO> properties = localeCatalogDAO.getAllEntityProperties(baseEntityDTO.getBaseEntityIID());
    localeCatalogDAO.applyLocaleInheritanceSchema(baseEntityDTO.getLocaleIds());
    baseEntityDAO.loadAllPropertyRecords(properties.toArray(new IPropertyDTO[0]));
    baseEntityDAO.getAllImmediateChildren(baseEntityDTO.getBaseEntityIID());
    
    return baseEntityDTO;
    
  }

  public static void deleteBaseEntity(Long baseEntityIID, BaseEntityDAO parentEntityDAO, IUserSessionDTO userSession, IAssetPurgeWrapperDTO assetPurgeWrapperDTO) throws Exception
  {
    IUserSessionDAO userSessionDao = RDBMSAppDriverManager.getDriver().newUserSessionDAO();
    ILocaleCatalogDAO catalogDao = userSessionDao.openLocaleCatalog(userSession, baseEntityIID);    
    IBaseEntityDTO entity = catalogDao.getEntityByIID(baseEntityIID);
    BaseEntityDAO baseEntityDAO = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
    if(parentEntityDAO != null) 
      parentEntityDAO.removeChildren(EmbeddedType.CONTEXTUAL_CLASS, catalogDao.getEntityByIID(baseEntityIID));
    
    boolean isAssetInstance = entity.getBaseType().equals(IBaseEntityIDDTO.BaseType.ASSET);
    if (isAssetInstance) {
      DeleteKlassInstances deleteKlassInstances =  BGProcessApplication.getApplicationContext().getBean(DeleteKlassInstances.class);
      deleteKlassInstances.preProcessData(baseEntityDAO);
    }
    
    TransactionThreadData transactionThread = BGProcessApplication.getApplicationContext().getBean(TransactionThreadData.class);
    Boolean isDeleteFromDICatalog = transactionThread.getTransactionData().getPhysicalCatalogId().equals(Constants.DATA_INTEGRATION_CATALOG_IDS);
    baseEntityDAO.delete(isDeleteFromDICatalog);
    
    if(!isAssetInstance) {
      catalogDao.postDeleteUpdate(baseEntityDAO.getBaseEntityDTO());
    }
    updateBaseEntityKpi(baseEntityIID, catalogDao);
    RDBMSComponentUtils rdbmsUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
    rdbmsUtils.getLocaleCatlogDAO().postDeleteUpdate(entity);
  }  
  
  public static void deleteChildren(IBaseEntityDTO childrenDTO, ILocaleCatalogDAO localeCatalogDAO) throws Exception
  {
    IBaseEntityDAO childrenDAO = localeCatalogDAO.openBaseEntity(childrenDTO);
    boolean isAssetInstance = childrenDTO.getBaseType().equals(IBaseEntityIDDTO.BaseType.ASSET);
   
    if (isAssetInstance) {
      DeleteKlassInstances deleteKlassInstances =  BGProcessApplication.getApplicationContext().getBean(DeleteKlassInstances.class);
      deleteKlassInstances.preProcessData(childrenDAO);
    }
    
    TransactionThreadData transactionThread = BGProcessApplication.getApplicationContext().getBean(TransactionThreadData.class);
    Boolean isDeleteFromDICatalog = transactionThread.getTransactionData().getPhysicalCatalogId().equals(Constants.DATA_INTEGRATION_CATALOG_IDS);
    childrenDAO.deleteVariants(isDeleteFromDICatalog);
    
    if(!isAssetInstance) {
      localeCatalogDAO.postDeleteUpdate(childrenDTO);
    }
    updateBaseEntityKpi(childrenDTO.getBaseEntityIID(), localeCatalogDAO);
    RDBMSComponentUtils rdbmsUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
    rdbmsUtils.getLocaleCatlogDAO().postDeleteUpdate(childrenDTO);
  }
  
  public static void updateBaseEntityKpi(Long baseEntityIID, ILocaleCatalogDAO localeCatalogDAO)
      throws RDBMSException
  {
    
    IRuleCatalogDAO ruleCatalogDAO = localeCatalogDAO.openRuleDAO();
    Map<Long, List<Long>> targetEntitiesIIDs = ruleCatalogDAO
        .getAllTargetEntitiesForKpiUniqueness(baseEntityIID);
    
    for (Long ruleExpressionIID : targetEntitiesIIDs.keySet()) {
      List<Long> listOfTargetEntityIIDs = targetEntitiesIIDs.get(ruleExpressionIID);
      if (listOfTargetEntityIIDs.size() == 1) {
        ruleCatalogDAO.updateTargetBaseEntityforUniquenessBlock(listOfTargetEntityIIDs,
            ruleExpressionIID, (double) 1);
      }
    }
    ruleCatalogDAO.deleteKpiOnBaseEntityDelete(baseEntityIID);
    ruleCatalogDAO.deleteFromKpiUniquenessViolation(baseEntityIID);
  }  
  
  public static IBusinessProcessTriggerModel getBusinessProcessModelForPropetiesSave(
      IBaseEntityDAO baseEntityDao, List<String> changedAttributeIds, List<String> changedTagIds)
      throws Exception, RDBMSException
  {
    if (changedAttributeIds.isEmpty() && changedTagIds.isEmpty()) {
      return null;
    }
    IBusinessProcessTriggerModel businessProcessEventModel = new BusinessProcessTriggerModel();
    BaseType baseType = baseEntityDao.getBaseEntityDTO().getBaseType();
    businessProcessEventModel.setKlassInstanceId(String.valueOf(baseEntityDao.getBaseEntityDTO().getBaseEntityIID()));
    switch (baseType) {
      case ARTICLE :
        businessProcessEventModel.setBusinessProcessActionType(WorkflowUtils.UseCases.SAVEARTIKLE.actionType);
        break;
      case ASSET :
        businessProcessEventModel.setBusinessProcessActionType(WorkflowUtils.UseCases.SAVEASSET.actionType);
        break;
    }
    businessProcessEventModel.setActionSubType(IBusinessProcessTriggerModel.ActionSubTypes.AFTER_PROPERTIES_SAVE);
    businessProcessEventModel.setBaseType(IConfigMap.getBaseTypeClassPath(baseType));
    businessProcessEventModel.setAttributeIds(changedAttributeIds);
    businessProcessEventModel.setTagIds(changedTagIds);
    businessProcessEventModel.setKlassIds(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDao));
    businessProcessEventModel.setTaxonomyIds(
        BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(baseEntityDao.getClassifiers()));
    return businessProcessEventModel;
  }
}
