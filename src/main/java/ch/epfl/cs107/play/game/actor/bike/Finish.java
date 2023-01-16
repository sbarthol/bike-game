package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.math.BasicContactListener;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Contact;
import ch.epfl.cs107.play.math.ContactListener;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Finish extends GameEntity implements Actor {
	
	private ImageGraphics graphics;
	private boolean finish;

	public Finish(BikeGame game, Vector vector, String image, float width, float height) throws IllegalArgumentException {
		
		super(game,true,vector);
		
		if(width <= 0.0) throw new IllegalArgumentException("width <= 0");
		if(height <= 0.0) throw new IllegalArgumentException("height <= 0");
		
		finish = false;
        
        graphics = new ImageGraphics(image, width, height); 
        graphics.setParent(getEntity());
        
        PartBuilder partBuilder = getEntity().createPartBuilder();
        Circle circle = new Circle(2f);
        partBuilder.setShape(circle);
        partBuilder.setGhost(true);
        partBuilder.setCollisionGroup(BikeGame.CATEGORY_FINISH);
        partBuilder.build();
        
        ContactListener listener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				
				if(contact.getOther().getCollisionGroup() == BikeGame.CATEGORY_BIKE) {
					finish = true;
				}
				 	
			}
			@Override
			public void endContact(Contact contact) {}
		};
		
		getEntity().addContactListener(listener);
        
		// TODO Auto-generated constructor stub
	}
	
	public void update(float deltaTime) {
		
		if(finish) {
			BikeGame bikeGame = (BikeGame)getOwner();
			bikeGame.didWin();
			
			destroy();
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
		
	}
	
	public void destroy() {
		super.destroy();
		getOwner().deleteActor(this);
	}

}
