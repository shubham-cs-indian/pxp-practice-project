package com.cs.core.config.propertycollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.PropagatePCOnPropertyDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IPropagatePCOnPropertyDeleteDTO;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionResponseModel;
import com.cs.core.config.strategy.usecase.propertycollection.ISavePropertyCollectionStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class SavePropertyCollectionService extends AbstractSaveConfigService<ISavePropertyCollectionModel, IGetPropertyCollectionModel>
    implements ISavePropertyCollectionService {
  
  private static final String               PROPAGATE_ON_PROPERTY_ADD_OR_DELETE = "PROPAGATE_ON_PROPERTY_ADD_OR_DELETE";
  
  private static final BGPPriority          BGP_PRIORITY                        = BGPPriority.LOW;
  
  @Autowired
  protected ISavePropertyCollectionStrategy savePropertyCollectionStrategy;
  
  @Override
  public IGetPropertyCollectionModel executeInternal(ISavePropertyCollectionModel model) throws Exception
  {
    PropertyCollectionValidations.validateSavePropertyCollection(model);
    ISavePropertyCollectionResponseModel saveRespose = savePropertyCollectionStrategy.execute(model);
    
    propagateOnModificationOfPC(saveRespose);
    
    IGetPropertyCollectionModel responseModel = saveRespose.getPropertyCollectionGetRespose();
    responseModel.setAuditLogInfo(saveRespose.getAuditLogInfo());
    
    return responseModel;
  }
  
  public void propagateOnModificationOfPC(ISavePropertyCollectionResponseModel saveRespose) throws RDBMSException, CSFormatException
  {
    Map<String, List<String>> deletedPropertiesFromSource = saveRespose.getDeletedPropertiesFromSource();
    List<IDefaultValueChangeModel> defaultValuesDiffList = saveRespose.getDefaultValuesDiff();
    
    List<String> deletedPropertyCodes = new ArrayList<String>();
    Set<String> classifierCodes = new HashSet<String>();
    Set<String> addedPropertyCodes = new HashSet<String>();
    
    if (!deletedPropertiesFromSource.isEmpty()) {
      deletedPropertiesFromSource.forEach((key, values) -> {
        deletedPropertyCodes.add(key);
        classifierCodes.addAll(values);
      });
    }

    if (!defaultValuesDiffList.isEmpty()) {
      for (IDefaultValueChangeModel defaultValueDiff : defaultValuesDiffList) {
        classifierCodes.addAll(defaultValueDiff.getKlassAndChildrenIds());
        
        // Currently we are handling only CALCULATED & CONCATENATED attribute types.
        if(defaultValueDiff.getType().equals(CommonConstants.ATTRIBUTE)) {
          addedPropertyCodes.add(defaultValueDiff.getEntityId());
        }
      }
    }
    
    // When property is removed from PC, but the PC isn't part of any Klass/Taxonomy
    if (classifierCodes.isEmpty()) {
      return;
    }
    
    IPropagatePCOnPropertyDeleteDTO propagateOnModificationOnPC = new PropagatePCOnPropertyDeleteDTO();
    propagateOnModificationOnPC.setClassifierCodes(classifierCodes);
    propagateOnModificationOnPC.setDeletedPropertyCodes(deletedPropertyCodes);
    propagateOnModificationOnPC.setAddedPropertyCodes(addedPropertyCodes);
    BGPDriverDAO.instance().submitBGPProcess(context.getUserName(), PROPAGATE_ON_PROPERTY_ADD_OR_DELETE, "", BGP_PRIORITY,
        new JSONContent(propagateOnModificationOnPC.toJSON()));
  }
}
