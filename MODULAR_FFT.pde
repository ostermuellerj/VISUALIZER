import themidibus.*; //Import the library

import ddf.minim.analysis.*;
import ddf.minim.*;
import peasy.*;
import peasy.org.apache.commons.math.*;
import peasy.org.apache.commons.math.geometry.*;
import java.util.*;

import ch.bildspur.postfx.builder.*;
import ch.bildspur.postfx.pass.*;
import ch.bildspur.postfx.*;

PostFX fx;
MidiBus myBus;

PeasyCam cam;
Minim minim;
AudioInput input;

boolean doColorChange=false;
color colorChange;

float size=40;
float amp;
float gain=100;
float init_gain, init_gain1;
float sum, best;

int numShapeTypes=4;

float colorSpec=40; //shift
float colorSpan=1.0;
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

void setup()
{
  init_gain = gain;
  init_gain1 = init_gain;
  MidiBus.list();
  background(0);
  // size(1200, 100, P3D);
  // size(1280, 800, P3D);
  fullScreen(P3D);
  colorMode(HSB);
  frameRate(20);
  noCursor();

  cam = new PeasyCam(this, 100);
  fx = new PostFX(this);  

  myBus = new MidiBus(this, 0, -1);
  minim = new Minim(this);
  input = minim.getLineIn();
  fft = new FFT(input.bufferSize(), input.sampleRate());

  perspective(PI/2, float(width)/float(height), 1, 80000);

  particles = new ArrayList<Particle>();
  ps = new ParticleSystem(new PVector(width/2,height/2));
  fxsys = new FXSystem();

  ellipseMode(RADIUS);

  // TRAILS = new DrawTrails(1, 4000, 0, 8000);
  // fxsys.effects.add(TRAILS);

  // println(fxsys.effects.toString());

  // noLoop();
}

void draw()
{
  pushMatrix();
  // maxNumParticles+=abs(sin(inc*10)*50);
  numParticlesToAdd+=abs(sin(inc*10)*2);
  
  if(random(1)>0.999) println("time:", millis());
  inc+=radians(.04);

  if(random(1)>0.99)
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
  if(random(1)>0.99) {
    // GainUp cc = new GainUp(20, 0, 0, 10000, "");
    // fxsys.effects.add(cc);
    // TRAILS = new DrawTrails(1, 1000, 0, random(1000,7000));
    // fxsys.effects.add(TRAILS);
    fxsys.startRandomEffect(int(random(0,fxsys.totalNumEffects)));
  }
  if(!doBackground && random(1)>random(0.9,0.96) && !jPressed)
  {
    // println("STOP TRAILS");
    // doBackground=true;
  } else if (doThicc && random(1)>random(0.9,0.96))
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

void event(int state){
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
