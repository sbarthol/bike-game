package ch.epfl.cs107.play.game.actor;

import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.Vector;

public abstract class GameEntity {

	private Entity entity;
	private ActorGame actorGame;
	
	public ActorGame getOwner() {
		return actorGame;
	}
	
	public Entity getEntity(){
		return entity;
	}
	
	public GameEntity(ActorGame game,boolean fixed,Vector position) throws NullPointerException {
		
		actorGame = game;
		
		if(game == null) throw new NullPointerException("ActorGame vaut null");
		if(position == null) throw new NullPointerException("position vaut null");
		
		EntityBuilder entityBuilder = actorGame.createBuilder();
		entityBuilder.setFixed(fixed);
		entityBuilder.setPosition(position);
		entity = entityBuilder.build();
	}
	
	public GameEntity(ActorGame game,boolean fixed) {
		
		this(game,fixed,Vector.ZERO);
	}
	
	
	public void destroy() {
		
		entity.destroy();
		
	}

}
