Game Concept: The Game is based on the Alien franchise. The spaceship transporting humanity to another planet has been invaded by deadly carnivorous-human flesh eating aliens. 

The spaceship is a 3D space of a cube 3 x 3 which player can go up to 6 directions connected to other rooms.
There are two types of rooms in the spaceship: the control room and basic room. Each basic room has at least 3 connected room or at most 6 connected room (up, down, right, left, front, back). 
The control room has only one connected room, which is AY1 - its back room. Vice versa, the only way to enter the control room is to "go front" from AY1 room
At the start of the game, there will be Aliens distributed among the basic rooms and the player is in the control room. The player has to go to each basic room to check whether there is 
an alien to kill. The person has a gun and some bullets. If bullets runs out, the person has to collect bullets distributed among the rooms to reload. There are some potions in random
rooms as well 

You can only escape one room safely if that room has no aliens or has all dead aliens
You can also escape the room that has alive aliens, but they will deal you loss of health equal to the number of alive aliens in that room
So please remember to "shoot" so that all aliens are dead before leaving the room. In the scenario that you doesnt have any bullets or doesnt have enough bullets to kill all aliens, 
you cannot avoid losing some health after escaping the room
You can regain some health by drinking potion


Tutorial: 
Type "go + direction" to move between the spaceship. There are 6 locations: front, back, right, left, up, down. => go front, go back, go left, go right, go up, go down
Type "quit" to end the game
Type "use" + items to use an item
Type "use bullets" to collect all the bullets in the current room and charge into your gun
Type "use potions" to collect the potions to gain some health
Type "shoot" to spend enough bullets to kill all aliens, or spend all bullets if the total health of alien is more than the number of bullets
Type "repair" to fix rooms that have infiltrating aliens after killing all of them so the ship can fly again. You need not fix rooms without aliens

You will win game if: You find all aliens and kill them all, have at least 1 health, repair all rooms damaged by aliens and return to the control Room before time runs out
You will lose game if: You lose all health or runs out of time or simply quit

Notes: For debugging purposes, here are the locations that I place
Aliens: on 8 corners of the 3x3 cube and one group of alien in the central position of the cube (9 rooms have aliens)
Potions: in 6 center positions of each face of cube's 6 face (6 rooms have potions)
Bullets: on all other locations (not edge and not center of each face of the cube's 6 face) (12 rooms have bullets)
9 + 6 + 12 = 27 = 3 x 3 cube

The game takes approximately 5-15 minutes to finish