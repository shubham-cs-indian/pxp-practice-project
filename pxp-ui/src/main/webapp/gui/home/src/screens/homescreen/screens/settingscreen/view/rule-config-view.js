
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ContextualGridView } from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';
import { view as RuleDetailView } from './rule-detail-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import NatureTypeDictionary from '../../../../../commonmodule/tack/nature-type-dictionary.js';
import { view as ConfigEntityCreationDialogView } from './config-entity-creation-dialog-view';
import MockDataForRuleTypes from '../../../../../commonmodule/tack/mock-data-for-rule-types';
import RuleTypeDictionary from '../../../../../commonmodule/tack/rule-type-dictionary';
import ViewUtils from './utils/view-utils';

const oEvents = {
  RULE_NATURE_CLASS_ADDED_IN_GOLDEN_RECORD: 'rule_nature_class_added_in_golden_record',
  HANDLE_RULE_CONFIG_DIALOG_BUTTON_CLICKED: "Rule config button clicked"
};
const oPropTypes = {
  activeRule: ReactPropTypes.object,
  attributeMap: ReactPropTypes.object,
  ruleListModel: ReactPropTypes.array,
  ruleListDetailData: ReactPropTypes.array,
  rightPanelData: ReactPropTypes.object,
  ruleEffect: ReactPropTypes.array,
  bRuleCreated: ReactPropTypes.bool,
  oCalculatedAttributeMapping: ReactPropTypes.object,
  classList: ReactPropTypes.array,
  dataForCalculatedAttributes: ReactPropTypes.object,
  selectedKlassType: ReactPropTypes.string,
  multiTaxonomyData: ReactPropTypes.object,
  multiTaxonomyDataForRulesEffect: ReactPropTypes.object,
  taxonomyPaginationData: ReactPropTypes.object,
  physicalCatalogIdsData: ReactPropTypes.array,
  portalIdsData: ReactPropTypes.array,
  lazyMSSReqResInfo: ReactPropTypes.object,
  isRuleDirty: ReactPropTypes.bool,
  entityType: ReactPropTypes.string,
  gridViewProps: ReactPropTypes.object,
  isDialogOpen: ReactPropTypes.bool,
  ruleDetailViewData: ReactPropTypes.object,
  isGridViewColumnOrganiserDialogOpen: ReactPropTypes.bool,
};

// @CS.SafeComponent
class RuleConfigView extends React.Component {
  static propTypes = oPropTypes;

  handleDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.HANDLE_RULE_CONFIG_DIALOG_BUTTON_CLICKED, sButtonId);
  };

  onNatureClassSelection = (sSelectedId) => {
    EventBus.dispatch(oEvents.RULE_NATURE_CLASS_ADDED_IN_GOLDEN_RECORD, sSelectedId);
  };

  getRuleDetailView = () => {
    let __props = this.props;
    let sContext = __props.ruleDetailViewData.entityType;
    let oRuleDetailViewData = __props.ruleDetailViewData;

    if (CS.isEmpty(__props.activeRule) && CS.isEmpty(oRuleDetailViewData)) {
      return null;
    }

    return (
        <RuleDetailView
            activeRule={oRuleDetailViewData.activeRule}
            ruleListDetailData={oRuleDetailViewData.ruleListDetailData}
            rightPanelData={oRuleDetailViewData.rightPanelData}
            ruleEffect={oRuleDetailViewData.ruleEffect}
            oCalculatedAttributeMapping={oRuleDetailViewData.oCalculatedAttributeMapping}
            classList={oRuleDetailViewData.classList}
            dataForCalculatedAttributes={oRuleDetailViewData.dataForCalculatedAttributes}
            selectedKlassType={oRuleDetailViewData.selectedKlassType}
            multiTaxonomyData={oRuleDetailViewData.multiTaxonomyData}
            multiTaxonomyDataForRulesEffect={oRuleDetailViewData.multiTaxonomyDataForRulesEffect}
            taxonomyPaginationData={oRuleDetailViewData.taxonomyPaginationData}
            physicalCatalogIdsData={oRuleDetailViewData.physicalCatalogIdsData}
            portalIdsData={oRuleDetailViewData.portalIdsData}
            lazyMSSReqResInfo={oRuleDetailViewData.lazyMSSReqResInfo}
            isRuleDirty={oRuleDetailViewData.isRuleDirty}
            context={sContext}
            dataLanguages={oRuleDetailViewData.dataLanguages}
        />
    );
  };

  getRuleTypeMssModel =(oActiveRule) => {
    let aRuleTypes = new MockDataForRuleTypes();
    let aRuleType = [oActiveRule.type] || [aRuleTypes[0].id];

    return {
      items: aRuleTypes,
      selectedItems: aRuleType,
      cannotRemove: true,
      context: "rules",
      disabled: false,
      label: "",
      isMultiSelect: false,
      disableCross: true,
      hideTooltip: true
    }
  };

  getDataLanguagesMSSModel = (oActiveRule) => {
    let aDataLanguages = this.props.ruleDetailViewData.dataLanguages;
    let aSelectedLanguagesCode = oActiveRule.languages || [];
    let aSelectedLanguagesId = ViewUtils.getIdsByCode(aDataLanguages, aSelectedLanguagesCode);
    return {
      items: aDataLanguages,
      selectedItems: aSelectedLanguagesId,
      context: "ruleDataLanguages",
      disabled: false,
      isMultiSelect: true,
    }
  };

  getNatureKlassesMSSModel = () => {
    let oReqResObj = {
      requestType: "customType",
      requestURL: "config/klasseslistbybasetype",
      responsePath: ["success", "list"],
      entityName: 'klasses',
      customRequestModel: {
        isNature: true,
        types: ["com.cs.core.config.interactor.entity.klass.ProjectKlass"],
        typesToExclude: [NatureTypeDictionary.GTIN, NatureTypeDictionary.EMBEDDED, NatureTypeDictionary.LANGUAGE]
      }
    };

    let oActiveRule = this.props.ruleDetailViewData.activeRule;
    let aSelectedItems = oActiveRule.klassIds;

    return {
      isMultiSelect: false,
      disabled: false,
      label: getTranslation().NATURE_CLASS,
      selectedItems: aSelectedItems,
      context: 'goldenRecord',
      cannotRemove: true,
      showColor: false,
      onApply: this.onNatureClassSelection,
      showCreateButton: false,
      isLoadMoreEnabled: true,
      requestResponseInfo: oReqResObj,
      referencedData: []
    }
  };

  createRuleDialog = () => {
    let sEntityType = this.props.ruleDetailViewData.entityType;
    let oActiveRule = this.props.ruleDetailViewData.activeRule;
    if (CS.isEmpty(oActiveRule)) {
      return null;
    }

    let oModel = {
      label: oActiveRule.label || "",
      code: oActiveRule.code || ""
    };

    if (sEntityType === 'rule') {
      oModel.ruleType = this.getRuleTypeMssModel(oActiveRule);
      let oRuleTypeDictionary = new RuleTypeDictionary();
      if(oActiveRule.type !== oRuleTypeDictionary.CLASSIFICATION_RULE) {
        oModel.isLanguageDependent = {
          isSelected: oActiveRule.isLanguageDependent,
          context: "rule",
        }
      }

      if (oActiveRule.isLanguageDependent) {
        oModel.dataLanguages = this.getDataLanguagesMSSModel(oActiveRule);
      }
    }
    else if (sEntityType === 'goldenRecordRule') {
      oModel.goldenRecordNatureClass = this.getNatureKlassesMSSModel();
    }

    return (
        <ConfigEntityCreationDialogView
            activeEntity={oActiveRule}
            entityType={sEntityType}
            model={oModel}
        />
    )
  };

  getDetailViewDialog = () => {
    let oProps = this.props;
    if (!oProps.isDialogOpen) {
      return null;
    }

    let oBodyStyle = {
      maxWidth: "1200px",
      minWidth: "800px",
      height: "70%",
      width: "100%"
    };

    let aButtonData = [];
    if (this.props.isRuleDirty) {
      aButtonData = [
        {
          id: "cancel",
          label: getTranslation().DISCARD,
          isDisabled: false,
          isFlat: false,
        },
        {
          id: "save",
          label: getTranslation().SAVE,
          isDisabled: false,
          isFlat: false,
        }];
    } else {
      aButtonData = [{
        id: "ok",
        label: getTranslation().OK,
        isFlat: false,
        isDisabled: false
      }];
    }

    let oRuleDetailViewDOM = this.getRuleDetailView();
    let fButtonHandler = this.handleDialogButtonClicked
    let sSelectedView = this.props.ruleDetailViewData.entityType;
    let sTitle = (sSelectedView === "rule") ? getTranslation().RULE : getTranslation().GOLDEN_RECORD_RULE;

    return (
        <CustomDialogView modal={true}
                          open={true}
                          title={sTitle}
                          bodyStyle={oBodyStyle}
                          contentStyle={oBodyStyle}
                          bodyClassName="ruleConfigDetailBody"
                          className="ruleConfigDetailDialog"
                          contentClassName="ruleConfigDetail"
                          buttonData={aButtonData}
                          onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                          buttonClickHandler={fButtonHandler}
                          children={oRuleDetailViewDOM}>
        </CustomDialogView>);
  }

  render(){
    let oGridViewProps = this.props.gridViewProps;
    let oCreateRuleDialog = this.createRuleDialog();
    let oDetailViewDialog = this.getDetailViewDialog();
    let oColumnOrganizerData = this.props.columnOrganizerData;

    return (
        <div className="configGridViewContainer" key="contextConfigGridViewContainer">
          <ContextualGridView
              context={oGridViewProps.context}
              tableContextId={oGridViewProps.context}
              showCheckboxColumn={oGridViewProps.showCheckboxColumn}
              enableImportExportButton={oGridViewProps.enableImportExportButton}
              selectedColumns={oColumnOrganizerData.selectedColumns}
              isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}
          />
          {oDetailViewDialog}
          {oCreateRuleDialog}
        </div>
    );
  }
}

export const view = RuleConfigView;
export const events = oEvents;
