import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import { view as CustomDatePicker } from '../../../../../viewlibraries/customdatepickerview/customdatepickerview.js';
import { view as TagSelectorView } from '../../../../../viewlibraries/tagselectorview/tag-selector-view';
import { view as ContextualAttributeEditSectionViewSelect } from './contextual-attribute-edit-section-select-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import ViewUtils from './utils/view-utils';
import Constants from '../../../../../commonmodule/tack/constants';
import EntitiesList from '../../../../../commonmodule/tack/entities-list';
import MockDataForContextTypes from '../tack/mock/mock-data-for-context-types';
import ContextTypeDictionary from '../../../../../commonmodule/tack/context-type-dictionary';
import ClassContextDialogLayout from '../tack/class-context-dialog-layout-data';

const oEvents = {
  CLASS_CONTEXT_DIALOG_OPEN_CLICKED: "class_context_dialog_open_clicked",
  CLASS_CONTEXT_DIALOG_OK_CLICKED: "class_context_dialog_ok_clicked",
  CLASS_CONTEXT_DIALOG_CLOSED: "class_context_dialog_closed",
  CLASS_CONTEXT_DIALOG_DATE_VALUE_CHANGED: "class_context_dialog_date_value_changed",
  CLASS_CONTEXT_DIALOG_ADD_TAG_COMBINATION: "class_context_dialog_add_tag_combination",
  CLASS_CONTEXT_DIALOG_DISCARD_CLICKED: "class_context_dialog_discard_clicked",
};

const oPropTypes = {
  context: ReactPropTypes.string,
  classContextData: ReactPropTypes.object,
  classContextDialogData: ReactPropTypes.object,
  tagMap: ReactPropTypes.object,
  activeTagUniqueSelectionId: ReactPropTypes.string,
  classContextTagOrder: ReactPropTypes.array,
  customTabList: ReactPropTypes.array,
  referencedData: ReactPropTypes.object,
  requestResponseInfo: ReactPropTypes.object,
  fieldsToExclude: ReactPropTypes.array,
  iconLibraryData: ReactPropTypes.object
};

// @CS.SafeComponent
class ClassContextDialogView extends React.Component {

  handleDialogButtonClicked = (sButtonId) => {
    if(sButtonId == 'save'){
      this.handleDialogOkClicked();
    }
    else if(sButtonId == 'discard') {
      this.handleDialogDiscardClicked();
    }
    else {
      this.handleCloseDialogClicked();
    }
  };

  handleOpenDialogClicked = () => {
    EventBus.dispatch(oEvents.CLASS_CONTEXT_DIALOG_OPEN_CLICKED);
  };

  handleCloseDialogClicked = () => {
    EventBus.dispatch(oEvents.CLASS_CONTEXT_DIALOG_CLOSED);
  };

  handleDialogOkClicked = () => {
    EventBus.dispatch(oEvents.CLASS_CONTEXT_DIALOG_OK_CLICKED);
  };

  handleDialogDiscardClicked = () => {
    EventBus.dispatch(oEvents.CLASS_CONTEXT_DIALOG_DISCARD_CLICKED);
  };

  handleDateValueChanged = (sKey, sNull, sValue) => {
    let sDateValue = (sKey != "fromCurrentDate") ? new Date(sValue).getTime() : "";
    EventBus.dispatch(oEvents.CLASS_CONTEXT_DIALOG_DATE_VALUE_CHANGED, sKey, sDateValue);
  };

  handleAddTagCombinationClicked = () => {
    EventBus.dispatch(oEvents.CLASS_CONTEXT_DIALOG_ADD_TAG_COMBINATION);
  };

  getDateForCustomDatePicker = (sDefaultFromDate) => {
    sDefaultFromDate = sDefaultFromDate ? +sDefaultFromDate ? new Date(+sDefaultFromDate) : null : null;
    if (CS.isNaN(Date.parse(sDefaultFromDate))) {
      sDefaultFromDate = null;
    }
    return sDefaultFromDate;
  };

  getToDateView = () => {
    let oContextData = this.props.classContextDialogData || {};
    let oDefaultTimeRange = oContextData.defaultTimeRange || {};

    let oCurrentDate = new Date();
    let oDefaultFromDate = this.getDateForCustomDatePicker(oDefaultTimeRange.from);//getDateForCustomDatePicker is returning object
    let oDefaultToDate = this.getDateForCustomDatePicker(oDefaultTimeRange.to);
    if (oDefaultTimeRange.isCurrentTime) {
      oDefaultFromDate = oCurrentDate;
    }

    return (<div className="defaultDateViewWrapper">
      <CustomDatePicker
          value={oDefaultToDate}
          minDate={oDefaultFromDate || undefined}
          allowInfinity={true}
          className="datePickerCustom"
          onChange={this.handleDateValueChanged.bind(this, "to")}
          disabled={oDefaultTimeRange.isCurrentTime}/>
    </div>);
  };

  getFromDateView = () => {
    let oContextData = this.props.classContextDialogData || {};
    let oDefaultTimeRange = oContextData.defaultTimeRange || {};
    let sDefaultFromDate = oDefaultTimeRange.from;
    let sDefaultToDate = oDefaultTimeRange.to;

    sDefaultToDate = this.getDateForCustomDatePicker(sDefaultToDate);
    sDefaultFromDate = this.getDateForCustomDatePicker(sDefaultFromDate);

    let bIsCurrentTime = oDefaultTimeRange.isCurrentTime;
    let sClassForCurrentDateEnabled = bIsCurrentTime ? ' noTextBoxForDefaultDate' : '';

    return (<div className="defaultDateViewWrapper">
      <CustomDatePicker
          value={sDefaultFromDate}
          disabled={bIsCurrentTime}
          maxDate={sDefaultToDate || undefined}
          allowInfinity={false}
          className="datePickerCustom"
          onChange={this.handleDateValueChanged.bind(this, "from")}/>
      <div className={"defaultCurrentDateSelectorWrapper" + sClassForCurrentDateEnabled}
           onClick={this.handleDateValueChanged.bind(this, "fromCurrentDate")}>
        <input type="checkbox" checked={bIsCurrentTime}/>
        <span className="useCurrentDefaultDateLabel">{getTranslation().USE_DEFAULT_DATE}</span>
      </div>
    </div>);
  };

  getTagCombinationViewData = () => {
    let oClassContextDialogData = this.props.classContextDialogData;
    let aContextTagOrder = this.props.classContextTagOrder;
    let sActiveUniqueSelectorId = this.props.activeTagUniqueSelectionId;

    return {
      tags: oClassContextDialogData.contextTags,
      tagOrder: aContextTagOrder,
      uniqueSelectors: oClassContextDialogData.uniqueSelectors,
      activeUniqueSelector: sActiveUniqueSelectorId
    };
  };

  getTagCombinationView = () => {
    let _props = this.props;
    let sContext =_props.context;
    let oContextData = _props.classContextDialogData || {};
    let aSelectedTagsData = oContextData.contextTags;
    let bIsAutoCreate = oContextData.isAutoCreate;

    if(!bIsAutoCreate || CS.isEmpty(aSelectedTagsData)){
      return null;
    }

    return (<div className="tagPossibleCombinationViewWrapper">
      <div className="tagCombinationButtonContainer">
        <div className="tagCombinationAddButton" onClick={this.handleAddTagCombinationClicked}></div>
      </div>
      <div className="tagPossibleCombinationViewBody">
        <ContextualAttributeEditSectionViewSelect viewData={this.getTagCombinationViewData()}
                                                  context={sContext}/>
      </div>
    </div>)
  };

  getTagSelectorView = () => {
    let _props = this.props;
    let oPaginationData = ViewUtils.getEntityPaginationData();
    let sSearchText = ViewUtils.getEntitySearchText();
    let oTagMap = _props.tagMap;
    let sContext = _props.context;
    let oContextData = _props.classContextDialogData || {};
    let aSelectedTagsData = oContextData.contextTags;


    return (<TagSelectorView
        selectedTagsData={aSelectedTagsData}
        tagMap={oTagMap}
        context={sContext}
        searchText={sSearchText}
        paginationData={oPaginationData}
        isTagValueListSearchEnabled = {true}
        isTagValueListLoadMoreEnabled = {true}
        referencedData={this.props.referencedData}
        requestResponseInfo={this.props.requestResponseInfo}
        isSingleSelect={oContextData.type === ContextTypeDictionary.LANGUAGE_VARIANT}
    />);
  };

  getCustomTabModel = () => {
    let _props = this.props;
    let oContextData = _props.classContextDialogData || {};
    let sSearchText = ViewUtils.getEntitySearchText();

    let sSelectedTabId = (oContextData.tab && oContextData.tab.id) || oContextData.tabId;
    let aCustomTabList = _props.customTabList;

    let oReferencedData = !CS.isEmpty(oContextData.configDetails) ? oContextData.configDetails.referencedTabs : {};
    let oCustomTabList = CS.keyBy(aCustomTabList, "id");
    CS.assign(oReferencedData, oCustomTabList);

    let oSelectedTab = oReferencedData[sSelectedTabId];
    if(CS.isEmpty(oSelectedTab) && oContextData.tab){
      oReferencedData[oContextData.tab.id] = oContextData.tab;
      aCustomTabList.push(oContextData.tab);
      oSelectedTab = oContextData.tab;
    }

    let aSelectedTabId = CS.isEmpty(sSelectedTabId) ? [] : [sSelectedTabId];
    let aSelectedTabObject = CS.isEmpty(oSelectedTab) ? [] : [oSelectedTab];

    let oReqResObj = {
      requestType: "configData",
      entityName: "tabs",
      typesToExclude: [Constants.TAB_RENDITION],
    };

    return ({
      isMultiSelect: false,
      items: aCustomTabList,
      selectedItems: aSelectedTabId,
      selectedObject: aSelectedTabObject,
      searchText: sSearchText,
      showCreateButton: true,
      context: "tab",
      isLoadMoreEnabled: true,
      referencedData: oReferencedData,
      requestResponseInfo: oReqResObj,
      cannotRemove: true
    });
  }

  getCommonConfigData = () => {
    let aEntitiesList = EntitiesList();
    let oContextData = this.props.classContextDialogData || {};
    let sContext = this.props.context;
    let sLabel = CS.getLabelOrCode(oContextData);
    let aSelectedEntities = oContextData.entities || [];
    let bIsAutoCreate = oContextData.isAutoCreate;
    let oDefaultFromDateView = this.getFromDateView();
    let oDefaultToDateView = this.getToDateView();
    let oTagSelectorView = this.getTagSelectorView();
    let oTagCombinationView = this.getTagCombinationView();
    let oCustomTabModel = this.getCustomTabModel();

    let oIsTimeEnabled = null;
    let oDefaultFromDate = null;
    let oDefaultToDate = null;

    if (sContext !== "languageKlassContext") {
      oIsTimeEnabled = {
        context: sContext,
        isSelected: oContextData.isTimeEnabled
      };
      oDefaultFromDate = oContextData.isTimeEnabled ? oDefaultFromDateView : null;
      oDefaultToDate = oContextData.isTimeEnabled ? oDefaultToDateView : null
    }

    let oIsDuplicateVariantAllowed = {
      context: sContext,
      isSelected: oContextData.isDuplicateVariantAllowed
    };

    let oIsAutoCreate= {
      context: sContext,
      isSelected: bIsAutoCreate
    };

    let oEntitiesModel = {
      context: sContext,
      singleSelect: false,
      items: aEntitiesList,
      selectedItems: aSelectedEntities
    };
    //For tecnical image variant context we restrict user to select entities.
    oEntitiesModel = (sContext == "technicalImageContext") ? null : oEntitiesModel;

    let oContextTypeModel = {
      disabled: true,
      label: "",
      items: new MockDataForContextTypes(),
      selectedItems: [oContextData.type],
      singleSelect: true,
      context: sContext,
      disableCross: true
    };

    let oIcon = {
      context: sContext,
      icon: oContextData.icon ? oContextData.icon: "",
      iconKey: oContextData.iconKey,
    };
    let oIsLimitedObject = null;

    return {
      label: sLabel,
      type: oContextTypeModel,
      tab: oCustomTabModel,
      isLimitedObject: oIsLimitedObject,
      isTimeEnabled: oIsTimeEnabled,
      isDuplicateVariantAllowed: oIsDuplicateVariantAllowed,
      isAutoCreate: oIsAutoCreate,
      defaultFromDate: oDefaultFromDate,
      defaultToDate: oDefaultToDate,
      entities: oEntitiesModel,
      tagSelector: oTagSelectorView,
      tagCombinationView: oTagCombinationView,
      icon: oIcon,
      showSelectIconDialog: oContextData.showSelectIconDialog,
      selectIconData: this.props.iconLibraryData
    };
  };

  getMainSectionView = () => {
    let oCommonConfigData = this.getCommonConfigData();
    CS.forEach(this.props.fieldsToExclude, function (sField) {
      delete oCommonConfigData[sField];
    });
    let sContext = this.props.context;
    let oClassContextDialogData = this.props.classContextDialogData;
    let oClassContextDialogLayout = new ClassContextDialogLayout();
    let aDisabledFields = [];
    if ((oClassContextDialogData && oClassContextDialogData.allSectionsDisabled) /*|| (oClassContextDialogData && oClassContextDialogData.isContextUsed)*/) {
      aDisabledFields = ViewUtils.getAllKeysFromSectionLayout(oClassContextDialogLayout);
    } else if (oClassContextDialogData && oClassContextDialogData.disabledSections) {
      aDisabledFields = oClassContextDialogData.disabledSections;
    }

    return (<div className="classContextDialogMainSection">
      <div className="classContextDialogSectionContainer">
        <CommonConfigSectionView
            context={sContext}
            data={oCommonConfigData}
            sectionLayout={oClassContextDialogLayout}
            disabledFields={aDisabledFields}
        />
      </div>
    </div>);
  };

  getContextLabelAndButtonView = () => {
    let oClassContextData = this.props.classContextData;
    let sButtonClassName = !CS.isEmpty(oClassContextData.id) ? "classContextDialogOpenButton editButton" : "classContextDialogOpenButton createButton";
    let sLabel = !CS.isEmpty(oClassContextData.id) ? CS.getLabelOrCode(oClassContextData) : getTranslation().CONTEXT_CREATE_MENU_ITEM_TITLE;

    return(<div className="classContextDialogContextLabelAndButtonContainer" onClick={this.handleOpenDialogClicked}>
      <TooltipView label={sLabel}>
        <div className="classContextLabel">{sLabel}</div>
      </TooltipView>
      <div className={sButtonClassName}></div>
    </div>);
  };

  render() {

    let bIsDialogOpen = this.props.classContextDialogData.isDialogOpen;
    let oClassContextData = this.props.classContextData;

    let oBodyStyle = {
      padding: 0,
      maxHeight: 'none',
      overflowY: "auto",
    };

    let oContentStyle = {
      height: "90%",
      maxHeight: "none",
      width: '80%',
      maxWidth: 'none'
    };
    let aButtonData = [];
    if(this.props.classContextDialogData.isDirty) {
      aButtonData = [
        {
          id: "discard",
          label: getTranslation().DISCARD,
          isDisabled: false,
          isFlat: false,
        },
        {
          id: "save",
          label: getTranslation().SAVE,
          isDisabled: false,
          isFlat: false,
        }
      ];
    } else {
      aButtonData = [
        {
          id: "ok",
          label: getTranslation().OK,
          isDisabled: false,
          isFlat: false,
        }
      ];

    }
    let fButtonHandler = this.handleDialogButtonClicked;
    let sTitle = !CS.isEmpty(oClassContextData.id) ? getTranslation().EDIT : getTranslation().CREATE;

    return (
        <div className="classContextDialogViewContainer">
          {this.getContextLabelAndButtonView()}
          <CustomDialogView modal={true} open={bIsDialogOpen}
                            title={sTitle}
                            bodyStyle={oBodyStyle}
                            buttonData={aButtonData}
                            contentStyle={oContentStyle}
                            onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                            buttonClickHandler={fButtonHandler}>
            <div className="classContextDialogContentContainer">
              {this.getMainSectionView()}
            </div>
          </CustomDialogView>
        </div>
    );
  }
}

export const view = ClassContextDialogView;
export const events = oEvents;
