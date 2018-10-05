var JSController = JSController || {};

var strategy = {
    'COSINE' : 1
}

JSController = {

    selectionBox: {},
    postResult: {},
    template: null,

    loadPostList: function (size){
        JSController.submitAjax("/list/"+size, null, "main", JSController.setList);
    },

    submitAjax: function (url,data,container,callback, beforeSend) {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url: url,
            data: JSON.stringify(data),
            dataType: "JSON",
            method: "POST",
            beforeSend: beforeSend,
            success: function(result){
                JSController.mountBox(result);
                callback(result,container);
            },
            fail: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            },
            done: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            }
        });
    },

    mountBox: function(data){
        for (var i = 0; i < data.length; i++) {
            var item = data[i];
            JSController.selectionBox[item.id_post] = item;
        }
    },
    
    setList: function (data,container) {
        var template = JSController.getTemplate('post');
        var html = template({result: data, title: 'Selection'});
        $("#"+container).html(html);
        JSController.initSliderValue();
    },

    setResult: function (data,container) {
        var template = JSController.getTemplate('result');
        var html = template({result: data.post, request:data.request, title: 'Result'});
        $("#"+container).html(html);
    },

    setLoading: function (data,container) {
        var template = JSController.getTemplate('loading');
        var html = template({request:data, title: 'Loading'});
        $("#"+container).html(html);
    },

    getTemplate: function(path) {
        var html = $("#"+path+"Template").html();
        return Handlebars.compile(html);
    },

    onClickPost:function(index){//
        var item = JSController.selectionBox[index];
        var similarity = document.getElementById("slider").value;
        var postRequest = {
            'content' : item.content,
            'postId' : item.id_post,
            'similarityLevel' : similarity,
            'pageSize' : 10,
            'strategy' : strategy.COSINE
        };
        var loadingFunc = function(){
            JSController.setLoading(item, "main");
        };
        JSController.submitAjax("/process", postRequest, "main", JSController.setResult, loadingFunc);
    },

    initSliderValue:function(){
        var slider = document.getElementById("slider");
        var output = document.getElementById("similarityLevel");
        output.innerHTML = slider.value;
        slider.oninput = function() {
            output.innerHTML = this.value;
        }
    }
}