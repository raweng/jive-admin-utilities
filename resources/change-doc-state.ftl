 <html>
<head>
    <content tag="pagetitle">Change document state</content>
    <content tag="pageID">change-doc-state</content>
</head>
<Body>
<p>Change Document State from EXPIRED to PUBLISHED or vice-versa</p>
<#include "/template/global/include/form-message.ftl" />
<#if successParameter>
<div class="jive-success">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr><td class="jive-icon"><img src="images/success-16x16.gif" width="16" height="16" border="0" alt="" /></td>
        <td class="jive-icon-label">
            Document/s ${successfulDocIDString} state updated to ${documentState} successfully.
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


<form action="change-doc-state.jspa" method="get" name="f">
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
            <input type="text" size="30" maxlength="50" name="documentID" > (Enter comma separated DocumentID's.)
            <@macroFieldErrors name="ALREADY_IN_STATE"/>
            <@macroFieldErrors name="EMPTY"/>
            <@macroFieldErrors name="ERROR_STATE"/>
            <@macroFieldErrors name="ERROR_DOC_NOT_EXIST"/>
        </td>
    </tr>
    <tr>
        <td>Change Document State to :</td>
        <td>
            <select name="documentState">
                <option value="EXPIRED">EXPIRED</option>
                <option value="PUBLISHED">PUBLISHED</option>
            </select> 
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