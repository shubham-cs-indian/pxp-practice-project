import {getTranslations as oTranslations} from '../../../../../../commonmodule/store/helper/translation-manager';

export default function () {
  return [
    {
      id: "Article",
      label: oTranslations().ARTICLES_LABEL,
      isExpanded: false,
      Column: 1,
      children: [
        {
          id: "Nature Classes",
          label: oTranslations().NATURE_CLASSES,
          count: 0,
          children: [
            {
              id: "singleArticle",
              label: oTranslations().SINGLE_ARTICLE,
              count: 0
            },
            {
              id: "pidSku",
              label: oTranslations().BASE_ARTICLE,
              count: 0
            },
            {
              id: "embedded",
              label: oTranslations().EMBEDDED,
              count: 0
            },
            {
              id: "fixedBundle",
              label: oTranslations().FIXED_BUNDLE,
              count: 0
            },
            {
              id: "setOfProducts",
              label: oTranslations().SET_OF_PRODUCTS,
              count: 0
            },
            {
              id: "gtin",
              label: oTranslations().GTIN,
              count: 0
            },
          ]
        },
        {
          id: "Attribution Classes",
          label: oTranslations().ATTRIBUTION_CLASSES,
          count: 0,
          children: []
        }
      ]
    },
    {
      id: "taxonomy",
      label: oTranslations().TAXONOMIES,
      isExpanded: false,
      Column: 2,
      children: [
        {
          id: "masterTaxonomy",
          label: oTranslations().MASTER_TAXONOMY_CONFIGURATION_TITLE,
          count: 0,
          children:[
            {
              id: "Total Nodes",
              label: oTranslations().TOTAL_NODES,
              count: 0
            },
          ]
        },
        {
          id: "minorTaxonomy",
          label: oTranslations().MINOR_TAXONOMIES,
          count: 0,
          children:[
            {
              id: "Total Nodes",
              label: oTranslations().TOTAL_NODES,
              count: 0
            },
          ]
        },
      ]
    },
    {
      id: "dataGovernance",
      label: oTranslations().CONFIG_MODULE_DATA_GOVERNANCE,
      isExpanded: false,
      Column: 3,
      children: [
        {
          id: "Rules",
          label: oTranslations().RULES,
          count: 0,
          children: [
            {
              id: "violation",
              label: oTranslations().VIOLATION,
              count: 0
            },
            {
              id: "standardization_and_normalization",
              label: oTranslations().STANDARDISATION_AND_NORMALISATION,
              count: 0
            },
            {
              id: "classification",
              label: oTranslations().CLASSIFICATION,
              count: 0
            },
          ]
        },
        {
          id: "KPIs",
          label: oTranslations().KPI_MENU_ITEM_TILE,
          count: 0,
          children: []
        },
        {
          id: "Golden Record Rules",
          label: oTranslations().GOLDEN_RECORD_MENU_ITEM_TILE,
          count: 0,
          children: []
        },
      ]
    },
    {
      id: "Asset",
      label: oTranslations().DAM_TAB,
      isExpanded: false,
      Column: 1,
      children: [
        {
          id: "Nature Classes",
          label: oTranslations().NATURE_CLASSES,
          count: 0,
          children: [
            {
              id: "imageAsset",
              label: oTranslations().IMAGE,
              count: 0
            },
            {
              id: "documentAsset",
              label: oTranslations().DOCUMENT,
              count: 0
            },
            {
              id: "embedded",
              label: oTranslations().EMBEDDED,
              count: 0
            },
            {
              id: "fileAsset",
              label: oTranslations().FILE,
              count: 0
            },
            {
              id: "technicalImage",
              label: oTranslations().TECHNICAL_IMAGE,
              count: 0
            },
            {
              id: "videoAsset",
              label: oTranslations().VIDEO,
              count: 0
            },
          ]
        },
        {
          id: "Attribution Classes",
          label: oTranslations().ATTRIBUTION_CLASSES,
          count: 0,
          children: []
        }
      ]
    },
    {
      id: "relationship",
      label: oTranslations().RELATIONSHIPS,
      isExpanded: false,
      Column: 2,
      children: [
        {
          id: "relationship",
          label: oTranslations().RELATIONSHIPS,
          count: 0
        },
        {
          id: "lite Relationship",
          label: oTranslations().LITE_RELATIONSHIPS,
          count: 0
        }
      ]
    },
    {
      id: "dataIntegration",
      label: oTranslations().DI,
      isExpanded: false,
      Column: 3,
      children: [
        {
          id: "endpoint",
          label: oTranslations().ENDPOINTS,
          count: 0
        },
        {
          id: "mapping",
          label: oTranslations().MAPPING,
          count: 0
        },
        {
          id: "Workflow",
          label: oTranslations().WORKFLOW,
          count: 0
        }
      ]
    },
    {
      id: "target",
      label: oTranslations().CHANNELS,
      isExpanded: false,
      Column: 1,
      children: [
        {
          id: "Nature Classes",
          label: oTranslations().NATURE_CLASSES,
          count: 0,
          children: [
            {
              id: "market",
              label: oTranslations().CHANNELS,
              count: 0
            },
            {
              id: "embedded",
              label: oTranslations().EMBEDDED,
              count: 0
            },
          ]
        },
        {
          id: "Attribution Classes",
          label: oTranslations().ATTRIBUTION_CLASSES,
          count: 0,
          children: []
        }
      ]
    },
    {
      id: "context",
      label: oTranslations().CONTEXTS,
      isExpanded: false,
      Column: 2,
      children: [
        {
          id: "productVariant",
          label: oTranslations().CONTEXT_TYPES_PRODUCT_VARIANT,
          count: 0
        },
        {
          id: "contextualVariant",
          label: oTranslations().CONTEXT_TYPES_CONTEXTUAL_VARIANT,
          count: 0
        },
        {
          id: "imageVariant",
          label: oTranslations().CONTEXT_TYPES_IMAGE_VARIANT,
          count: 0
        },
        {
          id: "relationshipVariant",
          label: oTranslations().CONTEXT_TYPES_RELATIONSHIP_VARIANT,
          count: 0
        }
      ]
    },
    {
      id: "collabration",
      label: oTranslations().WORKFLOW_WORK_BENCH,
      isExpanded: false,
      Column: 3,
      children: [
        {
          id: "task",
          label: oTranslations().TASKS,
          count: 0,
          children: [
            {
              id: "shared",
              label: oTranslations().SHARED,
              count: 0
            },
            {
              id: "personal",
              label: oTranslations().PERSONAL,
              count: 0
            }
          ],
        }
      ]
    },
    {
      id: "Supplier",
      label: oTranslations().SUPPLIER_TAB,
      isExpanded: false,
      Column: 1,
      children: [
        {
          id: "suppliers",
          label: oTranslations().BUSINESS_PARTNER,
          count: 0
        },
        {
          id: "content_enrichment_agency",
          label: oTranslations().ORGANISATION_TYPE_CONTENT_ENRICHMENT_AGENCY,
          count: 0
        },
        {
          id: "digital_asset_agency",
          label: oTranslations().ORGANISATION_TYPE_DIGITAL_ASSET_AGENCY,
          count: 0
        },
        {
          id: "distributors",
          label: oTranslations().ORGANISATION_TYPE_DISTRIBUTORS,
          count: 0
        },
        {
          id: "marketplaces",
          label: oTranslations().ORGANISATION_TYPE_MARKETPLACES,
          count: 0
        },
        {
          id: "translation_agency",
          label: oTranslations().ORGANISATION_TYPE_TRANSLATION_AGENCY,
          count: 0
        },
        {
          id: "wholesalers",
          label: oTranslations().ORGANISATION_TYPE_WHOLESALERS,
          count: 0
        }
      ]
    },
    {
      id: "PropertyGroup",
      label: oTranslations().PROPERTY_GROUPS_MENU_ITEM_TITLE,
      isExpanded: false,
      Column: 2,
      children: [
        {
          id: "Property Collections",
          label: oTranslations().PROPERTY_COLLECTIONS,
          count: 0
        },
        {
          id: "X-Ray Collections",
          label: oTranslations().X_RAY_COLLECTIONS,
          count: 0
        },
        {
          id: "Tabs",
          label: oTranslations().TABS,
          count: 0
        }
      ]
    },
    {
      id: "business Partners",
      label: oTranslations().PARTNERS,
      isExpanded: false,
      Column: 3,
      children: [
        {
          id: "business Partners",
          label: oTranslations().PARTNERS,
          count: 0
        },
        {
          id: "Role",
          label: oTranslations().ROLES,
          count: 0
        },
        {
          id: "user",
          label: oTranslations().USERS,
          count: 0
        }
      ]
    },
    {
      id: "TextAsset",
      label: oTranslations().TEXT_ASSET,
      isExpanded: false,
      Column: 1,
      children: [
        {
          id: "Nature Classes",
          label: oTranslations().NATURE_CLASSES,
          count: 0,
          children: [
            {
              id: "textAsset",
              label: oTranslations().TEXT_ASSET,
              count: 0
            },
            {
              id: "embedded",
              label: oTranslations().EMBEDDED,
              count: 0
            },
          ]
        },
        {
          id: "Attribution Classes",
          label: oTranslations().ATTRIBUTION_CLASSES,
          count: 0,
          children: []
        }
      ]
    },
    {
      id: "Properties",
      label: oTranslations().PROPERTIES,
      isExpanded: false,
      Column: 2,
      children: [
        {
          id: "CALCULATED",
          label: oTranslations().CALCULATED,
          count: 0
        },
        {
          id: "CONCATENATED",
          label: oTranslations().CONCATENATED,
          count: 0
        },
        {
          id: "DATE",
          label: oTranslations().DATE,
          count: 0
        },
        {
          id: "HTML",
          label: oTranslations().HTML,
          count: 0
        },
        {
          id: "MEASUREMENT",
          label: oTranslations().MEASUREMENT,
          count: 0
        },
        {
          id: "NUMBER",
          label: oTranslations().NUMBER,
          count: 0
        },
        {
          id: "PRICE",
          label: oTranslations().PRICE,
          count: 0
        },
        {
          id: "Standard Attribute",
          label: oTranslations().STANDARD_ATTRIBUTES,
          count: 0
        },
        {
          id: "TEXT",
          label: oTranslations().TEXT,
          count: 0
        },
        {
          id: "Boolean",
          label: oTranslations().BOOLEAN,
          count: 0
        },
        {
          id: "LOV",
          label: oTranslations().LOV,
          count: 0
        },
        {
          id: "Master",
          label: oTranslations().MASTER,
          count: 0
        },
        {
          id: "Status",
          label: oTranslations().STATUS,
          count: 0
        }
      ]
    },
    {
      id: "language",
      label: oTranslations().LANGUAGES,
      isExpanded: false,
      Column: 3,
      children: [
        {
          id: "languages",
          label: oTranslations().LANGUAGES,
          count: 0
        }
      ]
    },
  ]
};