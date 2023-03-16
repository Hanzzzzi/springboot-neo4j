<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv=Cache-Control content=no-cache />
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
    <meta http-equiv="expires" content="0">

    <title>术语浏览器之图数据库应用</title>
    <link rel="shortcut icon" href="/img/favicon.ico" />
    <link rel="stylesheet" href="/bs/css/bootstrap.css?version=${.now?string['hhmmSSsss']}">
    <link rel="stylesheet" href="/js/D3/css/style.css">
    <script src="/js/jquery-3.3.1.min.js?version=${.now?string['hhmmSSsss']}"></script>
    <script src="/js/jquery-1.11.2.js?version=${.now?string['hhmmSSsss']}"></script>
    <script src="/bs/js/bootstrap.min.js?version=${.now?string['hhmmSSsss']}"></script>

    <script type="text/javascript" src="/js/D3/js/d3.v3.min.js"></script>
    <script type="text/javascript" src="/js/D3/js/d3.view.js"></script>
    <script>
        var _hmt = _hmt || [];
        (function() {
            var hm = document.createElement("script");
            hm.src = "https://hm.baidu.com/hm.js?c5f999fb6da6b6ecd0233092b251f516";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();

    </script>

</head>
<body>

<div class="public-main">
    <div class="public-box">
        <div class="kgView-box">
            <div class="kgViewLeft-box">

                <div class="kgSearchBox">
                    <select id="selectLabelClass" class="form-control" style="width: 150px;display: inline-block;padding: 6px">
                        <option value="-1">请选择标签</option>
                        <#list labels as p>
                            <#if (p??)>
                                <option value="${(p)!}" num="1">${(p)!}</option>
                            </#if>
                        </#list>
                    </select>
                    <input id="kgKeyWord" class="kgSearchInput" placeholder="请输入要查询的关键字" type="text" data-type="知识图谱">
                    <button class="kgSearchBtn"><img width="24px" src="/img/searchIcon.svg" alt=""></button>

                    <select id="searchTypeClass" class="form-control" style="width: 140px;display: inline-block;padding: 6px;margin-left: 10px">
                        <option value="-1">请选择查找类型</option>
                        <option value="1">属性节点</option>
                        <option value="2">父节点</option>
                        <option value="3">子节点</option>
                    </select>

                </div>
            </div>

            <div class="kgViewImgBox">
                <!-- <canvas id="canvas"></canvas> -->
                <div id="svg"></div>
            </div>

        </div>
    </div>
</div>

</body>

<script>
    $(function(){

    });

    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            var keyWord = $("#kgKeyWord").val();
            var semanticTag = $("#selectLabelClass").val();
            var searchType = $("#searchTypeClass").val();
            var url = '';
            if(searchType == '1'){
                url = "/term/getRelation"
            }else if(searchType == '2'){
                url = "/term/findParentByName"
            }else if(searchType == '3'){
                url = "/term/findChildByName"
            }
            if (keyWord == ""){
                return;
            }
            if (semanticTag == -1){
                return;
            }
            if (searchType == -1){
                return;
            }
            getDetail(keyWord,semanticTag,url);
        }
    });

    $(".kgSearchBtn").click(function(){
        var keyWord = $("#kgKeyWord").val();
        var semanticTag = $("#selectLabelClass").val();
        var searchType = $("#searchTypeClass").val();
        var url = '';
        if(searchType == '1'){
            url = "/term/getRelation"
        }else if(searchType == '2'){
            url = "/term/findParentByName"
        }else if(searchType == '3'){
            url = "/term/findChildByName"
        }
        if (keyWord == ""){
            return;
        }
        if (semanticTag == -1){
            return;
        }
        if (searchType == -1){
            return;
        }
        getDetail(keyWord,semanticTag,url);
    })

    function getDetail(keyWord,semanticTag,url) {
        $.ajax({
            url: url,
            type: "get",
            data: "term="+keyWord+"&semanticTag="+semanticTag,
            success: function (data) {
                console.log(data)
                if (data.ok) {
                    if (data.result) {
                        getData(data.result);
                        svg(data.result);
                    }else {
                        alert(data.message);
                    }
                } else {
                    if (data.code != 200) {
                        alert(data.message);s
                    }
                }
            }
        });
    }



</script>


