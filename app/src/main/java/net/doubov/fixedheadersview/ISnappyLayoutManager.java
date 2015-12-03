package net.doubov.fixedheadersview;

/**
 * An interface that LayoutManagers that should snap to grid should implement.
 */
public interface ISnappyLayoutManager {

    /**
     * @param velocity
     * @return the resultant position from a fling for a single axis
     */
    int getPositionForVelocity(int velocity);

    /**
     * @return the position this list must scroll to to fix a state where the
     * views are not snapped to grid.
     */
    int getFixScrollPos();

}
