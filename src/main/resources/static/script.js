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

    submitAjax: function (url,data,container,callback) {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url: url,
            data: JSON.stringify(data),
            // data: data,
            dataType: "JSON",
            method: "POST",
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
    },

    setResult: function (data,container) {
        var template = JSController.getTemplate('result');
        var html = template({result: data.post, request:data.request, title: 'Result'});
        $("#"+container).html(html);
    },

    getTemplate: function(path) {
        var html = $("#"+path+"Template").html();
        return Handlebars.compile(html);
    },

    onClickPost:function(index){//
        var item = JSController.selectionBox[index];
        console.log(item)
        var postRequest = {
            'content' : item.content,
            'postId' : item.id_post,
            'similarityLevel' : 70,
            'pageSize' : 10,
            'strategy' : strategy.COSINE
        };

        console.log(postRequest);
        JSController.submitAjax("/process", postRequest, "main", JSController.setResult);
    }
}