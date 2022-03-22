package com.cs.core.rdbms.entity.idto;

import java.util.List;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.rdbms.idto.IRootDTO;

/**
 * A property record is an abstract common record information for TagRecord, ValueRecord and RelationSdieRecord It makes the relation with
 * the Property (attribute or list of values), the base entity and the concrete record It has status and coupling type to follow how it is
 * transferable from a catalog to another one
 *
 * @author vallee
 */
public interface IPropertyRecordDTO extends IRootDTO, Comparable {

  /**
   * @return the entity which owns that record
   */
  public long getEntityIID();

  ;
  
  /**
   * @return the property of this record
   */
  public IPropertyDTO getProperty();

  /**
   * @return the node ID of this record for dependency registration
   */
  public default String getNodeID() {
    return String.format("%s:%s", getEntityIID(), getProperty().getIID());
  }

  /**
   * @return the record status
   */
  public RecordStatus getRecordStatus();

  /**
   * @return the coupling type
   */
  public CouplingType getCouplingType();

  /**
   * @return the coupling behavior
   */
  public default CouplingBehavior getCouplingBehavior() {
    return getCouplingType().getBehavior();
  }

  /**
   * @return true when this record is coupled
   */
  public default boolean isCoupled() {
    return getRecordStatus() == RecordStatus.COUPLED || getRecordStatus() == RecordStatus.CLONED;
  }

  /**
   * @return true when this record is calculated
   */
  public default boolean isCalculated() {
    return false;
  }

  /**
   * @return true when the property is notified by coupling (i.e. the source value is different)
   */
  public boolean isNotifiedByCoupling();
  
  /**
   * @param isVersionable , weather property is versionable or not
   */
  public void isVersionable(Boolean isVersionable);

  /**
   * @return the signature of the object's property that drives this record if coupled
   */
  public String getMasterNodeID();

  /**
   * @return the master entity identifier in case of coupled record or 0L
   */
  public long getMasterEntityIID();

  /**
   * @return the master property identifier in case of coupled record or 0L
   */
  public long getMasterPropertyIID();

  /**
   * @return true if this record has no value or tags or relations attached to it (condition required for coupled records at creation)
   */
  public boolean isEmpty();

  /**
   * This service has for effect to remove all coupling rules.
   */
  public void resolveCoupling();

  /**
   * /!\\ Only the highest priority expression is retained and persisted on create/update operation
   *
   * @return the parsed coupling expressions sorted by priority if existing or null
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public List<ICSECoupling> getCouplingExpressions() throws CSFormatException;

  /**
   * Introduce a relationship coupling
   *
   * @param relationship the relation used for coupling
   * @param sourceSide the source side coupling - can only be 1 or 2
   * @param property the concerned property
   * @param dynamic true for dynamically coupling, false for tightly coupling
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void addRelationshipCoupling(IPropertyDTO relationship, int sourceSide,
          IPropertyDTO property, boolean dynamic) throws CSFormatException;

  /**
   * Introduce a classification coupling
   *
   * @param property the concerned property
   * @param dynamic true for dynamically coupling, false for tightly coupling
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void addCLassificationCoupling(IClassifierDTO classifier, boolean dynamic) throws CSFormatException;
  
  /**
   * Introduce an inheritance coupling from the parent (default)
   *
   * @param property the concerned property
   * @param dynamic true for dynamically coupling, false for tightly coupling
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void addInheritanceCoupling(IPropertyDTO property, boolean dynamic)
          throws CSFormatException;

  /**
   * Introduce a default value coupling from one another classifier
   *
   * @param classifier the source classifier
   * @param property the concerned property
   * @param dynamic true for dynamically coupling, false for tightly coupling
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void addDefaultValueCoupling(IClassifierDTO classifier, IPropertyDTO property,
          boolean dynamic) throws CSFormatException;

  /**
   * Introduce an inheritance or a default value coupling from an other predefined source
   *
   * @param source can be from top parent, nature class or origin
   * @param property the concerned property
   * @param dynamic true for dynamically coupling, false for tightly coupling
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void addPredefinedCoupling(ICSECouplingSource.Predefined source, IPropertyDTO property,
          boolean dynamic) throws CSFormatException;


  /**
   * @return true when the coupling definition has been changed
   */
  public boolean hasChangedCoupling();

  public enum RecordStatus {

    /*
        DIRECT: the record is not in coupled record table and it has an unique owner
        COUPLED: the record is shared and it is in coupled record table
        CLONED: the record is shared and it is in coupled record table
        DEFAULT_VALUE: the record belongs to a classifier and represents a default value (not in coupled record table)
        NOTIFIED: the record exists in coupled record table as alternate value of a direct record
     */
    UNDEFINED, DIRECT, FORKED, DEFAULT_VALUE, COUPLED, CLONED, NOTIFIED;

    private static final RecordStatus[] values = values();

    public static RecordStatus valueOf(int ordinal) {
      return values[ordinal];
    }
  }

  public void setEntityIID(Long entityIID);

}
