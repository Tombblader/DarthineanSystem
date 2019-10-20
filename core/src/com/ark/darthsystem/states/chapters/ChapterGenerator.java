package com.ark.darthsystem.states.chapters;

import com.ark.darthsystem.BattleDriver;
import static com.ark.darthsystem.database.CharacterDatabase.CHARACTER_LIST;
import com.ark.darthsystem.database.Database1;
import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.ActorSprite.SpriteModeFace;
import com.ark.darthsystem.graphics.GameTimer;
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
                switch (element.getName().toLowerCase()) {
                    case "page":
                        createPage(page, novel).run();
                        break;
                    case "printline":
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
                    case "condition":
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
//                                novel.chapters.add(novel.pageIndex + 1, createPage(choice, novel));
                                createPage(choice, novel).run();
                            }
                        }
                        for (XmlReader.Element choice : element.getChildrenByName("variable")) {
                            for (Entry<String, String> e : choice.getAttributes().iterator()) {
                                if (Database1.variables.get(e.key).equals(e.value)) {
                                    createPage(choice, novel).run();
                                }
                            }
                        }
                        for (XmlReader.Element choice : element.getChildrenByName("switch")) {
                            for (Entry<String, String> e : choice.getAttributes().iterator()) {
                                if (Database1.switches.get(e.key).equals(e.value)) {
                                    createPage(choice, novel).run();
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
                        for (Entry<String, String> e : element.getAttributes().iterator()) {
                            Database1.variables.put(e.key, e.value);
                        }
                        break;
                    case "addparty":
                        Database2.player.getAllFieldBattlers().add(CHARACTER_LIST.get(element.getText()));
                        break;
                    case "turnpage":
                        if (element.getText().equalsIgnoreCase("BACK")) {
                            novel.pageIndex -= -2;
                        } else {
                            novel.pageIndex += Integer.parseInt(element.getText());
                        }
                        break;
                    case "setpage":
                        novel.pageIndex = Integer.parseInt(element.getText()) - 1;
                        break;
                    default:
                        break;
                    case "playanimation":
                        Actor a = new Actor(element.getAttribute("name"),
                                (float) Double.parseDouble(element.getAttribute("x")),
                                (float) Double.parseDouble(element.getAttribute("y")),
                                1f / (float) Double.parseDouble(element.getAttribute("fps")),
                                true);
                        GraphicsDriver.
                                getPlayer().
                                getCurrentMap().
                                addActor(a);
                        novel.actorList.add(a);
                        novel.addTimer(new GameTimer(element.getAttribute("name"), 99999999) {
                            @Override
                            public void event(Actor a) {
                            }

                            @Override
                            public boolean isFinished() {
                                return a.isFinished();
                            }
                        });
                        break;
                }
            }
        };
    }
}
