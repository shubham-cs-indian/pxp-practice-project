package com.cs.di.jms;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.dataintegration.IAcknowledgementBodyModel;
import com.cs.core.runtime.interactor.model.dataintegration.IJMSConfigModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JMSAckListenerService extends AbstractJMSService implements IJMSAckListenerService {

  @Autowired
  RuntimeService runtimeService;
  
    @Override
    public void replaceJMSListeners(Set<IJMSConfigModel> oldJMSConfigList, boolean oldIsActive, Set<IJMSConfigModel> newJMSConfigList, boolean newIsActive, String processId) {
        Set<IJMSConfigModel> addedJMSConfigList = substractList(newJMSConfigList, oldJMSConfigList);
        Set<IJMSConfigModel> removedJMSConfigList = substractList(oldJMSConfigList, newJMSConfigList);
        Set<IJMSConfigModel> unchangedJMSConfigList = unchangedList(oldJMSConfigList, newJMSConfigList);

        removeJMSListeners(removedJMSConfigList, processId);
        addJMSListeners(addedJMSConfigList, processId, newIsActive);

        if(oldIsActive!=newIsActive && unchangedJMSConfigList.size()>0) {
            unchangedJMSConfigList.forEach(jmsConfig->{
                if(newIsActive)
                    tryActivating(jmsConfig, processId);
                else
                    tryDeactivating(jmsConfig, processId);
            });
        }
    }

    private void addJMSListeners(Set<IJMSConfigModel> jmsConfigList, String processId, boolean newIsActive) {
        jmsConfigList.forEach(jmsConfig->addJMSListener(jmsConfig, processId, newIsActive));
    }

    private void removeJMSListeners(Set<IJMSConfigModel> jmsConfigList, String processId) {
        jmsConfigList.forEach(jmsConfig->removeJMSListener(jmsConfig, processId));
    }

    @Override
    protected MessageListener createJMSListener(IJMSConfigModel jmsConfigModel) {
        return (message)->{
            try {
                RDBMSLogger.instance().info("Acknowledgement Received: Correlation Started");

                Map<String, Object> workflowParameters = new HashMap<>();
                HashMap<String, Object> body = new ObjectMapper().readValue(((TextMessage) message).getText(), HashMap.class);
                String status = body.get(IAcknowledgementBodyModel.STATUS).toString();
                String correlationId = (String) body.get(IAcknowledgementBodyModel.CORRELATION_ID);
                if (status.equalsIgnoreCase("success")) {
                    workflowParameters.put("isAcknowledged", true);
                    workflowParameters.put("CORRELATION_ID", correlationId);
                }
                else if (status.equalsIgnoreCase("failure")) {
                    workflowParameters.put("isAcknowledged", false);
                    workflowParameters.put("CORRELATION_ID", correlationId);
                }
                else
                    return;

                bpmnService.createMessageCorrelation(correlationId, workflowParameters);

                RDBMSLogger.instance().info("Acknowledgement Received: Correlation Completed");
            }  catch (Exception e) {
                RDBMSLogger.instance().exception(e);
            }
        };
    }

    private Set<IJMSConfigModel> substractList(Set<IJMSConfigModel> jmsConfigList1, Set<IJMSConfigModel> jmsConfigList2) {
        if(jmsConfigList1==null)
            return new HashSet<>();
        else if(jmsConfigList2==null)
            return jmsConfigList1;

        return jmsConfigList1
                .stream()
                .filter(jmsConfig->!jmsConfigList2.contains(jmsConfig))
                .collect(Collectors.toSet());
    }


    private Set<IJMSConfigModel> unchangedList(Set<IJMSConfigModel> jmsConfigList1, Set<IJMSConfigModel> jmsConfigList2) {
        if(jmsConfigList1==null || jmsConfigList2==null)
            return new HashSet<>();

        return jmsConfigList1
                .stream()
                .filter(jmsConfig->jmsConfigList2.contains(jmsConfig))
                .collect(Collectors.toSet());
    }
}
