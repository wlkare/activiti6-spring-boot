<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h3>启动流程—
    <#if hasStartFormKey>
        [${processDefinition.name}]，版本号：${processDefinition.version}
    </#if>
    <#if (!hasStartFormKey)>
        [${startFormData.processDefinition.name}]，版本号：${startFormData.processDefinition.version}
    </#if>
</h3>
<hr/>
<form action="process-instance/start/${processDefinitionId}" class="form-horizontal" method="post">
    <#if (hasStartFormKey)>
        ${startFormData}
    </#if>

    <#if (!hasStartFormKey)>
        <#list startFormData.formProperties as fp>
            <#--<c:set var="fpo" value="${fp}"/>-->
            <#--<%-->
            <#--// 把需要获取的属性读取并设置到pageContext域-->
            <#--FormType type = ((FormProperty)pageContext.getAttribute("fpo")).getType();-->
            <#--String[] keys = {"datePattern"};-->
            <#--for (String key: keys) {-->
            <#--pageContext.setAttribute(key, ObjectUtils.toString(type.getInformation(key)));-->
            <#--}-->
            <#--%>-->
            <div class="control-group">
                <%-- 文本或者数字类型 --%>
                <#if (fp.type.name == 'string' || fp.type.name == 'long')>
                    <label class="control-label" for="${fp.id}">${fp.name}:</label>
                    <div class="controls">
                        <input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value=""/>
                    </div>
                </#if>

                <%-- 日期 --%>
                <#if (fp.type.name == 'date')>
                    <label class="control-label" for="${fp.id}">${fp.name}:</label>
                    <div class="controls">
                        <input type="text" id="${fp.id}" name="${fp.id}" class="datepicker" data-type="${fp.type.name}"
                               data-date-format="${fn:toLowerCase(datePattern)}"/>
                    </div>
                </#if>

                <%-- Javascript --%>
                <#if (fp.type.name == 'javascript')>
                    <script type="text/javascript">${fp.value};</script>
                </#if>
            </div>
        </#list>
    </#if>

    <%-- 按钮区域 --%>
    <div class="control-group">
        <div class="controls">
            <#--<a href="javascript:history.back();" class="btn"><i class="icon-backward"></i>返回列表</a>-->
            <button type="submit" class="btn"><i class="icon-play"></i>启动流程</button>
        </div>
    </div>
</form>
</body>
</html>