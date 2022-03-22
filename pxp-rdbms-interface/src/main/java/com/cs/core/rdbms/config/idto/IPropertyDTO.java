package com.cs.core.rdbms.config.idto;

/**
 * DTO representation of PXP property = attribute or tag (synchronized from configuration DB)
 *
 * @author vallee
 */
public interface IPropertyDTO extends IRootConfigDTO {

  /**
   * @return the property RDBMS IID
   */
  public default long getIID() {
    return getPropertyIID();
  }

  /**
   * @return the property RDBMS IID
   */
  public long getPropertyIID();

  /**
   * @return the code of the property
   */
  public default String getPropertyCode() {
    return getCode();
  }

  ;
  
  /**
   * @return the super type of this property
   */
  public default SuperType getSuperType() {
    return getPropertyType().getSuperType();
  }

  ;
  
  /**
   * @return the type of this property
   */
  public PropertyType getPropertyType();

  /**
   * @return true when this property is part of the tracking information (create and last modify)
   */
  public boolean isTrackingProperty();

  /**
   * @return the relation side specification when appropriate
   */
  public RelationSide getRelationSide();

  /**
   * @param side overwritten side specification
   */
  public void setRelationSide(RelationSide side);

  /**
   *
   * @return is the property numeric
   */
  Boolean isNumeric();

  /**
   *
   * @return is the property an attribute
   */
  Boolean isAttribute();

  /**
   *
   * @return is the property a tag
   */
  Boolean isTag();

  /**
   *
   * @return is the property a relation
   */
  Boolean isRelational();

  /**
   *
   * @return is the property a standard tag
   */
  Boolean isStandardTag();
  /**
   * Super type of property
   */
  public enum SuperType {

    UNDEFINED, ATTRIBUTE, TAGS, RELATION_SIDE;

    private static final SuperType[] values = values();

    public static SuperType valueOf(int ordinal) {
      return values[ordinal];
    }
  }

  /**
   * Literal type of property
   */
  public enum LiteralType {
    Any, Text, Number, Boolean, Date, List, Range;
  }

  /**
   * Constant for Property type
   */
  public enum PropertyType {

    UNDEFINED(SuperType.UNDEFINED, LiteralType.Any), AUTO(SuperType.ATTRIBUTE, LiteralType.Text),
    CALCULATED(SuperType.ATTRIBUTE, LiteralType.Text),
    CONCATENATED(SuperType.ATTRIBUTE, LiteralType.Text),
    DATE(SuperType.ATTRIBUTE, LiteralType.Date), HTML(SuperType.ATTRIBUTE, LiteralType.Text),
    MEASUREMENT(SuperType.ATTRIBUTE, LiteralType.Number),
    NUMBER(SuperType.ATTRIBUTE, LiteralType.Number), PRICE(SuperType.ATTRIBUTE, LiteralType.Number),
    TEXT(SuperType.ATTRIBUTE, LiteralType.Text), BOOLEAN(SuperType.TAGS, LiteralType.Boolean),
    TAG(SuperType.TAGS, LiteralType.List),
    RELATIONSHIP(SuperType.RELATION_SIDE, LiteralType.List),
    ASSET_ATTRIBUTE(SuperType.ATTRIBUTE, LiteralType.Text),
    NATURE_RELATIONSHIP(SuperType.RELATION_SIDE, LiteralType.List);

    private static final PropertyType[] values = values();
    private final SuperType type;
    private final LiteralType lType;

    PropertyType(SuperType type, LiteralType ltype) {
      this.type = type;
      this.lType = ltype;
    }

    public static PropertyType valueOf(int ordinal) {
      return values[ordinal];
    }

    public SuperType getSuperType() {
      return type;
    }

    public LiteralType getLiteralType() {
      return lType;
    }
  }

  public enum RelationSide {

    UNDEFINED, SIDE_1, SIDE_2;

    private static final RelationSide[] values = values();

    public static RelationSide valueOf(int ordinal) {
      return values[ordinal];
    }

    public RelationSide getOppositeSide() {
      switch (this) {
        case SIDE_1:
          return SIDE_2;
        case SIDE_2:
          return SIDE_1;
      }
      return UNDEFINED;
    }
  }
}
