package com.ark.darthsystem.graphics;

import com.ark.darthsystem.Action;
import com.ark.darthsystem.Nameable;
import com.ark.darthsystem.database.SoundDatabase;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.database.Database1;
import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;

/**
 *
 * @author trankt1
 */
public class FieldSkill extends ActorCollision implements Nameable {

    private float aftercastDelay = 0;
    private Area area;
    private String fieldSoundName;
    private float chargeTime = 0;
    private transient Sound fieldSound;
    private transient Player invoker;
    private transient WeldJoint joint;
    private ArrayList<Type> tags;
    private String originalFieldImageName;
    private transient Sprite[] originalFieldImage;
    private float relX;
    private float relY;
    private Skill skill;
    private float speed;
    private float range;
    private String name;
    private ActorSprite.SpriteModeField castImageName;
    private String castSoundName;
    private transient Sound castSound;

    public FieldSkill() {
        super();
        chargeTime = 0;
        aftercastDelay = 0;
    }

    public FieldSkill(String img,
            float getX,
            float getY,
            float delay,
            Skill getSkill,
            String shape) {
        super("skills/" + img + "/field/" + img, getX, getY, delay, true, shape);
        this.tags = new ArrayList<>();
        this.fieldSound = SoundDatabase.fieldSwordSound;
        this.fieldSoundName = "SLASH";
        originalFieldImageName = img;
        try {
            originalFieldImage = GraphicsDriver.getMasterSheet().createSprites("skills/" + img + "/field/" + img).toArray(Sprite.class);
            if (originalFieldImage.length == 0) {
                originalFieldImage = GraphicsDriver.getMasterSheet().createSprites("items/equipment/" + img + "/field/field").toArray(Sprite.class);
                if (originalFieldImage.length == 0) {
                    originalFieldImage = GraphicsDriver.getMasterSheet().createSprites(img).toArray(Sprite.class);
                    if (originalFieldImage.length == 0) {
                        throw new Exception("EMPTY" + originalFieldImageName);
                    }
                }
                Animation<Sprite> a = new Animation<>(delay, originalFieldImage);
                changeAnimation(a);
                setCurrentImage(originalFieldImage[0]);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            originalFieldImage = GraphicsDriver.getMasterSheet().createSprites("skills/wiccan_cross/field/wiccan_cross").toArray(Sprite.class);
        }
        for (Sprite s : originalFieldImage) {
            s.setCenter(s.getWidth() / 2f, s.getHeight() / 2f);
            s.setOriginCenter();
        }
        relX = getX;
        relY = getY;
        skill = getSkill;
        aftercastDelay = delay;
        speed = 0;
        range = 0;
        area = Area.FRONT;
    }


    public FieldSkill(String img,
            float getX,
            float getY,
            float delay,
            Skill getSkill,
            Area getArea,
            String shape) {
        this(img, getX, getY, delay, getSkill, shape);
        this.tags = new ArrayList<>();
        this.fieldSound = SoundDatabase.fieldSwordSound;
        this.fieldSoundName = "SLASH";
        area = getArea;
    }

    public FieldSkill(String img,
            float getX,
            float getY,
            float getTranslateX,
            float getTranslateY,
            float fps,
            float castTime,
            float delay,
            Skill getSkill,
            Area getArea,
            String shape) {
        super("skills/" + img + "/field/" + img, getX, getY, fps, true, shape);
        this.tags = new ArrayList<>();
        this.fieldSound = SoundDatabase.fieldSwordSound;
//        this.battlerSound = SoundDatabase.battlerSwordSound;
        originalFieldImageName = img;
        try {
            originalFieldImage = GraphicsDriver.getMasterSheet().createSprites("skills/" + img + "/field/" + img).toArray(Sprite.class);
            if (originalFieldImage.length == 0) {
                originalFieldImage = GraphicsDriver.getMasterSheet().createSprites("items/equipment/" + img + "/field/field").toArray(Sprite.class);
                if (originalFieldImage.length == 0) {
                    originalFieldImage = GraphicsDriver.getMasterSheet().createSprites(img).toArray(Sprite.class);
                    if (originalFieldImage.length == 0) {
                        throw new Exception("EMPTY" + originalFieldImageName);
                    }
                }
            }
            Animation<Sprite> a = new Animation<>(delay, originalFieldImage);
            changeAnimation(a);
            setCurrentImage(originalFieldImage[0]);
        } catch (Exception e) {
            e.printStackTrace();
            originalFieldImage = GraphicsDriver.getMasterSheet().createSprites("skills/wiccan_cross/field/wiccan_cross").toArray(Sprite.class);
            originalFieldImageName = "wiccan_cross";
            setCurrentImage(originalFieldImage[0]);
        }
        for (Sprite s : originalFieldImage) {
            s.setCenter(s.getWidth() / 2f, s.getHeight() / 2f);
            s.setOriginCenter();
        }
        relX = getX;
        relY = getY;
        chargeTime = castTime;
        skill = getSkill;
        aftercastDelay = delay;
        speed = getTranslateX;
        range = getTranslateY;
        area = getArea;
    }

    public FieldSkill(String name,
            String castImg,
            String img,
            String castSoundName,
            String fieldSoundName,
            String[] tags,
            float getX,
            float getY,
            float getTranslateX,
            float getTranslateY,
            float fps,
            float castTime,
            float delay,
            Skill getSkill,
            Area getArea,
            String shape) {
        this(img, getX, getY, getTranslateX, getTranslateY, fps, castTime, delay, getSkill, getArea, shape);
        this.castImageName = ActorSprite.SpriteModeField.valueOf(castImg.toUpperCase());
        this.tags = new ArrayList<>();
        for (String tag : tags) {
            this.tags.add(Type.valueOf(tag));
        }
        this.fieldSoundName = fieldSoundName.toUpperCase();
        fieldSound = SoundDatabase.SOUNDS.get(this.fieldSoundName);
        this.castSoundName = castSoundName.toUpperCase();
        castSound = SoundDatabase.SOUNDS.get(this.castSoundName);
    }

    public FieldSkill(String name,
            ActorSprite.SpriteModeField castImg,
            String img,
            String castSoundName,
            String fieldSoundName,
            ArrayList<Type> tags,
            float getX,
            float getY,
            float getTranslateX,
            float getTranslateY,
            float fps,
            float castTime,
            float delay,
            Skill getSkill,
            Area getArea,
            String shape) {
        this(img, getX, getY, getTranslateX, getTranslateY, fps, castTime, delay, getSkill, getArea, shape);
        this.castImageName = castImg;
        this.tags = tags;
        this.fieldSoundName = fieldSoundName;
        fieldSound = SoundDatabase.SOUNDS.get(this.fieldSoundName);
        this.castSoundName = castSoundName;
        castSound = SoundDatabase.SOUNDS.get(this.castSoundName);
    }

    /**
     * Generate the physics body of this actor.
     *
     * @param map The map to do this on.
     */
    @Override
    public void generateBody(OverheadMap map) {
        super.generateBody(map);
        getMainBody().setBullet(true);
        Array<Joint> temp = new Array<>();
        map.getPhysicsWorld().getJoints(temp);
        if (joint != null && temp.contains(joint, true)) {
            map.removeJoint(joint);
        }
        Filter filter = new Filter();
        filter.categoryBits = !(getInvoker() instanceof FieldBattlerAI) ? ActorCollision.CATEGORY_PLAYER_SKILL : ActorCollision.CATEGORY_AI_SKILL;
        filter.maskBits = (short) (ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES | ((getInvoker() instanceof FieldBattlerAI) ? ActorCollision.CATEGORY_PLAYER_SKILL : ActorCollision.CATEGORY_AI_SKILL));
        getMainFixture().setFilterData(filter);
        filter.maskBits = ((getInvoker() instanceof FieldBattlerAI) ? ActorCollision.CATEGORY_PLAYER : ActorCollision.CATEGORY_AI);
        getSensorFixture().setFilterData(filter);
        if (invoker.getMainBody() != null && (speed == 0 && (area == Area.FRONT))) {
            WeldJointDef def = new WeldJointDef();
            def.dampingRatio = 1f;
            def.frequencyHz = 60;
            def.collideConnected = false;
            setX(invoker);
            setY(invoker);
            def.initialize(invoker.getMainBody(), getMainBody(), new Vector2(getX(), getY()));
            joint = (WeldJoint) map.getPhysicsWorld().createJoint(def);
        } else if (invoker.getMainBody() != null && (speed == 0 && (area == Area.SELF_BENEFIT || area == Area.SELF))) {
            getMainBody().setTransform(invoker.getMainBody().getPosition(), getMainBody().getAngle());
            getSensorBody().setTransform(invoker.getSensorBody().getPosition(), getSensorBody().getAngle());
            WeldJointDef def = new WeldJointDef();
            def.dampingRatio = 1f;
            def.frequencyHz = 60;
            def.collideConnected = false;
            def.initialize(invoker.getMainBody(), getMainBody(), new Vector2(invoker.getX(), invoker.getY()));
            joint = (WeldJoint) map.getPhysicsWorld().createJoint(def);
        } else if (speed != 0) {

        }

    }

    /**
     * Gets the aftercast delay of this skill.
     *
     * @return The aftercast delay in seconds.
     */
    public float getAftercastDelay() {
        return aftercastDelay;
    }

    public Area getArea() {
        return area;
    }

    public float getAnimationDelay() {
        return ((float) (originalFieldImage.length) * (this.getDelay())) + aftercastDelay;
    }

    public float getChargeTime() {
        return chargeTime;
    }

    public Player getInvoker() {
        return invoker;
    }

    public void setInvoker(Player p) {
        invoker = p;
    }

    public float getRelX() {
        return relX;
    }

    public float getRelY() {
        return relY;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill s) {
        skill = s;
    }

    public Skill getSkill(Player a) {
        invoker = a;
        return skill;
    }

    public void setX(ActorCollision a) {
        super.changeX(a.getFacing().getX());
        if (area != Area.SELF) {
            super.setX(((relX + .5f) / (a.getFacing().y != 0 ? 1.414f : 1)) * a.getFacing().x + a.getMainBody().getPosition().x);
        } else {
            super.setX(a.getMainBody().getPosition().x);
        }
    }

    public void setY(ActorCollision a) {
        super.changeY(a.getFacing().getY());
        if (area != Area.SELF) {
            super.setY(((relY + .5f) / (a.getFacing().x != 0 ? 1.414f : 1)) * a.getFacing().y + a.getMainBody().getPosition().y);
        } else {
            super.setY(a.getMainBody().getPosition().y);
        }
    }

    public void playFieldSound() {
        try {
            fieldSound.stop();
            fieldSound.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playCastSound() {
        try {
            castSound.stop();
            castSound.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAnimationFacing() {
        getCurrentImage().
                setRotation(this.getFacing()
                        .getRotate());
    }

    public void stopFieldSound() {
        try {
            fieldSound.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopCastSound() {
        try {
            castSound.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(float delta) {
        setFacing();
        if (speed != 0) {
            getMainBody().setLinearVelocity(getFacing().getX() * speed, getFacing().getY() * speed);
        }
        super.update(delta);
        setAnimationFacing();
    }

    public FieldSkill placeOnMap() {
        return new FieldSkill(name, //Name
                    castImageName, //castimage
                    originalFieldImageName, //final image
                    castSoundName, //cast sound
                    fieldSoundName, //sound
                    tags, //tag
                    relX, //startx
                    relY, //starty
                    speed, //speed
                    range, //range
                    getDelay(), //fps
                    chargeTime, //cast time
                    aftercastDelay, //aftercast delay
                    skill, //skill
                    area, //area
                    getShape() //shape
            );
    }

    public void activateRush(Player player) {
        setInvoker(invoker);
        FieldSkill tempSkill = placeOnMap();
        Array<Sprite> s = new Array<>();
        Animation leftAnimation;
        for (Object r : tempSkill.getCurrentAnimation().getKeyFrames()) {
            s.add(new Sprite((TextureRegion) r));
            s.peek().flip(true, false);
        }
        leftAnimation = new Animation<>(tempSkill.getDelay(), s);
        leftAnimation.setPlayMode(Animation.PlayMode.LOOP);
        tempSkill.setInvoker(invoker);
        tempSkill.setX(invoker);
        tempSkill.setY(invoker);
        invoker.setAttacking(true);
        GameTimer canSkill = new GameTimer("Skill", 7000) {
            @Override
            public void event(Actor a) {
                invoker.setCanSkill(true);
            }

            public boolean update(float delta, Actor a) {
                invoker.setCanSkill(false);
                return super.update(delta, a);
            }
        };
        invoker.addTimer(canSkill);
        invoker.setPause((1000f));
        GameTimer isAttacking = new GameTimer("Skill", 2000f) {
            @Override
            public void event(Actor a) {
                invoker.setAttacking(false);
            }
        };
        invoker.addTimer(isAttacking);

        invoker.addTimer(new GameTimer("Skill_Charge", 1000f) {
            @Override
            public void event(Actor a) {
                tempSkill.playFieldSound();
                invoker.setFieldState(ActorSprite.SpriteModeField.CUSTOM);
                invoker.setPause(speed == 0 ? tempSkill.getAnimationDelay() * 1000f : 400f);
                invoker.getSpriteSheet().setFieldAnimation(ActorSprite.SpriteModeField.CUSTOM, Facing.LEFT, leftAnimation);
                invoker.getSpriteSheet().setFieldAnimation(ActorSprite.SpriteModeField.CUSTOM, Facing.RIGHT, tempSkill.getCurrentAnimation());
                invoker.changeAnimation(tempSkill.getCurrentAnimation());
                invoker.resetAnimation();
                tempSkill.getCurrentAnimation().setPlayMode(Animation.PlayMode.LOOP);
                invoker.getSensorBody().setUserData(tempSkill);
                GameTimer tempTimer = new GameTimer("Skill", 1000) {
                    @Override
                    public void event(Actor a) {
                        invoker.getSensorBody().setUserData(invoker);
                        invoker.setCanAttack(true);
                        invoker.setDefaultFilter();
                        invoker.setFieldState(ActorSprite.SpriteModeField.STAND);
                        invoker.enableMovement();
                        invoker.resetSprite();
                    }

                    @Override
                    public boolean update(float delta, Actor a) {
                        invoker.setFieldState(ActorSprite.SpriteModeField.CUSTOM);
                        invoker.getMainBody().setLinearVelocity(
                                5 * (invoker.getFacing().getX() - invoker.getFacing().getX() * Math.abs(invoker.getFacing().getY()) * (1 - .707f)) * invoker.getSpeed() * delta,
                                5 * invoker.getSpeed() * delta * (invoker.getFacing().getY() - invoker.getFacing().getY() * Math.abs(invoker.getFacing().getX()) * (1 - .707f)));
                        invoker.changeX(invoker.getFacing().getX());
                        invoker.changeY(invoker.getFacing().getY());

                        invoker.setCanAttack(false);
                        invoker.setWalking(true);
                        invoker.disableMovement();
                        return super.update(delta, a);
                    }
                };
                invoker.setInvulnerability(1000);
                invoker.addTimer(tempTimer);
                Filter filter = new Filter();
                filter.categoryBits = !(getInvoker() instanceof FieldBattlerAI) ? ActorCollision.CATEGORY_PLAYER_SKILL : ActorCollision.CATEGORY_AI_SKILL;
                filter.maskBits = (short) (ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES | ((getInvoker() instanceof FieldBattlerAI) ? ActorCollision.CATEGORY_PLAYER_SKILL : ActorCollision.CATEGORY_AI_SKILL));
                invoker.getMainFixture().setFilterData(filter);
                filter.maskBits = ((getInvoker() instanceof FieldBattlerAI) ? ActorCollision.CATEGORY_PLAYER : ActorCollision.CATEGORY_AI);
                invoker.getSensorFixture().setFilterData(filter);
            }

            public boolean update(float delta, Actor a) {
                invoker.setFieldState(ActorSprite.SpriteModeField.SKILL);
                return super.update(delta, a);
            }

        });
    }

    public void activate(Player player) {
        activate(player, player.getCurrentBattler(), null);
    }

    public void activate(Player player, FieldBattler caster, FieldBattler target) {
        FieldSkill tempSkill;
        tempSkill = placeOnMap();
        if (caster.activateCurrentSkill() != null) {
            if (player.getFacing().getX() == -1) {
                Array<Sprite> s = new Array<>(Sprite.class);
                for (Object r : tempSkill.getCurrentAnimation().getKeyFrames()) {
                    s.add(new Sprite((TextureRegion) r));
                    s.peek().flip(true, false);
                }
                Animation<Sprite> a = new Animation<>(tempSkill.getDelay(), s);
                tempSkill.changeAnimation(a);
            }
            tempSkill.setInvoker(player);
            tempSkill.setX(player);
            tempSkill.setY(player);
            player.setAttacking(true);
            try {
                tempSkill.playCastSound();
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.addTimer(new GameTimer("Skill", tempSkill.getChargeTime() * 1000f + player.getSpriteSheet().getFieldAnimation(castImageName, player.getFacing()).getAnimationDuration() * 1000f + tempSkill.getAftercastDelay() * 1000f) {
                @Override
                public void event(Actor a) {
                    player.setCanSkill(true);
                }

                @Override
                public boolean update(float delta, Actor a) {
                    player.setCanSkill(false);
                    return super.update(delta, a);
                }
            });
            player.setPause((tempSkill.getChargeTime() * 1000f));
            player.addTimer(new GameTimer("Skill", player.getSpriteSheet().getFieldAnimation(castImageName, player.getFacing()).getAnimationDuration() * 1000f) {
                @Override
                public void event(Actor a) {
                    player.setAttacking(false);
                }

                public boolean update(float delta, Actor a) {
                    player.setFieldState(ActorSprite.SpriteModeField.SKILL);
                    player.setAttacking(true);
                    return super.update(delta, a);
                }
            });
            player.addTimer(new GameTimer("Skill_Charge", tempSkill.getChargeTime() * 1000f) {
                @Override
                public void event(Actor a) {
                    tempSkill.stopCastSound();
                    tempSkill.playFieldSound();
                    player.setAttacking(false);
                    player.setFieldState(ActorSprite.SpriteModeField.SKILL);
                    player.setPause((tempSkill.getCurrentAnimation().getAnimationDuration() * 1000f));
                    if (tempSkill.getSkill().getAlly()) {
                        Action action = new Action(Battle.Command.Skill,
                                tempSkill.getSkill().overrideCost(0),
                                caster.getBattler(),
                                target != null ? target.getBattler() : tempSkill.getInvoker().getCurrentBattler().getBattler(),
                                player.getAllBattlers());
                        action.calculateDamage(new Battle(tempSkill.getInvoker().getAllFieldBattlers(), player.getAllFieldBattlers(), Database1.inventory, null));
                    }
                    tempSkill.setMap(player.getCurrentMap());
                }

                public boolean update(float delta, Actor a) {
                    player.setFieldState(ActorSprite.SpriteModeField.SKILL);
                    player.setAttacking(true);
                    return super.update(delta, a);
                }

                public void clear() {
                    SoundDatabase.battlerCastingSound.stop();
                    player.setFieldState(ActorSprite.SpriteModeField.STAND);
                    player.setCanSkill(true);
                    player.setAttacking(false);
                }
            });
        }
    }

    public String getCastSoundName() {
        return castSoundName;
    }

    public void setCastSoundName(String castSoundName) {
        this.castSoundName = castSoundName;
    }

    public Sound getCastSound() {
        return castSound;
    }

    public void setCastSound(Sound castSound) {
        this.castSound = castSound;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return skill != null ? skill.getDescription() : "";
    }

    public enum Type {
        LIGHTNING, // (Powers certain items.  Deals damage to all enemies in WATER tiles.)
        WIND, // (Blows back enemy)
        FIRE, // (Set certain things on fire, such as torches)
        ICE, // (Freeze Water.  All Water spells put out fires.)
        WATER, // (Place Water on object.  Puts out fires or conducts electricity...)
        HEAL, // Just some healing..
        BUFF, // Just some buffing..
        STONE, // (Creates a stone wall that acts as an obstacle)
        SHINE, // (Temporarily removes darkness)
        PURIFY, // (Removes festering corruption)
        CORRUPT, // (Inflicts corruption on vulnerable creatures and items)
        SLASH, // (Cuts down things)
        PIERCE, // (bypass obstacles)
        SMASH, // Break stone
        RED, // Red
        BLUE, // Blue
        YELLOW, // Yellow
        GREEN, // Green
        WHITE, // White
        BLACK, // Black
        PRISMATIC, // All Colors
        EVENT // Call an Event instead.
    }

    public enum Area {
        ALL,
        BOOMERANG,
        CONE,
        CROSS,
        FRONT,
        LINE,
        SELF,
        SELF_BENEFIT,
        RADIUS,
        SPHERE;

        private int translateX;
        private int translateY;

        float updateX(float delta, Facing f) {
            switch (this) {
                case SELF:
                case SELF_BENEFIT:
                    return 0;
                case FRONT:
                    return translateX * f.getX();
                case LINE:
                    return f.getX();
                case RADIUS:
                    break;
                case CROSS:
                    break;
                case CONE:
                    break;
                case SPHERE:
                    break;
                case BOOMERANG:
                    break;
                default:
                    while (true) {
                        System.out.println("You shouldn't have done that.");
                    }
            }
            return 0;
        }

    }

}
