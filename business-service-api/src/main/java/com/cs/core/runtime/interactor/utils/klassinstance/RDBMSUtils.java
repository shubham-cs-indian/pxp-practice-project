package com.cs.core.runtime.interactor.utils.klassinstance;

import com.cs.config.standard.IConfigMap;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.*;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.dto.EntityRelationDTO;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import com.cs.utils.ContextUtil;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * provides some static utility methods to perform operation on RDBMS database
 */
public class RDBMSUtils {

  /**
   * @param prefix
   * @return new unique id with prefix
   * @throws RDBMSException
   */
  public static String newUniqueID(String prefix) throws RDBMSException
  {
    return RDBMSAppDriverManager.getDriver().newUniqueID(prefix);
  }

  /**
   * Create new user session DAO
   *
   * @return
   * @throws Exception
   */
  public static IUserSessionDAO newUserSessionDAO() throws RDBMSException
  {
    IUserSessionDAO userSessionDAO = RDBMSAppDriverManager.getDriver().newUserSessionDAO();
    return userSessionDAO;
  }

  /**
   * return configuration DAO
   *
   * @return
   * @throws Exception
   */
  public static IConfigurationDAO newConfigurationDAO() throws RDBMSException
  {
    IConfigurationDAO configurationDAO = RDBMSAppDriverManager.getDriver().newConfigurationDAO();
    return configurationDAO;
  }

  /**
   * Create classifier(i.e Klass, Taxonomy)
   *
   * @param classifierID
   * @param classifierCode
   * @param classifierType
   * @return
   * @throws Exception
   */
  public static IClassifierDTO createClassifier(String classifierCode, ClassifierType classifierType) throws Exception
  {
    IClassifierDTO classifierDTO = RDBMSUtils.newConfigurationDAO().createClassifier(classifierCode, classifierType);
    return classifierDTO;
  }

  public static List<Long> getBaseentityIIDsForTypes(List<Long> klassIds) throws Exception
  {
    return RDBMSUtils.newConfigurationDAO().getBaseentityIIDsForTypes(klassIds);
  }

  public static List<Long> getBaseentityIIDsForTaxonomies(List<Long> classifierIIDs) throws Exception
  {
    return RDBMSUtils.newConfigurationDAO().getBaseentityIIDsForTaxonomies(classifierIIDs);
  }

  public static List<Long> getBaseentityIIDsForLanguage(List<String> languageIds) throws Exception
  {
    return RDBMSUtils.newConfigurationDAO().getBaseentityIIDsForLanguage(languageIds);
  }

  /**
   * Use for already created context
   *
   * @param contextCode
   * @param type
   * @return
   * @throws Exception
   */
  public static IContextDTO getContextDTO(String contextCode, IContextDTO.ContextType type) throws Exception
  {
    IContextDTO contextDTO = RDBMSUtils.newConfigurationDAO().createContext(contextCode, type);
    return contextDTO;
  }

  public static IContextDTO getContextDTO(IVariantContext variantContext) throws Exception
  {
    ContextType contextType = ContextUtil.getContextTypeByType(variantContext.getType());
    IContextDTO contextDTO = RDBMSUtils.getContextDTO(variantContext.getCode(), contextType);
    return contextDTO;
  }

  public static IPropertyDTO createProperty(String propertyID, String propertyCode, String type) throws Exception
  {
    PropertyType propertyType = IConfigMap.getPropertyType(type);
    IPropertyDTO propertyDTO = RDBMSUtils.createProperty(propertyID, propertyCode, propertyType);
    return propertyDTO;
  }

  /**
   * Create new property (ATTRIBUTE, TAG, RELATIONSHIP)
   *
   * @param propertyID
   * @param propertyCode
   * @param propertyType
   * @return
   * @throws Exception
   */
  public static IPropertyDTO createProperty(String propertyID, String propertyCode, PropertyType propertyType) throws Exception
  {
    IPropertyDTO propertyDTO = RDBMSUtils.newConfigurationDAO().createProperty(propertyCode, propertyType);
    return propertyDTO;
  }

  public static IPropertyDTO newPropertyDTO(IAttribute attribute) throws Exception
  {
    long propertyIID = attribute.getPropertyIID();
    String propertyID = attribute.getId();
    String propertyCode = attribute.getCode();
    String type = attribute.getType();
    IPropertyDTO propertyDTO = RDBMSUtils.newPropertyDTO(propertyIID, propertyID, propertyCode, type);
    return propertyDTO;
  }

  public static IPropertyDTO newPropertyDTO(ITag tag) throws Exception
  {
    long propertyIID = tag.getPropertyIID();
    String propertyID = tag.getId();
    String propertyCode = tag.getCode();
    String type = tag.getType();
    IPropertyDTO propertyDTO = RDBMSUtils.newPropertyDTO(propertyIID, propertyID, propertyCode, type);
    return propertyDTO;
  }

  public static IPropertyDTO newPropertyDTO(long propertyIID, String propertyID, String propertyCode, String type) throws Exception
  {
    PropertyType propertyType = IConfigMap.getPropertyType(type);
    IPropertyDTO propertyDTO = RDBMSUtils.newConfigurationDAO().createProperty(propertyCode, propertyType);
    return propertyDTO;
  }

  public static IPropertyDTO newPropertyDTO(long propertyIID, String propertyID, String propertyCode,
      IPropertyDTO.PropertyType propertyType) throws Exception
  {
    IPropertyDTO propertyDTO = RDBMSUtils.newConfigurationDAO().createProperty(propertyCode, propertyType);
    return propertyDTO;
  }

  public static IPropertyDTO getPropertyByIID(long propertyIID) throws Exception
  {
    IPropertyDTO propertyDTO = RDBMSUtils.newConfigurationDAO().getPropertyByIID(propertyIID);
    return propertyDTO;
  }

  public static IValueRecordDTO newValueRecordDTO(long propertyRecordIID, long valueIID, IPropertyDTO property, String value,
      String localeID) throws Exception
  {

    ValueRecordDTO valueRecordDTO = new ValueRecordDTO(0L, valueIID, property, CouplingType.UNDEFINED, RecordStatus.UNDEFINED, value, "", 0,
        "", localeID, null);

    return valueRecordDTO;
  }

  /**
   * create new Tag
   *
   * @param tagValueCode
   * @param propertyID
   * @return
   * @throws Exception
   */
  public static ITagValueDTO createTagValue(String tagValueCode, String propertyID) throws Exception
  {
    ITagValueDTO tagValueDTO = RDBMSUtils.newConfigurationDAO().createTagValue(tagValueCode, propertyID);
    return tagValueDTO;
  }

  public static ITagsRecordDTO newTagsRecordDTO(long propertyRecordIID, IPropertyDTO propertyDTO) throws Exception
  {
    // TODO Why type casting required
    ITagsRecordDTO tagsRecordDTO = new TagsRecordDTO(propertyRecordIID, (PropertyDTO) propertyDTO);
    return tagsRecordDTO;
  }

  public static ITagValueDTO getOrCreateTagValueDTO(String tagValueCode, long propertyIID) throws Exception
  {
    ITagValueDTO tagValueDTO = RDBMSUtils.newConfigurationDAO().createTagValue(tagValueCode, propertyIID);
    return tagValueDTO;
  }

  public static ITagDTO newTagRecordDTO(long tagValueIID, int relevance, String tagValueID, String tagValueCode, long propertyIID)
      throws Exception
  {
    // TODO Why type casting required
    ITagValueDTO tagValueDTO = RDBMSUtils.getOrCreateTagValueDTO(tagValueCode, propertyIID);
    ITagDTO tagRecordDTO = new TagDTO(tagValueDTO.getCode(), relevance);
    return tagRecordDTO;
  }
  
  public static IEntityRelationDTO getEntityRelationDTO(long sideBaseEntityIID, String otherSideEntityId,
       IContextDTO contextDTO)
  {
    IEntityRelationDTO entityRelationDTO = new EntityRelationDTO(sideBaseEntityIID, otherSideEntityId,
        contextDTO.getCode());
    return entityRelationDTO;
  }
  
  public static IEntityRelationDTO getEntityRelationDTO(String sideEntityID, Long sideEntityIID)
  {
    IEntityRelationDTO entityRelationDTO = new EntityRelationDTO(sideEntityID, sideEntityIID);
    return entityRelationDTO;
  }

  public static IPropertyDTO getPropertyByCode(String propertyCode) throws Exception
  {
    return RDBMSUtils.newConfigurationDAO().getPropertyByCode(propertyCode);

  }

  public static IUserDTO getOrCreateUser(String userName) throws Exception
  {
    return RDBMSUtils.newConfigurationDAO().createUser(userName);
  }

  public static LinkedHashMap<String, OrderDirection> getSortMap(List<ISortDTO> sortOptions)
  {
    LinkedHashMap<String, OrderDirection> order = new LinkedHashMap<>();
    for (ISortDTO sortOption : sortOptions) {
      order.put(sortOption.getSortField(), OrderDirection.valueOf(sortOption.getSortOrder().name().toUpperCase()));
    }
    return order;
  }
  
  /**
   * Use this method if you want to work on default instance of classifier like
   * nature/non-nature/taxonomy
   * 
   * @return lacaleCatalogDAO , it will take default localeID
   * @throws Exception
   */
  public static ILocaleCatalogDAO getDefaultLocaleCatalogDAO() throws Exception
  {
    IUserSessionDAO userSessionDAO = RDBMSUtils.newUserSessionDAO();
    IUserSessionDTO userSessionDTO = userSessionDAO.openSession(CommonConstants.ADMIN_USER_ID, "defaultsession");
    
    ILocaleCatalogDTO localeCatalogDTO = userSessionDAO.newLocaleCatalogDTO(IStandardConfig.GLOBAL_LOCALE, CommonConstants.DEFAULT_KLASS_INSTANCE_CATALOG,
        IStandardConfig.STANDARD_ORGANIZATION_RCODE);
    
    ILocaleCatalogDAO localeCatalogDAO = userSessionDAO.openLocaleCatalog(userSessionDTO, localeCatalogDTO);
    return localeCatalogDAO;
  }

  /**
   * Use this method if you want to work on default instance of classifier like
   * nature/non-nature/taxonomy
   *
   * @return lacaleCatalogDAO , it will take default localeID
   * @throws Exception
   */
  public static ILocaleCatalogDAO getDefaultLocaleCatalogDAOWithLocale(String localeId) throws Exception
  {
    IUserSessionDAO userSessionDAO = RDBMSUtils.newUserSessionDAO();
    IUserSessionDTO userSessionDTO = userSessionDAO.openSession(CommonConstants.ADMIN_USER_ID, "defaultsession");

    ILocaleCatalogDTO localeCatalogDTO = userSessionDAO.newLocaleCatalogDTO(localeId,"diff-"+ IStandardConfig.StandardCatalog.pim.toString(),
        IStandardConfig.STANDARD_ORGANIZATION_RCODE);

    ILocaleCatalogDAO localeCatalogDAO = userSessionDAO.openLocaleCatalog(userSessionDTO, localeCatalogDTO);
    return localeCatalogDAO;
  }


  /**
   * Use this method if you want to work on default instance of classifier like
   * nature/non-nature/taxonomy
   *
   * @return lacaleCatalogDAO , it will take default localeID
   * @throws Exception
   */
  public static ILocaleCatalogDAO getDefaultLocaleCatalogDAO(String transactionID) throws Exception
  {
    IUserSessionDAO userSessionDAO = RDBMSUtils.newUserSessionDAO();
    IUserSessionDTO userSessionDTO = userSessionDAO.openSession(CommonConstants.ADMIN_USER_ID, "defaultsession");
    userSessionDTO.setTransactionId(transactionID);
    ILocaleCatalogDTO localeCatalogDTO = userSessionDAO.newLocaleCatalogDTO(IStandardConfig.GLOBAL_LOCALE,"diff-"+ IStandardConfig.StandardCatalog.pim.toString(),
        IStandardConfig.STANDARD_ORGANIZATION_RCODE);

    ILocaleCatalogDAO localeCatalogDAO = userSessionDAO.openLocaleCatalog(userSessionDTO, localeCatalogDTO);
    return localeCatalogDAO;
  }
  public static List<String> getOrganizationIdHavingRuntimeInstance(List<String> partnerIdsToBeDeleted) throws Exception{
    return RDBMSUtils.newConfigurationDAO().getOrganizationIdHavingRuntimeInstance(partnerIdsToBeDeleted);
  }
}
