package fr.neatmonster.nocheatplus.utilities;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import fr.neatmonster.nocheatplus.utilities.ds.CoordMap;

/**
 * Stand-alone BlockCache for setting data by access methods, for testing purposes.
 * @author dev1mc
 *
 */
public class FakeBlockCache extends BlockCache {

    /** Cached type-ids. */
    private final CoordMap<Integer> idMapStored = new CoordMap<Integer>(23);

    /** Cached data values. */
    private final CoordMap<Integer> dataMapStored = new CoordMap<Integer>(23);

    /** Cached shape values. */
    private final CoordMap<double[]> boundsMapStored = new CoordMap<double[]>(23);

    /**
     * Set with data=0 and bounds=full.
     * @param x
     * @param y
     * @param z
     * @param type
     */
    public void set(int x, int y, int z, Material type) {
        set(x, y, z, BlockProperties.getId(type));
    }

    /**
     * Set with data=0-
     * @param x
     * @param y
     * @param z
     * @param type
     * @param bounds
     */
    public void set(int x, int y, int z, Material type, double[] bounds) {
        set(x, y, z, BlockProperties.getId(type), 0, bounds);
    }

    /**
     * Set with data=0 and bounds=full.
     * @param x
     * @param y
     * @param z
     * @param typeId
     */
    public void set(int x, int y, int z, int typeId) {
        set(x, y, z, typeId, 0);
    }

    /**
     * Set with bounds=full.
     * @param x
     * @param y
     * @param z
     * @param typeId
     * @param data
     */
    public void set(int x, int y, int z, int typeId, int data) {
        set(x, y, z, typeId, data, new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.0});
    }

    /**
     * Set custom properties.
     * @param x
     * @param y
     * @param z
     * @param typeId
     * @param data
     * @param bounds
     */
    public void set(int x, int y, int z, int typeId, int data, double[] bounds) {
        idMapStored.put(x, y, z, typeId);
        dataMapStored.put(x, y, z, data);
        if (bounds == null) {
            // TODO: Might store full bounds.
            boundsMapStored.remove(x, y, z);
        } else {
            boundsMapStored.put(x, y, z, bounds);
        }
    }

    public void fill(int x1, int y1, int z1, int x2, int y2, int z2, Material type) {
        fill(x1, y1, z1, x2, y2, z2, BlockProperties.getId(type), 0, new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.0});
    }

    public void fill(int x1, int y1, int z1, int x2, int y2, int z2, int typeId, int data, double[] bounds) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y ++) {
                for (int z = z1; z <= z2; z++) {
                    set(x, y, z, typeId, data, bounds);
                }
            }
        }
    }

    public void walls(int x1, int y1, int z1, int x2, int y2, int z2, Material type) {
        walls(x1, y1, z1, x2, y2, z2, BlockProperties.getId(type), 0, new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.0});
    }

    public void walls(int x1, int y1, int z1, int x2, int y2, int z2, int typeId, int data, double[] bounds) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y ++) {
                for (int z = z1; z <= z2; z++) {
                    if (x == x1 || x == x2 || z == z1 || z == z2) {
                        set(x, y, z, typeId, data, bounds);
                    }
                }
            }
        }
    }

    public void room(int x1, int y1, int z1, int x2, int y2, int z2, Material type) {
        room(x1, y1, z1, x2, y2, z2, BlockProperties.getId(type), 0, new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.0});
    }

    public void room(int x1, int y1, int z1, int x2, int y2, int z2, int typeId, int data, double[] bounds) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y ++) {
                for (int z = z1; z <= z2; z++) {
                    if (x == x1 || x == x2 || z == z1 || z == z2 || y == y1 || y == y2) {
                        set(x, y, z, typeId, data, bounds);
                    }
                }
            }
        }
    }

    @Override
    public void setAccess(World world) {
        // Ignore.
    }

    @Override
    public int fetchTypeId(int x, int y, int z) {
        final Integer id = idMapStored.get(x, y, z);
        if (id == null) {
            return BlockProperties.getId(Material.AIR);
        } else {
            return id;
        }
    }

    @Override
    public int fetchData(int x, int y, int z) {
        final Integer data = dataMapStored.get(x,  y,  z);
        if (data == null) {
            return 0;
        } else {
            return data;
        }
    }

    @Override
    public double[] fetchBounds(int x, int y, int z) {
        final double[] bounds = boundsMapStored.get(x, y, z);
        //return new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.0};
        return bounds;
    }

    @Override
    public boolean standsOnEntity(Entity entity, double minX, double minY,
            double minZ, double maxX, double maxY, double maxZ) {
        // TODO: Consider adding cuboids which mean "ground" if the foot location is inside.
        return false;
    }

}
