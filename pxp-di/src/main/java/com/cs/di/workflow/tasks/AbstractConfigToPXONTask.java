package com.cs.di.workflow.tasks;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

public abstract class AbstractConfigToPXONTask extends AbstractTransformationTask {
  
  public static final String        RECEIVED_DATA       = "RECEIVED_DATA";
  public static final String        PXON                = "PXON";
  public static final String        FAILED_FILES        = "FAILED_FILES";
  public static final String        NODE_TYPE           = "NODE_TYPE";
  public static final String        PERMISSIONS_TYPE    = "PERMISSIONS_TYPE";
  public static final String        ROLE_IDS            = "ROLE_IDS";
  volatile String                   defaultLanguageCode = null;
   
  protected abstract void generatePXON(WorkflowTaskModel model);
  
  @Override
  public void transform(WorkflowTaskModel model)
  {
    defaultLanguageCode = model.getWorkflowModel().getTransactionData().getDataLanguage();
    generatePXON(model);
  }
  
  /**
   * Write entities to PXON file
   * 
   * @param entities
   * @param executionStatusTable
   * @return
   */
  protected String writePXONToFile(List<String> entities, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    String filePath = null;
    String filename = "inboundPXON_" + System.currentTimeMillis() + ".pxon";
    if (AbstractBGPTask.BGP_SHARED_DIRECTORY == null) {
      executionStatusTable.addError(MessageCode.GEN019);
    }
    else {
      filePath = AbstractBGPTask.BGP_SHARED_DIRECTORY + filename;
    }
    
    FileWriter myWriter = null;
    try {
      myWriter = new FileWriter(filePath);
      myWriter.write('[');
      int entityCount = 0;
      for (; entityCount < entities.size() - 1; entityCount++) {
        myWriter.write(entities.get(entityCount) + ',');
      }
      myWriter.write(entities.get(entityCount) + ']');
    }
    catch (IOException e) {
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN018, new String[] { filePath });
    }
    finally {
      if (myWriter != null) {
        try {
          myWriter.close();
        }
        catch (IOException e) {
          RDBMSLogger.instance().exception(e);
        }
      }
    }
    
    return filename;
  }
  
}
