/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states;

import com.ark.darthsystem.database.InterfaceDatabase;
import com.ark.darthsystem.GameOverException;
import com.ark.darthsystem.Nameable;
import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.graphics.Input;
import static com.ark.darthsystem.graphics.GraphicsDriver.getCurrentState;
import static com.ark.darthsystem.graphics.GraphicsDriver.removeCurrentState;
import com.ark.darthsystem.states.chapters.Novel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author Keven
 */
public abstract class Menu implements State {
    private final int PADDING_X = 20;
    private final int PADDING = 3;

    private final int BACK_BUTTON = Keys.X;
    private String[] choices;
    private final int MESSAGE_HEIGHT;
    private final int CONFIRM_BUTTON = Keys.Z;
    private Sprite cursorTexture;
    private int cursorIndex;
    private boolean destroyOnExit;
    private int DOWN_BUTTON = Keys.DOWN;
    private String header;
    private boolean isPause;
    private final int MENU_X;
    private float x;
    private float y;
    private final int MENU_Y;
    private int menuIndex = 0;
    private Array<Menu> subMenuList;
    private Array<Actor> subActors;
    private final int UP_BUTTON = Keys.UP;
    private BitmapFont font;

    public Menu(String header, String[] choices) {
        this(choices);
        this.subActors = new Array<>();
        this.header = header;
     }

    public Menu(String header, String[] choices, boolean pause, boolean mutable) {
        this(header, choices);
        this.subActors = new Array<>();
        isPause = pause;
        destroyOnExit = mutable;
    }
    
    public <T extends Nameable> Menu(String header, T[] choices) {
        this(header, Arrays.stream(choices).map(i -> i.getName()).collect(Collectors.toList()).toArray(new String[0]));
        this.subActors = new Array<>();
    }

    public Menu(String[] choices) {
        this.subActors = new Array<>();
        this.MESSAGE_HEIGHT = GraphicsDriver.getHeight() / 8;
        this.choices = choices;
        header = "";
        isPause = true;
        destroyOnExit = false;
        subMenuList = new Array<>();
        subMenuList.add(this);
        cursorTexture = GraphicsDriver.getMasterSheet().createSprite("interface/cursor");
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/monofont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.flip = true;
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.WHITE;
        font = gen.generateFont(parameter);
        gen.dispose();
        MENU_X = GraphicsDriver.getWidth() - 200;
        MENU_Y = GraphicsDriver.getHeight() - MESSAGE_HEIGHT - (24 + choices.length * (int) (GraphicsDriver.getFont().getData().capHeight * GraphicsDriver.getFont().getData().scaleY + PADDING));
    }

    public void addSubMenu(Menu m) {
        subMenuList.add(m);
    }
    
    public void addActor(Actor a) {
        subActors.add(a);
    }

    public String cancel() {
        if (destroyOnExit) {
            removeMenu(this);
        } else {
            cancelMenu();
        }
        return "";
    }

    public void cancelMenu() {
        Menu currentMenu = ((Menu) (GraphicsDriver.getCurrentState()));
        if (currentMenu.menuIndex > 0) {
            currentMenu.menuIndex--;
        }
    }

    public abstract Object confirm(String choice);

    private Object confirmMenu(String choice) {
        ((Menu) (GraphicsDriver.getCurrentState())).menuIndex++;
        Object temp = confirm(choice);
        cursorIndex = 0;
        return temp;
    }

    @Override
    public void dispose() {

    }

    public int getCursorIndex() {
        return cursorIndex;
    }

    public void increaseMenuIndex() {
        menuIndex++;
    }

    public boolean isPause() {
        return isPause;
    }

    public void removeMenu(Menu menu) {
        Menu currentMenu = ((Menu) (GraphicsDriver.getCurrentState()));
        currentMenu.subMenuList.removeValue(menu, true);
        menu.dispose();
        if (currentMenu.subMenuList.size == 0
                && currentMenu.menuIndex
                >= currentMenu.subMenuList.size) {
            GraphicsDriver.removeState(currentMenu);
        } else if (currentMenu.menuIndex > 0) {
            currentMenu.menuIndex--;
        }
    }

    public void render(SpriteBatch batch) {
        State s = null;
        for (State states : GraphicsDriver.getState()) {
            if (!(states instanceof Menu)
                    && !(states instanceof Message)
                    && !(states instanceof Novel)) {
                s = states;
            }
        }
        if (s != null) {
            s.render(batch);
        }
        if (!(subMenuList.size == 0)) {
            subMenuList.get(menuIndex).renderMenu(batch);
        }
    }

    public void renderMenu(Batch batch) {
//        final int PADDING_Y = 10;
        final int MENU_WIDTH = 200;
        final int MENU_HEIGHT = 24 + choices.length * (int) (GraphicsDriver.getFont().getData().capHeight * GraphicsDriver.getFont().getData().scaleY + PADDING);
        final int cursorPadding = 12;
        boolean isOverhead = false;
        State s = null;
        for (State states : GraphicsDriver.getState()) {
            if (!(states instanceof Menu)
                    && !(states instanceof Novel)
                    && !(states instanceof Message)) {
                s = states;
            }
        }
        if (s != null && s instanceof OverheadMap) {
            batch = ((OverheadMap) (s)).getBatch();
            isOverhead = true;
        }
        
        if (isOverhead) {
            batch.begin();
            batch.setProjectionMatrix(GraphicsDriver.getCamera().combined);
        }

        InterfaceDatabase.TEXT_BOX.draw(batch, MENU_X, MENU_Y, MENU_WIDTH, MENU_HEIGHT);
        
        
        for (int i = 0; i < choices.length; i++) {
            GraphicsDriver.drawMessage(batch, font,
                    choices[i],
                    (10 + MENU_X) + GraphicsDriver.getCamera().getScreenPositionX(), 
                    (MENU_Y + (font.getData().capHeight * font.getData().scaleY + PADDING) * (i + 1)) + GraphicsDriver.getCamera().getScreenPositionY());
        }
        batch.draw(cursorTexture,
                (MENU_X - PADDING_X) + GraphicsDriver.getCamera().getScreenPositionX(),
                (MENU_Y + cursorPadding + cursorIndex * (font.getData().capHeight * font.getData().scaleY + PADDING)) + GraphicsDriver.getCamera().getScreenPositionY(),
                    0,
                    0,
                    cursorTexture.getRegionWidth(),
                    cursorTexture.getRegionHeight(),
                    cursorTexture.getScaleX(),
                    cursorTexture.getScaleY(),
                    cursorTexture.getRotation());
        
        InterfaceDatabase.TEXT_BOX.draw(batch, GraphicsDriver.getCamera().getScreenPositionX(), GraphicsDriver.getHeight() - MESSAGE_HEIGHT + GraphicsDriver.getCamera().getScreenPositionY(), GraphicsDriver.getWidth(), MESSAGE_HEIGHT);        
        GraphicsDriver.drawMessage(batch, font,
                header,
                15 + GraphicsDriver.getCamera().getScreenPositionX(),
                (12 + (GraphicsDriver.getHeight() - MESSAGE_HEIGHT) + GraphicsDriver.getCamera().getScreenPositionY()));
        for (Actor a : subActors) {
            a.render(batch);
        }
        if (isOverhead) {
            batch.end();
            batch.setProjectionMatrix(GraphicsDriver.getPlayerCamera().combined);
        }
    }

    public void setChoice(int index) {
        cursorIndex = index;
    }

    public float update(float delta) {
        if (!(subMenuList.size == 0) && menuIndex < subMenuList.size) {
            subMenuList.get(menuIndex).updateMenu(delta);
        }
        if (menuIndex >= subMenuList.size) {
            subMenuList.clear();
            menuIndex = 0;
            if (getCurrentState() instanceof Menu) {
                removeCurrentState();
            }
        }
        return 1;
    }

    public void updateMenu(float delta) {
        if (Input.getKeyPressed(UP_BUTTON)) {
            cursorIndex--;
            if (cursorIndex < 0) {
                cursorIndex = choices.length - 1;
            }
        }
        if (Input.getKeyPressed(DOWN_BUTTON)) {
            cursorIndex++;
            if (cursorIndex >= choices.length) {
                cursorIndex = 0;
            }
        }
        if (Input.getKeyPressed(CONFIRM_BUTTON)) {
            confirmMenu(choices[cursorIndex]);
        }

        if (Input.getKeyPressed(BACK_BUTTON)) {
            cancel();
        }
        if (Input.getKeyPressed(Keys.ESCAPE)) {
            throw new GameOverException();
        }
        for (int i = 0; i < subActors.size; i++) {
            subActors.get(i).update(delta);
            if (subActors.get(i).isFinished()) {
                subActors.removeIndex(i);
                i--;
            }
        }
    }
    
    public void addSubActors(Actor a) {
        subActors.add(a);
    }
    
    public String getMusic() {
        return null;
    }
    
    public String[] getChoices() {
        return choices;
    }
    
    public void setChoices(String[] choices) {
        this.choices = choices;
    }
    
    public void setHeader(String header) {
        this.header = header;
    }
    
    public void reset() {
        cursorIndex = 0;
        menuIndex = 0;
        subMenuList.clear();
        subMenuList.add(this);
    }
}
