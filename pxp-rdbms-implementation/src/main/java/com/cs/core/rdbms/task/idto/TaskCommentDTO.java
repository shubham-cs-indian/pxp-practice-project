package com.cs.core.rdbms.task.idto;

import java.util.HashSet;
import java.util.Set;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.task.idto.ITaskCommentDTO;
import com.cs.core.rdbms.task.idto.ITaskCommentDTOBuilder;
import com.cs.core.technical.exception.CSFormatException;

public class TaskCommentDTO extends SimpleDTO implements ITaskCommentDTO {
  
  private final String TIME        = "time";
  private final String USER_NAME   = "userName";
  private final String TEXT        = "text";
  private final String ATTACHMENTS = "attachments";
  
  private long         time;
  private String       userName;
  private String       text;
  private Set<Long>    attachments;
  
  public static String getCacheCode(String taskCode)
  {
    return String.format("%c:%s", CSEObject.CSEObjectType.Task.letter(), taskCode);
  }
  
  @Override
  public long getTime()
  {
    return time;
  }
  
  @Override
  public Set<Long> getAttachments()
  {
    if (attachments == null)
      attachments = new HashSet<>();
    return attachments;
  }
  
  @Override
  public String getText()
  {
    return text;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(TIME, time),
        JSONBuilder.newJSONField(USER_NAME, userName), JSONBuilder.newJSONField(TEXT, text),
        !getAttachments().isEmpty() ? JSONBuilder.newJSONLongArray(ATTACHMENTS, getAttachments())
            : JSONBuilder.VOID_FIELD);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    time = json.getLong(TIME);
    userName = json.getString(USER_NAME);
    text = json.getString(TEXT);
    getAttachments().clear();
    json.getJSONArray(ATTACHMENTS)
        .forEach((iid) -> {
          getAttachments().add((Long) iid);
        });
  }
  
  public static class TaskCommentDTOBuilder implements ITaskCommentDTOBuilder {
    
    private final TaskCommentDTO commentDTO;
    
    /**
     * Default constructor
     */
    public TaskCommentDTOBuilder()
    {
      commentDTO = new TaskCommentDTO();
    }
    
    @Override
    public ITaskCommentDTOBuilder time(long time)
    {
      commentDTO.time = time;
      return this;
    }
    
    @Override
    public ITaskCommentDTOBuilder attachments(Set<Long> attachments)
    {
      commentDTO.attachments = attachments;
      return this;
    }
    
    @Override
    public ITaskCommentDTOBuilder attachment(Long attachment)
    {
      commentDTO.getAttachments()
          .add(attachment);
      return this;
    }
    
    @Override
    public ITaskCommentDTOBuilder text(String text)
    {
      commentDTO.text = text;
      return this;
    }
    
    @Override
    public ITaskCommentDTOBuilder userName(String userName)
    {
      commentDTO.userName = userName;
      return this;
    }
    
    @Override
    public ITaskCommentDTO build()
    {
      return commentDTO;
    }
  }
}
