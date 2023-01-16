package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import ch.epfl.cs107.play.math.Shape;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ShapeParticle extends Particle {
	
	private Shape shape;
	private Color fillColor;
	
	public ShapeParticle(Vector position, Vector velocity, Vector acceleration, float angularPosition,
			float angularVelocity, float angularAcceleration, float size, float growthSpeed, float lifeTime, Shape shape, Color fillColor)   {
		
		super(position, velocity, acceleration, angularPosition, angularVelocity, angularAcceleration, size, growthSpeed, lifeTime);
				
		this.shape = shape;
		this.fillColor = fillColor;
	}

	public ShapeParticle(Vector position, Vector velocity, Vector acceleration, float angularPosition,
			float angularVelocity, float angularAcceleration, float size, float lifeTime, Shape shape, Color fillColor)   {
		
		super(position, velocity, acceleration, angularPosition, angularVelocity, angularAcceleration, size, lifeTime);
				
		this.shape = shape;
		this.fillColor = fillColor;
	}
	
	public ShapeParticle(Vector position, Vector velocity, Vector acceleration, float size, float growthSpeed, float lifeTime, Shape shape, Color fillColor) {
		
		super(position, velocity, acceleration, size, growthSpeed, lifeTime);
		
		this.shape = shape;
		this.fillColor = fillColor;
	}


	@Override
	public void draw(Canvas canvas) {
		canvas.drawShape(shape, getTransform(), fillColor, null, 0.0f, getAlpha(), 0.0f);		
	}

	

}
