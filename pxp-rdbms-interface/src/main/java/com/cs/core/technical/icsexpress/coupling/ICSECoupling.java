package com.cs.core.technical.icsexpress.coupling;

import com.cs.core.technical.icsexpress.definition.ICSEProperty;

/**
 * Interface for a coupling expression
 *
 * @author vallee
 */
public interface ICSECoupling extends Comparable {

  /**
   * @return the source of coupling
   */
  ICSECouplingSource getSource();

  ;
  
  /**
   * false for dynamic, false for tightly as for transfer coupling status
   */
  public void setTransfer();

  ;
  
  /**
   * @return true if this is a dynamic coupling
   */
  public boolean isDynamic();

  /**
   * @param couplingStatus overwritten the coupling status: true for dynamic, false for tightly
   */
  public void setDynamic(boolean couplingStatus);

  /**
   * @return true if this is a tight coupling
   */
  public boolean isTight();

  /**
   * @return true if this is a transfer coupling
   */
  public default boolean isTransfer() {
    return !isTight() && !isDynamic();
  }

  /**
   * @return the source property involved in the coupling
   */
  public ICSEProperty getProperty();

  /**
   * @return a priority order by Transfer > Relationship > Inheritance > Default Value.
   */
  public int getPriority();

  /**
   * @return the coupling type inferred by this coupling definition
   */
  public CouplingType getCouplingType();

  @Override
  public String toString();

  public enum CouplingBehavior {

    UNDEFINED, NONE, INITIAL, DYNAMIC, TIGHTLY;

    private static final CouplingBehavior[] values = values();

    public static CouplingBehavior valueOf(int ordinal) {
      return values[ordinal];
    }
  }

  public enum CouplingType {

    UNDEFINED(CouplingBehavior.UNDEFINED, 8), NONE(CouplingBehavior.NONE, 7),
    DYN_RELATIONSHIP(CouplingBehavior.DYNAMIC, 2), TIGHT_RELATIONSHIP(CouplingBehavior.TIGHTLY,5),
    DYN_INHERITANCE(CouplingBehavior.DYNAMIC, 1), TIGHT_INHERITANCE(CouplingBehavior.TIGHTLY,4),
    DYN_CLASSIFICATION(CouplingBehavior.DYNAMIC, 3), TIGHT_CLASSIFICATION(CouplingBehavior.TIGHTLY,6),
    TIGHT_CONTEXTUAL(CouplingBehavior.TIGHTLY,7), DYN_CONTEXTUAL(CouplingBehavior.DYNAMIC,8), 
    LANG_INHERITANCE(CouplingBehavior.TIGHTLY,9);

    private static final CouplingType[] values = values();
    private final CouplingBehavior behavior;
    private final int precedence;

    CouplingType(CouplingBehavior behavior, int precedence) {
      this.behavior = behavior;
      this.precedence = precedence;
    }

    public static CouplingType valueOf(int ordinal) {
      return values[ordinal];
    }

    public static CouplingType getTypeByPrecedence(int precedence) {
      for(CouplingType value : values){
        int currentPrecedence = value.getPrecedence();
        if(precedence == currentPrecedence){
          return value;
        }
      }
      return UNDEFINED;
    }

    public CouplingBehavior getBehavior() {
      return behavior;
    }

    public int getPrecedence() {
      return precedence;
    }
  }


}
