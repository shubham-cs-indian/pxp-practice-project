package com.cs.di.workflow.trigger.standard;

import java.util.List;

import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.TransactionData;

public class BusinessProcessTriggerModel extends WorkflowEventTriggerModel implements IBusinessProcessTriggerModel {

    private static final long serialVersionUID = 1L;

    protected ActionSubTypes actionSubType;
    protected List<String> klassIds;
    protected List<String> taxonomyIds;
    protected List<String> attributeIds;
    protected List<String> tagIds;
    protected List<String> nonNatureKlassIds;
    protected String klassInstanceId;
    protected BusinessProcessActionType businessProcessActionType;
    protected String baseType;
    protected Usecase usecase;
    
    protected IUserSessionDTO user = null;
    protected TransactionData trans = null;
    
    @Override
    public IUserSessionDTO getUserSession(){
      return user;
    }
    
    @Override
    public void setUserSession(IUserSessionDTO user) {
      this.user = user;
    }
    
    @Override
    public TransactionData getTransactionData(){
      return trans;
    }
    
    @Override
    public void setTransactionData(TransactionData trans){
      this.trans = trans;
    }

    @Override
    public BusinessProcessActionType getBusinessProcessActionType() {
        return businessProcessActionType;
    }

    @Override
    public void setBusinessProcessActionType(BusinessProcessActionType businessProcessActionType) {
        this.businessProcessActionType = businessProcessActionType;
        this.triggeringType = businessProcessActionType.toString();
    }

    @Override
    public String getKlassInstanceId() {
        return klassInstanceId;
    }

    @Override
    public void setKlassInstanceId(String klassInstanceId) {
        this.klassInstanceId = klassInstanceId;
    }

    @Override
    public List<String> getKlassIds() {
        return this.klassIds;
    }

    @Override
    public void setKlassIds(List<String> klassIds) {
        this.klassIds = klassIds;
    }

    @Override
    public List<String> getTaxonomyIds() {
        return this.taxonomyIds;
    }

    @Override
    public void setTaxonomyIds(List<String> taxonomyIds) {
        this.taxonomyIds = taxonomyIds;
    }

    @Override
    public List<String> getAttributeIds() {
        return attributeIds;
    }

    @Override
    public void setAttributeIds(List<String> selectedAttributes) {
        this.attributeIds = selectedAttributes;
    }

    @Override
    public List<String> getTagIds() {
        return tagIds;
    }

    @Override
    public void setTagIds(List<String> selectedTags) {
        this.tagIds = selectedTags;
    }

    @Override
    public List<String> getNonNatureKlassIds() {
        return nonNatureKlassIds;
    }

    @Override
    public void setNonNatureKlassIds(List<String> nonNatureKlassIds) {
        this.nonNatureKlassIds = nonNatureKlassIds;
    }

    @Override
    public ActionSubTypes getActionSubType() {
        return actionSubType;
    }

    @Override
    public void setActionSubType(ActionSubTypes actionSubType) {
        this.actionSubType = actionSubType;
    }

    @Override
    public String getBaseType()
    {
      return baseType;
    }

    @Override
    public void setBaseType(String baseType)
    {
      this.baseType = baseType;     
    }

    @Override
    public Usecase getUsecase()
    {
      return usecase;
    }

    @Override
    public void setUsecase(Usecase usecase)
    {
      this.usecase = usecase;
    }
}
