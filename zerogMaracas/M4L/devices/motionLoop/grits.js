mgraphics.init();
mgraphics.relative_coords = 0;
mgraphics.autofill = 0;

var nbShells = 4;	
var nbGritsPerShell = 5;
var colorarray = new Array();
var shellCoords = new Array();
var gritCoords = new Array();
var shellSizes = new Array();


post("init\n");


// load initial array values

for (i=0; i<nbShells; i++) {
    post(nbShells);
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

function paint()
{
	// set up our relative drawing things
	var aspect = (box.rect[2] - box.rect[0]) / (box.rect[3] - box.rect[1]);
	var twotimes = aspect * 2;
	var circ_xsize = twotimes / nbShells;
		
	// draw the circles

		for (i=0; i<nbShells; i++) {
            post("shell " + i + " x " +  shellCoords[i][0] + " y " + shellCoords[i][1] + "\n");
			with (mgraphics) {
				set_source_rgba(colorarray[i][0], colorarray[i][1], colorarray[i][2],colorarray[i][3]);
                radius = shellSizes[i]/2;
				ellipse(shellCoords[i][0]-radius, shellCoords[i][1]-radius, shellSizes[i], shellSizes[i]);
                for (j = 0; j<nbGritsPerShell; j++){
                    ellipse(gritCoords[i][2*j]-radius, gritCoords[i][2*j + 1]-radius, shellSizes[i], shellSizes[i]);
                }
				fill();
			}
		}
}