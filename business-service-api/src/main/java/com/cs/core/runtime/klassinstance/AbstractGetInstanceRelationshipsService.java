package com.cs.core.runtime.klassinstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.entity.relationship.RelationshipSide;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForRelationshipPaginationModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestForRelationshipsModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestForRelationshipsModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForRelationshipPaginationStrategy;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Component
public abstract class AbstractGetInstanceRelationshipsService<P extends IGetKlassInstanceRelationshipsStrategyModel, R extends IGetKlassInstanceRelationshipPaginationModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ISessionContext                                    context;
  
  @Autowired
  protected IGetConfigDetailsForRelationshipPaginationStrategy getConfigDetailsForRelationshipPaginationStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                rdbmsComponentUtils;
  
  @Autowired
  SearchAssembler                                              searchAssembler;
  
  @Autowired
  protected GetAllUtils                                        getAllUtils;
  
  protected abstract R executeGetInstanceRelationships(P getKlassInstanceRelationshipsStrategyModel)
      throws Exception;
  
  protected abstract IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception;
  
  protected abstract BaseType getBaseType();
  
  @Override
  protected R executeInternal(P getKlassInstanceRelationshipsStrategyModel) throws Exception
  {
    String loginUserId = context.getUserId();
    getKlassInstanceRelationshipsStrategyModel.setCurrentUserId(loginUserId);
    
    String klassInstanceId = getKlassInstanceRelationshipsStrategyModel.getId();
    
    getKlassInstanceRelationshipsStrategyModel.setId(klassInstanceId);
    getKlassInstanceRelationshipsStrategyModel.setRelationshipId(getKlassInstanceRelationshipsStrategyModel.getRelationshipId());
    
    IKlassInstanceTypeModel klassInstanceTypeModel = getKlassInstanceType(klassInstanceId);
    
    IMulticlassificationRequestForRelationshipsModel multiclassificationrequestmodel = new MulticlassificationRequestForRelationshipsModel();
    multiclassificationrequestmodel.getKlassIds().addAll(klassInstanceTypeModel.getTypes());
    multiclassificationrequestmodel.setXRayAttributes(getKlassInstanceRelationshipsStrategyModel.getXRayAttributes());
    multiclassificationrequestmodel.setXRayTags(getKlassInstanceRelationshipsStrategyModel.getXRayTags());
    multiclassificationrequestmodel.setSideId(getKlassInstanceRelationshipsStrategyModel.getSideId());
    multiclassificationrequestmodel.setUserId(context.getUserId());
    
    IConfigDetailsForRelationshipPaginationModel multiClassificationDetails = getConfigDetailsForRelationshipPaginationStrategy.execute(multiclassificationrequestmodel);
    getKlassInstanceRelationshipsStrategyModel.setKlassIdsHavingRP(multiClassificationDetails.getKlassIdsHavingRP());
    getKlassInstanceRelationshipsStrategyModel.setEntities(multiClassificationDetails.getEntities());
    getKlassInstanceRelationshipsStrategyModel.setReferencedElements(multiClassificationDetails.getReferencedElements());
    
    IXRayConfigDetailsModel xrayConfigDetails = multiClassificationDetails.getXRayConfigDetails();
    
    List<IKlassInstanceInformationModel> klassInstanceInformationModelList = new ArrayList<IKlassInstanceInformationModel>();
    
    R responseModel = executeGetInstanceRelationships(getKlassInstanceRelationshipsStrategyModel);
    
    List<IBaseEntityDTO> relationshipInstances = getRelationshipInstances(getKlassInstanceRelationshipsStrategyModel, responseModel);
    Set<IPropertyDTO> properties = getAllUtils.getXrayProperties(xrayConfigDetails.getReferencedAttributes(),
        xrayConfigDetails.getReferencedTags());
  
    for(IBaseEntityDTO relationshipInstance: relationshipInstances) {
     
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(relationshipInstance, rdbmsComponentUtils);
     
      if (properties != null && !properties.isEmpty()) {
        IBaseEntityDAO baseEntityDAO = this.rdbmsComponentUtils.getBaseEntityDAO(relationshipInstance);
        IPropertyDTO[] propertiesArr = properties.toArray(new IPropertyDTO[properties.size()]);
        KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO,
            xrayConfigDetails.getReferencedAttributes(), xrayConfigDetails.getReferencedTags(), rdbmsComponentUtils, propertiesArr);
        
        klassInstanceBuilder.fillAttributeTagProperties(klassInstanceInformationModel);
      }
      klassInstanceInformationModelList.add(klassInstanceInformationModel);
    }
    
    fillNatureRelationInformation(klassInstanceInformationModelList,relationshipInstances, responseModel, getKlassInstanceRelationshipsStrategyModel);
    responseModel.setXRayConfigDetails(multiClassificationDetails.getXRayConfigDetails());
    //TODO set configDetails
    return responseModel;
  }

  public List<IBaseEntityDTO> getRelationshipInstances(IGetKlassInstanceRelationshipsStrategyModel getKlassInstanceRelationshipsStrategyModel, R responseModel) throws NumberFormatException, Exception {
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    IRelationship relationship = new Relationship();
    IRelationshipSide relationshipSide = new RelationshipSide();
    String sideId = getKlassInstanceRelationshipsStrategyModel.getSideId();
    String side = ((ReferencedSectionRelationshipModel)getKlassInstanceRelationshipsStrategyModel.getReferencedElements().get(sideId)).getSide();
    relationshipSide.setId(sideId);
    relationshipSide.setElementId(sideId);
    if(side.equals("side1")) {
      relationship.setSide1(relationshipSide);
      relationship.setSide2(new RelationshipSide());
    }
    else {
      relationship.setSide1(new RelationshipSide());
      relationship.setSide2(relationshipSide);
    }
    
    StringBuilder searchExpression = searchAssembler.getBaseQuery(getBaseType());
    String expressionForRelationship = getExpressionForRelationship(getKlassInstanceRelationshipsStrategyModel, relationship);
    if (!expressionForRelationship.isEmpty()) {
      searchExpression.append(expressionForRelationship);
    }
    IRDBMSOrderedCursor<IBaseEntityDTO> cursor = localeCatlogDAO.getAllEntitiesBySearchExpression(searchExpression.toString(), true);
    responseModel.setTotalContents(cursor.getCount());
    Integer from = getKlassInstanceRelationshipsStrategyModel.getFrom();
    responseModel.setFrom(from);
    List<IBaseEntityDTO> baseEntity = cursor.getNext(from, getKlassInstanceRelationshipsStrategyModel.getSize());
    return baseEntity;
  }
  
  protected String getExpressionForRelationship(IGetKlassInstanceRelationshipsStrategyModel dataModel, IRelationship relationship) throws NumberFormatException, Exception
  {
    String id = dataModel.getId();
    IBaseEntityDTO baseEntity = rdbmsComponentUtils.getBaseEntityDTO(Long.parseLong(id));
    String sideId = dataModel.getSideId();
    String side = ((ReferencedSectionRelationshipModel)dataModel.getReferencedElements().get(sideId)).getSide();
    Integer sideOrdinal = side.equals("side1") ? 1 : 2;
    return String.format(" $entity belongsto [e>%s $iid=%d].[%s $side=%d] ", baseEntity.getBaseEntityID(), baseEntity.getBaseEntityIID(),
        dataModel.getRelationshipId(), sideOrdinal);
  }
  
  protected IPropertyInstance getAttributesAndTags(IPropertyRecordDTO property,
      IBaseEntityDTO baseEntityDTO) throws NumberFormatException, Exception
  {
    switch (property.getProperty()
        .getSuperType()) {
      case ATTRIBUTE:
        IAttributeInstance attributeInstance = new AttributeInstance();
        IValueRecordDTO existingValueRecord = (IValueRecordDTO) property;
        
        attributeInstance.setId(KlassInstanceBuilder.getAttributeID(existingValueRecord));
        attributeInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
        attributeInstance.setBaseType(AttributeInstance.class.getName());
        attributeInstance.setLanguage(existingValueRecord.getLocaleID());
        attributeInstance.setValue(existingValueRecord.getValue());
        attributeInstance.setOriginalInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
        attributeInstance.setAttributeId(existingValueRecord.getProperty()
            .getPropertyCode());
        attributeInstance.setCode(existingValueRecord.getProperty()
            .getPropertyCode());
        
        attributeInstance.setValue(existingValueRecord.getValue());
        attributeInstance.setValueAsNumber(existingValueRecord.getAsNumber());
        attributeInstance.setValueAsHtml(existingValueRecord.getAsHTML());
        return attributeInstance;
      case TAGS:
        ITagInstance tagInstance = new TagInstance();
        ITagsRecordDTO tagsRecordDTO = (ITagsRecordDTO) property;
        tagInstance.setId(tagsRecordDTO.getNodeID());
        tagInstance.setKlassInstanceId(baseEntityDTO.getNatureClassifier()
            .getClassifierCode());
        tagInstance.setBaseType(TagInstance.class.getName());
        tagInstance.setTagId(tagsRecordDTO.getProperty()
            .getPropertyCode());
        
        // Tag values
        List<ITagInstanceValue> tagInstanceValues = new ArrayList<ITagInstanceValue>();
        tagsRecordDTO.getTags()
            .forEach(tagRecordDTO -> {
              ITagInstanceValue tagInstanceValue = new TagInstanceValue();
              tagInstanceValue.setTagId(tagRecordDTO.getTagValueCode());
              tagInstanceValue.setId(KlassInstanceBuilder.getTagValueID(tagRecordDTO));
              tagInstanceValue.setCode(KlassInstanceBuilder.getTagValueID(tagRecordDTO));
              tagInstanceValue.setRelevance(tagRecordDTO.getRange());
              tagInstanceValues.add(tagInstanceValue);
            });
        tagInstance.setTagValues(tagInstanceValues);
        return tagInstance;
      default:
        return null;
    }
  }
  
  private void fillNatureRelationInformation(List<IKlassInstanceInformationModel> klassInstanceInformationModelList, List<IBaseEntityDTO> relationshipInstances,
      R responseModel,
      IGetKlassInstanceRelationshipsStrategyModel klassInstanceRelationshipsStrategyModel)
      throws Exception
  {
    List<IKlassInstanceRelationshipInstance> natureRelationships = new ArrayList<>();
    IKlassInstanceRelationshipInstance instance = new KlassInstanceRelationshipInstance();
    List<String> elementIDs = new ArrayList<>();
    for (IKlassInstanceInformationModel klassInstanceInformationModel : klassInstanceInformationModelList) {
      elementIDs.add(klassInstanceInformationModel.getId());
    }
    instance.setElementIds(elementIDs);
    instance.setTotalCount((long) klassInstanceInformationModelList.size());
    instance.setSideId(klassInstanceRelationshipsStrategyModel.getSideId());
    instance.setId(klassInstanceRelationshipsStrategyModel.getRelationshipId());
    instance.setRelationshipId(klassInstanceRelationshipsStrategyModel.getRelationshipId());
    natureRelationships.add(instance);
    
    Map<String, List<IKlassInstanceInformationModel>> referenceNatureRelationshipMap = new HashMap<>();
    referenceNatureRelationshipMap.put(klassInstanceRelationshipsStrategyModel.getSideId(),
        klassInstanceInformationModelList);
    if (klassInstanceRelationshipsStrategyModel.getIsNatureRelationship()) {
      responseModel.setReferenceNatureRelationshipInstanceElements(referenceNatureRelationshipMap);
      responseModel.setNatureRelationships(natureRelationships);
    }
    else {
      responseModel.setReferenceRelationshipInstanceElements(referenceNatureRelationshipMap);
      responseModel.setContentRelationships(natureRelationships);
    }
  }
  protected void fillAttributesAndTags(Map<String, IAttribute> referenceAttributes, Map<String, ITag> referenceTags, Set<IPropertyDTO>
      properties,IBaseEntityDTO baseEntityDTO, 
      IKlassInstanceInformationModel klassInstanceInformationModel) throws Exception
  {
    if (properties != null && !properties.isEmpty()) {
      IBaseEntityDAO baseEntityDAO = this.rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
      
      IPropertyDTO[] propertiesArr = properties.toArray(new IPropertyDTO[properties.size()]);
      KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, referenceAttributes, referenceTags, rdbmsComponentUtils,
          propertiesArr);
      
      klassInstanceBuilder.fillAttributeTagProperties(klassInstanceInformationModel);
    }
  }
}


