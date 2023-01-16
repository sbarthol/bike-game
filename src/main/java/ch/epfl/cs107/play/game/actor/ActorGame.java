package ch.epfl.cs107.play.game.actor;

import java.util.ArrayList;

import ch.epfl.cs107.play.game.Game;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.math.RevoluteConstraintBuilder;
import ch.epfl.cs107.play.math.RopeConstraintBuilder;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.WheelConstraintBuilder;
import ch.epfl.cs107.play.math.World;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public abstract class ActorGame implements Game {

	private ArrayList<Actor> actors;
	private World world;
	private Window window;
	private FileSystem fileSystem;
	
	// Viewport properties
	private Vector viewCenter;
	private Vector viewTarget;
	private Positionable viewCandidate;
	private static final float VIEW_TARGET_VELOCITY_COMPENSATION = 0.2f;
	private static final float VIEW_INTERPOLATION_RATIO_PER_SECOND = 0.1f;
	private static final float VIEW_SCALE = 10.0f;
	
	
	public void addActor(Actor actor) {
		actors.add(actor);
	}
	
	public void deleteActor(Actor actor) {
		
		actors.remove(actor);
	}
	
	public void deleteAllActors() {
		while(actors.size()>0) {
			actors.get(0).destroy();
		}
	}
	
	
	public Positionable getViewCandidate() {
		return this.viewCandidate;
	}
	
	public void setViewCandidate(Positionable viewCandidate) {
		this.viewCandidate = viewCandidate;
	}
	
	public Keyboard getKeyboard(){ 
		return window.getKeyboard();
	}
	
	public Canvas getCanvas(){ 
		return window;
	}
	
	public WheelConstraintBuilder createWheelConstraintBuilder() {
		return world.createWheelConstraintBuilder();
	}
	
	public RopeConstraintBuilder createRopeConstraintBuilder() {
		return world.createRopeConstraintBuilder();
	}
	
	public RevoluteConstraintBuilder createRevoluteConstraintBuilder() {
		return world.createRevoluteConstraintBuilder();
	}
	
	public EntityBuilder createBuilder() {
		return world.createEntityBuilder();
	}
	
	public boolean begin(Window window, FileSystem fileSystem) {
		
		// Store context
        this.window = window;
        this.fileSystem = fileSystem;
        
        viewCenter = Vector.ZERO;
        viewTarget = Vector.ZERO;
     
        actors = new ArrayList<Actor>();
        
        world = new World();
        world.setGravity(new Vector(0.0f, -9.81f));
        
        return true;
	}
    
    /**
     * Simulates a single time step.
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
	
    public void update(float deltaTime) {
    	
    		world.update(deltaTime);
    		
    		for(int i=0;i<actors.size();i++) {
    			actors.get(i).update(deltaTime);
    		}
    		
    		// camera position
    		
    		// Update expected viewport center
    		if (viewCandidate != null) { 
    			viewTarget = viewCandidate.getPosition().add(viewCandidate.getVelocity() .mul(VIEW_TARGET_VELOCITY_COMPENSATION));
    		}
    		
    		// Interpolate with previous location
    		float ratio = (float)Math.pow(VIEW_INTERPOLATION_RATIO_PER_SECOND, deltaTime);
    		viewCenter = viewCenter.mixed(viewTarget,1.0f- ratio);
    		
    		// Compute new viewport
    		Transform viewTransform = Transform.I.scaled(VIEW_SCALE).translated(viewCenter);
    		window.setRelativeTransform(viewTransform);
    		
    		for(int i=0;i<actors.size();i++) {
    			actors.get(i).draw(window);
    		}
    }
    
    /** Cleans up things, called even if initialisation failed. */
    public void end() {
    	
    		
    }
}
