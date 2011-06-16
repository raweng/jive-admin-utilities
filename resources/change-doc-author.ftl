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
    <content tag="pagetitle">Change Document Author</content>
    <content tag="pageID">change-doc-author</content>
</head>
<Body>
<p>Change Document Author</p>
<#include "/template/global/include/form-message.ftl" />

<#if successParameter>
<div class="jive-success">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr><td class="jive-icon"><img src="images/success-16x16.gif" width="16" height="16" border="0" alt="" /></td>
        <td class="jive-icon-label">
            Document ${documentID} Author is changed to ${userID} successfully.
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

<@macroFieldErrors name="ALREADY_AUTHOR"/>

<form action="change-doc-author.jspa" method="get" name="f">
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
            <input type="text" size="30" maxlength="50" name="documentID" >
            <@macroFieldErrors name="DOCUMENT_NOT_EXIST"/>
            <@macroFieldErrors name="DOCUMENT"/>

        </td>
    </tr>
    <tr>
        <td>User ID:</td>
        <td>
            <input type="text" size="30" maxlength="50" name="userID" >
            <@macroFieldErrors name="USER_NOT_EXIST"/>
            <@macroFieldErrors name="USER"/>
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