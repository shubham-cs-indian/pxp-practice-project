package com.cs.core.rdbms.driver;

import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;

/**
 * generic method for all DAS interfaces
 *
 * @author vallee
 */
public abstract class RDBMSDataAccessService {

  protected static IConfigurationDAO configDao = null;
  protected final RDBMSAbstractDriver driver;
  protected final RDBMSConnection currentConnection;

  /**
   * create a new service interface
   *
   * @param connection
   */
  public RDBMSDataAccessService( RDBMSConnection connection) {
    this.driver = connection.getDriver();
    this.currentConnection = connection;
    if (configDao == null) {
      configDao = RDBMSAppDriverManager.getDriver()
              .newConfigurationDAO();
    }
  }

  /**
   * @param nb the number of identifiers to get
   * @param type defines the type of identifier
   * @return the required number of unique identifiers
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Long[] getRdbmsIdentifiers(int nb, SequenceType type)
          throws SQLException, RDBMSException, CSFormatException {
    IResultSetParser result = driver
            .getFunction(currentConnection, ResultType.IID_ARRAY, "pxp.fn_acquireIIDs")
            .setInput(ParameterType.INT, nb)
            .setInput(ParameterType.STRING, type.getRdbmsSequenceName())
            .execute();
    return result.getIIDArray();
  }

  public enum SequenceType {

    RuntimeSequence("seqIID"), TrackingSequence("seqObjectTrackIID"),
    GenericIdentification("generic");

    private final String rdbmsSequenceName;

    private SequenceType(String name) {
      rdbmsSequenceName = name;
    }

    public String getRdbmsSequenceName() {
      return rdbmsSequenceName;
    }
  }
}
