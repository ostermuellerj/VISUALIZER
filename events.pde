class FXSystem {

	ArrayList<Effect> effects;
	int totalNumEffects=8;

	FXSystem () {
		effects = new ArrayList<Effect>();
	}

	void startEffect() {
	}

	void startRandomEffect(int n) {

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

	void run() {
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

		fade_in = constrain(f_in, 0, 0.5);
		fade_out = constrain(f_out, 0, 0.5);

		length = l;

		// if(fade_in>0) 
		//   strength_inc = length*fade_in/frameRate;
	}

	void run() {	
		if(!started) {
			start_time = millis();
			println("effect started."); //:)
			started=true;
		}

		if (random(1)>0.94) 
			println(strength);
		update();
		display();
	}

	void update() {
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
	void display() { println("generic display"); }

	void kill() { println("generic kill"); }
}

class DrawTrails extends Effect {
	DrawTrails(float max_s, float f_in, float f_out, float l) {
		super(max_s, f_in, f_out, l);
	}
	
	@Override
	void display() {
		// println("override display");
		doBackground=false;
	}

	@Override
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

	@Override
	void display() {
		doThicc=true;
	}

	@Override
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
	
	@Override
	void display() {
		if(!executed)
		{
			println("shape type++");
			drawShapeType=(drawShapeType+1)%numShapeTypes;
			executed=true;
		}
	}

	@Override
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
	
	@Override
	void display() {
		if(!executed)
		{
			println("change a");
			temp_a=a;
			a+=random(-3,3);
			executed=true;
		}
	}

	@Override
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
	
	@Override
	void display() {
		if(!executed)
		{
			println("change vel");
			temp_vel=vel;
			vel+=random(-3,3);
			executed=true;
		}
	}

	@Override
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
	
	@Override
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

	@Override
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
	
	@Override
	void display() {
		if(!executed)
		{
			println("gain up");
			init_gain*=(1+max_strength);
			executed=true;
		}
	}

	@Override
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

	@Override
	void display() {
		cam.beginHUD();
		// .bloom(.5*abs(sin(inc*10)), 20, 40)
		fx.render()
		  .bloom(.01, 20, 40)
		  .compose();
		cam.endHUD();
	}

	@Override
	void kill() {
		// println("override display");
		println("effect killed.");
		
	}
}
//🍄
//🍄