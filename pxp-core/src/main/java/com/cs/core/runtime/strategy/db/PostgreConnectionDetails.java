/*package com.cs.core.runtime.strategy.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostgreConnectionDetails implements InitializingBean {

  @Value("${postgre.rdbms.url}")
  private String url;

  @Value("${postgre.user.name}")
  private String userName;

  @Value("${postgre.password}")
  private String password;

  @Value("${postgre.max.active.connections}")
  private int maxActiveConnections;

  @Value("${postgre.initial.connections.size}")
  private int initialConnectionSize;

  @Value("${postgre.max.open.prepared.statements}")
  private int maxOpenPreparedStatements;

  @Value("${postgre.driver.class.name}")
  private String driverClassName;

  static String  URL;
  static String  USER_NAME;
  static String  PASSWORD;
  static int     MAX_ACTIVE_CONNECTION;
  static int     INITIAL_CONNECTION_SIZE;
  static int     MAX_OPEN_PREPAREED_STATEMENTS;
  static String  DRIVER_CLASS_NAME;
  public static Properties SCHEMA_AND_QUERIES;

  @Override
  public void afterPropertiesSet() throws Exception
  {
    USER_NAME = userName;
    PASSWORD = password;
    URL = url;
    MAX_ACTIVE_CONNECTION = maxActiveConnections;
    INITIAL_CONNECTION_SIZE = initialConnectionSize;
    MAX_OPEN_PREPAREED_STATEMENTS = maxOpenPreparedStatements;
    DRIVER_CLASS_NAME = driverClassName;

    loadAllQueriesForPostgresSql();

    System.out.println("\n\n------------- Postgre RDBMS Details -------------");
    System.out.println(" URL                           ---> " + PostgreConnectionDetails.URL);
    System.out.println(" USER_NAME                     ---> " + PostgreConnectionDetails.USER_NAME);
    System.out.println(" PASSWORD                      ---> " + PostgreConnectionDetails.PASSWORD);
    System.out.println(" MAX_ACTIVE_CONNECTION         ---> " + PostgreConnectionDetails.MAX_ACTIVE_CONNECTION);
    System.out.println(" INITIAL_CONNECTION_SIZE       ---> " + PostgreConnectionDetails.INITIAL_CONNECTION_SIZE);
    System.out.println(" MAX_OPEN_PREPAREED_STATEMENTS ---> " + PostgreConnectionDetails.MAX_OPEN_PREPAREED_STATEMENTS);
    System.out.println(" DRIVER_CLASS_NAME             ---> " + PostgreConnectionDetails.DRIVER_CLASS_NAME);
    //System.out.println(" SCHEMA_AND_URL_LOADED         ---> " + PostgreConnectionDetails.SCHEMA_AND_QUERIES);
    System.out.println("\n\n");
  }

  private void loadAllQueriesForPostgresSql()
  {
    SCHEMA_AND_QUERIES = new Properties();
    InputStream is = null;
    try {
      Properties propetiesToLoad = new XProperties();
      is = PostgreConnectionDetails.class
          .getResourceAsStream("/databaseProperties/schema.properties");
      propetiesToLoad.load(is);
      setAllQueriesForPostgresSql(propetiesToLoad);
    }
    catch (FileNotFoundException e) {
      RDBMSLogger.instance().exception(e);
    }
    catch (IOException e) {
      RDBMSLogger.instance().exception(e);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }

  private void setAllQueriesForPostgresSql(Properties propetiesToLoad)
  {
    Set<Object> keySetOfProperties = propetiesToLoad.keySet();
    for (Object keyOfProperty : keySetOfProperties) {
      String convertedKeyIntoString = (String) keyOfProperty;
      SCHEMA_AND_QUERIES.setProperty(convertedKeyIntoString, propetiesToLoad.getProperty(convertedKeyIntoString));
    }
  }

}
*/
