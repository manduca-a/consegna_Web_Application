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



document.addEventListener('DOMContentLoaded', function toggleForm () {
    var addicon = document.querySelector('.add-icon');
    var form = document.querySelector('.upload-formm');
    var menuL = document.querySelector('.menu-laterale');

    let direction = -1;
    let rotation = 90;

    addicon.addEventListener('click', function () {
        form.classList.toggle('show-form');
        menuL.classList.remove('show-menu')
        rotation = rotation * direction;
        addicon.style.transform = `rotate(${rotation}deg)`;
    });

    window.addEventListener('resize', function () {
        if (window.innerWidth > 768) {
            form.classList.remove('show-form');
        }
    });
});