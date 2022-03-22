package com.cs.core.runtime.interactor.utils.klassinstance;

import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.strategy.usecase.user.IGetAllUsersStrategy;
import com.cs.core.exception.InvalidIdException;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.dao.MigrationDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IMigrationDAO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.process.idao.IRuleCatalogDAO;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.taxonomyinheritance.idao.ITaxonomyInheritanceDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * provides some utility methods to perform operation on RDBMS database
 * This class should be autowired.
 *
 */
@Component
public class RDBMSComponentUtils {
  
  @Autowired
  protected TransactionThreadData transactionThread;
  
  @Autowired
  protected ISessionContext       context;
  
  @Autowired
  protected IGetAllUsersStrategy  getAllUsersStrategy;

  protected ILocaleCatalogDAO localeCatalogDAO;
  
  public IUserDTO getCurrentUserDTO() throws Exception
  {
    return RDBMSUtils.newConfigurationDAO().createUser(context.getUserSessionDTO()
        .getUserName());
  }
  
  /**
   * Get user ID
   *
   * @return
   */
  public String getUserID()
  {
    return context.getUserId();
  }
  
  public long getUserIID()
  {
    return context.getUserSessionDTO().getUserIID();
  }
  
  /**
   * Get user name
   *
   * @return
   */
  public String getUserName()
  {
    return context.getUserName();
  }
  
  public String getDataLanguage()
  {
    return  transactionThread.getTransactionData()
        .getDataLanguage();
  }
  
  public String getUiLanguage()
  {
    return  transactionThread.getTransactionData()
        .getUiLanguage();
  }
  
  private ILocaleCatalogDTO newLocaleCatlogDTO() throws RDBMSException
  {
    // TODO provide catalog code
    TransactionData transactionData = transactionThread.getTransactionData();
    ILocaleCatalogDTO localeCatalogDTO = RDBMSUtils.newUserSessionDAO().newLocaleCatalogDTO(transactionData.getDataLanguage(),
        transactionData.getPhysicalCatalogId(), transactionData.getOrganizationId());
    return localeCatalogDTO;
  }
  
  /**
   * 
   * @return lacaleCatalogDAO , it will take localeID from transactionThreadData data language and UserSessionDTO from sessionContext 
   * @throws Exception
   */
  public ILocaleCatalogDAO getLocaleCatlogDAO() throws RDBMSException
  {
    localeCatalogDAO = RDBMSUtils.newUserSessionDAO().openLocaleCatalog(context.getUserSessionDTO(), newLocaleCatlogDTO());
    return localeCatalogDAO;
  }

  /**
   *
   * @return lacaleCatalogDAO , it will take localeID from transactionThreadData data language and UserSessionDTO from sessionContext
   * @throws Exception
   */
  public IMigrationDAO getMigrationDAO() throws RDBMSException
  {
    return new MigrationDAO(context.getUserSessionDTO(), newLocaleCatlogDTO());
  }

  public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntities(IBaseEntityIDDTO.BaseType baseType)
      throws Exception
  {
    IRDBMSOrderedCursor<IBaseEntityDTO> allEntities =  getLocaleCatlogDAO()
        .getAllEntities(baseType);
    return allEntities;
  }
  
  /**
   * 
   * @param baseEntityIID
   * @param baseEntityID
   * @param baseType
   * @param natureClass
   * @return baseEntityDTO
   * @throws Exception
   */
  public IBaseEntityDTO newBaseEntityDTO(long baseEntityIID, String baseEntityID, BaseType baseType,
      IClassifierDTO natureClass) throws Exception
  {
    IBaseEntityDTO baseEntityDTO = getLocaleCatlogDAO()
        .newBaseEntityDTOBuilder(baseEntityID, baseType, natureClass)
        .baseEntityIID(baseEntityIID)
        .endpointCode(transactionThread.getTransactionData().getEndpointId())
        .build();
    return baseEntityDTO;
  }
  
  public ICollectionDTO newCollectionDTO(CollectionType collectionType, String collectionCode,
      String organizationCode, String parentId, boolean isPublic, IJSONContent searchCriteria, Set<Long> linkedBaseEntityIIDs) throws Exception
  {
    ICollectionDTO collectionDTO = getLocaleCatlogDAO().newCollectionDTOBuilder(collectionType, collectionCode, organizationCode)
        .parentIID(Long.parseLong(parentId))
        .isPublic(isPublic)
        .linkedBaseEntityIIDs(linkedBaseEntityIIDs)
        .searchCriteria(searchCriteria)
        .build();
    return collectionDTO;
  }
  
  /**
   * @param iid
   * @return it will baseEntityDTO with minimal information in it, It will take locale id (data-language) from transactionThreadData
   * @throws Exception
   */
  public IBaseEntityDTO getBaseEntityDTO(long iid) throws Exception
  {
    return getLocaleCatlogDAO().getEntityByIID(iid);
  }
  
  /**
   * 
   * @param baseEntityDTO
   * @return baseEntityDAO without fetching BaseEntityDTO from database
   * @throws Exception
   */
  public IBaseEntityDAO getBaseEntityDAO(IBaseEntityDTO baseEntityDTO)
      throws Exception
  {
    IBaseEntityDAO baseEntityDAO =  getLocaleCatlogDAO()
        .openBaseEntity(baseEntityDTO);
    return baseEntityDAO;
  }
  /**
   * 
   * @param iid
   * @return baseEntityDAO
   * @throws Exception
   */
  public IBaseEntityDAO getBaseEntityDAO(long iid) throws Exception
  {
    return getLocaleCatlogDAO().openBaseEntity(getBaseEntityDTO(iid));
  }
  
  /**
   * 
   * @param iid
   * @param localeID
   * @return baseEntityDAO with BaseEntityDTO
   * @throws Exception
   */
  public IBaseEntityDAO getBaseEntityDAO(long iid, String localeID) throws Exception
  {
    ILocaleCatalogDAO localeCatlogDAO = getLocaleCatlogDAO(localeID);
    return localeCatlogDAO.openBaseEntity(localeCatlogDAO.getEntityByIID(iid));
  }
  
  /**
   * 
   * @param iid
   * @param localeID, specify the localeID 
   * @return it will baseEntityDTO with minimal information in it for specific locale
   * @throws Exception
   */
  public IBaseEntityDTO getBaseEntityDTO(long iid, String localeID) throws Exception {
    IBaseEntityDTO baseEntityDTO =  getLocaleCatlogDAO(localeID).getEntityByIID(iid);
    return baseEntityDTO;
  } 
  
  /**
   * 
   * @param localeID
   * @return locale catalogDAO
   * @throws Exception
   */
  private ILocaleCatalogDAO getLocaleCatlogDAO(String localeID) throws Exception
  {
    IUserSessionDAO userSessionDAO = RDBMSUtils.newUserSessionDAO();
    TransactionData transactionData = transactionThread.getTransactionData();
    
    ILocaleCatalogDTO localeCatalogDTO = userSessionDAO.newLocaleCatalogDTO(localeID, transactionData.getPhysicalCatalogId(),
        transactionData.getOrganizationId());
    
    ILocaleCatalogDAO localeCatalogDAO = userSessionDAO
        .openLocaleCatalog(context.getUserSessionDTO(), localeCatalogDTO);
    return localeCatalogDAO;
  }
  
  /**
   * return taskRecordDAO
   * @return
   * @throws Exception
   */
  public ITaskRecordDAO openTaskDAO() throws Exception
  {
    ITaskRecordDAO taskRecordDAO =  getLocaleCatlogDAO()
        .openTaskDAO();
    return taskRecordDAO;
  }

  /**
   * 
   * @return ruleCatalogDAO
   * @throws Exception
   */
  public IRuleCatalogDAO openRuleDAO() throws Exception
  {
    return  getLocaleCatlogDAO().openRuleDAO();
  }
  
  /**
   * 
   * @param iid
   * @param languageInheritance
   * @return it will baseEntityDAO object with baseEntityDTO 
   * @throws Exception
   */
  public IBaseEntityDAO getBaseEntityDAO(long iid, List<String> languageInheritance)
      throws Exception
  {
    try {
      IBaseEntityDTO baseEntityDTO = getLocaleCatlogDAO(languageInheritance).getEntityByIID(iid);
      return getLocaleCatlogDAO(languageInheritance).openBaseEntity(baseEntityDTO);
    }
    catch (RDBMSException e) {
      if (e.getMessage()
          .contains("locale catalog doesn't match entity"))
        return getLocaleCatlogDAO().openAnyBaseEntity(getBaseEntityDTO(iid));
      else
        throw e;
    }
  }
  
  /**
   * 
   * @param iid
   * @return it will return the collection of properties related to baseEntity
   * @throws Exception
   */
  public Collection<IPropertyDTO> getAllBaseEntityProperties(long iid) throws Exception
  {
    Collection<IPropertyDTO> propertyDTOs = this.getLocaleCatlogDAO().getAllEntityProperties(iid);
    return propertyDTOs;
  }
  
  
  /**
   * @return new revision dao
   * @throws Exception
   */
  public IRevisionDAO getRevisionDAO() throws Exception
  {
    return RDBMSAppDriverManager.getDriver()
        .newRevisionDAO(context.getUserSessionDTO());
  }
  
  /**creating new revision for base entity 
   * 
   * @param baseEntityDTO
   * @return latest  revision number
   * @throws Exception
   */
  public int createNewRevision(IBaseEntityDTO baseEntityDTO, Integer versionsToMaintain) throws Exception
  {
    return getRevisionDAO().createNewRevision(baseEntityDTO, "", versionsToMaintain);
  }
  
  //TODO: Temporary Method.
  public int createNewRevisionAfterRollback(IBaseEntityDTO baseEntityDTO, Integer versionsToMaintain) throws Exception
  {
    return getRevisionDAO().createNewRevisionAfterRollback(baseEntityDTO, "", versionsToMaintain);
  }

/**
 * 
 * @param languageInheritance
 * @return localeCatalogDAO with languageInheritanceSchema
 * @throws RDBMSException
 * @throws Exception
 */
  public ILocaleCatalogDAO getLocaleCatlogDAO(List<String> languageInheritance) throws RDBMSException, Exception
  {
    ILocaleCatalogDTO localeCatlogDTO = newLocaleCatlogDTO();
    localeCatlogDTO.setLocaleInheritanceSchema(languageInheritance);
    
    ILocaleCatalogDAO localeCatalogDAO = RDBMSUtils.newUserSessionDAO()
        .openLocaleCatalog(context.getUserSessionDTO(), localeCatlogDTO);
    return localeCatalogDAO;
  }
  
  public ICollectionDAO getCollectionDAO()
      throws Exception
  {
    ICollectionDAO collectionDAO =  getLocaleCatlogDAO()
        .openCollection();
    return collectionDAO;
  }
  
  /**
   * @param entityIID
   * @param sourceEntityIID
   * @param relationshipIID
   * @return taxonomyInheritanceDTO
   * @throws Exception
   */
  public ITaxonomyInheritanceDTO newTaxonomyInheritanceDTO(long entityIID, long sourceEntityIID, long relationshipIID) throws Exception
  {
    ITaxonomyInheritanceDTO taxonomyInheritanceDTO = getLocaleCatlogDAO()
        .newTaxonomyInheritanceDTOBuilder(entityIID, sourceEntityIID, relationshipIID).build();
    return taxonomyInheritanceDTO;
  }
  
  /**
   * @param taxonomyInheritanceDTO
   * @return ITaxonomyInheritanceDAO
   * @throws Exception
   */
  public ITaxonomyInheritanceDAO getTaxonomyInheritanceDAO(ITaxonomyInheritanceDTO taxonomyInheritanceDTO) throws Exception
  {
    ITaxonomyInheritanceDAO taxonomyInheritanceDAO = getLocaleCatlogDAO().openTaxonomyInheritance(taxonomyInheritanceDTO);
    return taxonomyInheritanceDAO;
  }
  
  public ITaxonomyInheritanceDTO getTaxonomyInheritanceDTO(long entityIID) throws Exception
  {
    return getLocaleCatlogDAO().getTaxonomyConflict(entityIID);
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, String> getUserIdVsUserNameMap() throws Exception{
    Map<String, String> userIdVsName = new HashMap<>();
    List<IUserInformationModel> allUsersDetails = (List<IUserInformationModel>) getAllUsersStrategy.execute(null).getList();
    
    for(IUserInformationModel user: allUsersDetails) {
      userIdVsName.put(user.getId(), user.getUserName());
    }
    return userIdVsName;
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, String> getUserNameVsUserIdMap() throws Exception{
    Map<String, String> userNameVsId = new HashMap<>();
    List<IUserInformationModel> allUsersDetails = (List<IUserInformationModel>) getAllUsersStrategy.execute(null).getList();
    
    for(IUserInformationModel user: allUsersDetails) {
      userNameVsId.put(user.getUserName(), user.getId());
    }
    return userNameVsId;
  }
  
  public IBaseEntityDAO getEmptyBaseEntityDAO()
  {
    return new BaseEntityDAO(new LocaleCatalogDAO(new UserSessionDTO(),
        new LocaleCatalogDTO()), new UserSessionDTO(), new BaseEntityDTO());
  }

  /**
   * @param iid
   * @return baseEntityDAO
   * @throws Exception
   */
  public IBaseEntityDAO getEntityDAO(long iid) throws Exception
  {
    return getLocaleCatlogDAO().openBaseEntity(getEntity(iid));
  }

  /**
   * @param iid
   * @return baseEntityDAO
   * @throws Exception
   */
  public IBaseEntityDTO getEntity(long iid) throws Exception
  {
    List<IBaseEntityDTO> baseEntitiesByIIDs = localeCatalogDAO.getBaseEntitiesByIIDs(List.of(String.valueOf(iid)));
    if(baseEntitiesByIIDs.isEmpty()){
      throw new InvalidIdException();
    }
    return baseEntitiesByIIDs.get(0);
  }

  public Set<IPropertyRecordDTO> getEntityPropertyRecords(long iid, Collection<IPropertyDTO> propertyDTOs) throws Exception
  {
    Map<Long, Set<IPropertyRecordDTO>> propertiesMap = getLocaleCatlogDAO().getPropertyRecordsForEntities(
        Set.of(iid),
        propertyDTOs.toArray(new IPropertyDTO[]{}));
    return propertiesMap.get(iid) != null ? propertiesMap.get(iid) : new HashSet<>();
  }

}
