package com.cs.core.bgprocess.services.partner;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.constants.Constants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.BGPDeletePartnersDTO;
import com.cs.core.bgprocess.dto.IBGPDeletePartnersDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.data.Text;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.klassinstance.DeleteInstanceDetails;
import com.cs.core.runtime.interactor.model.klassinstance.DeleteKlassInstancesRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstanceDetails;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstancesRequestModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.DeleteKlassInstances;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class DeletePartnersHandler extends AbstractBGProcessJob implements IBGProcessJob {
  
  IBGPDeletePartnersDTO       deletedPartnersDTO  = new BGPDeletePartnersDTO();
  private int                 currentBatchNo      = 0;
  private int                 nbBatches           = 1;
  RDBMSComponentUtils         rdbmsComponentUtils;
  private static final String Q_GET_BASEENTITYIID = " select b.baseentityiid from pxp.baseentity b where b.baseentityid in ( %s ) and b.organizationcode = 'stdo'";
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    deletedPartnersDTO.fromJSON(jobData.getEntryData().toString());
    rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception
  {
    deletePartners(deletedPartnersDTO.getDeletedPartnersList());
    
    setCurrentBatchNo(++currentBatchNo);
    
    IBGProcessDTO.BGPStatus status = null;
    
    jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    
    if (jobData.getProgress().getPercentageCompletion() == 100) {
      status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
    }
    else {
      status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    }
    return status;
  }
  
  private void deletePartners(List<String> deletedPartnersList) throws Exception
  {
    List<String> deletedPartnersIidList = new ArrayList<>();
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuffer query = new StringBuffer();
      query.append(String.format(Q_GET_BASEENTITYIID, Text.join(",", deletedPartnersList, "'%s'")));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while (resultSet.next()) {
        deletedPartnersIidList.add(resultSet.getString("baseentityiid"));
      }
    });
    
    IDeleteKlassInstancesRequestModel deleteKlassInstancesRequestModel = new DeleteKlassInstancesRequestModel();
    List<IDeleteInstanceDetails> allDeleteInstanceDetails = new ArrayList<>();
    
    for (String id : deletedPartnersIidList) {
      IDeleteInstanceDetails deleteInstanceDetails = new DeleteInstanceDetails();
      deleteInstanceDetails.setId(id);
      deleteInstanceDetails.setBaseType(Constants.SUPPLIER_INSTANCE_BASE_TYPE);
      allDeleteInstanceDetails.add(deleteInstanceDetails);
    }
    
    deleteKlassInstancesRequestModel.setAllDeleteInstanceDetails(allDeleteInstanceDetails);
    deleteKlassInstancesRequestModel.setHasDeletePermission(true);
    deleteKlassInstancesRequestModel.setIsDeleteFromArchive(false);
    
    DeleteKlassInstances deleteKlassInstances = BGProcessApplication.getApplicationContext().getBean(DeleteKlassInstances.class);
    deleteKlassInstances.execute(deleteKlassInstancesRequestModel);
  }
  
}
