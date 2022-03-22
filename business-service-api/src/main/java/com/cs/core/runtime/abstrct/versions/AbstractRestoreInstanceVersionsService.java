package com.cs.core.runtime.abstrct.versions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.templating.IGetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.runtime.interactor.exception.versions.MaxVersionCountViolateException;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.model.version.MoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetNumberOfVersionsToMaintainStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Component
public class AbstractRestoreInstanceVersionsService<P extends IMoveKlassInstanceVersionsModel, R extends IMoveKlassInstanceVersionsSuccessModel>
extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected IGetNumberOfVersionsToMaintainStrategy getNumberOfVersionsToMaintainStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                   rdbmsComponentUtils;
  
  @Override
  protected R executeInternal(P dataModel) throws Exception
  {
    Long entityIID = Long.valueOf(dataModel.getInstanceId());
    List<Integer> versionNumbers = dataModel.getVersionNumbers();
   
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(entityIID);
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    Integer maxVersionAllowed = getMaxVersionCount(baseEntityDTO);
    
    IRevisionDAO revisionDAO = rdbmsComponentUtils.getRevisionDAO();
    Integer existingNumberOfVersions = revisionDAO.getNumberOfVersionsInTimeline(entityIID);
    int versionsToRestoreCount = versionNumbers.size();
    if (maxVersionAllowed < (existingNumberOfVersions + versionsToRestoreCount)) {
      throw new MaxVersionCountViolateException();
    }
    else {
      revisionDAO.restoreRevisions(entityIID, versionNumbers);
    }
    IMoveKlassInstanceVersionsSuccessModel successModel = new MoveKlassInstanceVersionsSuccessModel();
    successModel.setVersionNumbers(versionNumbers);
    return (R) successModel;
  }
  
  protected Integer getMaxVersionCount(IBaseEntityDTO baseEntityDTO) throws Exception
  {
    List<String> classifiers = new ArrayList<>();
    classifiers.add(baseEntityDTO.getNatureClassifier().getClassifierCode());
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds(classifiers);
    IGetNumberOfVersionsToMaintainResponseModel response = getNumberOfVersionsToMaintainStrategy.execute(multiclassificationRequestModel);
    return response.getNumberOfVersionsToMaintain();
  }
}

