import ComponentParameterProperties from './process-component-parameter-properties-layout-data';

let ProcessComponentParameterLayoutData = function () {
  let oComponentParameterProperties = new ComponentParameterProperties();
  let ComponentProperties = oComponentParameterProperties.component_properties;

  return {
    components: {
      Article_Import: [
        ComponentProperties.SHEET_NAME, ComponentProperties.PRIMARY_KEY_COLUMN, ComponentProperties.SINGLE_CLASS_TYPES, ComponentProperties.CLASS_TYPE_COLUMN, ComponentProperties.IS_MULTICLASSIFICATION,
        ComponentProperties.TAXONOMY
      ],
      Extended_Article_Import: [
        ComponentProperties.SHEET_NAME, ComponentProperties.PRIMARY_KEY_COLUMN, ComponentProperties.MULTI_CLASS_TYPES, ComponentProperties.MULTI_CLASS_INFO, ComponentProperties.TAXONOMY
      ],
      Extended_Assets_Import: [
        ComponentProperties.SHEET_NAME, ComponentProperties.PRIMARY_KEY_COLUMN, ComponentProperties.MULTI_CLASS_TYPES, ComponentProperties.MULTI_CLASS_INFO, ComponentProperties.TAXONOMY
      ],
      Extended_TAM_Import: [
        ComponentProperties.SHEET_NAME, ComponentProperties.PRIMARY_KEY_COLUMN, ComponentProperties.MULTI_CLASS_TYPES, ComponentProperties.MULTI_CLASS_INFO, ComponentProperties.TAXONOMY
      ],
      Extended_Market_Import: [
        ComponentProperties.SHEET_NAME, ComponentProperties.PRIMARY_KEY_COLUMN, ComponentProperties.MULTI_CLASS_TYPES, ComponentProperties.MULTI_CLASS_INFO, ComponentProperties.TAXONOMY
      ],
      Article_Variant_Import: [
        ComponentProperties.SHEET_NAME, ComponentProperties.PRIMARY_KEY_COLUMN, ComponentProperties.PARENT_INSTANCE_ID, ComponentProperties.IS_ATTRIBUTE_VARIANT, ComponentProperties.VARIANT_TYPE, ComponentProperties.CONTEXT_TYPE_COLUMN, ComponentProperties.TAG_COLUMN_NAME, ComponentProperties.FROM_DATE_COLUMN_NAME, ComponentProperties.TO_DATE_COLUMN_NAME, ComponentProperties.TAXONOMY
      ],
      Relationships_Import: [
        ComponentProperties.SHEET_NAME, ComponentProperties.SOURCE_COLUMN_NAME, ComponentProperties.TARGET_COLUMN_NAME, ComponentProperties.RELATIONSHIP_TYPE, ComponentProperties.RELATIONSHIP,
        ComponentProperties.CONTEXT_COLUMN, ComponentProperties.CONTEXT_TAGS, ComponentProperties.FROM_DATE_COLUMN_NAME, ComponentProperties.TO_DATE_COLUMN_NAME
      ],
      Nature_Relationships_Import: [
        ComponentProperties.SHEET_NAME, ComponentProperties.SOURCE_COLUMN_NAME, ComponentProperties.TARGET_COLUMN_NAME, ComponentProperties.RELATIONSHIP, ComponentProperties.CONTEXT_COLUMN,
        ComponentProperties.CONTEXT_TAGS, ComponentProperties.FROM_DATE_COLUMN_NAME, ComponentProperties.TO_DATE_COLUMN_NAME
      ],
      Supplier_Staging_Import: [],
      Central_MDM_Import: [],
      Taxonomy_Import: [],
      File_Upload_Event: [],
      Save_Event: [],
      Export_Event: [],
      Supplier_Import: [
        ComponentProperties.SHEET_NAME, ComponentProperties.PRIMARY_KEY_COLUMN, ComponentProperties.SINGLE_CLASS_TYPES, ComponentProperties.CLASS_TYPE_COLUMN, ComponentProperties.IS_MULTICLASSIFICATION,
        ComponentProperties.TAXONOMY
      ],
      Extended_Supplier_Import: [
        ComponentProperties.SHEET_NAME, ComponentProperties.PRIMARY_KEY_COLUMN, ComponentProperties.MULTI_CLASS_TYPES, ComponentProperties.MULTI_CLASS_INFO, ComponentProperties.TAXONOMY
      ],
      Transfer_Component: [
        ComponentProperties.DESTINATION_CATALOG, ComponentProperties.DESTINATION_ORGANISATION
      ],
      Jms_Producer: [
        ComponentProperties.PRODUCER_IP, ComponentProperties.PRODUCER_PORT, ComponentProperties.QUEUE_NAME, ComponentProperties.ACKNOWLEDGEMENT_QUEUE_NAME
      ],
      Jms_Consumer: [
        ComponentProperties.CONSUMER_IP, ComponentProperties.CONSUMER_PORT, ComponentProperties.QUEUE_NAME, ComponentProperties.ACKNOWLEDGEMENT_QUEUE_NAME
      ],
      Save_Article: [],
      Di_Talend_Component: [
        ComponentProperties.TALEND_JOBS
      ],
      Assets_Import: [
        ComponentProperties.FILE_SOURCE, ComponentProperties.URL_COLUMN_NAME, ComponentProperties.SHEET_NAME, ComponentProperties.PRIMARY_KEY_COLUMN, ComponentProperties.SINGLE_CLASS_TYPES,
        ComponentProperties.CLASS_TYPE_COLUMN, ComponentProperties.TAXONOMY
      ],
      customComponent: [
        ComponentProperties.SHEET_NAME, ComponentProperties.PRIMARY_KEY_COLUMN, ComponentProperties.SINGLE_CLASS_TYPES, ComponentProperties.CLASS_TYPE_COLUMN, ComponentProperties.TAXONOMY
      ]
    }
  }
};

export default ProcessComponentParameterLayoutData;