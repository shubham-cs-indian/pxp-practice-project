package com.cs.core.runtime.interactor.usecase.klassinstance;

import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.entity.user.User;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.role.AbstractRoleCandidate;
import com.cs.core.runtime.interactor.entity.role.IRoleCandidate;
import com.cs.core.runtime.interactor.model.mail.IMailNotificationModel;
import com.cs.core.runtime.interactor.model.mail.MailNotificationModel;
import com.cs.core.runtime.interactor.usecase.logger.CSLogUtil;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class KlassInstanceNotificationTask implements Runnable {
  
  @Autowired
  String                                                        smtpUsername;
  
  /*@Autowired
  IEmailNotificationStrategy                                    emailNotificationStrategy;*/
  
  /*@Autowired
  CSLogger                                                      logger;*/
  
  protected IListModel<? extends IKlassInstance>                savedKlassInstancesModel;
  
  protected Map<String, Map<String, List<Map<String, Object>>>> klassInstanceRolesMapping;
  
  protected IUser                                               currentUser;
  
  protected String                                              requestId;
  
  protected String                                              sessionId;
  
  public void setData(IListModel<? extends IKlassInstance> savedKlassInstancesModel,
      Map<String, Map<String, List<Map<String, Object>>>> klassInstanceRolesMapping,
      IUser currentUser, String requestId, String sessionId)
  {
    this.savedKlassInstancesModel = savedKlassInstancesModel;
    this.klassInstanceRolesMapping = klassInstanceRolesMapping;
    this.currentUser = currentUser;
    this.requestId = requestId;
    this.sessionId = sessionId;
  }
  
  @Override
  public void run()
  {
    try {
      for (IKlassInstance klassInstanceModel : savedKlassInstancesModel.getList()) {
        Map<IRoleInstance, List<Map<String, Object>>> roles = new HashMap<>();
        Map<String, List<Map<String, Object>>> roleToFieldMapping = klassInstanceRolesMapping
            .get(klassInstanceModel.getId());
        List<String> roleIds = new ArrayList<>(roleToFieldMapping.keySet());
        for (IRoleInstance roleInstance : klassInstanceModel.getRoles()) {
          if (roleIds.contains(roleInstance.getRoleId())) {
            roles.put(roleInstance, roleToFieldMapping.get(roleInstance.getRoleId()));
          }
        }
        
        // The below code is not useful since owner comes into roleInstances.
        /*if (roleIds.contains(mandatoryData.get("ownerId"))) {
          IRoleInstance roleInstanceForOwner = new RoleInstance();
        
          IIdParameterModel idModel = new IdParameterModel(klassInstanceModel.getOwner());
          IUserModel ownerUser = neo4jGetUserStrategy.execute(idModel);
        
          List<AbstractRoleCandidate> candidates = new ArrayList<>();
          candidates.add((AbstractRoleCandidate) ownerUser.getEntity());
          roleInstanceForOwner.setCandidates(candidates);
          roles.put(roleInstanceForOwner, roleToFieldMapping.get(mandatoryData.get("ownerId")));
        }*/
        
        // sendNotification function
        sendNotificationsToUsersAndUserGroupsInRoles(klassInstanceModel.getName(), roles,
            currentUser);
      }
      
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      String errorStackTrace = CSLogUtil.getStackTrace(e);
      /*if (errorStackTrace != null) {
        logger.error("Thread_" + this.getClass().getSimpleName(), "run", e.getMessage(),
            errorStackTrace);
      }
      else {
        logger.error("Thread_" + this.getClass().getSimpleName(), "run", e.getMessage(), null);
      }*/
    }
  }
  
  private void sendNotificationsToUsersAndUserGroupsInRoles(String projectLabel,
      Map<? extends IRoleInstance, List<Map<String, Object>>> roles, IUser currentUser)
      throws Exception
  {
    String sender = currentUser.getEmail();
    if (sender == null || sender.isEmpty()) {
      sender = smtpUsername;
    }
    
    for (IRoleInstance roleInstance : roles.keySet()) {
      Set<IRoleCandidate> candidates = new HashSet<>(roleInstance.getCandidates());
      List<Map<String, Object>> modifiedFields = roles.get(roleInstance);
      transformModificationsData(modifiedFields);
      String message = "";
      
      for (IRoleCandidate roleCandidate : candidates) {
        IMailNotificationModel mailModel = new MailNotificationModel();
        
        String recipient = ((AbstractRoleCandidate) roleCandidate).getEmail();
        
        if (recipient == null || recipient.isEmpty()) {
          recipient = smtpUsername;
        }
        
        String recipientFirstName = ((User) roleCandidate).getFirstName();
        HashMap<String, Object> scope = new HashMap<String, Object>();
        scope.put("name", projectLabel);
        
        scope.put("reporter", currentUser.getLabel());
        scope.put("recipientFirstName", recipientFirstName);
        scope.put("updates", modifiedFields);
        StringWriter writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("updateKlassTemplate.txt");
        mustache.execute(writer, scope)
            .flush();
        message = writer.toString();
        
        mailModel.setSender(sender);
        mailModel.addRecipient(recipient);
        mailModel.setSubject("Attribute Updated");
        mailModel.setBody(message);
        
        /*emailNotificationStrategy.execute(mailModel);*/
      }
    }
  }
  
  private void transformModificationsData(List<Map<String, Object>> modifiedFields) throws Exception
  {
    for (Map<String, Object> map : modifiedFields) {
      if (map.get("value") == null || map.get("value")
          .equals("")) {
        map.put("value", "\"\"");
      }
      if (map.get("oldValue") == null || map.get("oldValue")
          .equals("")) {
        map.put("oldValue", "\"\"");
      }
      
      if (map.get("value") instanceof String) {
        continue;
        
      }
      else if (map.get("value") instanceof Map) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<br>");
        Map<String, List<String>> valueMap = (Map<String, List<String>>) map.get("value");
        List<String> oldList = (List<String>) map.get("oldValue");
        List<String> newValue = new ArrayList<String>();
        String oldData = null;
        
        // set old value list.
        if (oldList.size() > 0) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("<ul>");
          for (String item : oldList) {
            stringBuilder.append("<li>" + item + "</li>");
          }
          stringBuilder.append("</ul>");
          oldData = stringBuilder.toString();
        }
        else {
          oldData = "\"Empty List\"";
        }
        //
        
        if (valueMap.get("modified") != null) {
          List<String> modifiedList = (List<String>) valueMap.get("modified");
          for (String modifiedItem : modifiedList) {
            newValue.add(modifiedItem);
            String tagName = modifiedItem.split(":")[0];
            for (String oldItem : oldList) {
              if (oldItem.startsWith(tagName)) {
                oldList.remove(oldItem);
                oldList.add(modifiedItem);
                break;
              }
            }
          }
        }
        
        if (valueMap.get("removed") != null) {
          List<String> removedList = (List<String>) valueMap.get("removed");
          oldList.removeAll(removedList);
        }
        
        if (valueMap.get("added") != null) {
          List<String> addedList = (List<String>) valueMap.get("added");
          oldList.addAll(addedList);
          newValue.addAll(addedList);
        }
        
        // set new value list
        /*if (oldList.size() > 0) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("<ul>");
          for (String item : oldList) {
            stringBuilder.append("<li>" + item + "</li>");
          }
          stringBuilder.append("</ul>");
          map.put("value", stringBuilder.toString());
        }
        else {
          map.put("value", "\"Empty List\"");
        }*/
        //
        // set new value list
        if (newValue.size() > 0) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("<ul>");
          for (String item : newValue) {
            stringBuilder.append("<li>" + item + "</li>");
          }
          stringBuilder.append("</ul>");
          map.put("value", stringBuilder.toString());
        }
        else {
          map.put("value", "\"Empty List\"");
        }
        //
        
        map.put("oldValue", oldData);
      }
    }
  }
}
