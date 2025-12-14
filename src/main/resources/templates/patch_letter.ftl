<p style="font-family:Calibri, Arial, Helvetica, sans-serif; font-size:11pt; margin:0 0 10px 0;">
    Hello,
</p>

<p style="font-family:Calibri, Arial, Helvetica, sans-serif; font-size:11pt; margin:0 0 10px 0;">
    An application patch version <b>${ver}</b> has been released.
</p>

<p style="font-family:Calibri, Arial, Helvetica, sans-serif; font-size:11pt; margin:0 0 10px 0;">
    <b>Patch artifact:</b><br/>
    <a href="${patchUrl}" target="_blank" style="color:#0052CC; text-decoration:none;">
        ${patchUrl}
    </a>
</p>

<p style="font-family:Calibri, Arial, Helvetica, sans-serif; font-size:11pt; margin:0 0 10px 0;">
    <b>External build script:</b><br/>
    <a href="${sqlUrl}" target="_blank" style="color:#0052CC; text-decoration:none;">
        ${sqlUrl}
    </a>
</p>

<p style="font-family:Calibri, Arial, Helvetica, sans-serif; font-size:11pt; margin:0 0 4px 0;">
    <b>Changes included:</b>
</p>

<table style="border-collapse:collapse; margin:0 0 15px 0;">
    <tbody>
    <#list issues as issue>
    <tr>
        <td style="
                padding:4px 6px;
                font-family:Calibri, Arial, Helvetica, sans-serif;
                font-size:10pt;
                border-bottom:1px solid #e0e0e0;
            ">
            <a href="${issue.url()!''}"
               style="color:#0052CC; text-decoration:none;">
                <b>${issue.key()}</b> — ${issue.summary()}
            </a>
        </td>
    </tr>
    </
    #list>
    </tbody>
</table>

<p style="font-family:Calibri, Arial, Helvetica, sans-serif; font-size:9pt; color:#000; margin:0;">
    Best regards,<br/>
    <b>Release Management Tool</b>
</p>

<p style="font-family:Calibri, Arial, Helvetica, sans-serif; font-size:9pt; margin:6px 0 0 0; color:#666;">
    This message was generated automatically.
</p>
