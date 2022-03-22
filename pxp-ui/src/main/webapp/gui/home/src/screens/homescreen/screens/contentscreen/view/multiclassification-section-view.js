import CS from './../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import {getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import {view as ToolbarView} from "../../../../../viewlibraries/toolbarview/toolbar-view";
import MultiClassificationDialogToolbarLayoutData from "../tack/multiclassification-dialog-toolbar-layout-data";
import {view as MultiClassificationView} from "./multiclassification-view";
import {view as TreeView} from "../../../../../viewlibraries/treeviewnew/tree-view";
let MultiClassificationViewTypesIds = new MultiClassificationDialogToolbarLayoutData()["multiClassificationViewTypesIds"];

const oEvents = {
  MULTI_CLASSIFICATION_SECTION_DIALOG_BUTTON_CLICKED: "handle_classification_dialog_button_clicked",
  MULTI_CLASSIFICATION_SECTION_TREE_NODE_CHECKBOX_CLICKED: "multi_classification_section_tree_node_checkbox_clicked",
  MULTI_CLASSIFICATION_SECTION_TREE_NODE_CLICKED: "multi_classification_section_tree_node_clicked",
};

const oPropTypes = {
  context: ReactPropTypes.string,
  multiClassificationViewData: ReactPropTypes.object,
  dialogContext: ReactPropTypes.string,
  doNotShowClassification: ReactPropTypes.bool,
  dialogButtonHandler: ReactPropTypes.func,
};


// @CS.SafeComponent
class MultiClassificationSectionView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleClassificationDialogButtonClicked = (sButtonId) => {
    if (this.props.dialogButtonHandler) {
      this.props.dialogButtonHandler(sButtonId, this.props.context);
      return;
    }
    EventBus.dispatch(oEvents.MULTI_CLASSIFICATION_SECTION_DIALOG_BUTTON_CLICKED, sButtonId, this.props.context)
  };

  handleTreeNodeCheckBoxClicked = (sTreeNodeId) => {
    EventBus.dispatch(oEvents.MULTI_CLASSIFICATION_SECTION_TREE_NODE_CHECKBOX_CLICKED, sTreeNodeId, this.props.context, this.props.sTaxonomyId);
  };

  handleGetSelectedTaxonomyChildrenList = (sTreeNodeId) => {
    EventBus.dispatch(oEvents.MULTI_CLASSIFICATION_SECTION_TREE_NODE_CLICKED, sTreeNodeId, this.props.context);
  };

  getClassificationTabsView = (oClassificationDialogData) => {
    let oClassificationDialogToolbarData = oClassificationDialogData.classificationDialogToolbarData;
    return (<ToolbarView  toolbarData={oClassificationDialogToolbarData}/>);
  };

  getMultiClassificationView = (oData, sContext, bScrollSection) => {
    return (
        <MultiClassificationView data={oData}
                                 context={sContext}
                                 scrollSection={bScrollSection}
                                 sTaxonomyId={this.props.sTaxonomyId}
                                 multiClassificationViewData = {this.props.multiClassificationViewData}
        />
    )
  };

  getSelectedClassificationView = (oClassificationDialogData) => {
    let sSelectedTabIdInClassificationDialog = oClassificationDialogData.selectedTabIdInClassificationDialog;
    let oMultiClassificationData = oClassificationDialogData.multiClassificationData;
    let oData = {};
    if(sSelectedTabIdInClassificationDialog === MultiClassificationViewTypesIds.CLASSES) {
     oData.classes = oMultiClassificationData.classes;
    }  else if(sSelectedTabIdInClassificationDialog === MultiClassificationViewTypesIds.TAXONOMIES) {
      oData.taxonomies = oMultiClassificationData.taxonomies;
    }

    let oView = this.getMultiClassificationView(oData, this.props.dialogContext, true);

    return (
        <div className={"selectedClassificationView"}>
          {this.getClassificationTabsView(oClassificationDialogData)}
          {oView}
        </div>
    );
  };

  getTreeView = (oClassificationDialogData) => {
    let oMultiClassificationTreeData = oClassificationDialogData.multiClassificationTreeData;
    let sSelectedTabIdInClassificationDialog = oClassificationDialogData.selectedTabIdInClassificationDialog;
    let sSearchText = oMultiClassificationTreeData.searchText;
    let bIsSelectedTabTaxonomy = sSelectedTabIdInClassificationDialog === MultiClassificationViewTypesIds.TAXONOMIES;
    let fNodeCheckClickHandler = this.handleTreeNodeCheckBoxClicked;
    let bShowLevelInHeader = bIsSelectedTabTaxonomy && CS.isEmpty(sSearchText);

    return (
        <TreeView {...oMultiClassificationTreeData}
                  showLevelInHeader={bShowLevelInHeader}
                  nodeCheckClickHandler={fNodeCheckClickHandler}
                  loadChildNodesHandler={this.handleGetSelectedTaxonomyChildrenList}
                  showEndNodeIndicator={true}
        />
    )
  };

  getClassificationTreeView = (oClassificationDialogData) => {
    return (
        <div className={"multiClassificationTreeView"}>
          {this.getTreeView(oClassificationDialogData)}
        </div>
    )
  };

  getMultiClassificationDialogView = (oClassificationDialogData) => {
    let sClassName = "multiClassificationDialogView ";
    sClassName += oClassificationDialogData.multiClassificationTreeData.context === "minorTaxonomiesTreeView" ? " disableFirstCheckBox" : null;
    return (
        <div className={sClassName}>
          {this.getSelectedClassificationView(oClassificationDialogData)}
          {this.getClassificationTreeView(oClassificationDialogData)}
        </div>
    )
  };


  getEditClassificationDialogView = () => {
    let oMultiClassificationViewData = this.props.multiClassificationViewData;

    if(oMultiClassificationViewData && !oMultiClassificationViewData.showClassificationDialog || CS.isEmpty(oMultiClassificationViewData) ) {
      return;
    }

    let oClassificationDialogData = oMultiClassificationViewData.classificationDialogData;

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

    let bIsDirty = oClassificationDialogData.isDirty;
    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true
      }
    ];

    if (bIsDirty) {
      aButtonData.push(
          {
            id: "apply",
            label: getTranslation().APPLY,
            isFlat: false
          }
      )
    }

    let fButtonHandler = this.handleClassificationDialogButtonClicked;
    return (
        <CustomDialogView modal={false} open={oMultiClassificationViewData.showClassificationDialog}
                          bodyStyle={oBodyStyle}
                          contentStyle={oContentStyle}
                          buttonData={aButtonData}
                          buttonClickHandler={fButtonHandler}
                          onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                          title={getTranslation().ADD_CLASSIFICATION}>
          {this.getMultiClassificationDialogView(oClassificationDialogData)}
        </CustomDialogView>);
  };

  getView = () => {
    let oMultiClassificationViewData = this.props.multiClassificationViewData;
    let oMultiClassificationData = oMultiClassificationViewData && oMultiClassificationViewData.multiClassificationData;

    return (
        <div className="multiClassificationSectionView">
          {this.getEditClassificationDialogView()}
          {!this.props.doNotShowClassification && this.getMultiClassificationView(oMultiClassificationData, this.props.context)}
        </div>
    );
  };

  render () {
    return this.getView();
  }
}

MultiClassificationSectionView.propTypes = oPropTypes;

export const view = MultiClassificationSectionView;
export const events = oEvents;
