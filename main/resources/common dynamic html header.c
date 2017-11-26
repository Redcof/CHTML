<!-- DYNAMIC ANNOTATION 2 START header2.html-->
</head>
<body>
<div class='page-loader' id='pre-loader'>
<div></div>
&nbsp;Preparing
</div>
<ul id='notification'></ul>
<div class='popup-menu' id='popup-menu'></div>
<div id='popup'>
<div class='blur-overlay'></div>
<div id='popup-box'>
<span id='popup-heading'></span>
<span title='Close' id='close-popup-box' onclick='__mbcs_hidePopup();'>&times;</span>
<div id='popup-content' style='text-align: left;padding: 27px 5px 5px 5px;'></div>
<div id='popup-action-buttons'>
<span class='buttons'>OK</span><span class='buttons'>Cancel</span>
</div>
</div>
</div>
<header id='fixed-header'>
<span class='fixed-header-part1'>
<img id='logo' class='link' data-click='http://www.mbcontrol.com'/>
<span class='show-clock' title='System local time'></span>
</span>
<h3 class='heading'>
<?chtml IP_WEBS_SendString(pOutput,pPageHeaderTitle); ?>
</h3>
<h3 class='config-file-name configuration-file-status-success' data-config-file-info='File created <?chtml 
IP_WEBS_SendString(pOutput,FileTimeChar);
?>' title=''>
<span><?chtml IP_WEBS_SendString(pOutput,pConfFileName); ?></span>
</h3>
<h3 class='user-account'>
<span>
<span>Username: </span>
<span style='color: #3875b3'><?chtml IP_WEBS_SendString(pOutput,pUaseName); ?></span>
<span>Role: </span>
<span style='color: #3875b3'><?chtml IP_WEBS_SendString(pOutput,pUserRole); ?></span>
</span>
<span class='logout-button link' data-click='<?chtml IP_WEBS_SendString(pOutput,pLogoutURL); ?>'>Logout</span>
</h3>
</header>
<div class='close-menu-icon menu-icon menu-trigger' title='Open or close menu'>&DoubleLongLeftArrow;</div>
<!-- Responsive Info button setup - start-->
<div id='mbcs-responsive-info-trigger' style='display: none'>i</div>
<div id='mbcs-responsive-info-content' style='display: none'>
<!--<span class='caret-up mbcs-responsive-info-caret'></span>-->
<ul class='info-content'>
<li>
<span title='File created <?chtml 
IP_WEBS_SendString(pOutput,FileTimeChar);
?>' class='configuration-file-status-success'><?chtml IP_WEBS_SendString(pOutput,pConfFileName); ?></span>
</li>
<li>
<span>Username: </span>
<span style='color: #3875b3'><?chtml IP_WEBS_SendString(pOutput,pUaseName); ?></span>
</li>
<li>
<span>Role: </span>
<span style='color: #3875b3'><?chtml IP_WEBS_SendString(pOutput,pUserRole); ?></span>
</li>
<li>
<span style='color: #3875b3' class='show-clock'>Clock</span>
</li>
<li>
<span class='logout-button link' data-click='<?chtml IP_WEBS_SendString(pOutput,pLogoutURL); ?>'>Logout</span>
</li>
</ul>
</div>
<!-- Responsive Info button setup - end -->
<div class='wrapper' data-state='default'>
<!-- DYNAMIC ANNOTATION 2 END header2.html-->