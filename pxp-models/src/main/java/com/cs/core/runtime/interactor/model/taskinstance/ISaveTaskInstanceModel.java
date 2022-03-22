package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.entity.comment.IComment;
import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.taskinstance.IBaseTaskInstance;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedRoleInstanceModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.List;

@JsonTypeInfo(use = Id.NONE)
public interface ISaveTaskInstanceModel extends IBaseTaskInstance, IModel {
  
  public static final String ADDED_INCLUSIONS         = "addedInclusions";
  public static final String DELETED_INCLUSIONS       = "deletedInclusions";
  public static final String ADDED_EXCLUSIONS         = "addedExclusions";
  public static final String DELETED_EXCLUSIONS       = "deletedExclusions";
  public static final String MODIFIED_INCLUSIONS      = "modifiedInclusions";
  public static final String MODIFIED_EXCLUSIONS      = "modifiedExclusions";
  public static final String ADDED_LINKED_ENTITIES    = "addedLinkedEntities";
  public static final String DELETED_LINKED_ENTITIES  = "deletedLinkedEntities";
  public static final String MODIFIED_LINKED_ENTITIES = "modifiedLinkedEntities";
  public static final String ADDED_TAGS               = "addedTags";
  public static final String DELETED_TAGS             = "deletedTags";
  public static final String MODIFIED_TAGS            = "modifiedTags";
  public static final String ADDED_COMMENTS           = "addedComments";
  public static final String DELETED_COMMENTS         = "deletedComments";
  public static final String MODIFIED_COMMENTS        = "modifiedComments";
  public static final String ADDED_SUBTASKS           = "addedSubtasks";
  public static final String DELETED_SUBTASKS         = "deletedSubtasks";
  public static final String MODIFIED_SUBTASKS        = "modifiedSubtasks";
  public static final String ADDED_ATTACHMENTS        = "addedAttachments";
  public static final String DELETED_ATTACHMENTS      = "deletedAttachments";
  public static final String ADDED_ROLES              = "addedRoles";
  public static final String DELETED_ROLES            = "deletedRoles";
  public static final String MODIFIED_ROLES           = "modifiedRoles";
  public static final String STATUS_TAG_ID            = "statusTagId";
  public static final String PRIORITY_TAG_ID          = "priorityTagId";
  public static final String RESPONSIBLE              = "responsible";
  public static final String ACCOUNTABLE              = "accountable";
  public static final String CONSULTED                = "consulted";
  public static final String INFORMED                 = "informed";
  public static final String VERIFY                   = "verify";
  public static final String SIGN_OFF                 = "signoff";
  
  public List<IEventInstanceSchedule> getAddedInclusions();
  
  public void setAddedInclusions(List<IEventInstanceSchedule> addedInclusions);
  
  public List<IEventInstanceSchedule> getDeletedInclusions();
  
  public void setDeletedInclusions(List<IEventInstanceSchedule> deletedInclusions);
  
  public List<IEventInstanceSchedule> getAddedExclusions();
  
  public void setAddedExclusions(List<IEventInstanceSchedule> addedExclusions);
  
  public List<IEventInstanceSchedule> getDeletedExclusions();
  
  public void setDeletedExclusions(List<IEventInstanceSchedule> deletedExclusions);
  
  public List<IEventInstanceSchedule> getModifiedInclusions();
  
  public void setModifiedInclusions(List<IEventInstanceSchedule> modifiedInclusions);
  
  public List<IEventInstanceSchedule> getModifiedExclusions();
  
  public void setModifiedExclusions(List<IEventInstanceSchedule> modifiedExclusions);
  
  public List<ILinkedEntities> getAddedLinkedEntities();
  
  public void setAddedLinkedEntities(List<ILinkedEntities> addedLinkedEntities);
  
  public List<String> getDeletedLinkedEntities();
  
  public void setDeletedLinkedEntities(List<String> deletedLinkedEntities);
  
  public List<ILinkedEntities> getModifiedLinkedEntities();
  
  public void setModifiedLinkedEntities(List<ILinkedEntities> modifiedLinkedEntities);
  
  public List<ITagInstance> getAddedTags();
  
  public void setAddedTags(List<ITagInstance> addedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<IModifiedContentTagInstanceModel> getModifiedTags();
  
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags);
  
  public List<IComment> getAddedComments();
  
  public void setAddedComments(List<IComment> addedComments);
  
  public List<String> getDeletedComments();
  
  public void setDeletedComments(List<String> deletedComments);
  
  public List<IComment> getModifiedComments();
  
  public void setModifiedComments(List<IComment> modifiedComments);
  
  public List<ITaskInstance> getAddedSubtasks();
  
  public void setAddedSubtasks(List<ITaskInstance> addedSubtasks);
  
  public List<String> getDeletedSubtasks();
  
  public void setDeletedSubtasks(List<String> deletedSubtasks);
  
  public List<ISaveTaskInstanceModel> getModifiedSubtasks();
  
  public void setModifiedSubtasks(List<ISaveTaskInstanceModel> modifiedSubtasks);
  
  public List<String> getAddedAttachments();
  
  public void setAddedAttachments(List<String> addedAttachments);
  
  public List<String> getDeletedAttachments();
  
  public void setDeletedAttachments(List<String> deletedAttachments);
  
  public List<IRoleInstance> getAddedRoles();
  
  public void setAddedRoles(List<IRoleInstance> addedRoles);
  
  public List<String> getDeletedRoles();
  
  public void setDeletedRoles(List<String> deletedRoles);
  
  public List<IModifiedRoleInstanceModel> getModifiedRoles();
  
  public void setModifiedRoles(List<IModifiedRoleInstanceModel> modifiedRoles);
  
  public String getStatusTagId();
  
  public void setStatusTagId(String statusTagId);
  
  public String getPriorityTagId();
  
  public void setPriorityTagId(String priorityTagId);
  
  public ITaskRoleSaveEntity getResponsible();
  
  public void setResponsible(ITaskRoleSaveEntity responsible);
  
  public ITaskRoleSaveEntity getAccountable();
  
  public void setAccountable(ITaskRoleSaveEntity accountable);
  
  public ITaskRoleSaveEntity getConsulted();
  
  public void setConsulted(ITaskRoleSaveEntity consulted);
  
  public ITaskRoleSaveEntity getInformed();
  
  public void setInformed(ITaskRoleSaveEntity informed);
  
  public ITaskRoleSaveEntity getVerify();
  
  public void setVerify(ITaskRoleSaveEntity verify);
  
  public ITaskRoleSaveEntity getSignoff();
  
  public void setSignoff(ITaskRoleSaveEntity signoff);
}
