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