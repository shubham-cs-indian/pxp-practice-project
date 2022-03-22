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
import com.cs.runtime.interactor.model.indsserver.IINDSConfigurationTaskRequestModel;
import com.cs.runtime.interactor.model.indsserver.IINDSPingTaskResponseModel;
import com.cs.runtime.interactor.model.indsserver.IINDSTaskRequestModel;
import com.cs.runtime.interactor.model.indsserver.INDSPingTaskResponseModel;

/**
 * The configuration task class. It adds or removes the indesign server instances that are available with the scheduler.
 * Also, a ping request is sent before adding each INDS instance. See 'performTask' method.
 */
public class INDSConfigurationTask extends BaseINDSTask implements IINDSTask {

	public INDSConfigurationTask(AsyncContext asyncContext,
			IINDSScheduler scheduler,
			IINDSTaskRequestModel indsTaskRequestModel) {
		super(asyncContext, scheduler, indsTaskRequestModel);
		this.isConfigurationTask = true; //'isConfigurationTask' is set to 'true' since it's a configuration task.
	}

	public void performTask() throws Exception {
		IINDSConfigurationTaskRequestModel configurationTaskReqModel = (IINDSConfigurationTaskRequestModel) indsTaskRequestModel;
		List<IInDesignServerInstance> newInDesignServerInstances = configurationTaskReqModel.getServersToAdd();
		List<IInDesignServerInstance> removeInDesignServerInstances = configurationTaskReqModel.getServersToRemove();
		List<IInDesignServerInstance> updateInDesignServerInstances = configurationTaskReqModel.getServersToUpdate();
		List<IInDesignServerInstance> allInDesignServerInstances = configurationTaskReqModel.getAllServers();
    // Add new InDesign servers
    if (!newInDesignServerInstances.isEmpty()) {
      SOAPUtils.pingINDSInstances(newInDesignServerInstances);
      scheduler.updateINDSStatuses(newInDesignServerInstances);
      scheduler.addInDesignServerInstances(newInDesignServerInstances);
    }
    // Remove InDesign servers.
    if (!removeInDesignServerInstances.isEmpty()) {
      scheduler.removeInDesignServerInstances(removeInDesignServerInstances);
    }
    // Below code is added to handle a scenario, if a user updates the existing
    // server configuration and saves it.
    if (!updateInDesignServerInstances.isEmpty()) {
      SOAPUtils.pingINDSInstances(updateInDesignServerInstances);
      scheduler.removeInDesignServerInstances(updateInDesignServerInstances);
      scheduler.addInDesignServerInstances(updateInDesignServerInstances);
    }
    SOAPUtils.pingINDSInstances(allInDesignServerInstances);
		
		IINDSPingTaskResponseModel responseModel = new INDSPingTaskResponseModel();
		responseModel.setPingedServers(allInDesignServerInstances);

		ServletResponse response = asyncContext.getResponse();
		response.getWriter().println(ObjectMapperUtil.writeValueAsString(responseModel));
	}

}