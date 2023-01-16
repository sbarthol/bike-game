package ch.epfl.cs107.play.game.actor.bike;



import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.Shape;

public class Terrain extends GameEntity implements Actor {
	
	private Shape shape;

	public Terrain(ActorGame game, Polyline line) throws NullPointerException{
		super(game, true);
		
		if(line == null) throw new NullPointerException("line vaut null");
		
		PartBuilder partBuilder = getEntity().createPartBuilder();
        partBuilder.setShape(line);
        partBuilder.setFriction(20.0f);
        partBuilder.setCollisionGroup(BikeGame.CATEGORY_FLOOR);
        partBuilder.build();
        
        this.shape = partBuilder.getShape();
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

	@Override
	public void draw(Canvas canvas) {
		canvas.drawShape(shape, Transform.I, Color.GRAY, Color.BLACK, 0.1f, 1.0f, 0.0f);
		// TODO Auto-generated method stub
		
	}
	
	public void destroy() {
		super.destroy();
		getOwner().deleteActor(this);
	}

}
