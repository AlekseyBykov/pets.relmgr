Patch ${version} is available at:

${url}

Recommended installation approach:
1. Prepare the target environment for patch application.
2. Apply the contents of the archive to the application directory, allowing existing files to be updated.
3. Execute the database update script provided in the SQL directory (if applicable).
4. Verify the application startup and basic functionality after installation.

The patch should be applied to the following environments:
${installTargets}

Included changes:
<#list issues as issue>
- ${issue}
</#list>
