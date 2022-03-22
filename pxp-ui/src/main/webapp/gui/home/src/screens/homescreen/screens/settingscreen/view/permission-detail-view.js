import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ContextMenuViewNew } from './../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as ExpandableNestedMenuListItemView } from '../../../../../viewlibraries/expandablenestedmenulistview/expandable-nested-menu-list-view';
import { view as HorizontalTreeView } from '../../../../../viewlibraries/horizontaltreeview/horizontal-tree-view';
import ViewUtils from './utils/view-utils';
import {view as TableViewNew} from "./table-view-new";
import {view as NothingFoundView} from "../../../../../viewlibraries/nothingfoundview/nothing-found-view";
import {view as ConfigFileUploadButtonView} from "../../../../../viewlibraries/configfileuploadbuttonview/config-file-upload-button-view";
import PermissionEntityTypeDictionary from "../../../../../commonmodule/tack/permission-entity-type-dictionary";


const oEvents = {
  FETCH_ENTITY_CHILDREN_WITH_GLOBAL_PERMISSIONS: "fetch_entity_children_with_global_permissions",
  HANDLE_PERMISSION_BUTTON_TOGGLED: "handle_permission_button_toggled",
  HANDLE_PERMISSION_SELECTION_TOGGLED: "handle_permission_selection_toggled",
  HANDLE_PERMISSION_FIRST_LEVEL_ITEM_CLICKED: "handle_permission_first_level_item_clicked",
  HANDLE_PERMISSION_TEMPLATE_ADDED: "handle_permission_template_added",
  HANDLE_PERMISSION_KLASS_ITEM_CLICKED: "handle_permission_klass_item_clicked",
  HANDLE_PERMISSION_REMOVE_TEMPLATE_CLICKED: "handle_permission_remove_template_clicked",
  FETCH_ALLOWED_TEMPLATES: "fetch_allowed_templates",
  HANDLE_ALLOWED_TEMPLATES_SEARCH: "handle_allowed_templates_search",
  HANDLE_PERMISSION_RESTORE_BUTTON_CLICKED: "handle_permission_restore_button_clicked",
  HANDLE_PERMISSION_EXPORT_BUTTON_CLICKED: "handle_permission_export_button_clicked",
  HANDLE_PERMISSION_IMPORT_BUTTON_CLICKED: "handle_permission_import_button_clicked",
};

const oPropTypes = {
  permissionsData: ReactPropTypes.object,
  leftSectionData: ReactPropTypes.object,
  treeHierarchyData: ReactPropTypes.object
};

// @CS.SafeComponent
class PermissionDetailView extends React.Component {
  constructor (oProps) {
    super(oProps);

    this.permissionSearch = React.createRef();
  }
  static propTypes = oPropTypes;

  state = {
    searchString: "",
    showPropertyCollectionDropdown: false
  };

  handlePermissionButtonToggled = (sId, sProperty, sType, bForAllChildren) => {
    EventBus.dispatch(oEvents.HANDLE_PERMISSION_BUTTON_TOGGLED, sId, sProperty, sType, bForAllChildren);
  };

  handlePermissionSelectionToggled = (sId) => {
    EventBus.dispatch(oEvents.HANDLE_PERMISSION_SELECTION_TOGGLED, sId);
  };

  handleFirstLevelItemClicked = (sId, sType) => {
    EventBus.dispatch(oEvents.HANDLE_PERMISSION_FIRST_LEVEL_ITEM_CLICKED, sId, sType);
  };

  handleKlassItemClicked = (sId, sPermissionNodeId, sType) => {
    EventBus.dispatch(oEvents.HANDLE_PERMISSION_KLASS_ITEM_CLICKED, sId, sPermissionNodeId, sType);
  };

  handlePermissionRestoreClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_PERMISSION_RESTORE_BUTTON_CLICKED);
  };

  handlePermissionExportButtonClicked = (sSelectedRole) => {
    EventBus.dispatch(oEvents.HANDLE_PERMISSION_EXPORT_BUTTON_CLICKED, sSelectedRole);
  };

  addPermissionTemplateDropdown = (aSectionIds) => {
    if(!CS.isEmpty(aSectionIds)) {
      EventBus.dispatch(oEvents.HANDLE_PERMISSION_TEMPLATE_ADDED, aSectionIds);
    }
    this.setState({
      showPermissionTemplateDropdown: false
    });
  };

  fetchAllowedTemplates =(sContext, bIsLoadMore) => {
    EventBus.dispatch(oEvents.FETCH_ALLOWED_TEMPLATES, sContext, bIsLoadMore);
  };

  handleAllowedTemplatesSearch =(sSearchText) => {
    EventBus.dispatch(oEvents.HANDLE_ALLOWED_TEMPLATES_SEARCH, sSearchText);
  };

  showPermissionTemplateDropdown = () => {
    this.setState({
      showPermissionTemplateDropdown: true
    });
  };

  handleRemoveTemplateClicked = (sId, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.HANDLE_PERMISSION_REMOVE_TEMPLATE_CLICKED, sId);
  }

  getPermissionButtonView = (sId, sType, sProperty, sPropertyClass, sDisplayText, sTooltip, bShowTree) => {
    return (
          <div className={sPropertyClass} key={sProperty + "_" + sId}>
            <TooltipView label={sTooltip}>
              <div className="buttonIcon"
                   onClick={this.handlePermissionButtonToggled.bind(this, sId, sProperty, sType, false)}>{sDisplayText}</div>
            </TooltipView>
          </div>);
  };

  getPermissionButtonViewNew = (sId, sType, sProperty, sPropertyClass, sDisplayText, sTooltip, bShowTree) => {
    var oTreeView = null;

    return (
          <div className={sPropertyClass} key={sProperty + "_" + sId}>
            {oTreeView}
            <TooltipView label={sTooltip}>
              <div className="buttonIcon"
                   onClick={this.handlePermissionButtonToggled.bind(this, sId, sProperty, sType, false)}>{sDisplayText}</div>
            </TooltipView>
          </div>);
  };

  getPermissionButtonVisibility = (oUIProperties) => {
    let oPermissionButtonVisibility = {
      hideReadButton: false,
      hideCreateButton: false,
      hideUpdateButton: false,
      hideDeleteButton: false,
    };
    if (oUIProperties) {
      CS.assign(oPermissionButtonVisibility,oUIProperties)
    }
    return oPermissionButtonVisibility;
  };

  getTreePermissionsViewNew = (sId, sType, oTreePermissionMap, oPermissionButtonVisibility) => {
    if(CS.isEmpty(oTreePermissionMap)) {
      return null;
    }
    let bHideUpdateButton = oPermissionButtonVisibility.hideUpdateButton;
    let bHideCreateButton = oPermissionButtonVisibility.hideCreateButton;
    let bHideDeleteButton = oPermissionButtonVisibility.hideDeleteButton;
    let sCreateButtonCount = "three";
    if(oPermissionButtonVisibility.hideUpdateButton){
      sCreateButtonCount = "two";
    }
    var sCreateClass = `permissionButton withTree ${sCreateButtonCount} create `;
    var sUpdateClass = "permissionButton withTree two update ";
    var sDeleteClass = "permissionButton withTree one delete ";
    var bShowButtonIcon = sType != 'tasks';

    if (oTreePermissionMap) {
      if (oTreePermissionMap.canCreate) {
        sCreateClass += "selected ";
      }
      if (oTreePermissionMap.canEdit) {
        sUpdateClass += "selected ";
      }
      if (oTreePermissionMap.canDelete) {
        sDeleteClass += "selected ";
      }
    }

    return (
        <div className="permissionButtonsView">
          {!bHideCreateButton && this.getPermissionButtonView(sId, sType, "canCreate", sCreateClass, "C", getTranslation().CREATE, bShowButtonIcon)}
          {!bHideUpdateButton && this.getPermissionButtonView(sId, sType, "canEdit", sUpdateClass, "U", getTranslation().UPDATE, bShowButtonIcon)}
          {!bHideDeleteButton && this.getPermissionButtonView(sId, sType, "canDelete", sDeleteClass, "D", getTranslation().DELETE, bShowButtonIcon)}
        </div>
    )
  };

  getPropertyCollectionPermissionsView = (sId) => {
    var _this = this;
    var oPermissionData = _this.props.permissionsData;
    var oPCPermissionMap = oPermissionData.permissionsMap.propertyCollectionPermissions;
    var sReadOnlyClass = "permissionButton four readOnly ";
    var sCanEditClass = "permissionButton three canEdit ";
    var sIsHiddenClass = "permissionButton two isHidden ";
    var sIsCollapsedClass = "permissionButton one isCollapsed ";
    if (oPCPermissionMap[sId]) {
      if (oPCPermissionMap[sId].canEdit) {
        sCanEditClass += "selected ";
      } else {
        sReadOnlyClass += "selected ";
      }
      if (oPCPermissionMap[sId].isHidden) {
        sIsHiddenClass += "selected ";
      }
      if (oPCPermissionMap[sId].isCollapsed) {
        sIsCollapsedClass += "selected ";
      }
    }

    return (
      <div className="permissionButtonsView">
        {this.getPermissionButtonView(sId, "propertycollection", "readOnly", sReadOnlyClass, "R", getTranslation().READ_ONLY)}
        {this.getPermissionButtonView(sId, "propertycollection", "canEdit", sCanEditClass, "U", getTranslation().CAN_UPDATE)}
        {this.getPermissionButtonView(sId, "propertycollection", "isHidden", sIsHiddenClass, "H", getTranslation().HIDDEN)}
        {this.getPermissionButtonView(sId, "propertycollection", "isCollapsed", sIsCollapsedClass, "Cl", getTranslation().COLLAPSED)}
      </div>
    )
  };

  getPropertyPermissionsView = (sId, sType) => {
    var _this = this;
    var oPermissionData = _this.props.permissionsData;
    var oPropertyPermissionMap = oPermissionData.permissionsMap.propertyPermissions;
    var sReadOnlyClass = "permissionButton three readOnly ";
    var sCanEditClass = "permissionButton two canEdit ";
    var sIsHiddenClass = "permissionButton one isHidden ";
    if (oPropertyPermissionMap[sId]) {
      if (!oPropertyPermissionMap[sId].isDisabled) {
        sCanEditClass += "selected ";
      } else {
        sReadOnlyClass += "selected ";
      }
      if (oPropertyPermissionMap[sId].isHidden) {
        sIsHiddenClass += "selected ";
      }
    }

    return (
        <div className="permissionButtonsView propertyPermissions">
          {this.getPermissionButtonView(sId, sType, "readOnly", sReadOnlyClass, "R", getTranslation().READ_ONLY)}
          {this.getPermissionButtonView(sId, sType, "isDisabled", sCanEditClass, "U", getTranslation().CAN_UPDATE)}
          {this.getPermissionButtonView(sId, sType, "isHidden", sIsHiddenClass, "H", getTranslation().HIDDEN)}
        </div>
    );
  };

  getLevelItemView = (
    sId,
    sLabel,
    sClass,
    sType,
    bIsFirstLevel,
    oTreePermissionMap,
    oUIProperties,
  ) => {
    var oPermissionData = this.props.permissionsData;
    var oPermissionsView = null;
    var oLevelEyeView = null;
    var sClassName = "levelItem " + sClass;
    var sSelectedFirstLevelId = oPermissionData.selectedFirstLevelId;
    var sSelectedPropertyCollection = oPermissionData.selectedPropertyCollection;
    var sSelectedTemplate = oPermissionData.selectedTemplate;
    var bIsPermissionModeClass = oPermissionData.isPermissionModeClass;

    switch (sType) {
      case "propertycollection":
        oPermissionsView = this.getPropertyCollectionPermissionsView(sId, sType);
        break;

      case "attribute":
      case "tag":
      case "role":
      case "relationship":
        oPermissionsView = this.getPropertyPermissionsView(sId, sType);
        break;

      default:
        var _this = this;
        var aIgnoreTypes = ["context", "tasks", "taxonomy"];
          var sReadClass = "permissionButton withTree read ";
          if (!CS.isEmpty(oTreePermissionMap) && oTreePermissionMap.canRead) {
            sReadClass += "selected ";
          }

          sType = sType || "taxonomy";
          if (sType != "template") {
            var bShowButtonIcon = sType != 'tasks';
            let oPermissionButtonVisilibity = _this.getPermissionButtonVisibility(oUIProperties);
            let bHideReadButton = oPermissionButtonVisilibity.hideReadButton;
            oPermissionsView = _this.getTreePermissionsViewNew(sId, sType, oTreePermissionMap, oPermissionButtonVisilibity);
            oLevelEyeView = !CS.isEmpty(oTreePermissionMap) && !bHideReadButton && _this.getPermissionButtonViewNew(sId, sType, "canRead", sReadClass, "R", getTranslation().READ, bShowButtonIcon) || null;
          }
          var oLabelView1 = (<div className="levelItemLabel">{sLabel}</div>);
          if (sType != "tasks" && sType != "contexts" && sType != "taxonomies") {
            var sLabelClassName1 = "levelItemLabel firstLevel ";
            if ((bIsPermissionModeClass && sId == sSelectedFirstLevelId) ||
                (!bIsPermissionModeClass && sId == sSelectedPropertyCollection) ||
                (!bIsPermissionModeClass && sId == sSelectedTemplate)) {
              sLabelClassName1 += "selected";
            }
            var oHandler = null;
            if(bIsFirstLevel) {
              oHandler = _this.handleFirstLevelItemClicked.bind(_this, sId, sType);
              var aToIgnore = aIgnoreTypes.slice(0, aIgnoreTypes.length - 1);
              if(CS.includes(aToIgnore, sType)) {
                sLabelClassName1 = "levelItemLabel";
              }
            } else {
              var oActiveNatureClassDetails = oPermissionData.activeNatureClassDetails;
              if(sId == oActiveNatureClassDetails.id) {
                sLabelClassName1 += "selected";
              }
              var sPermissionNodeId = oTreePermissionMap.id;
              oHandler = !CS.includes(aIgnoreTypes, sType) && sId != "classes" &&
                  _this.handleKlassItemClicked.bind(_this, sId, sPermissionNodeId, sType) || CS.noop;
              if(CS.includes(aIgnoreTypes, sType)) {
                sLabelClassName1 = "levelItemLabel";
              }
            }
            oLabelView1 = (<div className={sLabelClassName1}
                                onClick={oHandler}>{sLabel}</div>);
          }
          return (
              <div className={sClassName}>
                {oLevelEyeView}
                {oLabelView1}
                {oPermissionsView}
              </div>);

    }

    var oLabelView = (<div className="levelItemLabel">{sLabel}</div>);
    if (sType != "tasks" && sType != "contexts" && sType != "taxonomies") {
      var sLabelClassName = "levelItemLabel firstLevel ";
      if ((bIsPermissionModeClass && sId == sSelectedFirstLevelId) ||
          (!bIsPermissionModeClass && sId == sSelectedPropertyCollection) ||
          (!bIsPermissionModeClass && sId == sSelectedTemplate)) {
        sLabelClassName += "selected";
      }
      oLabelView = (<div className={sLabelClassName}
                         onClick={this.handleFirstLevelItemClicked.bind(this, sId, sType)}>{sLabel}</div>);
    }
    return (
        <div className={sClassName}>
          {oLevelEyeView}
          {oLabelView}
          {oPermissionsView}
        </div>
    );
  };

  getLabelWithSubstring = (sLabel, sSearchString) => {
    var rPattern = new RegExp(sSearchString, "gi");
    var aList = CS.split(sLabel, rPattern);
    var aMatchedList = sLabel.match(rPattern);
    aMatchedList = CS.map(aMatchedList, function (sMatch) {
      return (<span className="highlight">{sMatch}</span>)
    });
    var aLabelList = CS.compact(CS.flatten(CS.zip(aList, aMatchedList)));
    return aLabelList;
  };

  getLevelView = (aLevelItems, bIsFirstLevel) => {
    var _this = this;
    var aNewLevelItems = aLevelItems;
    var oPermissionData = _this.props.permissionsData;
    var bIsPermissionModeClass = oPermissionData.isPermissionModeClass;
    if (!bIsFirstLevel && !bIsPermissionModeClass) {
      return [oPermissionData.templatePermissionView];
    }
    if (CS.isEmpty(aLevelItems)) {
      return [];
    }
    var aClassRootViews = [];

    var aChildren = [];

    //It is assumed that on second level only classes will come
    if(!bIsFirstLevel){
      aNewLevelItems = [];
      CS.forEach(aLevelItems, function(oClassGroup){
        if(oClassGroup.type == "klass"){

          var oNatureClassGroup = {
            id: oClassGroup.id,
            type: oClassGroup.type,
            label: getTranslation().NATURE,
            children: []
          };

          var oNonNatureClassGroup = {
            id: oClassGroup.id,
            type: oClassGroup.type,
            label: getTranslation().NON_NATURE,
            children: []
          };

          CS.forEach(oClassGroup.children, function(oChild){
            if(oChild.isNature){
              oNatureClassGroup.children.push(oChild);
            }else{
              oNonNatureClassGroup.children.push(oChild);
            }
          });
          aNewLevelItems.push(oNatureClassGroup);
          aNewLevelItems.push(oNonNatureClassGroup);
        }
      });
    }

    var sSearchString = this.state.searchString;
    var oClassVisibilityStatus = oPermissionData.classVisibilityStatus;
    CS.forEach(aNewLevelItems, function (oClassGroup) {
      if (!CS.isEmpty(oClassGroup.children) && oClassVisibilityStatus[oClassGroup.id] != false) {
        var oLevelItemView = _this.getLevelItemView(oClassGroup.id, oClassGroup.label, "levelGroupItem", oClassGroup.type, false, {});
        aClassRootViews.push(oLevelItemView);
        CS.forEach(oClassGroup.children, function (oChild) {
          var oTreePermissionMap = oChild.globalPermission || {};
          let oUIProperties = oChild.uiProperties;
          oChild.type = oChild.type ? oChild.type : oClassGroup.type;
          var sChildClassName = "";
          var sLabel = oChild.label;
          if (sSearchString) {
            if (CS.includes(CS.toLower(sLabel), CS.toLower(sSearchString))) {
              sLabel = _this.getLabelWithSubstring(sLabel, sSearchString);
            } else {
              sChildClassName += "notMatched";
            }
          }
          var sType = bIsPermissionModeClass ? oChild.type : "template";
          var oLevelItemView = _this.getLevelItemView(oChild.id, sLabel, sChildClassName, sType, bIsFirstLevel, oTreePermissionMap, oUIProperties);
          aClassRootViews.push(oLevelItemView);
          aChildren.push(oChild);
        });
      }
    });

    if (CS.isEmpty(aClassRootViews)) {
      return [];
    }
    var oLevelView = (
        <div className="level">
          {aClassRootViews}
        </div>
    );
    var aChildrenViews = this.getLevelView(aChildren);
    aChildrenViews.unshift(oLevelView);
    return aChildrenViews;
  };

  getAllowedTemplatesSection = () => {
    let oPermissionData = this.props.permissionsData;
    let oActiveNatureClassDetails = oPermissionData.activeNatureClassDetails;
    let aPermissionTemplateModels = oPermissionData.addAllowedTemplatesModels;
    let aAddedTemplatesData = oPermissionData.selectedAllowedTemplatesData;
    if (oActiveNatureClassDetails.type != "klass" || !oActiveNatureClassDetails.isNature) {
      return null;
    }
    let oSelectedRole = oPermissionData.selectedRole;

    let oAnchorOrigin = {
      horizontal: 'left',
      vertical: 'bottom'
    };

    let oTargetOrigin = {
      horizontal: 'right',
      vertical: 'bottom'
    };

    return (
        <div className="allowedTemplatesSection">
          <div className="permissionTemplateContainer">
            <div className="permissionTemplateWrapper">
              <div className="permissionTemplateHeader">
                <div className="headerLabel">{getTranslation().TEMPLATES}</div>
                <ContextMenuViewNew
                    contextMenuViewModel={aPermissionTemplateModels}
                    isMultiselect={true}
                    onApplyHandler={this.addPermissionTemplateDropdown}
                    onPopOverOpenedHandler={this.fetchAllowedTemplates}
                    searchText={ViewUtils.getEntitySearchText()}
                    searchHandler={this.handleAllowedTemplatesSearch}
                    anchorOrigin={oAnchorOrigin}
                    targetOrigin={oTargetOrigin}
                    context="templates"
                    loadMoreHandler={this.fetchAllowedTemplates.bind(this, "templates", true)}
                >
                  {oSelectedRole.isReadOnly ? null : <TooltipView placement="bottom" label={"Add Template"}>
                    <div className="addTemplateHandler" onClick={this.showPermissionTemplateDropdown}/>
                  </TooltipView>}
                </ContextMenuViewNew>
              </div>
              <div className="permissionTemplateBody">
                {this.getAddedTemplate(aAddedTemplatesData, oSelectedRole.isReadOnly)}
              </div>
            </div>
          </div>
        </div>
    );
  }

  getTableViewNew = (oPermissionData) => {
    let aRow = [];
    CS.forEach(oPermissionData, function (oProperty, key) {
      let oTableData = <div className="tableWrapper">
        <TableViewNew
            sectionHeaderData={oProperty.headerData}
            sectionBodyData={oProperty.rowData}
            context={key}
            screenContext = {oProperty.screenContext}
            showCode={oProperty.showCode}
        />
      </div>;
      aRow.push(oTableData)
    });
    return aRow
  };

  getImpExpButtonView = (sSelectedRole) => {
    return (
        <React.Fragment>
          <ConfigFileUploadButtonView label={getTranslation().IMPORT}
                                      context={"permission"}
                                      className={"importButton"}
          />
          <TooltipView label={getTranslation().EXPORT} >
            <div className="exportButton"
                 onClick={this.handlePermissionExportButtonClicked.bind(this, sSelectedRole)}></div>
          </TooltipView>

        </React.Fragment>
    )
  };

  getImpExpHeaderView = (sSelectedRole) => {
    let oTreeHierarchyData = this.props.treeHierarchyData;
    let sClassLabel = oTreeHierarchyData.oHierarchyTree.label;
    let sEntityType = oTreeHierarchyData.entityType;
    if (sEntityType === PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION) {
      return <NothingFoundView message={getTranslation().NO_PROPERTIES_AVAILABLE}/>
    }
    return (
        <div className="classPermissionDetailsView">
          <div className="permissionWrapper">
            <div className="permissionHeaderWrapper">
              <div className="classLabel">{sClassLabel}</div>
              <div className={"actionButtonWrapper"}>
                {this.getImpExpButtonView(sSelectedRole)}
              </div>
            </div>
            <NothingFoundView message={getTranslation().NO_PROPERTIES_AVAILABLE}/>
          </div>
        </div>
    )
  };

  getClassPermissionDetailsView = (oActiveNatureClassDetails) => {
    let sClassLabel = oActiveNatureClassDetails.label;
    let oPermissionData = this.props.permissionsData;
    let sSelectedRole = oPermissionData.selectedRole.code;
    let oAllowedTemplatesSectionView = this.getAllowedTemplatesSection();
    let oImpExpHeaderView = CS.isEmpty(oPermissionData.permissionData) || (CS.isEmpty(oPermissionData.permissionData.generalInformationTableData) &&
        CS.isEmpty(oPermissionData.permissionData.propertyCollectionData) &&
        CS.isEmpty(oPermissionData.permissionData.relationshipTableData))
        ? this.getImpExpHeaderView(sSelectedRole) : null;

    return (
            <div className="classPermissionDetailsView">
              {CS.isNotEmpty(oImpExpHeaderView) ? oImpExpHeaderView :
               <div className="permissionWrapper">
                <div className="permissionHeaderWrapper">
                  <div className="classLabel">{sClassLabel}</div>
                  <div className={"actionButtonWrapper"}>
                    {this.getImpExpButtonView(sSelectedRole)}
                  {sClassLabel && <div className="restore-btn" onClick={this.handlePermissionRestoreClicked}>Restore</div>}
                  </div>
                </div>
                <div className="detailedPermissionsSection">
                  {this.getTableViewNew(oPermissionData.permissionData.generalInformationTableData)}
                  {this.getTableViewNew(oPermissionData.permissionData.propertyCollectionData)}
                  {this.getTableViewNew(oPermissionData.permissionData.relationshipTableData)}
                </div>
              {oAllowedTemplatesSectionView}
               </div>}
            </div>
        );
  }

  getAddedTemplate = (aAddedTemplatesData, bIsReadOnlyRole) => {
    let that = this;
    let aTemplates = [];

    CS.forEach(aAddedTemplatesData, function (oTemplate) {
      if(oTemplate) {
        let sDefaultClass = "";
        aTemplates.push(<div className="templateItemWrapper">
          <div className={"itemTemplate" + sDefaultClass}>{oTemplate.label}</div>
          {bIsReadOnlyRole ? null :
           <div className="itemTemplateHandlerWrapper">
            <div className="removeItemTemplate" onClick={that.handleRemoveTemplateClicked.bind(that, oTemplate.id)}></div>
          </div>}
        </div>);
      }
    });
    return (<div className="addedTemplateWrapper">{aTemplates}</div>);
  };

  onListItemClick = (sItemId, sParentId) => {
    EventBus.dispatch(oEvents.FETCH_ENTITY_CHILDREN_WITH_GLOBAL_PERMISSIONS, sItemId, sParentId);
  };

  getPermissionsView = () => {

    let __props = this.props;
    let oLeftSectionData = __props.leftSectionData;
    let fOnListItemClick = this.onListItemClick;
    let oExpandableMenuView = (
        <ExpandableNestedMenuListItemView
            context="permissionConfig"
            linkItemsData={oLeftSectionData.linkItemsData}
            selectedItemId={oLeftSectionData.selectedItemId}
            onItemClick={fOnListItemClick}
            selectDefaultItem={fOnListItemClick}
            isNested={oLeftSectionData.isNested}
            itemsValuesMap={oLeftSectionData.leftNavigationTreeValuesMap}
        />
    );

    let oTreeView = (
        <HorizontalTreeView
            contentHierarchyData={__props.treeHierarchyData}
            showCode={true}
        />
    );

    var oPermissionData = __props.permissionsData;
    let bHideNoPropertiesSectionView = oPermissionData.bHideNoPropertiesSectionView;
    var oPropertyPermissionData = oPermissionData.activeNatureClassDetails;
    let oPropertyPermissionView = !bHideNoPropertiesSectionView ? this.getClassPermissionDetailsView(oPropertyPermissionData)
                                                                : null;

    return (
        <div className="permissionsContainer">
          <div className="leftContainer">
            {oExpandableMenuView}
          </div>
          <div className="rightContainer">
            {oTreeView}
            {oPropertyPermissionView}
          </div>
        </div>
    );
  };

  render() {
    var oDetailedView = this.getPermissionsView();
    return (
        <div className="permissionDetailedView">
          {oDetailedView}
        </div>
    );
  }
}

export const view = PermissionDetailView;
export const events = oEvents;
