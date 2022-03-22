import React, {Fragment} from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import {view as GridView} from './../../../../../viewlibraries/gridview/grid-view';
import {view as CustomDialogView} from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
const getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  BULK_ASSET_LINK_SHARING_ACTION_ITEM_CLICKED: "bulk_asset_link_sharing_action_item_clicked",
};

const oPropTypes = {
  assetLinkSharingGridViewData: ReactPropTypes.object,
};

// @CS.SafeComponent
class AssetLinkSharingView extends React.Component {
  constructor (props) {
    super(props);

    this.state = {}
  }

  static propTypes = oPropTypes;

  handleButtonClick = (sButtonId) => {
    let oAssetShareData = {
      bIsTechnicalImage: false,
      bIsShareSelected: true
    };
    EventBus.dispatch(oEvents.BULK_ASSET_LINK_SHARING_ACTION_ITEM_CLICKED, sButtonId, oAssetShareData);
  };

  closeLinkSharingDialog = () => {
    EventBus.dispatch(oEvents.BULK_ASSET_LINK_SHARING_ACTION_ITEM_CLICKED, "cancel", {});
  };

  getBulkLinkSharingDialog = () => {

    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true
      },
      {
        id: "share",
        label: getTranslation().SHARE,
        isFlat: false
      },
    ];

    let oAssetLinkSharingGridViewData = this.props.assetLinkSharingGridViewData;
    let oSkeleton = oAssetLinkSharingGridViewData.gridViewSkeleton;
    let aVisualData = oAssetLinkSharingGridViewData.gridViewVisualData;
    let aGridViewData = oAssetLinkSharingGridViewData.gridViewData;
    let iTotalNestedItems = oAssetLinkSharingGridViewData.totalNestedItems;
    let oScrollbarStyle = {
      height: "calc(100% - 10px)"
    };

    let oContentStyle = {height: "60%", width: '85%', maxWidth: '768px'};

    let oDialogGridView = (<div className={"bulkAssetLinkSharingDialogGridView"}>
      <GridView skeleton={oSkeleton}
                data={aGridViewData}
                visualData={aVisualData}
                totalNestedItems={iTotalNestedItems}
                isGridDataDirty={false}
                showCheckboxColumn={true}
                disableDeleteButton={true}
                disableCreate={true}
                enableImportExportButton={false}
                disableRefresh={true}
                hideUpperSection={true}
                showContentCheckBox={false}
                scrollbarStyle={oScrollbarStyle}
      />
    </div>);
    let oLinkSharingGridView = (
        <Fragment>
          <div className={"selectFromGridView"}>
            <div className={"selectAssetsLabel"}>{getTranslation().SELECT_ASSETS_TO_SHARE}</div>
          </div>
          {oDialogGridView}
        </Fragment>);

    let sClassName = "bulkAssetLinkSharingDialog";
    let sButtonLabel = getTranslation().SHARE;

    let oDialogView = (<div className={sClassName}>
      {oLinkSharingGridView}
    </div>);

    return (
        <CustomDialogView
            modal={true}
            open={true}
            onRequestClose={this.closeLinkSharingDialog}
            title={sButtonLabel}
            buttonData={aButtonData}
            buttonClickHandler={this.handleButtonClick}
            contentStyle={oContentStyle}>
          {oDialogView}
        </CustomDialogView>
    );
  };

  render () {

    return (
        <div className="bulkLinkSharingDialogContainer">
          {this.getBulkLinkSharingDialog()}
        </div>
    );
  }
}

export const view = AssetLinkSharingView;
export const events = oEvents;
