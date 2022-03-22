import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as SliderView } from '../sliderview/slider-view.js';
import { view as DoubleSliderView } from '../doublesliderview/double-slider-view.js';
import { view as RadioButtonGroupView } from '../radiobuttongroupview/radio-button-group-view.js';
import TooltipView from './../tooltipview/tooltip-view';
import TagGroupModel from '../../viewlibraries/taggroupview/model/tag-group-model';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";

const oEvents = {
  TAG_NODE_CLICKED: "TAG_NODE_CLICKED"
};

const oPropTypes = {
  childModel: ReactPropTypes.instanceOf(TagGroupModel).isRequired,
  tagRanges: ReactPropTypes.object,
  tagValues: ReactPropTypes.array,
  onApply: ReactPropTypes.func,
};
/**
 * @class TagNodeView
 * @memberOf Views
 * @property {custom} childModel
 * @property {object} tagRanges
 * @property {array} [tagValues]
 * @property {func} [onApply]
 */

// @CS.SafeComponent
class TagNodeView extends React.Component{
  constructor (props) {
    super(props);

    this.customTreeNodeWrapper = React.createRef();
    this.tagName = React.createRef();
    this.tagDetail = React.createRef();
  }
  componentDidMount() {
    this.calculateWidth();
    //Need to re-render after child DOM CSS changes through this.calculateWidth
    this.setState({});
  }

  componentDidUpdate() {
    this.calculateWidth();
  }

  calculateWidth =()=> {
    var oView = this.customTreeNodeWrapper.current;
    if(oView) {
      var oTagNameNode = this.tagName.current;
      var oTagDetailNode = this.tagDetail.current;
      if(oTagNameNode && oTagDetailNode) {
        if(oView.offsetWidth < 275) {
          oTagNameNode.classList.add('fullLengthTagName');
          oTagDetailNode.classList.add('fullLengthTagDetail');
        } else {
          oTagNameNode.classList.remove('fullLengthTagName');
          oTagDetailNode.classList.remove('fullLengthTagDetail');
        }
      }
    }
  }

  handleClickOnTagNode =(oEvent)=> {
    let sTargetClass = oEvent.currentTarget.className;
    if(sTargetClass.indexOf('customTreeNodeWrapper') == -1 && sTargetClass.indexOf('tagName') == -1) {
      return;
    }
    var iNewValue = 0;
    var iOldValue = this.props.childModel.relevance;
    if(iOldValue == 100){
      iNewValue = -100;
    } else if(iOldValue == -100) {
      iNewValue = 0;
    } else {
      iNewValue = 100;
    }
    this.props.childModel.properties['iNewValue'] =  iNewValue;
    EventBus.dispatch(oEvents.TAG_NODE_CLICKED, this, this.props.childModel);

  }

  handleOnApply =(oTagModel)=> {
    let __props = this.props;
    if(__props.onApply) {
      let oTagObj = {
        tagId: oTagModel.id,
        relevance: oTagModel.properties['iNewValue'],
        key: oTagModel.properties['key']
      };
      __props.onApply(oTagObj);
    }
  }

  getIconDOM = (sKey) => {
    let sThumbnailImageSrc = ViewUtils.getIconUrl(sKey);
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sThumbnailImageSrc}/>
        </div>
    );
  };

  getTagValueChangerView =()=> {
    var __props = this.props;
    var oTagModel = __props.childModel;
    var sTagViewType = oTagModel.properties['tagViewType'];
    var iSliderValue = oTagModel.relevance;
    var sTagId = oTagModel.id;
    if(sTagViewType == "slider") {
      return (
          <div className="tagDetail singleSlider" ref={this.tagDetail}>
            <SliderView
                minValue={__props.tagRanges.minValue}
                maxValue={__props.tagRanges.maxValue}
                value={iSliderValue}
                sliderModel={oTagModel}
                onApply={this.handleOnApply}
            />

          </div>
      );
    } else if(sTagViewType == "doubleSlider") {
      return (
          <div className="tagDetail doubleSlider" ref={this.tagDetail}>
            <DoubleSliderView
                minValue={__props.tagRanges.minValue}
                maxValue={__props.tagRanges.maxValue}
                from={iSliderValue.from}
                to={iSliderValue.to}
                sliderModel={oTagModel}
                onApply={this.handleOnApply}/>
          </div>
      );
    } else if(sTagViewType == "radioGroup") {
      return (
          <div className="tagDetail tagRadioGroupContainer" ref={this.tagDetail}>
            <RadioButtonGroupView
                radioGroupId={sTagId}
                dataList={__props.tagValues}
                selectedId={iSliderValue}
                containerModel={oTagModel}
                onApply={this.handleOnApply}
            />
          </div>
      );
    }
  }

  render(){

    //var oTag = this.props.tag;
    var oTagModel = this.props.childModel;
    var sDepth = parseInt(oTagModel.depth);

    var iSliderValue = oTagModel.relevance;
    var nodeSelected = iSliderValue != 0 ? "customTreeNodeSelected" : "";
    let oIconDOM = null;

    if(this.props.showDefaultIcon){
      oIconDOM = this.getIconDOM(oTagModel.properties.iconKey);
    }

    return(
        <div className = "customTreeParentNodeContainer">
          <TooltipView placement="bottom" label={CS.getLabelOrCode(oTagModel)}>
            <div className={"customTreeNodeWrapper depth" + sDepth + " " + nodeSelected}
                 ref={this.customTreeNodeWrapper}
                 onClick={this.handleClickOnTagNode}>
              {oIconDOM}
              <div className="tagName" ref={this.tagName}>{CS.getLabelOrCode(oTagModel)}</div>
              {this.getTagValueChangerView()}
            </div>
          </TooltipView>
        </div>
    )
  }

}

TagNodeView.propTypes = oPropTypes;

export const view = TagNodeView;
export const events = oEvents;
