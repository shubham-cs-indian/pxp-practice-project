

import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';

var oConfigElementProperties = function () {
  return  [
   /* {
      id: "isFilterable",
      label: oTranslations().IS_FILTERABLE,
      type: "checkbox"
    },
    {
      id: "isSortable",
      label: oTranslations().IS_SORTABLE,
      type: "checkbox"
    },*/
    {
      id: "defaultValue",
      label: oTranslations().DEFAULT_VALUE,
      className: "defaultValueWidth",
      type: "userDefined",
      width: 200
    },
    {
      id: "selectedTagValues",
      label: oTranslations().ALLOWED_VALUES,
      className: "defaultValueWidth",
      type: "userDefined",
      width: 200
    },
    {
      id: "precision",
      label: oTranslations().PRECISION,
      className: "precision",
      type: "mss",
      width: 70
    },
    {
      id: "couplingType",
      label: oTranslations().COUPLING,
      className: "couplingType",
      type: "mss",
      width: 140
    },
    /*{
      id: "isMandatory",
      label: oTranslations().MANDATORY,
      type: "checkbox"
    },*/
    {
      id: "mandatory",
      label: oTranslations().MANDATORY,
      className: "mandatory",
      type: "mss",
      width: 80
    },
    {
      id: "isSkipped",
      label: oTranslations().SKIP,
      className: "isSkipped",
      type: "checkbox",
      width: 50
    },
    {
      id: "propertyType",
      label: oTranslations().TYPE,
      className: "propertyType",
      type: "mss",
      width: 120
    },
    {
      id: "isMultiselect",
      label: oTranslations().MULTISELECT,
      className: "isMultiselect",
      type: "checkbox",
      width: 90
    },
    // {
    //   id :"isVariating",
    //   label: oTranslations().IS_VARIATING,
    //   type: "yesno",
    //   className : "isVariating"
    // }

    {
      id: "attributeVariantContext",
      label: oTranslations().ATTRIBUTE_CONTEXT_VARIANT,
      className: "mssWidth",
      type: "lazyMSS",
      width: 150
    },
    {
      id: "prefix",
      label: oTranslations().PREFIX,
      className: "prefixValueWidth",
      type: "text",
      width: 200
    },
    {
      id: "suffix",
      label: oTranslations().SUFFIX,
      className: "suffixValueWidth",
      type: "number",
      width: 200
    },
    {
      id: "isIdentifier",
      label: oTranslations().PRODUCT_IDENTIFIER,
      className: "productIdentifier",
      type: "yesno",
      width: 150
    },
    {
      id: "isTranslatable",
      label: oTranslations().LANGUAGE_DEPENDENT,
      className: "isTranslatable",
      type: "yesno",
      width: 150
    },
    {
      id: "isVersionable",
      label: oTranslations().VERSIONABLE,
      className: "isVersionable",
      type: "yesno",
      width: 150
    }
  ];
};

var oRuntimeTaxonomyElementProperties = function () {
  return [
    {
      id: "defaultValue",
      label: oTranslations().DEFAULT_VALUE,
      className: "defaultValueWidth",
      type: "userDefined"
    },
    {
      id: "couplingType",
      label: oTranslations().COUPLING,
      type: "mss"
    }
  ];
}

export default {
  configElementProperties : oConfigElementProperties,
  runtimeTaxonomyElementProperties : oRuntimeTaxonomyElementProperties
}