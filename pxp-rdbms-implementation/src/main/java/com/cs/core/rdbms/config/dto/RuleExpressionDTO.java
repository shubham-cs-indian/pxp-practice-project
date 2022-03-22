package com.cs.core.rdbms.config.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.config.idto.IConfigRuleDTO;
import com.cs.core.rdbms.config.idto.IRuleExpressionDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Rule Expression Data Transfer Object
 *
 * @author Niraj.Dighe
 */
public class RuleExpressionDTO extends RootConfigDTO implements IRuleExpressionDTO {
  
  private static final String RULE_PREFIX = "rule";
  
  private long                ruleExpressionIId;
  private Set<Long>           forPropertyIIDs;
  private Set<Long>           whenPropertyIIDs;
  private List<String>         localeIDs;
  private Set<String>         catalogCodes;
  private Set<String>         organizationCodes;
  private Set<String>         classifierCodes;
  private String              ruleExpression;
  private String              ruleCode;
  
  /**
   * Value Constructor
   */
  public RuleExpressionDTO(long ruleExpressionIId, Set<Long> forPropertyIIDs,
      Set<Long> whenPropertyIIDs, Collection<String> localeIDs, Collection<String> catalogCodes,
      Set<String> classifierCodes, String ruleExpression, String ruleCode, Collection<String> organizationCodes)
  {
    super();
    this.ruleExpressionIId = ruleExpressionIId;
    this.forPropertyIIDs = forPropertyIIDs;
    this.whenPropertyIIDs = whenPropertyIIDs;
    this.localeIDs = (List<String>) localeIDs;
    this.catalogCodes = (Set<String>) catalogCodes;
    this.classifierCodes = classifierCodes;
    this.ruleExpression = ruleExpression;
    this.ruleCode = ruleCode;
    this.organizationCodes = (Set<String>) organizationCodes;
  }
  
  /**
   * Copy constructor
   *
   * @param source
   */
  public RuleExpressionDTO(IConfigRuleDTO source)
  {
    super(source);
  }
  
  /**
   * Constructor from a result set
   *
   * @param parser
   * @throws java.sql.SQLException
   * @throws CSFormatException
   */
  public RuleExpressionDTO(IResultSetParser parser) throws SQLException, CSFormatException
  {
    super(parser, RULE_PREFIX);
    this.forPropertyIIDs = new HashSet<>(Arrays.asList(parser.getLongArray("forPropertyIIDs")));
    this.whenPropertyIIDs = new HashSet<>(Arrays.asList(parser.getLongArray("whenPropertyIIDs")));
    this.localeIDs = Arrays.asList(parser.getStringArray("localeIDs"));
    this.ruleExpression = parser.getString("expression");
    this.catalogCodes = new HashSet<String>(Arrays.asList(parser.getStringArray("catalogCodes")));
    this.ruleExpressionIId = parser.getLong("ruleExpressionIId");
    this.organizationCodes = new HashSet<String>(Arrays.asList(parser.getStringArray("organizationCodes")));
  }
  
  @Override
  public String getRuleExpression()
  {
    return ruleExpression;
  }
  
  @Override
  public Collection<String> getLocaleIDs()
  {
    return localeIDs;
  }
  
  @Override
  public Collection<String> getCatalogCodes()
  {
    return catalogCodes;
  }
  
  @Override
  public Collection<String> getClassifierCodes()
  {
    return classifierCodes;
  }
  
  @Override
  public Collection<Long> getWhenPropertyIIDs()
  {
    return whenPropertyIIDs;
  }
  
  @Override
  public Collection<Long> getForPropertyIIDs()
  {
    return forPropertyIIDs;
  }
  
  @Override
  public long getRuleExpressionIId()
  {
    return ruleExpressionIId;
  }
  
  @Override
  public String getRuleCode()
  {
    return ruleCode;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject gcse = (CSEObject) cse;
    fromCSExpression(gcse);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.Rule);
    return initCSExpression(cse, "");
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    // at least one of the IID, ID or Code is equal
    final RuleExpressionDTO other = (RuleExpressionDTO) obj;
    return new EqualsBuilder().append(this.ruleExpressionIId, other.ruleExpressionIId).isEquals();
  }
  
  @Override
  public int compareTo(Object obj)
  {
    RuleExpressionDTO that = ((RuleExpressionDTO) obj);
    return new CompareToBuilder().append(this.ruleExpressionIId, that.getRuleExpressionIId()).toComparison();
  }

  @Override
  public Collection<String> getOrganizationCodes()
  {
    return organizationCodes;
  }

}
