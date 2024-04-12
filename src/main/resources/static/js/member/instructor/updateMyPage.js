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

const initializeProfileBtn = document.getElementById('initialize-profile-btn');
initializeProfileBtn.addEventListener("click", async function (e) {
    e.preventDefault();

    const requestUrl = "/api/members/instructor/my-page/profile"
    const response = await fetch(requestUrl, {
        method: 'DELETE'
    })
        .then(res => {
            if (res.status === 204) {
                const redirectUrl = location.pathname;
                location.assign(encodeURI(redirectUrl));
            }
            else {
                return res.json();
            }
        }).then(res => {
            if (res.status === 400) {
                let noErrorFields = ['email'];
                if (res.errors) {
                    Object.keys(res.errors).forEach((value, key, array) => {
                        const errorField = document.getElementById(`${value}-error`);
                        errorField.innerText = `${res.errors[value]}`;
                        const index = noErrorFields.indexOf(value);
                        if (index !== -1) {
                            noErrorFields.splice(index, 1);
                        }});
                    removeErrorMessage(noErrorFields);
                }
                else {
                    removeErrorMessage(noErrorFields);
                    window.alert(res.message);
                }
            }
        });
});

const updateMyPageForm = document.getElementById('update-mypage-form');
updateMyPageForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const requestUrl = "/api/members/instructor/my-page"
    const formData = new FormData(updateMyPageForm);
    const updateFormData = new FormData();
    updateFormData.append("email", formData.get("email"));
    updateFormData.append("imageFile", formData.get("imageFile"));

    const response = await fetch(requestUrl, {
        method: 'PUT',
        body: updateFormData
    }).then(res => {
        if (res.status === 204) {
            const redirectUrl = "/instructor/my-page";
            location.assign(encodeURI(redirectUrl));
        }
        else {
            return res.json();
        }
    }).then(res => {
        if (res.status === 400) {
            window.alert(res.message);
        }
    })
});

function addProfileImage(data) {
    const currentImage = document.getElementById('currentImgContainer');
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
    const name = document.getElementById('name');
    const loginId = document.getElementById('loginId');
    const email = document.getElementById('email');

    name.value = `${data.name}`;
    loginId.value = `${data.loginId}`;
    email.value = `${data.email}`;
}
function removeErrorMessage(noErrorFields) {
    noErrorFields.forEach((value, index, array) => {
        const errorField = document.getElementById(`${value}-error`);
        errorField.innerText = null;
    });
}