import CS from '../../../../../libraries/cs';
import ReactPropTypes from 'prop-types';
import React, { useState } from 'react'
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import {view as ChipsView} from './../../../../../viewlibraries/chipsView/chips-view';
import {view as CustomDialogView} from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import {view as SimpleSearchBarView} from './../../../../../viewlibraries/simplesearchbarview/simple-search-bar-view';
import ViewUtils from './utils/view-utils';
import {view as ImageFitToContainerView} from "../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view";
import TooltipView from "../../../../../viewlibraries/tooltipview/tooltip-view";
import ContextTypeDictionary from '../../../../../commonmodule/tack/context-type-dictionary';
import {view as ContextMenuViewNew} from "../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new";
import ContextMenuViewModel from "../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model";
import { view as NothingFoundView } from './../../../../../viewlibraries/nothingfoundview/nothing-found-view';

import {view as NumberLocaleView} from "../../../../../viewlibraries/numberlocaleview/number-locale-view";

let getTranslation = ScreenModeUtils.getTranslationDictionary;

const MAX_CLONE_COUNT = 366;

const oEvents = {
  CLONE_WIZARD_VIEW_CREATE_CLONE_BUTTON_CLICKED: "clone_wizard_view_create_clone_button_clicked",
  CLONE_WIZARD_VIEW_CANCEL_CLONING_BUTTON_CLICKED: "clone_wizard_view_cancel_cloning_button_clicked",
  CLONE_WIZARD_VIEW_EXPAND_SECTION_TOGGLED: "clone_wizard_view_expand_section_toggled",
  CLONE_WIZARD_VIEW_ENTITY_CHECKBOX_CLICKED: "clone_wizard_view_entity_checkbox_clicked",
  CLONE_WIZARD_VIEW_ENTITY_GROUP_CHECKBOX_CLICKED: "clone_wizard_view_entity_group_checkbox_clicked",
  CLONE_WIZARD_VIEW_EXACT_CLONE_CHECKBOX_CLICKED: "clone_wizard_view_exact_clone_checkbox_clicked",
  CLONE_WIZARD_VIEW_GET_ALLOWED_TYPES_TO_CREATE_LINKED_VARIANT: "clone_wizard_view_get_allowed_types_to_create_linked_variant",
  CLONE_WIZARD_VIEW_SELECT_TYPE_TO_CREATE_LINKED_VARIANT: "clone_wizard_view_select_type_to_create_linked_variant",
  CLONE_WIZARD_VIEW_CLONE_COUNT_CHANGED: "clone_wizard_view_clone_count_changed"
};

const oPropTypes = {
  context: ReactPropTypes.string,
  cloningWizardViewData: ReactPropTypes.array,
  isCloningWizardOpen: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object.isRequired,
  selectedIdsForCloningWizardData: ReactPropTypes.object,
  isExactCloneSelected: ReactPropTypes.bool,
  showCloneCountField: ReactPropTypes.bool,
  cloneCount: ReactPropTypes.string
};

const CloneWizardView = (oProps) => {

  const [searchString, setSearchString] = useState("");
  const [showPopOver, setShowPopOver] = useState("");

  const handleGetAllowedTypesToCreateLinkedVariant = () => {
    setShowPopOver(true);
    EventBus.dispatch(oEvents.CLONE_WIZARD_VIEW_GET_ALLOWED_TYPES_TO_CREATE_LINKED_VARIANT);
  };

  const handleSelectTypeToCreateLinkedVariant = (sIdToRemove, oSelectedTypeModel) => {
    EventBus.dispatch(oEvents.CLONE_WIZARD_VIEW_SELECT_TYPE_TO_CREATE_LINKED_VARIANT, sIdToRemove, oSelectedTypeModel.id);
  };

  const handleCreateCloneButtonClicked = () => {
    setSearchString("");
    EventBus.dispatch(oEvents.CLONE_WIZARD_VIEW_CREATE_CLONE_BUTTON_CLICKED, oProps.context, oProps.filterContext);
  };

  const handleCancelCloneButtonClicked = () => {
    setSearchString("");
    EventBus.dispatch(oEvents.CLONE_WIZARD_VIEW_CANCEL_CLONING_BUTTON_CLICKED, oProps.context);
  };

  const handleExpandSectionToggled = (sTypeId) => {
    EventBus.dispatch(oEvents.CLONE_WIZARD_VIEW_EXPAND_SECTION_TOGGLED, sTypeId);
  };

  const handleSearchChanged = (sNewSearchString) => {
    setSearchString(sNewSearchString);
  };

  const handleCheckboxClick = (sId, sGroupId, sContext, bIsDisabled = false) => {
    if (!bIsDisabled) {
      EventBus.dispatch(oEvents.CLONE_WIZARD_VIEW_ENTITY_CHECKBOX_CLICKED, sId, sGroupId, sContext);
    }
  };

  const handleHeaderCheckBoxClicked = (id) => {
    EventBus.dispatch(oEvents.CLONE_WIZARD_VIEW_ENTITY_GROUP_CHECKBOX_CLICKED, id);
  };

  const handleButtonClick = (sButtonId) => {
    if (sButtonId === "save") {
      handleCreateCloneButtonClicked();
    }
    else {
      handleCancelCloneButtonClicked();
    }
  };

  const handleExactCloneClick = () => {
    EventBus.dispatch(oEvents.CLONE_WIZARD_VIEW_EXACT_CLONE_CHECKBOX_CLICKED);
  };

  const handleCloneCountChanged = (sValue) => {
    if (sValue > MAX_CLONE_COUNT) {
      sValue = MAX_CLONE_COUNT;
    } else if (sValue < 1) {
      sValue = 1;
    }
    EventBus.dispatch(oEvents.CLONE_WIZARD_VIEW_CLONE_COUNT_CHANGED, sValue);
  };

  const getCloneWizardHeaderView = () => {
    let sCloneLabel = oProps.productLabelToBeCloned;
    let bIsExactCloneSelected = oProps.isExactCloneSelected;
    let bHideExactCloneOption = (oProps.context === ContextTypeDictionary.PRODUCT_VARIANT);

    if(bHideExactCloneOption) {
      return null;
    }

    let sExactCloneClassName = bIsExactCloneSelected ? "cloneWizardCheckButton checkedItem" : "cloneWizardCheckButton";
    return (
        <div className="cloneHeaderView">
          <div className="cloneHeaderLabel">{sCloneLabel}</div>
          <div className="cloneHeaderItemContainer">
            <div className="exactCloneView">
              <div className={sExactCloneClassName} onClick={handleExactCloneClick}></div>
              <div className="exactCloneLabel">{getTranslation().EXACT_CLONE}</div>
            </div>
            {getCloneCountView()}
          </div>
        </div>
    );
  }

  const getContextMenuViewModel = (oSelectedClass) => {
    let aAllowedTypes = oProps.allowedTypesToCreateLinkedVariant;
    let aTypeModels = [];
    CS.forEach(aAllowedTypes, function (oType) {
      if(oType.id !== oSelectedClass.id) {
        let oProperties = {
          context: 'allowedTypesToCreateLinkedVariant'
        };
        if (CS.isEmpty(oType.icon)) {
          oProperties.customIconClassName = oType.type
        }
        aTypeModels.push(new ContextMenuViewModel(
            oType.id,
            CS.getLabelOrCode(oType),
            false,
            oType.iconKey,
            oProperties
        ));
      }
    });

    return aTypeModels;
  };

  const getNatureClassView = (oClassificationData) => {
    let sLabelClass = "itemLabel ";
    let sSrcURL = ViewUtils.getIconUrl(oClassificationData.icon);

    return (
        <ContextMenuViewNew
            contextMenuViewModel={getContextMenuViewModel(oClassificationData)}
            showCustomIcon={true}
            showPopover={showPopOver}
            onClickHandler={handleSelectTypeToCreateLinkedVariant.bind(this, oClassificationData.id)}>
          <div className="chipsView" onClick={handleGetAllowedTypesToCreateLinkedVariant}>
            <div className="items">
              <ImageFitToContainerView imageSrc={sSrcURL}/>
              <TooltipView label={CS.getLabelOrCode(oClassificationData)} placement="bottom">
                <div className={sLabelClass}>{CS.getLabelOrCode(oClassificationData)}</div>
              </TooltipView>
            </div>
          </div>
        </ContextMenuViewNew>
    )
  };

  const getClassificationViewForClasses = (oClassificationGroup) => {
    let aListNodeView = [];
    let _this = this;
    let oSelectedClassificationIds = oProps.selectedIdsForCloningWizardData;
    let bIsExactCloneSelected = oProps.isExactCloneSelected;
    let bIsProductVariant = oProps.context === ContextTypeDictionary.PRODUCT_VARIANT;

    CS.forEach(oClassificationGroup, function (oClassificationData, sSelectedClassOrTaxonomyId) {
      let bIsNature = oClassificationData.isNature;
      let sClassName = "listGroupItem ";

      if (bIsExactCloneSelected) {
        sClassName += " isDisabled ";
      }

      if(bIsNature) {
        sClassName += " isNatureClass isDisabled ";
        if(bIsProductVariant) {
          sClassName += "dropDown ";
        }
      }

      let oClassificationView = (bIsNature && bIsProductVariant) ? getNatureClassView(oClassificationData) : <ChipsView items={[oClassificationData]}/>;
      let bIsSelected = CS.includes(oSelectedClassificationIds.classes, sSelectedClassOrTaxonomyId);
      let sCheckboxClassName = bIsSelected ? "listGroupItemCheckbox checkedItem" : "listGroupItemCheckbox";
      let bIsDisabled = (bIsExactCloneSelected || bIsNature && !bIsProductVariant);

      aListNodeView.push(
          <div className={sClassName} key={sSelectedClassOrTaxonomyId}>
            <div className={sCheckboxClassName}
                 onClick={handleCheckboxClick.bind(_this, sSelectedClassOrTaxonomyId, "classes", "classes", bIsDisabled)}>
            </div>
            {oClassificationView}
          </div>
      )
    });

    return (
        <div className="listGroupContainer">
          <div className="listGroupHeader">
            <div className="listGroupHeaderLabel">{getTranslation().CLASSES}</div>
          </div>
          <div className="listGroup">
            {aListNodeView}
          </div>
        </div>
    )
  };

  const getClassificationViewForTaxonomies = (oClassificationGroup, sTaxonomyType, sGroupHeader) => {
    let aListNodeView = [];
    let _this = this;
    let oSelectedClassificationIds = oProps.selectedIdsForCloningWizardData;
    let bIsExactCloneSelected = oProps.isExactCloneSelected;

    CS.forEach(oClassificationGroup, function (aClassificationData, sSelectedClassOrTaxonomyId) {
      let bIsSelected = CS.includes(oSelectedClassificationIds[sTaxonomyType], sSelectedClassOrTaxonomyId);
      let sCheckboxClassName = bIsSelected ? "listGroupItemCheckbox checkedItem " : "listGroupItemCheckbox ";
      bIsExactCloneSelected && (sCheckboxClassName += "isDisabled ");

      aListNodeView.push(
          <div className={"listGroupItem"} key={sSelectedClassOrTaxonomyId}>
            <div className={sCheckboxClassName}
                 onClick={handleCheckboxClick.bind(_this, sSelectedClassOrTaxonomyId, sTaxonomyType, sTaxonomyType, bIsExactCloneSelected)}>
            </div>
            <ChipsView items={aClassificationData}/>
          </div>
      )
    });

    return (
        <div className="listGroupContainer">
          <div className="listGroupHeader">
            <div className="listGroupHeaderLabel">{sGroupHeader}</div>
          </div>
          <div className="listGroup">
            {aListNodeView}
          </div>
        </div>
    )
  };

  const getClassAndTaxonomiesView = () => {
    let oCloneData = oProps.cloningWizardViewData;

    return (
        <div className="classificationSelectionView">
            {getClassificationViewForClasses(oCloneData.classes)}
            {getClassificationViewForTaxonomies(oCloneData.taxonomies, "taxonomies", getTranslation().TAXONOMIES)}
        </div>
    )
  }

  const getPropertiesView = (oPropertiesGroup, bIsExpanded) => {
    let aPropertiesView = [];
    let sSearchString = searchString;
    let _this = this;
    let oSelectedIdsForCheckBox = oProps.selectedIdsForCloningWizardData;
    let oSelectedId = oSelectedIdsForCheckBox.properties;
    let bIsExactCloneSelected = oProps.isExactCloneSelected;

    CS.forEach(oPropertiesGroup.data, function (oProperty, sSelectedPropertiesId) {
      if (CS.isEmpty(oProperty)) {
        return;
      }

      let sIconClassName = "listGroupItemIcon " + oProperty.type;
      let bIsSelected = CS.includes(oSelectedId[oPropertiesGroup.id], sSelectedPropertiesId);
      let sCheckboxClassName = bIsSelected ? "listGroupItemCheckbox checkedItem " : "listGroupItemCheckbox ";
      bIsExactCloneSelected && (sCheckboxClassName += "isDisabled ");

      let oPropertyText = null;
      if (!CS.isEmpty(sSearchString)) {
        if(CS.getLabelOrCode(oProperty).toLowerCase().indexOf(sSearchString.toLowerCase()) == -1) {
          return;
        }
        oPropertyText = ViewUtils.getHighlightedHeaderText(CS.getLabelOrCode(oProperty), sSearchString, "cloneWizardHighlightedText");
      }

      let sPropertyClassLabelName = !CS.isEmpty(sSearchString) ? "listGroupItemLabel highLightedText" : "listGroupItemLabel";

      let oSearchStringOrLabel = CS.isEmpty(oPropertyText) ? CS.getLabelOrCode(oProperty) : oPropertyText;
      let sImageSrc = ViewUtils.getIconUrl(oProperty.iconKey);
      aPropertiesView.push(
          <div className={"listGroupItem"}>
            <div className={sCheckboxClassName}
                 onClick={handleCheckboxClick.bind(_this, sSelectedPropertiesId, oPropertiesGroup.id, "properties", bIsExactCloneSelected)}>
            </div>
            <div className="customIcon">
              <ImageFitToContainerView imageSrc={sImageSrc}/>
            </div>
            <div className={sPropertyClassLabelName}>{oSearchStringOrLabel} </div>
            <div className={sIconClassName}></div>
          </div>
      )
    });

    if (!bIsExpanded && !CS.isEmpty(aPropertiesView)) {
      aPropertiesView = [];
    }

    return aPropertiesView;
  };

  const getCheckboxStatus = (iSizeOfGroup, iSizeOfSelectedIdsOfGroup) => {
    if (iSizeOfGroup === iSizeOfSelectedIdsOfGroup) {
      return 1; /** whole group selected **/
    }
    else if(iSizeOfSelectedIdsOfGroup === 0) {
      return 0; /** nothing is selected in group **/
    }
    else if(iSizeOfSelectedIdsOfGroup > 0 && iSizeOfSelectedIdsOfGroup < iSizeOfGroup) {
      return -1; /** partial group selected **/
    }
  };

  const getPropertyView = () => {
    let _this = this;
    let oCloneData = oProps.cloningWizardViewData;
    let oPropertiesCollectionGroupList = oCloneData.properties;
    let aPropertiesGroupView = [];
    let bIsAnyPropertyIsExpanded = false;
    let sSearchString = searchString;
    let oSelectedIdsForCheckBox = oProps.selectedIdsForCloningWizardData;
    let bIsExactCloneSelected = oProps.isExactCloneSelected;

    CS.forEach(oPropertiesCollectionGroupList, (oPropertyCollection, sKey) => {
      let bIsExpanded = oPropertyCollection.isExpanded;
      let iSizeOfGroup = CS.size(oPropertyCollection.data);
      let iSizeOfSelectedIdsOfGroup = oSelectedIdsForCheckBox.properties[sKey].length;
      let iCheckboxStatus = getCheckboxStatus(iSizeOfGroup, iSizeOfSelectedIdsOfGroup);
      let sExpandIconClassName = bIsExpanded ? "listGroupExpandIcon " : "listGroupExpandIcon isCollapsed";
      let sCheckedBoxClassName = "listGroupCheckbox ";
      let sPropertyCollectionLabel = CS.getLabelOrCode(oPropertyCollection);

      if(iCheckboxStatus == 1) {
        sCheckedBoxClassName += bIsExactCloneSelected ? "checkedItem isDisabled" : "checkedItem";
      }
      else if (iCheckboxStatus == -1){
        sCheckedBoxClassName += "checkedSomeItem ";
      }

      bIsAnyPropertyIsExpanded = true;

      let oPropertyView = getPropertiesView(oPropertyCollection, bIsExpanded);
      let oHeaderText = null;

      if (CS.isEmpty(oPropertyView) && bIsExpanded) {
        oPropertyView = <div className="listGroupNoItem">{getTranslation().DATA_NOT_FOUND}</div>;
      }
      if (!CS.isEmpty(sSearchString)) {
        oHeaderText = ViewUtils.getHighlightedHeaderText(sPropertyCollectionLabel, sSearchString, "cloneWizardHighlightedText");
      }
      let oSearchStringOrLabel = CS.isEmpty(oHeaderText) ? sPropertyCollectionLabel : oHeaderText;

      aPropertiesGroupView.push(
          <div className={"listGroupContainer"}>
            <div className="listGroupHeader">
              <div className={sCheckedBoxClassName} onClick={handleHeaderCheckBoxClicked.bind(_this, sKey)}></div>
              <div className="listGroupHeaderLabel">{oSearchStringOrLabel}</div>
              <div className={sExpandIconClassName} onClick={handleExpandSectionToggled.bind(_this, sKey)}></div>
            </div>
            <div className="listGroup">
              {oPropertyView}
            </div>
          </div>
      )
    });

    let oView = CS.isEmpty(aPropertiesGroupView) ? <NothingFoundView message={getTranslation().NO_MATCHES_FOUND}/> : aPropertiesGroupView;

    return (
        <div className="propertiesSelectionView">
          {getSearchBarView()}
          <div className="propertiesGroupContainer">
              {oView}
          </div>
        </div>
    )
  };

  const getSearchBarView = () => {

    return (<div className="cloneWizardSearchBarContainer">
      <div className="cloneWizardSearchBar">
        <SimpleSearchBarView onChange={handleSearchChanged} searchText={searchString} placeHolder={""}/>
      </div>
    </div>);
  };

  const getCloneCountView = () => {
    if (oProps.showCloneCountField) {
      return (
          <div className="cloneWizardCloneCountContainer">
            <div className="cloneWizardCloneCountLabel">{getTranslation().NO_OF_CLONES_TO_CREATE}</div>
            <NumberLocaleView  minValue={1} maxValue={MAX_CLONE_COUNT}
                               value={oProps.cloneCount} onBlur={handleCloneCountChanged.bind(this)} label={getTranslation().NO_OF_CLONES_TO_CREATE} precision={0}/>
          </div>
      );
    }
  };

  const getCloneWizardView = () => {
    let sCloneWizardClassName = oProps.context === ContextTypeDictionary.PRODUCT_VARIANT ? "cloneWizardBody productVariant" : "cloneWizardBody";
    return (
        <div className="cloneWizardView">
          {getCloneWizardHeaderView()}
          <div className={sCloneWizardClassName}>
            {getClassAndTaxonomiesView()}
            {getPropertyView()}
          </div>
        </div>
    );
  }

  const getDialogData = () => {
    let oBodyStyle = {
      maxHeight: 'none',
      overflowY: "auto",
    };

    let oContentStyle = {
      height: "80%",
      maxHeight: "none",
      width: '80%',
      maxWidth: 'none'
    };

    let sLabel = getTranslation().APPLY;
    if (oProps.context.type === 'productVariant') {
      sLabel = getTranslation().CREATE;
    }

    var aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true
      },
      {
        id: "save",
        label: sLabel,
        isFlat: false
      }
    ];

    return {
      bodyStyle: oBodyStyle,
      contentStyle: oContentStyle,
      buttonData: aButtonData
    }
  }

    let bIsCloningWizardOpen = oProps.isCloningWizardOpen;
    let oDialogData = getDialogData();
    let sTitle = oProps.context === ContextTypeDictionary.PRODUCT_VARIANT ? getTranslation().CREATE_VARIANT : getTranslation().CREATE_CLONE;
    let fButtonHandler = handleButtonClick;

    return (
        <CustomDialogView modal={false} open={bIsCloningWizardOpen}
                          bodyStyle={oDialogData.bodyStyle}
                          contentStyle={oDialogData.contentStyle}
                          buttonData={oDialogData.buttonData}
                          onRequestClose={fButtonHandler.bind(this, oDialogData.buttonData[0].id)}
                          buttonClickHandler={fButtonHandler}
                          title={sTitle}>
          {getCloneWizardView()}
        </CustomDialogView>
    );
  }


CloneWizardView.propTypes = oPropTypes;

export default CS.SafeComponent(CloneWizardView);
export const events = oEvents;
