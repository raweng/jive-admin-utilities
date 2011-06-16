 <html>
<head>
    <content tag="pagetitle">Clear cache</content>
    <content tag="pageID">clear-caches</content>
</head>
<Body>
<p>Clear document caches</p>
 <#include "/template/global/include/form-message.ftl" />

<#if successParameter>
<div class="jive-success">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr><td class="jive-icon"><img src="images/success-16x16.gif" width="16" height="16" border="0" alt="" /></td>
        <td class="jive-icon-label">
            Caches of Docuement/s ${successfulDocIDString}  cleared successfully.
        </td></tr>
    </tbody>
    </table>
    </div><br />
</#if>

<#if error>
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


<form action="clear-caches.jspa" method="get" name="f">
<input type="hidden" name="clear" value="true" />
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
            <input type="text" size="30" maxlength="50" name="documentID" > (Give comma separated DocumentID's for multiple caches clear.)
        <@macroFieldErrors name="EMPTY"/>
        <@macroFieldErrors name="ERROR_DOC_NOT_EXIST"/>
        </td>
    </tr>
</tbody>
<tfoot>
    <tr>
        <td colspan="2">
            <input type="submit" value="Clear cache">
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