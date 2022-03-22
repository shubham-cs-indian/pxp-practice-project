package com.cs.loadbalancer.inds.task;

import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.loadbalancer.inds.scheduler.IINDSScheduler;
import com.cs.loadbalancer.inds.task.base.BaseINDSTask;
import com.cs.loadbalancer.inds.task.base.IINDSTask;
import com.cs.loadbalancer.inds.utils.SOAPUtils;
import com.cs.runtime.interactor.model.indsserver.IINDSPingTaskRequestModel;
import com.cs.runtime.interactor.model.indsserver.IINDSPingTaskResponseModel;
import com.cs.runtime.interactor.model.indsserver.IINDSTaskRequestModel;
import com.cs.runtime.interactor.model.indsserver.INDSPingTaskResponseModel;

/**
 * The ping task class. It pings the indesign server instances & updates their statuses. See 'performTask' method.
 */
public class INDSPingTask extends BaseINDSTask implements IINDSTask {

	public INDSPingTask(AsyncContext asyncContext, IINDSScheduler scheduler,
			IINDSTaskRequestModel indsTaskRequestModel) {
		super(asyncContext, scheduler, indsTaskRequestModel);
		this.isConfigurationTask = true; //'isConfigurationTask' is true since ping task is a configuration task
		this.isPriorityTask = true; //'isPriorityTask' is true since ping task is also a priority task
	}

	public void performTask() throws Exception {
		IINDSPingTaskRequestModel pingTaskReqModel = (IINDSPingTaskRequestModel) indsTaskRequestModel;
		List<IInDesignServerInstance> inDesignServerInstances = pingTaskReqModel.getServersToPing(); 
		
		SOAPUtils.pingINDSInstances(inDesignServerInstances);
		scheduler.updateINDSStatuses(inDesignServerInstances);
		
		IINDSPingTaskResponseModel responseModel = new INDSPingTaskResponseModel();
		responseModel.setPingedServers(inDesignServerInstances);
		
		ServletResponse response = asyncContext.getResponse();
		response.getWriter().println(ObjectMapperUtil.writeValueAsString(responseModel));
	}

}