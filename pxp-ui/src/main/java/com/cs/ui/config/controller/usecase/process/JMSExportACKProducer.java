package com.cs.ui.config.controller.usecase.process;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.component.ActiveMQConfig;
import com.cs.core.runtime.interactor.model.dataintegration.AcknowledgementPropertiesModel;
import com.cs.core.runtime.interactor.model.dataintegration.IAcknowledgementBodyModel;
import com.cs.core.runtime.interactor.model.dataintegration.IAcknowledgementPropertiesModel;
import com.cs.core.runtime.interactor.model.dataintegration.IJMSExportAcknowledgementStatusModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.naming.NamingException;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/runtime/jmsexportackproducer")
public class JMSExportACKProducer {
  
  @Autowired
  private ActiveMQConfig activeMQConfig;
  
  @Value("${jms.brokerUrl}")
  private String         jmsBrokerUrl;
  
  @RequestMapping(method = RequestMethod.POST)
  public void executeREST(@RequestBody String model) throws Exception
  {
    try {
      HashMap map = ObjectMapperUtil.readValue(model, HashMap.class);
      String acknowledgementQueueName = map.get("ackProducerQueue")
          .toString();
      String transactionId = (String) map.get(IJMSExportAcknowledgementStatusModel.ID_TRANSACTION);
      if (acknowledgementQueueName != null && !acknowledgementQueueName.isEmpty()) {
        IAcknowledgementPropertiesModel ackPropertiesModel = perpareProperties(transactionId);
        
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put(IAcknowledgementBodyModel.STATUS, map.get("ackStatus")
            .toString());
        String ackData = ObjectMapperUtil.writeValueAsString(dataMap);
        sendAcknowledgement(acknowledgementQueueName, ackPropertiesModel, jmsBrokerUrl, ackData);
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  private void sendAcknowledgement(String acknowledgementQueueName,
      IAcknowledgementPropertiesModel ackPropertiesModel, String brokerUrl, String ackData)
      throws JMSException, NamingException
  {
    activeMQConfig.getLock()
        .readLock()
        .lock();
    try {
      activeMQConfig.getTemplate(brokerUrl)
          .convertAndSend(acknowledgementQueueName, ackData,
              new CSMessagePostProcessor(ackPropertiesModel));
    }
    finally {
      activeMQConfig.getLock()
          .readLock()
          .unlock();
    }
  }
  
  private IAcknowledgementPropertiesModel perpareProperties(String id)
  {
    IAcknowledgementPropertiesModel ackPropertiesModel = new AcknowledgementPropertiesModel();
    ackPropertiesModel.setIDtransaction(id);
    return ackPropertiesModel;
  }
}

class CSMessagePostProcessor implements MessagePostProcessor {
  
  private IAcknowledgementPropertiesModel ackPropertiesModel;
  
  public CSMessagePostProcessor(IAcknowledgementPropertiesModel ackPropertiesModel)
  {
    this.ackPropertiesModel = ackPropertiesModel;
  }
  
  @Override
  public Message postProcessMessage(Message message) throws JMSException
  {
    message.setJMSTimestamp(System.currentTimeMillis());
    message.setJMSType("Ack");
    message.setStringProperty(IAcknowledgementPropertiesModel.ID_TRANSACTION,
        ackPropertiesModel.getIDtransaction());
    return message;
  }
}
