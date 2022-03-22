package com.cs.loadbalancer.inds.task.base;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.loadbalancer.inds.scheduler.IINDSScheduler;
import com.cs.loadbalancer.inds.task.INDSConfigurationTask;
import com.cs.loadbalancer.inds.task.INDSPingAndAddLoadBalancerTask;
import com.cs.loadbalancer.inds.task.INDSPingTask;
import com.cs.loadbalancer.inds.task.INDSPreviewTask;
import com.cs.loadbalancer.inds.task.INDSProcessingTask;
import com.cs.loadbalancer.inds.utils.INDSConstants;
import com.cs.loadbalancer.inds.utils.INDSLoadBalancerUtils;
import com.cs.runtime.interactor.model.indsserver.GeneratePdfFromIndesignFileModel;
import com.cs.runtime.interactor.model.indsserver.IINDSTaskRequestModel;
import com.cs.runtime.interactor.model.indsserver.INDSConfigurationTaskRequestModel;
import com.cs.runtime.interactor.model.indsserver.INDSPingTaskRequestModel;
import com.cs.runtime.interactor.model.indsserver.INDSProcessingTaskRequestModel;

/**
 * An implementation of the factory method. Returns instances of different task classes based on the task request type specified in the HTTP request headers. 
 */
public class TaskFactory {

	public static IINDSTask getTaskByRequestType(AsyncContext asyncContext, IINDSScheduler scheduler, HttpServletResponse response) throws Exception {
		HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
		String taskRequestType = request.getHeader(INDSConstants.TASK_REQUEST_TYPE);
		String requestContent = INDSLoadBalancerUtils.getContentInStringFromRequest(request);
		IINDSTaskRequestModel indsTaskRequestModel = null;
		IINDSTask task = null;

		switch (taskRequestType) {

		case INDSConstants.INDS_PROCESSING_REQUEST:
			indsTaskRequestModel =  ObjectMapperUtil.readValue(requestContent, INDSProcessingTaskRequestModel.class);
			task = new INDSProcessingTask(asyncContext, scheduler, indsTaskRequestModel, response);
			break;

		case INDSConstants.INDS_CONFIGURATION_REQUEST:
			indsTaskRequestModel =  ObjectMapperUtil.readValue(requestContent, INDSConfigurationTaskRequestModel.class);
			task = new INDSConfigurationTask(asyncContext, scheduler, indsTaskRequestModel);
			break;

		case INDSConstants.INDS_PING_REQUEST:
			indsTaskRequestModel =  ObjectMapperUtil.readValue(requestContent, INDSPingTaskRequestModel.class);
			task = new INDSPingTask(asyncContext, scheduler, indsTaskRequestModel);
			break;
			
		case INDSConstants.INDS_PING_AND_ADD_REQUEST:
		  indsTaskRequestModel =  ObjectMapperUtil.readValue(requestContent, INDSPingTaskRequestModel.class);
		  task = new INDSPingAndAddLoadBalancerTask(asyncContext, scheduler, indsTaskRequestModel);
		  break;
      
		case INDSConstants.GENERATE_PDF_OF_INDESIGN:
      indsTaskRequestModel = ObjectMapperUtil.readValue(requestContent, GeneratePdfFromIndesignFileModel.class);
      task = new INDSPreviewTask(asyncContext, scheduler, indsTaskRequestModel);
      break;
		}

		return task;
	}
	
}