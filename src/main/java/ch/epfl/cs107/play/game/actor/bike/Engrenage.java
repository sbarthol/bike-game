package ch.epfl.cs107.play.game.actor.bike;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RevoluteConstraint;
import ch.epfl.cs107.play.math.RevoluteConstraintBuilder;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Engrenage extends GameEntity implements Actor {
	
	private ImageGraphics graphics;
	private Entity block;
	private RevoluteConstraint constraint;
	private float speed;

	public Engrenage(ActorGame game, Vector vector, float speed, float radius) throws IllegalArgumentException {
		
		super(game,false,vector);
		
		if(radius <= 0.0) throw new IllegalArgumentException("radius <= 0");
		
		this.speed = speed;
		
        graphics = new ImageGraphics("spinner.1.png", 2*radius, 2*radius, new Vector(0.5f,0.5f)); 
        graphics.setParent(getEntity());
        
        PartBuilder partBuilder = getEntity().createPartBuilder();
        Circle circle = new Circle(radius);
        partBuilder.setShape(circle);
        partBuilder.setFriction(1000.0f);
        partBuilder.build();
        
        // create block
        
        EntityBuilder builder = getOwner().createBuilder();
        builder.setFixed(true);
        builder.setPosition(new Vector(vector.x,vector.y));
        
        block = builder.build();
        
        // constraint
        
        RevoluteConstraintBuilder revoluteConstraintBuilder = getOwner().createRevoluteConstraintBuilder();
        revoluteConstraintBuilder.setFirstEntity(getEntity());
        revoluteConstraintBuilder.setFirstAnchor(Vector.ZERO);
        revoluteConstraintBuilder.setSecondEntity(block);
        revoluteConstraintBuilder.setSecondAnchor(Vector.ZERO);
        revoluteConstraintBuilder.setInternalCollision(false);
        revoluteConstraintBuilder.setMotorMaxTorque(10.0f); 
        
        constraint = revoluteConstraintBuilder.build();
        constraint.setMotorEnabled(true);
        
        setOff();
       
		
		// TODO Auto-generated constructor stub
	}
	
	public void setOn() {
		constraint.setMotorSpeed(speed);
	}
	
	public void setOff() {
		constraint.setMotorSpeed(0.0f);
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
	}

}
