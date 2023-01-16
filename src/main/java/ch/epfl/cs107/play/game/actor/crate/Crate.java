package ch.epfl.cs107.play.game.actor.crate;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Crate extends GameEntity implements Actor {
	
	private ImageGraphics graphics;

	public Crate(ActorGame game, boolean fixed, Vector vector, String image, float width, float height) throws IllegalArgumentException {
		
		super(game,fixed,vector);
		
		if(width <= 0.0) throw new IllegalArgumentException("width <= 0");
		if(height <= 0.0) throw new IllegalArgumentException("height <= 0");
        
        graphics = new ImageGraphics(image, width, height); 
        graphics.setParent(getEntity());
        
        PartBuilder partBuilder = getEntity().createPartBuilder();
        Polygon polygon = new Polygon( 
        		new Vector(0.0f, 0.0f), 
        		new Vector(width, 0.0f), 
        		new Vector(width, height),
        		new Vector(0.0f, height) 
        		);
        partBuilder.setShape(polygon);
        partBuilder.build();
		
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
		graphics.draw(canvas);
		
	}
	
	public void destroy() {
		super.destroy();
		getOwner().deleteActor(this);
	}

}
