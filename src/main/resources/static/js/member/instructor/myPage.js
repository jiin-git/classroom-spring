const baseUrl = "/api/members/instructor/my-page";
async function getInstructorMyPageInfo() {
    const response = await fetch(baseUrl)
        .then(res => {
            return res.json();
        }).then(res => {
            if (res.status && res.status !== 200) {
                window.alert(res.message);
            } else {
                return res;
            }
        })
    return response;
}

const instructorMyPageInfo = getInstructorMyPageInfo()
    .then(data => {
        addProfileImage(data);
        addMyPageInfo(data);
    });

function addProfileImage(data) {
    const profileImage = document.getElementById('profile-image');
    const imageFile = data.profileImage;
    if (imageFile) {
        currentImage.innerHTML +=
            `<img src="data:${imageFile.dataType};base64,${imageFile.imageData}" class="img-fluid">`;
    } else {
        currentImage.innerHTML +=
            `<div class="profile-img-container">
                <svg xmlns="http://www.w3.org/2000/svg" class="bi bi-person-fill default-profile-img" viewBox="0 0 16 16">
                    <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3Zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"/>
                </svg>
            </div>`
    }
}
function addMyPageInfo(data) {
    const name = document.getElementById('mypage-info-name');
    const loginId = document.getElementById('mypage-info-id');
    const email = document.getElementById('mypage-info-email');

    name.value = `${data.name}`;
    loginId.value = `${data.loginId}`;
    email.value = `${data.email}`;
}