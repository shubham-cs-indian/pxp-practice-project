import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const ProcessComponentParameterPropertiesLayoutData = function () {
  return {
    component_properties: {
      SHEET_NAME: {
        id: "sheet_name",
        label: oTranslations().SHEET_NAME,
        type: "singleText",
        key: "sheet",
        hintText: oTranslations().ENTER_SHEET_NAME
      },

      PRIMARY_KEY_COLUMN: {
        id: "primary_key_column",
        label: oTranslations().COLUMN_PRIMARY_KEY,
        type: "singleText",
        key: "primaryKeyColumn",
        hintText: oTranslations().ENTER_PRIMARY_KEY_COLUMN_NAME
      },

      SINGLE_CLASS_TYPES: {
        id: "single_class_types",
        label: oTranslations().TYPE,
        type: "select",
        key: "type",
        options: [
          {
            id: "singleClass",
            label: oTranslations().SINGLE_CLASS
          },
          {
            id: "klassColumn",
            label: oTranslations().COLUMN_CLASS
          }
        ]
      },

      CLASS_TYPE_COLUMN: {
        id: "class_type_column",
        type: "dependentFieldSingleClass",
        hintText: "",
        singleClass: {
          label: oTranslations().CLASS_ID,
          key: "klassId",
          type: "lazyMss"
        },
        klassColumn: {
          label: oTranslations().CLASS_COLUMN,
          key: "klassColumn",
          type: "singleText",
          hintText: oTranslations().ENTER_CLASS_COLUMN_NAME
        }
      },

      IS_MULTICLASSIFICATION: {
        id: "is_multiclassification",
        label: oTranslations().ENABLE_MULTICLASSIFICATION,
        type: "customMulticlassificationView",
        key: "isMultiClassificationEnabled",
        dependentFields: ["MULTI_CLASS_TYPES", "MULTI_CLASS_INFO"]
      },

      MULTI_CLASS_TYPES: {
        id: "multi_class_type",
        label: oTranslations().TYPE,
        type: "select",
        key: "secondaryClassType",
        options: [
          {
            id: "multiClass",
            label: oTranslations().MULTIPLE_CLASS
          },
          {
            id: "klassColumn",
            label: oTranslations().COLUMN_CLASS
          }
        ]
      },

      MULTI_CLASS_INFO: {
        id: "class_type_column",
        type: "dependentFieldMultipleClass",
        multiClass: {
          label: oTranslations().MULTI_CLASS_ID,
          key: "secondaryClasses",
          type: "lazyMss"
        },
        klassColumn: {
          label: oTranslations().MULTI_CLASS_COLUMN,
          key: "secondaryClassColumnName",
          type: "singleText",
          hintText: oTranslations().MULTI_CLASS_COLUMN
        }
      },

      TAXONOMY: {
        id: "taxonomy",
        label: oTranslations().TAXONOMIES,
        type: "customTaxonomyView",
        key: "taxonomies"
      },

      PARENT_INSTANCE_ID: {
        id: "parent_instance_id",
        label: oTranslations().PARENT_INSTANCE_ID,
        type: "singleText",
        key: "masterInstanceId"
      },

      IS_ATTRIBUTE_VARIANT: {
        id: "is_attribute_variant",
        label: oTranslations().IS_VARIANT_ATTRIBUTE,
        type: "customAttributeVariantView",
        key: "isAttributeVariant",
        dependentFields: ["ATTRIBUTE_COLUMN", "ATTRIBUTE_VALUE_COLUMN"]
      },

      ATTRIBUTE_COLUMN: {
        id: "attribute_column",
        label: oTranslations().ATTRIBUTE_COLUMN,
        type: "customAttributeColumnView",
        key: "attributeColumn"
      },

      ATTRIBUTE_VALUE_COLUMN: {
        id: "attribute_value_column",
        label: oTranslations().ATTRIBUTE_VALUE_COLUMN,
        type: "singleText",
        key: "attributeValueColumn"
      },

      VARIANT_TYPE: {
        id: "variant_type",
        label: oTranslations().VARIANT_TYPE,
        type: "customVariantTypeView",
        key: "variantType",
        options: [
          {
            id: "variant",
            label: oTranslations().VARIANT
          },
          {
            id: "nestedVariant",
            label: oTranslations().ENABLE_NESTED_VARIANT
          }
        ],
        dependentFields: ["PARENT_CONTEXT_ID", "MASTER_VARIANT_INSTANCE_COLUMN"]
      },

      PARENT_CONTEXT_ID: {
        id: "parent_context_id",
        label: oTranslations().PARENT_CONTEXT_ID,
        type: "select",
        key: "parentContextId"
      },

      MASTER_VARIANT_INSTANCE_COLUMN: {
        id: "master_variant_instance_column",
        label: oTranslations().PARENT_VARIANT_COLUMN_NAME,
        type: "singleText",
        key: "masterVariantInstanceColumn"
      },


      CONTEXT_TYPE_COLUMN: {
        id: "context_type_column",
        label: oTranslations().CONTEXT_ID,
        type: "customContextTypeColumnView",
        key: "contextId"
      },

      TAG_COLUMN_NAME: {
        id: "tag_column_name",
        label: oTranslations().TAG_COLUMN_NAME,
        type: "singleText",
        key: "tagColumnName"
      },

      FROM_DATE_COLUMN_NAME: {
        id: "from_date_column_name",
        label: oTranslations().FROM_DATE_COLUMN_NAME,
        type: "singleText",
        key: "fromDateColumnName"
      },

      TO_DATE_COLUMN_NAME: {
        id: "to_date_column_name",
        label: oTranslations().TO_DATE_COLUMN_NAME,
        type: "singleText",
        key: "toDateColumnName"
      },

      SOURCE_COLUMN_NAME: {
        id: "source_column_name",
        label: oTranslations().SOURCE_COLUMN_NAME,
        type: "singleText",
        key: "sourceIdColumnName"
      },

      TARGET_COLUMN_NAME: {
        id: "target_column_name",
        label: oTranslations().TARGET_COLUMN_NAME,
        type: "singleText",
        key: "targetIdColumnName"
      },

      RELATIONSHIP_TYPE: {
        id: "relationship_type",
        label: oTranslations().TYPE,
        type: "select",
        key: "relationshipType",
        options: [
          {
            id: "relationshipColumn",
            label: oTranslations().RELATIONSHIP_COLUMN
          },
          {
            id: "singleRelationship",
            label: oTranslations().SINGLE_RELATIONSHIP
          }
        ]
      },

      RELATIONSHIP: {
        id: "relationship",
        label: oTranslations().RELATIONSHIP,
        type: "customRelationshipView",
        key: "relationshipId"
      },

      CONTEXT_COLUMN: {
        id: "context_column",
        label: oTranslations().CONTEXT_COLUMN,
        type: "singleText",
        key: "contextIdColumn"
      },

      CONTEXT_TAGS: {
        id: "context_tags",
        label: oTranslations().CONTEXT_TAGS,
        type: "singleText",
        key: "contextTagsColumn"
      },

      TAXONOMY_COLUMN_NAME: {
        id: "taxonomy_column_name",
        label: oTranslations().TAXONOMY_COLUMN_NAME,
        type: "singleText",
        key: "contextTagsColumn"
      },

      DESTINATION_CATALOG: {
        id: "destination_catalog",
        label: "Destination Catalog Id",
        type: "customDestinationCatalogView",
        key: "destinationCatalogId"
      },

      DESTINATION_ORGANISATION: {
        id: "destination_organisation",
        label: "Destination Organisation Id",
        type: "customDestinationOrganisationView",
        key: "destinationOrganizationId"
      },

      PRODUCER_IP: {
        id: "producer_ip",
        label: "Producer IP",
        type: "singleText",
        key: "producerIP"
      },

      PRODUCER_PORT: {
        id: "producer_port",
        label: "Producer Port",
        type: "singleText",
        key: "producerPort"
      },

      QUEUE_NAME: {
        id: "queue_name",
        label: "Destination Queue Name",
        type: "singleText",
        key: "queueName"
      },

      ACKNOWLEDGEMENT_QUEUE_NAME: {
        id: "acknowledgement_queue_name",
        label: "Acknowledgement Queue Name",
        type: "singleText",
        key: "acknowledgementQueueName"
      },

      CONSUMER_IP: {
        id: "consumer_ip",
        label: "Consumer IP",
        type: "singleText",
        key: "consumerIP"
      },

      CONSUMER_PORT: {
        id: "consumer_port",
        label: "Consumer Port",
        type: "singleText",
        key: "consumerPort"
      },

      TALEND_JOBS: {
        id: "talend_jobs",
        label: "Talend Job",
        type: "customTalendJobView",
        key: "talendExecutable"
      },

      FILE_SOURCE: {
        id: "file_source",
        label: oTranslations().FILE_SOURCE,
        type: "dependentSelect",
        key: "fileSource",
        options: [
          {
            id: "fromFolder",
            label: oTranslations().FROM_FOLDER
          },
          {
            id: "fromUrl",
            label: oTranslations().FROM_URL
          }
        ],
        dependentFields: ["IMAGE_FOLDER_PATH"]
      },

      IMAGE_FOLDER_PATH: {
        id: "image_folder_path",
        label: oTranslations().IMAGE_FOLDER_PATH,
        type: "singleText",
        key: "imageFolderPath"
      },

      URL_COLUMN_NAME: {
        id: "url_column_name",
        label: oTranslations().URL_COLUMN_NAME,
        type: "singleText",
        key: "filePathColumnName"
      }
    }
  }
};

export default ProcessComponentParameterPropertiesLayoutData;