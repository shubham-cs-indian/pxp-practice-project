package com.cs.dam.rdbms.downloadtracker.dto;

import java.sql.SQLException;

import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.dam.rdbms.downloadtracker.idto.IDownloadTrackerDTO;


public class DownloadTrackerDTO extends RDBMSRootDTO implements IDownloadTrackerDTO {
  
  private static final long serialVersionUID = 1L;
  private long              assetInstanceIId;
  private String            assetInstanceName;
  private String            assetFileName;
  private String            assetInstanceClassId;
  private String            assetInstanceClassCode;
  private String            renditionInstanceIId;
  private String            renditionInstanceName;
  private String            renditionFileName;
  private String            renditionInstanceClassId;
  private String            renditionInstanceClassCode;
  private long              primaryKey;
  private String            userId;
  private long              timeStamp;
  private String            comment;
  private String            downloadId;
  private String            userName;
  private boolean           isReset;
  
  public DownloadTrackerDTO() throws SQLException
  {

  }

  public DownloadTrackerDTO(long assetInstanceIId, String assetInstanceName, String assetFileName,
      String assetInstanceClassId, String assetInstanceClassCode, String renditionInstanceIId,
      String renditionInstanceName, String renditionFileName, String renditionInstanceClassId,
      String renditionInstanceClassCode, String userId, long timeStamp, String comment,
      String downloadId, String userName)
  {
    this.assetInstanceIId = assetInstanceIId;
    this.assetInstanceName = assetInstanceName;
    this.assetFileName = assetFileName;
    this.assetInstanceClassId = assetInstanceClassId;
    this.assetInstanceClassCode = assetInstanceClassCode;
    this.renditionInstanceIId = renditionInstanceIId;
    this.renditionInstanceName = renditionInstanceName;
    this.renditionFileName = renditionFileName;
    this.renditionInstanceClassId = renditionInstanceClassId;
    this.renditionInstanceClassCode = renditionInstanceClassCode;
    this.userId = userId;
    this.timeStamp = timeStamp;
    this.comment = comment;
    this.downloadId = downloadId;
    this.userName = userName;
  }

  public DownloadTrackerDTO(IResultSetParser parser) throws SQLException
  {
    this.assetInstanceIId = parser.getLong(IDownloadTrackerDTO.ASSET_INSTANCE_IID);
    this.assetInstanceName = parser.getString("assetInstanceName");
    this.assetFileName = parser.getString("assetFileName");
    this.assetInstanceClassId = parser.getString("assetInstanceClassId");
    this.assetInstanceClassCode = parser.getString("assetInstanceClassCode");
    this.renditionInstanceIId = parser.getString(IDownloadTrackerDTO.RENDITION_INSTANCE_IID);
    this.renditionInstanceName = parser.getString("renditionInstanceName");
    this.renditionFileName = parser.getString("renditionFileName");
    this.renditionInstanceClassId = parser.getString("renditionInstanceClassId");
    this.renditionInstanceClassCode = parser.getString("renditionInstanceClassCode");
    this.primaryKey = parser.getLong("primaryKey");
    this.userId = parser.getString("userId");
    this.timeStamp = parser.getLong("timeStamp");
    this.comment = parser.getString("comment");
    this.downloadId = parser.getString("downloadId");
    this.userName = parser.getString("userName");
    this.isReset = parser.getBoolean("isReset");
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    
  }

  @Override
  public long getAssetInstanceIId()
  {
    return assetInstanceIId;
  }

  @Override
  public void setAssetInstanceIId(long assetInstanceIId)
  {
    this.assetInstanceIId = assetInstanceIId;
  }
  
  @Override
  public String getAssetInstanceName()
  {
    return assetInstanceName;
  }

  @Override
  public void setAssetInstanceName(String assetInstanceName)
  {
    this.assetInstanceName = assetInstanceName;
  }

  @Override
  public String getAssetFileName()
  {
    return assetFileName;
  }

  @Override
  public void setAssetFileName(String assetFileName)
  {
    this.assetFileName = assetFileName;
  }

  @Override
  public String getAssetInstanceClassId()
  {
    return assetInstanceClassId;
  }

  @Override
  public void setAssetInstanceClassId(String assetInstanceClassId)
  {
    this.assetInstanceClassId = assetInstanceClassId;
  }

  @Override
  public String getAssetInstanceClassCode()
  {
    return assetInstanceClassCode;
  }

  @Override
  public void setAssetInstanceClassCode(String assetInstanceClassCode)
  {
    this.assetInstanceClassCode = assetInstanceClassCode;
  }

  @Override
  public String getRenditionInstanceIId()
  {
    return renditionInstanceIId;
  }

  @Override
  public void setRenditionInstanceIid(String renditionInstanceIId)
  {
    this.renditionInstanceIId = renditionInstanceIId;
  }

  @Override
  public String getRenditionInstanceName()
  {
    return renditionInstanceName;
  }

  @Override
  public void setRenditionInstanceName(String renditionInstanceName)
  {
    this.renditionInstanceName = renditionInstanceName;
  }

  @Override
  public String getRenditionFileName()
  {
    return renditionFileName;
  }

  @Override
  public void setRenditionFileName(String renditionFileName)
  {
    this.renditionFileName = renditionFileName;
  }

  @Override
  public String getRenditionInstanceClassId()
  {
    return renditionInstanceClassId;
  }

  @Override
  public void setRenditionInstanceClassId(String renditionInstanceClassId)
  {
    this.renditionInstanceClassId = renditionInstanceClassId;
  }

  @Override
  public String getRenditionInstanceClassCode()
  {
    return renditionInstanceClassCode;
  }

  @Override
  public void setRenditionInstanceClassCode(String renditionInstanceClassCode)
  {
    this.renditionInstanceClassCode = renditionInstanceClassCode;
  }

  @Override
  public Long getPrimaryKey()
  {
    return primaryKey;
  }

  @Override
  public void setPrimaryKey(Long primaryKey)
  {
    this.primaryKey = primaryKey;
    
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }

  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  @Override
  public long getTimeStamp()
  {
    return timeStamp;
  }

  @Override
  public void setTimeStamp(long timeStamp)
  {
    this.timeStamp = timeStamp;
  }

  @Override
  public String getComment()
  {
    return comment;
  }

  @Override
  public void setComment(String comment)
  {
    this.comment = comment;
  }

  @Override
  public String getDownloadId()
  {
    return downloadId;
  }

  @Override
  public void setDownloadId(String downloadId)
  {
    this.downloadId = downloadId;
  }

  @Override
  public String getUserName()
  {
    return userName;
  }

  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  @Override
  public boolean isReset()
  {
    return isReset;
  }

  @Override
  public void setReset(boolean isReset)
  {
    this.isReset = isReset;
  }

}
