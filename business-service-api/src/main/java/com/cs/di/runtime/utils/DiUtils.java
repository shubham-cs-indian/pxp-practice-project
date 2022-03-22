package com.cs.di.runtime.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;

import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.EntityRelationDTO;
import com.cs.core.rdbms.entity.dto.RelationsSetDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.DiConstants;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * @author mangesh.metkari
 *
 */
public class DiUtils extends RDBMSUtils {
  private static final Properties diProperties = DiFileUtils.loadProperties("diconfig.properties");
  
  /**
   * Preparing userSessionDTO
   * 
   * @return
   */
  public static IUserSessionDTO createUserSessionDto()
  {
    IUserSessionDTO openSession = null;
    try {
      openSession = RDBMSAppDriverManager.getDriver().newUserSessionDAO().openSession(diProperties.getProperty("di.userName"), IUserSessionDTO.createUniqueSessionID("di"));
    }
    catch (RDBMSException e) {
      RDBMSLogger.instance().exception(e);
    }
    return openSession;
  }
  
  /**
   * Preparing transactionData
   * 
   * @return
   */
  public static ITransactionData createTransactionData()
  {
    ITransactionData transactionData = new TransactionData();
    transactionData.setDataLanguage(diProperties.getProperty("di.dataLanguage"));
    transactionData.setUiLanguage(diProperties.getProperty("di.uiLanguage"));
    transactionData.setEndpointId(diProperties.getProperty("di.endpointId"));
    transactionData.setPhysicalCatalogId(diProperties.getProperty("di.physicalCatalogId"));
    transactionData.setUserId(diProperties.getProperty("di.userId"));
    transactionData.setUserName(diProperties.getProperty("di.userName"));
    transactionData.setOrganizationId(diProperties.getProperty("di.organizationId"));
    return transactionData;
  }
  
  public static ILocaleCatalogDAO createLocaleCatalogDAO(IUserSessionDTO iUserSessionDTO, ITransactionData transactionData)
  {
    ILocaleCatalogDAO localeCatalogDao = null;
    try {
      IUserSessionDAO userSession = RDBMSAppDriverManager.getDriver().newUserSessionDAO();
      
      ILocaleCatalogDTO localeCatalogDTO = userSession.newLocaleCatalogDTO(transactionData.getDataLanguage(),
          transactionData.getPhysicalCatalogId(), transactionData.getOrganizationId());
      localeCatalogDao = userSession.openLocaleCatalog(iUserSessionDTO, localeCatalogDTO);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    return localeCatalogDao;
  }
 
  public static IRelationsSetDTO getRelationsSetDTO(long sideBaseEntityIID, IPropertyDTO relationship, IPropertyDTO.RelationSide side,
      long extensionClassifierIID)
  {
    //TODO:- use factory method and builder class to construct DTO object
    IRelationsSetDTO relationsSetDTO = new RelationsSetDTO(sideBaseEntityIID, (PropertyDTO) relationship, side);
    //relationsSetDTO.setExtensionClassifierIID(extensionClassifierIID);
    return relationsSetDTO;
  }
  
  public static IEntityRelationDTO getEntityRelationDTO(long sideBaseEntityIID, long extendedEntityIID, String cxtCode)
  {
    //TODO:- use factory method and builder class to construct DTO object
    IEntityRelationDTO entityRelationDTO = new EntityRelationDTO(sideBaseEntityIID, cxtCode);
    return entityRelationDTO;
  }
	
	public static TransactionData getTransactionData(WorkflowTaskModel model)
  {
    TransactionData transactionData = (TransactionData) model.getWorkflowModel()
        .getTransactionData();
    return transactionData;
  }
	
  /**
   * this method first check localCatalogDAO object is already created or not if not then create new object
   * otherwise return already created object
   * 
   * @param localCatalogDAOMap
   * @param languageCode
   * @param workflowTaskModel
   * @return
   */
  public static ILocaleCatalogDAO getLocaleCatalogDAO(Map<String, ILocaleCatalogDAO> localCatalogDAOMap, String languageCode,
      WorkflowTaskModel workflowTaskModel)
  {
    if (localCatalogDAOMap.containsKey(languageCode)) {
      return localCatalogDAOMap.get(languageCode);
    }
    else {
      try {
        ITransactionData transactionData = (ITransactionData) workflowTaskModel.getWorkflowModel().getTransactionData();
        IUserSessionDAO userSession = RDBMSAppDriverManager.getDriver().newUserSessionDAO();
        ILocaleCatalogDTO localeCatalogDTO = userSession.newLocaleCatalogDTO(languageCode, transactionData.getPhysicalCatalogId(),
            transactionData.getOrganizationId());
        localCatalogDAOMap.put(languageCode,
            userSession.openLocaleCatalog((IUserSessionDTO) workflowTaskModel.getWorkflowModel().getUserSessionDto(), localeCatalogDTO));
        return localCatalogDAOMap.get(languageCode);
      }
      catch (Exception e) {
        RDBMSLogger.instance().exception(e);
      }
    }
    return null;
  }
  
  /**convert to long for date in "yyyy-MM-dd HH:mm:ss.SSS" i.e 24 hr format
   * 
   * 
   * @param dateAsString
   * @return
   */
  public static long getLongValueOfDateWithTimestamp(String dateAsString)
  {
    Long time = null;
    if (dateAsString != null) {
      DateFormat formatter = new SimpleDateFormat(DiConstants.DATE_TIME_FORMAT_24HR);
      Date date;
      try {
        date = formatter.parse(dateAsString);
        time = date.getTime();
      }
      catch (ParseException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    }
    return time;
  }
  public static long getLongValueOfDateString(String dateAsString)
  {
    Long time = 0l;
    if (!StringUtils.isBlank(dateAsString)) {
      DateFormat formatter = new SimpleDateFormat(DiConstants.DATE_FORMAT);
      Date date;
      try {
        date = formatter.parse(dateAsString);
        time = date.getTime();
      }
      catch (ParseException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    }
    return time;
  }
  
  /**Function to append timestamp to "End Date" or "To date" of context
   * 
   * @param toDate
   * @return
   */
  public static String appendTimeToToDate(String toDate)
  {
    toDate = toDate + " 23:59:59.999";
    return toDate;
  }
  
  public static String getBaseTypeByKlassType(String klassType)
  {
    switch (klassType) {
      case Constants.PROJECT_KLASS_TYPE:
        return Constants.ARTICLE_INSTANCE_BASE_TYPE;
      case Constants.ASSET_KLASS_TYPE:
        return Constants.ASSET_INSTANCE_BASE_TYPE;
      
      case Constants.MASTER_IMPORT_ARTICLE_KLASS_TYPE:
        return Constants.MASTER_ARTICLE_INSTANCE_BASE_TYPE;
      case Constants.IMPORT_SYSTEM_FILE_ARTICLE_KLASS_TYPE:
      case Constants.IMPORT_SYSTEM_INSTANCE_KLASS_TYPE:
      case Constants.IMPORT_SYSTEM_FILE_KLASS_TYPE:
      case Constants.IMPORT_ARTICLE_KLASS_TYPE:
        return Constants.IMPORT_SYSTEM_INSTANCE_BASE_TYPE;
      
      case Constants.SUPPLIER_KLASS_TYPE:
        return Constants.SUPPLIER_INSTANCE_BASE_TYPE;
      case Constants.TEXT_ASSET_KLASS_TYPE:
        return Constants.TEXTASSET_INSTANCE_BASE_TYPE;
      //TODO: PXPFDEV-21454: Deprecate Virtual Catalog
      /*case Constants.VIRTUAL_CATALOG_KLASS_TYPE:
        return Constants.VIRTUAL_CATALOG_INSTANCE_BASE_TYPE;*/
    }
    return null;
  }

  /**
   * Making a zip of files at given path.
   * 
   * @param fileList
   * @param Path
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void makeZipFiles(List<File> fileList, String Path)
      throws FileNotFoundException, IOException
  {
    byte[] buffer = new byte[1024];
    FileOutputStream fileOutputStream = new FileOutputStream(Path);
    ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
    for (File file : fileList) {
      ZipEntry zipEntry = new ZipEntry(file.getName());
      zipOutputStream.putNextEntry(zipEntry);
      FileInputStream fileInputStream = new FileInputStream(file);
      int len;
      while ((len = fileInputStream.read(buffer)) > 0) {
        zipOutputStream.write(buffer, 0, len);
      }
      fileInputStream.close();
      //deleting file after adding in zip
       file.delete();
    }
    zipOutputStream.closeEntry();
    zipOutputStream.close();
  }

  
  public static final String DI_BASE_URL = getDIBaseURL();
  private static String getDIBaseURL()
  {
    try {
      String serverUrl = CSProperties.instance()
      .getString("external.server.url");
      if(!serverUrl.endsWith("/")) {
        serverUrl = serverUrl+ "/";
      }
      String war = CSProperties.instance()
          .getString("tomcat.war.name");
          if(!war.endsWith("/")) {
            war = war+ "/";
          }
      return serverUrl + war;
    }
    catch (CSInitializationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
      
    }
    return "";
  }
}