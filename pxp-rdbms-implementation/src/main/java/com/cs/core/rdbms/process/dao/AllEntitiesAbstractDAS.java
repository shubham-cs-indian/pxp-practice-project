package com.cs.core.rdbms.process.dao;

import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.services.resolvers.DynamicViewDAS;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author vallee
 */
public abstract class AllEntitiesAbstractDAS extends RDBMSDataAccessService {
  
  protected static final String    PROPERTY_VIEW_NAME = "PROPERTY_VIEW_NAME";
  
  protected final LocaleCatalogDTO localeCatalog;
  protected final List<String>     localeInheritanceSchema;
  protected final int              baseType;
  
  public AllEntitiesAbstractDAS(RDBMSConnection connection,
      LocaleCatalogDTO localeCatalog, List<String> localeInheritanceSchema, int baseTypeCode)
  {
    super(connection);
    this.localeCatalog = localeCatalog;
    this.localeInheritanceSchema = localeInheritanceSchema;
    this.baseType = baseTypeCode;
  }
  
  /**
   * Prepare a query with order by characteristics
   *
   * @param offsetExpression
   * @param orderingDirection
   * @param orderingFields
   * @param query
   * @return the query appended with ordering by characteristics
   */
  protected static StringBuffer appendOrderingFieldaAndOffset(String offsetExpression,
      IRDBMSOrderedCursor.OrderDirection orderingDirection, List<String> orderingFields,
      String query)
  {
    StringBuffer allEntitiesQuery = new StringBuffer(query);
    // Prepare the corresponding query with order by characteristics if
    // specified :
    if (orderingFields.size() > 0) {
      allEntitiesQuery.append(" order by ");
      for (int i = 0; i < orderingFields.size(); i++) {
        allEntitiesQuery.append(orderingFields.get(i))
            .append(" ")
            .append(orderingDirection.toString());
        if (i < orderingFields.size() - 1) {
          allEntitiesQuery.append(", ");
        }
      }
    }
    allEntitiesQuery.append(" ")
        .append(offsetExpression);
    return allEntitiesQuery;
  }
  
  /**
   * Create dynamic view according to localeInheritanceSchema and replace all
   * occurrences of PROPERTY_VIEW_NAME by the created name view
   *
   * @param query
   *          the query with PROPERTY_VIEW_NAME occurrences to be transformed
   * @return the transformed query text
   * @throws RDBMSException
   * @throws SQLException
   */
  protected String prepareTrackingQueryWithViewName(String query)
      throws RDBMSException, SQLException
  {
    // Manage views for value access in last version and locale and return the
    // final view name
    DynamicViewDAS propertyService = new DynamicViewDAS( currentConnection,
        localeInheritanceSchema);
    String viewName = propertyService.createDynamicValueView();
    // Prepare the corresponding query:
    return query.replaceAll(PROPERTY_VIEW_NAME, viewName);
  }
  
  /**
   * @param classifierDTOs
   *          prepare query for filter by classIDs
   * @param queryFilterByClass
   *          filter query
   * @return filter query with classifierIds
   */
  public String queryWithFilterClass(List<IClassifierDTO> classifierDTOs, String queryFilterByClass)
  {
    StringBuffer classifierIDs = new StringBuffer();
    for (IClassifierDTO classifierDTO : classifierDTOs) {
      classifierIDs.append("'" + classifierDTO.getClassifierIID() + "'");
      classifierIDs.append(",");
    }
    return queryFilterByClass.replace("?", classifierIDs.deleteCharAt(classifierIDs.length() - 1));
  }
  
  public String queryWithNotIN(List<Long> sideBaseEntityIIDs, String qFilterEntityNotIn)
  {
    StringBuffer query = new StringBuffer();
    for (Long sideBaseEntityIID : sideBaseEntityIIDs) {
      query.append(sideBaseEntityIID);
      query.append(",");
    }
    return qFilterEntityNotIn.replace("?", query.deleteCharAt(query.length() - 1));
  }
}
