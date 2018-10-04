var JSController = JSController || {};

JSController = {

    postResult: null,
    template: null,

    loadPostList: function (size){
        JSController.submitAjax("/list/"+size, null, "main", JSController.setList);
    },

    submitAjax: function (url,data,container,callback) {
        $.ajax({
            url: url,
            data: data,
            dataType: "JSON",
            method: "POST",
            success: function(result){
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
    
    setList: function (data,container) {
        var template = JSController.getTemplate('post');
        console.log(data);
        var html = template({result: data, title: 'Result'});
        $("#"+container).html(html);
    },

    setResult: function (data,container) {
        var template = JSController.getTemplate('result');
        console.log(data);
        var html = template({result: data, title: 'Result'});
        $("#"+container).html(html);
    },

    getTemplate: function(path) {
        var html = $("#"+path+"Template").html();
        return Handlebars.compile(html);
    },

    onClickPost:function(idPost){//
        console.log('Olá: '+idPost)
        //JSController.submitAjax("/process", postRequest, "main", JSController.setContent);
    }
}