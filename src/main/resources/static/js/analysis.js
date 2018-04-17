function analysis(){

    $('#resp').html('');
    var input = $('#inputText').val();

    $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/analysis/input",
            data: input,
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function (data) {

                data.forEach(function(element) {
                  var match = $("<div/>").html(element.sentence+' | match : '+element.matchList.length)
                  var list =$("<ul/>");
                  if(element.matchList.length>0){
                      var i = 1;
                      element.matchList.forEach(function(m) {
                        var li = $('<li/>').append("["+(i++)+"] "+m.gid+" "+m.title+" anchors : "+m.anchors);
                        list.append(li);
                      });
                  }
                  $('#resp').append(match).append(list);
                });

            },
            error: function (e) {

            }
        });
}