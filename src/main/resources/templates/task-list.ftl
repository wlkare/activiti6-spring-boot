<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>任务列表</title>
</head>
<body>
<#--<#if test="${not empty message}">-->
    <#--<div id="message" class="alert alert-success">${message}</div>-->
    <#--<!-- 自动隐藏提示信息 &ndash;&gt;-->
    <#--<script type="text/javascript">-->
        <#--setTimeout(function() {-->
            <#--$('#message').hide('slow');-->
        <#--}, 5000);-->
    <#--</script>-->
<#--</#if>-->
<table width="100%" class="table table-bordered table-hover table-condensed">
    <thead>
    <tr>
        <th>任务ID</th>
        <th>任务名称</th>
        <th>流程实例ID</th>
        <th>流程定义ID</th>
        <th>任务创建时间</th>
        <th>办理人</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <#list tasks as task>
        <tr>
            <td>${task.id }</td>
            <td>${task.name }</td>
            <td>${task.processInstanceId }</td>
            <td>${task.processDefinitionId }</td>
            <td>${task.createTime?string("dd.MM.yyyy HH:mm:ss")}</td>
            <td><#if (task.assignee)??>${task.assignee}<#else> </#if></td>
            <td>
                <#if (task.assignee)??>
                    <a class="btn" href="task-claim/${task.id}"><i class="icon-eye-open"></i>签收</a>
                <#else>
                    <a class="btn" href="task-getform/${task.id}"><i class="icon-user"></i>办理</a>
                </#if>
            </td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>