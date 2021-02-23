void keyPressed() {
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

void keyReleased() {
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