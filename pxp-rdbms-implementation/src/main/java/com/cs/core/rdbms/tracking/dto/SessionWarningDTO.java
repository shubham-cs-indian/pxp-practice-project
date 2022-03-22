package com.cs.core.rdbms.tracking.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.dto.SessionDTO;
import com.cs.core.rdbms.tracking.idto.ISessionWarningDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;

/**
 * Session Warning Data Transfer Object
 *
 * @author PankajGajjar
 */
public class SessionWarningDTO extends RDBMSRootDTO implements ISessionWarningDTO {
  
  public static final String  WARNING_MESSAGE = PXONTag.msg.toReadOnlyTag();
  public static final String  WARNING_NO      = PXONTag.no.toReadOnlyTag();
  private static final String SESSION         = PXONTag.session.toJSONContentTag();
  private SessionDTO          session         = new SessionDTO();
  private String              warningMessage  = "";
  private WarningType         warningType     = WarningType.UNDEFINED;
  private int                 warningNo       = -1;
  
  /**
   * Enabled default constructor
   */
  public SessionWarningDTO()
  {
  }
  
  /**
   * Value constructor
   *
   * @param session
   * @param warningMessage
   * @param warningType
   * @param warningNo
   */
  public SessionWarningDTO(SessionDTO session, String warningMessage,
      ISessionWarningDTO.WarningType warningType, int warningNo)
  {
    this.session = session;
    this.warningMessage = warningMessage;
    this.warningType = warningType;
    this.warningNo = warningNo;
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject cse = new CSEObject(CSEObjectType.Tracking);
    cse.setIID(getIID());
    cse.setSpecification(ICSEElement.Keyword.$type, warningType);
    return cse; // returns meta information
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject gcse = (CSEObject) cse;
    setIID(gcse.getIID());
    warningType = gcse.getSpecification(WarningType.class, ICSEElement.Keyword.$type);
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    session.fromJSON(parser.getJSONParser(SESSION));
    warningMessage = parser.getString(WARNING_MESSAGE);
    warningNo = parser.getInt(WARNING_NO);
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
        JSONBuilder.newJSONField(SESSION, session.toJSON()),
        JSONBuilder.newJSONField(WARNING_MESSAGE, warningMessage, true),
        JSONBuilder.newJSONField(WARNING_NO, warningNo));
  }
  
  @Override
  public int getWarningNo()
  {
    return warningNo;
  }
  
  @Override
  public void setWarningNo(int warningNo)
  {
    this.warningNo = warningNo;
  }
  
  @Override
  public WarningType getWarningType()
  {
    return warningType;
  }
  
  @Override
  public void setWarningType(WarningType warningType)
  {
    this.warningType = warningType;
  }
  
  @Override
  public String getWarningMessage()
  {
    return warningMessage;
  }
  
  @Override
  public void setWarningMessage(String warningMessage)
  {
    this.warningMessage = warningMessage;
  }
  
  @Override
  public String getSessionID()
  {
    return session.getSessionID();
  }
  
  @Override
  public void setSessionID(String sessionID)
  {
    session.setSessionID(sessionID);
  }
  
  @Override
  public long getLoginTime()
  {
    return session.getLoginTime();
  }
  
  @Override
  public void setLoginTime(long time)
  {
    session.setLoginTime(time);
  }
}
