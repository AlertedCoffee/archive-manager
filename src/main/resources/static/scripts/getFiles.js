

function getFiles(){
  request(null, '/api/get_trashed_files')
}

function request(path, url){
  $.ajax({
    url: url,
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