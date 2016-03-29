/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States;

import com.ark.darthsystem.Database.InterfaceDatabase;
import com.ark.darthsystem.GameOverException;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.Graphics.Input;
import static com.ark.darthsystem.Graphics.GraphicsDriver.getCurrentState;
import static com.ark.darthsystem.Graphics.GraphicsDriver.removeCurrentState;
import com.ark.darthsystem.States.chapters.Novel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Keven
 */
public abstract class Menu implements State {
    private final int PADDING_X = 20;
    private final int PADDING = 3;

    private final int BACK_BUTTON = Keys.X;
    private String[] choices;
    private final int MESSAGE_HEIGHT = HEIGHT / 6;
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
    private final int UP_BUTTON = Keys.UP;
    private BitmapFont font;

    public Menu(String header, String[] choices) {
        this.choices = choices;
        this.header = header;
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
        MENU_X = WIDTH - 200;
        MENU_Y = HEIGHT - MESSAGE_HEIGHT - (24 + choices.length * (int) (GraphicsDriver.getFont().getData().capHeight * GraphicsDriver.getFont().getData().scaleY + PADDING));
    }

    public Menu(String header, String[] choices, boolean pause, boolean mutable) {
        this.choices = choices;
        this.header = header;
        isPause = pause;
        subMenuList = new Array<>();
        destroyOnExit = mutable;
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
        MENU_X = WIDTH - 200;
        MENU_Y = HEIGHT - MESSAGE_HEIGHT - (24 + choices.length * (int) (GraphicsDriver.getFont().getData().capHeight * GraphicsDriver.getFont().getData().scaleY + PADDING));
    }

    public Menu(String[] choices) {
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
        MENU_X = WIDTH - 200;
        MENU_Y = HEIGHT - MESSAGE_HEIGHT - (24 + choices.length * (int) (GraphicsDriver.getFont().getData().capHeight * GraphicsDriver.getFont().getData().scaleY + PADDING));
    }

    public void addSubMenu(Menu m) {
        subMenuList.add(m);
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
                (MENU_Y + cursorIndex * (font.getData().capHeight * font.getData().scaleY + PADDING)) + GraphicsDriver.getCamera().getScreenPositionY(),
                    0,
                    0,
                    cursorTexture.getRegionWidth(),
                    cursorTexture.getRegionHeight(),
                    cursorTexture.getScaleX(),
                    cursorTexture.getScaleY(),
                    cursorTexture.getRotation());
        
        InterfaceDatabase.TEXT_BOX.draw(batch, GraphicsDriver.getCamera().getScreenPositionX(), HEIGHT - MESSAGE_HEIGHT + GraphicsDriver.getCamera().getScreenPositionY(), WIDTH, MESSAGE_HEIGHT);        
        GraphicsDriver.drawMessage(batch, font,
                header,
                15 + GraphicsDriver.getCamera().getScreenPositionX(),
                (12 + (HEIGHT - MESSAGE_HEIGHT) + GraphicsDriver.getCamera().getScreenPositionY()));
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
    }
    
    public String getMusic() {
        return null;
    }
    
    public void reset() {
        cursorIndex = 0;
        menuIndex = 0;
        subMenuList.clear();
        subMenuList.add(this);
    }
}
