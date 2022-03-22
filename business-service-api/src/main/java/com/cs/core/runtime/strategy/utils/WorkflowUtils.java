package com.cs.core.runtime.strategy.utils;

import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.di.workflow.trigger.standard.*;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel.ApplicationActionType;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.BusinessProcessActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkflowUtils {

    @Autowired
    protected IApplicationTriggerService applicationEvent;

    @Autowired
    protected IBusinessProcessTriggerService businessProcessEvent;

    /**
     * Use this method to execute application events
     *
     * @param actionType
     * @param serviceData
     * @param bgpPriority
     * @throws Exception
     */
    public void executeApplicationEvent(ApplicationActionType actionType, String serviceData,
                                        BGPPriority bgpPriority) throws Exception {
        if(actionType==null)
            return;

        ApplicationTriggerModel model = new ApplicationTriggerModel();
        model.setPriority(bgpPriority);
        model.setServiceData(serviceData);
        model.setApplicationActionType(actionType);
        applicationEvent.triggerQualifyingWorkflows(model);
    }

    /**
     * Use this method to execute business process events
     *
     * @param actionType
     * @param request
     * @param response
     * @throws Exception
     */
    public void executeBusinessProcessEvent(BusinessProcessActionType actionType, IModel request, IModel response) throws Exception {
        if(actionType==null)
            return;

        IBusinessProcessTriggerModel businessProcessEventModel = fillBusinessProcessEventModel(actionType, request, response);
        if(businessProcessEventModel!=null)
            businessProcessEvent.triggerQualifyingWorkflows(businessProcessEventModel);
    }

    /**
     *
     * @param actionType
     * @param request
     * @param response
     * @return
     */
    public IBusinessProcessTriggerModel fillBusinessProcessEventModel(BusinessProcessActionType actionType, IModel request, IModel response) {
        IBusinessProcessTriggerModel businessProcessEventModel = new BusinessProcessTriggerModel();
        businessProcessEventModel.setBusinessProcessActionType(actionType);
        switch (actionType) {
            case AFTER_CREATE:
                if (request instanceof ICreateInstanceModel) {
                    List<String> klassIds = new ArrayList<>();
                    klassIds.add(((ICreateInstanceModel) request).getType());
                    businessProcessEventModel.setKlassIds(klassIds);
                }
                if (response instanceof IKlassInstanceInformationModel ) {
                  businessProcessEventModel.setBaseType(((IKlassInstanceInformationModel) response).getBaseType());
                }
                else
                    return null;
                break;
            case AFTER_SAVE:
                if (request instanceof IKlassInstanceSaveModel) {
                    IKlassInstanceSaveModel propertiesSaveRequest = (IKlassInstanceSaveModel) request;
                    fillPropertiesCriteria(propertiesSaveRequest, businessProcessEventModel);
                    fillClassificationCriteria(request, response, businessProcessEventModel);
                    businessProcessEventModel.setActionSubType(IBusinessProcessTriggerModel.ActionSubTypes.AFTER_PROPERTIES_SAVE);
                    businessProcessEventModel.setBaseType(((IKlassInstanceSaveModel) request).getBaseType());
                } else if (request instanceof ISaveRelationshipInstanceModel) {
                    fillClassificationCriteria(request, response, businessProcessEventModel);
                    businessProcessEventModel.setActionSubType(IBusinessProcessTriggerModel.ActionSubTypes.AFTER_RELATIONSHIP_SAVE);
                    businessProcessEventModel.setBaseType(((ISaveRelationshipInstanceModel) request).getBaseType());
                } else if (request instanceof IKlassInstanceTypeSwitchModel && response instanceof IConfigDetailsForSwitchTypeRequestModel) {
                    fillClassificationCriteria(request, response, businessProcessEventModel);
                    businessProcessEventModel.setActionSubType(IBusinessProcessTriggerModel.ActionSubTypes.AFTER_CLASSIFICATION_SAVE);
                    businessProcessEventModel.setBaseType(((IConfigDetailsForSwitchTypeRequestModel) response).getBaseType());
                } else
                    return null;
                break;
          default:
            return null;
        }
        fillKlassInstanceId(businessProcessEventModel, request, response);
        return businessProcessEventModel;
    }

    /**
     *
     * @param request
     * @param triggerCriteria
     */
    private void fillPropertiesCriteria(IKlassInstanceSaveModel request, IBusinessProcessTriggerModel triggerCriteria) {
        List<String> attributeIds = new ArrayList<>();
        for (IModifiedContentAttributeInstanceModel modifiedAttribute : request.getModifiedAttributes()) {
            attributeIds.add(modifiedAttribute.getAttributeId());
        }
        for (IContentAttributeInstance addedAttribute : request.getAddedAttributes()) {
            attributeIds.add(addedAttribute.getAttributeId());
        }
        triggerCriteria.setAttributeIds(attributeIds);

        List<String> tagIds = new ArrayList<>();
        for (IModifiedContentTagInstanceModel modifiedTag : request.getModifiedTags()) {
            tagIds.add(modifiedTag.getTagId());
        }
        for (ITagInstance addedTag : request.getAddedTags()) {
            tagIds.add(addedTag.getTagId());
        }
        triggerCriteria.setTagIds(tagIds);
    }

    /**
     *
     * @param request
     * @param response
     * @param triggerCriteria
     */
    private void fillClassificationCriteria(IModel request, IModel response, IBusinessProcessTriggerModel triggerCriteria) {
        IContentInstance classInstance = null;
        if (response instanceof IGetKlassInstanceModel) {
            classInstance = ((IGetKlassInstanceModel) response).getKlassInstance();
        } else if (response instanceof IContentInstance) {
            classInstance = (IContentInstance) response;
        } else if (request instanceof IContentInstance) {
            classInstance = (IContentInstance) request;
        } else if(response instanceof IConfigDetailsForSwitchTypeRequestModel) {
            IConfigDetailsForSwitchTypeRequestModel model = (IConfigDetailsForSwitchTypeRequestModel) response;
            triggerCriteria.setTaxonomyIds(model.getSelectedTaxonomyIds());
            triggerCriteria.setKlassIds(model.getKlassIds());
            return;
        }
        if (classInstance != null) {
            triggerCriteria.setTaxonomyIds(classInstance.getSelectedTaxonomyIds());
            triggerCriteria.setKlassIds(classInstance.getTypes());
        }
    }

    protected void fillKlassInstanceId(IBusinessProcessTriggerModel businessProcessEventModel, IModel request, IModel response) {
        IContentInstance classInstance = null;
        String klassInstanceId = null;

        // Article
        if (response instanceof IGetKlassInstanceModel) {
            classInstance = ((IGetKlassInstanceModel) response).getKlassInstance();
        } else if (response instanceof IContentInstance) {
            classInstance = (IContentInstance) response;
        } else if (request instanceof IContentInstance) {
            classInstance = (IContentInstance) request;
        }
        // Asset
        else if(response instanceof IKlassInstanceInformationModel) {
            klassInstanceId = ((IKlassInstanceInformationModel) response).getId();
        }
        //Type Switch
        else if(request instanceof IKlassInstanceTypeSwitchModel) {
            klassInstanceId =  ((IKlassInstanceTypeSwitchModel)request).getKlassInstanceId();
        }
        // Varient
        else if (request instanceof ICreateVariantModel && response instanceof IIdParameterModel) {
            klassInstanceId = ((IIdParameterModel) response).getId();
        }

        if (classInstance != null) {
            klassInstanceId = classInstance.getId();
        }
        businessProcessEventModel.setKlassInstanceId(klassInstanceId);
    }
    
    public void executeBusinessProcessEvent(IBusinessProcessTriggerModel businessProcessEventModel) throws Exception {
      businessProcessEvent.triggerQualifyingWorkflows(businessProcessEventModel);
}

    public enum UseCases {
        //For Business Process Events
        SAVEARTIKLE(BusinessProcessActionType.AFTER_SAVE), UNDEFINED(null), ROLLBACKARTIKLE(BusinessProcessActionType.AFTER_SAVE), CREATEARTIKLE(BusinessProcessActionType.AFTER_CREATE), TRANSFERCONTENT(BusinessProcessActionType.AFTER_SAVE), SAVETAXONOMY(BusinessProcessActionType.AFTER_CONFIG_SAVE),
        CREATETAXONOMY(BusinessProcessActionType.AFTER_CONFIG_CREATE), SAVEATTRIBUTIONTAXONOMY(BusinessProcessActionType.AFTER_CONFIG_SAVE), CREATEATTRIBUTIONTAXONOMY(BusinessProcessActionType.AFTER_CONFIG_CREATE),
        CREATEBULKATTRIBUTIONTAXONOMY(BusinessProcessActionType.AFTER_CONFIG_CREATE), CREATEBULKTAXONOMY(BusinessProcessActionType.AFTER_CONFIG_CREATE), DELETETAXONOMYLEVEL(BusinessProcessActionType.AFTER_CONFIG_SAVE),
        DELETEATTRIBUTIONTAXONOMYLEVEL(BusinessProcessActionType.AFTER_CONFIG_SAVE), SAVEASSET(BusinessProcessActionType.AFTER_SAVE), IMPORTARTICLE(BusinessProcessActionType.AFTER_SAVE), CREATEASSET(BusinessProcessActionType.AFTER_CREATE), SAVEARTIKLERELATIONSHIP(BusinessProcessActionType.AFTER_SAVE), SAVEASSETRELATIONSHIP(BusinessProcessActionType.AFTER_SAVE), CLASSIFICATIONSAVE(BusinessProcessActionType.AFTER_SAVE);

        final public BusinessProcessActionType actionType;

        UseCases(BusinessProcessActionType actionType) {
            this.actionType = actionType;
        }
    }
}
