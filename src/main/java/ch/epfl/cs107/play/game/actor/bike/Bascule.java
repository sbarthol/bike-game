package ch.epfl.cs107.play.game.actor.bike;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RevoluteConstraintBuilder;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Bascule extends GameEntity implements Actor {
	
	private ImageGraphics graphics;

	public Bascule(ActorGame game, Vector vector,float width, float height) throws IllegalArgumentException {
		
		super(game,false,vector);
		
		if(width <= 0.0) throw new IllegalArgumentException("width <= 0");
		if(height <= 0.0) throw new IllegalArgumentException("height <= 0");
        
        graphics = new ImageGraphics("wood.cracked.4.png", width, height, new Vector(width,height)); 
        graphics.setParent(getEntity());
        
        PartBuilder partBuilder = getEntity().createPartBuilder();
        Polygon polygon = new Polygon( 
        		new Vector(-width/2.0f, -height/2.0f), 
        		new Vector(width/2.0f, -height/2.0f), 
        		new Vector(width/2.0f, height/2.0f),
        		new Vector(-width/2.0f, height/2.0f) 
        		);
        partBuilder.setShape(polygon);
        partBuilder.setFriction(20.0f);
        partBuilder.build();
        
        EntityBuilder entityBuilder = getOwner().createBuilder();
        entityBuilder.setFixed(true);
        entityBuilder.setPosition(vector);
        Entity block = entityBuilder.build();
        
        partBuilder = block.createPartBuilder();
        polygon = new Polygon( 
        		new Vector(-1.0f, -1.0f), 
        		new Vector(1.0f, -1.0f), 
        		new Vector(1.0f, 1.0f),
        		new Vector(-1.0f, 1.0f) 
        		);
        
        partBuilder.setShape(polygon);
        partBuilder.setGhost(true);
        partBuilder.build();
        
        // now the pivot
        
        RevoluteConstraintBuilder revoluteConstraintBuilder =
        		getOwner().createRevoluteConstraintBuilder();
        revoluteConstraintBuilder.setFirstEntity(block);
        revoluteConstraintBuilder.setFirstAnchor(new Vector(0.0f,2.0f));
        revoluteConstraintBuilder.setSecondEntity(getEntity());
        revoluteConstraintBuilder.setSecondAnchor(Vector.ZERO);
        revoluteConstraintBuilder.setInternalCollision(false);
        revoluteConstraintBuilder.build();
		
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
