ymaps.ready(init);
var myMap, myPlacemark;

function init() {

    myMap = new ymaps.Map("yamap", {
        center: [59.91815364, 30.30557800],
        zoom: 10
    });
}


function addLabel(robot) {
    myPlacemark = new ymaps.Placemark([robot.coordinates.latitude, robot.coordinates.longitude], {
        hintContent: robot.id,
        balloonContent: robot.id
    });

    myMap.geoObjects.add(myPlacemark);
}