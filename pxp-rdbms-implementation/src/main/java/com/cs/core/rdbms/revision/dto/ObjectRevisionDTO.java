package com.cs.core.rdbms.revision.dto;

import java.sql.SQLException;
import java.util.Map;

import com.cs.core.data.TextArchive;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.revision.idto.IObjectRevisionDTO;
import com.cs.core.rdbms.tracking.dto.ObjectTrackingDTO;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.IUserDTO;

public class ObjectRevisionDTO extends ObjectTrackingDTO implements IObjectRevisionDTO {
  
  private static final String REVISION_NO       = "revisionNo";
  private static final String REVISION_COMMENT  = "revisionComment";
  private static final String REVISION_TIMELINE = "revisionTimeline";
  private int                 revisionNo;
  private String              revisionComment;
  private JSONContent         revisionTimeline;
  private JSONContent         objectArchive;
  
  /**
   * Default constructor
   */
  public ObjectRevisionDTO()
  {
    this.revisionNo = 0;
    this.revisionComment = "";
    this.revisionTimeline = new JSONContent();
    this.objectArchive = new JSONContent();
  }
  
  /**
   * Value constructor with empty archive
   *
   * @param source
   * @param comment
   */
  public ObjectRevisionDTO(ObjectTrackingDTO source, String comment)
  {
    super(source);
    this.revisionNo = 0;
    this.revisionComment = comment;
    this.revisionTimeline = new JSONContent();
    this.objectArchive = new JSONContent();
  }
  
  /**
   * Constructor from database parsing
   *
   * @param rsParser
   * @throws SQLException
   * @throws CSFormatException
   */
  public ObjectRevisionDTO(IResultSetParser rsParser) throws SQLException, CSFormatException
  {
    super.set(rsParser);
    mapFromObjectRevision(rsParser);
  }
  
  public void mapFromObjectRevision(IResultSetParser rsParser) throws SQLException, CSFormatException
  {
    this.setIID(rsParser.getInt("trackIID"));
    this.revisionNo = rsParser.getInt("revisionNo");
    this.revisionComment = rsParser.getString("revisionComment");
    String jsonTimeLine = rsParser.getStringFromJSON("revisionTimeline").trim();
    this.revisionTimeline = !jsonTimeLine.isEmpty() ? new JSONContent(jsonTimeLine) : new JSONContent();
    String jsonContent = TextArchive.unzip(rsParser.getBinaryBlob("objectarchive")).trim();
    this.objectArchive = !jsonContent.isEmpty() ? new JSONContent(jsonContent) : new JSONContent();
    
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    revisionNo = parser.getInt(REVISION_NO);
    revisionComment = parser.getString(REVISION_COMMENT);
    revisionTimeline = parser.getJSONContent(REVISION_TIMELINE);
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
        JSONBuilder.newJSONField(REVISION_NO, revisionNo),
        JSONBuilder.newJSONField(REVISION_COMMENT, revisionComment),
        revisionTimeline.isEmpty() ? JSONBuilder.VOID_FIELD
            : JSONBuilder.newJSONField(REVISION_TIMELINE, revisionTimeline));
  }
  
  @Override
  public IJSONContent getObjectArchive()
  {
    return objectArchive;
  }
  
  public void setObjectArchive(JSONContent content)
  {
    objectArchive = content;
  }
  
  @Override
  public IBaseEntityDTO getBaseEntityDTOArchive() throws CSFormatException
  {
    BaseEntityDTO object = new BaseEntityDTO();
    object.fromPXON(new JSONContentParser(objectArchive.toString()));
    return object;
  }
  
  @Override
  public IBaseEntityDTO getBaseEntityDTOArchive(String objectArchive) throws CSFormatException
  {
    BaseEntityDTO object = new BaseEntityDTO();
    object.fromPXON(new JSONContentParser(objectArchive));
    return object;
  }
  
  @Override
  public int compareTo(Object o)
  {
    int comparison = super.compareTo(o);
    if (comparison != 0) {
      return comparison;
    }
    return revisionNo - ((ObjectRevisionDTO) o).revisionNo;
  }
  
  @Override
  public int getRevisionNo()
  {
    return revisionNo;
  }
  
  @Override
  public Map<ChangeCategory, ICSEList> getTimelines() throws CSFormatException
  {
    ITimelineDTO timelineDTO = new TimelineDTO();
    timelineDTO.fromJSON(revisionTimeline.toString());
    return timelineDTO.getTimelines();
  }
  
  @Override
  public String getRevisionComment()
  {
    return revisionComment;
  }
  
  @Override
  public IUserDTO getAuthor()
  {
    return new UserDTO(getUserIID(), getUserName());
  }
  
  @Override
  public IJSONContent getRevisionTimeline()
  {
    return revisionTimeline;
  }
}
