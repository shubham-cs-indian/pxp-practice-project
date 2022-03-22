import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import ViewUtils from './../view/utils/view-utils';
import { view as ImageSimpleView } from './../../../../../viewlibraries/imagesimpleview/image-simple-view';
import TaxonomyBaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-taxonomy-base-types-dictionary';

const oEvents = {
  TAXONOMY_LEVEL_ITEM_CLICKED: "taxonomy_level_item_clicked",
  TAXONOMY_LEVEL_ITEM_LABEL_CHANGED: "taxonomy_level_item_label_changed",
  TAXONOMY_LEVEL_CHILD_ACTION_ITEM_CLICKED: "taxonomy_level_child_action_item_clicked",
};

const oPropTypes = {
  levelIndex: ReactPropTypes.number,
  item: ReactPropTypes.object,
  selectedItemId: ReactPropTypes.string,
  actionItems: ReactPropTypes.array,
  context: ReactPropTypes.string,
  activeDetailedTaxonomy: ReactPropTypes.object
};

// @CS.SafeComponent
class TaxonomyLevelChildItemView extends React.Component {
  static propTypes = oPropTypes;

  state = {
    editMode: false,
    label: ""
  };

  handleLevelChildClicked = (sItemId, oEvent) => {
    if (oEvent.nativeEvent.dontRaise) {
      return;
    }
    var iLevelIndex = +this.props.levelIndex;
    EventBus.dispatch(oEvents.TAXONOMY_LEVEL_ITEM_CLICKED, iLevelIndex, sItemId, this.props.context);
  };

  handleOnBlur = () => {
    var oItem = this.props.item;
    var sLabel = this.state.label;
    var iLevelIndex = +this.props.levelIndex;
    EventBus.dispatch(oEvents.TAXONOMY_LEVEL_ITEM_LABEL_CHANGED, iLevelIndex, oItem.id, sLabel, this.props.context);
    this.setState({editMode: false});
  };

  handleOnKeyDown = (oEvent) => {
    if (oEvent.keyCode == 13) {
      this.handleOnBlur();
    }
  };

  handleLevelChildActionItemClicked = (sActionId, sChildId, oEvent) => {
    if (sActionId == "rename") {
      var oItem = this.props.item;
      this.setState({editMode: true, label: oItem.label});
      oEvent.stopPropagation();
      return;
    }

    var iLevelIndex = +this.props.levelIndex;
    EventBus.dispatch(oEvents.TAXONOMY_LEVEL_CHILD_ACTION_ITEM_CLICKED, iLevelIndex, sActionId, sChildId, this.props.context);
    oEvent.nativeEvent.dontRaise = true;
  };

  handleLabelChanged = (oEvent) => {
    this.setState({label: oEvent.target.value});
  };

  handleStopPropagation = (oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
  };

  getImageView = (sKey) => {
    if (sKey) {
      var sThumbnailImageSrc = ViewUtils.getIconUrl(sKey);
      return (<ImageSimpleView classLabel="taxonomyIcon" imageSrc={sThumbnailImageSrc}/>);
    }
    return null;
  };

  getIconDom = (oItem) => {
    let oIconDom = null;
    if (oItem.iconKey) {
      oIconDom = this.getImageView(oItem.iconKey)
    }
    else if (oItem.customIconClassName) {
      oIconDom = (<div className={"taxonomyIcon " + oItem.customIconClassName} key="taxonomyIcon"></div>);
    }
    return oIconDom;
  };

  render() {
    var _this = this;
    var oItem = this.props.item;
    var sSelectedChildId = this.props.selectedItemId;
    var aActionItems = this.props.actionItems;
    var bIsEditMode = this.state.editMode;
    var bIsSelected = oItem.id == sSelectedChildId;
    var sChildClass = "taxonomyLevelChild";
    sChildClass += bIsSelected ? " selected " : "";

    var aChildActionViews = bIsEditMode ? null : CS.map(aActionItems, function (oActionItem) {
      if (!bIsSelected && oActionItem.id == "rename") {
        return;
      }
      let sActionItemTooltip = "";
      if (oActionItem.className === "delete") {
        sActionItemTooltip = getTranslation().DELETE;
      } else if (oActionItem.className === "edit") {
        sActionItemTooltip = getTranslation().EDIT;
      }
      return (<TooltipView label={sActionItemTooltip} key={oActionItem.id}>
        <div className={"childActionItem " + oActionItem.className}
             onClick={_this.handleLevelChildActionItemClicked.bind(_this, oActionItem.id, oItem.id)}></div>
      </TooltipView>);
    });
    let sTooltipLabel = oItem.label;
    let oIconDom = null;
    if (oItem.baseType === TaxonomyBaseTypeDictionary.masterTaxonomy) {
      oIconDom = _this.getIconDom(oItem);
      sTooltipLabel = _this.props.allowedTaxonomyHierarchyList[oItem.id];
    }

    var oLabelView = bIsEditMode
        ? <input type="text" className="childLabelInput" value={this.state.label}
                 onChange={_this.handleLabelChanged} onBlur={_this.handleOnBlur} onClick={_this.handleStopPropagation}
                 onKeyDown={_this.handleOnKeyDown} autoFocus/>
        : <TooltipView label={sTooltipLabel}>
          <div className="childLabel">{CS.getLabelOrCode(oItem)}</div>
        </TooltipView>;

    var oTaxonomyTypeView = bIsEditMode ? null: <div className={oItem.taxonomyType}></div>;
    return (

        <div className={sChildClass}
             onClick={_this.handleLevelChildClicked.bind(_this, oItem.id)}>
          {oIconDom}
          {oLabelView}
          {oTaxonomyTypeView}
          <div className="childActionItems">
            {aChildActionViews}
          </div>
        </div>
    );
  }
}

export const view = TaxonomyLevelChildItemView;
export const events = oEvents;
