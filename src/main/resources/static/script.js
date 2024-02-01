const gnbLi = document.querySelectorAll('.gnb > li');
const subMenu = document.querySelectorAll('.sub-wrap');

for(let i = 0; i < gnbLi.length; i++){
    gnbLi[i].addEventListener('mouseover', function() {
        subMenu[i].classList.add('active');
    });
}

for(let i = 0; i < gnbLi.length; i++){
    gnbLi[i].addEventListener('mouseleave', function() {
        subMenu[i].classList.remove('active')
    });
}
