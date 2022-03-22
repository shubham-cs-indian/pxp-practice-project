import CS from '../../../../../../../libraries/cs';
import SettingScreenProps from "../../model/setting-screen-props";
import SettingUtils from "../setting-utils";
import {AuditLogRequestMapping} from "../../../tack/setting-screen-request-mapping"
import Moment from "moment";
import GridViewStore from "../../../../../../../viewlibraries/contextualgridview/store/grid-view-store";
import GridViewContexts from "../../../../../../../commonmodule/tack/grid-view-contexts";

class AuditLogProvider {
  static preparePostData() {
    let oScreenProps = SettingScreenProps.screen;
    let sSelectedLeftTreeNode = oScreenProps.getSelectedLeftNavigationTreeItem();
    let sSelectedParentId = oScreenProps.getSelectedLeftNavigationTreeParentId();
    let oSelectedLeftTreeNode = SettingUtils.getSelectedTreeItemById(sSelectedLeftTreeNode, sSelectedParentId);

    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.AUDIT_LOG);
    delete oPostData.searchText;
    delete oPostData.searchBy;
    oPostData.types = oSelectedLeftTreeNode.types;

    let oAuditLogProps = SettingScreenProps.auditLogProps;
    let aSearchFilterData = oAuditLogProps.getSearchFilterData();
    aSearchFilterData = aSearchFilterData.clonedObject || aSearchFilterData;
    let aAppliedFilterData = oAuditLogProps.getAppliedFilterData();

    CS.forEach(aSearchFilterData, function (oSearchData) {
      oPostData[oSearchData.id] = oSearchData.value;
    });

    CS.forEach(aAppliedFilterData, function (oAppliedFilter) {
      oPostData[oAppliedFilter.id] = oAppliedFilter.children.map(oChild => oChild.id);
    });

    delete oPostData.date;

    let oDateFilter = CS.find(aAppliedFilterData, {id: "date"});

    if(CS.isNotEmpty(oDateFilter))
      if(oDateFilter.isDefault) {
        oPostData.fromDate = +(Moment().subtract(6, 'days'));
        oPostData.toDate = +(Moment());
      } else {
        let oRange = oDateFilter.value;
        oPostData.fromDate = oRange.startTime;
        oPostData.toDate = oRange.endTime;
      }

      return oPostData;
  }

  static fetchAuditLogListForGridView(fSuccess, fFailure){
    let oPostData = this.preparePostData();
    SettingUtils.csPostRequest(AuditLogRequestMapping.getAuditLogListForGridView, {}, oPostData, AuditLogProvider.success.bind(this, fSuccess), fFailure);
  }

  static success(fCallback, oResponse){
    return fCallback(oResponse.success);
  }
}

export default AuditLogProvider;