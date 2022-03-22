package com.cs.core.transactionend.services.handlers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.transactionend.handlers.dto.DependencyChangeDTO;
import com.cs.core.transactionend.handlers.dto.DependencyDTO;
import com.cs.core.transactionend.handlers.dto.IDeltaInfoDTO;
import com.cs.core.transactionend.handlers.dto.IDependencyChangeDTO;
import com.cs.core.transactionend.handlers.dto.IDependencyChangeDTO.Change;
import com.cs.core.transactionend.handlers.dto.IDependencyDTO;

public class DependencyTracer {

  private static final String INITIAL_QUERY = "select entityIID, propertyIID from pxp.coupledRecord " + " where masterEntityIID = %s and masterPropertyIID in (%s) and localeIId in (%s)";

  private static final String RECURSIVE_QUERY = " select cr.entityIID, cr.propertyIID from dependency join pxp.coupledRecord cr " + "ON dependency.entityIID = cr.masterEntityIID and dependency.propertyIID = cr.masterPropertyIID where localeIID in (%s) ";

  private static final String GET_DEPENDENTS_QUERY_FORMAT = " WITH  RECURSIVE dependency (entityIID, propertyIID) AS ( %s  UNION ALL %s ) SELECT * FROM dependency;";

  public static IDependencyDTO traceDependents(Map<Long, IDeltaInfoDTO> deltaInfo, String localeID)
      throws RDBMSException, SQLException
  {
    DependencyDTO dependencyDTO = new DependencyDTO();

    for (Long deltaInfoKey : deltaInfo.keySet()) {
      IDeltaInfoDTO deltaInfoDTO = deltaInfo.get(deltaInfoKey);
      Map<Long, IDependencyChangeDTO> dependencyWithChanges = new HashMap<>();

      Set<Long> properties = getProperties(deltaInfoDTO);

      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        if (!properties.isEmpty()) {

          String finalQuery = prepareQuery(localeID, properties, deltaInfoKey);

          PreparedStatement prepareStatement = currentConn.prepareStatement(finalQuery);
          IResultSetParser result = currentConn.getResultSetParser(prepareStatement.executeQuery());
          while (result.next()) {
            fillDependents(deltaInfoDTO, dependencyWithChanges, result);
          }
        }
      });

      fillCoupledProperties(deltaInfoKey, deltaInfoDTO, dependencyWithChanges);
      dependencyDTO.getDependencies().putAll(dependencyWithChanges);
    }
    return dependencyDTO;
  }

  protected static String prepareQuery(String localeID, Set<Long> properties, Long baseEntityIID) throws RDBMSException
  {
    ILanguageConfigDTO languageConfig = ConfigurationDAO.instance().getLanguageConfig(localeID);
    String locales = Text.join(",", "0", languageConfig.getLanguageIID());

    String initial = String.format(INITIAL_QUERY, String.valueOf(baseEntityIID), Text.join(",", properties), locales);
    String recursive = String.format(RECURSIVE_QUERY, locales);
    String finalQuery = String.format(GET_DEPENDENTS_QUERY_FORMAT, initial, recursive);
    return finalQuery;
  }

  protected static void fillDependents(IDeltaInfoDTO deltaInfoDTO, Map<Long, IDependencyChangeDTO> dependencyWithChanges,
      IResultSetParser result) throws SQLException, RDBMSException
  {
    long entityIID = result.getLong("entityIID");
    IDependencyChangeDTO dependent = dependencyWithChanges.get(entityIID);

    if (dependent == null) {
      dependent = new DependencyChangeDTO();
      dependent.setEntityIID(entityIID);
      dependent.setCreated(deltaInfoDTO.isCreated());
      dependencyWithChanges.put(entityIID, dependent);
    }
    long propertyIID = result.getLong("propertyIID");
    IPropertyDTO property = ConfigurationDAO.instance().getPropertyByIID(propertyIID);
    if (deltaInfoDTO.getAddedPropertyIIDs().contains(propertyIID)) {
      dependent.getPropertiesChange().put(property.getCode(), Change.Added);
    }
    if (deltaInfoDTO.getDeletedPropertyIIDs().contains(propertyIID)) {
      dependent.getPropertiesChange().put(property.getCode(), Change.Deleted);
    }
    if (deltaInfoDTO.getModifiedPropertyIIDs().contains(propertyIID)) {
      dependent.getPropertiesChange().put(property.getCode(), Change.Modified);
    }
    if(deltaInfoDTO.getAddedCoupledProperties().contains(property.getCode())) {
      dependent.getPropertiesChange().put(property.getCode(), Change.Added);
    }
    if(deltaInfoDTO.getRemovedCoupledProperties().contains(property.getCode())) {
      dependent.getPropertiesChange().put(property.getCode(), Change.Deleted);
    }
  }

  protected static void fillCoupledProperties(Long deltaInfoKey, IDeltaInfoDTO deltaInfoDTO,
      Map<Long, IDependencyChangeDTO> dependencyWithChanges)
  {
    List<String> removedCoupledProperties = deltaInfoDTO.getRemovedCoupledProperties();
    List<String> addedCoupledProperties = deltaInfoDTO.getAddedCoupledProperties();

    for (String removedCoupledProperty : removedCoupledProperties) {
      IDependencyChangeDTO dependencyChangeDTO = dependencyWithChanges.get(deltaInfoKey);
      if (dependencyChangeDTO == null) {
        dependencyChangeDTO = new DependencyChangeDTO();
        dependencyChangeDTO.setEntityIID(deltaInfoKey);
        dependencyWithChanges.put(deltaInfoKey, dependencyChangeDTO);
      }
      dependencyChangeDTO.getPropertiesChange().put(removedCoupledProperty, Change.Deleted);
    }

    for (String addedCoupledProperty : addedCoupledProperties) {
      IDependencyChangeDTO dependencyChangeDTO = dependencyWithChanges.get(deltaInfoKey);
      if (dependencyChangeDTO == null) {
        dependencyChangeDTO = new DependencyChangeDTO();
        dependencyChangeDTO.setEntityIID(deltaInfoKey);
        dependencyWithChanges.put(deltaInfoKey, dependencyChangeDTO);
      }
      dependencyChangeDTO.getPropertiesChange().put(addedCoupledProperty, Change.Added);
    }
  }

  protected static Set<Long> getProperties(IDeltaInfoDTO deltaInfoDTO) throws RDBMSException, SQLException
  {
    List<String> addedCoupledProperties = deltaInfoDTO.getAddedCoupledProperties();
    addedCoupledProperties.addAll(deltaInfoDTO.getRemovedCoupledProperties());
    
    List<Long> propertyIIDsFromPropertyCodes = ConfigurationDAO.instance().getPropertyIIDsFromPropertyCodes(addedCoupledProperties);
    
    Set<Long> properties = new HashSet<>(deltaInfoDTO.getAddedPropertyIIDs());
    properties.addAll(deltaInfoDTO.getDeletedPropertyIIDs());
    properties.addAll(deltaInfoDTO.getModifiedPropertyIIDs());
    properties.addAll(propertyIIDsFromPropertyCodes);
    return properties;
  }
}
