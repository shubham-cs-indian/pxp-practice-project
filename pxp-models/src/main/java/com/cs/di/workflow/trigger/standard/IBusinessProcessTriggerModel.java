package com.cs.di.workflow.trigger.standard;

import java.util.List;

import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.TransactionData;

public interface IBusinessProcessTriggerModel extends IWorkflowEventTriggerModel {
    String ACTION_SUB_TYPE = "actionSubType";
    String KLASS_IDS = "klassIds";
    String ATTRIBUTE_IDS = "attributeIds";
    String TAG_IDS = "tagIds";
    String TAXONOMY_IDS = "taxonomyIds";
    String NON_NATURE_KLASS_IDS = "nonNatureKlassIds";
    String KLASS_INSTANCE_ID = "klassInstanceId";
    String BUSINESS_PROCESS_ACTION_TYPE = "businessProcessActionType";
    String BASETYPE = "baseType";
    String USECASE = "usecase";

    String getKlassInstanceId();

    void setKlassInstanceId(String klassInstanceId);
       
    String getBaseType();
    
    void setBaseType(String baseType);

    List<String> getKlassIds();

    void setKlassIds(List<String> klassIds);

    List<String> getTaxonomyIds();

    void setTaxonomyIds(List<String> taxonomyIds);

    List<String> getAttributeIds();

    void setAttributeIds(List<String> attributeIds);

    List<String> getTagIds();

    void setTagIds(List<String> tagIds);

    List<String> getNonNatureKlassIds();

    void setNonNatureKlassIds(List<String> nonNatureKlassIds);

    ActionSubTypes getActionSubType();

    void setActionSubType(ActionSubTypes actionSubType);

    BusinessProcessActionType getBusinessProcessActionType();
    void setBusinessProcessActionType(BusinessProcessActionType businessProcessActionType);
    
    IUserSessionDTO getUserSession();
    
    void setUserSession(IUserSessionDTO user);
    
    TransactionData getTransactionData();
    
    void setTransactionData(TransactionData transactionData);
    
    public Usecase getUsecase();
    
    public void setUsecase(Usecase usecase);

    enum BusinessProcessActionType {
        AFTER_SAVE("afterSave"), AFTER_CREATE("afterCreate"), AFTER_CONFIG_SAVE("afterConfigSave"), AFTER_CONFIG_CREATE("afterConfigCreate"), ;

        private String actionType;
        public String toString() {
            return actionType;
        }

        BusinessProcessActionType(String actionType) {
            this.actionType = actionType;
        }
    }

    enum ActionSubTypes {
        AFTER_PROPERTIES_SAVE("afterPropertiesSave"), AFTER_CLASSIFICATION_SAVE("afterClassificationSave"),
        AFTER_RELATIONSHIP_SAVE("afterRelationshipSave"), AFTER_CONTEXT_SAVE("afterContextSave");

        private String actionSubType;

        ActionSubTypes(String actionSubType) {
            this.actionSubType = actionSubType;
        }

        public String toString() {
            return actionSubType;
        }
    }
    
    enum Usecase {
      IMPORT("import"), TRANSFER("transfer"),
      OTHERS("others");

      private String usecase;

      Usecase(String usecase) {
          this.usecase = usecase;
      }

      public String toString() {
          return usecase;
      }
  }
    
}
