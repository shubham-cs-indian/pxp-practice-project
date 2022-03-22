package com.cs.ui.runtime.controller.usecase.workflow.callback;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/bgp")
public class BGPCallbackHandlerController extends BaseController implements IRuntimeController {
  
  @Autowired
  RuntimeService runtimeService;
  
  @RequestMapping(value = "/callback", method = RequestMethod.PUT)
  public void execute(@RequestParam String job, String status, String processInstanceId,
      String service, @RequestBody Map<String, Object> model) throws Exception
  {
    HashMap<String, Object> processVariables = new HashMap<>();
    processVariables.put("jobId", job);
    processVariables.put("status", status);
    if (model != null ) {
      processVariables.put("callBackBodyData", model);
    }

    Map<String, Object> correlationKeys = new HashMap<String, Object>();
    correlationKeys.put("rootProcessInstanceId", processInstanceId);

    // Raise signal to the corresponding Workflow process instance
    runtimeService.correlateMessage(service, correlationKeys, processVariables);
  }
}
