//draw polyhedron at XYZ with polys number of polygon and size polyhsize 
void drawPolyhedron(int polys, float x, float y, float z, float polyhsize)
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
void triangle(float x, float y, float z, float w, float p)
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

float q (float ceiling) { return random(0,ceiling); }

//pick drawshapetype
void drawShape(float a, float k) {
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