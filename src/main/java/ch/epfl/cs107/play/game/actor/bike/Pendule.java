package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RevoluteConstraintBuilder;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Pendule extends GameEntity implements Actor {
	
	private Entity ball,rope;
	private ImageGraphics blockGraphics,ballGraphics;
	private ShapeGraphics ropeGraphics;
	

	public Pendule(ActorGame game, Vector vector, float ropeLength) throws IllegalArgumentException {
		
		// block = entity
		
		super(game,true,vector);
		
		if(ropeLength <= 0.0) throw new IllegalArgumentException("length <= 0");
        
		blockGraphics = new ImageGraphics("explosive.debris.3.png", 1.0f, 1.0f, new Vector(0.5f,0.5f)); 
		blockGraphics.setParent(getEntity());
        
        PartBuilder partBuilder = getEntity().createPartBuilder();
        Polygon polygon = new Polygon( 
        		new Vector(-0.5f, -0.5f), 
        		new Vector(0.0f, -0.5f), 
        		new Vector(0.5f, 0.5f),
        		new Vector(-0.5f, 0.5f) 
        		);
        partBuilder.setShape(polygon);
        partBuilder.build();
        
        // ball
        
        EntityBuilder builder = getOwner().createBuilder();
        builder.setPosition(new Vector(vector.x,vector.y-ropeLength));
        builder.setFixed(false);
        
        ball = builder.build();
        
        partBuilder = ball.createPartBuilder(); 
        Circle circle = new Circle(1.0f);
        partBuilder.setShape(circle);
        partBuilder.setGhost(false);
        partBuilder.build();
        
        ballGraphics = new ImageGraphics("coin.diamond.png", 2.0f, 2.0f, new Vector(0.5f,0.5f)); 
        ballGraphics.setParent(ball);
        
        // rope
        
        builder.setPosition(new Vector(vector.x,vector.y-ropeLength/2.0f));
        
        rope = builder.build();
		
        partBuilder = rope.createPartBuilder(); 
        polygon = new Polygon( 
        		new Vector(-0.001f, -ropeLength/2.0f), 
        		new Vector(0.001f, -ropeLength/2.0f), 
        		new Vector(0.001f, ropeLength/2.0f),
        		new Vector(-0.001f, ropeLength/2.0f) 
        		);
        partBuilder.setShape(polygon);
        partBuilder.setGhost(true);
        partBuilder.build();
        
        ropeGraphics = new ShapeGraphics(polygon, Color.ORANGE, Color.ORANGE, .1f, 1.f, -1.0f);
        ropeGraphics.setParent(rope);
        
        // constraints
        
        RevoluteConstraintBuilder revoluteConstraintBuilder = getOwner().createRevoluteConstraintBuilder();
        revoluteConstraintBuilder.setFirstEntity(getEntity());
        revoluteConstraintBuilder.setFirstAnchor(Vector.ZERO);
        revoluteConstraintBuilder.setSecondEntity(rope);
        revoluteConstraintBuilder.setSecondAnchor(new Vector(0.0f,
        		ropeLength/2.0f));
        revoluteConstraintBuilder.setInternalCollision(false);
        revoluteConstraintBuilder.build();
        
        revoluteConstraintBuilder.setFirstEntity(ball);
        revoluteConstraintBuilder.setFirstAnchor(Vector.ZERO);
        revoluteConstraintBuilder.setSecondEntity(rope);
        revoluteConstraintBuilder.setSecondAnchor(new Vector(0.0f,
        		-ropeLength/2.0f));
        revoluteConstraintBuilder.setInternalCollision(false);
        revoluteConstraintBuilder.build();
        
        // give a little push
        
        ball.applyImpulse(new Vector(40.0f,0.0f), null);
        
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
		blockGraphics.draw(canvas);
		ballGraphics.draw(canvas);
		ropeGraphics.draw(canvas);
		
	}
	
	public void destroy() {
		super.destroy();
		getOwner().deleteActor(this);
	}

}
