package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;


import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

abstract public class Particle implements Positionable, Graphics{
	
	private Vector position ; // dans le repere absolu
	private Vector velocity ;
	private Vector acceleration ;
	
	private float angularPosition;
	private float angularVelocity; 
	private float angularAcceleration;
	
	private float size;
	private float growthSpeed;
	private float lifeTime; // in seconds
	private float age;
	private float alpha;
	
	public Particle(Vector position, Vector velocity, Vector acceleration, float angularPosition, float angularVelocity, float angularAcceleration, float size, float growthSpeed, float lifeTime)
	throws IllegalArgumentException
	{
		
		if(size <= 0.0) throw new IllegalArgumentException("size <= 0");
		
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
		
		this.angularPosition = angularPosition;
		this.angularVelocity = angularVelocity;
		this.angularAcceleration = angularAcceleration;
		
		this.size = size;
		this.growthSpeed = growthSpeed;
		this.lifeTime = lifeTime;
		this.age = 0.0f;
		this.alpha = 1.0f;
	}
	
	public Particle(Vector position, Vector velocity, Vector acceleration, float angularPosition, float angularVelocity, float angularAcceleration, float size, float lifeTime){
				
		this(position,velocity,acceleration,angularPosition,angularVelocity,angularAcceleration,size,0.01f,lifeTime);
	}
	
	public Particle(Vector position, Vector velocity, Vector acceleration, float size, float lifeTime){
		
		this(position,velocity,acceleration,0.0f,0.0f,0.0f,size,0.01f,lifeTime);
	}
	
	public Particle(Vector position, Vector velocity, Vector acceleration, float size, float growthSpeed, float lifeTime){
		
		this(position,velocity,acceleration,0.0f,0.0f,0.0f,size,growthSpeed,lifeTime);
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public float getSize() {
		return size;
	}
	
	public boolean isDead() {
		
		return age>=lifeTime;
	}
	
	@Override
	public abstract void draw(Canvas canvas);
	
	@Override
	public Vector getVelocity() {
		return velocity;
	}
	
	@Override
	public Transform getTransform() {
		return Transform.I.rotated(angularPosition).scaled(size).translated(new Vector(position.x,position.y));
	}
	
	public void update(float deltaTime) {
		
		age += deltaTime;
		size+=growthSpeed;
		
		alpha = (float)(1.0f-(age/(float)lifeTime));
		
		velocity = velocity.add(acceleration.mul(deltaTime)) ;
		position = position.add(velocity.mul(deltaTime)) ;
		
		angularVelocity += angularAcceleration * deltaTime ;
		angularPosition += angularVelocity * deltaTime ;
    }
	
}