package com.ark.darthsystem.States;

import com.ark.darthsystem.Action;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.BattlerAI;
import com.ark.darthsystem.Database.Database1;

import com.ark.darthsystem.Database.Database2;
import com.ark.darthsystem.Database.InterfaceDatabase;
import com.ark.darthsystem.Graphics.Actor;
import com.ark.darthsystem.Graphics.ActorAI;
import com.ark.darthsystem.Graphics.ActorBattler;
import com.ark.darthsystem.Graphics.ActorCollision;
import com.ark.darthsystem.Graphics.ActorSkill;
import com.ark.darthsystem.Graphics.GameTimer;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.Graphics.Input;
import com.ark.darthsystem.Graphics.Player;
import com.ark.darthsystem.Graphics.PlayerCamera;
import static com.ark.darthsystem.States.State.HEIGHT;
import static com.ark.darthsystem.States.State.WIDTH;
import com.ark.darthsystem.States.events.Event;
import com.ark.darthsystem.States.events.Teleport;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class OverheadMap extends OrthogonalTiledMapRenderer implements State {
    private Fixture boundXMinFixture, boundYMinFixture, boundXMaxFixture, boundYMaxFixture;
    private boolean worldStep;
    private Array<Body> deleteQueue = new Array<>();
    private String bgm;
    private ArrayList<String> message = new ArrayList<>();
    private int elapsed = 0;
    private final int MESSAGE_TIME = 3000;

    public float getWidth() {
        return width / PlayerCamera.PIXELS_TO_METERS;
    }

    public float getHeight() {
        return height / PlayerCamera.PIXELS_TO_METERS;
    }
    
    public String getMusic() {
        return bgm;
    }

    private void generateBounds() {
        BodyDef genericBodyType = new BodyDef();
        genericBodyType.type = BodyDef.BodyType.StaticBody;
        genericBodyType.fixedRotation = true;
        {
            genericBodyType.position.set(0, 0);
            boundXMin = world.createBody(genericBodyType);
            boundYMin = world.createBody(genericBodyType);
            boundXMax = world.createBody(genericBodyType);
            boundYMax = world.createBody(genericBodyType);
        }
        FixtureDef fixtureDef = new FixtureDef();
        EdgeShape shape1 = new EdgeShape() {
            {
                this.set(new Vector2(0, 0), new Vector2(0, OverheadMap.height / PlayerCamera.PIXELS_TO_METERS));
            }
        };
        EdgeShape shape2 = new EdgeShape() {
            {
                this.set(new Vector2(0, 0), new Vector2(OverheadMap.width / PlayerCamera.PIXELS_TO_METERS, 0));
            }
        };
        EdgeShape shape3 = new EdgeShape() {
            {
                this.set(new Vector2(OverheadMap.width / PlayerCamera.PIXELS_TO_METERS, 0), new Vector2(OverheadMap.width / PlayerCamera.PIXELS_TO_METERS, OverheadMap.height / PlayerCamera.PIXELS_TO_METERS));
            }
        };
        EdgeShape shape4 = new EdgeShape() {
            {
                this.set(new Vector2(0, OverheadMap.height / PlayerCamera.PIXELS_TO_METERS), new Vector2(OverheadMap.width / PlayerCamera.PIXELS_TO_METERS, OverheadMap.height / PlayerCamera.PIXELS_TO_METERS));
            }
        };
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.filter.categoryBits = ActorCollision.CATEGORY_WALLS;
        fixtureDef.filter.maskBits = -1;
        // Create our fixture and attach it to the body
        fixtureDef.shape = shape1;
        boundXMinFixture = boundXMin.createFixture(fixtureDef);
        fixtureDef.shape = shape2;
        boundXMinFixture = boundYMin.createFixture(fixtureDef);
        fixtureDef.shape = shape3;
        boundXMinFixture = boundXMax.createFixture(fixtureDef);
        fixtureDef.shape = shape4;
        boundXMinFixture = boundYMax.createFixture(fixtureDef);
        shape1.dispose();
        shape2.dispose();
        shape3.dispose();
        shape4.dispose();

    }

    private static float ppt = 0;

    private Array<Body> generateObjects() {
        ppt = PlayerCamera.PIXELS_TO_METERS;
        MapObjects objects = map.getLayers().get("collision").getObjects();
        Array<Body> bodies = new Array<>();
        
        
        for(MapObject object : objects) {
            if (object instanceof TextureMapObject) {
                continue;
            }
            
            MapProperties properties = object.getProperties();
            
            if (properties.get("type", String.class).equalsIgnoreCase("actor")) {
//                Actor a = AIDatabase.actors.get(properties.get("Name", String.class));
//                a.setMap(this, false);
//                a.setX(properties.get("x", Float.class));
//                a.setY(properties.get("y", Float.class));
                continue;
            }

            Shape shape;
            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject)object);
            }
            else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject)object);
            }
            else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject)object);
            }
            else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject)object);
            }
            else {
                continue;
            }

            BodyDef bd = new BodyDef();
            bd.type = BodyType.StaticBody;            
            Body body = world.createBody(bd);
            
            Fixture f = body.createFixture(shape, 1);
            Filter filter = new Filter();
            body.setUserData(object);
            
            if (properties.get("type", String.class).equalsIgnoreCase("wall")) {
                filter.categoryBits = ActorCollision.CATEGORY_WALLS;               
                filter.maskBits = -1;
            } else if (properties.get("type", String.class).equalsIgnoreCase("obstacle")) {
                filter.categoryBits = ActorCollision.CATEGORY_OBSTACLES;
                filter.maskBits = ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_PLAYER;
            } else if (properties.get("type", String.class).equalsIgnoreCase("event")) {
                body.setUserData(addEventFromMap(object));
                filter.categoryBits = ActorCollision.CATEGORY_EVENT;
                f.setSensor(true);
                filter.maskBits = ActorCollision.CATEGORY_PLAYER;
            }
            
            f.setFilterData(filter);            
            bodies.add(body);

            shape.dispose();
        }
        return bodies;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / ppt,
                                   (rectangle.y + rectangle.height * 0.5f ) / ppt);
        polygon.setAsBox(rectangle.width * 0.5f / ppt,
                         rectangle.height * 0.5f / ppt,
                         size,
                         0.0f);
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / ppt);
        circleShape.setPosition(new Vector2(circle.x / ppt, circle.y / ppt));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / ppt;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / ppt;
            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
        }

        ChainShape chain = new ChainShape(); 
        chain.createChain(worldVertices);
        return chain;
    }
    
    private Event addEventFromMap(MapObject object) {
        MapProperties prop = object.getProperties();
        Event e = null;
        switch (Integer.parseInt(prop.get("eventID", String.class))) {
            case 0:
                break;
            case 1:
                break;
            case 2: //Teleport
                String [] parameters = prop.get("parameters", String.class).split(",* ");
                System.out.println(parameters[0]);
                e = new Teleport(null, 
                        prop.get("x", Float.class),
                        prop.get("y", Float.class), 
                        6/60f, 
                        parameters[0], 
                        Integer.parseInt(parameters[1]),
                        Integer.parseInt(parameters[2]));
                break;
            default:
                break;            
        }
        e.setMap(this, false);
        return e;
    }

    private void updateProperties(MapProperties prop) {
        try {
            bgm = "music/" + prop.get("music", String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private enum TileType {

        WALL,
        OBSTACLE, //You can jump over these.
        EVENT,
        PIT;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    private Array<Actor> playerActors;
    private Array<Actor> enemyActors;
    private final int DRAW_SPRITES_AFTER_LAYER = 1;
    private World world;
    private Box2DDebugRenderer debugRender = new Box2DDebugRenderer();
    private static int width;
    private static int height;
    private Body boundXMin, boundXMax, boundYMin, boundYMax;
    

    public OverheadMap(String mapName) {
        super((new TmxMapLoader().load(mapName)), 1f / PlayerCamera.PIXELS_TO_METERS);

        Parameters parameters = new Parameters();
        parameters.flipY = false;
        TiledMap tiledMap = (new TmxMapLoader().load(mapName, parameters));
        for (MapLayer m : (tiledMap.getLayers())) {
            if (!(m instanceof TiledMapTileLayer)) {
                for (MapObject mo : m.getObjects()) {
                    if (mo instanceof TextureMapObject) {
                        ((TextureMapObject) (mo)).getTextureRegion().flip(false, true);
                    }
                }
            }
        }
        for (TiledMapTileSet tileset : tiledMap.getTileSets()) {
            for (Iterator iterator = tileset.iterator(); iterator.hasNext();) {
                TiledMapTile tiled = (TiledMapTile) (iterator.next());
                tiled.getTextureRegion().flip(false, true);
            }
        }
        this.setMap(tiledMap);
        MapProperties prop = tiledMap.getProperties();
        updateProperties(prop);
        width = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        height = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);        
        playerActors = new Array<>();
        enemyActors = new Array<>();
        World.setVelocityThreshold(1000f);
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();
                if (a.isSensor() || b.isSensor()) {
                    if (a.getBody().getUserData() instanceof ActorSkill) {
                        if (b.getBody().getUserData() instanceof Player) {
                            renderCollision(b, a);
                        }
                    }
                    if (b.getBody().getUserData() instanceof ActorSkill) {
                        if (a.getBody().getUserData() instanceof Player) {
                            renderCollision(a, b);
                        }
                    }
                    if (a.getBody().getUserData() instanceof Player && b.getBody().getUserData() instanceof Player) {
                        battleStart();
                    }
                    if (a.getBody().getUserData() instanceof Player && b.getBody().getUserData() instanceof Event) {
                        renderEvent(a, b);
                    }
                    if (a.getBody().getUserData() instanceof Event && b.getBody().getUserData() instanceof Player) {
                        renderEvent(b, a);
                    }
                }
            }

            @Override
            public void endContact(Contact cntct) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void preSolve(Contact cntct, Manifold mnfld) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void postSolve(Contact cntct, ContactImpulse ci) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            private void renderCollision(Fixture a, Fixture b) {
                final float KNOCKBACK = 4f;
                a.getBody().setLinearVelocity(
                        ((Actor)(b.getBody().getUserData())).getFacing().getX() * a.getBody().getPosition().add(b.getBody().getPosition()).x * KNOCKBACK, 
                        ((Actor)(b.getBody().getUserData())).getFacing().getY() * a.getBody().getPosition().add(b.getBody().getPosition()).y * KNOCKBACK);
                if (a.getBody().getUserData() instanceof ActorAI) {
                    ActorAI tempAI = (ActorAI) a.getBody().getUserData();
                    ActorSkill tempSkill = (ActorSkill) b.getBody().getUserData();

                    if (tempSkill.getSkill() == null) {
                        Action action = new Action(Battle.Command.Attack,
                                tempSkill.getInvoker()
                                .getCurrentBattler().getBattler(),
                                tempAI.getCurrentBattler().getBattler(),
                                tempAI.getAllBattlers());
                        action.calculateDamage(new Battle(tempSkill.getInvoker().getAllActorBattlers(),
                                        tempAI.getAllActorBattlers(),
                                Database1.inventory, null));
                    } else {
                        Action action = new Action(Battle.Command.Skill,
                                tempSkill.getSkill(),
                                tempSkill.getInvoker()
                                .getCurrentBattler().getBattler(),
                                tempAI.getCurrentBattler().getBattler(),
                                tempAI.getAllBattlers());
                        action.calculateDamage(new Battle(tempSkill.getInvoker().getAllActorBattlers(),
                                        tempAI.getAllActorBattlers(),
                                Database1.inventory, null));
                    }
                    if (tempAI.totalPartyKill()) {
                        for (Battler battler : tempSkill.getInvoker().getAllBattlers()) {
                            for (BattlerAI getBattlerAI : tempAI.getAllBattlerAI()) {
                                battler.changeExperiencePoints(getBattlerAI.getExperienceValue());
                            }
                        }
                        removeActor(tempAI, false);
                    }

                    //clear certain called events
                    for (Actor actors :  enemyActors) {
                        if (actors instanceof ActorSkill) {
                            if (((ActorSkill)(actors)).getInvoker().equals(tempAI)) {
                                removeActor(actors, false);
                                ((ActorSkill)(actors)).stopFieldSound();
                            }
                        }
                    }
                    for (GameTimer t : tempAI.getTimers()) {
                        switch (t.getName().toUpperCase()) {
                            case "ATTACK":
                            case "SKILL":
                            case "CHARGE":
                            case "JUMP":
                            case "OUCH":
                                t.event(tempAI);
                                tempAI.getTimers().remove(t);
                                break;
                        }
                    }
                    
                }
                else {
                    Player tempPlayer = (Player) a.getBody().getUserData();
                    ActorSkill tempSkill = (ActorSkill) b.getBody().getUserData();
                    if (tempSkill.getSkill() == null) {
                        Action action = new Action(Battle.Command.Attack,
                                tempSkill.getInvoker()
                                .getCurrentBattler().getBattler(),
                                tempPlayer.getCurrentBattler().getBattler(),
                                tempPlayer.getAllBattlers());
                        action.calculateDamage(new Battle(
                                        tempPlayer.getAllActorBattlers(),
                                        tempSkill.getInvoker().getAllActorBattlers(),
                                        Database1.inventory, null));
                    } else {
                        Action action = new Action(Battle.Command.Skill,
                                tempSkill.getSkill(),
                                tempSkill.getInvoker()
                                .getCurrentBattler().getBattler(),
                                tempPlayer.getCurrentBattler().getBattler(),
                                tempPlayer.getAllBattlers());
                        action.calculateDamage(new Battle(tempSkill.getInvoker().getAllActorBattlers(), tempPlayer.getAllActorBattlers(), Database1.inventory, null));
                    }                                
                    for (Actor actors:  playerActors) {
                        if (actors instanceof ActorSkill) {
                            if (((ActorSkill)(actors)).getInvoker().equals(tempPlayer)) {
                                removeActor(actors, true);
                                ((ActorSkill)(actors)).stopFieldSound();
                            }
                        }
                    }
                    for (GameTimer t : tempPlayer.getTimers()) {
                        switch (t.getName().toUpperCase()) {
                            case "ATTACK":
                            case "SKILL":
                            case "CHARGE":
                            case "JUMP":
                            case "OUCH":
                                t.event(tempPlayer);
                                tempPlayer.getTimers().remove(t);
                                break;
                        }
                    }
                }
            }

            private void renderEvent(Fixture a, Fixture b) {
//                Player tempPlayer = (Player) a.getBody().getUserData();
                Event tempEvent = (Event) b.getBody().getUserData();
                if (tempEvent.isTriggered(Event.TriggerMethod.TOUCH)) {
                    tempEvent.run();
                }
            }
            
        });
        generateBounds();
        generateObjects();
    }
    
    public OverheadMap(String mapName, String bgmName) {
        this(mapName);
        bgm = bgmName;
    }

    @Override
    public float update(float delta) {
        for (Actor a : playerActors) {
            a.update(delta);
            if (a.isFinished()) {
                removeActor(a, true);
            }
        }
        for (Actor a : enemyActors) {
            if (a instanceof ActorAI && ((ActorAI) (a)).totalPartyKill()) {
                removeActor(a, false);
            } else {
                a.update(delta);
                if (a.isFinished()) {
                    removeActor(a, false);
                }
            }
        }
        if (Input.getKeyPressed(Keys.ESCAPE)) {
            GraphicsDriver.clearAllStates();
            GraphicsDriver.addState(new Title());

        }
        worldStep = true;
        if (!message.isEmpty()) {
            elapsed += delta;
            if (elapsed >= MESSAGE_TIME) {
                elapsed = 0;
                clearMessages();
            }
        }
        return delta;
    }

    public void addActor(Actor a, boolean isPlayer) {
        if (isPlayer) {
            if (!playerActors.contains(a, true)) {
                playerActors.add(a);
            }
        } else if (!enemyActors.contains(a, true)) {
            enemyActors.add(a);
        }
    }

    public void removeActor(Actor a, boolean isPlayer) {
        if (isPlayer) {
            playerActors.removeValue(a, true);
        } else {
            enemyActors.removeValue(a, true);
        }
        
        if (!(a instanceof Player)) {
            a.resetAnimation();
        }
        if (a instanceof ActorCollision) {
            deleteQueue.add(((ActorCollision) (a)).getMainBody());
            deleteQueue.add(((ActorCollision) (a)).getSensorBody());
        }
    }
    
    public void removeActor(Actor a, Iterator it) {
        it.remove();
        
        if (!(a instanceof Player)) {
            a.resetAnimation();
        }
        if (a instanceof ActorCollision) {
            deleteQueue.add(((ActorCollision) (a)).getMainBody());
            deleteQueue.add(((ActorCollision) (a)).getSensorBody());
        }
    }
    
    
    private void battleStart() {
            ArrayList<ActorBattler> encounters = new ArrayList<>();
            for (Actor enemyActors2 : enemyActors) {
                if (enemyActors2 instanceof ActorAI && ((ActorAI) (enemyActors2)).vision()) {
                    encounters.addAll(((ActorAI) (enemyActors2)).getAllActorBattlers());
                }
            }
            GraphicsDriver.addState(new Battle(GraphicsDriver.getPlayer().getAllActorBattlers(), encounters, Database1.inventory, null).start());
    }    
    
    @Override
    public void render(SpriteBatch batch) {
        GraphicsDriver.setCurrentCamera(GraphicsDriver.getPlayerCamera());
        GraphicsDriver.getPlayerCamera().followPlayer(
                Math.round(Database2.player.getX() * 50f) / 50f,
                Math.round(Database2.player.getY() * 50f) / 50f,
                width,
                height);
        GraphicsDriver.getPlayerCamera().update();
        setView(GraphicsDriver.getPlayerCamera());
        beginRender();
        int currentLayer = 0;
        for (MapLayer layer : map.getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer) layer);
                    currentLayer++;
                    if (currentLayer == DRAW_SPRITES_AFTER_LAYER) {
                        for (Actor a : playerActors) {
                            a.render(this.batch);
                        }
                        for (Actor a : enemyActors) {
                            a.render(this.batch);
                        }
                    }
                } else {
                    for (MapObject mo : layer.getObjects()) {
                        renderObject(mo);
                    }
                }
            }
        }
//        GraphicsDriver.getPlayer().renderGlobalData(batch);
        this.batch.setProjectionMatrix(GraphicsDriver.getCamera().combined);
        GraphicsDriver.getPlayer().renderGlobalData(this.batch);
        drawMessage(this.batch);
        endRender();
        
        if (worldStep) {
            world.step(1f/60f, 6, 2);  //Fix the second and third values.
            for (Body bodies : deleteQueue) {
                bodies.setActive(false);
                deleteQueue.removeValue(bodies, true);
            }
            worldStep = false;
        }
        
        debugRender.render(world, GraphicsDriver.getCurrentCamera().combined);
    }

    @Override
    public void renderObject(MapObject object) {
        if (object instanceof TextureMapObject) {
            TextureMapObject textureObject = (TextureMapObject) object;
            this.batch.draw(textureObject.getTextureRegion(),
                    textureObject.getX() / PlayerCamera.PIXELS_TO_METERS,
                    textureObject.getY() / PlayerCamera.PIXELS_TO_METERS,
                    textureObject.getOriginX(),
                    textureObject.getOriginY(),
                    textureObject.getTextureRegion().getRegionWidth(),
                    textureObject.getTextureRegion().getRegionHeight(),
                    textureObject.getScaleX() / PlayerCamera.PIXELS_TO_METERS,
                    textureObject.getScaleY() / PlayerCamera.PIXELS_TO_METERS,
                    textureObject.getRotation());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
    }

    public void setMap(String mapName) {
        Parameters parameters = new Parameters();
        parameters.flipY = false;
        TiledMap tiledMap = (new TmxMapLoader().load(mapName, parameters));
        for (MapLayer m : (tiledMap.getLayers())) {
            if (!(m instanceof TiledMapTileLayer)) {
                for (MapObject mo : m.getObjects()) {
                    if (mo instanceof TextureMapObject) {
                        ((TextureMapObject) (mo)).getTextureRegion().flip(false, true);
                    }
                }
            }
        }
        for (TiledMapTileSet tileset : tiledMap.getTileSets()) {
            for (Iterator iterator = tileset.iterator(); iterator.hasNext();) {
                TiledMapTile tiled = (TiledMapTile) (iterator.next());
                tiled.getTextureRegion().flip(false, true);
            }
        }
        this.setMap(tiledMap);
        MapProperties prop = tiledMap.getProperties();
        width = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        height = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);
    }
    
    public World getPhysicsWorld() {
        return world;
    }
    
    public void setMessage(String message) {
        this.message.clear();
        this.message.add(message);
        elapsed = 0;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
        elapsed = 0;
    }

    public void appendMessage(String message) {
        this.message.add(message);
        elapsed = 0;
    }

    public void appendMessage(ArrayList<String> message) {
        this.message.addAll(message);
        while (this.message.size() > 4) {
            this.message.remove(0);
        }
        elapsed = 0;
    }
    
    public void clearMessages() {
        this.message.clear();
        elapsed = 0;
    }
    
    private void drawMessage(Batch batch) {
        final int PADDING_X = 15;
        final int PADDING_Y = 12;
        final int MESSAGE_HEIGHT = HEIGHT / 6;
        final float FONT_HEIGHT = GraphicsDriver.getFont().getLineHeight();
        
        InterfaceDatabase.TEXT_BOX.draw(batch, GraphicsDriver.getCamera().getScreenPositionX(), HEIGHT - MESSAGE_HEIGHT + GraphicsDriver.getCamera().getScreenPositionY(), WIDTH, MESSAGE_HEIGHT);
        
        int i = 0;
        
        for (String m : message) {
            GraphicsDriver.drawMessage(batch, GraphicsDriver.getPlayer().getFont(), m,
                PADDING_X + GraphicsDriver.getCamera().getScreenPositionX(),
                ((PADDING_Y + HEIGHT - MESSAGE_HEIGHT + FONT_HEIGHT * i) + GraphicsDriver.getCamera().getScreenPositionY()));
            i++;
        }
                
    }

}
