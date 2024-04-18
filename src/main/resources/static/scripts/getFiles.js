function getFiles(path){
    $.ajax({
      url: '/api/get_files',
      dataType:'json',
      type:'GET',
      data: {"path":path },
      success: function (data) {
        showData(data);
      },
      error: function (data) {
        console.error('Error:', data);
      }
    });
}