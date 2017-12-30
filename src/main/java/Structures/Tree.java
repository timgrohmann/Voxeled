package Structures;

import Entities.Block;
import World.World;

import java.util.Random;

public class Tree extends Structure {

    public Tree(World world, int x, int y, int z) {
        super(world,x,y,z);
    }

    @Override
    public void add() {
        int height = (int) (Math.random() * 3 + 4);
        //int height = 5;
        Random r = world.generalPurposeRandom;

        //Stem:
        for (int i = 0; i < height; i++) {
            world.setBlockForCoordinates(Block.Type.WOOD, x, y +i, z);
            //entityArrayList.add(new Block(x,y+i,z, Block.Type.WOOD));
        }

        for (int i = -2; i <=2 ; i++) {
            for (int j = -2; j <= 2; j++) {
                if ((i == -1 || i == 0 || i == 1) && (j == -1 || j == 0 || j == 1)){
                    world.setBlockForCoordinates(Block.Type.LEAVES, this.x + i, this.y + height, this.z + j);
                    //entityArrayList.add(new Block(this.x + i, this.y + height, this.z + j, Block.Type.LEAVES));
                }


                if (i == 0 && j == 0) continue;
                world.setBlockForCoordinates(Block.Type.LEAVES, this.x + i, this.y + height - 2, this.z + j);
                world.setBlockForCoordinates(Block.Type.LEAVES, this.x + i, this.y + height - 1, this.z + j);


            }
        }
    }
}
