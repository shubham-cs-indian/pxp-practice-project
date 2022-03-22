package com.cs.core.runtime.contentgrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.strategy.usecase.attribute.IGetGridAttributesStrategy;
import com.cs.core.config.strategy.usecase.klass.gridview.IGetConfigDetailsForContentGridViewStrategy;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.contentgrid.GetContentGridKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.contentgrid.IGetContentGridKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.gridcontent.GetContentGridElasticResponseModel;
import com.cs.core.runtime.interactor.model.gridcontent.GetKlassConfigRequestModel;
import com.cs.core.runtime.interactor.model.gridcontent.GetReferencedElementRequestModel;
import com.cs.core.runtime.interactor.model.gridcontent.GridContentInstanceModel;
import com.cs.core.runtime.interactor.model.gridcontent.IConfigDetailsForContentGridViewModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetContentGridElasticResponseModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetContentGridRequestModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetKlassConfigRequestModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetReferencedElementRequestModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGridContentInstanceModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGridInstanceConfigDetailsModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.utils.BaseEntityUtils;

@Service
public class GetContentGridViewService extends
    AbstractRuntimeService<IGetContentGridRequestModel, IGetContentGridKlassInstanceResponseModel> implements IGetContentGridViewService {
  
  @Autowired
  protected ISessionContext                             context;
  
  @Autowired
  protected IGetConfigDetailsForContentGridViewStrategy getConfigDetailsForContentGridViewStrategy;
  
  @Autowired
  protected PermissionUtils                             permissionUtils;
  
  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  protected IGetGridAttributesStrategy                  getAllAttributesForGridStrategy;
  
  @Override
  public IGetContentGridKlassInstanceResponseModel executeInternal(IGetContentGridRequestModel dataModel) throws Exception
  {
    
    IGetContentGridElasticResponseModel elasticResponseModel = new GetContentGridElasticResponseModel();
    
    Set<IBaseEntityDAO> baseEntities = new HashSet<>();
    Map<String, IGetReferencedElementRequestModel> klassElementRequestMap = new HashMap<>();
    for (IIdAndBaseType idAndBaseType : dataModel.getKlassInstances()) {
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(idAndBaseType.getId()));
      IGetReferencedElementRequestModel elementModel = new GetReferencedElementRequestModel();
      elementModel.setKlassIds(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO));
      elementModel.setTaxonomyIds(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers()));
      String id = String.valueOf(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID());
      klassElementRequestMap.put(id, elementModel);
      baseEntities.add(baseEntityDAO);
    }
    
    IGetKlassConfigRequestModel configRequestModel = new GetKlassConfigRequestModel();
    configRequestModel.setKlassReferencedElements(klassElementRequestMap);
    configRequestModel.setSelectedPropertyIds(dataModel.getSelectedPropertyIds());
    configRequestModel.setUserId(context.getUserId());
    configRequestModel.setTagIdTagValueIdsMap(elasticResponseModel.getTagIdTagValueIdsMap());
    
    // call to orient
    IConfigDetailsForContentGridViewModel configDetails = getConfigDetailsForContentGridViewStrategy.execute(configRequestModel);
    Map<String, IGridInstanceConfigDetailsModel> gridInstanceConfigDetails = configDetails.getInstanceConfigDetails();
    // prepare response model
    List<IGridContentInstanceModel> responseKlassInstancesList = new ArrayList<>();
    
    for (IBaseEntityDAO baseEntityDAO : baseEntities) {
      String id = String.valueOf(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID());
      IGridInstanceConfigDetailsModel gridInstanceConfigDetailsModel = configDetails.getInstanceConfigDetails().get(id);
      KlassInstanceBuilder KlassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails.getReferencedAttributes(),
          configDetails.getReferencedTags(), gridInstanceConfigDetailsModel.getReferencedElements(), rdbmsComponentUtils);
      IContentInstance klassInstance = (IContentInstance) KlassInstanceBuilder.getKlassInstance();
      
      IGridContentInstanceModel responseInstance = new GridContentInstanceModel();
      responseInstance.setKlassInstance(klassInstance);
      
      IGridInstanceConfigDetailsModel instanceConfigDetails = gridInstanceConfigDetails.get(klassInstance.getId());
      Map<String, IReferencedSectionElementModel> referencedElements = instanceConfigDetails.getReferencedElements();
      
      permissionUtils.manageGridPermission(instanceConfigDetails, responseInstance);
      responseInstance.setReferencedElements(referencedElements);
      responseKlassInstancesList.add(responseInstance);
      preparePropertyInstances(configDetails, klassInstance);
    }
    
    IGetContentGridKlassInstanceResponseModel returnModel = new GetContentGridKlassInstanceResponseModel();
    returnModel.setKlassInstances(responseKlassInstancesList);
    returnModel.setReferencedAttributes(configDetails.getReferencedAttributes());
    returnModel.setReferencedTags(configDetails.getReferencedTags());
    returnModel.setGridPropertySequenceList(configDetails.getGridPropertySequenceList());
    return returnModel;
  }
  
  /**
   * remove attributeInstance and tagInstance from klassInstance which are not
   * present in referencedAttribute or tag as this property is not need in
   * content grid
   *
   * @author Niraj
   * @param configDetails
   * @param klassInstance
   */
  private void preparePropertyInstances(IConfigDetailsForContentGridViewModel configDetails, IContentInstance klassInstance)
  {
    
    klassInstance.getRoles().clear();
    
    // Removing Unrequired Attributes
    List<IContentAttributeInstance> requiredAttributes = new ArrayList<>();
    List<? extends IContentAttributeInstance> attributes = klassInstance.getAttributes();
    Set<String> attributeIds = configDetails.getReferencedAttributes().keySet();
    
    if (attributeIds.isEmpty()) {
      attributes.clear();
    }
    else {
      for (IContentAttributeInstance attribute : attributes) {
        if (attributeIds.contains(attribute.getAttributeId())) {
          requiredAttributes.add(attribute);
        }
      }
      klassInstance.setAttributes(requiredAttributes);
    }
    // Removing Unrequired Tags
    List<? extends IContentTagInstance> tags = klassInstance.getTags();
    List<IContentTagInstance> requiredTags = new ArrayList<>();
    Set<String> tagIds = configDetails.getReferencedTags().keySet();
    
    if (tagIds.isEmpty()) {
      tags.clear();
    }
    else {
      for (IContentTagInstance tag : tags) {
        if (tagIds.contains(tag.getTagId())) {
          requiredTags.add(tag);
        }
      }
      klassInstance.setTags(requiredTags);
    }
  }
}
