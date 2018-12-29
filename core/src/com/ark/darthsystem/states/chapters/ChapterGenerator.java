/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.chapters;

import com.ark.darthsystem.BattleDriver;
import static com.ark.darthsystem.database.CharacterDatabase.CHARACTER_LIST;
import com.ark.darthsystem.database.Database1;
import com.ark.darthsystem.graphics.ActorSprite.SpriteModeFace;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.chapters.Novel.Condition;
import com.ark.darthsystem.states.chapters.Novel.Page;
import com.ark.darthsystem.states.events.Event;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.XmlReader;

/**
 *
 * @author Keven
 */
public class ChapterGenerator {

    public static Novel generateNovel(String filename) {
        Novel n = new Novel() {
            @Override
            public String getMusic() {
                return null;
            }
        };
        try {
            XmlReader reader = new XmlReader();
            XmlReader.Element results = reader.parse(Gdx.files.internal(filename));
            Array<XmlReader.Element> pages = results.getChildrenByName("page");

            for (XmlReader.Element page : pages) {
                n.chapters.add(createPage(page, n));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return n;
    }

    private static Page createPage(XmlReader.Element page, Novel novel) {
        return (Page) () -> {
            for (int i = 0; i < page.getChildCount(); i++) {
                XmlReader.Element element = page.getChild(i);
                switch (element.getName()) {
                    case "page":
                        createPage(page, novel).run();
                        break;
                    case "PrintLine":
                        if (element.hasAttribute("name")) {
                            if (element.hasAttribute("face")) {
                                BattleDriver.printline(CHARACTER_LIST.get(element.getAttribute("name")),
                                        SpriteModeFace.valueOf(element.getAttribute("face").toUpperCase()), element.getText());
                            } else {
                                BattleDriver.printline(CHARACTER_LIST.get(element.getAttribute("name")), element.getText());
                            }
                        } else {
                            BattleDriver.printline(element.getText());
                        }
                        break;
                    case "Condition":
                        Array<XmlReader.Element> choices = element.getChildrenByName("Choice");
                        String[] s = new String[choices.size];
                        for (int j = 0; j < choices.size; j++) {
                            s[j] = choices.get(j).getText();
                        }
                        GraphicsDriver.addMenu(novel.new Condition(element.getText(), s));
                        break;
                    case "if":
                        for (XmlReader.Element choice : element.getChildrenByName("Choice")) {
                            if (choice.getAttribute("name").equalsIgnoreCase(novel.choices)) {
                                novel.chapters.add(novel.pageIndex + 1, createPage(choice, novel));
                            }
                        }
                        for (XmlReader.Element choice : element.getChildrenByName("variable")) {
                            for(Entry<String, String> e : choice.getAttributes().iterator()) {
                                if (Database1.variables.get(e.key).equals(e.value)) {
                                    novel.chapters.add(novel.pageIndex + 1, createPage(choice, novel));
                                }
                            }
                        }
                        break;
                    case "event":
                        try {
                            MapProperties prop = new MapProperties();
                            prop.put("x", 0f);
                            prop.put("y", 0f);
                            prop.put("height", 0f);
                            prop.put("width", 0f);
                            prop.put("parameters", element.getAttribute("parameters"));
                            prop.put("trigger", "auto");
                            ((Event) (Class.forName("com.ark.darthsystem.states.events." + element.getAttribute("name")).newInstance())).createFromMap(prop).run();
                        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "set":
                        for(Entry<String, String> e : element.getAttributes().iterator()) {
                            Database1.variables.put(e.key, e.value);
                        }
                        break;
                    case "TurnPage":
                        if (element.getText().equalsIgnoreCase("BACK")) {
                            novel.pageIndex -= -2;
                        } else {
                            novel.pageIndex += Integer.parseInt(element.getText());
                        }
                        break;
                    case "SetPage":
                        novel.pageIndex = Integer.parseInt(element.getText()) - 1;
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
