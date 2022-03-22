import CS from '../../libraries/cs';
import CurrentZoomValueContext from '../../commonmodule/HOC/current-zoom-value-context';
import React from 'react';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ThumbnailXRayTemplateView } from './templates/thumbnail-x-ray-template-view';
import { view as ThumbnailTemplateViewNew } from './templates/thumbnail-template-view-new';
import ThumbnailModel from './model/thumbnail-model';
import ThumbnailModeConstants from '../../commonmodule/tack/thumbnail-mode-constants';
import ViewLibrariesUtils from '../utils/view-library-utils';

let imageHeight;

let imageWidth;

const oEvents = {
  THUMB_SINGLE_CLICKED: "thumb_single_clicked",
  THUMB_CONTEXT_MENU_CLICKED: "thumb_context_menu_clicked",
  THUMB_CONTEXT_MENU_POPOVER_CLOSED: "thumb_context_menu_popover_closed",
  THUMB_CONTEXT_MENU_ITEM_CLICKED: "thumb_context_menu_item_clicked",
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(ThumbnailModel).isRequired,
  viewMode: ReactPropTypes.string,
  onClickHandler: ReactPropTypes.func,
  filterContext: ReactPropTypes.object.isRequired,
  currentZoom: ReactPropTypes.number
};
/**
 * @class ThumbnailView - Use for Display Thumbnail in Product.
 * @memberOf Views
 * @property {custom} model model contain parameter like id, label , imageSrc, tags, type, className, properties.
 * @property {string} [viewMode] -  a viewMode for changing the theme for view.
 * @property {func} [onClickHandler] -  a function which used for when user click on thumbnail.
 * @property {number} [currentZoom] - this prop is number of currentZoom(ex 1 for small and 2 for big views.).
 */

// @CurrentZoomValueContext
// @CS.SafeComponent
class ThumbnailView extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      tagsExpanded: false
    }

    imageHeight = null;
    imageWidth = null;

    this.thumbnailViewContainer = React.createRef();
  }

  handleImageOnLoad =(oEvent)=> {

   /* if(String(this.props.viewMode).toLocaleLowerCase() == "b") {
      var iWidth = oEvent.target.width;
      var iHeight = oEvent.target.height;

      this.imageWidth = iWidth;
      this.imageHeight = iHeight;

      var $thumbnail = $(ReactDOM.findDOMNode(this.refs.thumbnailViewContainer));
      var iThumbnailHeight = $thumbnail.height();
      var iThumbnailWidth = iThumbnailHeight * iWidth / iHeight;
      $thumbnail.width(iThumbnailWidth);

      if(iThumbnailWidth < 160){
        $thumbnail.addClass('lessWidth');
      } else {
        $thumbnail.removeClass('lessWidth');
      }
    }*/
  }

  _handleSingleClicked =(oModel, oEvent)=> {
    if (this.props.onClickHandler) {
      this.props.onClickHandler(oEvent, oModel);
      return;
    }
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.THUMB_SINGLE_CLICKED, oEvent, oModel, this.props.filterContext);
  }

  _handleContextMenuClicked =(oEvent)=> {
    oEvent.preventDefault();
    var oModel = this.props.model;
    EventBus.dispatch(oEvents.THUMB_CONTEXT_MENU_CLICKED, oModel);
  }

  _handleContextMenuPopoverClose =(oEvent)=> {
    EventBus.dispatch(oEvents.THUMB_CONTEXT_MENU_POPOVER_CLOSED);
  }

  _handleContextMenuItemClick =(sContextItemId)=> {
    EventBus.dispatch(oEvents.THUMB_CONTEXT_MENU_ITEM_CLICKED, sContextItemId);
  }

/*
  getThumbnailImageView =()=>{
    var oModel = this.props.model;
    var sImageClass = "thumbImage";

    var sType = oModel.type;
    if(!sType) {
      sType = oModel.properties['entityType'];
    }
    var oProperties = {};
    var oImageViewStyle = {};

    var oImageViewModel = new ImageViewModel(
        '',
        oModel.imageSrc,
        sType,
        "thumbnailImagePreview",
        oImageViewStyle,
        oProperties
    );

    return (<div className={sImageClass}>
          <ImageView model={oImageViewModel} onLoad={this.handleImageOnLoad}/>
      {/!*<ImageSimpleView classLabel="thumbnailImagePreview" onLoad={this.handleImageOnLoad} imageSrc={sContentImageSrc} />*!/}
    </div>);
  }
*/

  getViewAccordingToMode =()=> {
    var oModel = this.props.model;
    var sId = oModel.id;
    var sName = CS.getLabelOrCode(oModel);
    var sClassName = oModel.className;
    var sImageSrc = oModel.imageSrc;
    var sType = oModel.type;
    var aTags = oModel.tags;
    var oEntity = oModel.properties['entity'];
    var aHits = oEntity.hits;
    var oActiveXRayPropertyGroup = oModel.properties['activeXRayPropertyGroup'];
    var oXRayData = oModel.properties['xRayData'] || {};
    var iPropsZoom = this.props.currentZoom;
    var iCurrentZoomValue = iPropsZoom ? iPropsZoom : this.props.currentZoomValue;
    var sMode = oModel.properties['thumbnailMode'];
    var sTheme = "PIM"; //TODO: get theme from props eventually
    var bHideSearchInfo = true;

    if (!CS.isEmpty(aHits)) {
      bHideSearchInfo = false;
    }

    switch (iCurrentZoomValue) {
      default:
        if(String(this.props.viewMode).toLocaleLowerCase() == "b") {
          sTheme = "MAM";
        }
        switch (sMode) {
          case ThumbnailModeConstants.BASIC:
              return (<ThumbnailTemplateViewNew
                  id={sId}
                  name={sName}
                  className={sClassName}
                  imageSrc={sImageSrc}
                  type={sType}
                  tags={aTags}
                  theme={sTheme}
                  onSingleClickHandler={this._handleSingleClicked}
                  thumbnailViewModel={oModel}
                  filterContext={this.props.filterContext}
                  zoomLevel = {iCurrentZoomValue}
              />);


          case ThumbnailModeConstants.XRAY:
            return (<ThumbnailXRayTemplateView
                id={sId}
                name={sName}
                className={sClassName}
                imageSrc={sImageSrc}
                type={sType}
                tags={aTags}
                theme={sTheme}
                hits={aHits}
                thumbnailViewModel={oModel}
                activeXRayPropertyGroup={oActiveXRayPropertyGroup}
                xRayConfigData={oXRayData}
                filterContext={this.props.filterContext}
                hideSearchInfo={bHideSearchInfo}
            />);

          default:
            return null;
        }
    }
  }

  getChildContextMenuPopOverView =()=> {
    var oModel = this.props.model;
    var aContextMenuList = oModel.properties['contextMenuList'];

    var _this = this;
    let aContextItemDOMNodes = [];
    CS.forEach(aContextMenuList, function (oContextItem) {
      aContextItemDOMNodes.push(
          <div className="contextMenuNode"
               onClick={_this._handleContextMenuItemClick.bind(this, oContextItem.id)}>
            {CS.getLabelOrCode(oContextItem)}
          </div>
      )
    });

    return <div className="thumbnailContextMenuContainer">{aContextItemDOMNodes}</div>
  }

  render(){
    var oModel = this.props.model;
    var bTagsExpanded = this.state.tagsExpanded;
    var sThumbnailClass = "thumbnailViewContainer lessWidth ";
    var sClassIcon = oModel.properties['classIcon'];
    var sClassName = oModel.properties['className'];
    var sImageSrc = oModel.imageSrc;
    var sMode = oModel.properties['thumbnailMode'];

    if(oModel.properties['isSelected']){
      sThumbnailClass += "selectedThumb ";
    }

    if(!sClassIcon){
      sThumbnailClass += sClassName + ' ';
    }

    if(!sImageSrc){
      // sThumbnailClass += "lessWidth ";
    }

    if(bTagsExpanded){
      sThumbnailClass += "tagsExpanded ";
    }

    if(!oModel.properties['disableView']) {
      sThumbnailClass += "isOpenable ";
    }

    if(oModel.properties['isToCutEntity']){
      sThumbnailClass += "isToCutEntity ";
    }

    var iPropsZoom = this.props.currentZoom;
    var iCurrentZoomValue = iPropsZoom ? iPropsZoom : this.props.currentZoomValue;
    if (sMode == ThumbnailModeConstants.BASIC) { //thumbnailViewContainer will have heights only in case of basic view, else template heights will be used
      sThumbnailClass += ("zoom_" + iCurrentZoomValue);
    }
    var oView = this.getViewAccordingToMode();
    var sRelevanceValue = oModel.properties['relevanceValue'];
    var sThumbnailRelevanceClassName = ViewLibrariesUtils.getClassNameAsPerRelevanceValue(sRelevanceValue);
    sThumbnailClass += (" " + sThumbnailRelevanceClassName + " ");

    var oReturnDom = null;
    var bAllowRightClick = oModel.properties["allowRightClick"];
    if(bAllowRightClick){
      var bIsContextMenuPopOverVisible = oModel.properties["thumbContextPopoverOpenStatus"] &&
          !CS.isEmpty(oModel.properties['contextMenuList']);
      var oChildView = bIsContextMenuPopOverVisible ? this.getChildContextMenuPopOverView() : null;

      oReturnDom = (
          <div className={sThumbnailClass}
               ref={this.thumbnailViewContainer}
               data-content-id={oModel.id}
               onContextMenu={this._handleContextMenuClicked}>
            <div/>
            {oView}
            <CustomPopoverView
                className="popover-root"
                open={bIsContextMenuPopOverVisible}
                anchorEl={this.thumbnailViewContainer.current}
                anchorOrigin={{horizontal: 'middle', vertical: 'center'}}
                transformOrigin={{horizontal: 'left', vertical: 'top'}}
                onClose={this._handleContextMenuPopoverClose}
            >
              {oChildView}
            </CustomPopoverView>
          </div>);
    }else {
      oReturnDom = <div className={sThumbnailClass} ref={this.thumbnailViewContainer} data-content-id={oModel.id}>
        <div/>
        {oView}
      </div>;
    }

    return oReturnDom;
  }
}

ThumbnailView.propTypes = oPropTypes;



export const view = CurrentZoomValueContext(ThumbnailView);
export const events = oEvents;
