/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.chapters;

import com.ark.darthsystem.BattleDriver;
import static com.ark.darthsystem.database.CharacterDatabase.CHARACTER_LIST;
import com.ark.darthsystem.graphics.ActorSprite.SpriteModeFace;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.chapters.Novel.Condition;
import com.ark.darthsystem.states.chapters.Novel.Page;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
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
        FileHandle f = Gdx.files.internal(filename);
        try {
            XmlReader reader = new XmlReader();
            XmlReader.Element results = reader.parse(f);
            Array<XmlReader.Element> pages = results.getChildrenByName("page");

            for (XmlReader.Element page : pages) {
                n.chapters.add(createPage(page, n));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return n;
    }

    private static Page createPage(XmlReader.Element page, Novel n) {
        return (Page) () -> {
            for (int i = 0; i < page.getChildCount(); i++) {
                XmlReader.Element element = page.getChild(i);
                switch (element.getName()) {
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
                        GraphicsDriver.addMenu(n.new Condition(element.getText(), s));
                        break;
                    case "Switch":
                        for (XmlReader.Element choice : element.getChildrenByName("Choice")) {
                            if (choice.getAttribute("name").equalsIgnoreCase(n.choices)) {
                                createPage(choice, n).run();
                            }
                        }
                        break;
                    case "TurnPage":
                        if (element.getText().equalsIgnoreCase("BACK")) {
                            n.pageIndex -= -3;
                        } else {
                            n.pageIndex += Integer.parseInt(element.getText());
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
