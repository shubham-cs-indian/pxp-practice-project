package com.cs.core.bgprocess.relationship;

import com.cs.core.bgprocess.dto.BGPRelationshipContextRemoveDTO;
import com.cs.core.bgprocess.idao.IBGPRelationshipDAO;
import com.cs.core.bgprocess.idto.IBGPRelationshipContextRemoveDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class SaveRelationshipHandler extends AbstractBGProcessJob implements IBGProcessJob {
  
  private int                      currentBatchNo               = 0;
  private int                      nbBatches                    = 1;
  
  IBGPRelationshipContextRemoveDTO relationshipContextRemoveDTO = new BGPRelationshipContextRemoveDTO();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    relationshipContextRemoveDTO.fromJSON(jobData.getEntryData().toString());
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception
  {
    deleteRelationshipContext();
    
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
  
  private void deleteRelationshipContext() throws Exception
  {
    Long relationshipPropertyId = relationshipContextRemoveDTO.getRelationshipPropertyId();
    String removedSide1ContextId = relationshipContextRemoveDTO.getRemovedSide1ContextId();
    String removedSide2ContextId = relationshipContextRemoveDTO.getRemovedSide2ContextId();
    IBGPRelationshipDAO bgpRelationshipDAO = RDBMSUtils.newUserSessionDAO().newBGPRelationshipDAO();
    bgpRelationshipDAO.deleteContextFromRelationship(relationshipPropertyId, removedSide1ContextId, removedSide2ContextId);
  }
}
