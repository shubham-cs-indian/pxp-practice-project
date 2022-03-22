package com.cs.core.rdbms.task.idto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.FormFieldDTO;
import com.cs.core.rdbms.config.dto.TagValueDTO;
import com.cs.core.rdbms.config.dto.TaskDTO;
import com.cs.core.rdbms.config.idto.IFormFieldDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.task.idto.ITaskCommentDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTOBuilder;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.IUserDTO;

public class TaskRecordDTO extends RDBMSRootDTO implements ITaskRecordDTO {
  
  public static final String         COMMENTS            = "comments";
  public static final String         FORM_FIELDS          = "formFields";
  private long                       taskIID;
  private ITaskDTO                   taskDTO;
  private String                     taskName            = "";
  private long                       parentTaskIID;
  private long                       propertyIID;
  private Set<ITaskRecordDTO>        childrens;
  private Map<RACIVS, Set<String>>   roles;
  private Map<RACIVS, Set<IUserDTO>> users;
  private ITagValueDTO               statusTag;
  private ITagValueDTO               priorityTag;
  private long                       createdTime;
  private long                       dueDate;
  private long                       overDueDate;
  private long                       startDate;
  private Set<Long>                  attachments;
  private String                     description         ="";
  private List<ITaskCommentDTO>      comments;
  private boolean                    wfCreated;
  private String                     wfProcessID         = "";
  private String                     wfProcessInstanceID = "";
  private String                     wfTaskInstanceID    = "";
  private JSONContent                position;
  private List<IFormFieldDTO>        formFields;
  
  public TaskRecordDTO()
  {
  }
  
  public TaskRecordDTO(IResultSetParser parser) throws SQLException, CSFormatException
  {
    super(parser.getLong("entityiid"));
    taskDTO = new TaskDTO(parser.getString("taskcode"), ITaskDTO.TaskType.UNDEFINED);
    
    taskIID = parser.getLong("taskIID");
    taskName = parser.getString("taskName");
    parentTaskIID = parser.getLong("parentTaskIID");
    propertyIID = parser.getLong("propertyIID");
    statusTag = new TagValueDTO(parser.getString("statuscode"));
    priorityTag = new TagValueDTO(parser.getString("prioritycode"));
    createdTime = parser.getLong("createdTime");
    dueDate = parser.getLong("dueDate");
    overDueDate = parser.getLong("overDueDate");
    startDate = parser.getLong("startDate");
    attachments = new HashSet<>(Arrays.asList(parser.getLongArray("attachments")));
    description = parser.getString("description");
    this.getPosition().fromString(parser.getStringFromJSON("position"));
    JSONContentParser jsonParser = new JSONContentParser(parser.getStringFromJSON("comments"));
    JSONArray commentsArray = jsonParser.getJSONArray(COMMENTS);
    for (Object recordJSON : commentsArray) {
      JSONContentParser commentParser = new JSONContentParser((JSONObject) recordJSON);
      TaskCommentDTO commentDTO = new TaskCommentDTO();
      commentDTO.fromJSON(commentParser);
      getComments().add((ITaskCommentDTO)commentDTO);
    }
    JSONContentParser jsonParserforFields = new JSONContentParser(
        parser.getStringFromJSON("formFields"));
    JSONArray formFields = jsonParserforFields.getJSONArray(FORM_FIELDS);
    for (Object recordJSON : formFields) {
      JSONContentParser formFieldparser = new JSONContentParser((JSONObject) recordJSON);
      FormFieldDTO formFieldDTO = new FormFieldDTO();
      formFieldDTO.fromJSON(formFieldparser);
      getFormFields().add((IFormFieldDTO) formFieldDTO);
    }
    wfCreated = parser.getBoolean("wfCreated");
    wfProcessID = parser.getString("wfProcessID");
    wfProcessInstanceID = parser.getString("wfProcessInstanceID");
    wfTaskInstanceID = parser.getString("wfTaskInstanceID");

    String userIID = parser.getString("userIIDs");
    String userCode = parser.getString("usercodes");
    String userRACIVS = parser.getString("userRACIVSs");
    String[] userIIDs = userIID.split(",");
    String[] userCodes = userCode.split(",");
    String[] userRACIVSs = userRACIVS.split(",");
    // initialize users map if null
    getUsersMap();
    for (int i = 0; i < userIIDs.length; i++) {
      Set<IUserDTO> iids = users.get(RACIVS.valueOf(Integer.parseInt(userRACIVSs[i])));
      if (iids != null) {
        IUserDTO userDTO = new UserDTO(Long.parseLong(userIIDs[i]), userCodes[i]);
        iids.add(userDTO);
      }
      else {
        Set<IUserDTO> user = new HashSet<IUserDTO>();
        IUserDTO userDTO = new UserDTO(Long.parseLong(userIIDs[i]), userCodes[i]);
        user.add(userDTO);
        users.put(RACIVS.valueOf(Integer.parseInt(userRACIVSs[i])), user);
      }
    }
    String roleCodes = parser.getString("roleCodes");
    String roleRCIs = parser.getString("roleRCIs");
    String[] roleCodesArray = roleCodes == null || roleCodes.trim().equals("") ? new String[0] :roleCodes.split(",");
    String[] roleRCIArray = roleRCIs == null || roleRCIs.trim().equals("") ? new String[0] : roleRCIs.split(",");
   // initialize roles map if null
    getRolesMap();
    for (int i = 0; i < roleCodesArray.length; i++) {
      Set<String> codes = roles.get(RACIVS.valueOf(Integer.parseInt(roleRCIArray[i])));
      if (codes != null) {
        codes.add(roleCodesArray[i]);
      }    
      else {
        Set<String> role = new HashSet<>();
        role.add(roleCodesArray[i]);
        roles.put(RACIVS.valueOf(Integer.parseInt(roleRCIArray[i])), role);
      }
    }
    
    
  }
  
  @Override
  public long getTaskIID()
  {
    return taskIID;
  }
  
  /**
   * @param taskIID
   *          overwritten task IID
   * 
   */
  public void setTaskIID(long taskIID)
  {
    this.taskIID = taskIID;
  }
  
  @Override
  public ITaskDTO getTask()
  {
    return taskDTO != null ? taskDTO : new TaskDTO();
  }
  
  @Override
  public long getEntityIID()
  {
    return getIID();
  }
  
  @Override
  public long getParentTaskIID()
  {
    return parentTaskIID;
  }
  
  @Override
  public long getPropertyIID()
  {
    return propertyIID;
  }
  
  @Override
  public Set<ITaskRecordDTO> getChildren()
  {
    if (childrens == null)
      childrens = new HashSet<>();
    return childrens;
  }
  
  public void addChildren(ITaskRecordDTO taskRecordDTO)
  {
    getChildren().add(taskRecordDTO);
  }
  
  @Override
  public Map<RACIVS, Set<String>> getRolesMap()
  {
    if (roles == null)
      roles = new HashMap<>();
    return roles;
  }
  
  @Override
  public void setRolesMap(Map<RACIVS, Set<String>> roles)
  {
    this.roles = roles;
    setChanged(true);
  }
  
  @Override
  public void addRole(RACIVS racivs, Set<String> roles)
  {
    this.getRolesMap()
        .put(racivs, roles);
    setChanged(true);
  }
  
  @Override
  public Map<RACIVS, Set<IUserDTO>> getUsersMap()
  {
    if (users == null)
      users = new HashMap<>();
    return users;
  }
  
  @Override
  public void setUserMap(Map<RACIVS, Set<IUserDTO>> users)
  {
    this.users = users;
    setChanged(true);
  }
  
  @Override
  public void addUser(RACIVS racivs, Set<IUserDTO> users)
  {
    this.getUsersMap().put(racivs, users);
    setChanged(true);
  }
  
  @Override
  public ITagValueDTO getStatusTag()
  {
    if (statusTag == null)
      statusTag = new TagValueDTO();
    return statusTag;
  }
  
  @Override
  public void setStatusTag(ITagValueDTO statusTag)
  {
    this.statusTag = statusTag;
  }
  
  @Override
  public ITagValueDTO getPriorityTag()
  {
    if (priorityTag == null)
      priorityTag = new TagValueDTO();
    return priorityTag;
  }
  
  @Override
  public void setPriorityTag(ITagValueDTO priorityTag)
  {
    this.priorityTag = priorityTag;
  }
  
  @Override
  public long getCreatedTime()
  {
    return createdTime;
  }
  
  @Override
  public long getDueDate()
  {
    return dueDate;
  }
  
  @Override
  public void setDueDate(long dueDate)
  {
    this.dueDate = dueDate;
  }
  
  @Override
  public long getOverdueDate()
  {
    return overDueDate;
  }
  
  @Override
  public void setOverdueDate(long overdueDate)
  {
    this.overDueDate = overdueDate;
  }
  
  @Override
  public long getStartDate()
  {
    return startDate;
  }
  
  @Override
  public void setStartDate(long startDate)
  {
    this.startDate = startDate;
  }
  
  @Override
  public String getTaskName()
  {
    return taskName;
  }
  
  @Override
  public void setTaskName(String taskName)
  {
    this.taskName = taskName;
  }
  
  @Override
  public Set<Long> getAttachments()
  {
    if (attachments == null)
      attachments = new HashSet<>();
    return attachments;
  }
  
  @Override
  public void setAttachments(Set<Long> attachments)
  {
    this.attachments = attachments;
  }
  
  @Override
  public void addAttachment(Long attachment)
  {
    this.getAttachments()
        .add(attachment);
  }
  
  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }

  @Override
  public List<ITaskCommentDTO> getComments()
  {
    if (comments == null)
      comments = new ArrayList<>();
    return comments;
  }
  
  @Override
  public void setComments(List<ITaskCommentDTO> comments)
  {
    this.comments = comments;
  }
  
  @Override
  public void addComment(ITaskCommentDTO comment)
  {
    this.getComments().add(comment);
  }
  
  @Override
  public boolean isWfCreated()
  {
    return wfCreated;
  }
  
  @Override
  public String getWfProcessID()
  {
    return wfProcessID;
  }
  
  @Override
  public String getWfProcessInstanceID()
  {
    return wfProcessInstanceID;
  }
  
  @Override
  public String getWfTaskInstanceID()
  {
    return wfTaskInstanceID;
  }
  
  @Override
  public IJSONContent getPosition()
  {
    if (position == null)
      position = new JSONContent();
    return position;
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
  
  /**
   * hashCode with unique task IID 
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(5, 17).append(taskIID)
        .build();
  }
  
  /**
   * equals check unique task IID 
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TaskRecordDTO other = (TaskRecordDTO) obj;
    if (!new EqualsBuilder().append(this.taskIID, other.taskIID).isEquals()) {
      return false;
    }
    return true;
  }
  
  @Override
  public int compareTo(Object t)
  {
    int compare = super.compareTo(t); // comparison by IID
    if (compare != 0) {
      return compare;
    }
    TaskRecordDTO that = (TaskRecordDTO) t;
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    if (taskIID != 0 && that.taskIID != 0)
      return compareToBuilder.append(taskIID, that.taskIID).toComparison();
    if (compare != 0)
      return compare;
    return 0;
  }
  
  /**
   * builder class for Task Record DTO
   *
   * @author Janak.Gurme
   */
  public static class TaskRecordDTOBuilder implements ITaskRecordDTOBuilder {
    
    private final TaskRecordDTO taskRecordDTO;
    
    /**
     * Default constructor
     */
    public TaskRecordDTOBuilder()
    {
      taskRecordDTO = new TaskRecordDTO();
    }
    
    @Override
    public ITaskRecordDTOBuilder taskIID(long taskIID)
    {
      taskRecordDTO.taskIID = taskIID;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder taskName(String taskName)
    {
      taskRecordDTO.taskName = taskName;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder task(ITaskDTO taskDTO)
    {
      taskRecordDTO.taskDTO = taskDTO;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder entityIID(long iid)
    {
      taskRecordDTO.setIID(iid);
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder parentTaskIID(long parentTaskIID)
    {
      taskRecordDTO.parentTaskIID = parentTaskIID;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder propertyIID(long propertyIID)
    {
      taskRecordDTO.propertyIID = propertyIID;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder childrens(Set<ITaskRecordDTO> childrens)
    {
      taskRecordDTO.childrens = childrens;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder children(ITaskRecordDTO children)
    {
      taskRecordDTO.getChildren()
          .add(children);
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder rolesMap(Map<RACIVS, Set<String>> roles)
    {
      taskRecordDTO.roles = roles;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder roleMap(RACIVS racivis, Set<String> roles)
    {
      taskRecordDTO.getRolesMap()
          .put(racivis, roles);
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder userMap(RACIVS racivis, Set<IUserDTO> users)
    {
      taskRecordDTO.getUsersMap().put(racivis, users);
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder usersMap(Map<RACIVS, Set<IUserDTO>> users)
    {
      taskRecordDTO.users = users;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder statusTag(ITagValueDTO statusTag)
    {
      taskRecordDTO.statusTag = statusTag;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder priorityTag(ITagValueDTO priorityTag)
    {
      taskRecordDTO.priorityTag = priorityTag;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder createdTime(long createdTime)
    {
      taskRecordDTO.createdTime = createdTime;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder dueDate(long dueDate)
    {
      taskRecordDTO.dueDate = dueDate;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder overdueDate(long overdueDate)
    {
      taskRecordDTO.overDueDate = overdueDate;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder startDate(long startDate)
    {
      taskRecordDTO.startDate = startDate;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder wfCreated(boolean wfCreated)
    {
      taskRecordDTO.wfCreated = wfCreated;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder wfProcessID(String wfProcessID)
    {
      taskRecordDTO.wfProcessID = wfProcessID;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder wfProcessInstanceID(String wfProcessInstanceID)
    {
      taskRecordDTO.wfProcessInstanceID = wfProcessInstanceID;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder wfTaskInstanceID(String wfTaskInstanceID)
    {
      taskRecordDTO.wfTaskInstanceID = wfTaskInstanceID;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder attachments(Set<Long> attachments)
    {
      taskRecordDTO.attachments = attachments;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder attachment(Long attachment)
    {
      taskRecordDTO.getAttachments()
          .add(attachment);
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder description(String description)
    {
      taskRecordDTO.description = description;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder comments(List<ITaskCommentDTO> comments)
    {
      taskRecordDTO.comments = comments;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder comment(ITaskCommentDTO comments)
    {
      taskRecordDTO.getComments().add(comments);
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder position(String jsonContent) throws CSFormatException
    {
      taskRecordDTO.getPosition().fromString(jsonContent);
      return this;
    }

    @Override
    public ITaskRecordDTO build()
    {
      return taskRecordDTO;
    }

    @Override
    public ITaskRecordDTOBuilder wfFormFields(List<IFormFieldDTO> formfields)
    {
      taskRecordDTO.formFields = formfields;
      return this;
    }
    
    @Override
    public ITaskRecordDTOBuilder wfFormField(IFormFieldDTO formfields)
    {
      taskRecordDTO.getFormFields()
          .add(formfields);
      return this;
    }


  }

  @Override
  public List<IFormFieldDTO> getFormFields()
  {
    if (formFields == null)
      formFields = new ArrayList<>();
    return formFields;
  }

  @Override
  public void setFormFields(List<IFormFieldDTO> formField)
  {
   this.formFields = formField;
    
  }

  @Override
  public void setFormField(IFormFieldDTO formField)
  {
  this.getFormFields().add(formField);
  }

}
