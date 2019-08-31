<?xml version="1.0" encoding="UTF-8"?>
<tileset name="TileSet" tilewidth="32" tileheight="32" tilecount="64" columns="8">
 <image source="tileset.png" width="256" height="256"/>
 <tile id="19" type="wall">
  <objectgroup draworder="index">
   <object id="3" name="boulder" type="obstacle" x="0" y="0" width="32" height="32">
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="25" type="wall">
  <objectgroup draworder="index">
   <object id="2" name="emptiness" type="wall" x="0" y="0" width="32" height="32"/>
  </objectgroup>
 </tile>
 <tile id="32">
  <animation>
   <frame tileid="32" duration="200"/>
   <frame tileid="33" duration="200"/>
  </animation>
 </tile>
 <tile id="34" type="wall">
  <objectgroup draworder="index">
   <object id="1" name="brick" type="wall" x="0" y="0" width="32" height="32"/>
  </objectgroup>
 </tile>
</tileset>
