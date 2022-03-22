package com.cs.core.rdbms.services.resolvers;

import com.cs.core.data.Text;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Common behaviors between resolvers used for entity search
 * @author frva
 */
public abstract class AbstractSearchEntityResolver extends RDBMSDataAccessService {
  protected final TemporaryTableDAS tempTableDas;
  protected final Set<String> catalogCodes;
  protected final Set<Integer> baseTypes;
  protected final Set<String> organizationCodes;
  protected final Set<Long> baseEntityIIDs;
  protected final Set<String> endpointCodes;
  private static final String Q_BASE_ENTITY = 
    "select a.baseEntityIID from pxp.baseEntity a where a.ismerged != true and a.catalogCode %CATALOGCODES% and a.organizationCode %ORGANIZATIONCODES%";
  protected String baseQuery = Q_BASE_ENTITY;
  private String baseQueryResult = null;
  public static final String tagsRecordTable = "pxp.allTagsRecord";
  
  public AbstractSearchEntityResolver( 
      RDBMSConnection connection, Set<String> catalogCodes, Set<BaseType> baseTypes, String tempTableName, Set<String> organizationCodes,
      Set<Long> baseEntityIIDs, Set<String> endpointCodes)
  {
    super(connection);
    tempTableDas = new TemporaryTableDAS( connection, tempTableName);
    this.catalogCodes = catalogCodes;
    this.organizationCodes = organizationCodes;
    this.baseTypes = baseTypes.stream()
            .map( Enum::ordinal )
            .collect( Collectors.toSet());
    this.baseEntityIIDs = baseEntityIIDs;
    this.endpointCodes = endpointCodes;
    prepareBaseQuery();   
  }
    
  /**
   * Prepare the base query that consists in filtering entities by catalog and base type
   */
  private void prepareBaseQuery() {
    if(!baseTypes.isEmpty()) {
      baseQuery = baseQuery + " and a.baseType %BASETYPES% "; 
    }
    if ( baseTypes.size() == 1 )
      baseQuery = baseQuery.replace("%BASETYPES%", String.format( " = %d", baseTypes.iterator().next()));
    else {
      baseQuery = baseQuery.replace( "%BASETYPES%", String.format( " in (%s)", 
          Text.join( ",", baseTypes.stream()
                      .map( type -> { return String.format("%d",type); } )
                      .collect( Collectors.toSet()))));
    }
    if ( catalogCodes.size() == 1 )
      baseQuery = baseQuery.replace("%CATALOGCODES%", String.format( " = '%s'", catalogCodes.iterator().next()));
    else {
      baseQuery = baseQuery.replace("%CATALOGCODES%", String.format( " in (%s)",
            Text.join( ",", catalogCodes.stream()
                      .map( code -> { return "'" + code + "'"; } )
                      .collect( Collectors.toSet()))));
    }

    if ( organizationCodes.size() == 0 )
      baseQuery = baseQuery.replace("%ORGANIZATIONCODES%", String.format( " = '%s'", "-1"));
    if ( organizationCodes.size() == 1 )
      baseQuery = baseQuery.replace("%ORGANIZATIONCODES%", String.format( " = '%s'", organizationCodes.iterator().next()));
    else {
      baseQuery = baseQuery.replace("%ORGANIZATIONCODES%", String.format( " in (%s)",
          Text.join( ",", organizationCodes.stream()
              .map( code -> { return "'" + code + "'"; } )
              .collect( Collectors.toSet()))));
    }
    
    if (!baseEntityIIDs.isEmpty()) {
      baseQuery = baseQuery + " and a.baseentityiid %BASEENTITYIIDS% ";
      baseQuery = baseQuery.replace("%BASEENTITYIIDS%", String.format(" in (%s)", Text.join(",", baseEntityIIDs.stream().map(iid -> {
        return String.format("%d", iid);
      }).collect(Collectors.toSet()))));
    }
    
    if ( endpointCodes.size() == 1 )
      baseQuery = baseQuery + String.format(" and a.endpointCode = '%s' ", endpointCodes.iterator().next());
    else if(endpointCodes.size() > 1){
      baseQuery = baseQuery + String.format(" and a.endpointCode in (%s) ", Text.join(",", endpointCodes,"'%s'"));
    }

  }  
  
  /**
   * @return the temporary table containing the result of base filtering
   * @throws SQLException
   * @throws RDBMSException 
   */
  protected String getBaseQueryResult() throws SQLException, RDBMSException {
    if ( baseQueryResult == null )
      baseQueryResult = tempTableDas.createTable(baseQuery);
    return baseQueryResult;
  }
  
  /**
   * @param operator
   * @param leftResultTable
   * @param rightResultTable
   * @return
   * @throws RDBMSException
   * @throws SQLException 
   */
  protected String resolveLogicalOperator(ICSECalculation.Operator operator,
    String leftResultTable, String rightResultTable) throws RDBMSException, SQLException {
    String resultTable;
    switch (operator) {
      case Land:
        resultTable = tempTableDas.intersectResults(leftResultTable, rightResultTable);
        break;
      case Lor:
        resultTable = tempTableDas.unionResults(leftResultTable, rightResultTable);
        break;
      case Lxor:
        resultTable = tempTableDas.exclusiveResults(leftResultTable, rightResultTable);
      default:
        throw new RDBMSException(100, "Program", "unexpected operator for search: "
                + operator.getSymbol());
    }
    return resultTable;
  }
}
