package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;
import java.util.ArrayList;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.math.BasicContactListener;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Puits extends GameEntity implements Actor {
	
	private BasicContactListener contactListener;
	private Emitter emitter;

	public Puits(ActorGame game, Vector vector, float width, float height) throws IllegalArgumentException {
		
		super(game,true,vector);
        
		
		if(width <= 0.0) throw new IllegalArgumentException("width <= 0");
		if(height <= 0.0) throw new IllegalArgumentException("height <= 0");
        
        PartBuilder partBuilder = getEntity().createPartBuilder();
        Polygon polygon = new Polygon( 
        		new Vector(0.0f, 0.0f), 
        		new Vector(width, 0.0f), 
        		new Vector(width, height),
        		new Vector(0.0f, height) 
        		);
        partBuilder.setShape(polygon);
        partBuilder.setGhost(true);
        partBuilder.build();    
        
        contactListener = new BasicContactListener(); 
        getEntity().addContactListener(contactListener);
        
        emitter = new Emitter(getOwner(),this,Vector.ZERO,300,true,2,width,height);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public Transform getTransform() {
		return getEntity().getTransform();
	}

	@Override
	public Vector getVelocity() {
		return getEntity().getVelocity();
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		emitter.draw(canvas);
		
	}
	
	public void destroy() {
		super.destroy();
		getOwner().deleteActor(this);
	}

	
	public void update(float deltaTime) {
		
		emitter.update(deltaTime);
		for(Entity entity: contactListener.getEntities()) {
			entity.applyForce(new Vector(0.0f,20.0f), null);
		}
	}
}
