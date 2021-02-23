import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import themidibus.*; 
import ddf.minim.analysis.*; 
import ddf.minim.*; 
import peasy.*; 
import peasy.org.apache.commons.math.*; 
import peasy.org.apache.commons.math.geometry.*; 
import java.util.*; 
import ch.bildspur.postfx.builder.*; 
import ch.bildspur.postfx.pass.*; 
import ch.bildspur.postfx.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MODULAR_FFT extends PApplet {

 //Import the library












PostFX fx;
MidiBus myBus;

PeasyCam cam;
Minim minim;
AudioInput input;

boolean doColorChange=false;
int colorChange;

float size=40;
float amp;
float gain=100;
float init_gain, init_gain1;
float sum, best;

int numShapeTypes=4;

float colorSpec=40; //shift
float colorSpan=1.0f;
float numParticlesToAdd = 3;
int maxNumParticles = 200;

float vel=2; //particle velocity 
float a=1; //particle accel

float offset=55;

int drawShapeType=0;
int state=0;

float sensitivity=1;
float bkg=255;
boolean doShake=true,
        doBackground=true,
        jPressed=false,
        doThicc=false;


FFT fft;
int[][] colo=new int[300][3];
//AudioIn in;
float inc;
float inc2; 
PFont helv, helv1;
ParticleSystem ps;
FXSystem fxsys;
ArrayList<Particle> particles;
DrawTrails TRAILS;

public void setup()
{
  init_gain = gain;
  init_gain1 = init_gain;
  MidiBus.list();
  background(0);
  // size(1200, 100, P3D);
  // size(1280, 800, P3D);
  
  colorMode(HSB);
  frameRate(20);
  noCursor();

  cam = new PeasyCam(this, 100);
  fx = new PostFX(this);  

  myBus = new MidiBus(this, 0, -1);
  minim = new Minim(this);
  input = minim.getLineIn();
  fft = new FFT(input.bufferSize(), input.sampleRate());

  perspective(PI/2, PApplet.parseFloat(width)/PApplet.parseFloat(height), 1, 80000);

  particles = new ArrayList<Particle>();
  ps = new ParticleSystem(new PVector(width/2,height/2));
  fxsys = new FXSystem();

  ellipseMode(RADIUS);

  // TRAILS = new DrawTrails(1, 4000, 0, 8000);
  // fxsys.effects.add(TRAILS);

  // println(fxsys.effects.toString());

  // noLoop();
}

public void draw()
{
  pushMatrix();
  // maxNumParticles+=abs(sin(inc*10)*50);
  numParticlesToAdd+=abs(sin(inc*10)*2);
  
  if(random(1)>0.999f) println("time:", millis());
  inc+=radians(.04f);

  if(random(1)>0.99f)
  {
    println("do a invert");
    for(int i=0; i<particles.size(); i++)
    {
      // particles.get(i).velocity.x*=-1;
      // particles.get(i).velocity.y*=-1;
      // particles.get(i).velocity.z*=-1;

      Particle temp = new Particle(particles.get(i).location, particles.get(i).multi);
      temp.velocity.x*=-1;
      temp.velocity.y*=-1;
      temp.velocity.z*=-1;
      particles.set(i, temp);
      // particles.set(i, particles.get(i).velocity.y);
      // particles.set(i, particles.get(i).velocity.z);
    }
  }

  // if(random(1)>0.993 && fxsys.effects.size()==0) {
  if(random(1)>0.99f) {
    // GainUp cc = new GainUp(20, 0, 0, 10000, "");
    // fxsys.effects.add(cc);
    // TRAILS = new DrawTrails(1, 1000, 0, random(1000,7000));
    // fxsys.effects.add(TRAILS);
    fxsys.startRandomEffect(PApplet.parseInt(random(0,fxsys.totalNumEffects)));
  }
  if(!doBackground && random(1)>random(0.9f,0.96f) && !jPressed)
  {
    // println("STOP TRAILS");
    // doBackground=true;
  } else if (doThicc && random(1)>random(0.9f,0.96f))
  {
    // println("STOP VARIABLESTROKE");
    // doThicc=false;
  }

  
  
    if(doBackground) {
      cam.beginHUD();
        background(0,0,0);
      cam.endHUD();
    }
  
  fft.forward(input.mix);

  sum=0;
  translate(-width/2, -height/2);  

    // println(cam.getPosition());

    //rotate
    // cam.rotateX(inc/10);  // rotate around the x-axis passing through the subject
    // cam.rotateY(sin(inc/15));  // rotate around the y-axis passing through the subject
    // cam.rotateZ(sin(inc/15));  // rotate around the z-axis passing through the subject
    // cam.setDistance(sin(inc/15));  // distance from looked-at point
    // cam.pan(0, sin(inc/50)*0);   // move the looked-at point relative to current orientation
    // cam.pan(width/2, height/2);
    pushMatrix();
      rotateX(sin(inc/15));
      rotateY(cos(inc/15));
      rotateZ(sin(inc/15));
    popMatrix();

    //gain adjust
    gain=init_gain*abs(sin(inc/100));
    init_gain=init_gain1*abs(10000*cos(inc/10));

    //generate new particles and run particlesystem
    for(int i=0; i<numParticlesToAdd && ps.particles.size()<maxNumParticles; i++) 
      ps.addParticle();
    
    ps.run();

    // if(random(1)>0.9) println(ps.particles.size());
    fxsys.run();

    //trigger events at random
    // if(random(1)>.995)
    //   event(0);
    // if(random(1)>.995)
    //   event(1);
    // if(random(1)>.95)
    //   event(2);
  popMatrix();
}

public void event(int state){
  switch(state) {
    case 0:
    // println("DRAWTYPE++(CURRENTLY DISABLED)");
    // println("DRAWTYPE++(CURRENTLY ENABLED)");
    drawShapeType=(drawShapeType+1)%4; //RANDOM EVENT DRAWSHAPETYPE
    // println("///////////");
    break;

    case 1:
    // println("TRAILS");
    doBackground=false;
    break;

    case 2:
    // println("VARIABLESTROKE");
    doThicc=true;
    break;
  }
}
class FXSystem {

	ArrayList<Effect> effects;
	int totalNumEffects=8;

	FXSystem () {
		effects = new ArrayList<Effect>();
	}

	public void startEffect() {
	}

	public void startRandomEffect(int n) {

		switch(n) {
			case 0:
				DrawTrails trails = new DrawTrails(1, 1000, 0, random(1000, 7000));
		    	fxsys.effects.add(trails);
		    	break;
			case 1:
				ThickLines thick = new ThickLines(1, 1000, 0, random(1000, 10000));
		    	fxsys.effects.add(thick);
		    	break;
			case 2:
				// IncShapeType shapeType = new IncShapeType(1, 1000, 0, random(1000, 7000), random(0,1), random(int numShapeTypes=4) );
				IncShapeType shapeType = new IncShapeType(1, 1000, 0, random(1000, 7000), (drawShapeType+1)%numShapeTypes);
		    	fxsys.effects.add(shapeType);
		    	break;
			case 3:
				// IncShapeType shapeType = new IncShapeType(1, 1000, 0, random(1000, 7000), random(0,1), random(int numShapeTypes=4) );
				ChangeMoveType cmt = new ChangeMoveType(1, 1000, 0, random(1000, 7000), "");
		    	fxsys.effects.add(cmt);
		    	break;
			case 4:
				// IncShapeType shapeType = new IncShapeType(1, 1000, 0, random(1000, 7000), random(0,1), random(int numShapeTypes=4) );
				ChangeMoveType2 cmt2 = new ChangeMoveType2(1, 1000, 0, random(1000, 7000), "");
		    	fxsys.effects.add(cmt2);
		    	break;
		    case 5:
				// IncShapeType shapeType = new IncShapeType(1, 1000, 0, random(1000, 7000), random(0,1), random(int numShapeTypes=4) );
				ChangeColor cc = new ChangeColor(1, 0, 0, random(20, 1000), "");
		    	fxsys.effects.add(cc);
		    	break;
		    case 6:
				// IncShapeType shapeType = new IncShapeType(1, 1000, 0, random(1000, 7000), random(0,1), random(int numShapeTypes=4) );
				GainUp gu = new GainUp(random(2), 0, 0, random(20, 8000), "");
		    	fxsys.effects.add(gu);
		    	break;
		    case 7:
				// IncShapeType shapeType = new IncShapeType(1, 1000, 0, random(1000, 7000), random(0,1), random(int numShapeTypes=4) );
				Bloom b = new Bloom(1, 1000, 0, random(1000, 7000));
		    	fxsys.effects.add(b);
		    	break;
    	}
	}

	public void run() {
		Iterator iter = effects.iterator();
      	Effect temp;
      	while (iter.hasNext()) {
          temp = (Effect) iter.next();
          if (temp.dead) {
          	temp.kill();
            iter.remove();
            println("removed a dead effect.");
            break;
          }
          else
          	temp.run();
      	}
	}
}

class Effect {
	boolean started=false, dead=false;
	float start_time;
	float strength=1, strength_inc=0, max_strength;
	float fade_in, fade_out; //length of fade in/out in ms
	float length; //length of animation in ms
	
	//max strength, fade in length, fade out length, total animation length (-1 = always on)
	Effect(float max_s, float f_in, float f_out, float l) {
		max_strength = max_s;

		fade_in = constrain(f_in, 0, 0.5f);
		fade_out = constrain(f_out, 0, 0.5f);

		length = l;

		// if(fade_in>0) 
		//   strength_inc = length*fade_in/frameRate;
	}

	public void run() {	
		if(!started) {
			start_time = millis();
			println("effect started."); //:)
			started=true;
		}

		if (random(1)>0.94f) 
			println(strength);
		update();
		display();
	}

	public void update() {
		// println(strength, strength_inc);
		//if animation time < fade in length, increment strength 
		// if(millis()-start_time<length*fade_in) {


		// 	strength_inc = length*fade_out/frameRate;
		// 	strength+=strength_inc;


		// }
		// //if animation time > length - fade out length, increment strength 
		// else if (millis()-start_time<length*fade_in)
		// {
		// 	strength_inc = length*fade_out/frameRate;
		// 	strength-=strength_inc;
		// }
		// else if (length!=-1 && millis()-start_time>length)
		// 	dead=true;

		if (length!=-1 && millis()-start_time>length)
			dead=true;
	}
	
	//each effect class has their own display function
	public void display() { println("generic display"); }

	public void kill() { println("generic kill"); }
}

class DrawTrails extends Effect {
	DrawTrails(float max_s, float f_in, float f_out, float l) {
		super(max_s, f_in, f_out, l);
	}
	
	public @Override
	void display() {
		// println("override display");
		doBackground=false;
	}

	public @Override
	void kill() {
		// println("override display");
		println("effect killed.");
		doBackground=true;
	}
}

class ThickLines extends Effect {
	ThickLines(float max_s, float f_in, float f_out, float l) {
		super(max_s, f_in, f_out, l);
	}

	public @Override
	void display() {
		doThicc=true;
	}

	public @Override
	void kill() {
		// println("override display");
		println("effect killed.");
		doThicc=false;
	}
}

class IncShapeType extends Effect {
	int leaveType;
	boolean executed=false;

	IncShapeType(float max_s, float f_in, float f_out, float l, int leaveType_) {
		super(max_s, f_in, f_out, l);
		leaveType=leaveType_;
	}
	
	public @Override
	void display() {
		if(!executed)
		{
			println("shape type++");
			drawShapeType=(drawShapeType+1)%numShapeTypes;
			executed=true;
		}
	}

	public @Override
	void kill() {
		// println("override display");
		println("effect killed.");
		drawShapeType=leaveType;
	}
}

class ChangeMoveType extends Effect {
	String leaveType;
	boolean executed=false;
	float temp_a;

	ChangeMoveType(float max_s, float f_in, float f_out, float l, String leaveType_) {
		super(max_s, f_in, f_out, l);
		leaveType=leaveType_;
	}
	
	public @Override
	void display() {
		if(!executed)
		{
			println("change a");
			temp_a=a;
			a+=random(-3,3);
			executed=true;
		}
	}

	public @Override
	void kill() {
		// println("override display");
		println("normal a");
		a=temp_a;
	}
}

class ChangeMoveType2 extends Effect {
	String leaveType;
	boolean executed=false;
	float temp_vel;

	ChangeMoveType2(float max_s, float f_in, float f_out, float l, String leaveType_) {
		super(max_s, f_in, f_out, l);
		leaveType=leaveType_;
	}
	
	public @Override
	void display() {
		if(!executed)
		{
			println("change vel");
			temp_vel=vel;
			vel+=random(-3,3);
			executed=true;
		}
	}

	public @Override
	void kill() {
		// println("override display");
		println("normal vel");
		vel=temp_vel;
	}
}

class ChangeColor extends Effect {
	String leaveType;
	boolean executed=false;

	ChangeColor(float max_s, float f_in, float f_out, float l, String leaveType_) {
		super(max_s, f_in, f_out, l);
		leaveType=leaveType_;
	}
	
	public @Override
	void display() {
		if(!executed)
		{
			println("change color");
			doColorChange=true;
			float randomR=random(255);
			float randomG=random(255);
			float randomB=random(255);
			colorChange=color(randomR, randomG, randomB);
			executed=true;
		}
	}

	public @Override
	void kill() {
		// println("override display");
		println("normal colors");
		doColorChange=false;
	}
}
class GainUp extends Effect {
	String leaveType;
	boolean executed=false;

	GainUp(float max_s, float f_in, float f_out, float l, String leaveType_) {
		super(max_s, f_in, f_out, l);
		leaveType=leaveType_;
	}
	
	public @Override
	void display() {
		if(!executed)
		{
			println("gain up");
			init_gain*=(1+max_strength);
			executed=true;
		}
	}

	public @Override
	void kill() {
		// println("override display");
		println("normal colors");
		init_gain/=(1+max_strength);
		doColorChange=false;
	}
}

class Bloom extends Effect {
	Bloom(float max_s, float f_in, float f_out, float l) {
		super(max_s, f_in, f_out, l);
	}

	public @Override
	void display() {
		cam.beginHUD();
		// .bloom(.5*abs(sin(inc*10)), 20, 40)
		fx.render()
		  .bloom(.01f, 20, 40)
		  .compose();
		cam.endHUD();
	}

	public @Override
	void kill() {
		// println("override display");
		println("effect killed.");
		
	}
}
//\ud83c\udf44
//\ud83c\udf44
public void keyPressed() {
  if (key == '1' && state >= 0) {
    background(255);
    updatePixels();
    state--;
    println(state);
  } else if (key == '2' && state <= 8) {
    background(255);
    state++;
    println(state);
  }
  drawShapeType=state;

  if (keyCode == LEFT) 
  {
    // gain/=2;
    init_gain1/=2;  
    println(gain);
  } 
  else if (keyCode == RIGHT)
  {
    // gain*=2;
    init_gain1*=2;
    println(gain);
  }
  else if (key == 'k')
  {
    doShake=true;
  }
  else if (key == 'j')
  {
    doBackground=!doBackground;
    jPressed=!jPressed;
  }
  else if (key == 'l')
  {
    bkg=80;
  }
  // println("GAIN:",gain);
}

public void keyReleased() {
  if (key == 'k')
  {
    doShake=true;
  }
  if (key == 'j')
  {
    doBackground=true;
  }
  else if (key == 'l')
  {
    bkg=200;
  }
}


//send change on note press
public void noteOn(int channel, int pitch, int velocity) {
  println("Pitch:"+pitch);  
  switch (pitch) {
    case 48:
    drawShapeType=0;
    println("48:!!!");
    break;

    case 49:
    drawShapeType=1;    
    println("49:!!!");
    break;

    case 50:
    drawShapeType=2;    
    println("50:!!!");
    break;

    case 51:
    drawShapeType=3;    
    println("51:!!!");
    break;

    case 44:
    doShake=!doShake;    
    println("44:!!!");
    break;
  }
}

//run on note off
public void noteOff(int channel, int pitch, int velocity) {
  // // Receive a noteOff
  // println();
  // println("Note Off:");
  // println("--------");
  // println("Channel:"+channel);
  // println("Pitch:"+pitch);
  // println("Velocity:"+velocity);
}

//run on controller signal
public void controllerChange(int channel, int number, int value) {
  // Receive a controllerChange
  // println();
  // println("Controller Change:");
  // println("--------");
  // println("Channel:"+channel);
  // println("Number:"+number);
  // println("Value:"+value);
  switch(number)
  {
    case 1:
    bkg=value*2;
    println("BKG:",bkg);
    break;

    case 2:
    init_gain1=sensitivity*(PApplet.parseFloat(value));
    println("GAIN:",init_gain1);    
    break;

    case 3:
    colorSpan=value/64;
    println("colorSpan:",colorSpan);    
    break;

    case 4:
    colorSpec=value*2;
    println("colorSpec:",colorSpec);
    break;

    case 5:
    numParticlesToAdd=value;
    break;

    case 6:
    vel=value/10;
    break;

    case 8:
    sensitivity=100*(PApplet.parseFloat(value)/127.0f);
    println("SENS:",sensitivity);    
    break;

  }
}
class ParticleSystem {

  ArrayList<Particle> particles;
  PVector origin;
 
  ParticleSystem(PVector location) {
    origin = location.get();
    particles = new ArrayList<Particle>();
  }
 
  public void addParticle() {
    int multiHolder=1;
    if(random(1)>.99f)
      multiHolder=PApplet.parseInt(random(20));
    particles.add(new Particle(origin, multiHolder));
  }
 
  public void applyForce(PVector f) {
    for (Particle p: particles) {
      p.applyForce(f);
    }
  }
 
  public void run() {
    Iterator<Particle> it = particles.iterator();
    while (it.hasNext()) {
      Particle p = (Particle) it.next();
      p.run();
      if (p.isDead()) {
        it.remove();
      }
    }
  }
}

//particle declaration and constructor
class Particle {
  PVector location;
  PVector velocity;
  PVector acceleration;
  float lifespan;
  int multi=1;
 
  float mass = 1;
 
  Particle(PVector l, int multiNum) {
    
    acceleration = new PVector(random(-a,a), random(-a,a), random(-a,a));
    velocity = new PVector(random(-vel,vel),random(-vel,vel),random(-vel,vel));
    location = l.get();
    lifespan = 255;
    multi = multiNum;
  }
 
 //run update and display for each particle
  public void run() {
    update();
    display();
  }

//apply force to partciles
  public void applyForce(PVector force) {
    PVector f = force.get();
    f.div(mass);
    acceleration.add(f);
  }
 
 //update particle position
  public void update() {
    velocity.add(acceleration);

    location.add(velocity);
    acceleration.mult(0);
    lifespan -= 4.0f;
  }
 
 //display particles in psystem
  public void display() {
    noFill();
    // fill(255,lifespan);
    pushMatrix();
      translate(location.x,location.y,location.z);
      if(random(1)>0.50f && doThicc)
        strokeWeight(random(80));
      else strokeWeight(1);
      pushMatrix();
        if(doShake) {
          inc2+=radians(3);

          //rotate by lifespan and noise
          rotateX(lifespan*cos(inc)*sin(inc)/10*noise(inc));
          rotateY(lifespan*sin(inc)*cos(inc)/10*noise(inc+PI/3));
          rotateZ(lifespan*sin(inc)*sin(inc)/10*noise(inc+TWO_PI/3));
        }

        amp= 0.002f*gain-fft.getBand(PApplet.parseInt((lifespan/255)*512))*10*gain/100;

        for(float i=multi; i>0; i--) {      

          if(!doColorChange) 
            // stroke((((lifespan/colorSpan)%255+colorSpec+inc*500)%255*noise(location.x,location.y,location.z)+10*noise(location.z,location.y,location.x))%255, lifespan*2, lifespan*2, lifespan*2);
            stroke((((lifespan/colorSpan)%255+colorSpec+inc*500)%255*noise(location.x,location.y,location.z)+90*noise(location.z,location.y,location.x))%255, lifespan*noise(location.x,location.y,location.z), lifespan/2, lifespan/2);
          else
            stroke(colorChange);

          drawShape(amp,i);
        }
      popMatrix();
    popMatrix();
  }
 
  public boolean isDead() {
    if (lifespan < 0.0f)
      return true;
    else
      return false;
  }
}
//draw polyhedron at XYZ with polys number of polygon and size polyhsize 
public void drawPolyhedron(int polys, float x, float y, float z, float polyhsize)
{
  for(float i=0; i<polys; i++)
  {
    pushMatrix();
      translate(x, y, z);
      if(i%2==1)
        rotateX(PI);
      rotateY((i/polys)*PI);
      // rotateZ((i/polys)*TWO_PI);    
      triangle(0,0,0,polyhsize,0);
    popMatrix();
  }
}

//draw triangle @ XYZ with width w and noise value p
public void triangle(float x, float y, float z, float w, float p)
{
  pushMatrix();
  translate(0, w*sqrt(3)/3, 0); //translate from relative coords to top point to relative coords to centroid
    beginShape();
    vertex(x+q(p), y+q(p), z+q(p)); //top point
    vertex(x-(w/2)+q(p), y-((sqrt(3)/2)*w)+q(p), z+q(p)); //bottom left
    vertex(x+(w/2)+q(p), y-((sqrt(3)/2)*w)+q(p), z+q(p)); //bottom right
    endShape(CLOSE);
  popMatrix();
}

public float q (float ceiling) { return random(0,ceiling); }

//pick drawshapetype
public void drawShape(float a, float k) {
  switch(drawShapeType) {
    case 0:
    box(amp);
    break;

    case 1:
    drawPolyhedron(8,0,0,0,amp);
    break;

    case 2:
    ellipse(0,0,amp,amp);
    break;

    case 3:
    triangle(0,0,0,amp,0); //
    break;

    case 4:
    triangle(0,0,0,amp,0);
    break;

    case 5:
    triangle(0,0,0,amp,0);
    break;

    case 6:
    triangle(0,0,0,amp,0);
    break;

    case 7:
    triangle(0,0,0,amp,0);
    break;

    case 8:
    triangle(0,0,0,amp,0);
    break;    
  }
}
  public void settings() {  fullScreen(P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "MODULAR_FFT" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
