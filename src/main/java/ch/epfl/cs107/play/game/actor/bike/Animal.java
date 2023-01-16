package ch.epfl.cs107.play.game.actor.bike;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Image;

public class Animal extends GameEntity implements Actor {
	
	private String image;
	
	public Animal(ActorGame game, Vector vector, String image) {
		
		super(game,false,vector);
		
		this.image = image;
                
        PartBuilder partBuilder = getEntity().createPartBuilder();
        Polygon polygon = new Polygon( 
        		new Vector(0.0f, 0.0f), 
        		new Vector(2.0f, 0.0f), 
        		new Vector(2.0f, 2.0f),
        		new Vector(0.0f, 2.0f) 
        		);
        partBuilder.setShape(polygon);
        partBuilder.setGhost(false);
        partBuilder.build();
		
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
        Transform transform = Transform.I.translated(0.0f,0.0f).scaled(2.0f, 2.0f).transformed(getTransform());
        canvas.drawImage(canvas.getImage(image), transform, 1.0f, 2.0f);
		
	}
	
	public void destroy() {
		super.destroy();
		getOwner().deleteActor(this);
	}
	
	public void update(float deltaTime) {
		
		int random = (int)(Math.random()*100.0);
		if(random==0)getEntity().applyForce(new Vector(0.0f, 1200.0f),null);
		
	}

}
