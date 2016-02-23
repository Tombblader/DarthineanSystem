/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States;

import com.ark.darthsystem.GameOverException;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.Graphics.Input;
import static com.ark.darthsystem.Graphics.GraphicsDriver.getCurrentState;
import static com.ark.darthsystem.Graphics.GraphicsDriver.removeCurrentState;
import com.ark.darthsystem.States.chapters.Novel;
import com.badlogic.gdx.Gdx;

import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Keven
 */
public abstract class Menu implements State {

    private final int BACK_BUTTON = Keys.X;
    private String[] choices;

    private final int CONFIRM_BUTTON = Keys.Z;
    private Sprite cursorTexture;
    private int cursorIndex;
    private boolean destroyOnExit;
    private int DOWN_BUTTON = Keys.DOWN;
    private String header;
    private boolean isPause;
    private final int MENU_WIDTH = 200;
    private final int MENU_X = (int) (700);
    private int x;
    private int y;
    private final int MENU_Y = (int) (400);
    private int menuIndex = 0;
    private CopyOnWriteArrayList<Menu> subMenuList;
    private final int UP_BUTTON = Keys.UP;
    private BitmapFont font;
    private final float FONT_SCALE = .25f;

    public Menu(String header,
            String[] choices) {
        this.choices = choices;
        this.header = header;
        isPause = true;
        destroyOnExit = false;
        subMenuList = new CopyOnWriteArrayList<>();
        subMenuList.add(this);
        cursorTexture = GraphicsDriver.getMasterSheet().createSprite("interface/cursor");
        font = new BitmapFont(Gdx.files.internal(
                "fonts/font.fnt"),
                true);
//        font.setColor(Color.BLACK);
        font.getData().scaleX = FONT_SCALE;
        font.getData().scaleY = FONT_SCALE;
    }

    public Menu(String header,
            String[] choices,
            boolean pause,
            boolean mutable) {
        final int MENU_HEIGHT = 10
                + choices.length
                * (int) (GraphicsDriver.
                getFont().getCapHeight());
        this.choices = choices;
        this.header = header;
        isPause = pause;
        subMenuList = new CopyOnWriteArrayList<>();
        destroyOnExit = mutable;
        subMenuList.add(this);
        cursorTexture = GraphicsDriver.getMasterSheet().createSprite("interface/cursor");
        font = new BitmapFont(Gdx.files.internal(
                "fonts/font.fnt"),
                true);
        font.setColor(Color.BLACK);
        font.getData().scaleX = FONT_SCALE;
        font.getData().scaleY = FONT_SCALE;
    }

    public Menu(String[] choices) {
        final int MENU_HEIGHT = 10
                + choices.length
                * (int) (GraphicsDriver.
                getFont().
                getCapHeight());
        this.choices = choices;
        header = "";
        isPause = true;
        destroyOnExit = false;
        subMenuList = new CopyOnWriteArrayList<>();
        subMenuList.add(this);
        cursorTexture = GraphicsDriver.getMasterSheet().createSprite("interface/cursor");
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
        if (currentMenu.menuIndex
                > 0) {
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
        currentMenu.subMenuList.remove(menu);
        menu.dispose();
        if (currentMenu.subMenuList.isEmpty()
                && currentMenu.menuIndex
                >= currentMenu.subMenuList.size()) {
            GraphicsDriver.removeState(currentMenu);
        } else if (currentMenu.menuIndex
                > 0) {
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
        if (!subMenuList.isEmpty()) {
            subMenuList.get(menuIndex).renderMenu(batch);
        }
    }

    public void renderMenu(Batch batch) {
        final int PADDING_X = 20;
        final int PADDING = 3;
//        final int MENU_WIDTH = 200;
        final int MENU_HEIGHT = 10 + choices.length * (int) (GraphicsDriver.getFont().getData().capHeight * GraphicsDriver.getFont().getData().scaleY + PADDING);
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
        }
        float conversion = 1f;
        conversion = GraphicsDriver.getCurrentCamera().getConversion();
        font.getData().scaleX = FONT_SCALE / conversion;
        font.getData().scaleY = FONT_SCALE / conversion;
        for (int i = 0; i < choices.length; i++) {
            GraphicsDriver.drawMessage(batch, font,
                    choices[i],
                    ((5 + MENU_X) / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionX()), 
                    ((MENU_Y + (font.getData().capHeight * font.getData().scaleY * conversion + PADDING)  * (i + 1)) / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionY()));
//            System.out.println(choices[i] + " " + 
//                    ((5 + MENU_X) / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionX()) + " " +
//                    ((MENU_Y + (font.getData().capHeight * font.getData().scaleY * conversion + PADDING)  * (i + 1)) / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionY()));

        }
        batch.draw(cursorTexture,
                ((MENU_X - PADDING_X) / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionX()),
                ((MENU_Y + cursorIndex * (font.getData().capHeight * font.getData().scaleY * conversion + PADDING)) / conversion) + GraphicsDriver.getCurrentCamera().getScreenPositionY(),
                    0,
                    0,
                    cursorTexture.getRegionWidth(),
                    cursorTexture.getRegionHeight(),
                    cursorTexture.getScaleX() / conversion,
                    cursorTexture.getScaleY() / conversion,
                    cursorTexture.getRotation());
        GraphicsDriver.drawMessage(batch, font,
                header,
                15 / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionX(),
                ((HEIGHT - HEIGHT / 4) / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionY()));
//                System.out.println(header + " " +
//                    15 / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionX() + " " +
//                    ((HEIGHT - HEIGHT / 4) / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionY()));
        
//        font.getData().scaleX = FONT_SCALE;
//        font.getData().scaleY = FONT_SCALE;
        if (isOverhead) {
            batch.end();
        }
    }

    public void setChoice(int index) {
        cursorIndex = index;
    }

    public float update(float delta) {
        if (!subMenuList.isEmpty()
                && menuIndex
                < subMenuList.size()) {
            subMenuList.get(menuIndex).
                    updateMenu(delta);
        }
        if (menuIndex
                >= subMenuList.size()) {
            subMenuList.clear();
            menuIndex = 0;
            if (getCurrentState() instanceof Menu) {
                removeCurrentState();
            }
        }
        return 1;
    }

    public void updateMenu(float delta) {
        String choice = "";
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
}
