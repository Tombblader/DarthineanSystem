/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States;

import com.ark.darthsystem.Graphics.*;
import com.ark.darthsystem.States.chapters.Novel;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author trankt1
 */
public class Message implements State {

    private final int HEIGHT = GraphicsDriver.getHeight();
    private final int WIDTH = GraphicsDriver.getWidth();
    private String header = "Testing";
    private boolean isPause;
    private final int CONFIRM_BUTTON = Keys.Z;
    private boolean destroyOnExit;
    private ArrayList<String> messages;
    private LinkedList<ArrayList<String>> messageQueue;
    private final int MESSAGE_FONT_SIZE = 12;
    private final float FONT_SCALE = .25f;
    private BitmapFont font;
    private Animation face = null;
    

    public Message(String getMessage) {
        this(new ArrayList<String>() {{this.add(getMessage);}});
//        messageQueue = new LinkedList<>();
//        messages = new ArrayList<>();
//        messages.add(getMessage);
//        messageQueue.add(messages);
//        messages = messageQueue.remove();
//        isPause = true;
//        destroyOnExit = false;
//        font = new BitmapFont(Gdx.files.internal(
//                "com/ark/darthsystem/assets/fonts/font.fnt"),
//                true);
//        font.setColor(Color.BLACK);
//        font.getData().scaleX = FONT_SCALE;
//        font.getData().scaleY = FONT_SCALE;
    }

    public Message(ArrayList<String> getMessage) {
        messageQueue = new LinkedList<>();
        messages = new ArrayList<>(getMessage);
        messageQueue.add(messages);
        messages = messageQueue.remove();
        isPause = true;
        destroyOnExit = false;
        font = new BitmapFont(Gdx.files.internal(
                "fonts/font.fnt"),
                true);
        font.setColor(Color.BLACK);
        font.getData().scaleX = FONT_SCALE;
        font.getData().scaleY = FONT_SCALE;
    }
    
    public Message(ActorBattler header, ArrayList<String> getMessage) {
        this(getMessage);
        this.header = header.getBattler().getName();
        this.face = header.getSprite().getFaceAnimation(ActorSprite.SpriteModeFace.NORMAL);
    }
    
    public Message(String header, Animation face, ArrayList<String> message) {
        this(message);
        this.header = header;
        this.face = face;
    }


    public boolean isPause() {
        return isPause;
    }

    @Override
    public void render(SpriteBatch batch) {
        State ste = null;
        if (GraphicsDriver.getCurrentState() instanceof Message) {
            for (State state : GraphicsDriver.getState()) {
                if (!(state instanceof Message)
                        && !(state instanceof Menu)
                        && !(state instanceof Novel)//&& !(state instanceof Pause)
                        ) {
                    ste = state;
                }
            }
            if (ste != null) {
                ste.render(batch);
            }
            
        }
        renderMessage(batch);
    }
    
    private void renderMessage(Batch batch) {
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

        
        int i = 0;
        float conversion = GraphicsDriver.getCurrentCamera().getConversion();
        font.getData().scaleX = FONT_SCALE / conversion;
        font.getData().scaleY = FONT_SCALE / conversion;
        GraphicsDriver.drawMessage(batch, font, header,
                conversion,
                ((HEIGHT - HEIGHT / 4 - 24) / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionY()));
        for (String message : messages) {
            GraphicsDriver.drawMessage(batch, font,
                message,
                15 / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionX(),
                ((HEIGHT - HEIGHT / 4 + font.getCapHeight() * FONT_SCALE * i) / conversion + GraphicsDriver.getCurrentCamera().getScreenPositionY()));
            i++;
        }

        if (isOverhead) {
            batch.end();
        }
    }

    public float update(float delta) {
        if (Input.getKeyPressed(CONFIRM_BUTTON)) {
            if (messageQueue.isEmpty()) {
                GraphicsDriver.removeState(this);
            }
            else {
                messages = messageQueue.remove();
            }
        }
        return 0;
    }

    public void dispose() {
    }

    public void addMessage(ArrayList<String> message) {
        messageQueue.add(message);
    }

    public void appendMessage(String getMessage) {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(getMessage);
        appendMessage(temp);
    }

    public void appendMessage(ArrayList<String> getMessage) {
        messages.addAll(getMessage);
    }
}
