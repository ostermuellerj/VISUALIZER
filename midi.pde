

//send change on note press
void noteOn(int channel, int pitch, int velocity) {
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
void noteOff(int channel, int pitch, int velocity) {
  // // Receive a noteOff
  // println();
  // println("Note Off:");
  // println("--------");
  // println("Channel:"+channel);
  // println("Pitch:"+pitch);
  // println("Velocity:"+velocity);
}

//run on controller signal
void controllerChange(int channel, int number, int value) {
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
    init_gain1=sensitivity*(float(value));
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
    sensitivity=100*(float(value)/127.0);
    println("SENS:",sensitivity);    
    break;

  }
}