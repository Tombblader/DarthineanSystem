/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.chapters;

import com.ark.darthsystem.graphics.ActorSprite;

/**
 *
 * @author keven
 */
public class Message {
    private String name;
    private ActorSprite.SpriteModeFace face;
    private String message;
    public Message(String name, ActorSprite.SpriteModeFace face, String message) {
        this.name = name;
        this.face = face;
        this.message = message;
    }
}
