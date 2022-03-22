package com.cs.core.rdbms.config.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * Implementation of IContextDTO
 *
 * @author vallee
 */
public class ContextDTO extends RootConfigDTO implements IContextDTO, Serializable {
  
  private static final String     CONTEXT_PREFIX = "context";
  private static final String ALLOW_DUPLICATE    = IPXON.PXONTag.dupl.toTag();
  private IContextDTO.ContextType contextType    = ContextType.UNDEFINED;
  
  /**
   * Enabled default constructor
   */
  public ContextDTO()
  {
  }
  
  /**
   * Value constructor
   *
   * @param contextCode
   * @param contextType
   */
  public ContextDTO(String contextCode, ContextType contextType)
  {
    super(contextCode);
    this.contextType = contextType;
  }
  
  /**
   * Constructor from a result set
   *
   * @param parser
   * @throws java.sql.SQLException
   */
  public ContextDTO(IResultSetParser parser) throws SQLException
  {
    super(parser, CONTEXT_PREFIX);
    this.contextType = ContextType.valueOf(parser.getInt("contextType"));
  }
  
  public static String getCacheCode(String contextCode)
  {
    return String.format("%c:%s", CSEObject.CSEObjectType.Context.letter(), contextCode);
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject cse = new CSEObject(CSEObjectType.Context);
    return initCSExpression(cse, contextType.toString());
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject ccse = (CSEObject) cse;
    fromCSExpression(ccse);
    this.contextType = ccse.getSpecification(ContextType.class, ICSEElement.Keyword.$type);
  }
  
  @Override
  public ContextType getContextType()
  {
    return contextType;
  }
  
  /**
   * @param type
   *          overwritten context type
   */
  public void setContextType(ContextType type)
  {
    this.contextType = type;
  }
}
