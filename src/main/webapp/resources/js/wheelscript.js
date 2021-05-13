/* 
 *  Lewin Hafner
 *  Lewin.Hafner4@stud.bbbaden.ch
 */

//var width = window.innerWidth - (window.innerWidth / 3);
//var height = window.innerHeight - (window.innerHeight / 3);

var width = 500;
var height = 270;

Konva.angleDeg = false;
var angularVelocity = 6;
var angularVelocities = [];
var lastRotation = 0;
var controlled = false;
var numWedges = 15;
var angularFriction = 0.2;
var target, activeWedge, stage, layer, wheel, pointer;
var finished = false;
var reward = ['100', '200', '25', '300', '50', '400', 'Risk', '100', '1000', 'Bankrupt', '15', '500', '200', '25', '100'];

function getAverageAngularVelocity() {
    var total = 0;
    var len = angularVelocities.length;

    if (len === 0) {
        return 0;
    }

    for (var n = 0; n < len; n++) {
        total += angularVelocities[n];
    }

    return total / len;
}
function purifyColor(color) {
    var randIndex = Math.round(Math.random() * 3);
    color[randIndex] = 0;
    return color;
}
function getRandomColor() {
    var r = 100 + Math.round(Math.random() * 55);
    var g = 100 + Math.round(Math.random() * 55);
    var b = 100 + Math.round(Math.random() * 55);
    return purifyColor([r, g, b]);
}

function getRandomReward(n) {
    return reward[n];
}

function shuffle(array) {
    var currentIndex = array.length, temporaryValue, randomIndex;

    // while there remain elements to shuffle
    while (0 !== currentIndex) {

        // pick remaining element
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex -= 1;

        // swap with current element
        temporaryValue = array[currentIndex];
        array[currentIndex] = array[randomIndex];
        array[randomIndex] = temporaryValue;
    }

    return array;
}

function addWedge(n) {
    var s = getRandomColor();
    var reward = getRandomReward(n);
    var r = s[0];
    var g = s[1];
    var b = s[2];
    var angle = (2 * Math.PI) / numWedges;

    var endColor = 'rgb(' + r + ',' + g + ',' + b + ')';
    r += 100;
    g += 100;
    b += 100;

    var startColor = 'rgb(' + r + ',' + g + ',' + b + ')';

    var wedge = new Konva.Group({
        rotation: (2 * n * Math.PI) / numWedges
    });

    var wedgeBackground = new Konva.Wedge({
        radius: 250,
        angle: angle,
        fillRadialGradientStartPoint: 0,
        fillRadialGradientStartRadius: 0,
        fillRadialGradientEndPoint: 0,
        fillRadialGradientEndRadius: 250,
        fillRadialGradientColorStops: [0, startColor, 1, endColor],
        fill: '#64e9f8',
        fillPriority: 'radial-gradient',
        stroke: '#ccc',
        strokeWidth: 2
    });

    wedge.add(wedgeBackground);

    var yoffset;
    if (reward === 'Bankrupt') {
        yoffset = 10;
    } else if (reward === '1000') {
        yoffset = 30;
    } else {
        yoffset = 38;
    }

    var text = new Konva.Text({
        text: reward,
        fontFamily: 'Calibri',
        fontSize: 20,
        fill: 'white',
        align: 'center',
        stroke: 'yellow',
        strokeWidth: 1,
        rotation: (Math.PI + angle) / 2,
        x: 230,
        y: yoffset,
        listening: false
    });

    wedge.add(text);
    text.cache();
    wedge.startRotation = wedge.rotation();

    wheel.add(wedge);
}
function animate(frame) {
    // handle wheel spin
    var angularVelocityChange =
            (angularVelocity * frame.timeDiff * (1 - angularFriction)) / 1000;
    angularVelocity -= angularVelocityChange;

    // activate / deactivate wedges based on point intersection
    var shape = stage.getIntersection({
        x: stage.width() / 2,
        y: 100
    });

    if (controlled) {
        if (angularVelocities.length > 10) {
            angularVelocities.shift();
        }

        angularVelocities.push(
                ((wheel.rotation() - lastRotation) * 1000) / frame.timeDiff
                );
    } else {
        var diff = (frame.timeDiff * angularVelocity) / 1000;
        if (diff > 0.0001) {
            wheel.rotate(diff);
        } else if (!finished && !controlled) {
            if (shape) {
                var text = shape
                        .getParent()
                        .findOne('Text')
                        .text();
                var price = text.split('\n').join('');
                //alert('Your price is ' + price);
                document.getElementById("rolledForm:rolledField").value = price;
                var submitBtn = document.getElementById("rolledForm:submitRolled");
                submitBtn.click();
            }
            finished = true;
        }
    }
    lastRotation = wheel.rotation();

    if (shape) {
        if (shape && (!activeWedge || shape._id !== activeWedge._id)) {
            pointer.y(20);

            new Konva.Tween({
                node: pointer,
                duration: 0.3,
                y: 30,
                easing: Konva.Easings.ElasticEaseOut
            }).play();

            if (activeWedge) {
                activeWedge.fillPriority('radial-gradient');
            }
            shape.fillPriority('fill');
            activeWedge = shape;
        }
    }
}

function init() {
    stage = new Konva.Stage({
        container: 'container',
        width: width,
        height: height
    });
    layer = new Konva.Layer();
    wheel = new Konva.Group({
        x: stage.width() / 2,
        y: 270
    });

    shuffle(reward);

    for (var n = 0; n < numWedges; n++) {
        addWedge(n);
    }
    pointer = new Konva.Wedge({
        fillRadialGradientStartPoint: 0,
        fillRadialGradientStartRadius: 0,
        fillRadialGradientEndPoint: 0,
        fillRadialGradientEndRadius: 30,
        fillRadialGradientColorStops: [0, 'white', 1, 'red'],
        stroke: 'white',
        strokeWidth: 2,
        lineJoin: 'round',
        angle: 1,
        radius: 30,
        x: stage.width() / 2,
        y: 33,
        rotation: -90,
        shadowColor: 'black',
        shadowOffsetX: 3,
        shadowOffsetY: 3,
        shadowBlur: 2,
        shadowOpacity: 0.5
    });

    // add components to the stage
    layer.add(wheel);
    layer.add(pointer);
    stage.add(layer);

    var anim = new Konva.Animation(animate, layer);

    // wait one second and then spin the wheel
    setTimeout(function () {
        anim.start();
    }, 1000);
}
init();