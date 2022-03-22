package com.cs.core.bgprocess.idto;

import java.util.Collection;
import java.util.Set;

/**
 * Specification for processing a set of base entities
 *
 * @author vallee
 */
public interface IBaseEntityPlanDTO extends IInitializeBGProcessDTO {

  /**
   * @return the list of base entity identifiers to be processed
   */
  Set<Long> getBaseEntityIIDs();

  /**
   * @param baseEntityIIDs overwritten list of base entity identifiers to be processed
   */
  public void setBaseEntityIIDs(Collection<Long> baseEntityIIDs);

  /**
   * @return the list of properties to be processed per base entity
   */
  Set<Long> getPropertyIIDs();

  /**
   * @param propertyIIDs
   */
  public void setPropertyIIDs(Collection<Long> propertyIIDs);

  /**
   * @return the all property flag
   */
  public boolean getAllProperties();

  /**
   * @param status overwritten all property flag
   */
  public void setAllProperties(boolean status);
}
