package com.cs.core.config.businessapi.tag;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.bgprocess.dto.PropertyDeleteDTO;
import com.cs.core.bgprocess.dto.TagValueDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IPropertyDeleteDTO;
import com.cs.core.bgprocess.idto.ITagValueDeleteDTO;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IInstanceCountRequestModel;
import com.cs.core.config.interactor.model.klass.InstanceCountRequestModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.tag.IDeleteTagStrategy;
import com.cs.core.config.strategy.usecase.tag.IGetTagsByIdStrategy;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.runtime.interactor.exception.delete.InstanceExistsForTaxonomyException;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.services.CSCache;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeleteTagsService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> implements IDeleteTagsService{
  
  @Autowired
  protected IDeleteTagStrategy              neo4jDeleteTagsStrategy;
  
  @Autowired
  protected IGetTagsByIdStrategy            getTagsByIdStrategy;
  
  @Autowired
  protected TransactionThreadData           transactionThread;
  
  @Autowired
  protected IGetEntityConfigurationStrategy getTagEntityConfigurationStrategy;
  
  @Autowired
  protected WorkflowUtils                   workflowUtils;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    IGetEntityConfigurationResponseModel getEntityResponse = getTagEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(dataModel.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();
    
    if (!referenceData.keySet()
        .isEmpty()) {
      throw new EntityConfigurationDependencyException();
    }
    
    IListModel<ITag> list = getTagsByIdStrategy.execute(dataModel);
    List<String> taxonomyIds = new ArrayList<>();
    Map<String, List<String>> taxonomyIdVsChildTaxonomyIds = new HashMap<>();
    List<ITag> tags = new ArrayList<>(list.getList());
    tags.forEach(tag -> {
      String id = tag.getId();
      taxonomyIds.add(id);
      List<? extends ITreeEntity> children = tag.getChildren();
      children.forEach(child -> taxonomyIds.add(child.getId()));
      taxonomyIdVsChildTaxonomyIds.put(id, taxonomyIds);
    });
    
    IInstanceCountRequestModel instanceCountRequestModel = new InstanceCountRequestModel();
    instanceCountRequestModel.setTaxonomyIdVsChildTaxonomyIds(taxonomyIdVsChildTaxonomyIds);
    
    if (dataModel.getIds()
        .isEmpty()) {
      throw new InstanceExistsForTaxonomyException();
    }
    IBulkDeleteReturnModel responseModel = neo4jDeleteTagsStrategy.execute(dataModel);
    
    this.propertyDeleteBGP(list, responseModel);
    
    return responseModel;
  }
  
  @SuppressWarnings("unchecked")
  protected void propertyDeleteBGP(IListModel<ITag> list, IBulkDeleteReturnModel responseModel) throws Exception
  {
    List<String> success = (List<String>) responseModel.getSuccess();
    
    Set<String> deleteTagValue = new HashSet<>();
    Set<IPropertyDTO> propertyDelete = new HashSet<>();
    
    for(ITag tag : list.getList()) {
      if(success.contains(tag.getCode())) {
        if(tag.getParent() != null) {
          deleteTagValue.add(tag.getCode());
        }else {
          propertyDelete.add(RDBMSUtils.getPropertyByCode(tag.getCode()));
          CSCache.deletePropertyCache(tag.getCode());
        }
      }
      
    }
    
    if(! deleteTagValue.isEmpty()) {
      ITagValueDeleteDTO tagValueDeleteDTO = new TagValueDeleteDTO();
      tagValueDeleteDTO.setTagValues(deleteTagValue);
      this.workflowUtils.executeApplicationEvent(IApplicationTriggerModel.ApplicationActionType.TAG_VALUE_DELETE, tagValueDeleteDTO.toJSON(),
              BGPPriority.MEDIUM);
    }
    
    if(! propertyDelete.isEmpty()) {
      IPropertyDeleteDTO propertyDeleteDTO = new PropertyDeleteDTO();
      propertyDeleteDTO.setProperties(propertyDelete);
      this.workflowUtils.executeApplicationEvent(IApplicationTriggerModel.ApplicationActionType.PROPERTY_DELETE, propertyDeleteDTO.toJSON(),
              BGPPriority.MEDIUM);
    }
  }
  
}
