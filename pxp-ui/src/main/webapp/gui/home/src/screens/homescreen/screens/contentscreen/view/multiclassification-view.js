import CS from './../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import {getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager';
import {view as ActionableChipsView} from './../../../../../viewlibraries/actionablechipsview/actionable-chips-view';
import {view as ChipsView} from './../../../../../viewlibraries/chipsView/chips-view';
import TaxonomyInheritanceView from './taxonomy-inheritance-view';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import MultiClassificationDialogToolbarLayoutData from "../tack/multiclassification-dialog-toolbar-layout-data";

let MultiClassificationViewTypesIds = new MultiClassificationDialogToolbarLayoutData()["multiClassificationViewTypesIds"];

const oEvents = {
  MULTI_CLASSIFICATION_VIEW_REMOVE_NODE_CLICKED: "multi_classification_view_remove_node_clicked",
  TAXONOMY_INHERITANCE_CONFLICT_ICON_CLICKED: "taxonomy_inheritance_conflict_icon_clicked",
  MULTI_CLASSIFICATION_SECTION_EDIT_CLASSIFICATION_ICON_CLICKED: "handle_edit_classification_icon_clicked"
};

const oPropTypes = {
  data: ReactPropTypes.object,
  context: ReactPropTypes.string,
  scrollSection: ReactPropTypes.bool,
  sTaxonomyId: ReactPropTypes.string,
  multiClassificationViewData: ReactPropTypes.object,
};


// @CS.SafeComponent
class MultiClassificationView extends React.Component {

  handleRemoveMultiClassificationNodeClicked = (sLeafNodeId, sIdToRemove) => {
    EventBus.dispatch(oEvents.MULTI_CLASSIFICATION_VIEW_REMOVE_NODE_CLICKED, sLeafNodeId, sIdToRemove, this.props.context, this.props.sTaxonomyId);
  };

  handleTaxonomyInheritanceViewOpen = () => {
    EventBus.dispatch(oEvents.TAXONOMY_INHERITANCE_CONFLICT_ICON_CLICKED)
  };

  handleEditClassificationIconClicked = (sTabId) => {
    let __props = this.props;
    EventBus.dispatch(oEvents.MULTI_CLASSIFICATION_SECTION_EDIT_CLASSIFICATION_ICON_CLICKED, sTabId, __props.sTaxonomyId, __props.context);
  };

  getDataBasedOnContext = (sContextKey) => {
    let fRemoveHandler = CS.noop;
    let sHeaderLabel = "";
    let sHeaderClassName = "";
    let sEmptyMessage = "";
    let oIcon = "";
    switch (sContextKey) {
      case MultiClassificationViewTypesIds.CLASSES:
        sHeaderLabel = getTranslation().CLASSES;
        sHeaderClassName = "classSwitchingView";
        sEmptyMessage = "";
        break;

      case MultiClassificationViewTypesIds.TAXONOMIES:
        sHeaderLabel = getTranslation().TAXONOMIES;
        sHeaderClassName = "taxonomySwitchingView";
        sEmptyMessage = getTranslation().NO_TAXONOMY_ADDED_IN_CLASSIFICATION;
        if(this.props.context  === "minorTaxonomiesDialogContext"
            || this.props.context  === "minorTaxonomiesDialogContextInEmbeddedTable") {
          sEmptyMessage = getTranslation().NO_TAXONOMY_ADDED;
        }
        let oMultiClassificationViewData = this.props.multiClassificationViewData;
        let oTaxonomyConflictValue = oMultiClassificationViewData && oMultiClassificationViewData.taxonomyConflictingValues || {};
        let bHideConflictIcon = oTaxonomyConflictValue && oTaxonomyConflictValue.length>0 && oTaxonomyConflictValue[0].taxonomyInheritanceSetting == "manual" ? oTaxonomyConflictValue[0].isResolved : true;
        oIcon = !bHideConflictIcon ? <TooltipView placement="top" label={getTranslation().TAXONOMY_CHANGED_IN_BASE_ARTICLE}><span className="taxonomyConflictIcon" onClick={this.handleTaxonomyInheritanceViewOpen}></span></TooltipView> : null;
        break;
    }
    return {
      headerLabel: sHeaderLabel,
      headerClassName: sHeaderClassName,
      removeHandler: fRemoveHandler,
      emptyMessage: sEmptyMessage,
      oIcon: oIcon
    }
  };

  getChipsView = (oClassificationData, sContextKey) => {
    let _this = this;
    let sContext = this.props.context;
    if (sContextKey === MultiClassificationViewTypesIds.CLASSES) {
      if (sContext === "multiClassificationDialogView") {
        return (<ActionableChipsView items={CS.values(oClassificationData)}
                                     showLink={false}
                                     removeHandler={_this.handleRemoveMultiClassificationNodeClicked.bind(this, "")}
                                     context={sContextKey}/>)
      } else {
        return (<ChipsView items={CS.values(oClassificationData)}/>)
      }
    } else if (sContextKey === MultiClassificationViewTypesIds.TAXONOMIES) {
      let aViews = [];
      CS.forEach(oClassificationData, function (aData, sSelectedTaxonomyId) {
        if (sContext === "multiClassificationDialogView" || sContext === "minorTaxonomiesDialogContext"
            || sContext === "minorTaxonomiesDialogContextInEmbeddedTable") {
          aViews.push(<ActionableChipsView items={aData}
                                           showLink={true}
                                           removeHandler={_this.handleRemoveMultiClassificationNodeClicked.bind(this, sSelectedTaxonomyId)}
                                           context={sContextKey}/>)
        } else {
          aViews.push(<ChipsView items={aData}/>)
        }
      });
      return aViews;
    }

  };

  getTaxonomyInheritanceView = () => {
    let oMultiClassificationViewData = this.props.multiClassificationViewData;
    let oTaxonomyInheritanceData = oMultiClassificationViewData && oMultiClassificationViewData.taxonomyInheritanceData;
    let bOpenDialog = oTaxonomyInheritanceData ? oTaxonomyInheritanceData.bOpenDialog : false;
    return (bOpenDialog ? <TaxonomyInheritanceView
      key={"taxonomyInheritanceView"}
      bOpenDialog= {bOpenDialog}
      contentIdVsTypesTaxonomies={oTaxonomyInheritanceData.contentIdVsTypesTaxonomies}
      referencedTaxonomies={oTaxonomyInheritanceData.referencedTaxonomies}
      parentContentId={oTaxonomyInheritanceData.parentContentId}
      articleId={oTaxonomyInheritanceData.articleId}
    /> : null)
  }

  getUpperIconContainer = (sTabId) => {
    let oMultiClassificationViewData = this.props.multiClassificationViewData;

    if (!(CS.isNotEmpty(oMultiClassificationViewData) && oMultiClassificationViewData.showEditClassificationIcon && !oMultiClassificationViewData.showClassificationDialog)) {
      return null;
    }

    return (
        <TooltipView placement={"bottom"} label={getTranslation().ADD_CLASSIFICATION}>
          <div className="editMultiClassificationIcon"
               onClick={this.handleEditClassificationIconClicked.bind(this, sTabId)}>
          </div>
        </TooltipView>
    );
  };

  getView = () => {
    let _this = this;
    let oData = this.props.data;
    let aViews = [];
    CS.forEach(oData, function (oMultiClassificationData, sContextKey) {
      let oDataBasedOnKey = _this.getDataBasedOnContext(sContextKey);
      let aClassificationView = [];
      let oSectionTitleDOM = (<div className={"sectionTitle"}>
        {oDataBasedOnKey.oIcon}
        {oDataBasedOnKey.headerLabel}
      </div>);

      if (CS.isEmpty(oMultiClassificationData)) {
        aClassificationView.push(<div className={"nothingSelectedView"} key="nothingSelectedView">
          {oDataBasedOnKey.emptyMessage}
        </div>);
      } else {
        aClassificationView.push(<React.Fragment key="chipsView">
          {_this.getChipsView(oMultiClassificationData, sContextKey)}
          </React.Fragment>
        );
        if (_this.props.scrollSection) {
          aClassificationView = <div className={"classificationViewContainer"}>{aClassificationView}</div>
        }
      }

      aViews.push(
          <div className={oDataBasedOnKey.headerClassName} key={sContextKey}>
            {_this.getUpperIconContainer(sContextKey)}
            {(_this.props.context === "minorTaxonomiesSectionView") ? null : oSectionTitleDOM}
            <div className={"multiClassificationView"}>
              {aClassificationView}
            </div>
          </div>
      );
    });

    aViews.push(_this.getTaxonomyInheritanceView());
    return aViews;
  };

  render () {
    return this.getView();
  }
}

MultiClassificationView.propTypes = oPropTypes;

export const view = MultiClassificationView;
export const events = oEvents;
