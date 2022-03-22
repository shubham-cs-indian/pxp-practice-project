package com.cs.core.rdbms.task.idto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.rdbms.config.idto.IFormFieldDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.IRootDTO;
import com.cs.core.technical.rdbms.idto.IUserDTO;

/**
 * A task record is the instance of a task attached to an entity
 *
 * @author vallee
 */
public interface ITaskRecordDTO extends IRootDTO, Comparable {

  /**
   * Constants for task types
   */
  public enum RACIVS {

    UNDEFINED, RESPONSIBLE, ACCOUNTABLE, CONSULTED, INFORMED, VERIFIER, SIGNOFF;

    private static final RACIVS[] values = values();

    public static RACIVS valueOf(int ordinal) {
      return values[ordinal];
    }
  }

  /**
   * @return the unique identifier of this record
   */
  public long getTaskIID();

  /**
   * @return the name of the task record
   */
  public String getTaskName();

  /**
   * @param taskName overwritten as taskName
   */
  public void setTaskName(String taskName);

  /**
   * @return the parent task iid
   */
  public long getParentTaskIID();

  /**
   * @return the task configured for this record
   */
  public ITaskDTO getTask();

  /**
   * @return the entity which owns that record
   */
  public long getEntityIID();

  /**
   * @return the property of this record
   */
  public long getPropertyIID();

  /**
   * @return the subtasks of this task
   */
  public Set<ITaskRecordDTO> getChildren();

  /**
   * @return the roles attached to this task, according to their RACIVS position
   */
  public Map<RACIVS, Set<String>> getRolesMap();

  /**
   * @param roles overwritten as roles
   */
  public void setRolesMap(Map<RACIVS, Set<String>> roles);

  /**
   * @param add role in roles
   */
  public void addRole(RACIVS racivs, Set<String> roles);

  /**
   * @return the user IIDs attached to this task, according to their RACIVS position
   */
  public Map<RACIVS, Set<IUserDTO>> getUsersMap();

  /**
   * @param users overwritten as users
   */
  public void setUserMap(Map<RACIVS, Set<IUserDTO>> users);

  /**
   * @param add user in users
   */
  public void addUser(RACIVS racivs, Set<IUserDTO> users);

  /**
   * @return the status tag of the task
   */
  public ITagValueDTO getStatusTag();

  /**
   * @param statusTag overwritten as statusTag
   */
  public void setStatusTag(ITagValueDTO statusTag);

  /**
   * @return the priority tag of the task
   */
  public ITagValueDTO getPriorityTag();

  /**
   * @param priorityTag overwritten as priorityTag
   */
  public void setPriorityTag(ITagValueDTO priorityTag);

  /**
   * @return the attachment identifiers of this task
   */
  public Set<Long> getAttachments();

  /**
   * @param attachments overwritten as attachments
   */
  public void setAttachments(Set<Long> attachments);

  /**
   * @param attachment to be added in attachments
   */
  public void addAttachment(Long attachment);

  /**
   *
   * @return description on task
   */
  public String getDescription();

  /**
   *
   * @param description
   */
  public void setDescription(String description);

  /**
   * @return comments of the task
   */
  public List<ITaskCommentDTO> getComments();

  /**
   * @param comments overwritten as comments
   */
  public void setComments(List<ITaskCommentDTO> comments);
  
  /**
   * @return formField of the task
   */
  public List<IFormFieldDTO> getFormFields();

  /**
   * @param formFields 
   */
  public void setFormFields(List<IFormFieldDTO> formField);
  
  /**
   * @param formFields to be added
   */
  
  public void setFormField(IFormFieldDTO formField);

  /**
   * @param comment to be added comments
   */
  public void addComment(ITaskCommentDTO comment);

  /**
   * @return the created time of the task
   */
  public long getCreatedTime();

  /**
   * @return the due date
   */
  public long getDueDate();

  /**
   * @param dueDate overwritten as dueDate
   */
  public void setDueDate(long dueDate);

  /**
   * @return the overdue date
   */
  public long getOverdueDate();

  /**
   * @param overdueDate overwritten as overdueDate
   */
  public void setOverdueDate(long overdueDate);

  /**
   * @return the start date
   */
  public long getStartDate();

  /**
   * @param startDate overwritten as startDate
   */
  public void setStartDate(long startDate);

  /**
   * @return the workflow status of the task
   */
  public boolean isWfCreated();

  /**
   * @return the corresponding Workflow process ID
   */
  public String getWfProcessID();

  /**
   * @return the corresponding Workflow instance ID
   */
  public String getWfProcessInstanceID();

  /**
   * @return the corresponding Workflow task ID
   */
  public String getWfTaskInstanceID();

  /**
   * @return the position of task as JSON content
   */
  public IJSONContent getPosition();
}
