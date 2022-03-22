package com.cs.core.rdbms.config.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * Tag Value Data Transfer Object
 *
 * @author vallee
 */
public class TagValueDTO extends RootConfigDTO implements ITagValueDTO, Serializable {
  
  private static final String TAGVALUE_PREFIX = "tagValue";
  private long                propertyIID     = 0L;
  
  /**
   * Enabled default constructor
   */
  public TagValueDTO()
  {
  }
  
  /**
   * Value constructor
   *
   * @param tagValueCode
   * @param property
   */
  public TagValueDTO(String tagValueCode, PropertyDTO property)
  {
    super(tagValueCode);
    this.propertyIID = property.getIID();
  }
  
  /**
   * Value constructor
   *
   * @param tagValueCode
   * @param propertyIID
   */
  public TagValueDTO(String tagValueCode, long propertyIID)
  {
    super(tagValueCode);
    this.propertyIID = propertyIID;
  }
  
  /**
   * Constructor from query result
   *
   * @param parser
   * @throws SQLException
   */
  public TagValueDTO(IResultSetParser parser) throws SQLException
  {
    super(parser, TAGVALUE_PREFIX);
    this.propertyIID = parser.getLong("propertyIID");
  }
  
  /**
   * Copy constructor
   *
   * @param source
   */
  public TagValueDTO(TagValueDTO source)
  {
    super(source);
    this.propertyIID = source.propertyIID;
  }
  
  /**
   * Minimal constructor
   *
   * @param tagValueCode
   */
  public TagValueDTO(String tagValueCode)
  {
    super(tagValueCode);
    this.propertyIID = 0L;
  }
  
  public static String getCacheCode(String tagValueCode)
  {
    return String.format("%c:%s", CSEObject.CSEObjectType.TagValue.letter(), tagValueCode);
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject cse = new CSEObject(CSEObjectType.TagValue);
    if (propertyIID > 0L)
      cse.setSpecification(Keyword.$prop, String.format("%d", propertyIID));
    return initCSExpression(cse, "");
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject gcse = (CSEObject) cse;
    fromCSExpression(gcse);
    String parentIID = gcse.getSpecification(ICSEElement.Keyword.$prop);
    if (!parentIID.isEmpty() && Character.isDigit(parentIID.charAt(0)))
      propertyIID = Long.parseLong(parentIID);
    else
      propertyIID = 0L;
  }
  
  @Override
  public long getPropertyIID()
  {
    return propertyIID;
  }
  
  /**
   * @param propertyIID
   *          overwritten property IID
   */
  public void setProperty(long propertyIID)
  {
    this.propertyIID = propertyIID;
  }
}
