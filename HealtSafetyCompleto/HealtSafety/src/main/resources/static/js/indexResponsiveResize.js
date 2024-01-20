document.addEventListener('DOMContentLoaded', function toggleMenu () {
    var menuIcon = document.querySelector('.menu-icon');
    var menuLinks = document.querySelector('.menu-laterale');

    menuIcon.addEventListener('click', function () {
        menuLinks.classList.toggle('show-menu');
    });

    window.addEventListener('resize', function () {
        if (window.innerWidth > 768) {
            menuLinks.classList.remove('show-menu');
        }
    });
});