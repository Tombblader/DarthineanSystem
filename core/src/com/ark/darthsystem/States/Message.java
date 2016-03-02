/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States;

import com.ark.darthsystem.Database.InterfaceDatabase;
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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 *
 * @author trankt1
 */
public class Message implements State {

    private final int MESSAGE_HEIGHT = HEIGHT / 6;
    private String header = "";
    private boolean isPause;
    private final int CONFIRM_BUTTON = Keys.Z;
    private boolean destroyOnExit;
    private ArrayList<String> currentMessage;
    private Message message;
    private LinkedList<Message> messageQueue;
//    private final int MESSAGE_FONT_SIZE = 12;
//    private final float FONT_SCALE = .25f;
    private BitmapFont font;
    private Animation face = null;
    private float elapsed = 0;
    private final float MESSAGE_SPEED = 30f;
    private int messageIndex = 0;
    

    public Message(String getMessage) {
        this(new ArrayList<String>() {{this.add(getMessage);}});
    }
    
    public Message() {
        
    }

    public Message(ArrayList<String> getMessage) {
        messageQueue = new LinkedList<>();
        message = new Message();
        currentMessage = getMessage;
        messageQueue.add(this);
        message = messageQueue.remove();
        isPause = true;
        destroyOnExit = false;
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/monofont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.flip = true;
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.WHITE;
        font = gen.generateFont(parameter);
        gen.dispose();
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
        final int PADDING_X = 81;
        final int PADDING_Y = 12;
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

        InterfaceDatabase.TEXT_BOX.draw(batch, GraphicsDriver.getCamera().getScreenPositionX(), HEIGHT - MESSAGE_HEIGHT + GraphicsDriver.getCamera().getScreenPositionY(), WIDTH, MESSAGE_HEIGHT);
        
        int i = 0;
        if (message.face != null) {
            batch.draw(message.face.getKeyFrame(GraphicsDriver.getCurrentTime() * 1000), (PADDING_X - 64) / 2, 
                 HEIGHT - MESSAGE_HEIGHT / 2 - 32 + GraphicsDriver.getCamera().getScreenPositionY());
        }
        
        if (!message.header.equals("")) {
            InterfaceDatabase.TEXT_BOX.draw(batch, 
                    1 + GraphicsDriver.getCamera().getScreenPositionX(), 
                    HEIGHT - MESSAGE_HEIGHT - 30 + GraphicsDriver.getCamera().getScreenPositionY(),
                    message.header.length() * 14,
                    font.getCapHeight() + 18);
            GraphicsDriver.drawMessage(batch, font, message.header,
                    10 + GraphicsDriver.getCamera().getScreenPositionX(),
                    ((HEIGHT - MESSAGE_HEIGHT - 24) + GraphicsDriver.getCamera().getScreenPositionY()));
        }
        for (String m : message.currentMessage) {
            GraphicsDriver.drawMessage(batch, font,
                    i == messageIndex ? m.substring(0, (int) (elapsed)) : (i < messageIndex ? m : ""),
                PADDING_X + GraphicsDriver.getCamera().getScreenPositionX() - (message.face == null ? 64 : 0),
                ((PADDING_Y + HEIGHT - MESSAGE_HEIGHT + font.getLineHeight() * font.getScaleY() * i) + GraphicsDriver.getCamera().getScreenPositionY()));
            i++;
        }

        if (isOverhead) {
            batch.end();
            batch.setProjectionMatrix(GraphicsDriver.getPlayerCamera().combined);
        }
    }

    public float update(float delta) {
        if (messageIndex < message.currentMessage.size()) {
            elapsed += delta / 1000f * MESSAGE_SPEED;
        }
        if (message.currentMessage.size() > messageIndex && 
                elapsed >= message.currentMessage.get(messageIndex).length()) {
            elapsed = 0;
            messageIndex++;
        }
        if (Input.getKeyPressed(CONFIRM_BUTTON)) {
            if (messageIndex >= message.currentMessage.size()) {
                if (messageQueue.isEmpty()) {
                    GraphicsDriver.removeState(this);
                }
                else {
                    message = messageQueue.remove();
                    elapsed = 0;
                    messageIndex = 0;
                }
            }
            else {
                messageIndex = message.currentMessage.size();
            }
        }
        return 0;
    }

    public void dispose() {
    }

    public void addMessage(ArrayList<String> message) {
        messageQueue.add(new Message(message));
    }
    
    public void addMessage(Message m) {
        messageQueue.add(m);
    }

    public void appendMessage(String getMessage) {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(getMessage);
        appendMessage(temp);
    }

    public void appendMessage(ArrayList<String> getMessage) {
        message.currentMessage.addAll(getMessage);
    }
    
    public String getMusic() {
        return null;
    }
}
