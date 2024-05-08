let pdfDocument = null;
let pageNumber = 1;
let url = '';
let canvasId = '';

async function Canvas(pN, u, canId, searchString){
    pageNumber = pN;
    url = u;
    canvasId = canId;

    await renderCanvas();
    searchInPDF(searchString);
}

async function renderCanvas() {
    if (url == '') return;
      //Setting URL of the PDF document that you want to render
    //   var { pdfjsLib } = globalThis;
    //   pdfjsLib.GlobalWorkerOptions.workerSrc = '/static/pdfjs-dist/build/pdf.worker.mjs';
      
    //   // Rendering the PDF on the canvas
    //   pdfjsLib.getDocument(url).promise.then(pdf => {
    //       // Loading the first page of the PDF
    //       pdfDocument = pdf;
    //       return pdf.getPage(pageNumber);
    //     }).then(page => {
        
    //       // Setting the PDF zoom level to 100% by setting scale to 1
    //       const viewport = page.getViewport({ scale: 1 });
        
    //       // Prepare canvas using PDF page dimensions
    //       const canvas = document.getElementById(canvasId);
    //       const context = canvas.getContext("2d");
    //       canvas.height = viewport.height;
    //       canvas.width = viewport.width;
        
    //       // Render PDF page into canvas context
    //       const renderContext = {
    //         canvasContext: context,
    //         viewport: viewport,
    //       };
        
    //       return page.render(renderContext);
    //     });

    var { pdfjsLib } = globalThis;
    pdfjsLib.GlobalWorkerOptions.workerSrc = '/static/pdfjs-dist/build/pdf.worker.mjs';

    try {
        const pdf = await pdfjsLib.getDocument(url).promise;
        pdfDocument = pdf;
        const page = await pdf.getPage(pageNumber);

        const viewport = page.getViewport({ scale: 1 });

        const canvas = document.getElementById(canvasId);
        const context = canvas.getContext("2d");
        canvas.height = viewport.height;
        canvas.width = viewport.width;

        const renderContext = {
            canvasContext: context,
            viewport: viewport,
        };

        await page.render(renderContext);
    } catch (error) {
        console.error('Error rendering canvas:', error);
    }
        
}

    

function searchInPDF(searchParam) {
    if (pdfDocument == null) return;
    const searchText = searchParam.toLowerCase().trim().replace(/[.,\/#!$%\^&\*;:{}=\-_`~()]/g,"");;

    // Exit if no text entered
    if (!searchText) return;

    // Remove previous highlights
    const previousHighlights = document.querySelectorAll('.highlight');
    previousHighlights.forEach(highlight => highlight.remove());

    // Perform search
    pdfDocument.getPage(pageNumber).then(function(page) {
        page.getTextContent().then(function(textContent) {
            searchedItems = [];

            textContent.items.forEach(function(item) {
                const text = item.str.toLowerCase().trim();
                let searchTerms = searchText.split(" ");
                searchTerms.forEach(function(searchItem){
                    
                    if (text == searchItem) {
                        searchedItems.push(item);
                    }

                });
            });
            

            searchedItems.forEach(function(item){
                const textLayerDiv = document.createElement('div');
                textLayerDiv.classList.add('highlight');
                textLayerDiv.style.position = 'absolute';
                textLayerDiv.style.left = (item.transform[4]) + 'px';
                textLayerDiv.style.bottom = (item.transform[5] - 117) + 'px';
                textLayerDiv.style.width = item.width + 'px';
                textLayerDiv.style.height = item.height + 'px';
                textLayerDiv.style.backgroundColor = 'rgba(255, 255, 0, 0.5)';
                document.getElementById(canvasId).parentNode.appendChild(textLayerDiv);
            });
        });
    });
}