package com.cs.api.estordbmsmigration.interactor.migration;

import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.api.estordbmsmigration.model.migration.IConfigDataForHelperTablesModel;
import com.cs.api.estordbmsmigration.model.migration.ISyncCongifToRDBMSRequestModel;
import com.cs.api.estordbmsmigration.model.migration.SyncCongifToRDBMSRequestModel;
import com.cs.api.estordbmsmigration.services.MigrationProperties;
import com.cs.api.estordbmsmigration.strategy.migration.IGetConfigDataForHelperTablesStrategy;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/***
 * This service is responsible for inserting config data into helper tables for runtime migration.
 * @author vannya.kalani
 *
 */
@Service
public class HelperTablesForRuntimeMigration extends AbstractRuntimeService<IVoidModel, IVoidModel> implements IHelperTablesForRuntimeMigration {
  
  private static final String           REFERENCE = "Reference";
  
  @Autowired
  IGetConfigDataForHelperTablesStrategy getConfigDataForHelperTablesStrategy;
  
  @Override
  public IVoidModel executeInternal(IVoidModel dataModel) throws Exception
  {
    int batchSize = MigrationProperties.instance().getInt("batchsize");
    populateHelperTables(batchSize);
    return null;
  }
  
  /***
   * Populate helper tables using populateKlasses method with all type of vertices 
   * @param batchSize
   * @throws Exception
   */
  private void populateHelperTables(int batchSize) throws Exception
  {
    populateKlasses(0, batchSize, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    populateKlasses(0, batchSize, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    populateKlasses(0, batchSize, VertexLabelConstants.VARIANT_CONTEXT);
    populateKlasses(0, batchSize, VertexLabelConstants.ENTITY_TYPE_USER);
    populateKlasses(0, batchSize, VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    populateKlasses(0, batchSize, VertexLabelConstants.NATURE_RELATIONSHIP);
    //populateKlasses(0, batchSize, REFERENCE);
    populateKlasses(0, batchSize, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    populateKlasses(0, batchSize, VertexLabelConstants.ENTITY_TAG);
    populateKlasses(0, batchSize,  VertexLabelConstants.ENDPOINT);
    populateKlasses(0, batchSize,  VertexLabelConstants.ENTITY_TYPE_TASK);
    populateKlasses(0, batchSize,  VertexLabelConstants.ENTITY_TYPE_ROLE);
    populateKlasses(0, batchSize,  VertexLabelConstants.ORGANIZATION);
  }

  /***
   * Get config data and call respective methods to insert data into helper tables.
   * @param from
   * @param batchSize
   * @param vertexType
   * @throws Exception
   */
  private void populateKlasses(int from, int batchSize, String vertexType) throws Exception
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(batchSize);
    requestModel.setVertexType(vertexType);
    IListModel<IConfigDataForHelperTablesModel> configResponse = getConfigDataForHelperTablesStrategy.execute(requestModel);
    
    switch (vertexType) {
      case VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS:
      case VertexLabelConstants.ROOT_KLASS_TAXONOMY:
      case VertexLabelConstants.VARIANT_CONTEXT:
      case VertexLabelConstants.ENTITY_TAG:
      case VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE:
      case VertexLabelConstants.ORGANIZATION:
        insertIntoHelperConfig(configResponse);
        break;
      
      case VertexLabelConstants.ENTITY_TYPE_USER:
        insertIntoHelperUserConfig(configResponse);
        break;
        
      case VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP:
      case VertexLabelConstants.NATURE_RELATIONSHIP:
      case REFERENCE:
        insertIntoHelperRelationshipConfig(configResponse);
        break;
      
      case VertexLabelConstants.ENDPOINT:
      case VertexLabelConstants.ENTITY_TYPE_TASK:
      case VertexLabelConstants.ENTITY_TYPE_ROLE:  
        insertIntoHelperEndpointConfig(configResponse);
        break;
      default:
        break;
    }
    
    if(configResponse.getList().size() == batchSize)
      populateKlasses(from + batchSize, batchSize, vertexType);
  }


  /***
   * Insert data into helper_config table.
   * @param configResponse
   */
  public static final String INSERT_HELPER_CONFIG_QUERY = "INSERT INTO staging.helper_config VALUES (?, ?, ?, ?)";  
  private void insertIntoHelperConfig(IListModel<IConfigDataForHelperTablesModel> configResponse)
  {
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement prepareStatement = currentConn.prepareStatement(INSERT_HELPER_CONFIG_QUERY);
        
        for(IConfigDataForHelperTablesModel row : configResponse.getList()) {
          prepareStatement.setString(1, row.getCid());
          prepareStatement.setString(2, row.getCode());
          prepareStatement.setBoolean(3, row.getIsNature());
          prepareStatement.setLong(4, row.getIId());
          prepareStatement.addBatch();
        }
        prepareStatement.executeBatch();
        currentConn.commit();
      });
    }
    catch (RDBMSException e) {
      //TODO Logging
      System.out.println("Failure :: insertIntoHelperConfig :: " + e.getMessage());
    }
  }
  
  /***
   * Insert data into helper_userconfig table.
   * @param configResponse
   */
  public static final String INSERT_HELPER_USER_CONFIG_QUERY = "INSERT INTO staging.helper_userconfig VALUES (?, ?, ?)";  
  private void insertIntoHelperUserConfig(IListModel<IConfigDataForHelperTablesModel> configResponse)
  {
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement prepareStatement = currentConn.prepareStatement(INSERT_HELPER_USER_CONFIG_QUERY);
        
        for(IConfigDataForHelperTablesModel row : configResponse.getList()) {
          prepareStatement.setString(1, row.getUserName());
          prepareStatement.setString(2, row.getCode());
          prepareStatement.setLong(3, row.getIId());
          prepareStatement.addBatch();
        }
        prepareStatement.executeBatch();
        currentConn.commit();
      });
    }
    catch (RDBMSException e) {
      //TODO Logging
      System.out.println("Failure :: insertIntoHelperUserConfig :: " + e.getMessage());
    }
  }
  
  /***
   * Insert data into helper_relationshipconfig table.
   * @param configResponse
   */
  public static final String INSERT_HELPER_RELATIONSHIP_CONFIG_QUERY = "INSERT INTO staging.helper_relationshipconfig VALUES (?, ?, ?, ?, ?)";  
  private void insertIntoHelperRelationshipConfig(IListModel<IConfigDataForHelperTablesModel> configResponse)
  {
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement prepareStatement = currentConn.prepareStatement(INSERT_HELPER_RELATIONSHIP_CONFIG_QUERY);
        
        for(IConfigDataForHelperTablesModel row : configResponse.getList()) {
          prepareStatement.setString(1, row.getCid());
          prepareStatement.setString(2, row.getSide1ElementId());
          prepareStatement.setString(3, row.getSide2ElementId());
          prepareStatement.setString(4, row.getCode());
          prepareStatement.setLong(5, row.getIId());
          prepareStatement.addBatch();
        }
        prepareStatement.executeBatch();
        currentConn.commit();
      });
    }
    catch (RDBMSException e) {
      //TODO Logging
      System.out.println("Failure :: insertIntoHelperRelationshipConfig :: " + e.getMessage());
    }
  }

  /***
   * Insert data into helper_endpointconfig table.
   * @param configResponse
   */
  public static final String INSERT_HELPER_ENDPOINT_CONFIG_QUERY = "INSERT INTO staging.helper_endpointconfig VALUES (?, ?)";  
  private void insertIntoHelperEndpointConfig(IListModel<IConfigDataForHelperTablesModel> configResponse)
  {
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement prepareStatement = currentConn.prepareStatement(INSERT_HELPER_ENDPOINT_CONFIG_QUERY);
        
        for(IConfigDataForHelperTablesModel row : configResponse.getList()) {
          prepareStatement.setString(1, row.getCid());
          prepareStatement.setString(2, row.getCode());
          prepareStatement.addBatch();
        }
        prepareStatement.executeBatch();
        currentConn.commit();
      });
    }
    catch (RDBMSException e) {
      //TODO Logging
      System.out.println("Failure :: insertIntoHelperEndpointConfig :: " + e.getMessage());
    }
  }
}
