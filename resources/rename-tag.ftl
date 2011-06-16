 <html>
<head>
    <content tag="pagetitle">Rename Tag</content>
    <content tag="pageID">rename-tag</content>
</head>
<Body>
<p>Rename tag or replacing tag. (NOTE: If new tag name already exist then tag will get replaced. )</p>
<#include "/template/global/include/form-message.ftl" />

<#if successParameter>
<div class="jive-success">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr><td class="jive-icon"><img src="images/success-16x16.gif" width="16" height="16" border="0" alt="" /></td>
        <td class="jive-icon-label">
            ${oldTagName} tag is renamed with ${newTagName}.
        </td></tr>
    </tbody>
    </table>
    </div><br />
</#if>

<#if error >
<div class="jive-error">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr><td class="jive-icon"><img src="images/error-16x16.gif" width="16" height="16" border="0" alt=""/></td>
        <td class="jive-icon-label">
            Error -- Something went wrong please make sure all the fields below are filled correctly.
        </td></tr>
    </tbody>
    </table>
    </div><br />
 </#if>

<@macroFieldErrors name="EQUAL_TAG_NAME"/>

<form action="rename-tag.jspa" method="get" name="f">
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
        <td>Old tag name:</td>
        <td>
            <input type="text" size="30" maxlength="50" name="oldTagName" >
            <@macroFieldErrors name="OLD_TAG"/>
            <@macroFieldErrors name="CONTENT_TAG_NOT_FOUND"/>
        </td>
    </tr>
    <tr>
        <td>New tag name:</td>
        <td>
            <input type="text" size="30" maxlength="50" name="newTagName" >
            <@macroFieldErrors name="NEW_TAG"/>
            <@macroFieldErrors name="SPACE_IN_TAG"/>
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