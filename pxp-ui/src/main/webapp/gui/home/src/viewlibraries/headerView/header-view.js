import CS from '../../libraries/cs';
import React from 'react';
import AutosizeInput from 'react-input-autosize';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from '../../screens/homescreen/screens/contentscreen/store/helper/screen-mode-utils';

import TooltipView from '../tooltipview/tooltip-view';
import { view as ContentEditToolbarView } from '../../screens/homescreen/screens/contentscreen/view/content-edit-toolbar-view';
import ContextMenuViewModel from '../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import ViewUtils from '../../screens/homescreen/screens/contentscreen/view/utils/view-utils';

var getTranslation = ScreenModeUtils.getTranslationDictionary;
var oEvents = {
  HEADER_VIEW_SAVE_CLICKED: "header_view_save_clicked",
  HEADER_VIEW_COLLECTION_LABEL_CHANGED: "header_view_collection_label_changed",
  HEADER_VIEW_COLLECTION_COMMENT_CHANGE: "header_view_collection_comment_change",
  HEADER_VIEW_COLLECTION_VISIBILITY_CHANGED: "header_view_collection_visibility_changed"
};

const oPropTypes = {
  activeCollection: ReactPropTypes.object,
  isShakingEnable: ReactPropTypes.bool,
  hierarchyLabel: ReactPropTypes.string,
  onClickHandler: ReactPropTypes.func,
  label: ReactPropTypes.string,
  disable: ReactPropTypes.bool,
  breadCrumbView: ReactPropTypes.object
};
/**
 * @class ActiveCollectionBarView - not in use.
 * @memberOf Views
 * @property {object} [activeCollection] -  object of activeCollection.
 * @property {bool} [isShakingEnable] boolean value of isShakingEnable or not.
 * @property {string} [hierarchyLabel] string of hierarchyLabel.
 * @property {func} [onClickHandler] function which use when onClickHandler event call.
 * @property {string} [label] label.
 * @property {bool} [disable] bollean for disable or not.
 * @property {object} [breadCrumbView] breadcrumb view in object.
 */

// @CS.SafeComponent
class ActiveCollectionBarView extends React.Component {
  static propTypes = oPropTypes;

  state = {
    isCollectionLabelEditable: false
  };

  handleActiveCollectionSaveClicked = (sContext) => {
    EventBus.dispatch(oEvents.HEADER_VIEW_SAVE_CLICKED, sContext);
  };

  handleActiveCollectionLabelChanged = (oEvent) => {
    var sLabel = oEvent.target.value;
    EventBus.dispatch(oEvents.HEADER_VIEW_COLLECTION_LABEL_CHANGED, sLabel);
  };

  handleActiveCollectionVisibiltyChanged = () => {
    EventBus.dispatch(oEvents.HEADER_VIEW_COLLECTION_VISIBILITY_CHANGED);
  };

/*
  handleCommentChange = (oEvent) => {
    var oDom = oEvent.target;
    var sNewValue = !CS.isEmpty(oDom) ? oDom.value : "";
    EventBus.dispatch(oEvents.HEADER_VIEW_COLLECTION_COMMENT_CHANGE, this, sNewValue);
  };
*/

  handleEditIconClicked = () => {
    this.setState({
      isCollectionLabelEditable: true
    });
  };

  handleInputOnBlur = () => {
    this.setState({
      isCollectionLabelEditable: false
    });
  };

/*
  handleInputOnFocus = (oEvent) => {
    oEvent.target.value = oEvent.target.value; //hack to make the cursor go to the end (initially it is at the beginning)
  };
*/

  handleOnBlur = (oEvent) => {
    oEvent.stopPropagation();
    this.handleInputOnBlur();
  };

  getEditableCollectionLabelView = (oActiveCollection) => {
    var oAutoResizeStyle = {
      "maxWidth": "calc(100% - 60px);"
    };
    return (
        <div className="activeCollection" onBlur={this.handleOnBlur} autoFocus={true}>
          {/*<input className="activeCollectionLabel editable"
                 onKeyPress={null}
                 onBlur={this.handleInputOnBlur}
                 onChange={this.handleActiveCollectionLabelChanged}
                 value={oActiveCollection.label}
                 type="text"
                 autoFocus={true}
                 onFocus={this.handleInputOnFocus}/>*/}
          <AutosizeInput value={CS.getLabelOrCode(oActiveCollection)}
                         name="form-field-name"
                         className={"autoSizeContainer"}
                         inputClassName={"labelInput"}
                         style={oAutoResizeStyle}
                         onChange={this.handleActiveCollectionLabelChanged}/>
          {this.getVisibilityButtonDom()}
        </div>
    );
  };

  getDefaultCollectionLabelView = (oActiveCollection) => {
    return (
        <div className="activeCollection">
          <TooltipView label={CS.getLabelOrCode(oActiveCollection)}>
            <div className="headerLabel">{CS.getLabelOrCode(oActiveCollection)}</div>
          </TooltipView>
          <div className="editIcon" onClick={this.handleEditIconClicked}></div>
          {this.getVisibilityButtonDom()}
        </div>
    );
  };

  getVisibilityButtonDom = () => {
    var oActiveCollection = this.props.activeCollection;
    var sVisibilityIcon = oActiveCollection.isPublic ? "visibilityModeIcon" : "visibilityModeIcon privateMode";
    var sVisibilityTooltipLabel = oActiveCollection.isPublic ? getTranslation().COLLECTION_PUBLIC_MODE : getTranslation().COLLECTION_PRIVATE_MODE;

    if (oActiveCollection.createdBy == ViewUtils.getCurrentUser().id) {
      return (<TooltipView placement="bottom" label={sVisibilityTooltipLabel}>
        <div className={sVisibilityIcon} onClick={this.handleActiveCollectionVisibiltyChanged}></div>
      </TooltipView>);
    }

    return null;
  };

  getToolBarView = () => {
    let oActiveCollection = this.props.activeCollection;
    let bContentEditToolbarViewStatus = oActiveCollection.isDirty || false;
    let bIsSaveCommentDisabled = false;
    let oContentEditToolbarViewDOM = null;
    let aMoreButtons = [];
    let aTextButtons = [];

    if (bContentEditToolbarViewStatus) {
      aMoreButtons.push(new ContextMenuViewModel("discardCollection", "Discard Changes", false, "", {}));
      aTextButtons.push({id: 'saveCollection', label: "Save"});
    }

    if (!CS.isEmpty(aTextButtons)) {
      let oToolBarData = {
        moreButtons: aMoreButtons,
        textButtons: aTextButtons,
      };

      oContentEditToolbarViewDOM = <ContentEditToolbarView
          toolbarItems={oToolBarData}
          isSaveCommentDisabled={bIsSaveCommentDisabled}
      />
    }

    return (
        <div className="headerToolBarWrapper">
          {oContentEditToolbarViewDOM}
        </div>
    );
  }

  getCollectionView = () => {
    let oActiveCollection = this.props.activeCollection;
    // let bSnackbarStatus = oActiveCollection.isDirty || false;

    let bSnackbarShakeStatus = this.props.isShakingEnable;
    let sCollectionType = "";
    let oCollectionLabelView = null;
    let sClassName = "collectionOrHierarchyIcon ";
    if (ViewUtils.getIsDynamicCollectionScreen()) {
      sCollectionType = getTranslation().BOOKMARK;
      sClassName += "Bookmark";
    } else {
      sCollectionType = getTranslation().COLLECTION;
      sClassName += "Collection";
    }

    if (this.state.isCollectionLabelEditable) {
      oCollectionLabelView = this.getEditableCollectionLabelView(oActiveCollection);
    }
    else {
      oCollectionLabelView = this.getDefaultCollectionLabelView(oActiveCollection);
    }
    let oCollectionType = <div className="activeCollectionType">{sCollectionType + " : "}</div>;

    return <div className="headerViewWrapper">
      <div className={sClassName}></div>
      {oCollectionType}
      {oCollectionLabelView}
      {/*<EntitySnackBarView*/}
          {/*message={getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE}*/}
          {/*open={bSnackbarStatus}*/}
          {/*onSave={this.handleActiveCollectionSaveClicked.bind(this, 'save')}*/}
          {/*onDiscard={this.handleActiveCollectionSaveClicked.bind(this, 'discard')}*/}
          {/*onComment={this.handleCommentChange}*/}
          {/*shake={bSnackbarShakeStatus}*/}
      {/*/>*/}
      {/*{oContentEditToolbarViewDOM}*/}
    </div>
  }

  render () {
    let oHeaderBarView = null;
    let oToolBarView = null;
    let sClassName = "collectionOrHierarchyIcon ";
    sClassName += this.props.hierarchyLabel;
    if (this.props.activeCollection) {
      oHeaderBarView = this.getCollectionView();
      oToolBarView = this.getToolBarView();
    }
    else {
      oHeaderBarView = <div className="headerViewWrapper">
        <div className={sClassName}></div>
        <TooltipView label={this.props.hierarchyLabel}>
          <div className="headerLabel">{this.props.hierarchyLabel}</div>
        </TooltipView>
      </div>;
    }

    let sLabel = this.props.label;
    let bHideHeader = this.props.disable;
    let oBreadCrumbView = this.props.breadCrumbView || null;

    return (
        <div className="headerViewContainer">
          {oBreadCrumbView}
          {oHeaderBarView}
          {oToolBarView}
        </div>
    );
  }
}

export const view = ActiveCollectionBarView;
export const events = oEvents;
