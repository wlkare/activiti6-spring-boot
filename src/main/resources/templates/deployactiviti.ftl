<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Activiti6流程设计器Demo</title>

</head>
<body>
<h2>
    <fieldset id="deployFieldset">
        <legend>部署流程资源</legend>
        <form action="deploy" method="post" enctype="multipart/form-data" style="margin-top:1em;">
            <input type="file" name="file"/>
            <input type="submit" value="Submit" class="btn"/>
        </form>
    </fieldset>
</h2>
<div>
    <table width="100%" class="table table-bordered table-hover table-condensed">
        <thead>
        <tr>
            <th>流程定义ID</th>
            <th>部署ID</th>
            <th>流程定义名称</th>
            <th>流程定义KEY</th>
            <th>版本号</th>
            <th>XML资源名称</th>
            <th>图片资源名称</th>
            <th width="80">操作</th>
        </tr>
        </thead>
        <tbody>
            <#list processDefinitionList as pd>
            <tr>
                <td>${pd.id }</td>
                <td>${pd.deploymentId }</td>
                <td><#if (pd.name)??>${pd.name}<#else> </#if></td>
                <td><#if (pd.key)??>${pd.key}<#else> </#if></td>
                <td>${pd.version }</td>
                <td><a target="_blank"
                       href="/read-resource?pdid=${pd.id }&resourceName=${pd.resourceName }">${pd.resourceName }</a>
                </td>
                <td><a target="_blank" href="/read-resource?pdid=${pd.id }&resourceName=${pd.diagramResourceName }">${pd.diagramResourceName }</a></td>
                <td>
                    <a target="_blank" href="/delete-deployment?deploymentId=${pd.deploymentId}">删除</a>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</body>
</html>