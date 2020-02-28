package com.ark.darthsystem.states;

import com.ark.darthsystem.Action;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.BattlerAI;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.database.Database1;
import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.database.InterfaceDatabase;
import com.ark.darthsystem.database.MapDatabase;
import com.ark.darthsystem.database.MonsterDatabase;
import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.FieldBattlerAI;
import com.ark.darthsystem.graphics.FieldBattler;
import com.ark.darthsystem.graphics.ActorCollision;
import com.ark.darthsystem.graphics.FieldSkill;
import com.ark.darthsystem.graphics.GameTimer;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.graphics.Input;
import com.ark.darthsystem.graphics.Player;
import com.ark.darthsystem.graphics.PlayerCamera;
import com.ark.darthsystem.states.events.Event;
import com.ark.darthsystem.states.events.Pickup;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Ellipse;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class OverheadMap implements State {

    private transient float ppt = 0;
    private transient Fixture boundXMinFixture, boundYMinFixture, boundXMaxFixture, boundYMaxFixture;
    private transient OrthogonalTiledMapRenderer renderer;
    private String mapName;
    private transient boolean worldStep;
    private transient Array<Body> deleteQueue = new Array<>();
    private transient Array<Joint> deleteJointQueue = new Array<>();
    private transient Array<ActorCollision> createQueue = new Array<>();
    private String bgm;
    private transient ArrayList<String> message = new ArrayList<>();
    private int elapsed = 0;
    private final int MESSAGE_TIME = 3000;
    private transient Array<Actor> actorList;
    private final int DRAW_SPRITES_AFTER_LAYER = 2;
    private transient World world;
    private transient Box2DDebugRenderer debugRender = new Box2DDebugRenderer();
    private Array<GameTimer> timers = new Array<>();
    private int width;
    private int height;
    private transient Body boundXMin, boundXMax, boundYMin, boundYMax;
    private final Object DEAD = new Object(); //Flag object as to be removed.

    public OverheadMap(String mapName) {
        renderer = new OrthogonalTiledMapRenderer(MapDatabase.getMap(mapName), 1f / PlayerCamera.PIXELS_TO_METERS);
        this.mapName = mapName;
        MapProperties prop = renderer.getMap().getProperties();
        updateProperties(prop);
        width = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        height = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);
        actorList = new Array<>(Actor.class);
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new OverheadContactListener());
        generateBounds();
        generateObjects();
    }

    public OverheadMap(String mapName, String bgmName) {
        this(mapName);
        bgm = bgmName;
    }

    private PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / ppt,
                (rectangle.y + rectangle.height * 0.5f) / ppt);
        polygon.setAsBox(rectangle.width * 0.5f / ppt,
                rectangle.height * 0.5f / ppt,
                size,
                0.0f);
        return polygon;
    }

    private CircleShape getCircle(EllipseMapObject circleObject) {
        Ellipse circle = circleObject.getEllipse();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.width / ppt / 2);
        circleShape.setPosition(new Vector2(circle.x / ppt + .5f, circle.y / ppt + .5f));
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
        float[] polyVertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[polyVertices.length / 2];

        for (int i = 0; i < polyVertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = polyVertices[i * 2] / ppt;
            worldVertices[i].y = polyVertices[i * 2 + 1] / ppt;
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

    @Override
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
                this.set(new Vector2(0, 0), new Vector2(0, height / PlayerCamera.PIXELS_TO_METERS));
            }
        };
        EdgeShape shape2 = new EdgeShape() {
            {
                this.set(new Vector2(0, 0), new Vector2(width / PlayerCamera.PIXELS_TO_METERS, 0));
            }
        };
        EdgeShape shape3 = new EdgeShape() {
            {
                this.set(new Vector2(width / PlayerCamera.PIXELS_TO_METERS, 0), new Vector2(width / PlayerCamera.PIXELS_TO_METERS, height / PlayerCamera.PIXELS_TO_METERS));
            }
        };
        EdgeShape shape4 = new EdgeShape() {
            {
                this.set(new Vector2(0, height / PlayerCamera.PIXELS_TO_METERS), new Vector2(width / PlayerCamera.PIXELS_TO_METERS, height / PlayerCamera.PIXELS_TO_METERS));
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
        Array<Body> bodies = new Array<>();
        for (int i = 0; i < renderer.getMap().getLayers().size(); i++) {
            MapLayer layer = renderer.getMap().getLayers().get(i);
            if (layer != null && layer instanceof TiledMapTileLayer && i <= DRAW_SPRITES_AFTER_LAYER) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
                for (int x = 0; x < tileLayer.getWidth(); x++) {
                    for (int y = 0; y < tileLayer.getHeight(); y++) {
                        if (tileLayer.getCell(x, y) != null && tileLayer.getCell(x, y).getTile() != null) {
                            for (MapObject object : tileLayer.getCell(x, y).getTile().getObjects()) {
                                if (object == null) {
                                    continue;
                                }
                                Body b = genTile(object);
                                if (b != null) {
                                    b.setTransform(x, y, 0);
                                    bodies.add(b);
                                }
                            }
                        }
                    }
                }

            }
            for (MapObject object : layer.getObjects()) {
                if (object instanceof TextureMapObject) {
                    continue;
                }
                Body b = genTile(object);
                if (b != null) {
                    bodies.add(b);
                }
            }
        }
        return bodies;
    }

    private Body genTile(MapObject object) {
        MapProperties properties = object.getProperties();
        if (properties.get("type", String.class) != null && properties.get("type", String.class).equalsIgnoreCase("actor")) {
            MonsterDatabase.MONSTER_LIST.get(properties.get("parameters", String.class).toUpperCase())
                    .clone().setMap(this, properties.get("x", Float.class) / PlayerCamera.PIXELS_TO_METERS, properties.get("y", Float.class) / PlayerCamera.PIXELS_TO_METERS);
            return null;
        }

        Shape shape;
        if (object instanceof RectangleMapObject) {
            shape = getRectangle((RectangleMapObject) object);
        } else if (object instanceof PolygonMapObject) {
            shape = getPolygon((PolygonMapObject) object);
        } else if (object instanceof PolylineMapObject) {
            shape = getPolyline((PolylineMapObject) object);
        } else if (object instanceof EllipseMapObject) {
            shape = getCircle((EllipseMapObject) object);
        } else {
            return null;
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
            Object o = addEventFromMap(object);
            if (o == null) {
                o = DEAD;
            }
            body.setUserData(o);
            filter.categoryBits = (short) (ActorCollision.CATEGORY_EVENT | (properties.get("collisionType", String.class).equalsIgnoreCase("WALL") ? ActorCollision.CATEGORY_WALLS : 0));
            f.setSensor(!properties.get("collisionType", String.class).equalsIgnoreCase("WALL"));
            filter.maskBits = ActorCollision.CATEGORY_PLAYER;
        }

        f.setFilterData(filter);
        shape.dispose();
        return body;

    }

    private Event addEventFromMap(MapObject object) {
        MapProperties prop = object.getProperties();
        Event e = null;
        try {
            e = ((Event) (Class.forName("com.ark.darthsystem.states.events." + prop.get("eventName", String.class))
                    .newInstance())).
                    createFromMap(prop);
            e.setID(prop.get("id", Integer.class));
            e.setMap(this);
            if (e.isFinished()) {
                e = null;
            }
            return e;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(OverheadMap.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(prop.get("eventName", String.class));
        }
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
        AnimatedTiledMapTile.updateAnimationBaseTime();
        for (int i = 0; i < actorList.size; i++) {
            Actor a = actorList.get(i);
            if (a instanceof FieldBattlerAI && ((FieldBattlerAI) (a)).totalPartyKill()) {
                removeActor(a);
                i--;
            } else {
                Input.disableInput();
                if (a instanceof Player) {
                    ((Player) a).updatePartial(delta);
                }
                Input.enableInput();
                if (a.isFinished()) {
                    removeActor(a);
                    i--;
                }
            }
        }
        worldStep = true;
        for (int i = 0; i < createQueue.size; i++) {
            if (!world.isLocked()) {
                createQueue.get(i).generateBody(this);
                addActor(createQueue.get(i));
                createQueue.removeValue(createQueue.get(i), true);
                i--;
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

    @Override
    public float update(float delta) {
        AnimatedTiledMapTile.updateAnimationBaseTime();
        for (int i = 0; i < timers.size; i++) {
            GameTimer a = timers.get(i);
            if (a.update(delta)) {
                timers.removeValue(a, true);
                i--;
            }
        }
        for (int i = 0; i < actorList.size; i++) {
            Actor a = actorList.get(i);
            if (a instanceof FieldBattlerAI && ((FieldBattlerAI) (a)).totalPartyKill()) {
                removeActor(a);
                i--;
            } else {
                a.update(delta);
                if (a.isFinished()) {
                    removeActor(a);
                    i--;
                }
            }
        }

        if (Input.getKeyPressed(Keys.ESCAPE)) {
            GraphicsDriver.clearAllstates();
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

    public void addActor(Actor a) {
        if (!actorList.contains(a, true)) {
            actorList.add(a);
        }
    }

    public void clearTemporaryActors() {
        for (int i = 0; i < actorList.size; i++) {
            Actor actor = actorList.get(i);
            if (actor instanceof FieldSkill) {
                clearTempRunningTimers(((FieldSkill) actor).getInvoker());
                removeActor(actor);
                i--;
            }
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
            //Searches for bodies marked for deletion, and dead on arrival bodies.
            for (Body b : temp) {
                if ((b.getUserData() != null && b.getUserData().equals(a)) || b.getUserData() == DEAD) {
                    deleteQueue.add(b);
                }
            }
        }
    }

    private void battleStart() {
        Array<FieldBattlerAI> clear = new Array<>();
        ArrayList<FieldBattler> encounters = new ArrayList<>();
        for (Actor enemyActors2 : actorList) {
            if (enemyActors2 instanceof FieldBattlerAI && ((FieldBattlerAI) (enemyActors2)).vision()) {
                encounters.addAll(((FieldBattlerAI) (enemyActors2)).getAllFieldBattlers());
                clear.add((FieldBattlerAI) enemyActors2);
            }
        }
        for (int i = 0; i < clear.size; i++) {
            if (clear.get(i).totalPartyKill()) {
                clear.removeIndex(i);
                i--;
            }
        }
        if (clear.size > 0) {

            for (FieldBattlerAI tempAI : clear) {
                //clear certain called events
                for (int i = 0; i < actorList.size; i++) {
                    Actor actors = actorList.get(i);
                    if (actors instanceof FieldSkill && ((FieldSkill) (actors)).getInvoker().equals(tempAI)) {
                        removeActor(actors);
                        i--;
                        ((FieldSkill) (actors)).stopFieldSound();
                    }

                }
                clearTempRunningTimers(tempAI);
            }

            for (int i = 0; i < actorList.size; i++) {
                Actor actors = actorList.get(i);
                if (actors instanceof FieldSkill) {
                    if (((FieldSkill) (actors)).getInvoker().equals(GraphicsDriver.getPlayer())) {
                        removeActor(actors);
                        i--;
                        ((FieldSkill) (actors)).stopFieldSound();
                    }
                }
            }
            clearTempRunningTimers(GraphicsDriver.getPlayer());
            GraphicsDriver.getPlayer().setInvulnerability(1000);            
            GraphicsDriver.addState(new Battle(GraphicsDriver.getPlayer().getAllFieldBattlers(), encounters, Database1.inventory, null).start());
        }
    }

    private void clearTempRunningTimers(Player tempPlayer) {
        for (int i = 0; i < tempPlayer.getTimers().size(); i++) {
            GameTimer t = tempPlayer.getTimers().get(i);
            switch (t.getName().toUpperCase()) {
                case "ATTACK":
                case "SKILL":
                case "CHARGE":
                case "JUMP":
//                case "OUCH":
                    t.event(tempPlayer);
                case "ATTACK_CHARGE":
                case "SKILL_CHARGE":
                    tempPlayer.getTimers().remove(i);
                    i--;
                    t.clear();
                    break;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        GraphicsDriver.setCurrentCamera(GraphicsDriver.getPlayerCamera());
        GraphicsDriver.getPlayerCamera().followPlayer(
                Math.round(Database2.player.getX() * 32f) / 32f,
                Math.round(Database2.player.getY() * 32f) / 32f,
                width < GraphicsDriver.getWidth() ? GraphicsDriver.getWidth() : width,
                height);
        GraphicsDriver.getPlayerCamera().update();
        renderer.setView(GraphicsDriver.getPlayerCamera());
        renderer.getBatch().begin();
        int currentLayer = 0;
        for (MapLayer layer : renderer.getMap().getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderer.renderTileLayer((TiledMapTileLayer) layer);
                    currentLayer++;
                    if (currentLayer == DRAW_SPRITES_AFTER_LAYER) {
                        for (Actor a : actorList) {
                            if (a.inCamera(GraphicsDriver.getCurrentCamera())) {
                                a.render(renderer.getBatch());
                            }
                        }
                    }
                } else {
                    for (MapObject mo : layer.getObjects()) {
                        renderer.renderObject(mo);
                    }
                }
            }
        }
        renderer.getBatch().setProjectionMatrix(GraphicsDriver.getCamera().combined);
        GraphicsDriver.getPlayer().renderGlobalData(renderer.getBatch());
        drawMessage(renderer.getBatch());
        renderer.getBatch().end();

        debugRender.render(world, GraphicsDriver.getCurrentCamera().combined);
        Array<Body> temp = new Array<>();
        world.getBodies(temp);
        for (int i = 0; i < deleteQueue.size; i++) {
            if (!world.isLocked()) {
                if (temp.contains(deleteQueue.get(i), true)) {
                    world.destroyBody(deleteQueue.get(i));
                    deleteQueue.removeValue(deleteQueue.get(i), true);
                    i--;
                }
            }
        }
        Array<Joint> temp2 = new Array<>();
        world.getJoints(temp2);

        for (int i = 0; i < deleteJointQueue.size; i++) {
            if (!world.isLocked()) {
                if (temp2.contains(deleteJointQueue.get(i), true)) {
                    world.destroyJoint(deleteJointQueue.get(i));
                    deleteJointQueue.removeValue(deleteJointQueue.get(i), true);
                    i--;
                }
            }
        }

        if (worldStep) {
            for (int i = 0; i < createQueue.size; i++) {
                if (!world.isLocked()) {
                    createQueue.get(i).generateBody(this);
                    addActor(createQueue.get(i));
                    createQueue.removeValue(createQueue.get(i), true);
                    i--;
                }
            }
            world.step(1f / 60f, 6, 2);  //Fix the second and third values.

            worldStep = false;
        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
        world.dispose();
        debugRender.dispose();
    }

    public void setMap(String mapName) {
        if (!this.mapName.equals(mapName)) {
            renderer.setMap(MapDatabase.getMap(mapName));
            this.mapName = mapName;
            MapProperties prop = renderer.getMap().getProperties();
            updateProperties(prop);
            width = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
            height = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);
            actorList = new Array<>(Actor.class);
            world = new World(new Vector2(0, 0), true);
            world.setContactListener(new OverheadContactListener());
            generateBounds();
            generateObjects();
        }
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
        while (this.message.size() > 3) {
            this.message.remove(0);
        }
        elapsed = 0;
    }

    public void appendMessage(ArrayList<String> message) {
        this.message.addAll(message);
        while (this.message.size() > 3) {
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
        final int MESSAGE_HEIGHT = GraphicsDriver.getHeight() / 8;
        final float FONT_HEIGHT = GraphicsDriver.getFont().getLineHeight();

        InterfaceDatabase.TEXT_BOX.draw(batch, GraphicsDriver.getCamera().getScreenPositionX(), GraphicsDriver.getHeight() - MESSAGE_HEIGHT + GraphicsDriver.getCamera().getScreenPositionY(), GraphicsDriver.getWidth(), MESSAGE_HEIGHT);

        int i = 0;

        for (String m : message) {
            GraphicsDriver.drawMessage(batch, GraphicsDriver.getFont(), m,
                    PADDING_X + GraphicsDriver.getCamera().getScreenPositionX(),
                    ((PADDING_Y + GraphicsDriver.getHeight() - MESSAGE_HEIGHT + FONT_HEIGHT * i) + GraphicsDriver.getCamera().getScreenPositionY()));
            i++;
        }
    }

    public String getMapName() {
        return mapName;
    }

    public Batch getBatch() {
        return renderer.getBatch();
    }

    public TiledMap getMap() {
        return renderer.getMap();
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

    private class OverheadContactListener implements ContactListener {

        public OverheadContactListener() {
        }

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();
            if (a.isSensor() || b.isSensor()) {
                if (a.getBody().getUserData() instanceof FieldSkill) {
                    if (b.getBody().getUserData() instanceof Player) {
                        renderCollision(b, a);
                    }
                }
                if (b.getBody().getUserData() instanceof FieldSkill) {
                    if (a.getBody().getUserData() instanceof Player) {
                        renderCollision(a, b);
                    }
                }
                if (a.getBody().getUserData() instanceof Player && b.getBody().getUserData() instanceof Player) {
                    boolean isInBattle = false;
                    for (State s : GraphicsDriver.getState()) {
                        isInBattle |= s instanceof Battle;
                    }
                    if (!isInBattle) {
                        battleStart();
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
        public void endContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();
            if (a.isSensor() || b.isSensor()) {
                if (a.getBody().getUserData() instanceof Player && b.getBody().getUserData() instanceof Event) {
                    endEvent(a, b);
                }
                if (a.getBody().getUserData() instanceof Event && b.getBody().getUserData() instanceof Player) {
                    endEvent(b, a);
                }
            }
        }

        protected void renderKnockback(Fixture a, Fixture b) {
            final float DISTANCE = (float) (5f * ((Player) (b.getBody().getUserData())).getCurrentBattler().getBattler().getDefend());
            Vector2 result = new Vector2(((Actor) (b.getBody().getUserData())).getX(), ((Actor) (b.getBody().getUserData())).getY()).sub(new Vector2(((FieldSkill) (a.getBody().getUserData())).getInvoker().getX(), ((FieldSkill) (a.getBody().getUserData())).getInvoker().getY()));
            result.set(result.scl(DISTANCE));
            ((Actor) (b.getBody().getUserData())).addTimer(new GameTimer("KNOCKBACK", 99999) {
                @Override
                public void event(Actor a) {
//                    b.getBody().setLinearVelocity(result);
                    b.getBody().applyLinearImpulse(result, b.getBody().getMassData().center, true);
                }

                @Override
                public boolean isFinished() {
                    return !world.isLocked();
                }
            });
            ((Player) (b.getBody().getUserData())).ouch();
        }

        protected void renderCollision(Fixture a, Fixture b) {
            renderKnockback(b, a);
            Player tempActor = (Player) a.getBody().getUserData();
            FieldSkill tempSkill = (FieldSkill) b.getBody().getUserData();
            Action action;
            if (tempSkill.getSkill() == null) {
                action = new Action(Battle.Command.Attack, tempSkill.getInvoker().getCurrentBattler().getBattler(), tempActor.getCurrentBattler().getBattler(), tempActor.getAllBattlers());
            } else {
                action = new Action(Battle.Command.Skill, tempSkill.getSkill().overrideCost(0), tempSkill.getInvoker().getCurrentBattler().getBattler(), tempActor.getCurrentBattler().getBattler(), tempActor.getAllBattlers());
            }
            action.calculateDamage(new Battle(tempSkill.getInvoker().getAllFieldBattlers(), tempActor.getAllFieldBattlers(), Database1.inventory, null));
            if (tempActor instanceof FieldBattlerAI && tempActor.totalPartyKill()) {
                createPickupFromActor(tempActor);
                tempSkill.getInvoker().getAllBattlers().forEach((Battler battler) -> {
                    ((FieldBattlerAI) (tempActor)).getAllBattlerAI().forEach((BattlerAI getBattlerAI) -> {
                        battler.changeExperiencePoints(getBattlerAI.getExperienceValue());
                    });
                });
                removeActor(tempActor);
            }
            //clear certain called events
            for (int i = 0; i < actorList.size; i++) {
                Actor actors = actorList.get(i);
                if (actors instanceof FieldSkill) {
                    if (((FieldSkill) (actors)).getInvoker().equals(tempActor)) {
                        removeActor(actors);
                        i--;
                        ((FieldSkill) (actors)).stopFieldSound();
                    }
                }
            }
            clearTempRunningTimers(tempActor);
        }

        protected void renderEvent(Fixture a, Fixture b) {
            Event tempEvent = (Event) b.getBody().getUserData();
            Player tempPlayer = (Player) a.getBody().getUserData();
            if (tempEvent.isTriggered(Event.TriggerMethod.TOUCH)) {
                tempEvent.run();
            }
            if (tempEvent.isTriggered(Event.TriggerMethod.PRESS)) {
                tempPlayer.addButtonEvent(tempEvent);
            }
        }

        protected void createPickupFromActor(Player actor) {
            ArrayList<Item> dropped = new ArrayList<>();
            int money = 0;
            for (Battler enemy : actor.getAllBattlers()) {
                for (int i = 0; i < ((BattlerAI) enemy).getDroppedItem().length; i++) {
                    Item item = (Math.random() < ((BattlerAI) enemy).getDropRate()[i]) ? (Item) (((BattlerAI) enemy).getDroppedItem()[i].clone()) : null;
                    if (item != null && item.isStackable() && dropped.contains(item)) {
                        dropped.get(dropped.indexOf(item)).increaseQuantity(((BattlerAI) enemy).getDropNumber()[i]);
                    } else if (item != null) {
                        dropped.add(item);
                    }
                }
                money += ((BattlerAI) (enemy)).getMoney();
            }
            if (!dropped.isEmpty() || money > 0) {
                new Pickup(dropped.isEmpty()
                        ? "items/money/icon" : dropped.get(0).getIcon(),
                        actor.getX(), actor.getY(),
                        1 / 12, dropped.toArray(new Item[dropped.size()]), money).setTemporaryMap(OverheadMap.this);
            }
        }

        protected void endEvent(Fixture a, Fixture b) {
            Event tempEvent = (Event) b.getBody().getUserData();
            Player tempPlayer = (Player) a.getBody().getUserData();
            if (tempEvent.isTriggered(Event.TriggerMethod.PRESS)) {
                tempPlayer.removeButtonEvent(tempEvent);
            }
        }

        @Override
        public void preSolve(Contact cntct, Manifold mnfld) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void postSolve(Contact cntct, ContactImpulse ci) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    public void addTimer(GameTimer t) {
        timers.add(t);
    }

}
