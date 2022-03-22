import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import TooltipView from './../tooltipview/tooltip-view';
import CircledTagNodeModel from './model/circled-tag-node-model';
import ViewUtils from './../utils/view-library-utils';

const oEvents = {
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(CircledTagNodeModel).isRequired,
  tagGroupId: ReactPropTypes.string,
  handleNodeClicked: ReactPropTypes.func,
  disabled: ReactPropTypes.bool,
};
/**
 * @class TagNodeView - Use for Display Tag node vie for add variant tags.
 * @memberOf Views
 * @property {custom} model - Pass data like id, label, icon, color, type, relevance, childrenModels,
 * properties
 * @property {string} [tagGroupId] - Pass Id of Tag group.
 * @property {func} [handleNodeClicked] - Function for handle tag clicked.
 * @property {bool} [disabled] - when true click disable on tags.
 */

// @CS.SafeComponent
class TagNodeView extends React.Component {

  constructor(props) {
    super(props);
  }

  shouldComponentUpdate=(oNextProps)=> {
    return !(
        CS.isEqual(this.props.tagGroupId, oNextProps.tagGroupId) &&
        CS.isEqual(this.props.disabled, oNextProps.disabled) &&
        CS.isEqual(this.props.model, oNextProps.model)
    );
  }

  handleClickOnTagNode =()=> {
    var oModel = this.props.model;
    var sId = oModel.id;

    if(this.props.handleNodeClicked){
      this.props.handleNodeClicked(sId);
    }
  }

  getNodeImageView =()=> {
    var oModel = this.props.model;
    var sIcon = oModel.iconKey;
    var sColor = oModel.color;
    var oCircleView = null;
    var sLabel = CS.getLabelOrCode(oModel);
    var iRelevance = oModel.relevance;
    var sCircleContainerClassName = "nodeImageCircleContainer ";
    sCircleContainerClassName += (iRelevance == 100) ? "green " : (iRelevance == -100) ? "red " : "";

    if(sIcon){
      var sUrl = ViewUtils.getIconUrl(sIcon);
      oCircleView = <ImageFitToContainerView imageSrc={sUrl}/>;
    } else if(sColor){
      var oStyle = {backgroundColor: sColor};
      oCircleView = <div className="nodeCircleColorContainer" style={oStyle}></div>;
    } else {
      oCircleView = <div className="nodeCircleTextContainer">{sLabel[0] + (sLabel[1] || "")}</div>;
    }

    return (<div className={sCircleContainerClassName}>{oCircleView}</div>);
  }

  getNodeLabelView =()=> {
    var oModel = this.props.model;

    var sLabel = CS.getLabelOrCode(oModel);

    return (<TooltipView label={sLabel}><div className="nodeTextContainer">{sLabel}</div></TooltipView>);
  }

  getView =()=> {
    var oNodeImageView = this.getNodeImageView();
    var oNodeLabelView = this.getNodeLabelView();
    var sContainerClassName = this.props.disabled ? "circledTagNodeViewContainer disabled " : "circledTagNodeViewContainer ";

    return (<div className={sContainerClassName} onClick={this.handleClickOnTagNode}>
      {oNodeImageView}
      {oNodeLabelView}
    </div>);
  }

  render(){
    return this.getView();
  }

}

TagNodeView.propTypes = oPropTypes;

export const view = TagNodeView;
export const events = oEvents;
