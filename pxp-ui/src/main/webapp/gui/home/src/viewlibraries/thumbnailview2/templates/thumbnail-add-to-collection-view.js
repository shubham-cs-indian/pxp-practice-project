import CS from '../../../libraries/cs';
import StaticCollectionListContext from '../../../commonmodule/HOC/static-collection-list-context';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../../../viewlibraries/customPopoverView/custom-popover-view';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager';
import ContextMenuViewModel from '../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import { view as ContextMenuItemView } from '../../contextmenuwithsearchview/context-menu-item-view.js';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import TooltipView from '../../tooltipview/tooltip-view';

const oEvents = {
  THUMBNAIL_ADD_TO_COLLECTION_BUTTON_CLICKED: "thumbnail_add_to_collection_button_clicked",
  THUMBNAIL_MODIFY_STATIC_COLLECTION_CLICKED: "thumbnail_modify_static_collection_clicked"
};

const oPropTypes = {
  contentId: ReactPropTypes.string,
  contentModel: ReactPropTypes.object,
  style: ReactPropTypes.object,
  staticCollectionList: ReactPropTypes.array
};
/**
 * @class ThumbnailAddToCollectionView - use for thumbnail to add to collection button.
 * @memberOf Views
 * @property {string} [contentId] -  string of contentId of media thumbnail.
 * @property {object} [contentModel] -  style object which is used contentModel.
 * @property {object} [style] -  style object which is used for giving style to any element.
 */

// @StaticCollectionListContext
// @CS.SafeComponent
class ThumbnailAddToCollectionView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      showStaticCollectionsPopover: false
    }

    this.updatePosition = CS.noop;
  }

  updatePopoverPosition = (fUpdatePosition) => {
    if(fUpdatePosition) {
      this.updatePosition = fUpdatePosition;
    }
  };

  handleStaticCollectionsPopoverVisibility =(oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.THUMBNAIL_ADD_TO_COLLECTION_BUTTON_CLICKED, this);
    this.setState({
      showStaticCollectionsPopover: true,
      staticCollection: oEvent.currentTarget
    });
    this.updatePosition();
  }

  handleStaticCollectionsPopoverClose =(oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    this.setState({
      showStaticCollectionsPopover: false
    });
  }

  handleAddToStaticCollectionClicked =(oModel)=> {
    var sCollectionId = oModel.id;
    var sContentId = this.props.contentId;
    var oContentModel = this.props.contentModel;
    EventBus.dispatch(oEvents.THUMBNAIL_MODIFY_STATIC_COLLECTION_CLICKED, sCollectionId, sContentId, oContentModel, this.props.filterContext);
    this.setState({
      showStaticCollectionsPopover: false
    });
  }

  getStaticCollectionModelsList =(aStaticCollectionList)=> {
    var aStaticCollectionModels = [];
    CS.forEach(aStaticCollectionList, function (oStaticCollection) {
      aStaticCollectionModels.push(new ContextMenuViewModel(
          oStaticCollection.id,
          CS.getLabelOrCode(oStaticCollection),
          false,
          "",
          {context: 'StaticCollection'}
      ));
    });

    return aStaticCollectionModels;
  }

  getStaticCollectionView =()=> {
    return (
        <TooltipView placement="top" label={getTranslation().ADD_TO_COLLECTION}>
          <div className="thumbnailInfo staticCollectionListContainer"
               onClick={this.handleStaticCollectionsPopoverVisibility}>
          </div>
        </TooltipView>
    );
  }

  render() {

    var aStaticCollectionList = this.props.staticCollectionList;
    var aStaticCollectionContextMenuModelList = this.getStaticCollectionModelsList(aStaticCollectionList);

    return (<div className="thumbnailAddToCollectionContainer" style={this.props.style}>
      {this.getStaticCollectionView()}
      <CustomPopoverView
          className="popover-root"
          open={this.state.showStaticCollectionsPopover}
          anchorEl={this.state.staticCollection}
          anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
          transformOrigin={{horizontal: 'left', vertical: 'top'}}
          onClose={this.handleStaticCollectionsPopoverClose}
          updatePosition={this.updatePopoverPosition}
      >
        <ContextMenuItemView contextMenuViewModel={aStaticCollectionContextMenuModelList}
                             onClickHandler={this.handleAddToStaticCollectionClicked}/>
      </CustomPopoverView>
    </div>);
  }

}

ThumbnailAddToCollectionView.propTypes = oPropTypes;





export const view = StaticCollectionListContext(ThumbnailAddToCollectionView);
export const events = oEvents;
