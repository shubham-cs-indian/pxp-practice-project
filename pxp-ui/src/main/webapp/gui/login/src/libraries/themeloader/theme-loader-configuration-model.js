import {hexToFilter} from "css-filter-generator";
import ColorUtils from '../../commonmodule/util/color-utils.js';

export default function (oThemeConfigurations) {
  return [
    {
      id: "generalThemeColor",
      rules: `background-color : ${oThemeConfigurations.generalThemeColor}`,
      klassesSelectors: [".newHeaderMenuContainer",
                         ".searchInputField",
                         ".fullScreenView.fullScreen .fullScreenHeader",
                         ".contentComparisonFullScreenViewWrapper .fullScreenView.fullScreen .fullScreenHeader, .contentListViewWrapper .fullScreenView.fullScreen .fullScreenHeader",
                         ".contentScreenContainer .filterSearchViewContainer .taxonomySelectorContainer",
                         ".contentScreenContainer .filterSearchViewContainer .collectionOptionsContainer .staticCollectionsSection", ".aboutDialogViewContent .aboutDialogWrapper .aboutDialogOtherInfo"]
    },
    {
      id: "headerFontColor",
      rules: `color : ${oThemeConfigurations.generalFontColor}`,
      klassesSelectors: [".newHeaderMenuContainer",
                         ".selectedLanguageLabel",
                         ".selectTaxonomyButtonLabel",
                         ".searchBarContainer .searchInput",
                         ".searchBarContainer .searchInput::placeholder",
                         ".searchInputField .searchInput::placeholder",
                         ".searchBarContainer .searchClearIcon",
                         ".settingScreenBodyViewContainer .searchButtonWrapper",
                         ".searchInputField .searchInputFieldBar",
                         ".searchButtonWrapper .searchLabel",
                         ".searchContainerSection .searchInputField",
                         ".physicalCatalogLabel",
                         ".searchContainerWrapper .searchInput",
                         ".searchContainerWrapper .searchInput::placeholder",
                         ".fullScreenView.fullScreen .fullScreenHeader .fullScreenHeaderLabel",
                         ".aboutDialogViewContent .aboutDialogWrapper .aboutDialogOtherInfo .aboutDialogInfoStrip .aboutDialogInfoKey",
                         ".aboutDialogViewContent .aboutDialogWrapper .aboutDialogOtherInfo .aboutDialogInfoStrip .aboutDialogInfoVal",
                         ".aboutDialogViewContent .aboutDialogWrapper .aboutDialogOtherInfo .aboutDialogInfoStrip .aboutDialogInfoKey",
                         ".aboutDialogViewContent .aboutDialogWrapper .aboutDialogOtherInfo .aboutDialogInfoStrip" +
                         " .aboutDialogInfoVal", ".aboutDialogViewContent .aboutDialogWrapper .aboutDialogOtherInfo" +
                         " .aboutDialogInfoStrip .aboutDialogInfoKey",
                         ".customDialogTitleView"]
    },
    {
      id: "headerIconColor",
      rules: hexToFilter(oThemeConfigurations.generalFontColor).filter,
      klassesSelectors: [".menuItem",
                         ".userImageWrapper.noImage",
                         ".physicalCatalogIcon",
                         ".uiLanguageImage",
                         ".dataLanguageImage",
                         ".physicalCatalogSelectorIcon .buttonIcon",
                         ".staticCollectionsSection .staticCollectionsButton",
                         ".dynamicCollectionsSection .dynamicCollectionsButton",
                         ".searchBarContainer .searchInputIcon",
                         ".searchButtonWrapper .searchButton",
                         ".searchInputField .searchInputFieldBar .searchIcon",
                         ".searchInputField .searchInputFieldBar .crossIcon",
                         ".selectTaxonomyButton .downArrowIcon",
                         ".filterSearchViewContainer .searchBarWrapper .searchBarContainer .searchClearIcon",
                         ".searchContainerWrapper .searchIcon",
                         ".searchContainerWrapper .crossIcon",
                         ".fullScreenView.fullScreen .fullScreenHeader .fullScreenButtonContainer .fullScreenButton"]
    },
    {
      id: "selectionIconColor",
      rules: hexToFilter(oThemeConfigurations.generalSelectionColor).filter,
      klassesSelectors: [".toolbarItemNew.toolUnCheckAll", ".thumbnailTemplateHeaderView .thumbnailCheckButtonIcon.isSelected",
                         ".horizontalTreeGroupElContainer .treeNodeCheck.checked",
                         ".gridView.gridViewContainer .gridViewMiddleSection .gridViewMiddleSectionWrapper" +
                         " .gridViewRow .gridViewCell.selectOption .selectIcon.isSelected",
                         ".detailedListItemContainer .basicTemplateContainerZoom2.PIM .checkButton.isSelected .checkButtonIcon",
                         ".contentEditViewContainer .linkedContentsAndPanelWrapper .contentDetailsWrapper" +
                         " .contentInformationSidebarViewContainer .imageAndVersionContainer .defaultImageIconContainer",
                         ".klassTreeViewContainer .horizontalTreeViewContainer .horizontalTreeLevelContainer" +
                         " .horizontalTreeGroupContainer .horizontalTreeGroupHeader .treeNodeCheck.checked",
                         ".filterItemWrapper .filterItemChildrenContainer .filterItemChildContainer .filterItemChildCheckbox.checked"]
    },
    {
      id: "dialogBackgroundColor",
      rules: `background-color : ${oThemeConfigurations.generalThemeColor}`,
      klassesSelectors: [".customDialogTitleView"],
    },
    {
      id: "headerSearchConfig",
      rules: `border-color : ${ColorUtils.shadeBlendConvert(-0.10, oThemeConfigurations.generalThemeColor)};`,
      klassesSelectors: [".settingScreenBodyViewContainer .searchButtonWrapper",
                         ".newHeaderMenuRightSideContainer .headerMenuSubContainer .uiAndDataLanguageView",
                         ".searchContainerSection .searchInputFieldBar",
                         ".filterSearchViewContainer .taxonomySelectorContainer",
                         ".filterSearchViewContainer .searchBarWrapper",
                         ".filterSearchViewContainer .staticCollectionsSection",
                         ".settingScreenBodyViewContainer .searchContainerWrapper"],
    },
    {
      id: "headerSearchConfigForLanguage",
      rules: `border-left-color : ${ColorUtils.shadeBlendConvert(-0.10, oThemeConfigurations.generalThemeColor)};`,
      klassesSelectors: [".uiAndDataLanguageInfoContainer .dataLanguageWrapper"],
    },
    {
      id: "generalButtonBackgroundColor",
      rules: `background : ${oThemeConfigurations.generalButtonBackgroundColor};`,
      klassesSelectors: [".createButtonViewWrapper",
                         ".themeConfigurationViewContainer .snackBarView button",
                         ".raisedButton"],
    },
    {
      id: "generalButtonFontColor",
      rules: `color : ${oThemeConfigurations.generalButtonFontColor} !important;`,
      klassesSelectors: [".themeConfigurationViewContainer .snackBarView ", ".raisedButton > span", ".createButtonViewWrapper .createButtonView .buttonText"],
    },
    {
      id: "generalButtonFontColor",
      rules: hexToFilter(oThemeConfigurations.generalButtonFontColor).filter,
      klassesSelectors: [".createButtonViewWrapper .createButtonView .buttonImage"],
    },
    {
      id: "generalSelectionColor",
      rules: `--box-shadow-color : ${oThemeConfigurations.generalSelectionColor};
        box-shadow: 0 1px 0 0 #fff, 0 0 0 1px #eee, inset 0 3px 0 0 var(--box-shadow-color);`,
      klassesSelectors: [".dashboardScreenControllerWrapper .dashboardScreenController .tabLayoutView .tabsSection .tabsList .tabItem.isActive ",
                         ".settingScreenContainerWrapper .settingScreenViewContainer .settingScreenBodyViewContainer .linksContainer .expandableMenuListView .listItemsSection .listItem.selected::after",
                         ".contentScreenWrapperView .mainSection > .expandableMenuListView .listItemsSection .listItem.selected::after",
                         ".contentEditViewContainer .linkedContentsAndPanelWrapper .contentDetailsWrapper .contentDetailViewContainer .tabLayoutView .tabsSection .tabItem.isActive"],
    },
    {
      id: "generalSelectionColorForGrid",
      rules: `--box-shadow-color : ${oThemeConfigurations.generalSelectionColor};
        box-shadow: inset 0 0 0 1px var(--box-shadow-color);`,
      klassesSelectors: [".gridView.gridViewContainer .gridViewMiddleSection .gridViewMiddleSectionWrapper" +
                         " .gridViewRow .gridViewCell.activeCell", ".contentHierarchyTreeViewContainer" +
                         ".contentHorizontalTreeViewContainer .contentHorizontalTreeLevelContainer .contentHorizontalTreeGroupContainer .contentHorizontalTreeGroupNodes .contentHorizontalTreeGroupElContainer.active",
                         ".selectionToggleView .tagElement.tagElementSelected", ".contentHierarchyTreeViewContainer" +
                         ".contentHorizontalTreeViewContainer .contentHorizontalTreeLevelContainer .contentHorizontalTreeGroupContainer .contentHorizontalTreeGroupNodes .contentHorizontalTreeGroupElContainer.active",
                         ".contentHierarchyTreeViewContainer .contentHorizontalTreeViewContainer .contentHorizontalTreeLevelContainer .contentHorizontalTreeGroupContainer .contentHorizontalTreeGroupNodes .contentHorizontalTreeGroupElContainer.active"]
    },
    {
      id: "generalSelectionColor2",
      rules: ` background :  ${oThemeConfigurations.generalSelectionColor}`,
      klassesSelectors: [".contentScreenWrapperView .mainSection > .expandableMenuListView .listItemsSection .listItem.selected::after",
                         ".settingScreenContainerWrapper .settingScreenViewContainer .settingScreenBodyViewContainer .linksContainer .expandableMenuListView .listItemsSection .listItem.selected::after "],
    },
    {
      id: "generalSelectionColor3",
      rules: ` border:  1px solid ${oThemeConfigurations.generalSelectionColor}`,
      klassesSelectors: [" .contentLinkedSectionsView .linkedSectionsContainer .linkedSection .sectionWrapper.selected > div"]
    },
    {
      id: "generalSelectionColor3",
      rules: ` border : 1px solid  ${oThemeConfigurations.generalSelectionColor}`,
      klassesSelectors: [" .thumbnailTemplateViewNewWrapper.selected"]
    },
    {
      id: "generalSelectionColor4",
      rules: `border-bottom: solid 1px  ${oThemeConfigurations.generalSelectionColor}`,
      klassesSelectors: [" .thumbnailTemplateViewNewWrapper.selected .thumbnailTemplateViewContainer .imageContainer"]
    },
    {
      id: "generalSelectionColor5",
      rules: ` box-shadow: 0px 0px 2px 1px ${oThemeConfigurations.generalSelectionColor}`,
      klassesSelectors: [" .rulerTagTypeView .rulerTagIconLabel.rulerTagIconLabelSelected"]
    },
    {
      id: "generalSelectionColor6",
      rules: ` box-shadow:inset 0px 0px 0px 1px ${oThemeConfigurations.generalSelectionColor}, -1px 0px 0px 0px #ddd, 1px 0px 0px 0px #ddd`,
      klassesSelectors: [".imageSliderView .imagesContainer .sliderImageWrapper.selected"]
    },
    {
      id: "generalSelectionColor9",
      rules: `box-shadow: 0px 0px 0px 2px ${oThemeConfigurations.generalSelectionColor};  border: none;`,
      klassesSelectors: [".sectionElementNew.selected "]
    },
    {
      id: "generalButtonBackgroundColor1",
      rules: `background-color : ${oThemeConfigurations.generalButtonBackgroundColor} !important;`,
      klassesSelectors: [".raisedButton"]
    },
    {
      id: "generalButtonBackgroundColorCreateButton",
      rules: `border-color : ${oThemeConfigurations.generalButtonBackgroundColor};`,
      klassesSelectors: [".createButtonViewWrapper"]
    },

    {
      id: "dialogHeaderTabBorderColor",
      rules: `border-color : ${oThemeConfigurations.generalThemeColor};`,
      klassesSelectors: [".multiClassificationToolbar.clubbedButtonsWrapper", ".comparisonViewTypes.clubbedButtonsWrapper"]
    },
    {
      id: "dialogSelectedHeaderTabColor",
      rules: `background-color :  ${oThemeConfigurations.generalThemeColor}`,
      klassesSelectors: [".multiClassificationToolbar.clubbedButtonsWrapper .customButtonContainer" +
                         " .customButton.active .textContent.light",
                         ".comparisonViewTypes.clubbedButtonsWrapper .customButtonContainer" +
                         " .customButton.active .textContent.light"]
    },
    {
      id: "dialogDeselectedHeaderTabFontColor",
      rules: `color : ${oThemeConfigurations.generalThemeColor}`,
      klassesSelectors: [".multiClassificationToolbar.clubbedButtonsWrapper .customButtonContainer" +
                         " .customButton .iconTextWrapper .textContent.light",
                         ".comparisonViewTypes.clubbedButtonsWrapper .customButtonContainer" +
                         " .customButton .iconTextWrapper .textContent.light"]
    },
    {
      id: "dialogSelectedHeaderTabFontColor",
      rules: `color : ${oThemeConfigurations.dialogFontColor}`,
      klassesSelectors: [".multiClassificationToolbar.clubbedButtonsWrapper .customButtonContainer" +
                         " .customButton.active .iconTextWrapper .textContent.light",
                         ".comparisonViewTypes.clubbedButtonsWrapper .customButtonContainer" +
                         " .customButton.active .iconTextWrapper .textContent.light"]
    },
    {
      id: "fullScreenSelectedHeaderTabFontColor",
      rules: `color : ${oThemeConfigurations.headerIconColor};`,
      klassesSelectors: [".bulkEditToolbar.clubbedButtonsWrapper .customButtonContainer .customButton.active .textContent.light"]
    }
  ]
};