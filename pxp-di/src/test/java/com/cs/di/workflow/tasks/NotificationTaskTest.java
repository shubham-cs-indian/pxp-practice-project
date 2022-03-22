package com.cs.di.workflow.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.di.common.test.DiTestUtil;
import com.cs.di.common.test.DiMockitoTestConfig;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.WorkflowTaskModel;

@SuppressWarnings("unchecked")
public class NotificationTaskTest extends DiMockitoTestConfig {
  
  @InjectMocks
  @Autowired
  NotificationTask notifictaiontask;
  
  @Test
  public void nullInputTest()
  {
    List<String> notify = new ArrayList<String>();
    notify.add("user");
    WorkflowTaskModel model = DiTestUtil.createTaskModel("notifictaiontask",
        new String[] { notifictaiontask.NOTIFICATION_BODY, notifictaiontask.NOTIFICATION_SUBJECT, notifictaiontask.NOTIFICATION },
        new Object[] { null, "asc", notify });
    notifictaiontask.executeTask(model);
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter NOTIFICATION_BODY is null."));
  }
  
  @Test
  public void emptyInputTest()
  {
    List<String> notify = new ArrayList<String>();
    notify.add("user");
    WorkflowTaskModel model = DiTestUtil.createTaskModel("notifictaiontask",
        new String[] { notifictaiontask.NOTIFICATION_BODY, notifictaiontask.NOTIFICATION_SUBJECT, notifictaiontask.NOTIFICATION },
        new Object[] { "", "asc", notify });
    notifictaiontask.executeTask(model);
    assertTrue(DiTestUtil.verifyExecutionStatus(model, MessageType.ERROR, "Input parameter NOTIFICATION_BODY is empty."));
  }
  
  @Test
  public void validInputTest()
  {
    List<String> notify = new ArrayList<String>();
    notify.add("user");
    WorkflowTaskModel model = DiTestUtil.createTaskModel("notifictaiontask",
        new String[] { notifictaiontask.NOTIFICATION_BODY, notifictaiontask.NOTIFICATION_SUBJECT, notifictaiontask.NOTIFICATION },
        new Object[] { "body", "asc", notify });
    notifictaiontask.executeTask(model);
    DiTestUtil.verifyExecutionStatus2(model.getExecutionStatusTable(), MessageType.ERROR, "Input parameter NOTIFICATION_BODY is empty.");
    assertFalse(model.getExecutionStatusTable().isErrorOccurred());
    
  }
}
