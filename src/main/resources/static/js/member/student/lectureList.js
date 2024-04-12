const baseUrl = "/api/members/student/lectures";
async function getLectures() {
    const response = await fetch(`${baseUrl}${location.search}`)
        .then((res) => {
            return res.json();
        }).then(res => {
            if (res.status && res.status === 400) {
                window.alert(res.message);
            } else {
                return res;
            }
        })
    return response
}

const myLectures = getLectures()
    .then(res => {
        const lectures = res.content;
        for (const lecture of lectures) {
            addLectureList(lecture);
        }
        addPageButton(res);
    });

function addLectureList(lecture) {
    const lectureBasicInfo = lecture.lectureBasicInfo;
    const instructorName = lecture.instructorName;
    const profileImage = lecture.profileImage;
    const mapperId = lecture.mapperId;

    addLectureContainer(lectureBasicInfo);
    addLectureInfo(lectureBasicInfo, instructorName, mapperId);
    addProfileImage(profileImage, lectureBasicInfo.id);
}
function addLectureContainer(lectureBasicInfo) {
    const lectureList = document.getElementById('lecture-list');
    lectureList.innerHTML +=
        `<div id="${lectureBasicInfo.id}" class="row g-0">
           <div class="col-md-4 text-center display-vertical-center p-3">
               <div id="lecture-profile-${lectureBasicInfo.id}" class="small-rounded-square-container"></div>
            </div>

           <div class="col-md-8">
               <div id="lecture-info-${lectureBasicInfo.id}" class="card-body"></div>
           </div>
       </div>`;
}
function addLectureInfo(lectureBasicInfo, instructorName, mapperId) {
    const lectureInfoContainer = document.getElementById(`lecture-info-${lectureBasicInfo.id}`);
    lectureInfoContainer.innerHTML += `
            <h5 class="card-title gmarketSansFont">${lectureBasicInfo.name}</h5>
            <p class="card-text"><small class="text-body-secondary">${instructorName}</small></p>
            <a class="btn btn-secondary" href="/student/lectures/${mapperId}">강의정보</a>`;
}
function addProfileImage(profileImage, lectureId) {
    const lectureProfile = document.getElementById(`lecture-profile-${lectureId}`);
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
            `<img src="data:${profileImage.dataType};base64,${profileImage.imageData}" class="img-fluid">`;
    }
}
function addPageButton(response) {
    const showPages = getShowPages(response);
    const pagination = document.getElementById('pagination');
    const totalPages = ((response.totalPages > 0) ? response.totalPages : 1);

    const checkStartPage = showPages.some((page) => page === 1);
    const checkEndPage = showPages.some((page) => page === totalPages);

    pagination.innerHTML += 
        `<li id="first-page" class="page-item">
            <button type="submit" class="page-link" name="page" value="1" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </button>
        </li>`;
    
    if (!checkStartPage) {
        pagination.innerHTML +=
            `<li class="page-item">
                <a class="page-link" href="#" aria-label="morePrevious">...</a>
            </li>`;
    }

    for (let p = showPages[0]; p <= showPages[showPages.length - 1]; p++) {
        pagination.innerHTML +=
            `<li class="page-item">
                <button type="submit" class="page-link" name="page" value="${p}">${p}</button>
            </li>`
    }

    if (!checkEndPage) {
        pagination.innerHTML +=
            `<li class="page-item">
               <a class="page-link" href="#" aria-label="moreNext">...</a>
            </li>`;
    }

    pagination.innerHTML +=
        `<li class="page-item">
            <button class="page-link" name="page" value="${showPages.pop()}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </button>
        </li>`
}
function getShowPages(response) {
    const startPage = 1;
    const showPageUnit = 3;

    const page = response.pageable.pageNumber + 1;
    const endPage = ((response.totalPages > 0) ? response.totalPages : 1);

    let pages = [];
    for (let i = 1; i <= endPage; i++) {
        pages.push(i);
    }

    if (endPage > showPageUnit) {
        if (page == startPage) {
            return pages.slice(startPage - 1, showPageUnit);
        } else if (page == endPage) {
            return pages.slice(endPage - showPageUnit, endPage);
        } else {
            return pages.slice(page - 2, page + 1);
        }
    } else {
        return pages;
    }
}
