const baseUrl = "/api/members/instructor/lectures"
const lectureId = location.pathname.split('/instructor/lectures/')[1];

async function getLectureInfo() {
    const response = await fetch(`${baseUrl}/${lectureId}`)
        .then(res => res.json());
    return response;
}
const lectureInfo = getLectureInfo()
    .then(response => {
        if (response.status && response.status === 400) {
            window.alert(response.message)
        }
        else {
            const lecture = response;
            const lectureBasicInfo = lecture.lectureBasicInfo;
            const instructorInfo = lecture.instructorInfo;
            const profileImage = lecture.profileImage;

            addLectureBasicInfo(lectureBasicInfo);
            addInstructorInfo(instructorInfo);
            addButtonLink(lectureBasicInfo.id);
            addProfileImage(profileImage);
        }
    })

function addLectureBasicInfo(lectureBasicInfo) {
    const lectureId = document.getElementById('lectureId');
    const lectureName = document.getElementById('lectureName');
    const personnel = document.getElementById('personnel');
    const remainingPersonnel = document.getElementById('remainingPersonnel');
    const lectureStatus = document.getElementById('lectureStatus');

    lectureId.value = `${lectureBasicInfo.id}`;
    lectureName.value = `${lectureBasicInfo.name}`;
    personnel.value = `${lectureBasicInfo.personnel}`;
    remainingPersonnel.value = `${lectureBasicInfo.remainingPersonnel}`;
    lectureStatus.value = `${lectureBasicInfo.lectureStatus}`;
}
function addInstructorInfo(instructorInfo) {
    const instructorId = document.getElementById('instructorId');
    const instructorName = document.getElementById('instructorName');
    instructorId.value = `${instructorInfo.id}`;
    instructorName.value = `${instructorInfo.name}`;
}
function addButtonLink(lectureId) {
    const appliedStudent = document.getElementById('appliedStudent');
    const editButton = document.getElementById('edit-button');
    appliedStudent.href = `/instructor/lectures/${lectureId}/applicants`;
    editButton.href = `/instructor/edit/lectures/${lectureId}`;
}
function addProfileImage(profileImage) {
    const lectureProfile = document.getElementById("lecture-profile");
    if (profileImage.imageData == null) {
        lectureProfile.innerHTML +=
            `<div class="profile-img-container">
                <svg xmlns="http://www.w3.org/2000/svg" class="bi bi-images default-lecture-img" viewBox="0 0 16 16">
                    <path d="M4.502 9a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3z"/>
                    <path d="M14.002 13a2 2 0 0 1-2 2h-10a2 2 0 0 1-2-2V5A2 2 0 0 1 2 3a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v8a2 2 0 0 1-1.998 2zM14 2H4a1 1 0 0 0-1 1h9.002a2 2 0 0 1 2 2v7A1 1 0 0 0 15 11V3a1 1 0 0 0-1-1zM2.002 4a1 1 0 0 0-1 1v8l2.646-2.354a.5.5 0 0 1 .63-.062l2.66 1.773 3.71-3.71a.5.5 0 0 1 .577-.094l1.777 1.947V5a1 1 0 0 0-1-1h-10z"/>
                </svg>
            </div>`;
    } else {
        lectureProfile.innerHTML +=
            `<img src="data:${profileImage.dataType};base64,${profileImage.imageData}" class="img-fluid">`
    }
}