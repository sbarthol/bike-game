package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;
import java.util.ArrayList;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.math.BasicContactListener;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Contact;
import ch.epfl.cs107.play.math.ContactListener;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RevoluteConstraintBuilder;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Tapis extends GameEntity implements Actor {
	
	private ArrayList<Engrenage> engrenages;
	private ImageGraphics graphics;
	private boolean bikeTouching;

	public Tapis(ActorGame game, Vector vector, float width, int wheels) throws IllegalArgumentException {
		
		// tapis = entity
		
		
		super(game,false,vector);
		
		if(width <= 0.0) throw new IllegalArgumentException("width <= 0");
		
		bikeTouching = false;
        
		graphics = new ImageGraphics("wood.broken.2.png", width, 0.5f); 
		graphics.setParent(getEntity());
        
        PartBuilder partBuilder = getEntity().createPartBuilder();
        Polygon polygon = new Polygon( 
        		new Vector(0.0f, 0.0f), 
        		new Vector(width, 0.0f), 
        		new Vector(width, 0.5f),
        		new Vector(0.0f, 0.5f) 
        		);
        partBuilder.setShape(polygon);
        partBuilder.setFriction(20.0f);
        partBuilder.setCollisionGroup(BikeGame.CATEGORY_FLOOR);
        partBuilder.build();
        
        ContactListener listener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				
				if(contact.getOther().getCollisionGroup() == BikeGame.CATEGORY_BIKE) {
					bikeTouching = true;
				}
			}
			@Override
			public void endContact(Contact contact) {
				
				if(contact.getOther().getCollisionGroup() == BikeGame.CATEGORY_BIKE) {
					bikeTouching = false;
				}
			}
		};
		
		getEntity().addContactListener(listener);
        
        // ball
        
        engrenages = new ArrayList<Engrenage>();
        
        for(int i=0;i<wheels;i++) {
        		engrenages.add(new Engrenage(getOwner(),new Vector(vector.x+i*3.0f,vector.y-1.0f),6.0f,1.0f));
        }
	}
	
	public void setOn() {
		for (Engrenage engrenage: engrenages) {
			engrenage.setOn();
		}
	}
	
	public void setOff() {
		for (Engrenage engrenage: engrenages) {
			engrenage.setOff();
		}
	}
	
	public void update(float deltaTime) {
				
		if(bikeTouching) {
			setOn();
		}else {
			setOff();
		}
		
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
		graphics.draw(canvas);
		for(Engrenage engrenage: engrenages) {
			engrenage.draw(canvas);
		}
		
	}
	
	public void destroy() {
		super.destroy();
		getOwner().deleteActor(this);
		
		for(Engrenage engrenage: engrenages) {
			engrenage.destroy();
		}
	}

}
