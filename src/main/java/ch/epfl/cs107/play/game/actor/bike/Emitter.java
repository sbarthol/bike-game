package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;
import java.util.ArrayList;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Emitter extends GameEntity implements Actor {
	
	private int max,type;
	private float spawnWidth,spawnHeight;
	private Vector position; // position relative
	private GameEntity vehicle;
	private boolean isOn;
	private ArrayList<Particle> particles;
	
	public Emitter(ActorGame game, GameEntity vehicle, Vector position,int max,boolean isOn,int type,float spawnWidth,float spawnHeight) {
		
		super(game, false, position);
		
		if(spawnWidth < 0.0) throw new IllegalArgumentException("spawnWidth < 0");
		if(spawnHeight < 0.0) throw new IllegalArgumentException("spawnHeight < 0");
		
		this.max = max;
		this.type = type;
		this.spawnWidth = spawnWidth;
		this.spawnHeight = spawnHeight;
		this.position = position;
		this.vehicle = vehicle;
		this.isOn = isOn;
		particles = new ArrayList<Particle>();
		
	}
	
	public Emitter(ActorGame game, GameEntity vehicle, Vector position,int max,int type) {
		this(game,vehicle,position,max,false,type,0.0f,0.0f);
	}
	
	public void setOn() {
		isOn = true;
	}
	public void setOff() {
		isOn = false;
	}

	@Override
	public void draw(Canvas canvas) {
		for(Particle particle:particles) {
			particle.draw(canvas);
		}		
	}
	
	public void update(float deltaTime) {
		
		for(int i=0;i<particles.size();i++) {
			if(particles.get(i).isDead()) {
				particles.remove(i);
			}
		}
		
		for(Particle particle:particles) {
			particle.update(deltaTime);
		}
		
		if(!isOn)return;
		
		Vector p = new Vector(vehicle.getEntity().getPosition().x+position.x+(float)Math.random()*spawnWidth,vehicle.getEntity().getPosition().y+position.y+(float)Math.random()*spawnHeight); // position absolue
		
		while(particles.size()<max) {
			
			if(type == 1) {
				
				Vector velocity = new Vector((float)(Math.random())*4.0f-2,(float)(Math.random()));
				Vector acceleration = Vector.ZERO;
				
				float angularPosition = (float)(Math.random()*2.0*Math.PI);
				float angularVelocity = 2.0f;
				float angularAcceleration = 0.0f;
				
				float size = 0.7f;
				float lifeTime = (float)Math.random()*2.0f;
				
				particles.add(new ImageParticle(p,velocity,acceleration,angularPosition,angularVelocity,angularAcceleration,size,lifeTime,"smoke.gray.1.png"));;
				
			}else if (type == 2) {
				
				Vector velocity = new Vector(0.0f,1.0f);
				Vector acceleration = Vector.ZERO;
				
				float size = 0.5f;
				float growthSpeed = 0.01f;
				float lifeTime = (float)Math.random()*2.0f;
				
				particles.add(new ShapeParticle(p,velocity,acceleration,size,growthSpeed,lifeTime,new Circle(size/2.0f),Color.MAGENTA));;
			}
		}
		
    }

	@Override
	public Transform getTransform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

}
