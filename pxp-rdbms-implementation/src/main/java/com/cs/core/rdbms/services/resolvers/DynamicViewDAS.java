package com.cs.core.rdbms.services.resolvers;

import com.cs.core.rdbms.driver.*;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.services.CSCache;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;
import java.util.List;

/**
 * This special interface offers methods for property values management
 *
 * @author vallee
 */
public class DynamicViewDAS extends RDBMSDataAccessService {

  private static final String Q_VALUE_VIEW = "select distinct b.entityiid, b.contextualobjectiid, b.propertyiid,"
          + " coalesce( VIEW_VALUE_IID_LIST) as valueIID," + " coalesce( VIEW_VALUE_LIST) as value,"
          + " coalesce( VIEW_LOCALE_ID_LIST) as localeID, " + "coalesce( VIEW_ASHTML_LIST) as ashtml "
          + " from pxp.allvaluerecord b ";
  // To be appended with the left join list...
  private static final String Q_LEFT_JOIN = " left join " + "pxp.allvaluerecord VX "
          + "on VX.entityIID = b.entityIID and VX.propertyIID = b.propertyIID and VX.localeID = 'LOCALE_ID' ";
  private static final String Q_FINAL_LEFT_JOIN = " left join " + "pxp.allvaluerecord vf "
          + "on vf.entityIID = b.entityIID and vf.propertyIID = b.propertyIID and vf.localeID is null ";
  private final List<String> localeInheritanceSchema;
  private final String PROPERTY_VIEW_NAME_PREFIX = "pxp.val";

  /**
   * Create a new service interface
   *
   * @param driver
   * @param connection current connection
   * @param localeInheritanceSchema the locale inheritance hierarchy
   */
  public DynamicViewDAS(RDBMSConnection connection,
          List<String> localeInheritanceSchema) {
    super(connection);
    this.localeInheritanceSchema = localeInheritanceSchema;
  }

  /**
   * @param localeInheritanceSchema
   * @return a final select statement for dynamic value view
   */
  private String buildValueViewSelectStatement(List<String> localeInheritanceSchema) {
    StringBuffer viewValueIIDList = new StringBuffer();
    StringBuffer viewValueList = new StringBuffer();
    StringBuffer viewLocaleIDList = new StringBuffer();
    StringBuffer viewAsHtmlList = new StringBuffer();
    StringBuffer leftJoinList = new StringBuffer();
    // Build the select statement of the view
    for (int i = 0; i < localeInheritanceSchema.size(); i++) {
      String placeHolder = "v" + i;
      String leftJoinStatement = Q_LEFT_JOIN
              .replaceFirst("LOCALE_ID", localeInheritanceSchema.get(i))
              .replaceAll("VX", placeHolder);
      leftJoinList.append(leftJoinStatement);
      viewValueIIDList.append(placeHolder)
              .append(".valueIID,");
      viewValueList.append(placeHolder)
              .append(".value,");
      viewLocaleIDList.append(placeHolder)
              .append(".localeID,");
      viewAsHtmlList.append(placeHolder)
              .append(".ashtml,");
    }
    // Manage to remove the last comma from lists and to add the value for
    // localeID is null
    viewValueIIDList.append("vf.valueIID");
    viewValueList.append("vf.value");
    viewLocaleIDList.append("null");
    viewAsHtmlList.append("vf.ashtml");// ORACLE requires to have at least 2
    // parameters in coalesce call
    // integrate in the final select statement
    StringBuffer selectStatement = new StringBuffer(
            Q_VALUE_VIEW.replaceFirst("VIEW_VALUE_IID_LIST", viewValueIIDList.toString())
                    .replaceFirst("VIEW_VALUE_LIST", viewValueList.toString())
                    .replaceFirst("VIEW_LOCALE_ID_LIST", viewLocaleIDList.toString())
                    .replaceFirst("VIEW_ASHTML_LIST", viewAsHtmlList.toString())).append(leftJoinList)
            .append(Q_FINAL_LEFT_JOIN);
    // .append(";");
    return selectStatement.toString();
  }

  /**
   * @return the name of the view managed by this service
   */
  public String getViewName() {
    return PROPERTY_VIEW_NAME_PREFIX + String.join("", localeInheritanceSchema);
  }

  /**
   * @return true when the view managed by this service already exists
   * @throws RDBMSException
   * @throws SQLException
   */
  private boolean checkDynamicValueView() throws RDBMSException, SQLException {
    String viewName = getViewName();
    IResultSetParser result = driver
            .getFunction(currentConnection, RDBMSAbstractFunction.ResultType.INT, "pxp.fn_checkDynamicView")
            .setInput(RDBMSAbstractFunction.ParameterType.STRING, viewName)
            .execute();
    boolean viewExists = (result.getInt() == 1);
    RDBMSLogger.instance().debug("Check existing dynamic view %s return %b", viewName, viewExists);
    return viewExists;
  }

  /**
   * Create dynamically the view to fetch value record according to the current locale inheritance schema
   *
   * @return the final created view name
   * @throws RDBMSException
   * @throws java.sql.SQLException
   */
  public String createDynamicValueView() throws RDBMSException, SQLException {
    String viewName = getViewName();
    if (CSCache.instance().isKept("view:" + viewName)) {
      return viewName;
    }
    synchronized (DynamicViewDAS.class) { // IMPORTANT Mutual exclusive access here
      if (!checkDynamicValueView()) {
        String selectStatement = buildValueViewSelectStatement(localeInheritanceSchema);
        // Create Dynamic View ( viewName, selectStatement) in a separate connection
        RDBMSConnectionManager.instance().runTransaction((embeddedConnection) -> {
          embeddedConnection.getProcedure( "pxp.sp_createDynamicView")
                  .setInput(RDBMSAbstractFunction.ParameterType.STRING, viewName)
                  .setInput(RDBMSAbstractFunction.ParameterType.STRING, selectStatement)
                  .execute();
        });
        RDBMSLogger.instance().debug("Dynamic view created %s", viewName);
      }
      CSCache.instance().keep("view:" + viewName, "");
    }
    return viewName;
  }
}
