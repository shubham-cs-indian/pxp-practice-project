package com.cs.core.rdbms.config.idto;

import java.util.Collection;

/**
 * @author vallee
 */
public interface IRuleExpressionDTO extends IRootConfigDTO {

  /**
   * @return the rule expression's IId
   */
  public long getRuleExpressionIId();

  /**
   * @return the rule expression as text
   */
  public String getRuleExpression();

  /**
   * @return Ids of locale on which the current rule should be applicable.
   */
  public Collection<String> getLocaleIDs();

  /**
   * @return Codes of catalog on which the current rule should be applicable.
   */
  public Collection<String> getCatalogCodes();

  /**
   * @return Codes of classifiers on which the current rule should be applicable.
   */
  public Collection<String> getClassifierCodes();

  /**
   * @return IIds of property, which are in cause section of the rule.
   */
  public Collection<Long> getWhenPropertyIIDs();

  /**
   * @return IIds of property, which are in effect section of the rule.
   */
  public Collection<Long> getForPropertyIIDs();

  /**
   * @return IIds of property, which are in effect section of the rule.
   */
  public String getRuleCode();

  /**
   * @return IIds of property, which are in effect section of the rule.
   */
  public Collection<String> getOrganizationCodes();

}
