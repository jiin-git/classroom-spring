function loadFile() {
    // console.log("call load File");
    let currentImgContainer = document.getElementById('currentImgContainer');
    let previewImgContainer = document.getElementById('previewImgContainer');
    let previewImg = document.getElementById('previewProfileImg');
    let imageFile = document.getElementById('imageFile');
    const [file] = imageFile.files;

    if (file) {
        previewImg.src = URL.createObjectURL(file);
        if (previewImgContainer.classList.contains('d-none')) {
            previewImgContainer.classList.remove('d-none');
            currentImgContainer.classList.add('d-none');
        }
    }
    else {
        previewImgContainer.classList.add("d-none");
        if (currentImgContainer.classList.contains("d-none")) {
            currentImgContainer.classList.remove("d-none");
        }
    }
};