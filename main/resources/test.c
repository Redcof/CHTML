/* Starting a scope*/
IP_WEBS_SendString(pOutput,"/**\n\
 * Created by int_soumen on 05-05-2017.\n\
 */\n\
/** This variable stores the CSS selector of\n\
     * monitoring container of currently selected tab */\n\
var LogContainerParentSelectorPrefix = '';\n\
var LogContainer, __MBCS_GLOBAL_LOG_INTERVAL_HANDELLER;\n\
var LogFileMemoryUsagesBox = null;\n\
/** Decides whether monitoring should be running or not */\n\
var MonitoringAbility = !1;\n\
var MemoryUsages = 0;\n\
var PlayPauseBtn;\n\
var SavedFileCounter = 0;\n\
const __MBCS_LOG_CLEAR_CONFORMATION_MESSAGE = \"It will remove all the communication messages logged.If you want\" +\n\
    \" to save the logged messages, click on 'Cancel' and save the logged messages in 'file' Else continue by clicking on 'OK'.\";\n\
const __MBCS_MAX_MEMORY_ALLOWED = ");

 IP_WEBS_SendUnsigned(pOutput,LogFileSizeInByte,10,0);
/* Starting a scope*/
IP_WEBS_SendString(pOutput,";/* (in byte)*/\n\
const __MAX_FILE_SIZE_LIMIT_IN_HUMAN_READABLE = bytesToSize(__MBCS_MAX_MEMORY_ALLOWED);\n\
var __MBCS_LOG_FETCH_URL = '");

 IP_WEBS_SendString(pOutput,pAJAXMonitoringLogCGI); 
/* Starting a scope*/
IP_WEBS_SendString(pOutput,"';\n\
var __MBCS_LOG_FETCH_INTERVAL = ");

 IP_WEBS_SendUnsigned(pOutput,LogFetchInterval_Sec,10,0);
/* Starting a scope*/
IP_WEBS_SendString(pOutput,";\n\
    /* in mill Second */\n\
var MonitoringRefreshRequestParams;\n\
var __MBCS_MASTER_MONITORING_CONFIG = ");

 IP_WEBS_SendString(pOutput,pMonitoringLogConfigurationJSON);

/*3.5 KB Limit */
IP_WEBS_SendString(pOutput,";\n\
var __MBCS_MASTER_MONITORING_TAB_CONF = null;\n\
\n\
function setMasterMonitoring(Object, key, value) {\n\
    Object[key] = value;\n\
}\n\
\n\
function getMasterMonitoring(Object, key) {\n\
    return Object[key];\n\
}\n\
\n\
/** */\n\
function changeLogContainerParentSelector(tabIndex) {\n\
    LogContainerParentSelectorPrefix = ' div#tabpage_' + tabIndex + '.tabpage > div.comm-log ';\n\
    __MBCS_MASTER_MONITORING_TAB_CONF = __MBCS_MASTER_MONITORING_CONFIG['tabpage_' + tabIndex];\n\
    __MBCS_LOG_FETCH_URL = getMasterMonitoring(__MBCS_MASTER_MONITORING_TAB_CONF, 'cgi');\n\
    __MBCS_LOG_FETCH_INTERVAL = getMasterMonitoring(__MBCS_MASTER_MONITORING_TAB_CONF, 'refresh_mSec');\n\
}\n\
\n\
function reloadMonitoringLogFromCache() {\n\
    SSThread(function () {\n\
        /** Appending Log lo local storage*/\n\
        var arrayPastMonData = JSON.parse(localStorage.getItem(getLocalStorageKeyForMonitoringLog(__MBCS_SELECTED_TAB_INDEX)));\n\
        arrayPastMonData = arrayPastMonData || [];\n\
        localStorage.removeItem(getLocalStorageKeyForMonitoringLog(__MBCS_SELECTED_TAB_INDEX));\n\
        clearLog();\n\
        /** Appending log to container*/\n\
        appendMonitoringLog(__MBCS_SELECTED_TAB_INDEX, arrayPastMonData);\n\
    }, 5);\n\
\n\
}\n\
\n\
function getLocalStorageKeyForMonitoringLog(tabID) {\n\
    return 'mon_dat_for_K' + __MBCS_PAGE_ID + '_T' + tabID;\n\
}\n\
\n\
function appendMonitoringLog(tabID, data, CSSClass) {\n\
    SSThread(function () {\n\
        CSSClass = CSSClass || 'some-class';\n\
\n\
        /** Preparing log container*/\n\
        var LogContainerParentSelectorPrefixL = ' div#tabpage_' + tabID + '.tabpage > div.comm-log ';\n\
        var LogContainerL = $(LogContainerParentSelectorPrefixL + ' div.comm-log-container > ol')[0];\n\
        var LogFileMemoryUsagesBox = $(LogContainerParentSelectorPrefixL + ' .MemoryUsages')[0];\n\
\n\
        /** Scroll the container to bottom*/\n\
        var scrollDiv = $(LogContainerParentSelectorPrefixL + ' div.comm-log-container')[0];\n\
\n\
        var ArrayType = Array.isArray(data);\n\
\n\
        /** Appending Log lo local storage*/\n\
        var arrayPastMonData = JSON.parse(localStorage.getItem(getLocalStorageKeyForMonitoringLog(tabID)));\n\
        arrayPastMonData = arrayPastMonData || [];\n\
\n\
        if (ArrayType === false) {\n\
            data = [data];\n\
        }\n\
\n\
        var Length = data.length;\n\
\n\
        for (var Ctr = 0; Ctr < Length; ++Ctr) {\n\
            var Content = data[Ctr];\n\
            var li = document.createElement('li');\n\
");

/*3.5 KB Limit */
IP_WEBS_SendString(pOutput,"\n\
            li.innerHTML = decodeURI(Content);\n\
\n\
            li.className = CSSClass;\n\
\n\
            /** Appending log to container*/\n\
            LogContainerL.appendChild(li);\n\
\n\
            /** Appending Log lo local storage*/\n\
            arrayPastMonData.push(Content);\n\
        }\n\
\n\
\n\
        /** Scroll the container to bottom*/\n\
        scrollDiv.scrollTop = scrollDiv.scrollHeight;\n\
\n\
\n\
        /** Appending Log lo local storage*/\n\
        localStorage.setItem(getLocalStorageKeyForMonitoringLog(tabID), JSON.stringify(arrayPastMonData));\n\
    }, 10);\n\
\n\
}\n\
\n\
function fetchMonitoringLog() {\n\
    if (!0 === MonitoringAbility) {\n\
        ajax({\n\
            url: __MBCS_LOG_FETCH_URL,\n\
            params: MonitoringRefreshRequestParams,\n\
            success: function (response) {\n\
                if (checkMemoryUsages()) {\n\
                    try {\n\
                        var JSONObj = JSON.parse(response);\n\
                        /* This will make sure two things\n\
                        * 1.Valid data is available, other wise will create exception\n\
                        * 2.At least 1 well formatted data is available, otherwise will go to else\n\
                        * */\n\
                        if (JSONObj.data.length > 0) {\n\
                            /** Appending log*/\n\
                            appendMonitoringLog(JSONObj.T, JSONObj.data);\n\
                            /** Update memory usages*/\n\
                            MemoryUsages = LogContainer.innerHTML.length;\n\
                            fetchMonitoringLog();\n\
                        } else {\n\
                            setTimeout(fetchMonitoringLog, __MBCS_LOG_FETCH_INTERVAL);\n\
                        }\n\
                    }\n\
                    catch (e) {\n\
                        setTimeout(fetchMonitoringLog, __MBCS_LOG_FETCH_INTERVAL);\n\
                    }\n\
                }\n\
            },\n\
            error: function (error) {\n\
                setTimeout(fetchMonitoringLog, __MBCS_LOG_FETCH_INTERVAL);\n\
            }\n\
        });\n\
    }\n\
}\n\
\n\
/** @author int_soumen\n\
 * Initialize Monitoring Operation Before Starting\n\
 * @param void\n\
 * @return boolean\n\
 * */\n\
const LinearGradientRegex = /(rgba\\(([0-9]{1,3},\\s){3}[0-9]{1,3}\\))\\s((linear-gradient)\\(([a-z\\s]+),\\s(((rgb\\(([0-9]{1,3},\\s){2}[0-9]{1,3}\\))\\s[0-9]{1,3}%,\\s){1,3}(rgb\\(([0-9]{1,3},\\s){2}[0-9]{1,3}\\))\\s[0-9]{1,3}%)\\))/;\n\
const RGBRegex = /(rgb\\([0-9]{1,3},\\s[0-9]{1,3},\\s[0-9]{1,3}\\))\\s[0-9]{1,3}%,\\s/;");

/*3.5 KB Limit */
IP_WEBS_SendString(pOutput,"\n\
var MBCS_MEMORY_STATUS_GRADIENT_COLOR_VALUE1 = '';\n\
var MBCS_MEMORY_STATUS_GRADIENT_COLOR_VALUE2 = '';\n\
\n\
function initLogMonitoring() {\n\
    try {\n\
        if (0 < __MBCS_MASTER_MONITORING_CONFIG.monitoringTabId.length) {\n\
            if (-1 < __MBCS_MASTER_MONITORING_CONFIG.monitoringTabId.indexOf(Number(__MBCS_SELECTED_TAB_INDEX))) {\n\
                changeLogContainerParentSelector(__MBCS_SELECTED_TAB_INDEX);\n\
                LogContainer = $(LogContainerParentSelectorPrefix + ' div.comm-log-container > ol')[0];\n\
                LogFileMemoryUsagesBox = $(LogContainerParentSelectorPrefix + ' .MemoryUsages')[0];\n\
                var MemoryStatusGradientStyle = window.getComputedStyle(LogFileMemoryUsagesBox);\n\
                var GradientValue = MemoryStatusGradientStyle.getPropertyValue('background');\n\
\n\
                var Match = LinearGradientRegex.exec(GradientValue);\n\
                var MatchedRegex = Match[6].split(RGBRegex);\n\
\n\
                MBCS_MEMORY_STATUS_GRADIENT_COLOR_VALUE1 = MatchedRegex[1];\n\
                MBCS_MEMORY_STATUS_GRADIENT_COLOR_VALUE2 = MatchedRegex[5];\n\
\n\
                PlayPauseBtn = $(LogContainerParentSelectorPrefix + ' .PlayPauseBtn')[0];\n\
                MonitoringRefreshRequestParams = urlEncode({\n\
                    V: __MBCS_V,\n\
                    K: __MBCS_PAGE_ID,\n\
                    T: __MBCS_SELECTED_TAB_INDEX\n\
                });\n\
                return true;\n\
            }\n\
            return false;\n\
\n\
        }\n\
        return false;\n\
    }\n\
    catch (e) {\n\
        return false;\n\
    }\n\
\n\
}\n\
\n\
/** @author int_soumen\n\
 * Start Monitoring Operation\n\
 * @param void\n\
 * @return void\n\
 * */\n\
function startLogMonitoring() {\n\
\n\
    if (initLogMonitoring()) {\n\
        __mbcs_bindMonitoringWindowActions();\n\
        startLogging();\n\
    }\n\
}\n\
\n\
function checkMemoryUsages() {\n\
    try {\n\
        LogFileMemoryUsagesBox.innerHTML = 'File Size: ' + bytesToSize(MemoryUsages, 2) + ' of ' + __MAX_FILE_SIZE_LIMIT_IN_HUMAN_READABLE;\n\
        var Re = (MemoryUsages / __MBCS_MAX_MEMORY_ALLOWED) * 100;\n\
        /*Update progress in log bar*/\n\
        LogFileMemoryUsagesBox.style.background = 'linear-gradient(to right, ' +\n\
            MBCS_MEMORY_STATUS_GRADIENT_COLOR_VALUE1 +\n\
            ' 0%, ' +\n\
            MBCS_MEMORY_STATUS_GRADIENT_COLOR_VALUE1 +\n\
            ' ' +\n\
            Re +\n\
            '%, ' +\n\
            MBCS_MEMORY_STATUS_GRADIENT_COLOR_VALUE2");

/*3.5 KB Limit */
IP_WEBS_SendString(pOutput," +\n\
            ' ' +\n\
            Re +\n\
            '%, ' +\n\
            MBCS_MEMORY_STATUS_GRADIENT_COLOR_VALUE2 +\n\
            ' 100%)';\n\
        /*Update progress in log title*/\n\
        LogFileMemoryUsagesBox.title = (100 - Re).toFixed(2) + '% free';\n\
\n\
        if (MemoryUsages >= __MBCS_MAX_MEMORY_ALLOWED) {\n\
            /** If maximum memory limit reached\n\
             * stop logging; save log file; clear log; restart logging*/\n\
            stopLogging();\n\
            /** Stop Logging*/\n\
            SavedFileCounter += 1;\n\
            var FileName = getMasterMonitoring(__MBCS_MASTER_MONITORING_TAB_CONF, 'logFileName') + dateFormatAs('MM-DD-YYYY_HH:mm:ss') + '_' + SavedFileCounter;\n\
            saveFile(FileName);\n\
            SSThread(clearLog, 10);\n\
            /** Save Log*/\n\
            __mbcs_showNotification('Log memory size limit is reached.<br/>Log is save as <u>' + FileName + '.htm</u> at your browser\\'s default download location.', __MBCS_NOTI_TYPE_MESSAGE);\n\
\n\
            return false;\n\
        }\n\
        return true;\n\
    }\n\
    catch (e) {\n\
    }\n\
\n\
}\n\
\n\
function startLogging() {\n\
    setMasterMonitoring(__MBCS_MASTER_MONITORING_TAB_CONF, 'currentMonitoringStatus', true);\n\
    PlayPauseBtn.innerHTML = '<i class=\"flaticon-pause-button\" ></i>Pause';\n\
    addClass(PlayPauseBtn, 'status-playing');\n\
    MonitoringAbility = !0;\n\
    fetchMonitoringLog();\n\
}\n\
\n\
function stopLogging() {\n\
    try {\n\
        setMasterMonitoring(__MBCS_MASTER_MONITORING_TAB_CONF, 'currentMonitoringStatus', false);\n\
        removeClass(PlayPauseBtn, 'status-playing');\n\
        PlayPauseBtn.innerHTML = '<i class=\"flaticon-play-button\"></i>Continue';\n\
        MonitoringAbility = !1;\n\
    }\n\
    catch (e) {\n\
    }\n\
\n\
}\n\
\n\
function toggleLogging() {\n\
    if (true === !!getMasterMonitoring(__MBCS_MASTER_MONITORING_TAB_CONF, 'currentMonitoringStatus')) {\n\
        stopLogging();\n\
    }\n\
    else if (MemoryUsages <= __MBCS_MAX_MEMORY_ALLOWED) {\n\
        startLogging();\n\
    }\n\
    else {\n\
        __mbcs_showNotification('Log memory size limit is reached.<br/>Please export log, clear log and continue logging.', __MBCS_NOTI_TYPE_ERROR);\n\
    }\n\
}\n\
\n\
function clearLogPrompt() {\n\
    var clearLogWindowJSON = {\n\
        heading: '',\n\
        body: __MBCS_LOG_CLEAR_CONFORMATION_MESSAGE,\n\
        height: '',\n\
        width: '',\n\
        buttons:");

/*3.5 KB Limit */
IP_WEBS_SendString(pOutput," [\n\
            {\n\
                title: '<i class=\"flaticon-delete\" style=\"color: #ff0000;\"></i> OK',\n\
                hvtxt: 'Click OK to clear logs',\n\
                url: 'action://onclick/clearLog'\n\
            },\n\
            {\n\
                title: 'Cancel',\n\
                hvtxt: 'Click to close window',\n\
                url: 'action://onclick/__mbcs_hidePopup'\n\
            }\n\
        ]\n\
    };\n\
\n\
    __mbcs_showPopup(JSON.stringify(clearLogWindowJSON));\n\
}\n\
\n\
function clearLog() {\n\
    LogContainer.innerHTML = '';\n\
    LogFileMemoryUsagesBox.innerHTML = 'File Size: 0 B of ' + __MAX_FILE_SIZE_LIMIT_IN_HUMAN_READABLE;\n\
    MemoryUsages = 0;\n\
    __mbcs_hidePopup();\n\
    localStorage.removeItem(getLocalStorageKeyForMonitoringLog(__MBCS_SELECTED_TAB_INDEX));\n\
}\n\
\n\
function fileSavePrompt() {\n\
    SavedFileCounter += 1;\n\
    var FileName = getMasterMonitoring(__MBCS_MASTER_MONITORING_TAB_CONF, 'logFileName') + dateFormatAs('MM-DD-YYYY_HH:mm:ss') + '_' + SavedFileCounter;\n\
    FileName = prompt('Please enter file name ', FileName);\n\
    saveFile(FileName);\n\
}\n\
\n\
function saveFile(FileName) {\n\
    if (!!FileName) {\n\
        var comm_log = $(LogContainerParentSelectorPrefix)[0];\n\
        var actions = $E(comm_log, '.action-buttons')[0];\n\
\n\
        stopLogging();\n\
        actions.style.visibility = 'hidden';\n\
\n\
        var CSS = '@media all { ' +\n\
            '*{font-family:Consolas,Monaco,monospaced;font-size:14px} ' +\n\
            'li{font-size:12px} ' +\n\
            'li:nth-child(odd){background:#e9e9e9} ' +\n\
            '}' +\n\
            '@media only print {' +\n\
            'button{display:none} ' +\n\
            'li:nth-child(odd){background:#e9e9e9} ' +\n\
            '}';\n\
\n\
\n\
        var blob = new Blob([\n\
                '<html>' +\n\
                '<head>' +\n\
                '<style>' +\n\
                CSS +\n\
                '</style>' +\n\
                '</head>' +\n\
                '<body>' +\n\
                /*'<a href=\"mailto:iudith.m@zim.co.il?subject=my report&body=see attachment&attachment=\\\"/my_location_virtual_path/myfile.lis\\\"\">email</a>' +*/\n\
                '<button style=\"position: absolute; right: 5px;\" onclick=\"window.print();\" >Print</button>' + /* Send Email To Be Added */\n\
                '<div class=\\'comm-log\\'>' +\n\
                comm_log.innerHTML +\n\
                '</div>' +\n\
                '</body>' +\n\
                '</html>'],\n\
");

/*3.5 KB Limit */
IP_WEBS_SendString(pOutput,"            {type: 'text/html;charset=utf-8'});\n\
        saveAs(blob, FileName + '.htm');\n\
\n\
        actions.style.visibility = 'visible';\n\
\n\
        startLogging();\n\
    }\n\
}\n\
\n\
function __mbcs_bindMonitoringWindowActions() {\n\
    /** Toggle monitoring window action*/\n\
    var MonitoringWindows = $('.comm-log-heading');\n\
\n\
    MonitoringWindows.forEach(function (element) {\n\
        element.addEventListener('click', function (event) {\n\
            var MonitoringWindow = $ClosestParent(element, '.comm-log');\n\
            var CurrentState = MonitoringWindow.getAttribute('data-current-toggle-view-state');\n\
            CurrentState = CurrentState || 'B';\n\
            switch (CurrentState) {\n\
                case 'A':\n\
                    MonitoringWindow.setAttribute('data-current-toggle-view-state', 'B');\n\
                    MonitoringWindow.style.position = 'relative';\n\
                    break;\n\
                case 'B':\n\
                    MonitoringWindow.setAttribute('data-current-toggle-view-state', 'A');\n\
                    MonitoringWindow.style.position = 'absolute';\n\
                    break;\n\
            }\n\
        });\n\
\n\
    });\n\
\n\
    /** Toggle monitoring action*/\n\
    var StartMonitoringButtons = $('.PlayPauseBtn');\n\
    StartMonitoringButtons.forEach(function (element) {\n\
        element.addEventListener('click', toggleLogging);\n\
    });\n\
\n\
    /** Export monitoring log action*/\n\
    var LogExportButtons = $('.ExportBtn');\n\
    LogExportButtons.forEach(function (element) {\n\
        element.addEventListener('click', fileSavePrompt);\n\
    });\n\
}\n\
\n\
/**\n\
 * @author codeaid\n\
 * */\n\
\n\
function bytesToSize(bytes, precision) {\n\
    var kilobyte = 1024;\n\
    var megabyte = kilobyte * 1024;\n\
    var gigabyte = megabyte * 1024;\n\
    var terabyte = gigabyte * 1024;\n\
\n\
    if ((bytes >= 0) && (bytes < kilobyte)) {\n\
        return bytes + ' B';\n\
\n\
    } else if ((bytes >= kilobyte) && (bytes < megabyte)) {\n\
        return (bytes / kilobyte).toFixed(precision) + ' KB';\n\
\n\
    } else if ((bytes >= megabyte) && (bytes < gigabyte)) {\n\
        return (bytes / megabyte).toFixed(precision) + ' MB';\n\
\n\
    } else if ((bytes >= gigabyte) && (bytes < terabyte)) {\n\
        return (bytes / gigabyte).toFixed(precision) + ' GB';\n\
\n\
    } else if (bytes >= terabyte) {\n\
        return (bytes / terabyte).toFixed(precision) + ' TB';");

/*3.5 KB Limit */
IP_WEBS_SendString(pOutput,"\n\
\n\
    } else {\n\
        return bytes + ' B';\n\
    }\n\
}\n\
\n\
\n\
/** @source http://purl.eligrey.com/github/FileSaver.js/blob/master/FileSaver.js */\n\
var saveAs = saveAs || function (e) {\n\
    'use strict';\n\
    if (typeof e === 'undefined' || typeof navigator !== 'undefined' && /MSIE [1-9]\\./.test(navigator.userAgent)) {\n\
        return\n\
    }\n\
    var t = e.document, n = function () {\n\
            return e.URL || e.webkitURL || e\n\
        }, r = t.createElementNS('http://www.w3.org/1999/xhtml', 'a'), o = 'download' in r, a = function (e) {\n\
            var t = new MouseEvent('click');\n\
            e.dispatchEvent(t)\n\
        }, i = /constructor/i.test(e.HTMLElement) || e.safari, f = /CriOS\\/[\\d]+/.test(navigator.userAgent),\n\
        u = function (t) {\n\
            (e.setImmediate || e.setTimeout)(function () {\n\
                throw t\n\
            }, 0)\n\
        }, s = 'application/octet-stream', d = 1e3 * 40, c = function (e) {\n\
            var t = function () {\n\
                if (typeof e === 'string') {\n\
                    n().revokeObjectURL(e)\n\
                } else {\n\
                    e.remove()\n\
                }\n\
            };\n\
            setTimeout(t, d)\n\
        }, l = function (e, t, n) {\n\
            t = [].concat(t);\n\
            var r = t.length;\n\
            while (r--) {\n\
                var o = e['on' + t[r]];\n\
                if (typeof o === 'function') {\n\
                    try {\n\
                        o.call(e, n || e)\n\
                    } catch (a) {\n\
                        u(a)\n\
                    }\n\
                }\n\
            }\n\
        }, p = function (e) {\n\
            if (/^\\s*(?:text\\/\\S*|application\\/xml|\\S*\\/\\S*\\+xml)\\s*;.*charset\\s*=\\s*utf-8/i.test(e.type)) {\n\
                return new Blob([String.fromCharCode(65279), e], {type: e.type})\n\
            }\n\
            return e\n\
        }, v = function (t, u, d) {\n\
            if (!d) {\n\
                t = p(t)\n\
            }\n\
            var v = this, w = t.type, m = w === s, y, h = function () {\n\
                l(v, 'writestart progress write writeend'.split(' '))\n\
            }, S = function () {\n\
                if ((f || m && i) && e.FileReader) {\n\
                    var r = new FileReader;\n\
                    r.onloadend = function () {\n\
                        var t = f ? r.result : r.result.replace(/^data:[^;]*;/, 'data:attachment/file;');\n\
                        var n = e.open(t, '_blank');\n\
     ");
/*Else before exiting loop*/
IP_WEBS_SendString(pOutput,"                   if (!n) e.location.href = t;\n\
                        t = undefined;\n\
                        v.readyState = v.DONE;\n\
                        h()\n\
                    };\n\
                    r.readAsDataURL(t);\n\
                    v.readyState = v.INIT;\n\
                    return\n\
                }\n\
                if (!y) {\n\
                    y = n().createObjectURL(t)\n\
                }\n\
                if (m) {\n\
                    e.location.href = y\n\
                } else {\n\
                    var o = e.open(y, '_blank');\n\
                    if (!o) {\n\
                        e.location.href = y\n\
                    }\n\
                }\n\
                v.readyState = v.DONE;\n\
                h();\n\
                c(y)\n\
            };\n\
            v.readyState = v.INIT;\n\
            if (o) {\n\
                y = n().createObjectURL(t);\n\
                setTimeout(function () {\n\
                    r.href = y;\n\
                    r.download = u;\n\
                    a(r);\n\
                    h();\n\
                    c(y);\n\
                    v.readyState = v.DONE\n\
                });\n\
                return\n\
            }\n\
            S()\n\
        }, w = v.prototype, m = function (e, t, n) {\n\
            return new v(e, t || e.name || 'download', n)\n\
        };\n\
    if (typeof navigator !== 'undefined' && navigator.msSaveOrOpenBlob) {\n\
        return function (e, t, n) {\n\
            t = t || e.name || 'download';\n\
            if (!n) {\n\
                e = p(e)\n\
            }\n\
            return navigator.msSaveOrOpenBlob(e, t)\n\
        }\n\
    }\n\
    w.abort = function () {\n\
    };\n\
    w.readyState = w.INIT = 0;\n\
    w.WRITING = 1;\n\
    w.DONE = 2;\n\
    w.error = w.onwritestart = w.onprogress = w.onwrite = w.onabort = w.onerror = w.onwriteend = null;\n\
    return m\n\
}(typeof self !== 'undefined' && self || typeof window !== 'undefined' && window || this.content);\n\
if (typeof module !== 'undefined' && module.exports) {\n\
    module.exports.saveAs = saveAs\n\
} else if (typeof define !== 'undefined' && define !== null && define.amd !== null) {\n\
    define('FileSaver.js', function () {\n\
        return saveAs\n\
    });\n\
}\n\
");
