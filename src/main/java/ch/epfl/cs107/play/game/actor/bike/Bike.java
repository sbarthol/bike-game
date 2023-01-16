package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import com.sun.glass.events.KeyEvent;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Contact;
import ch.epfl.cs107.play.math.ContactListener;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Bike extends GameEntity implements Actor {
	
	private static final float MAX_WHEEL_SPEED = 20.0f;
	private ShapeGraphics hitboxGraphics;
	private Wheel leftWheel,rightWheel;
	private boolean looksForward,hit,armsUp;

	

	public Bike(BikeGame game, Vector position) {
		super(game, false, position);
		looksForward = true;
		hit = false;
		armsUp = false;
		
		// create hitbox
		
		PartBuilder partBuilder = getEntity().createPartBuilder();
        Polygon polygon = new Polygon( 
        		0.0f, 0.5f,
			0.5f, 1.0f, 
			0.0f, 2.0f, 
			-0.5f, 1.0f
		);
        partBuilder.setShape(polygon);
        partBuilder.setGhost(true);
        partBuilder.setCollisionGroup(BikeGame.CATEGORY_BIKE);
        partBuilder.build();
		
		hitboxGraphics = new ShapeGraphics(polygon,Color.ORANGE,null,0.0f, 0.5f, -1.0f);
		hitboxGraphics.setParent(getEntity());
		
		ContactListener listener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {

				if (contact.getOther().isGhost())
					return ;
				if(contact.getOther().getCollisionGroup() == BikeGame.CATEGORY_BIKE) {
					return;
				}				
				 
				hit = true;
			}
			@Override
			public void endContact(Contact contact) {}
		};
		
		getEntity().addContactListener(listener);
		
		// create wheels
		
		leftWheel = new Wheel(getOwner(),new Vector(position.x-1.0f,position.y),0.5f,getEntity());
		leftWheel.attach(getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		
		rightWheel = new Wheel(getOwner(),new Vector(position.x+1.0f,position.y),0.5f,getEntity());
		rightWheel.attach(getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));
		
        
	}

	public GameEntity getLeftWheel() {
		return leftWheel;
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
		hitboxGraphics.draw(canvas);
		leftWheel.draw(canvas);
		rightWheel.draw(canvas);
		
		
		
		drawHead(canvas);
		drawLeftArm(canvas);
		drawRightArm(canvas);
		drawBody(canvas);
		drawLeftLeg(canvas);
		drawRightLeg(canvas);
	}
	
	public void destroy() {
		super.destroy();
		leftWheel.destroy();
		rightWheel.destroy();
		getOwner().deleteActor(this);
	}
	
	public void setArmsUp(boolean armsUp) {
		this.armsUp = armsUp;
	}
	
	public void update(float deltaTime) {
		
		if(hit) {
			
			// game over
			
			BikeGame bikeGame = (BikeGame)getOwner();
			bikeGame.didFall();
			
			destroy();
			
			return;
		}
		
		leftWheel.update(deltaTime);
		rightWheel.update(deltaTime);
		
		leftWheel.relax();
		rightWheel.relax();
		
		if(getOwner().getKeyboard().get(KeyEvent.VK_SPACE).isReleased()) {
			looksForward=!looksForward;
		}if(getOwner().getKeyboard().get(KeyEvent.VK_DOWN).isDown()) {
			leftWheel.power(0.0f);
			rightWheel.power(0.0f);
		}if(getOwner().getKeyboard().get(KeyEvent.VK_UP).isDown()) {
			if(looksForward) {
				if(leftWheel.getSpeed()<MAX_WHEEL_SPEED) {
					leftWheel.power(-MAX_WHEEL_SPEED);
				}
			}else {
				if(rightWheel.getSpeed()>-MAX_WHEEL_SPEED) {
					rightWheel.power(MAX_WHEEL_SPEED);
				}
			}
		}if(getOwner().getKeyboard().get(KeyEvent.VK_LEFT).isDown()) {
			getEntity().applyAngularForce(10.0f);
		}
		if(getOwner().getKeyboard().get(KeyEvent.VK_RIGHT).isDown()) {
			getEntity().applyAngularForce(-10.0f);
		}
	}
	
	private void drawHead(Canvas canvas) {
		Circle head = new Circle(0.2f, getHeadLocation());
		ShapeGraphics graphics = new ShapeGraphics(head, Color.BLACK, Color.BLACK, .1f, 1.f, 0);
        graphics.setParent(getEntity());
        graphics.draw(canvas);
	}
	
	private void drawLeftArm(Canvas canvas) {
		
		Polyline arm = new Polyline(
				getLeftHandLocation(),
				getNeckLocation()
				);
		
		ShapeGraphics graphics = new ShapeGraphics(arm, Color.BLACK, Color.BLACK, .1f, 1.f, 0);
        graphics.setParent(getEntity());
        graphics.draw(canvas);
		
	}
	
	private void drawRightArm(Canvas canvas) {
		
		Polyline arm = new Polyline(
				getRightHandLocation(),
				getNeckLocation()
				);
		
		ShapeGraphics graphics = new ShapeGraphics(arm, Color.BLACK, Color.BLACK, .1f, 1.f, 0);
        graphics.setParent(getEntity());
        graphics.draw(canvas);
		
	}
	
	private void drawBody(Canvas canvas) {
		
		Polyline body = new Polyline(
				getNeckLocation(),
				getButtLocation()
				);
		
		ShapeGraphics graphics = new ShapeGraphics(body, Color.BLACK, Color.BLACK, .1f, 1.f, 0);
        graphics.setParent(getEntity());
        graphics.draw(canvas);
		
	}
	
	private void drawRightLeg(Canvas canvas) {
		
		Polyline rightLeg = new Polyline(
				getButtLocation(),
				getRightKneeLocation(),
				getRightFootLocation(),
				getRightKneeLocation()
				);
		
		ShapeGraphics graphics = new ShapeGraphics(rightLeg, Color.BLACK, Color.BLACK, .1f, 1.f, 0);
        graphics.setParent(getEntity());
        graphics.draw(canvas);
		
	}
	
	private void drawLeftLeg(Canvas canvas) {
		
		Polyline leftLeg = new Polyline(
				getButtLocation(),
				getLeftKneeLocation(),
				getLeftFootLocation(),
				getLeftKneeLocation()
				);
		
		ShapeGraphics graphics = new ShapeGraphics(leftLeg, Color.BLACK, Color.BLACK, .1f, 1.f, 0);
        graphics.setParent(getEntity());
        graphics.draw(canvas);
		
	}
	
	
	
	private Vector getHeadLocation() {
		if(armsUp) {
			if(looksForward) {
				return new Vector(-0.4f,1.8f);
			}else {
				return new Vector(0.4f,1.8f);
			}
		}
		return new Vector(0.0f,1.75f);
	}
	
	private Vector getLeftHandLocation() {
		
		if(armsUp) {
			if(looksForward)return new Vector(-1.0f,2.0f);
			else return new Vector(-0.2f,2.0f);
			
		}else {
			if(looksForward)return new Vector(0.5f,1.0f);
			return new Vector(-0.5f,1.0f);
		}
		
	}
	
	private Vector getRightHandLocation() {
		
		if(armsUp) {
			if(looksForward)return new Vector(0.2f,2.0f);
			else return new Vector(1.0f,2.0f);
			
		}else {
			if(looksForward)return new Vector(0.5f,1.0f);
			return new Vector(-0.5f,1.0f);
		}
		
	}
	
	private Vector getNeckLocation() {
		if(armsUp) {
			if(looksForward)return new Vector(-0.4f,1.4f);
			return new Vector(0.4f,1.4f);
		}else {
			if(looksForward)return new Vector(-0.2f,1.5f);
			return new Vector(0.2f,1.5f);
		}
	}
	
	private Vector getButtLocation() {
		if(looksForward)return new Vector(-0.4f,0.8f);
		return new Vector(0.4f,0.8f);
	}
	
	private Vector getLeftKneeLocation() {
		if(looksForward)return circle2PtsRad(getButtLocation(),getLeftFootLocation(),0.7f);
		else return circle2PtsRad(getLeftFootLocation(),getButtLocation(),0.7f);
	}
	
	private Vector getRightKneeLocation() {
		if(looksForward)return circle2PtsRad(getButtLocation(),getRightFootLocation(),0.7f);
		else return circle2PtsRad(getRightFootLocation(),getButtLocation(),0.7f);
	}
	
	private Vector getLeftFootLocation() {
		
		Wheel motor;
		if(looksForward) motor = leftWheel;
		else motor = rightWheel;
		
		double a = motor.getEntity().getAngularPosition();
		if(!looksForward)a=-a;
		
		float x = (float) Math.cos(a+1.57)*motor.getRadius()*0.4f-0.25f;
		float y = (float) Math.sin(a+1.57)*motor.getRadius()*0.4f;
		
		if(!looksForward)x=-x;
		return new Vector(x,y);
	}
	
	private Vector getRightFootLocation() {
		
		Wheel motor;
		if(looksForward) motor = leftWheel;
		else motor = rightWheel;
		
		double a = motor.getEntity().getAngularPosition();
		if(!looksForward)a=-a;
		
		float x = (float) Math.cos(a)*motor.getRadius()*0.4f+0.25f;
		float y = (float) Math.sin(a)*motor.getRadius()*0.4f;
		
		if(!looksForward)x=-x;
		return new Vector(x,y);
	}
	
	private Vector circle2PtsRad(Vector p1, Vector p2, double r) { 
		double d2 = (p1.x - p2.x) * (p1.x - p2.x) +
			(p1.y - p2.y) * (p1.y - p2.y); 
		double det = r * r / d2 - 0.25;
		if (det < 0.0) return Vector.ZERO;
		double h = Math.sqrt(det);
			
			return new Vector((float)((p1.x + p2.x) * 0.5 + (p1.y - p2.y) * h),(float)((p1.y + p2.y) * 0.5 + (p2.x - p1.x) * h));
		}

}
