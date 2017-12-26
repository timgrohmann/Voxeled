package Structures;

import World.World;

abstract class Structure {
    final int x;
    final int y;
    final int z;

    final World world;

    Structure(World world, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.world = world;
    }

    abstract public void add();
}
