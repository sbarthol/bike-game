package ch.epfl.cs107.play.game.actor.bike;

import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ImageParticle extends Particle {
	
	private String imageName;

	public ImageParticle(Vector position, Vector velocity, Vector acceleration, float angularPosition,
			float angularVelocity, float angularAcceleration, float size, float lifeTime, String imageName)   {
		
		super(position, velocity, acceleration, angularPosition, angularVelocity, angularAcceleration, size, lifeTime);
				
		this.imageName = imageName;
	}
	
	public ImageParticle(Vector position, Vector velocity, Vector acceleration, float size, float lifeTime, String imageName) {
		
		super(position, velocity, acceleration, size, lifeTime);
		this.imageName = imageName;
	}


	@Override
	public void draw(Canvas canvas) {
		
		Transform transform = Transform.I.translated(-0.5f,-0.5f);
		transform = transform.transformed(getTransform());
		
		canvas.drawImage(canvas.getImage(imageName), transform, getAlpha(), 0.0f);
		
	}

	

}
