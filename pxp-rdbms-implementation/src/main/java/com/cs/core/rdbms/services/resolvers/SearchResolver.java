 package com.cs.core.rdbms.services.resolvers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.cs.core.csexpress.calculation.CSECalculationNode;
import com.cs.core.csexpress.scope.CSEScope;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode.FilterNodeType;
import com.cs.core.technical.icsexpress.scope.ICSEScope;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * A resolver of search expression (indirectly also resolves scope)
 * @author vallee
 */
public class SearchResolver extends RDBMSDataAccessService {

  /**
   * Create a new service interface for search resolution
   *
   * @param connection current connection
   */
  public SearchResolver( RDBMSConnection connection) {
    super(connection);
  }
  
  private Set<Long> getBaseEntityIIDs(String tempTable) throws RDBMSException, SQLException {
    PreparedStatement statement = currentConnection.prepareStatement( 
            String.format( "select baseEntityIID from %s", tempTable));
    IResultSetParser rs = driver.getResultSetParser( statement.executeQuery());
    Set<Long> entityIIDs = new HashSet<>();
    while (rs.next())
      entityIIDs.add( rs.getLong("baseEntityIID"));
    return entityIIDs; 
  }
  /**
   * Build a temporary table that contains the entity IIDs corresponding to the scope definition
   * /!\ closing the connection or ending the transaction contributes to empty the temporary table
   * @param entityScope entry specification
   * @return the temporary table name
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws java.sql.SQLException
   * @throws CSFormatException 
   */
  public String resolveScope(CSEScope entityScope) throws RDBMSException, SQLException, CSFormatException
  {
    EntityFilterResolver filterResolver = new EntityFilterResolver(currentConnection, entityScope.getCatalogCodes(),
        entityScope.getBaseTypes(), entityScope.getOrganizationCodes(), entityScope.getEntityIIDs(), entityScope.getEndpointCodes());
    return filterResolver.resolve(entityScope.getEntityFilter(), entityScope.getLocaleIDs());
  }
  
  /**
   * @param entityScope
   * @return the set of base entity IIDs that correspond to the scope
   * @throws RDBMSException
   * @throws SQLException 
   * @throws CSFormatException 
   */
  public Set<Long> getScopeResult( CSEScope entityScope) throws RDBMSException, SQLException, CSFormatException {
    String scopeTable = resolveScope( entityScope);
    return getBaseEntityIIDs(scopeTable);
  }
  
  /**
   * Build a temporary table that contains the entity IIDs corresponding to the conditions
   * /!\ closing the connection or ending the transaction contributes to empty the temporary table
   * @param catalogCodes the set of catalog to which the search apply
   * @param baseTypes the set of entity base types to which the search apply
   * @param localeIDs the locale inheritance schema in which to resolve value records
   * @param condition entry specification for conditions
   * @param isContextSearch , true if this search is for context like embedded variant
   * @return the temporary table name
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws java.sql.SQLException
   */
  public String resolveCalculation(Set<String> catalogCodes, Set<BaseType> baseTypes, List<String> localeIDs, CSECalculationNode condition,
      Boolean isContextSearch, Set<String> organizationCodes, Set<Long> baseEntityIIDs, Set<String> endpointCodes) throws RDBMSException, SQLException, CSFormatException
  {
    CalculatedConditionResolver calcResolver = new CalculatedConditionResolver(currentConnection, catalogCodes, baseTypes, localeIDs,
        isContextSearch, organizationCodes, baseEntityIIDs, endpointCodes);
    return calcResolver.resolve(condition);
  }

  /**
   * @param catalogCodes
   * @param baseTypes
   * @param localeIDs
   * @param condition
   * @return the set of base entity IIDs that correspond to the conditions
   * @throws RDBMSException
   * @throws SQLException 
   */
  public Set<Long> getCalculationResult( Set<String> catalogCodes, 
          Set<BaseType> baseTypes, List<String> localeIDs, CSECalculationNode condition, Set<String> organizationCodes, Set<Long> baseEntityIIDs, Set<String> endpointCodes)
          throws RDBMSException, SQLException, CSFormatException {
    String resultTable = resolveCalculation( catalogCodes, baseTypes, localeIDs, condition, false, organizationCodes, baseEntityIIDs, endpointCodes);
    return getBaseEntityIIDs( resultTable);
  }
  
  /**
   *
   * @return
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public Set<Long> getTotalResult(ICSEScope scope,ICSECalculationNode evaluation)
      throws RDBMSException, SQLException, CSFormatException
  {
    String unionResults = evaluateTotalResult(scope, evaluation);
    return getBaseEntityIIDs(unionResults);
  }

  /**
   * 
   * @param scope
   * @param evaluation`
   * @return
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public String evaluateTotalResult(ICSEScope scope, ICSECalculationNode evaluation) throws RDBMSException, SQLException, CSFormatException
  {
    Set<BaseType> baseTypes = scope.getBaseTypes()
        .stream()
        .map(x -> x.getBaseType())
        .collect(Collectors.toSet());
    
    String scopeTable = resolveScope((CSEScope) scope);
    if(evaluation == null) {
    	return scopeTable;
    }
    boolean isContextSearch = false;
    ICSEEntityFilterNode entityFilter = scope.getEntityFilter();
    if (entityFilter != null && entityFilter.getType().equals(FilterNodeType.byContext)) {
      isContextSearch = true;
    }
    String resolveCalculation = resolveCalculation(scope.getCatalogCodes(), baseTypes, scope.getLocaleIDs(),
        (CSECalculationNode) evaluation, isContextSearch, scope.getOrganizationCodes(), scope.getEntityIIDs(), scope.getEndpointCodes());
    return new TemporaryTableDAS(currentConnection, "uScope").intersectResults(scopeTable, resolveCalculation);
  }

}
