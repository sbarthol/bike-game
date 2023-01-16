package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import java.awt.event.KeyEvent;

import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.actor.crate.Crate;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

public class BikeGame extends ActorGame {
	
	private TextGraphics message;
	
	public static final short CATEGORY_BIKE = -1;
	public static final short CATEGORY_FINISH = -2;
	public static final short CATEGORY_FLOOR = 1;
	private boolean finish;
	private Bike velo;
	
	public boolean begin(Window window, FileSystem fileSystem) {
		
		super.begin(window, fileSystem);
	
		createAllActors();
		
		finish = false;
		
		message = new TextGraphics("", 0.2f, Color.RED, Color.WHITE, 0.02f, true, false, new Vector(0.5f, 0.5f), 1.0f, 100.0f);
		message.setParent(getCanvas());
		message.setRelativeTransform(Transform.I.translated(0.0f, -1.0f));

		
		return true;
	}

	private void createAllActors() {
		
		Polyline line = new Polyline(
				-1000.0f, -1000.0f,
				-1000.0f, 0.0f, 
				0.0f, 0.0f,
				3.0f, 1.0f,
				8.0f, 1.0f, 
				15.0f, 3.0f,  
				25.0f, 4.0f, 
				25.0f, -15.0f,
				45.0f, -14.0f,
				45.0f, -7.0f,
				70.0f, -14.0f,
				80.0f, -14.0f,
				80.0f, -21.0f,
				125.0f, -36.0f,
				
				6500.0f, -40.0f
				);
		
		addActor(new Terrain(this,line));
		
		
		
		for(int i=0;i<30;i++) {
			
			Vector randomPos = new Vector((float)Math.random()*3.0f-11,(float)Math.random()*3.0f+7);
			addActor(new Caisse(this,false,randomPos, "box.4.png", 1.0f, 1.0f));
		}
		
		
		//velo = new Bike(this,new Vector(168.0f,-20.0f));
		//velo = new Bike(this,new Vector(128.0f,0.0f));
		velo = new Bike(this,new Vector(4.0f,5.0f));
		
		this.setViewCandidate(velo);
		addActor(velo);
		
		addActor(new Finish(this,new Vector(158.0f,-25.0f),"flag.red.png",1.0f,1.0f));
		
		addActor(new Bascule(this,new Vector(35.0f,-5.0f),17.0f,0.5f));
		for(int i=0;i<6;i++) {
			addActor(new Animal(this,new Vector(30.0f,-13.0f), "fish.blue.left.3.png"));
			addActor(new Animal(this,new Vector(40.0f,-13.0f), "fish.green.left.3.png"));
			addActor(new Animal(this,new Vector(50.0f,-13.0f), "fish.pink.left.3.png"));
		}
		
		
		addActor(new Caisse(this,true,new Vector(85.0f,-18.0f), "crate.3.png", 5.0f, 1.0f));
		addActor(new Caisse(this,true,new Vector(95.0f,-20.0f), "crate.3.png", 5.0f, 1.0f));
		addActor(new Caisse(this,true,new Vector(105.0f,-22.0f), "crate.3.png", 5.0f, 1.0f));
		addActor(new Caisse(this,true,new Vector(115.0f,-24.0f), "crate.3.png", 5.0f, 1.0f));
		
		for(int i=0;i<8;i++) {
			addActor(new Animal(this,new Vector(85.0f,-21.0f), "frog.left.leap.png"));
			addActor(new Animal(this,new Vector(95.0f,-26.0f), "frog.left.png"));
			addActor(new Animal(this,new Vector(105.0f,-30.0f), "frog.right.leap.png"));
		}
		
		
		addActor(new Pendule(this,new Vector(90.0f,-8.5f),5.0f));
		addActor(new Pendule(this,new Vector(110.0f,-12.0f),5.0f));
		
		addActor(new Tapis(this,new Vector(126.0f,-28.0f),10.0f,10));
		
		addActor(new Puits(this,new Vector(156.0f,-36.0f),10.0f,30.0f));
		
	}

	public void update(float deltaTime) {
		super.update(deltaTime);
		message.draw(getCanvas());
		if(getKeyboard().get(KeyEvent.VK_R).isReleased()) {
			
			deleteAllActors();
			
			createAllActors();
			
			finish = false;
			message.setText("");
			
		}
	}
	
	
	public void didFall() {
		if(!finish)message.setText("Game over");
	}
	
	public void didWin() {
		message.setText("Well done");
		finish = true;
		velo.setArmsUp(true);
	}
}
