package ch.epfl.cs107.play.game.actor.bike;

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
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.WheelConstraint;
import ch.epfl.cs107.play.math.WheelConstraintBuilder;
import ch.epfl.cs107.play.window.Canvas;

public class Wheel extends GameEntity implements Actor {

	private float radius;
	private Vector position;
	private ImageGraphics graphics;
	private ActorGame game;
	private WheelConstraint constraint;
	private Emitter emitter;
	private Entity vehicle,floor;
	private boolean isTouchingGround;
	
	public float getRadius() {
		return radius;
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public void draw(Canvas canvas) {
		graphics.draw(canvas);
		emitter.draw(canvas);
	}
	
	public void power(float speed) {
		
		constraint.setMotorEnabled(true);
		constraint.setMotorSpeed(speed);
		
	}
	
	public void detach() {
		
		constraint.destroy();
	}
	
	public void destroy() {
		super.destroy();
		this.detach();
}
	
	/**
	@return relative rotation speed, in radians per second */
	public float getSpeed() {
		return constraint.getMotorSpeed()-vehicle.getAngularVelocity();
	}
	
	public void relax() {
		
		constraint.setMotorEnabled(false);
	
	}
	
	public void attach(Entity vehicle, Vector anchor, Vector axis) {
		
		WheelConstraintBuilder constraintBuilder = game.createWheelConstraintBuilder();
		constraintBuilder.setFirstEntity(vehicle);
		// point d'ancrage du véhicule :
		constraintBuilder.setFirstAnchor(anchor);
		// Entity associée à la roue :
		constraintBuilder.setSecondEntity(getEntity());
		// point d'ancrage de la roue (son centre):
		constraintBuilder.setSecondAnchor(Vector.ZERO);
		// axe le long duquel la roue peut se déplacer :
		constraintBuilder.setAxis(axis);
		// fréquence du ressort associé
		constraintBuilder.setFrequency(3.0f); 
		constraintBuilder.setDamping(0.5f);
		// force angulaire maximale pouvant être appliquée 
		//à la roue pour la faire tourner :
		constraintBuilder.setMotorMaxTorque(10.0f); 
		
		constraint=constraintBuilder.build();
	}

	public Wheel(ActorGame game, Vector position, float radius, Entity vehicle) throws IllegalArgumentException, NullPointerException {
		super(game, false, position);
		
		if(radius <= 0.0) throw new IllegalArgumentException("width <= 0");
		if(vehicle == null) throw new NullPointerException("vehicle vaut null");
		
		this.position = position;
		this.radius = radius;
		this.game = game;
		this.vehicle = vehicle;
		floor = vehicle;
		isTouchingGround = false;
		
		
		Circle circle = new Circle(radius);
		graphics = new ImageGraphics("explosive.11.png", 2*radius, 2*radius, new Vector(radius,radius)); 
        graphics.setParent(getEntity());
        
        PartBuilder partBuilder = getEntity().createPartBuilder();
        partBuilder.setShape(circle);
        partBuilder.setCollisionGroup(BikeGame.CATEGORY_BIKE);
        partBuilder.build();
        
        emitter = new Emitter(getOwner(),this,new Vector(0.0f,-radius),10,1);
        
        ContactListener listener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				
				if(contact.getOther().getCollisionGroup() == BikeGame.CATEGORY_FLOOR) {
					isTouchingGround = true;
					floor = contact.getOther().getEntity();
				}
			}
			@Override
			public void endContact(Contact contact) {
				
				if(contact.getOther().getCollisionGroup() == BikeGame.CATEGORY_FLOOR) {
					isTouchingGround = false;
				}
			}
		};
		
		getEntity().addContactListener(listener);
	}
	
	public void update(float deltaTime) {
		
		if(constraint.getMotorSpeed()==0.0&&constraint.isMotorEnabled()&&Math.abs(vehicle.getVelocity().x-floor.getVelocity().x)>2.0&&isTouchingGround)
			emitter.setOn();
		else
			emitter.setOff();
		
		emitter.update(deltaTime);
	}

	@Override
	public Transform getTransform() {
		
		return getEntity().getTransform();
	}

	@Override
	public Vector getVelocity() {
		
		return getEntity().getVelocity();
	}

	
}
