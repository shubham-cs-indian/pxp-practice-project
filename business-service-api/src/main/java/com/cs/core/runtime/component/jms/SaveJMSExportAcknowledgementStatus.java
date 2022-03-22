package com.cs.core.runtime.component.jms;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.component.jms.ISaveJMSExportAcknowledgementStatus;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dataintegration.IAcknowledgementBodyModel;
import com.cs.core.runtime.interactor.model.dataintegration.IAcknowledgementModel;
import com.cs.core.runtime.interactor.model.dataintegration.IAcknowledgementPropertiesModel;
import com.cs.core.runtime.interactor.model.dataintegration.IReceiveModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SaveJMSExportAcknowledgementStatus
    implements ISaveJMSExportAcknowledgementStatus {
  
  @Autowired
  RuntimeService  runtimeService;
  
  @Override
  @SuppressWarnings("unchecked")
  public IModel execute(IReceiveModel receiveModel) throws Exception
  {
    
    try {
      ObjectMapper mapper = new ObjectMapper();
      HashMap<String, Object> dataMap = mapper.readValue(receiveModel.getData(), HashMap.class);
      HashMap<String, Object> properties = (HashMap<String, Object>) dataMap
          .get(IAcknowledgementModel.PROPERTIES);
      String idTransaction = (String) properties
          .get(IAcknowledgementPropertiesModel.ID_TRANSACTION);
      String dataMapBodyString = (String) dataMap.get(IAcknowledgementModel.BODY);
      HashMap<String, Object> body = mapper.readValue(dataMapBodyString, HashMap.class);
      String status = body.get(IAcknowledgementBodyModel.STATUS)
          .toString();
      
      // for testing JMS ACK through active mq url
      if(idTransaction==null) {
        idTransaction = (String) ((HashMap<String, Object>) dataMap.get(IAcknowledgementModel.HEADERS)).get("JMSCorrelationID");
        }
      // end
      
      if (status.equalsIgnoreCase("success")) {
        System.out.println("################ Message Throw Event :: success ######################");
        
        List<MessageCorrelationResult> result = runtimeService.createMessageCorrelation(idTransaction)
            .setVariable("isAcknowledged", true)
            .correlateAllWithResult();
        System.out.println(" Message : " + idTransaction + " has been Thrown. Afected instances : " + result.size());
      }
      else if (status.equalsIgnoreCase("failure")) {
        RDBMSLogger.instance().info("################ Message Throw Event :: failure ######################");
       
        runtimeService.createMessageCorrelation(idTransaction)
            .setVariable("isAcknowledged", false)
            .correlateAllWithResult();
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    return null;
  }
}
