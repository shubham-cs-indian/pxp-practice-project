import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ImageSimpleView } from './../imagesimpleview/image-simple-view';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';

const oEvents = {
  HANDLE_DISABLED_TAG_NODE_CLICKED: "handle_disabled_tag_node_clicked"
};
const oPropTypes = {
  rulerTagGroupModel: ReactPropTypes.object,
  isDisabled: ReactPropTypes.bool,
  extraData: ReactPropTypes.object,
  onChange: ReactPropTypes.func,
  masterTagList: ReactPropTypes.oneOfType([ReactPropTypes.object, ReactPropTypes.array]),
};
/**
 * @class UniqueSelectionView - Use for Display life cycle status views button.
 * @memberOf Views
 * @property {object} [rulerTagGroupModel] - Pass properties like childrenModels, depth, id, label, relevence, tagId
 * etc.
 * @property {bool} [isDisabled] -  when tru its disabled to select.
 * @property {object} [extraData] - Not in use.
 * @property {func} [onChange] - Pass function for change the select tag view.
 * @property {array} [masterTagList] - Contains referenced tag list.
 */

// @CS.SafeComponent
class UniqueSelectionView extends React.Component {

  constructor(props) {
    super(props);
  }

  handleTagItemClicked =(sTagId, oEvent)=> {
    let _props = this.props;
    if (CS.isFunction(_props.onChange) && !_props.isDisabled) {
      _props.onChange(sTagId);
      oEvent.nativeEvent.dontRaise = true;
    }
  }

  handleDisabledTagNodeClicked =(sContext)=>{
    EventBus.dispatch(oEvents.HANDLE_DISABLED_TAG_NODE_CLICKED, sContext);
  }

  getTagValueView =(oData)=> {
    let _this = this;
    let aMasterTagList = _this.props.masterTagList;
    let oTagGroupModel = this.props.rulerTagGroupModel;
    let oTagMaster = ViewUtils.getNodeFromTreeListById(aMasterTagList, oData.tagId);
    /**
     * - We will get master tag empty if some other tags are added into allowed tags from property collection.
     */
    if (CS.isEmpty(oTagMaster)) {
      return null;
    }
    let oTagValuesProperties =  oTagGroupModel.properties["tagValuesProperties"];
    let sId = oData.id;
    let oTagValueProperties = oTagValuesProperties[sId];
    let sLabel = CS.getLabelOrCode(oTagMaster);
    let sColor = oTagMaster.color;
    let sIconId = oTagMaster.iconKey;
    let bIsValueDisabled = CS.isNotEmpty(oTagValueProperties)  && oTagValueProperties.isDisabled;
    let oIconDom = null;
    let bIsSelected = oData.relevance > 0;
    let oTickDOM = null;

    let sIconClassName = "rulerTagIcon ";
    let sLabelIconClassName = "rulerTagIconLabel ";

    if (bIsSelected) {
      sIconClassName += "rulerTagIconSelected";
      sLabelIconClassName += "rulerTagIconLabelSelected";
      oTickDOM = (<div className="rulerTagIconTick"></div>);
    }
    if (this.props.showDefaultIcon) {
      let sThumbnailImageSrc = ViewUtils.getIconUrl(sIconId);
      oIconDom = (
          <div className="rulerTagIconWrapper">
            <ImageSimpleView classLabel={sIconClassName} imageSrc={sThumbnailImageSrc}/>
          </div>
      );
    } else if (CS.isNotEmpty(sIconId)) {
      let sThumbnailImageSrc = ViewUtils.getIconUrl(sIconId);
      oIconDom = (
          <div className="rulerTagIconWrapper">
            <ImageSimpleView classLabel={sIconClassName} imageSrc={sThumbnailImageSrc}/>
          </div>
      );
    } else if (!CS.isEmpty(sColor)) {
      let oIconStyle = {
        backgroundColor: sColor,
        height: "100%",
        width: "100%"
      };
      oIconDom = (
          <div className="rulerTagIconColorWrapper">
            <div className={sIconClassName} style={oIconStyle}></div>
          </div>
      );
    }
    let fHandler = _this.handleTagItemClicked.bind(_this, oData.tagId);
    if (_this.props.isDisabled || bIsValueDisabled) {
      sLabelIconClassName += " disabledNode";
      fHandler = _this.handleDisabledTagNodeClicked.bind(_this, oTagGroupModel.properties["context"]);
    } else {
      !bIsSelected && (sLabelIconClassName += " enabledNode");
    }

    return (<div className={sLabelIconClassName} key={sId} title={sLabel} onClick={fHandler}>
      {oIconDom}
      <div className="rulerTagLabel">{sLabel}</div>
      {oTickDOM}
    </div>);
  }

  getView =()=> {
    let _this = this;
    let oTagGroupModel = this.props.rulerTagGroupModel;
    let oEntityTag = oTagGroupModel.entityTag;
    let aTagValues = oEntityTag.tagValues;

    return CS.map(aTagValues, _this.getTagValueView);
  }

  render() {
    var oView = this.getView();

    return (
        <div className="rulerTagTypeView">
          {oView}
        </div>
    );
  }
}

UniqueSelectionView.propTypes = oPropTypes;

export const view = UniqueSelectionView;
export const events = oEvents;
