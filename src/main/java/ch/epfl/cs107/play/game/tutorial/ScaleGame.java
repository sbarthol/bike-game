package ch.epfl.cs107.play.game.tutorial;

import com.sun.glass.events.KeyEvent;
import com.sun.prism.paint.Color;

import ch.epfl.cs107.play.game.Game;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RevoluteConstraintBuilder;
import ch.epfl.cs107.play.math.RopeConstraintBuilder;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.World;
import ch.epfl.cs107.play.window.Window;

/**
 * Simple game, to show basic the basic architecture
 */
public class ScaleGame implements Game {

    // Store context
    private Window window;
    
    // We need our physics engine
    private World world;
    
    // And we need to keep references on our game objects
    private Entity block,ball,plank;
    
 // graphical representation of the body
    private ImageGraphics blockGraphics,plankGraphics,ballGraphics;
  
    

    // This event is raised when game has just started
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        
        // Store context
        this.window = window;
        
       // TO BE COMPLETED
        
        
        world = new World();
        world.setGravity(new Vector(0.0f, -9.81f));
        
     // To create an object, you need to use a builder
        EntityBuilder entityBuilder = world.createEntityBuilder();
        
     // Make sure this does not move
        entityBuilder.setFixed(true);
        
     // This helps you define properties, like its initial location
        entityBuilder.setPosition(new Vector(-5.f, -1.f));
        
     // Once ready, the body can be built
        block = entityBuilder.build();
        
        blockGraphics = new ImageGraphics("stone.broken.4.png", 10, 1); 
        blockGraphics.setParent(block);
        
        PartBuilder partBuilder = block.createPartBuilder();
        Polygon polygon = new Polygon( 
        		new Vector(0.0f, 0.0f), 
        		new Vector(10.0f, 0.0f), 
        		new Vector(10.0f, 1.0f),
        		new Vector(0.0f, 1.0f) 
        		);
        
        partBuilder.setShape(polygon);
        partBuilder.setFriction(100.0f);
        partBuilder.build();
        
        // ball cane be built
        
        entityBuilder.setPosition(new Vector(0.5f, 4.0f));
        entityBuilder.setFixed(false);
        
        ball = entityBuilder.build();
        
        Circle circle = new Circle(0.5f);
        
        ballGraphics = new ImageGraphics("explosive.11.png", 1, 1, new Vector(0.5f, 0.5f));
        ballGraphics.setParent(ball);
        
        partBuilder = ball.createPartBuilder();
        
        partBuilder.setShape(circle);
        partBuilder.build();
        
        // now we build the plank
        
        entityBuilder.setPosition(new Vector(-2.5f, 0.8f));
        entityBuilder.setFixed(false);
        
        plank = entityBuilder.build();
        
        plankGraphics = new ImageGraphics("wood.cracked.4.png", 5, 0.2f); 
        plankGraphics.setParent(plank);
        
        partBuilder = plank.createPartBuilder();
        polygon = new Polygon( 
        		new Vector(0.0f, 0.0f), 
        		new Vector(5.0f, 0.0f), 
        		new Vector(5.0f, 0.2f),
        		new Vector(0.0f, 0.2f) 
        		);
        
        partBuilder.setShape(polygon);
        partBuilder.setFriction(100.0f);
        partBuilder.build();
        
        // now the pivot
        
        RevoluteConstraintBuilder revoluteConstraintBuilder =
        		world.createRevoluteConstraintBuilder();
        revoluteConstraintBuilder.setFirstEntity(block);
        revoluteConstraintBuilder.setFirstAnchor(new Vector(blockGraphics.getWidth()/2,
        		(blockGraphics.getHeight()*7)/4));
        revoluteConstraintBuilder.setSecondEntity(plank);
        revoluteConstraintBuilder.setSecondAnchor(new Vector(plankGraphics.getWidth()/2,
        		plankGraphics.getHeight()/2));
        revoluteConstraintBuilder.setInternalCollision(true);
        revoluteConstraintBuilder.build();

        
     // Successfully initiated
        
        return true;
    }

    // This event is called at each frame
    @Override
    public void update(float deltaTime) {
    	
    	if(window.getKeyboard().get(KeyEvent.VK_LEFT).isDown()) {
    		ball.applyAngularForce(10.0f);
    	}else if(window.getKeyboard().get(KeyEvent.VK_RIGHT).isDown()) {
    		ball.applyAngularForce(-10.0f);
    	}
    	
    		
    	// Game logic comes here // Nothing to do, yet
    	// Simulate physics
    	// Our body is fixed, though, nothing will move world.update(deltaTime);
    	// We can render our scene now,
    	world.update(deltaTime);
    	
    	blockGraphics.draw(window);
    	ballGraphics.draw(window);
    	plankGraphics.draw(window);
    	
    	// we must place the camera where we want
    	// We will look at the origin (identity) and increase the view size a bit
    	window.setRelativeTransform(Transform.I.scaled(10.0f));
      
        // The actual rendering will be done now, by the program loop
    }

    // This event is raised after game ends, to release additional resources
    @Override
    public void end() {
        // Empty on purpose, no cleanup required yet
    }
    
}
