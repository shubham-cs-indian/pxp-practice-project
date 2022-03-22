package com.cs.core.bgprocess.services.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.TransferPlanDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.ITransferPlanDTO;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.bgprocess.services.dataintegration.BaseEntityTransferDAS;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Transfer of base entities
 *
 * @author vallee
 */
public class BaseEntityTransfer extends AbstractBaseEntityProcessing implements IBGProcessJob {
  
  private static final String VIOLATION_CHECK_QUERY = "SELECT propertyiid, qualityflag FROM pxp.baseentityqualityrulelink where baseentityiid = ?";
  private ITransferPlanDTO transferPlan = new TransferPlanDTO();
  RDBMSComponentUtils        rdbmsComponentUtils;

  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    transferPlan.fromJSON(jobData.getEntryData().toString());
    rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
  }
  
  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs) throws JsonProcessingException, Exception
  {
    List<Long> successIIDs = new ArrayList<>();
    List<Long> failedIIDs = new ArrayList<>();
    for (Long baseEntityIID : batchIIDs) {
      try {
        userSession.setTransactionId(UUID.randomUUID().toString());
        // TODO later => filter out base entities with quality violations
        ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, baseEntityIID);
        userSession.setTransactionId(UUID.randomUUID().toString());
        IBaseEntityDTO target = transferBaseEntity(catalogDao, baseEntityIID, successIIDs, failedIIDs);
        if (target != null) {
          LocaleCatalogDAO targetCatalogDAO = new LocaleCatalogDAO(userSession, new LocaleCatalogDTO(transferPlan.getLocaleID(),
              transferPlan.getTargetCatalogCode(), transferPlan.getTargetOrganizationCode()));
          targetCatalogDAO.postUsecaseUpdate(target.getBaseEntityIID(), EventType.ELASTIC_UPDATE);
        }
      }
      catch (Exception e) {
        jobData.getLog().error("transfer of Base Entity %s failed", baseEntityIID);
        jobData.getLog().error(e.getMessage());
        failedIIDs.add(baseEntityIID);
      }
    }
    //ToDo : remove this logic once success and failed IIDs scenario completely implemented
    List<Long> tempSuccessIIDs = jobData.getRuntimeData().getArrayField(SUCCESS_IIDS, Long.class);
    tempSuccessIIDs.addAll(successIIDs);
    jobData.getRuntimeData().setLongArrayField(SUCCESS_IIDS, tempSuccessIIDs);
    
    List<Long> tempFailedIIDs = jobData.getRuntimeData().getArrayField(FAILED_IIDS, Long.class);
    tempFailedIIDs.addAll(failedIIDs);
    jobData.getRuntimeData().setLongArrayField(FAILED_IIDS, tempFailedIIDs);
  }
  
  /**
   * Transfer a base entity given by IID
   *
   * @param baseEntityIID
   * @param successIIDs 
   * @param failedIIDs 
   * @return
   * @throws Exception 
   * @throws JsonProcessingException 
   */
  private IBaseEntityDTO transferBaseEntity(ILocaleCatalogDAO catalogDao, long baseEntityIID, List<Long> successIIDs, List<Long> failedIIDs)
      throws JsonProcessingException, Exception
  {
    // TODO: This code is copied/pasted and could find better optimization in AbstractBaseEntityProcessing
    // since it is common with the Clone process
    Set<IPropertyDTO> properties = new HashSet<>();
    List<Long> orangeViolatedIIDs = new ArrayList<Long>(); 
    Boolean [] redViolationExists = new Boolean[1];
    redViolationExists[0] = false;
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statement = currentConn.prepareStatement(VIOLATION_CHECK_QUERY);
      statement.setLong(1, baseEntityIID);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        switch(resultSet.getInt("qualityflag")) {
          case 0 : redViolationExists[0] = true; break;
          case 1 : orangeViolatedIIDs.add(resultSet.getLong("propertyiid"));
        }
      }
    });
    if(redViolationExists[0])
    {
      jobData.getLog().error("Skipping transfer of Base Entity %s as it contains red violations", baseEntityIID);
      failedIIDs.add(baseEntityIID);
      return null;
    }
    if (processPlan.getAllProperties()) {
      properties.addAll(catalogDao.getAllEntityProperties(baseEntityIID));
    }
    else {
      Set<Long> propertyIIDs = processPlan.getPropertyIIDs();
      for (Long propertyIID : propertyIIDs)
        properties.add(new PropertyDTO(ConfigurationDAO.instance().getPropertyByIID(propertyIID)));
    }
    // ensure to have the name attribute in the list in any cases:
    properties.add( ConfigurationDAO.instance().getPropertyByIID(IStandardConfig.StandardProperty.nameattribute.getIID()));
   
    /*IBaseEntityDTO newEntity = catalogDao.transferEntityByIID(baseEntityIID, transferPlan,
        properties.toArray(new IPropertyDTO[0]));*/
    
    properties = properties.stream().filter(x -> !orangeViolatedIIDs.contains(x.getIID())).collect(Collectors.toSet());
    
    IBaseEntityDTO newEntity = new BaseEntityTransferDAS(userSession).transferEntityByIID(
        catalogDao, baseEntityIID, transferPlan, properties.toArray(new IPropertyDTO[0]), false);
    
    //RDBMSAppDriverManager.getDriver().newRevisionDAO(userSession).createNewRevision(newEntity, ""); 
    
    jobData.getLog().info("Base Entity %s transferred from base entity IID %d", newEntity.toCSExpressID(), baseEntityIID);
    jobData.getSummary().setSuccessCount(jobData.getSummary().getSuccessCount() + 1);
    successIIDs.add(baseEntityIID);
    return newEntity;
  }
  
  @Override
  protected String getCallbackData()
  {
    Map<String, Object> callBackDataMap = new HashMap<>();
    try {
      List<Long> successInstanceIIDs = jobData.getRuntimeData().getArrayField(SUCCESS_IIDS, Long.class);
      List<Long> failedInstanceIIDs = jobData.getRuntimeData().getArrayField(FAILED_IIDS, Long.class);
      callBackDataMap.put(SUCCESS_IIDS, successInstanceIIDs);
      callBackDataMap.put(FAILED_IIDS, failedInstanceIIDs);
    }
    catch (CSFormatException e) {
      jobData.getLog().exception(e);
    }
    return new JSONObject(callBackDataMap).toString();
  }
}
