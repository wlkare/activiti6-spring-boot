<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>任务办理</title>
</head>
<body>
<#if (pd.name)??>${pd.name}<#else> </#if>
<h3>任务办理—[<#if hasFormKey == true>${task.name}<#else> ${taskFormData.task.name}</#if>]，
    流程定义ID：[<#if hasFormKey == true>${task.processDefinitionId}<#else> ${taskFormData.task.processDefinitionId}</#if>]</h3>
<hr/>
<form action="task-complete/<#if hasFormKey == true>${task.id}<#else> ${taskFormData.task.id}</#if>" class="form-horizontal" method="post">
    <#if hasFormKey == true>   <#--"??" 判断hasFormKey是否为null，如果不为null则执行${taskFormData}-->
        ${taskFormData}
    </#if>
    <#if hasFormKey == false>
        <#list taskFormData.formProperties as fp>
            <#--<#set var="fpo" value="${fp}"/>-->
            <#--<#set var="disabled" value="${fp.writable ? '' : 'disabled'}" />-->
            <#--<#set var="readonly" value="${fp.writable ? '' : 'readonly'}" />-->
            <#--<#set var="required" value="${fp.required ? 'required' : ''}" />-->
            <#--<%-->
            <#--// 把需要获取的属性读取并设置到pageContext域-->
            <#--FormType type = ((FormProperty)pageContext.getAttribute("fpo")).getType();-->
            <#--String[] keys = {"datePattern", "values"};-->
            <#--for (String key: keys) {-->
            <#--pageContext.setAttribute(key, type.getInformation(key));-->
            <#--}-->
            <#--%>-->
            <div class="control-group">
                <#--<%-- 文本或者数字类型 --%>-->
                <#if fp.type.name == 'string' || fp.type.name == 'long'>
                    <label class="control-label" for="${fp.id}">${fp.name}:</label>
                    <div class="controls">
                        <input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value="${fp.value}" ${readonly} ${required} />
                    </div>
                </#if>

                <#--<%-- 日期 --%>-->
                <#if fp.type.name == 'date'>
                    <label class="control-label" for="${fp.id}">${fp.name}:</label>
                    <div class="controls">
                        <input type="text" id="${fp.id}" name="${fp.id}" class="datepicker" value="${fp.value}" data-type="${fp.type.name}"  ${readonly} ${required}/>
                    </div>
                </#if>

                <%-- 下拉框 --%>
                <#if fp.type.name == 'enum'>
                    <label class="control-label" for="${fp.id}">${fp.name}:</label>
                    <div class="controls">
                        <select name="${fp.id}" id="${fp.id}" ${disabled} ${required}>
                            <#list values as item>
                                <option value="${item.key}" <#if test="${item.value == fp.value}">selected</#if>>${item.value}</option>
                            </#list>
                        </select>
                    </div>
                </#if>

                <%-- Javascript --%>
                <#if test="${fp.type.name == 'javascript'}">
                    <script type="text/javascript">${fp.value};</script>
                </#if>

            </div>
        </#list>
    </#if>

    <#-- 按钮区域 -->
    <div class="control-group">
        <div class="controls">
            <a href="javascript:history.back();" class="btn"><i class="icon-backward"></i>返回列表</a>
            <button type="submit" class="btn"><i class="icon-ok"></i>完成任务</button>
        </div>
    </div>
</form>
</body>
</html>