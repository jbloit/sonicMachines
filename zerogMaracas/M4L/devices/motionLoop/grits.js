// DISPLAY PARTICLES (GRITS) FROM 4 SHAKERS PROPERTIES
// AND OUPTUT MOUSE EVENTS
// 
// SHAKER INPUT LIST SYNTAX :
//  <shaker index, grit size, shaker X, shaker Y, grit_1 X, grit_1 Y, grit_2 X, grit_2 Y, ...>
// in the code, a "SHELL" refers to a shaker.

mgraphics.init();
mgraphics.relative_coords = 0;
mgraphics.autofill = 0;

var nbShells = 4;	
var nbGritsPerShell = 5;
var colorarray = new Array();
var shellCoords = new Array();
var gritCoords = new Array();
var shellSizes = new Array();


// load initial array values

for (i=0; i<nbShells; i++) {
	colorarray[0] = new Array(1., 0.4, 0, 0.8);
    colorarray[1] = new Array(0.7, 0.2, 0.2, 0.8);
    colorarray[2] = new Array(0., 0.6, 0.6, 0.8);
    colorarray[3] = new Array(0., 0.7, 0, 0.8);
    gritCoords[i] = new Array( ); 
    shellCoords[i] = new Array();  
    shellSizes[i] = 10;
}

function list(){
    // update shells and grits
    idx = arguments[0];
    N = (arguments.length - 4) / 2;
    
    shellSizes[idx] = arguments[1]*100;
    shellCoords[idx][0] = arguments[2];
    shellCoords[idx][1] = arguments[3];
    
    for (i = 0; i<N; i++){
        gritCoords[idx][2*i] =  arguments[4 + (2*i)];
        gritCoords[idx][2*i + 1] =  arguments[4 + (2*i) + 1];
    }
}

function onclick(x,y,but,cmd,shift,capslock,option,ctrl){
    var coords = new Array();
    coords.push(x);
    coords.push(y);
    coords.push(but);
    outlet(0, coords);
}

function ondrag(x,y,but,cmd,shift,capslock,option,ctrl){
    var coords = new Array();
    coords.push(x);
    coords.push(y);
    coords.push(but);
    outlet(0, coords);
}

function onidle (x, y, button, mod1, shift, caps, opt, mod2){
    var coords = new Array();
    coords.push(x);
    coords.push(y);
    coords.push(button);
    outlet(0, coords);
}

function bang()
{
	mgraphics.redraw();
}

function paint(){
    var width = box.rect[2] - box.rect[0];
    var height = box.rect[3] - box.rect[1];

    // background
    with (mgraphics) {
        set_source_rgba(0, 0, 0 , 1);
        rectangle(0, 0, width, height);
        fill();
    }
        
	// draw the circles

		for (i=0; i<nbShells; i++) {
			with (mgraphics) {
				set_source_rgba(0.9,0.9,0.9,1.);
				ellipse(shellCoords[i][0]-5, shellCoords[i][1]-5, 10, 10);
                fill();
                radius = shellSizes[i]/2;
                set_source_rgba(colorarray[i][0], colorarray[i][1], colorarray[i][2],colorarray[i][3]);
                for (j = 0; j<nbGritsPerShell; j++){
                    ellipse(gritCoords[i][2*j]-radius, gritCoords[i][2*j + 1]-radius, shellSizes[i], shellSizes[i]);
                }
				fill();
			}
		}
}