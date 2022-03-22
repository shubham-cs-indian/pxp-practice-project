package com.cs.core.rdbms.tracking.dto;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.data.TextArchive;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.ijosn.IJSONContent;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.sql.SQLException;

/**
 * Object Tracking Data Transfer Object
 *
 * @author PankajGajjar
 */
public class ObjectTrackingDTO extends TrackingDTO implements IObjectTrackingDTO {
  
  private static final String OBJECT        = PXONTag.object.toReadOnlyCSETag();
  private static final String TIMELINE_DATA = PXONTag.changed.toJSONContentTag();
  private static final String PXON_EXTRACT  = PXONTag.pxon.toJSONContentTag();
  private long                objectIID     = 0L;
  private String              objectID      = "";
  private ICatalogDTO         catalog       = new CatalogDTO();
  private JSONContent         timelineData  = new JSONContent();
  private JSONContent         pxonExtract   = new JSONContent();
  
  /**
   * Enabled default constructor
   */
  public ObjectTrackingDTO()
  {
  }
  
  /**
   * Value Constructor
   *
   * @param tracking
   * @param catalogCode
   * @param objectID
   * @param objectIID
   */
  public ObjectTrackingDTO(TrackingDTO tracking, ICatalogDTO catalog, String objectID,
      long objectIID)
  {
    super(tracking);
    this.catalog = catalog;
    this.objectID = objectID;
    this.objectIID = objectIID;
  }
  
  /**
   * Copy constructor
   *
   * @param source
   */
  public ObjectTrackingDTO(ObjectTrackingDTO source)
  {
    super(source);
    objectID = source.getObjectID();
    objectIID = source.getObjectIID();
    catalog = source.getCatalog();
    timelineData = (JSONContent) source.getJSONTimelineData();
    pxonExtract = (JSONContent) source.getJSONExtract();
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    CSEObject object = (CSEObject) parser.getCSEElement(OBJECT);
    objectIID = object.getIID();
    objectID = object.getCode();
    catalog = new CatalogDTO(object.getSpecification(ICSEElement.Keyword.$ctlg), object.getSpecification(ICSEElement.Keyword.$org));
    timelineData = parser.getJSONContent(TIMELINE_DATA);
    pxonExtract = parser.getJSONContent(PXON_EXTRACT);
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    CSEObject object = new CSEObject(CSEObjectType.Entity);
    object.setIID(objectIID);
    object.setCode(objectID);
    object.setSpecification(ICSEElement.Keyword.$ctlg, catalog.getCatalogCode());
    String organizationCode = catalog.getOrganizationCode();
    if (!organizationCode.equals(IStandardConfig.STANDARD_ORGANIZATION_CODE))
      object.setSpecification(Keyword.$org, organizationCode);
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
        JSONBuilder.newJSONField(OBJECT, object),
        timelineData.isEmpty() ? JSONBuilder.VOID_FIELD
            : JSONBuilder.newJSONField(TIMELINE_DATA, timelineData),
        pxonExtract.isEmpty() ? JSONBuilder.VOID_FIELD
            : JSONBuilder.newJSONField(PXON_EXTRACT, pxonExtract));
  }
  
  @Override
  public void set(IResultSetParser rsParser) throws SQLException, CSFormatException
  {
    super.set(rsParser);
    catalog = new CatalogDTO(rsParser.getString("catalogCode"), rsParser.getString("organizationCode"));
    objectIID = rsParser.getLong("objectIID");
    objectID = rsParser.getString("objectID");
    String jsonTimeLine = rsParser.getStringFromJSON("timelineData");
    if (!jsonTimeLine.isEmpty())
      timelineData = new JSONContent(jsonTimeLine);
    String jsonContent = TextArchive.unzip(rsParser.getBinaryBlob("pxondelta"))
        .trim();
    if (!jsonContent.isEmpty())
      pxonExtract = new JSONContent(jsonContent);
  }
  
  @Override
  public long getTrackIID()
  {
    return this.getIID();
  }
  
  @Override
  public long getObjectIID()
  {
    return objectIID;
  }
  
  @Override
  public String getObjectID()
  {
    return objectID;
  }
  
  @Override
  public ICatalogDTO getCatalog()
  {
    return catalog;
  }
  
  @Override
  public IJSONContent getJSONTimelineData()
  {
    return timelineData;
  }
  
  @Override
  public TimelineDTO getTimelineData() throws CSFormatException
  {
    TimelineDTO tldata = new TimelineDTO();
    tldata.fromJSON(timelineData.toString());
    return tldata;
  }
  
  @Override
  public IJSONContent getJSONExtract()
  {
    return pxonExtract;
  }
  
  /**
   * @param jsonComments
   *          overwritten tracking data
   * @throws CSFormatException
   */
  public void setTrackingData(String jsonComments) throws CSFormatException
  {
    timelineData.fromString(jsonComments);
  }

  public void setPxonExtract(String jsonComments) throws CSFormatException
  {
    pxonExtract.fromString(jsonComments);
  }

  @Override
  public int compareTo(Object t)
  {
    int comparison = new CompareToBuilder()
        .append(this.getObjectIID(), ((ObjectTrackingDTO) t).getObjectIID())
        .toComparison();
    if (comparison != 0)
      return comparison;
    return super.compareTo(t);
  }
}
