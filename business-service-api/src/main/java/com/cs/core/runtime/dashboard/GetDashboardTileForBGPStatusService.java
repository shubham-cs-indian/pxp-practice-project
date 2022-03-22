package com.cs.core.runtime.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idao.IBGPCursor;
import com.cs.core.bgprocess.idao.IBGPCursor.Characteristic;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.strategy.usecase.user.IGetAllUsersStrategy;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.runtime.interactor.model.dashboard.DashboardBGPStatusResponseModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardBGPStatusRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardBGPStatusResponseModel;
import com.cs.core.runtime.interactor.model.dashboard.IJobDataModel;
import com.cs.core.runtime.interactor.model.dashboard.JobDataModel;
import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;

@Service
public class GetDashboardTileForBGPStatusService extends AbstractRuntimeService<IDashboardBGPStatusRequestModel, IDashboardBGPStatusResponseModel>
    implements IGetDashboardTileForBGPStatusService {
  
  @Autowired
  IGetAllUsersStrategy getAllUsersStrategy;
  
  @Override
  protected IDashboardBGPStatusResponseModel executeInternal(IDashboardBGPStatusRequestModel requestData)
      throws Exception
  {
    IBGPCursor allCursor = BGPDriverDAO.instance().getBGProcesses();
    
    List<IBGProcessDTO> jobsList = getFilteredJobData(requestData, allCursor);
  
    IDashboardBGPStatusResponseModel response = new DashboardBGPStatusResponseModel();
    response.setFrom(requestData.getFrom());
    response.setTotalCount(allCursor.getCount());
    
    Map<String,String> usersMap = new HashMap<>();
    List<IJobDataModel> bgpJobs = new ArrayList<>();
    
    IListModel<IUserInformationModel> listModel = getAllUsersStrategy.execute(new UserModel());
    Collection<? extends IUserInformationModel> list = listModel.getList();
    Map<Long, String> fullNameMap = new HashMap<>();
    
    for (IUserInformationModel iUserInformationModel : list) {
      fullNameMap.put(iUserInformationModel.getUserIID(), iUserInformationModel.getFirstName() + " " + iUserInformationModel.getLastName());
    }
    
    for (IBGProcessDTO jobDTO : jobsList) {
      IJobDataModel job = new JobDataModel();
      job.setJobId(jobDTO.getJobIID());
      job.setStatus(jobDTO.getStatus().toString());
      job.setCreated(jobDTO.getCreatedTime());
      job.setProgress(jobDTO.getProgress().getPercentageCompletion());
      IUserDTO user = jobDTO.getUser();
      job.setCreatedBy(user.getUserIID());
      job.setService(jobDTO.getService());
     
      // TODO :here giving the code of user to UI need to fix the BGP cursor query.
      usersMap.put(String.valueOf(user.getUserIID()), fullNameMap.get(user.getUserIID())); 
      bgpJobs.add(job);
    }
    response.setBgpJobs(bgpJobs);
    response.setUsersMap(usersMap);
    
    return response;
  }

  private List<IBGProcessDTO> getFilteredJobData(IDashboardBGPStatusRequestModel requestData,
      IBGPCursor allCursor) throws RDBMSException
  {
    String jobStatus = requestData.getJobStatus();
    if (jobStatus != null && !jobStatus.isEmpty()) {
      BGPStatus status = BGPStatus.valueOf(jobStatus);
      allCursor.setStatusFilter(status);
    }
    
    SortModel sortOptions = requestData.getSortOptions();
    if (sortOptions != null) {
      String sortOrder = sortOptions.getSortOrder().toUpperCase();// doing upper case because getting lower case from UI
      String sortField = sortOptions.getSortField();
      allCursor.setOrdering(OrderDirection.valueOf(sortOrder), Characteristic.valueOf(sortField));
    }
    
    String bgpService = requestData.getBgpService();
    if (bgpService != null && !bgpService.isEmpty()) {
      allCursor.setServiceFilter(bgpService);
    }
    
    Set<String> userNames = requestData.getUserNames();
    if (userNames != null && !userNames.isEmpty()) {
      allCursor.setUserNameFilter(userNames);
    }
    List<IBGProcessDTO> jobsList = allCursor.getNext(requestData.getFrom(), requestData.getSize());
    return jobsList;
  }
  
}
