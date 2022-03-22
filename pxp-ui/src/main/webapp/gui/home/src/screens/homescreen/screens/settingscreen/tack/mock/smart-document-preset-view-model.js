import MockDataForEntityBaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
var SmartDocumentPresetViewModel = function (oPreset, oModelExtraData, sContext) {
  let oPDFConfigurations = oPreset.smartDocumentPresetPdfConfiguration;
  this.label = oPreset.label;
  this.code = oPreset.code;
  this.languageCode = {
    items: oModelExtraData.languageCodes,
    selectedItems: [oPreset.languageCode],
    context: sContext+oModelExtraData.splitter+"languageCode",
    isMultiSelect: false,
    cannotRemove: true,
    disabled: false,
    searchText: oModelExtraData.searchText,
    isLoadMoreEnabled: false,
  };
  this.splitDocument = {
    context: sContext,
    isSelected: oPreset.splitDocument,
    isDisabled: false
  };
  this.saveDocument = {
    context: sContext,
    isSelected: oPreset.saveDocument,
    isDisabled: false
  };
  this.showPreview = {
    context: sContext,
    isSelected: oPreset.showPreview,
    isDisabled: false
  };
  this.smartDocumentPresetPdfConfiguration = {
    pdfAuthor: oPDFConfigurations.pdfAuthor,
    pdfKeywords: oPDFConfigurations.pdfKeywords,
    pdfTitle: oPDFConfigurations.pdfTitle,
    pdfSubject: oPDFConfigurations.pdfSubject,
    pdfUserPassword: oPDFConfigurations.pdfUserPassword,
    pdfOwnerPassword: oPDFConfigurations.pdfOwnerPassword,
    pdfColorSpace: {
      items: oModelExtraData.pdfColorSpace,
      selectedItems: [oPDFConfigurations.pdfColorSpace],
      context: sContext + oModelExtraData.splitter + "pdfColorSpace",
      isMultiSelect: false,
      cannotRemove: false,
      disabled: false,
      searchText: oModelExtraData.searchText,
      isLoadMoreEnabled: false,
    },
    pdfMarksBleeds: {
      items: oModelExtraData.pdfMarksBleeds,
      selectedItems: [oPDFConfigurations.pdfMarksBleeds],
      context: sContext + oModelExtraData.splitter + "pdfMarksBleeds",
      isMultiSelect: false,
      cannotRemove: false,
      disabled: false,
      searchText: oModelExtraData.searchText,
      isLoadMoreEnabled: false,
    },

    pdfAllowAnnotations: {
      context: sContext,
      isSelected: oPDFConfigurations.pdfAllowAnnotations,
      isDisabled: true
    },
    pdfAllowCopyContent: {
      context: sContext,
      isSelected: oPDFConfigurations.pdfAllowCopyContent,
      isDisabled: true
    },
    pdfAllowModifications: {
      context: sContext,
      isSelected: oPDFConfigurations.pdfAllowModifications,
      isDisabled: true
    },
    pdfAllowPrinting: {
      context: sContext,
      isSelected: oPDFConfigurations.pdfAllowPrinting,
      isDisabled: true
    }
  };
  this.smartDocumentPresetSelectionConfiguration = {
    attributes: oPreset.attributeIds,
    tags: oPreset.tagIds,
    configDetails: {
      referencedAttributes: oPreset.referencedAttributes,
      referencedTags: oPreset.referencedTags,
    },
    lazyMSSReqResInfo: {},
  }
  this.smartDocumentPresetFilterConfiguration = {
    taxonomy: oModelExtraData.taxonomyCustomView,
    klassIds: {
      selectedItems: oModelExtraData.klassesData.selectedKlassIds,
      context: sContext+oModelExtraData.splitter+"klassIds",
      isMultiSelect: true,
/*
      fOnApply: SmartDocumentStore.handle
*/
      requestResponseInfo: {
        requestType: "customType",
        responsePath: ["success", "list"],
        requestURL: oModelExtraData.klassesData.url,
        customRequestModel: {
          types: [
            MockDataForEntityBaseTypesDictionary.articleKlassBaseType
            ],
          isStandard: false,
          isNature: null
        }
      },
      referencedData: oModelExtraData.klassesData.referencedData,
    }
  }
};

export default SmartDocumentPresetViewModel;