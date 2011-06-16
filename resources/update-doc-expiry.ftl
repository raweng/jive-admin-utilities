<!--This module is licenced under the BSD license.

Copyright (C) 2011 by raw engineering <ninad.hatkar (at) raweng (dot) com, mayank (at) raweng (dot) com>.

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

    * Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.-->

 <html>
<head>
    <content tag="pagetitle">Update Expiry Date</content>
    <content tag="pageID">update-doc-expiry</content>

    <script language="JavaScript" type='text/javascript' src="<@s.url value='/resources/scripts/domLib.js' />"></script>
    <script language="JavaScript" type='text/javascript' src="<@s.url value='/resources/scripts/domTT.js' />"></script>

    <link rel="stylesheet" type="text/css" href="<@s.url value='/styles/zapatec/styles/aqua.css'/>" media="all"/>

    <script language="JavaScript" type='text/javascript'
            src="<@s.url value='/resources/scripts/jive/namespace.js'/>"></script>
    <script language="JavaScript" type='text/javascript'
            src="<@s.url value='/resources/scripts/jive/util.js'/>"></script>
    <#-- Kongregate Asynchronous JavaScript Loader -->
    <#include "/template/decorator/default/header-javascript-kjs.ftl" />
    <@resource.javascript file="/resources/scripts/zapatec/utils/zapatec.js" header="true" id="zapatecCal" />
    <@resource.javascript file="/resources/scripts/zapatec/zpcal/src/calendar.js" header="true" id="zapatecCal" />
    <@resource.javascript file="/resources/scripts/zapatec/zpcal/lang/${calendarFileName}" header="true" id="zapatecCal" />
    <@resource.javascript header="true" id="zapatecCal">
        <#-- Set the javascript calendar date format -->
        Zapatec.Calendar._TT["DEF_DATE_FORMAT"] = "${calendarOverrideFormat?js_string}";
    </@resource.javascript>
    <@resource.javascript header="true" id="zapatecCal" asyncOutput=true satisfies="Zapatec.Calendar" />
    <script language="JavaScript" type='text/javascript' src="<@s.url value='/resources/scripts/zapatec/zpcal/src/calendar-bootstrap.js'/>"></script>

    <style type="text/css">
        table.jive-profile-datetime-container tbody tr td { padding: 0 5px 0 0;}
    </style>
</head>
<Body>
<p>Update document expiration date.</p>
 <#include "/template/global/include/form-message.ftl" />

<#if successParameter >
<div class="jive-success">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr><td class="jive-icon"><img src="images/success-16x16.gif" width="16" height="16" border="0" alt="" /></td>
        <td class="jive-icon-label">
            Document Expiry date is updated successfully from ${oldExpirationDate?datetime} to ${expirationDate?datetime}.
        </td></tr>
    </tbody>
    </table>
    </div><br />
</#if>

<#if error >
<div class="jive-error">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr><td class="jive-icon"><img src="images/error-16x16.gif" width="16" height="16" border="0" alt="" /></td>
        <td class="jive-icon-label">
                Error -- Something went wrong please make sure all the fields below are filled correctly.
        </td></tr>
    </tbody>
    </table>
    </div><br />
 </#if>


<form action="update-doc-expiry.jspa" method="get" name="f">
<input type="hidden" name="update" value="true" />
<div class="jive-table">
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<thead>
    <tr>
        <th>&nbsp;</th>
        <th>&nbsp;</th>
    </tr>
</thead>
<tbody>
   <tr>
        <td>Document ID:</td>
        <td>
            <input type="text" size="30" maxlength="50" name="documentID" />
            <@macroFieldErrors name="EMPTY"/>
            <@macroFieldErrors name="ERROR_DOC_NOT_EXIST"/>
        </td>
   </tr>

    <tr>
        <td>Expiration Date :</td>
        <td>
            <@s.datetimepicker theme="simple" id="expiration-date" name="expirationDate" value=""/>
            <@macroFieldErrors name="EQUAL_EXPIRATION_DATE"/>
        </td>
    </tr>

</tbody>
<tfoot>
    <tr>
        <td colspan="2">
            <input type="submit" value="Update">
        </td>
    </tr>
</tfoot>
</table>
</div>

</form>

 <script language="JavaScript" type="text/javascript">
document.f.documentID.focus();
</script>
</Body>
</html>