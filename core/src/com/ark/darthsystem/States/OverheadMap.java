package com.ark.darthsystem.States;


import com.ark.darthsystem.Database.Database;
import com.ark.darthsystem.Database.EventDatabase;
import com.ark.darthsystem.Database.InterfaceDatabase;
import com.ark.darthsystem.Graphics.Actor;
import com.ark.darthsystem.Graphics.Player.TeamColor;
import com.ark.darthsystem.Graphics.Monster;
import com.ark.darthsystem.Graphics.ActorCollision;
import com.ark.darthsystem.Graphics.ActorSkill;
import com.ark.darthsystem.Graphics.GameTimer;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.Graphics.Input;
import com.ark.darthsystem.Graphics.Player;
import com.ark.darthsystem.Graphics.PlayerCamera;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.States.events.Base;
import com.ark.darthsystem.States.events.Event;
import com.ark.darthsystem.States.events.Pickup;
import com.ark.darthsystem.States.events.Teleport;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
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
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class OverheadMap extends OrthogonalTiledMapRenderer implements State {

    private BitmapFont font = null;
    private float ppt = 0;    
    private Fixture boundXMinFixture, boundYMinFixture, boundXMaxFixture, boundYMaxFixture;
    private boolean worldStep;
    private Array<Body> deleteQueue = new Array<>();
    private Array<Joint> deleteJointQueue = new Array<>();
    private Array<ActorCollision> createQueue = new Array<>();
    private String bgm;
    private ArrayList<String> message = new ArrayList<>();
    private int elapsed = 0;
    private Array<Actor> actorList;
    private final int DRAW_SPRITES_AFTER_LAYER = 1;
    private World world;
    private Box2DDebugRenderer debugRender = new Box2DDebugRenderer();
    private int width;
    private int height;
    private Body boundXMin, boundXMax, boundYMin, boundYMax;
    private Array<Player> players;
    private int TEAM_SIZE = 1;
    private Array<Player> teamRed = new Array<Player>(TEAM_SIZE);
    private Array<Player> teamBlue = new Array<Player>(TEAM_SIZE);
    private Array<Player> teamYellow = new Array<Player>(1);
    private Array<Player> allPlayers = new Array<Player>();
    private int teamRedCurrentLife = 15;
    private int teamBlueCurrentLife = 15;
    private Sprite circleUI;
    private Sprite[] lifeUI;
    public static final int BASE_RED_X = 3;
    public static final int BASE_RED_Y = 16;
    public static final int BASE_BLUE_X = 56;
    public static final int BASE_BLUE_Y = 16;


    

    public OverheadMap(String mapName) {
        super((new TmxMapLoader().load(mapName, new Parameters() {{this.flipY = false; this.convertObjectToTileSpace = false;}})), 1f / PlayerCamera.PIXELS_TO_METERS);
        for (MapLayer m : (getMap().getLayers())) {
            if (!(m instanceof TiledMapTileLayer)) {
                if (m instanceof TiledMapImageLayer) {
                    ((TiledMapImageLayer) m).getTextureRegion().flip(false, true);
                }
                for (MapObject mo : m.getObjects()) {
                    if (mo instanceof TextureMapObject) {
                        ((TextureMapObject) (mo)).getTextureRegion().flip(false, true);
                    }
                }
            }
        }
        for (TiledMapTileSet tileset : getMap().getTileSets()) {
            for (Iterator iterator = tileset.iterator(); iterator.hasNext();) {
                TiledMapTile tiled = (TiledMapTile) (iterator.next());
                tiled.getTextureRegion().flip(false, true);
            }
        }
        MapProperties prop = getMap().getProperties();
        updateProperties(prop);
        width = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        height = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);        
        actorList = new Array<>();
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {    
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();
                if (a.isSensor() || b.isSensor()) {
                    if (a.getBody().getUserData() instanceof ActorSkill) {
                        if (b.getBody().getUserData() instanceof Player && ((Player) (b.getBody().getUserData())).getCurrentLife() > 0) {
                            renderCollision(b, a);
                        }
                    }
                    if (b.getBody().getUserData() instanceof ActorSkill) {
                        if (a.getBody().getUserData() instanceof Player && ((Player) (a.getBody().getUserData())).getCurrentLife() > 0) {
                            renderCollision(a, b);
                        }
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
            public void preSolve(Contact contact, Manifold mnfld) {
                Actor a = (Actor) contact.getFixtureA().getBody().getUserData();
                Actor b = (Actor) contact.getFixtureB().getBody().getUserData();
                System.out.println("ACTOR A: " + a);
                System.out.println("ACTOR B: " + b);
                if ((a != null && b != null) && !(a instanceof Player && b instanceof Player) && (a.isInvulnerable() || b.isInvulnerable())) {
                    contact.setEnabled(false);
                }
                if (((a != null && b != null) && (a instanceof Pickup && b instanceof Pickup) && (a.isInvulnerable() || b.isInvulnerable()))) {
                    System.out.println("Item called");
                }
            }

            @Override
            public void postSolve(Contact cntct, ContactImpulse ci) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            private void renderCollision(Fixture a, Fixture b) {
                Player tempActor1 = (Player) a.getBody().getUserData();
                Player tempActor2 = ((ActorSkill) b.getBody().getUserData()).getInvoker();
                tempActor1.reduceLife(tempActor2.getAttack());
                if (tempActor1.getCurrentLife() <= 0) {
                    removeActor(tempActor1);
                } 
                //clear certain called events
                for (Actor actors :  actorList) {
                    if (actors instanceof ActorSkill) {
                        if (((ActorSkill)(actors)).getInvoker().equals(tempActor1)) {
                            removeActor(actors);
                            ((ActorSkill)(actors)).stopFieldSound();
                        }
                    }
                }
                clearTempRunningTimers(tempActor1);
                if (tempActor1 instanceof Monster) {

                } else {
                    tempActor1.setInvulnerability(2000);
                    if (tempActor1.getItem() != null && tempActor1.getItem().getName().equalsIgnoreCase("Meat")) {
                        tempActor1.setItem(null);
                        createPickupFromActor(tempActor1);
                    }
                }
            }

            private void renderEvent(Fixture a, Fixture b) {
                Event tempEvent = (Event) b.getBody().getUserData();
                if (tempEvent.isTriggered(Event.TriggerMethod.TOUCH)) {
                    tempEvent.run((Actor) (a.getBody().getUserData()));
                }
            }
            
        });
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/monofont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 144;
        parameter.flip = true;
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.BLACK;
        font = gen.generateFont(parameter);
        gen.dispose();

        
        generateBounds();
        generateObjects();
        buildUI();
    }
    
    public OverheadMap(String mapName, String bgmName) {
        this(mapName);
        bgm = bgmName;
    }
    
    private void buildUI() {
        final String UI_ROCK = "interface/rock";
        final String UI_LIFE = "interface/health_bar/health_bar";
        circleUI = GraphicsDriver.getMasterSheet().createSprite(UI_ROCK);
        circleUI.setOriginCenter();
        lifeUI = GraphicsDriver.getMasterSheet().createSprites(UI_LIFE).toArray(Sprite.class);
        for (Sprite s : lifeUI) {
            s.setOriginCenter();
        }
        
    }
    
    private void createPickupFromActor(Actor a) {
        Event e = new Pickup((Sprite[]) GraphicsDriver.getMasterSheet().createSprites("items/meat/icon").toArray(Sprite.class), a.getX(), a.getY(), .1f, new Item("Meat"));
        e.setMap(this);
        e.setInvulnerability(3000);
        e.setX(a.getX());
        e.setY(a.getY());
    }
    
    private PolygonShape getRectangle(RectangleMapObject rectangleObject) {
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
    private CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / ppt);
        circleShape.setPosition(new Vector2(circle.x / ppt, circle.y / ppt));
        return circleShape;
    }
    private PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();
        
        float[] worldVertices = new float[vertices.length];
        
        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / ppt;
        }
        
        polygon.set(worldVertices);
        return polygon;
    }
    private ChainShape getPolyline(PolylineMapObject polylineObject) {
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
    public float getWidth() {
        return Math.round(width / PlayerCamera.PIXELS_TO_METERS);
    }
    public float getHeight() {
        return Math.round(height / PlayerCamera.PIXELS_TO_METERS);
    }
    public String getMusic() {
        return bgm;
    }
    private void generateBounds() {
        final int BOUND_X0 = 0;
        final int BOUND_Y0 = 4;
        
        BodyDef genericBodyType = new BodyDef();
        genericBodyType.type = BodyDef.BodyType.StaticBody;
        genericBodyType.fixedRotation = true;
        {
            genericBodyType.position.set(0,0);
            boundXMin = world.createBody(genericBodyType);
            boundYMin = world.createBody(genericBodyType);
            boundXMax = world.createBody(genericBodyType);
            boundYMax = world.createBody(genericBodyType);
        }
        FixtureDef fixtureDef = new FixtureDef();
        EdgeShape shape1 = new EdgeShape() {
            {
                this.set(new Vector2(BOUND_X0, BOUND_Y0), new Vector2(BOUND_X0, height / PlayerCamera.PIXELS_TO_METERS));
            }
        };
        EdgeShape shape2 = new EdgeShape() {
            {
                this.set(new Vector2(BOUND_X0, BOUND_Y0), new Vector2(width / PlayerCamera.PIXELS_TO_METERS, BOUND_Y0));
            }
        };
        EdgeShape shape3 = new EdgeShape() {
            {
                this.set(new Vector2(width / PlayerCamera.PIXELS_TO_METERS, BOUND_Y0), new Vector2(width / PlayerCamera.PIXELS_TO_METERS, height / PlayerCamera.PIXELS_TO_METERS));
            }
        };
        EdgeShape shape4 = new EdgeShape() {
            {
                this.set(new Vector2(BOUND_X0, height / PlayerCamera.PIXELS_TO_METERS), new Vector2(width / PlayerCamera.PIXELS_TO_METERS, height / PlayerCamera.PIXELS_TO_METERS));
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
    private Array<Body> generateObjects() {
        ppt = PlayerCamera.PIXELS_TO_METERS;
//        MapObjects objects = map.getLayers().get("collision").getObjects();
        Array<Body> bodies = new Array<>();
        for (MapLayer layer: map.getLayers()) {
            for(MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    continue;
                }

                MapProperties properties = object.getProperties();

                if (properties.get("type", String.class) != null && properties.get("type", String.class).equalsIgnoreCase("actor")) {
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
                    filter.maskBits = ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_RED;
                } else if (properties.get("type", String.class).equalsIgnoreCase("event")) {
                    body.setUserData(addEventFromMap(object));
                    filter.categoryBits = ActorCollision.CATEGORY_EVENT;
                    f.setSensor(true);
                    filter.maskBits = ActorCollision.CATEGORY_RED;
                }

                f.setFilterData(filter);
                bodies.add(body);

                shape.dispose();
            }
        }
        return bodies;
    }
    
    private Event addEventFromMap(MapObject object) {
        MapProperties prop = object.getProperties();
        Event e = null;
        String[] parameters;
        switch (Integer.parseInt(prop.get("eventID").toString())) {
            case 0:
                break;
            case 1: //NovelMode
                parameters = prop.get("parameters", String.class).split(",* ");
                break;
            case 2: //Teleport
                parameters = prop.get("parameters", String.class).split(",* ");
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
        e.setMap(this);
        return e;
    }
    private void updateProperties(MapProperties prop) {
        try {
            bgm = "music/" + prop.get("music", String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updatePartial(float delta) {
        for (Actor a : actorList) {
            if (a instanceof Monster && ((Monster) (a)).getCurrentLife() <= 0) {
                removeActor(a);
            } else {
                a.update(delta);
                if (a.isFinished()) {
                    removeActor(a);
                }
            }
        }
        worldStep = true;
        for (ActorCollision a : createQueue) {
            if (!world.isLocked()) {
                a.generateBody(this);
                addActor(a);
                createQueue.removeValue(a, true);
            }
        }
    }
    
        
    public void addBody(ActorCollision body) {
        createQueue.add(body);
    }
    
    public void removeJoint(Joint joint) {
        deleteJointQueue.add(joint);
    }
    public void removeBody(Body body) {
        deleteQueue.add(body);
    }

    public float update(float delta) {

        for (Actor a : actorList) {
            if (a instanceof Player && ((Player) (a)).getCurrentLife() <= 0) {
                removeActor(a);
            } else {
                a.update(delta);
                if (a.isFinished()) {
                    removeActor(a);
                }
            }
        }
        for (Player p : teamRed) {
            if (p.getCurrentLife() <= 0) {
                teamRed.removeValue(p, true);
                teamRedCurrentLife--;
                if (p.getItem() != null) {
                    Event e = new Pickup((Sprite[]) GraphicsDriver.getMasterSheet().createSprites("items/meat/icon").toArray(Sprite.class), 350.0f, 350.0f, .1f, p.getItem());
                    e.setMap(this);
                    e.setX(p.getX());
                    e.setY(p.getY());
                }
            }
            if (!actorList.contains(p, true)) {
                for (GameTimer t : p.getTimers()) {
                    if (t.update(delta, p)) {
                        p.getTimers().removeValue(t, true);
                    }                    
                }
            }            
        }
        for (Player p : teamBlue) {
            if (p.getCurrentLife() <= 0) {
                teamBlue.removeValue(p, true);
                teamBlueCurrentLife--;
                if (p.getItem() != null) {
                    Event e = new Pickup((Sprite[]) GraphicsDriver.getMasterSheet().createSprites("items/meat/icon").toArray(Sprite.class), 350.0f, 350.0f, .1f, p.getItem());
                    e.setMap(this);
                    e.setX(p.getX());
                    e.setY(p.getY());
                }
            }
            if (!actorList.contains(p, true)) {
                for (GameTimer t : p.getTimers()) {
                    if (t.update(delta, p)) {
                        p.getTimers().removeValue(t, true);
                    }                    
                }
            }
        }
        for (Player p : teamYellow) {
            if (p.getCurrentLife() <= 0) {
                teamYellow.removeValue(p, true);
                Event e = new Pickup((Sprite[]) GraphicsDriver.getMasterSheet().createSprites("items/meat/icon").toArray(Sprite.class), 350.0f, 350.0f, .1f, new Item("Meat"));
                e.setMap(this);
                e.setX(p.getX());
                e.setY(p.getY());
            }
        }
        while (teamRed.size < TEAM_SIZE && teamRedCurrentLife > 0) {
            teamRed.add(new Player(Player.TeamColor.RED, Database.defaultRedSprite, BASE_RED_X, BASE_RED_Y));
            teamRed.get(teamRed.size - 1).setPause(2000);
            teamRed.get(teamRed.size - 1).addTimer(new GameTimer("RESPAWN", 2000) {
                @Override
                public void event(Actor a) {
                    ((Player) (a)).setMap((OverheadMap) (GraphicsDriver.getCurrentState()), BASE_RED_X, BASE_RED_Y);
                    ((Player) (a)).setInvulnerability(2000);
                }            
            });
        }
        while (teamBlue.size < TEAM_SIZE && teamBlueCurrentLife > 0) {
            teamBlue.add(new Player(Player.TeamColor.BLUE, Database.defaultRedSprite, BASE_BLUE_X, BASE_BLUE_Y));
            teamBlue.get(teamBlue.size - 1).addTimer(new GameTimer("RESPAWN", 2000) {
                @Override
                public void event(Actor a) {
                    ((Player) (a)).setMap((OverheadMap) (GraphicsDriver.getCurrentState()), BASE_BLUE_X, BASE_BLUE_Y);
                    ((Player) (a)).setInvulnerability(2000);
                }            
            });
        }
        if (Input.getKeyPressed(Keys.ESCAPE)) {
            GraphicsDriver.clearAllStates();
            GraphicsDriver.addState(new Title());

        }
        worldStep = true;
        return delta;
    }

    public void addActor(Actor a) {
        if (!actorList.contains(a, true)) {
            actorList.add(a);
        }
    }

    public void removeActor(Actor a) {
        actorList.removeValue(a, true);
        
        if (!(a instanceof Player)) {
            a.resetAnimation();
        }
        if (a instanceof ActorCollision) {
            Array<Body> temp = new Array<>();
            world.getBodies(temp);
            if (!deleteQueue.contains(((ActorCollision) (a)).getMainBody() , true) && temp.contains(((ActorCollision) (a)).getMainBody() , true)) {
                deleteQueue.add(((ActorCollision) (a)).getMainBody());
            }
            if (!deleteQueue.contains(((ActorCollision) (a)).getSensorBody(), true) && temp.contains(((ActorCollision) (a)).getSensorBody(), true)) {
                deleteQueue.add(((ActorCollision) (a)).getSensorBody());
            }
        }
    }    
    

    private void clearTempRunningTimers(Player tempPlayer) {
        for (GameTimer t : tempPlayer.getTimers()) {
            switch (t.getName().toUpperCase()) {
                case "ATTACK":
                case "SKILL":
                case "CHARGE":
                case "DODGE":
                case "OUCH":
                    t.event(tempPlayer);
                case "ATTACK_CHARGE":
                    t.clear();
                case "SKILL_CHARGE":
                    tempPlayer.getTimers().removeValue(t, true);
                    break;
            }
        }
    }
    
    @Override
    public void render(SpriteBatch batch) {

        GraphicsDriver.setCurrentCamera(GraphicsDriver.getPlayerCamera());
        GraphicsDriver.getPlayerCamera().followPlayer(Math.round(Database.player.getX() * 25f) / 25f,
                Math.round(Database.player.getY() * 25f) / 25f,
                width < GraphicsDriver.getWidth() ? GraphicsDriver.getWidth() : width,
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
                        for (Actor a : actorList) {
                            a.render(this.batch);
                        }
                    }
                } else if (layer instanceof TiledMapImageLayer) {
                    batch.setProjectionMatrix(GraphicsDriver.getCamera().combined);
                    batch.draw(((TiledMapImageLayer) layer).getTextureRegion(), 0, 0);
                    batch.setProjectionMatrix(GraphicsDriver.getPlayerCamera().combined);
                    currentLayer++;
                    if (currentLayer == DRAW_SPRITES_AFTER_LAYER) {
                        for (Actor a : actorList) {
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
        this.batch.setProjectionMatrix(GraphicsDriver.getCamera().combined);
        renderUI(this.batch);
        endRender();
        debugRender.render(world, GraphicsDriver.getCurrentCamera().combined);
        
        if (worldStep) {
            for (ActorCollision a : createQueue) {
                if (!world.isLocked()) {
                    a.generateBody(this);
                    addActor(a);
                    createQueue.removeValue(a, true);
                }
            }
            world.step(1f/60f, 6, 2);  //Fix the second and third values.
            Array<Body> temp = new Array<>();
            world.getBodies(temp);
            for (Body bodies : deleteQueue) {
                if (!world.isLocked()) {
                    if (temp.contains(bodies, true)) {
                        world.destroyBody(bodies);
                    }
                    deleteQueue.removeValue(bodies, true);
                    bodies = null;
                }
            }
            Array<Joint> temp2 = new Array<>();
            world.getJoints(temp2);            
            for (Joint joints : deleteJointQueue) {
                if (!world.isLocked()) {
                    if (temp2.contains(joints, true)) {
                        world.destroyJoint(joints);
                    }
                    deleteJointQueue.removeValue(joints, true);
                    joints = null;
                }
            }
            worldStep = false;
        }        
    }
    
    public void renderUI(Batch batch) {
        final float X1 = 0;
        final float Y1 = GraphicsDriver.getHeight() - circleUI.getHeight() / 2;
        final float X2 = GraphicsDriver.getWidth() - X1 - circleUI.getWidth() / 2;
        final float Y2 = Y1;
        batch.draw(circleUI, X1, Y1, circleUI.getOriginX(), circleUI.getOriginY());
        batch.draw(circleUI, X2, Y2, circleUI.getOriginX(), circleUI.getOriginY());
        final float X_PADDING = 30;
        final float Y_PADDING = 50;
        font.draw(batch, Integer.toString(teamRedCurrentLife), X1 + X_PADDING, Y1 + Y_PADDING);
        font.draw(batch, Integer.toString(teamBlueCurrentLife), X2 + X_PADDING, Y2 + Y_PADDING);
        final float XDELTA = circleUI.getWidth() / 2;
        for (int i = 0; i < teamRed.size; i++) {
            batch.draw(lifeUI[teamRed.get(i).getCurrentLife()], X1 + XDELTA * (i + 1), Y1, 
                    lifeUI[teamRed.get(i).getCurrentLife()].getOriginX(),
                    lifeUI[teamRed.get(i).getCurrentLife()].getOriginY());
        }
        for (int i = 0; i < teamBlue.size; i++) {
            batch.draw(lifeUI[teamBlue.get(i).getCurrentLife()], X2 - XDELTA * (i + 1), Y2,
                    lifeUI[teamBlue.get(i).getCurrentLife()].getOriginX(),
                    lifeUI[teamBlue.get(i).getCurrentLife()].getOriginY());
        }
    }

    @Override
    public void renderObject(MapObject object) {
        if (object instanceof TextureMapObject) {
            TextureMapObject textureObject = (TextureMapObject) object;
            this.batch.draw(textureObject.getTextureRegion(),
                    textureObject.getX() / PlayerCamera.PIXELS_TO_METERS,
                    textureObject.getY() / PlayerCamera.PIXELS_TO_METERS,
                    0,
                    0,
                    textureObject.getTextureRegion().getRegionWidth() / PlayerCamera.PIXELS_TO_METERS,
                    textureObject.getTextureRegion().getRegionHeight() / PlayerCamera.PIXELS_TO_METERS,
                    textureObject.getScaleX(),
                    textureObject.getScaleY(),
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
                if (m instanceof TiledMapImageLayer) {
                    ((TiledMapImageLayer) m).getTextureRegion().flip(false, true);
                    ((TiledMapImageLayer) m).setY(-((TiledMapImageLayer) m).getY());
                    
                }

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
    
    public Array<Player> getAllPlayers() {
        allPlayers.clear();
        allPlayers.addAll(teamRed);
        allPlayers.addAll(teamBlue);
        for (Player player : allPlayers) {
            if (!actorList.contains(player, true)) {
                allPlayers.removeValue(player, true);
            }
        }
        return allPlayers;
    }
    
    

    public void initialize() {
        Sprite[] s = (Sprite[]) GraphicsDriver.getMasterSheet().createSprites("event/hut").toArray(Sprite.class);
        for (Sprite sprite: s) {
            sprite.flip(true, false);
        }
        (new Base(s, BASE_RED_X, BASE_RED_Y, 1/12f, TeamColor.RED)).setMap(this);
        s = (Sprite[]) GraphicsDriver.getMasterSheet().createSprites("event/hut").toArray(Sprite.class);
        (new Base(s, BASE_BLUE_X, BASE_BLUE_Y, 1/12f, TeamColor.BLUE)).setMap(this);
        for (int i = 0; i < TEAM_SIZE; i++) {
            teamRed.add(new Player(Player.TeamColor.RED, Database.defaultRedSprite, BASE_RED_X, BASE_RED_Y));
            teamRed.get(i).setMap(this, BASE_RED_X, BASE_RED_Y);
            teamBlue.add(new Player(Player.TeamColor.BLUE, Database.defaultBlueSprite, BASE_BLUE_X, BASE_BLUE_Y));            
            teamBlue.get(i).setMap(this, BASE_BLUE_X, BASE_BLUE_Y);
        }
        teamYellow.add(new Monster(Database.defaultYellowSprite, 30, 17));
        teamYellow.get(0).setItem(new Item("Meat"));
        teamYellow.get(0).setMap(this, 30, 17);
        teamRedCurrentLife = 15;
        teamBlueCurrentLife = 15;
    }
    
    public Array getTeam(Player.TeamColor color) {
        switch (color) {
            case RED:
                return teamRed;
            case BLUE:
                return teamBlue;
            case YELLOW:
                return teamYellow;
        }
        return null;
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

}
