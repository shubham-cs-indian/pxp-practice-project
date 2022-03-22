package com.cs.core.rdbms.config.dto;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.Constants;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Property Data Transfer Object
 *
 * @author vallee
 */
public class PropertyDTO extends RootConfigDTO implements IPropertyDTO, Serializable {

  private static final String       PROPERTY_PREFIX = "property";
  protected RelationSide            relationSide    = RelationSide.UNDEFINED; // transient
                                                                              // information
  private long                      propertyIID     = 0L;
  private IPropertyDTO.PropertyType propertyType    = PropertyType.UNDEFINED;

  /**
   * Enabled default constructor
   */
  public PropertyDTO()
  {
  }


  /**
   * Enabled copy constructor
   */
  public PropertyDTO(IPropertyDTO propertyDTO)
  {
    super(propertyDTO.getCode());
    this.propertyIID = propertyDTO.getPropertyIID();
    this.propertyType = propertyDTO.getPropertyType();
    this.relationSide = propertyDTO.getRelationSide();
  }
  /**
   * Value constructor
   *
   * @param propertyIID
   * @param code
   * @param propertyType
   */
  public PropertyDTO(long propertyIID, String code, IPropertyDTO.PropertyType propertyType)
  {
    super(code);
    this.propertyIID = propertyIID;
    this.propertyType = propertyType;
  }
  
  /**
   * Constructor from a result set
   *
   * @param parser
   * @throws java.sql.SQLException
   */
  public PropertyDTO(IResultSetParser parser) throws SQLException
  {
    super(parser, PROPERTY_PREFIX);
    propertyIID = parser.getLong("propertyIID");
    propertyType = PropertyType.valueOf(parser.getInt("propertyType"));
  }
  
  public static String getCacheCode(String propertyCode)
  {
    return String.format("%c:%s", CSEObject.CSEObjectType.Property.letter(), propertyCode);
  }
  
  public static String getCacheCode(long propertyIID)
  {
    return String.format("%c:%d", CSEObject.CSEObjectType.Property.letter(), propertyIID);
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEProperty cse = new CSEProperty();
    cse = (CSEProperty) initCSExpression(cse, propertyType.toString());
    cse.setIID(propertyIID);
    if (relationSide != RelationSide.UNDEFINED)
      cse.setSpecification(Keyword.$side, String.format("%d", relationSide.ordinal()));
    return cse;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEProperty pcse = (CSEProperty) cse;
    fromCSExpression(pcse);
    propertyIID = pcse.getIID();
    propertyType = pcse.getSpecification(PropertyType.class, ICSEElement.Keyword.$type);
    String side = pcse.getSpecification(ICSEElement.Keyword.$side);
    if (side.isEmpty())
      relationSide = RelationSide.UNDEFINED;
    else
      relationSide = RelationSide.valueOf(Integer.parseInt(side));
  }
  
  @Override
  public IPropertyDTO.PropertyType getPropertyType()
  {
    return propertyType;
  }
  
  /**
   * @param type
   *          overwritten property type
   */
  public void setPropertyType(IPropertyDTO.PropertyType type)
  {
    propertyType = type;
  }
  
  @Override
  public boolean isTrackingProperty()
  {
    return IStandardConfig.isTrackingProperty(getPropertyIID(), getCode());
  }
  
  @Override
  public RelationSide getRelationSide()
  {
    return relationSide;
  }
  
  @Override
  public void setRelationSide(RelationSide side)
  {
    relationSide = side;
  }
  
  @Override
  public int compareTo(Object t)
  {
    int compare = super.compareTo(t);
    return compare != 0 ? compare
        : this.relationSide.ordinal() - ((PropertyDTO) t).relationSide.ordinal();
  }
  
  @Override
  public boolean equals(Object obj)
  {
    boolean equal = super.equals(obj);
    return equal ? this.relationSide == ((PropertyDTO) obj).relationSide : false;
  }
  
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(5, 17).append(super.hashCode())
        .append(relationSide)
        .build();
  }
  
  /**
   * @param iid
   *          overwritten IID
   */
  public void setIID(long iid)
  {
    propertyIID = iid;
  }
  
  @Override
  public long getPropertyIID()
  {
    return propertyIID;
  }

  @Override
  public Boolean isNumeric()
  {
    return Arrays.asList(IPropertyDTO.PropertyType.CALCULATED, IPropertyDTO.PropertyType.MEASUREMENT, IPropertyDTO.PropertyType.PRICE,
        IPropertyDTO.PropertyType.NUMBER, IPropertyDTO.PropertyType.DATE).contains(propertyType);
  }

  @Override
  public Boolean isAttribute()
  {
    return propertyType.getSuperType().equals(SuperType.ATTRIBUTE);
  }

  @Override
  public Boolean isTag()
  {
    return propertyType.getSuperType().equals(SuperType.TAGS);
  }

  @Override
  public Boolean isRelational()
  {
    return  Constants.relationType.contains(this.propertyType);
  }

  @Override
  public Boolean isStandardTag()
  {
    return Constants.standardTags.contains(getCode());
  }
}
