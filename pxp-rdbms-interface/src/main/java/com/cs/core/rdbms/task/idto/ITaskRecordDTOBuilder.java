package com.cs.core.rdbms.task.idto;

import com.cs.core.rdbms.config.idto.IFormFieldDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Builder interface to construct ITaskRecordDTOs
 * @author janak
 */
  public interface ITaskRecordDTOBuilder extends IRootDTOBuilder<ITaskRecordDTO> {

    /**
     * @param taskIID the unique identifier of this record
     * @return the builder
     */
    public ITaskRecordDTOBuilder taskIID(long taskIID);

    /**
     * @param taskName the task name
     * @return the builder
     */
    public ITaskRecordDTOBuilder taskName(String taskName);

    /**
     * @param taskDTO a task data
     * @return the builder
     */
    public ITaskRecordDTOBuilder task(ITaskDTO taskDTO);

    /**
     * @param iid the entity identifier
     * @return the builder 
     */
    public ITaskRecordDTOBuilder entityIID(long iid);

    /**
     * @param parentTaskIID  the parent task identifier
     * @return the builder
     */
    public ITaskRecordDTOBuilder parentTaskIID(long parentTaskIID);

    /**
     * @param propertyIID the property identifier
     * @return the builder
     */
    public ITaskRecordDTOBuilder propertyIID(long propertyIID);

    /**
     * @param children the set of children tasks
     * @return the builder
     */
    public ITaskRecordDTOBuilder childrens(Set<ITaskRecordDTO> children);

    /**
     * @param child te child task
     * @return the builder
     */
    public ITaskRecordDTOBuilder children(ITaskRecordDTO child);

    /**
     * @param roles the overwritten set of roles
     * @return the builder
     */
    public ITaskRecordDTOBuilder rolesMap(Map<ITaskRecordDTO.RACIVS, Set<String>> roles);

  /**
   * @param racivis the overwritten RACIVS permission
   * @param roles the concerned roles
   * 
   * @return the builder
   */
    public ITaskRecordDTOBuilder roleMap(ITaskRecordDTO.RACIVS racivis, Set<String> roles);

    /**
     * @param users the overwritten set of users
     * @return the builder
     */
    public ITaskRecordDTOBuilder usersMap(Map<ITaskRecordDTO.RACIVS, Set<IUserDTO>> users);
  
  /**
   * @param racivis the overwritten RACIVS permission
   * @param users the concerned users
   * 
   * @return the builder
   */
    public ITaskRecordDTOBuilder userMap(ITaskRecordDTO.RACIVS racivis, Set<IUserDTO> users);

    /**
     * @param statusTag the overwritten status
     * @return the builder
     */
    public ITaskRecordDTOBuilder statusTag(ITagValueDTO statusTag);

    /**
     * @param priorityTag the overwritten priority
     * @return the builder
     */
    public ITaskRecordDTOBuilder priorityTag(ITagValueDTO priorityTag);

    /**
     * @param attachments the overwritten set of attachments
     * @return the builder
     */
    public ITaskRecordDTOBuilder attachments(Set<Long> attachments);

    /**
     * @param attachment the overwritten attachment
     * @return the builder
     */
    public ITaskRecordDTOBuilder attachment(Long attachment);

    /**
     * @param description the task description
     * @return the builder
     */
    public ITaskRecordDTOBuilder description(String description);

    /**
     * @param comments the task list of comments
     * @return the builder
     */
    public ITaskRecordDTOBuilder comments(List<ITaskCommentDTO> comments);

    /**
     * @param comment the task comment
     * @return the builder
     */
    public ITaskRecordDTOBuilder comment(ITaskCommentDTO comment);

    /**
     * @param createdTime the overwritten create time
     * @return the builder
     */
    public ITaskRecordDTOBuilder createdTime(long createdTime);

    /**
     * @param dueDate the overwritten due date
     * @return the builder
     */
    public ITaskRecordDTOBuilder dueDate(long dueDate);

    /**
     * @param overdueDate the overwritten overdue date
     * @return the builder
     */
    public ITaskRecordDTOBuilder overdueDate(long overdueDate);

    /**
     * @param startDate the overwritten start date
     * @return the builder
     */
    public ITaskRecordDTOBuilder startDate(long startDate);

    /**
     * @param wfCreated true when attached to a workflow task
     * @return the builder
     */
    public ITaskRecordDTOBuilder wfCreated(boolean wfCreated);

    /**
     * @param wfProcessID the corresponding workflow task ID
     * @return the builder
     */
    public ITaskRecordDTOBuilder wfProcessID(String wfProcessID);

    /**
     * @param wfProcessInstanceID the corresponding workflow instance ID
     * @return the builder
     */
    public ITaskRecordDTOBuilder wfProcessInstanceID(String wfProcessInstanceID);

    /**
     * @param wfTaskInstanceID the corresponding workflow ID
     * @return the builder
     */
    public ITaskRecordDTOBuilder wfTaskInstanceID(String wfTaskInstanceID);
    
    /**
     * @param formFields the corresponding formFields
     * @return the builder
     */
    public ITaskRecordDTOBuilder wfFormFields(List<IFormFieldDTO> formFields);
    
    /**
     * @param formFields the corresponding formFields
     * @return the builder
     */   
    public ITaskRecordDTOBuilder wfFormField(IFormFieldDTO formField);

    /**
     * @param position the task position for asset
     * @return the builder
     * @throws CSFormatException in case of format exception
     */
    public ITaskRecordDTOBuilder position(String position) throws CSFormatException;


  }
